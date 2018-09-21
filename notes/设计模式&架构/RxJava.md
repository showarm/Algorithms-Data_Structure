Observable (被观察者)、 Observer (观察者)、 OnSubscribe (订阅事件)
观察者模式，生产者消费者模型。
响应式编程是一种基于异步数据流概念的编程模式。数据流就像一条河：它们是流动的。你可以“过滤”(filter)一条河，你可以“转换”(transform)一条河，你可以将两条河合并(combine)成一个，然后依然畅流如初。最后，它就成了你想要的那条河。
“Be Water，my friend”            --Bruce Lee

https://github.com/jhusain/learnrxjava 
http://rxmarbles.com/#distinct 
1 operators分类：创建／转换／过滤／合并／错误处理／辅助／条件／算术／异步／连接／阻塞／字符串。。。
2 5种调度器Schedulers
Schedulers.io() ／ .computation() ／ .immediate() ／ .newThread() ／ .trampoline()

3 背压
getApps() 
.onBackpressureBuffer() //告诉Observable发射的数据如果比观察者消费的数据要更快的话，它必须把它们存储在缓存中并提供一个合适的时间给它们。
.subscribeOn(Schedulers.io()) 
.observeOn(AndroidSchedulers.mainThread()) 
.subscribe(new Observer<AppInfo>() { [...]

4 
subject是一个神奇的对象，它可以是一个Observable同时也可以是一个Observer.
一旦Subject订阅了Observable，它将会触发Observable开始发射。
RxJava提供四种不同的Subject：
PublishSubject / BehaviorSubject / ReplaySubject / AsyncSubject

```
// 其实是单例使Subject成为一个全局变量。
public enum RxBus {

    INSTANCE;
    private final Subject<Object,Object> bus = new SerializedSubject<>(PublishSubject.create());

    /**
     * 订阅方法
     * @param eventType 事件数据, 只接收符合事件数据类型的事件
     * @param tag 事件类型, 只接收与事件类型相同的事件
     */
    public <T> Observable<T> toObservable(final Class<T> eventType, final String tag) {
        return bus.filter(new Func1<Object, Boolean>() {
            @Override
            public Boolean call(Object o) {
                if (!(o instanceof RxBusObject)) return false;
                RxBusObject ro = (RxBusObject) o;
                return eventType.isInstance(ro.getObj()) && tag != null
                        && tag.equals(ro.getTag());
            }
        }).map(new Func1<Object, T>() {
            @Override
            public T call(Object o) {
                RxBusObject ro = (RxBusObject) o;
                return (T) ro.getObj();
            }
        });
    }

    /**
     * 发送事件,bus是订阅者
     * @param obj
     */
    public void send(Object o, String tag) {
        // 发送带tag标记的事件
        bus.onNext(new RxBusObject(tag, o));
    }

    /**
     * 事件数据, 携带一个Object的数据, 以及String的标志.
     */
    public static class RxBusObject {
        private String tag;
        private Object obj;

        public RxBusObject(String tag, Object obj) {
            this.tag = tag;
            this.obj = obj;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public Object getObj() {
            return obj;
        }

        public void setObj(Object obj) {
            this.obj = obj;
        }

        public static RxBusObject newInstance(String tag, Object obj) {
            return new RxBusObject(tag, obj);
        }
    }
}

```

https://github.com/yuxingxin/RxJava-Essentials-CN https://github.com/hamen/rxjava-essentials 

2013年二月份,Ben Christensen 和 Jafar Husain发在Netflix技术博客的一篇文章第一次向世界展示了RxJava。


## Rx源码解读
（一）基本订阅流程
http://blog.csdn.net/ziwang_/article/details/78618976 
http://leoray.leanote.com/post/RxJava-basic 

```
public class Observable<T> {
    //事件,他也是Observable唯一的成员变量
    private OnSubscribe<T> onSubscribe;
    //构造器就是传入一个OnSubscribe事件
    protected Observable(OnSubscribe<T> onSubscribe) {
        this.onSubscribe = onSubscribe;
    }
    public static <T> Observable<T> create(OnSubscribe<T> onSubscribe) {
        return new Observable<T>(onSubscribe);
    }
    //调用该方法以后,事件才开始执行
    //Subscriber是Observer的一个实现类(但还是抽象的,Observer的三个接口他都没有去实现)
    public Subscription subscribe(Subscriber<T> subscriber) {
        try {
            //其实就只是执行了onSubscribe.call()方法,传入了订阅时的Subscriber,返回了一个Subscription对象,所以call()方法中应该是我们的业务代码,并且在里面合适的时机去调用Subscriber的各个回调方法,如onNext,onCompleted,onError等.
            onSubscribe.call(subscriber);
            //Subscriber同时实现了Subscription接口,所以这里返回的是Subscription类型
            return subscriber;
        } catch (Throwable t) {
            //出错的时候执行onError()
            subscriber.onError(t);
            //并且返回一个默认的Subscription,这个Subscription的状态是已经取消了
            return UNSUBSCRIBED;
        }
    }
```
构造Observable，持有一个OnSubscribe事件，然后subscribe(Subscriber<T> subscriber)的时候，执行事件的Call方法，事件会执行观察者的onNext方法。

onSubscribe.call(subscriber);
然后在call()方法中subscriber.onNext("hello");
这样流动起来
```
Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<String> subscriber) {
                //执行自己的代码
                //...
                //调用观察者
                subscriber.onNext("hello");
                subscriber.onCompleted();
            }
        }).subscribe(new Subscriber<String>() {
            @Override
            public void onNext(String s) {
                Log.d(TAG, s);
            }
            @Override
            public void onError(Throwable t) {
            }
            @Override
            public void onCompleted() {
            }
        });
```
所以View.setOnClickListener()应该是这样的：
Clickable.create(new OnClickListener(){
    public void onClick(View v){
        // ...
        v.doSthAfterClick();

    }
}).perform(new View(){
    public viod doSthAfterClick(){

    }
})

（二）线程切换   todo
http://blog.csdn.net/ziwang_/article/details/78619533
https://www.tuicool.com/articles/buMzEfy 

```
public ObservableObserveOn(ObservableSource<T> source, Scheduler scheduler, boolean delayError, int bufferSize) {
    super(source);
    // observeOn（）指定的
    this.scheduler = scheduler;
    this.delayError = delayError;
    this.bufferSize = bufferSize;
}

@Override
protected void subscribeActual(Observer<? super T> observer) {
    // 如果传入的 scheduler 是 Scheduler.trampoline() 的情况
    // 该线程的意义是传入当前线程，也就是不做任何线程切换操作
    if (scheduler instanceof TrampolineScheduler) {
        source.subscribe(observer);
    } else {
        Scheduler.Worker w = scheduler.createWorker();
        source.subscribe(new ObserveOnObserver<T>(observer, w, delayError, bufferSize));
    }
}
```
Observable#observeOn(Scheduler)，事件执行线程，Observer 的传递是由下往上的，依次触发每行Observable的subscribe()

* 看OperatorSubscribeOn 类
Observable#subscribeOn(Scheduler)，观察者执行线程， 将 Observable#subscribe(Observer) 的执行过程移到了指定线程。Observer 的传递是由下往上的，所以指定的第一行subscribeOn（）才是有效的。

## 理解
https://yarikx.github.io/NotRxJava/ 

解决异步回调地狱问题，（参照高阶函数的各种 & ）

1 函数的返回值是一个函数（匿名内部类的那个方法就是外部方法的返回值）；AsyncJob的抽象也是这个思路，把业务方法当作接口方法的返回值，看AsyncJob<List<Cat>> queryCats(String query)
java高阶函数：返回匿名内部类

2 函数的参数是一个函数（interface Func<T, R>当作参数）

3 把两个参数看成整体，多参变单参。（map与flatMap）

4 最后想要达到的效果是，同步的写法,异步的内核，也就是链式调用。

5 dataflow，流动的数据是有状态的，一个操作导致一种状态，生成一种数据。（queryCats／findCutest／store）

