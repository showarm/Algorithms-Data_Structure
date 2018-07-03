## square/retrofit.git

https://github.com/square/retrofit/wiki/Retrofit-Tutorials 

0 概念 Retrofit is pluggable  

动态代理
```
public <T> T create(final Class<T> service) {
    Utils.validateServiceInterface(service);
    if (validateEagerly) {
      eagerlyValidateMethods(service);
    }
    return (T) Proxy.newProxyInstance(service.getClassLoader(), new Class<?>[] { service },
        new InvocationHandler() {
          private final Platform platform = Platform.get();
          @Override public Object invoke(Object proxy, Method method, Object[] args)
              throws Throwable {
            // If the method is a method from Object then defer to normal invocation.
            if (method.getDeclaringClass() == Object.class) {
              return method.invoke(this, args);
            }
            if (platform.isDefaultMethod(method)) {
              return platform.invokeDefaultMethod(method, service, proxy, args);
            }
            ServiceMethod<Object, Object> serviceMethod =
                (ServiceMethod<Object, Object>) loadServiceMethod(method);
            OkHttpCall<Object> okHttpCall = new OkHttpCall<>(serviceMethod, args);
            return serviceMethod.callAdapter.adapt(okHttpCall); 
          }
        });
  }
```
ServiceMethod记录了：
* Call Adapters ： com.squareup.retrofit2:adapter-rxjava2，以RxJavaCallAdapter为例，把一个Call转换为Observable。Observable<?> observable = Observable.create(func);
* Converters：com.squareup.retrofit2:converter-gson
* 用Call包装了Request，实现类OkHttpCall
* 你定义的service interface中的方法，注解／返回类型／参数

真正的执行是在 RxJavaCallAdapter.adapt(okHttpCall) 

1 Response结果能在UI线程直接使用吗？

```
  Response<T> parseResponse(okhttp3.Response rawResponse) throws IOException {
    ResponseBody rawBody = rawResponse.body();
    // Remove the body's source (the only stateful object) so we can pass the response along.
    rawResponse = rawResponse.newBuilder()
        .body(new NoContentResponseBody(rawBody.contentType(), rawBody.contentLength()))
        .build();
    int code = rawResponse.code();

    if (code < 200 || code >= 300) {／／错误码
      try {
        // Buffer the entire body to avoid future I/O.
        ResponseBody bufferedBody = Utils.buffer(rawBody);
        return Response.error(bufferedBody, rawResponse);
      } finally {
        rawBody.close();
      }
    }
    if (code == 204 || code == 205) {／／成功，但没有响应体
      rawBody.close();
      return Response.success(null, rawResponse);
    }
    ExceptionCatchingRequestBody catchingBody = new ExceptionCatchingRequestBody(rawBody);
    try {
      T body = serviceMethod.toResponse(catchingBody);
      return Response.success(body, rawResponse);／／成功，有响应体
    } catch (RuntimeException e) {
      // If the underlying source threw an exception, propagate that rather than indicating it was
      // a runtime exception.
      catchingBody.throwIfCaught();
      throw e;
    }
  }
```

跟RxJavaCallAdapterFactory并列的默认加入的ExecutorCallAdapterFactory，它的callbackExecutor是MainThreadExecutor，也就是一个handler，用来把响应结果回调给UI线程。


2 来看Retrofit.Builder中的参数：

okhttp3.Call.Factory callFactory：真正处理请求的对象，默认是okhttp3。

HttpUrl baseUrl：请求的根地址。

List<Converter.Factory> converterFactories：解析器，负责解析请求的注解和应答的数据源。

List<CallAdapter.Factory> adapterFactories：执行请求的单元，默认是ExecutorCallAdapterFactory。

Executor callbackExecutor：操作请求的单元，默认是MainThreadExecutor。

boolean validateEagerly：是否提前执行方法参数注解的解析。


3 使用方式

Retrofit.create(final Class<T>service);//

        每个类都是Class的范性

        service必须是接口，不能有继承

@GET("/api/4/news/latest")

    Observable<Object> getLastestNews(Callback callback);//错误写法

   返回void +无Callback参//也是错误写法

Must have return type or Callback as last argument。//要么要么

正确： 

  返回void ＋ 最后一个参数为Callback+ 必须指定泛型

  返回非void ＋ 没有Callback参数

这两种写法都是异步的，会启动一个叫Retrofit-Idle的线程(它底层用了OkhttpThreadPool)，

4 动态代理  

https://mp.weixin.qq.com/s?__biz=MzIxOTI1NTk5Nw==&mid=2650047517&idx=1&sn=85d88950bfda21106c58be536cdfc9cc&chksm=8fde21cfb8a9a8d946c1276f4c6e5374b167eb11168428333213c4164264d840bd38473f9d02&scene=21#wechat_redirect 
因为Proxy.newProxyInstance()的第二个参数，所以你知道为什么service必须是接口。
可以干很多事：无痛埋点。。
