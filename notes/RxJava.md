Observable (可观察者，即被观察者)、 Observer (观察者)、 subscribe (订阅)

http://rxmarbles.com/#distinct 

==Observable的operators分类：创建／变换／过滤／组合／错误处理／辅助／条件／算术／异步／连接／阻塞／字符串。。。

＝＝创建一个Observable:

Observable.create()    /    Observable.from()    /    Observable.just()

just()方法也可以接受列表或数组，就像from()方法，但是它不会迭代列表发射每个值,它将会发射整个列表。

＝＝过滤：filter()／take()／distinct()／

＝＝变换：*map家族／groupBy()／buffer()／

＝＝组合：merge()／zip()



＝＝调度器Schedulers


RxJava提供了5种调度器：

Schedulers.io() ／ .computation() ／ .immediate() ／ .newThread() ／ .trampoline()



getApps() 

.onBackpressureBuffer() //告诉Observable发射的数据如果比观察者消费的数据要更快的话，它必须把它们存储在缓存中并提供一个合适的时间给它们。

.subscribeOn(Schedulers.io()) 

.observeOn(AndroidSchedulers.mainThread()) 

.subscribe(new Observer<AppInfo>() { [...]


==

谨记可观测序列就像一条河：它们是流动的。你可以“过滤”(filter)一条河，你可以“转换”(transform)一条河，你可以将两条河合并(combine)成一个，然后依然畅流如初。最后，它就成了你想要的那条河。

“Be Water，my friend”            --Bruce Lee

https://github.com/yuxingxin/RxJava-Essentials-CN https://github.com/hamen/rxjava-essentials 

响应式编程是一种基于异步数据流概念的编程模式。数据流就像一条河：它可以被观测，被过滤，被操作.

2013年二月份,Ben Christensen 和 Jafar Husain发在Netflix技术博客的一篇文章第一次向世界展示了RxJava。

从纯Java的观点看，RxJava Observable类源自于经典的观察者模式。

四种角色：Observables和Subjects是两个“生产”实体，Observers和Subscribers是两个“消费”实体。

==


Subject = Observable + Observer

subject是一个神奇的对象，它可以是一个Observable同时也可以是一个Observer.

一旦Subject订阅了Observable，它将会触发Observable开始发射。

RxJava提供四种不同的Subject：

PublishSubject / BehaviorSubject / ReplaySubject / AsyncSubject


## Rx源码解读

http://leoray.leanote.com/post/RxJava-basic 

Observable中包含一个OnSubscribe对象

然后，当subscribe(Subscriber<T> subscriber)的时候，onSubscribe.call(subscriber);

然后在call()方法中subscriber.onNext("hello");

这样流动起来


## 理解
https://yarikx.github.io/NotRxJava/ 

解决异步回调地狱问题，（参照高阶函数的各种 & ）

1 函数的返回值是一个函数（匿名内部类的那个方法就是外部方法的返回值）；AsyncJob的抽象也是这个思路，把业务方法当作接口方法的返回值，看AsyncJob<List<Cat>> queryCats(String query)

2 函数的参数是一个函数（interface Func<T, R>当作参数）

3 把两个参数看成整体，多参变单参。（map与flatMap）

4 最后想要达到的效果是，同步的写法,异步的内核，也就是链式调用。

5 dataflow，流动的数据是有状态的，一个操作导致一种状态，生成一种数据。（queryCats／findCutest／store）

