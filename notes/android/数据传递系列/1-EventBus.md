https://github.com/greenrobot/EventBus

https://www.tuicool.com/articles/fEvUzib
https://www.tuicool.com/articles/iYFjYzA
EventBus.getDefault().register(this);
@Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent1(RemindBean bean){
    }
EventBus.getDefault().post(new RemindBean())
注解+反射；各种容器；线程切换；

```
private void postToSubscription(Subscription subscription, Object event, boolean isMainThread) {
        switch (subscription.subscriberMethod.threadMode) {
            case POSTING://一般没定义的，这个就是post在哪个线程，响应就在哪个线程执行
                invokeSubscriber(subscription, event);
                break;
            case MAIN:
                if (isMainThread) {
                    invokeSubscriber(subscription, event);
                } else {
                    mainThreadPoster.enqueue(subscription, event);
                }
                break;
            case BACKGROUND:
                if (isMainThread) {
                    backgroundPoster.enqueue(subscription, event);
                } else {
                    invokeSubscriber(subscription, event);
                }
                break;
            case ASYNC:
                asyncPoster.enqueue(subscription, event);
                break;
            default:
                throw new IllegalStateException("Unknown thread mode: " + subscription.subscriberMethod.threadMode);
        }
    }
```

总之EventBus内部繁复，不建议使用，可以自己实现小型的Bus。Bus是总线的意思。

https://www.tuicool.com/articles/Ubaaiii 
用LiveDataBus替代RxBus、EventBus

