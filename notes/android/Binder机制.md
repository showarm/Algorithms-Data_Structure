
###  AIDL
简化的Binder
https://github.com/YoungPeanut/Android_BinderDemo

1 class Stub extends android.os.Binder implements cs.shawn.binderdemo.aidl.IRemoteService 存根Stub是IRemoteService的内部类，它既是IRemoteService又是Binder
2 C/S模型 Activity和Service在两个进程中， aidl是二者交互的桥梁，对activity来说它是interface，对service来说它是IBinder。 bind方式启动服务，onBind(Intent intent)返回IBinder，onServiceConnected()得到后转为interface。 关键是aidl的Stub既是IRemoteService又是Binder。


### Binder机制 
http://www.tuicool.com/articles/QNZRjur 

在启动Activity的流程当中:
1，ActivityManagerService会创建ActivityRecord由其本身来管理，同时会为这个ActivityRecord创建一个IApplication（本质上就是一个Binder）；
2，由于AMS和WMS都是有SystemServer起的，AMS调用WMS提供的接口，让WMS记录下这个Binder；当AMS这边完成数据结构的添加之后，会返回给ActivityThread一个ActivityClientRecord数据结构，中间就包含了Binder对象；
3, ActivityThread这边拿到这个Binder对象之后，就需要让WMS去在界面上添加一个对应窗口，在添加窗口传给WMS的数据中WindowManager.LayoutParams这里面就包含了Binder；最终WMS在添加窗口的时候，就需要将这个Binder和之前AMS保存在里面的Binder做比较，对得上说明是合法的，否则，就会抛出BadTokenException这个异常。（这里插一句，如果client端调用server端，server端出了异常，传回给client端的Parcel是带有异常信息的）从上面一段描述中可以看出，Binder在系统中确实是起到一种信息验证的作用，能够起到一种令牌的作用。

Binder通信采用C/S架构。

Binder是虚拟驱动设备，主要完成以下几件事：

初始化(binder_init)：创建/dev/binder设备节点

打开 (binder_open)：获取Binder Driver的文件描述

映射(binder_mmap)：内核中分配内存，用于存放数据数据

（将同一块物理内存分别映射到内核虚拟地址空间和用户虚拟内存空间，从而实现了用户空间的Buffer和内核空间的Buffer同步操作）

操作(binder_ioctl)：将IPC数据作为参数传给Binder Driver

