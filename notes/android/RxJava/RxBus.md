
https://www.tuicool.com/articles/Y32eI3J
网上现有 RxBus 存有的问题：
使用的 RxBus 大多停留在 RxJava1 版本
RxBus 实现的粘性事件很多都是有问题的
如果事件抛了异常，之后便再也无法接收到的问题
同类型事件需自己再次封装 Bean 进行区别。



  private PublishSubject<RxEvent> bus = PublishSubject.create();
  不注销肯定会有问题，因为在Activity注册的时候new Subscriber匿名内部类，而RxBus是单例的。那么unRegister的方式？











