
### square/okhttp.git: Socket连接

0. 首先明确概念
https://github.com/square/okhttp/wiki/Calls 

OkHttpClient,Request,Call,Response,Interceptor

```
// 异步Post请求：
private void postAsynHttp() {
     mOkHttpClient=new OkHttpClient();
     RequestBody formBody = new FormBody.Builder()
             .add("size", "10")
             .build();
     Request request = new Request.Builder()
             .url("http://api.1-blog.com/biz/bizserver/article/list.do")
             .post(formBody)
             .build();
     Call call = mOkHttpClient.newCall(request);
     // enqueue 异步
     call.enqueue(new Callback() {
         @Override
         public void onFailure(Call call, IOException e) {
         }
         @Override
         public void onResponse(Call call, Response response) throws IOException {
             String str = response.body().string();
             Log.i("wangshu", str);
             runOnUiThread(new Runnable() {
                 @Override
                 public void run() {
                     Toast.makeText(getApplicationContext(), "请求成功", Toast.LENGTH_SHORT).show();
                 }
             });
         }
     });
 }
```

call.enqueue(Callback c)执行了
```
    client.dispatcher().enqueue(new AsyncCall(responseCallback));
```
把AsyncCall扔给了Dispatcher,而AsyncCall是RealCall的内部类，继承了Runnable，封装了Call,Callback,Request.

最后   // 执行一堆Interceptor，得到Response
        Response response = getResponseWithInterceptorChain();

1. Dispatcher  
[Dispatcher](
https://github.com/square/okhttp/blob/c358656c8799d30fd422448153e99a5dd37e298a/okhttp/src/main/java/com/squareup/okhttp/Dispatcher.java
),主要用于控制并发的请求  

```
/** 最大并发请求数*/
private int maxRequests = 64;
/** 每个主机最大请求数*/
private int maxRequestsPerHost = 5;
/** 消费者线程池 */
private ExecutorService executorService;
/** 将要运行的异步请求队列 */
private final Deque<AsyncCall> readyAsyncCalls = new ArrayDeque<>();
/**正在运行的异步请求队列 */
private final Deque<AsyncCall> runningAsyncCalls = new ArrayDeque<>();
/** 正在运行的同步请求队列 */
private final Deque<RealCall> runningSyncCalls = new ArrayDeque<>();

// executorService() 线程池+阻塞队列+线程工厂  
 executorService = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS,new SynchronousQueue<Runnable>(),Util.threadFactory("OkHttp Dispatcher", false));

// 请求数超过64，请求主机数小于5，加入到readyAsyncCalls中进行缓存等待
// runningAsyncCalls，readyAsyncCalls是Deque双端队列，
 synchronized void enqueue(AsyncCall call) {
  if (runningAsyncCalls.size() < maxRequests && runningCallsForHost(call) < maxRequestsPerHost) {
    runningAsyncCalls.add(call);
    executorService().execute(call); //在线程池中执行Runnable
  } else {
    readyAsyncCalls.add(call);
  }
}
```

AsyncCall是Runnable，它的run()方法调用了execute()，启动拦截器链式执行当前Call，然后把下一个Call扔进线程池。
```

    @Override protected void execute() {
      boolean signalledCallback = false;
      try {
          // 执行一堆Interceptor，得到Response
        Response response = getResponseWithInterceptorChain();
        if (retryAndFollowUpInterceptor.isCanceled()) {
          signalledCallback = true;
          responseCallback.onFailure(RealCall.this, new IOException("Canceled"));
        } else {
          signalledCallback = true;
          responseCallback.onResponse(RealCall.this, response);
        }
      } catch (IOException e) {
        if (signalledCallback) {
          // Do not signal the callback twice!
          Platform.get().log(INFO, "Callback failure for " + toLoggableString(), e);
        } else {
          eventListener.callFailed(RealCall.this, e);
          responseCallback.onFailure(RealCall.this, e);
        }
      } finally {
          // 最后，从runningAsyncCalls队列移除，然后取出下一个扔进线程池
        client.dispatcher().finished(this);
      }
    }
```

3 RealCall :自定义的拦截器（可以header,log,https），重试拦截器，缓存拦截器，连接拦截器。。

```
  Response getResponseWithInterceptorChain() throws IOException {
    // Build a full stack of interceptors.
    List<Interceptor> interceptors = new ArrayList<>();
    ／／用户可定义的
    interceptors.addAll(client.interceptors());
    //重试
    interceptors.add(retryAndFollowUpInterceptor);
    interceptors.add(new BridgeInterceptor(client.cookieJar()));
    //缓存
    interceptors.add(new CacheInterceptor(client.internalCache()));
    //http连接
    interceptors.add(new ConnectInterceptor(client));
    //重定向之类
    if (!forWebSocket) {
      interceptors.addAll(client.networkInterceptors());
    }
//the last interceptor所以拦截器链的trick是最后一个拦截器是真正的网络请求
    interceptors.add(new CallServerInterceptor(forWebSocket));
    Interceptor.Chain chain = new RealInterceptorChain(
        interceptors, null, null, null, 0, originalRequest);
    // 拦截器链式执行
    return chain.proceed(originalRequest);
  }
```

4 retryAndFollowUpInterceptor 重试

4 CacheInterceptor 缓存
如果从缓存取了response，怎么中断拦截器链的？

```
    // 不进行网络请求，而且缓存可以使用，直接返回缓存
    // 没有执行chain.proceed()，所以拦截链中断
    if (networkRequest == null) {
      return cacheResponse.newBuilder()
          .cacheResponse(stripBody(cacheResponse))
          .build();
    }

    ...

    networkResponse = chain.proceed(networkRequest);

```
final DiskLruCache cache;

4 CallServerInterceptor，真正的网络请求
httpCodec.createRequestBody(request, request.body().contentLength())
httpCodec.openResponseBody(response)

5 责任链模式

 https://zh.wikipedia.org/wiki/%E8%B4%A3%E4%BB%BB%E9%93%BE%E6%A8%A1%E5%BC%8F 

https://github.com/simple-android-framework/android_design_patterns_analysis/tree/master/chain-of-responsibility/AigeStudio ViewGroup事件传递

每一个处理对象决定它能处理哪些命令对象，它也知道如何将它不能处理的命令对象传递给该链中的下一个处理对象。

这里有个crush：interface Interceptor {

    if (response == null) {
      throw new NullPointerException("interceptor " + interceptor + " returned null");
    }
解决：你自定义的Interceptor，要把异常抛出来 intercept(Chain chain) throws IOException
6 http://www.jianshu.com/p/7b29b89cd7b5 

7 OKio

8 https://publicobject.com/2016/07/03/the-last-httpurlconnection/ 作者之一 比较HttpURLConnection

9 应用

http://www.tuicool.com/articles/3eiM3aI  利用 OkHttp Interceptor 模拟数据

10 封装库

11 复用连接池  
StreamAllocation.java

12 DNS模块

1、基本原理：
OkHttp支持设置自定义dns，重写lookUp方法，返回正确的ip地址列表。

public List<InetAddress> lookup(String hostname) throws UnknownHostException
dns服务商有本地dns服务（默认服务）和网络dns服务（补充服务，如百度dns）。

本地dns服务可能会被运营商劫持导致返回错误的ip，我们需要把本地dns服务和网络dns服务做一个merge，以尽可能的保证服务的稳定性。

2、网络dns
网络dns，如百度dns会返回域名对应的ip列表，http://180.76.76.112/?dn=www.baidu.com

因为lookUp方法会被频繁调用，为了防止频繁访问网络，需要加一层数据缓存。

3、数据merge
数据合法性校验：网络dns服务一般返回一个字符串，所以需要对数据合法性做校验，判断是否是IPV4&IPV6地址。

参考 InetAddressValidator.java 进行数据合法性校验。

生成正确的InetAddress：网络在Https协议下必须通过域名访问，在生成InetAddress的时候必须包含域名。

InetAddress addressWithoutHost = InetAddress.getByName(ip);
if (addressWithoutHost != null) {
    //非常重要：为InetAddress添加host
 InetAddress address = InetAddress.getByAddress(host,
 addressWithoutHost.getAddress());
}
数据merge：将本地dns服务返回的数据与网络dns服务返回的数据进行去重。

4、路由选择
由于OkHttp具备路由选择的功能，所以DNS服务没必要花太多精力到路由选择上，只需做好本职工作，返回正确的ip列表即可，OkHttp会挨个试直到找到可用的ip。

OkHttp通过RouteSelector&RouteDatabase进行路由选择，总共干3件事情：1.收集所有可用的路由，2.依次选择可用的路由，3.维护连接失败的路由。

如果有连接成功的路由，则下次复用该路由，如果有连接失败的路由，则加入RouteDatabase中。

5、手机连接代理
手机连接代理时，lookUp方法的hostName是一个ip地址，因为HtppDns的作用是域名解析，所以当域名为ip的时候，无需做处理，使用本地dns即可。

 

