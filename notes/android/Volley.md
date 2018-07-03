##### Http、TCP、UDP

TCP/IP是个协议组，可分为四个层次：网络接口层、网络层、传输层和应用层。
网络层有IP协议，传输层中有TCP协议与UDP协议，应用层有FTP、HTTP DNS
socket只是一种连接模式，面向的两种协议，基于流的TCP，基于用户数据包的UDP


======

Volley    生产者消费者

NetworkDispatcher/ExecutorDelivery





Volley 源码解析

http://a.codekk.com/detail/Android/grumoon/Volley%20%E6%BA%90%E7%A0%81%E8%A7%A3%E6%9E%90

http://blog.csdn.net/column/details/yuan-android-net.html

https://android.googlesource.com/platform/frameworks/volley/

https://github.com/mcxiaoke/android-volley

http://www.jianshu.com/p/9e17727f31a1 

1 整个流程

Volley中的线程交互

你知道或者被告知，request是新线程异步的，onResponse是UI线程的，但是为什么呢？来扒一扒。

涉及到Volley中的这几个实现类：

Volley	入口类

RequestQueue	

NetworkDispatcher extends Thread

ExecutorDelivery implements ResponseDelivery

然后一个Request实现，比如StringRequest

00 

 newRequestQueue();

 queue.add(request);		//使用者往队列加

Volley

01  queue.start();

RequestQueue

02  

记录一个UI线程的Handler

    public RequestQueue(Cache cache, Network network, int threadPoolSize) {

        this(cache, network, threadPoolSize,

                new ExecutorDelivery(new Handler(Looper.getMainLooper())));

    }

4个NetworkDispatcher,启了4个线程，不停的轮训请求队列

for (int i = 0; i < mDispatchers.length; i++) {

            NetworkDispatcher networkDispatcher = new NetworkDispatcher(mNetworkQueue, mNetwork,

                    mCache, mDelivery);

            mDispatchers[i] = networkDispatcher;

            networkDispatcher.start();

        }

NetworkDispatcher extends Thread

03  不断从队列里取request，执行，分发

while (true) {

 request = mQueue.take();			//从队列取

 NetworkResponse networkResponse = mNetwork.performRequest(request);

 mDelivery.postResponse(request, response);

}

ExecutorDelivery implements ResponseDelivery

04  通过那个handler把response传递给UI线程

mResponsePoster = new Executor() {

            @Override

            public void execute(Runnable command) {

                handler.post(command);

            }

        };

mResponsePoster.execute(new ResponseDeliveryRunnable(request, response, runnable));

mRequest.deliverResponse(mResponse.result);

然后一个Request实现，比如StringRequest

05

@Override

    protected void deliverResponse(String response) {

        mListener.onResponse(response);

    }

Volley.newRequestQueue(SportsApp.getContext());发生了什么？

尼玛，线程池＋while(true)。好意思问！

你为什么敢在onResponse(BaseParser response)里更新UI？

尼玛，万恶的handler，万恶的looper

2 Volley与HttpUrlConnection什么关系？

Volley.java

new newRequestQueue()的时候

if (Build.VERSION.SDK_INT >= 9) {

                stack = new HurlStack();

            } else {

                // Prior to Gingerbread, HttpUrlConnection was unreliable.

                // See: http://android-developers.blogspot.com/2011/09/androids-http-clients.html

                stack = new HttpClientStack(AndroidHttpClient.newInstance(userAgent));

            }

3 出错重试机制  DefaultRetryPolicy

基于连接时间的

int timeoutMs = request.getTimeoutMs();

        connection.setConnectTimeout(timeoutMs);

        connection.setReadTimeout(timeoutMs);

4 缓存机制 CacheDispatcher

final Request<?> request = mCacheQueue.take();//把之前的请求存了

6 异常机制

VolleyError

https://github.com/mcxiaoke/android-volley/blob/master/src/main/java/com/android/volley/VolleyError.java

https://github.com/mcxiaoke/android-volley/blob/master/src/main/java/com/android/volley/NetworkError.java

7  请求优先机制

AtomicInteger mSequenceGenerator = new AtomicInteger();

request.setSequence(getSequenceNumber());

Request<T> implements Comparable<Request<T>>

先比较enum Priority

相同再比较Sequence

8 debugging / log

mEventLog.add(tag, Thread.currentThread().getId());

9 用到的结构

PriorityBlockingQueue<Request<?>> mCacheQueue

PriorityBlockingQueue<Request<?>> mNetworkQueue//使用者add进来的request

5

Volleye的线程机制是 ： 4个线程，不停的轮训请求队列

AsyncTask    内部是:3.0前是一个ThreadPoolExecutor，线程数跟CPU有关。3.0后是一个worker线程

ThreadPoolExcutor

java.util.concurrent的概念

Runnable   一个可执行单元

Executor 	可执行单元的执行器

https://github.com/mcxiaoke/android-volley/blob/master/src/main/java/com/android/volley/ExecutorDelivery.java

Volley http://www.jianshu.com/p/9e17727f31a1 

RequestQueue
用来管理各种请求队列，其中包含有4个队列
a) 所有请求集合，通过RequestQueue.add()添加的Request都会被添加进来，当请求结束之后删除。
b) 所有等待Request，这是Volley做的一点优化，想象一下，我们同时发出了三个一模一样的Request，此时底层其实不必真正走三个网络请求，而只需要走一个请求即可。所以Request1被add之后会被调度执行，而Request2 和Request3被加进来时，如果Request1还未执行完毕，那么Request2和 Request3只需要等着Request1的结果即可。
c) 缓存队列，其中的Request需要执行查找缓存的工作
d) 网络工作队列 其中的Request需要被执行网络请求的工作

NetworkDispatcher
执行网络Request的线程，它会从网络工作队列中取出一个请求，并执行。Volley默认有四个线程作为执行网络请求的线程。

CacheDispatcher
执行Cache查找的线程，它会从缓存队列中取出一个请求，然后查找该请求的本地缓存。Volley只有一个线程执行Cache任务。

内存缓存：缓存队列里是请求，缓存里是实体，实体是否超时决定是否重新请求。

ResponseDelivery
请求数据分发器，可以发布Request执行的结果。





