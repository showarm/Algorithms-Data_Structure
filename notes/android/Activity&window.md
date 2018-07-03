其实关键是AMS,WMS，这些service都是系统进程，你的app是另一个进程

https://zhuanlan.zhihu.com/p/20376790
https://zhuanlan.zhihu.com/p/20377272
http://blog.csdn.net/u010375364/article/details/51866330
Dialog使用Application作为context为什么会报错？
因为activity这类context是带有Token信息的，而Application没有，所以在WMS中会得到区别对待。Dialog窗口没有token，其目的就是依附于Activity的窗口，所以需要其token。
```
android.view.WindowManager$BadTokenException: Unable to add window -- token null is not for an application
at android.view.ViewRoot.setView(ViewRoot.java:509)
```
坏令牌异常，令牌这个概念在android的系统service中有经常使用到，其本质是Binder，利用Binder对象无论跨多少进程都会指向同一个对象，android系统中将其作为一种安全机制。

AMS创建ActivityRecord（产生token），传给ActivityThread，让WMS去在界面上添加一个对应窗口，传给WMS的数据中WindowManager.LayoutParams这里面就包含了Binder

举个简单的例子：在启动Activity的流程当中，首先，ActivityManagerService会创建ActivityRecord由其本身来管理，同时会为这个ActivityRecord创建一个IApplication（本质上就是一个Binder）；然后，由于AMS和WMS都是有SystemServer起的，AMS调用WMS提供的接口，让WMS记录下这个Binder；当AMS这边完成数据结构的添加之后，会返回给ActivityThread一个ActivityClientRecord数据结构，中间就包含了Binder对象；ActivityThread这边拿到这个Binder对象之后，就需要让WMS去在界面上添加一个对应窗口，在添加窗口传给WMS的数据中WindowManager.LayoutParams这里面就包含了Binder；最终WMS在添加窗口的时候，就需要将这个Binder和之前AMS保存在里面的Binder做比较，对得上说明是合法的，否则，就会抛出BadTokenException这个异常。（这里插一句，如果client端调用server端，server端出了异常，传回给client端的Parcel是带有异常信息的）从上面一段描述中可以看出，Binder在系统中确实是起到一种信息验证的作用，能够起到一种令牌的作用。

### Activity路由
https://www.tuicool.com/articles/Ab6Ffij 
其实Activity注册的<Intent-filter>可以在Activity头上加注解的方式替换，

## Fragment
https://juejin.im/entry/56a87b2b2e958a0051906227 Activity 与 Fragment 通信