
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

