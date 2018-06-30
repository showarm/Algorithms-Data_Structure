## 一 RxJava波及到的概念
* 响应式编程
* 函数式编程
* 观察者模式
* 责任链模型
* 事件驱动
* 异步操作
* 链式调用
* ReactiveX：Reactive Extensions，即Rx = Observables（异步数据流） + 操作符 + Schedulers（并发处理）

问题：在没有接触RxJava之前，你怎么理解这些概念？反过来，有了这些概念，你怎么理解RxJava？

### 二 RxJava范畴内的概念
* 元素
Observable (被观察者) 和 Observer (观察者)通过 subscribe() 方法实现订阅关系，Observable分发OnSubscribe(事件)给Observer消费。

* 基本操作
operators分类：创建／转换／过滤／合并／错误处理／辅助／条件／算术／异步／连接／阻塞／字符串。。。

http://rxmarbles.com/#map

* 调度器  
Schedulers.immediate()：默认的，直接在当前线程运行，相当于不指定线程。
Schedulers.newThread()：总是启用新线程，并在新线程执行操作。
Schedulers.io()：I/O 操作（读写文件、读写数据库、网络信息交互等）所使用的 Scheduler。行为模式和 newThread() 差不多，区别在于 io() 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率。
Schedulers.computation()：计算所使用的 Scheduler，例如图形的计算。这个 Scheduler 使用的固定的线程池，大小为 CPU 核数。不要把 I/O 操作放在 computation() 中，否则 I/O 操作的等待时间会浪费 CPU。
Schedulers.trampoline()：当我们想在当前线程执行一个任务时，并不是立即时，可以用.trampoline()将它入队。这个调度器将会处理它的队列并且按序运行队列中每一个任务。
AndroidSchedulers.mainThread()：RxAndroid库提供的Scheduler，它指定的操作在主线程中运行。

* 基本使用
https://github.com/ReactiveX/RxJava/tree/1.x   
https://github.com/ReactiveX/RxAndroid/tree/1.x  

```
dependencies {
    ...
    compile 'io.reactivex:rxjava:1.1.6'
    compile 'io.reactivex:rxandroid:1.2.1'
}

1 创建 Observable(被观察者)
2 创建Observer（观察者）
3 Subscribe (订阅)

Observable.create(new Observable.OnSubscribe<String>() {
        @Override
        public void call(Subscriber<? super String> subscriber) {
            subscriber.onNext("安静");
            subscriber.onCompleted();
        }
    })

.map(new Func1<String,Integer>() {
                    @Override
                    public Integer call(String s) {
                        //do String --> Integer
                        return 666;
                    }
                }

.subscribeOn(Schedulers.io())
.observeOn(AndroidSchedulers.mainThread())

.subscribe(new Subscriber<Integer>() {
    // 不会再有新的 onNext
    @Override
    public void onCompleted() {
        Log.i("tnt","我在用TNT");
    }

    //事件队列异常，队列自动终止
    @Override
    public void onError(Throwable e) {
        Log.i("tnt","TNT炸了");
    }

    //处理事件队列中的事件
    @Override
    public void onNext(Integer s) {
        Log.i("tnt",s+"");
    }

    //准备工作
    @Override
    public void onStart() {
        Log.i("tnt","革命一代");
    }
});

```

所以，RxJava是什么，解决了什么：
RxJava是ReactiveX的一种java实现。
基于观察者模式，事件流从上往下，从订阅源传递到观察者。流(Stream)是按时间排列的 Events 序列,分三种不同的 Events：Value、Error、Completed Signal。对应三种事件处理函数。
链式调用的写法，执行异步操作。
高阶函数（Java匿名内部类）解决回调问题。

(个人疑惑：链式调用与Builder模式)
### 四 应用

* Rx实现EventBus

```

public enum RxBus {

    INSTANCE;

    /**
     * Subject 既是订阅者，也是被观察者
     */
    private final Subject<Object,Object> bus = new SerializedSubject<>(PublishSubject.create());

    /**
     * 获取Observable以便订阅
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

    public void send(Object o, String tag) {
        // 发送带tag标记的事件
        bus.onNext(new RxBusObject(tag, o));
    }
    
    public boolean hasObservers() {
        return bus.hasObservers();
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

* RxActivityResult
PublishSubject + Fragment代理
https://blog.csdn.net/heiseyouguan/article/details/74380082


不可见fragment的应用场景

###五 源码&原理&理解

1 元素间引用关系  
Observable构造时持有OnSubscribe对象；
Observable的subscribe(Subscriber subscriber)方法调用onSubscribe.call(subscriber)；
OnSubscribe的call(Subscriber subscriber)调用subscriber.onNext(args);

2 添加操作符：map(Transformer<? super T, ? extends R> transformer)为例  

map操作符的作用是将T类型的Event转化成R类型

每调用一次操作符的方法，就相当于在上层数据源和下层观察者之间桥接了一个新的Observable（其实还有新的OnSubscribe，新的Subscriber）。

新Observable构造时持有新OnSubscribe对象；
新Observable的subscribe(Subscriber subscriber)方法调用 新onSubscribe.call(subscriber)；
新OnSubscribe的call(Subscriber subscriber)调用

Observable的subscribe(Subscriber subscriber)方法调用onSubscribe.call(subscriber)；
OnSubscribe的call(Subscriber subscriber)调用subscriber.onNext(args);
subscriber.onNext(args)调用transformer.call(var1)；

RxJava中对链式调用中：
subscribe（）之前对每一行都是一个新的Observable；
这些Observable的subscribe(Subscriber subscriber)方法逆序执行，就是先执行最下面的；
这些onSubscribe.call(subscriber)也是。
而这些subscriber.onNext(args)是顺序执行的。

逆序执行得到了顺序结果。

3 subscribeOn() observeOn()   

由2的结论和下面伪源码：
subscribeOn()影响它上面的代码，连续多次调用只有第一个subscribeOn() 起作用。
最终Subscriber的执行线程与最后一次observeOn()的调用有关。


```

public class Observable<T> {
    final OnSubscribe<T> onSubscribe;

    private Observable(OnSubscribe<T> onSubscribe) {
        this.onSubscribe = onSubscribe;
    }

    public static <T> Observable<T> create(OnSubscribe<T> onSubscribe) {
        return new Observable<T>(onSubscribe);
    }

    public void subscribe(Subscriber<? super T> subscriber) {
        subscriber.onStart();
        onSubscribe.call(subscriber);
    }

   public <R> Observable<R> map(Transformer<? super T, ? extends R> transformer) {
       return create(new OnSubscribe<R>() { // 生成一个桥接的Observable和 OnSubscribe
           @Override
           public void call(Subscriber<? super R> subscriber) {
               Observable.this.subscribe(new Subscriber<T>() { // 订阅上层的Observable
                   @Override
                   public void onCompleted() {
                       subscriber.onCompleted();
                   }

                   @Override
                   public void onError(Throwable t) {
                       subscriber.onError(t);
                   }

                   @Override
                   public void onNext(T var1) {
                       // 将上层的onSubscribe发送过来的Event，通过转换和处理，转发给目标的subscriber
                       subscriber.onNext(transformer.call(var1));
                   }
               });
           }
       });
   }

    public Observable<T> subscribeOn(Scheduler scheduler) {
        return Observable.create(new OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                subscriber.onStart();
                // 将事件生产切换到新的线程
                scheduler.createWorker().schedule(new Runnable() {
                    @Override
                    public void run() {
                        Observable.this.onSubscribe.call(subscriber);
                    }
                });
            }
        });
    }

    public Observable<T> observeOn(Scheduler scheduler) {
        return Observable.create(new OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                subscriber.onStart();
                Scheduler.Worker worker = scheduler.createWorker();
                Observable.this.onSubscribe.call(new Subscriber<T>() {
                    @Override
                    public void onCompleted() {
                        worker.schedule(new Runnable() {
                            @Override
                            public void run() {
                                subscriber.onCompleted();
                            }
                        });
                    }

                    @Override
                    public void onError(Throwable t) {
                        worker.schedule(new Runnable() {
                            @Override
                            public void run() {
                                subscriber.onError(t);
                            }
                        });
                    }

                    @Override
                    public void onNext(T var1) {
                        worker.schedule(new Runnable() {
                            @Override
                            public void run() {
                                subscriber.onNext(var1);
                            }
                        });
                    }
                });
            }
        });
    }

    public interface OnSubscribe<T> {
        void call(Subscriber<? super T> subscriber);
    }

    public interface Transformer<T, R> {
        R call(T from);
    }
}

```

###六 RxJava2

######背压

###