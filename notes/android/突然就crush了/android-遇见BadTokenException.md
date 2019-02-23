

https://www.fabric.io/lianjia2/android/apps/com.lianjia.beike/issues/5be2e4f0f8b88c2963ead073?time=last-twenty-four-hours&selectedCrashInsightsMatcherTag=unable-to-add-window-token-not-valid
Activity在或已经finish的时候(onDestory)，show一个Dialog，BadTokenException

https://www.fabric.io/lianjia2/android/apps/com.lianjia.beike/issues/5be6c3f4f8b88c296355ac50?time=last-twenty-four-hours&selectedCrashInsightsMatcherTag=dialog-dismiss-view-not-attached-to-window-manager
Activity在或已经finish的时候(onDestory)，dismiss一个Dialog，IllegalArgumentException: View=DecorView@ab16bab[] not attached to window manager

https://www.fabric.io/lianjia2/android/apps/com.lianjia.beike/issues/5b15f4906007d59fcdeaeb6e?time=last-twenty-four-hours&selectedCrashInsightsMatcherTag=cannot-perform-action-after-onsaveinstancestate
IllegalStateException: Can not perform this action after onSaveInstanceState
Activity在onSaveInstanceState之后（其实是onStop后），不能立即执行FragmentManager操作，如commit，popBackStack等。onDestory后也报IllegalStateException。


### 吃着火锅，唱着歌，突然就crush了

子线程任务结束，通过Handler在Activity中弹一个对话框，这本无可厚非。但是，弹框之前，用户点了back键，使当前Activity处于正在Destroy的状态，就会出现：

```
Fatal Exception: android.view.WindowManager$BadTokenException: Unable to add window -- token android.os.BinderProxy@3e081b68 is not valid; is your activity running?
       at android.view.ViewRootImpl.setView(ViewRootImpl.java:578)
       at android.view.WindowManagerGlobal.addView(WindowManagerGlobal.java:271)
       at android.view.WindowManagerImpl.addView(WindowManagerImpl.java:74)
       at android.app.Dialog.show(Dialog.java:298)
       at com.homelink.android.newhouse.house.detail.agent.NewHouseAgentListAdapter$2.onResponse(NewHouseAgentListAdapter.java:268)
       
```

### 从异常log打印栈读懂Android源码

BadToken，令牌这个概念在android的系统service中有经常使用到，其本质是Binder，利用Binder对象无论跨多少进程都会指向同一个对象，android系统中将其作为一种安全机制。

在启动Activity的流程当中，
        首先，ActivityManagerService会创建ActivityRecord，在ActivityRecord的构造器中创建一个IApplication（本质上就是一个Binder），它就是appToken；
        然后，AMS会把Binder（appToken）传给WMS；当AMS这边完成数据结构的添加之后，会返回给ActivityThread一个ActivityClientRecord数据结构，中间就包含了Binder对象；ActivityThread这边拿到这个Binder对象之后，就需要让WMS去在界面上添加一个对应窗口，在添加窗口传给WMS的数据中WindowManager.LayoutParams这里面就包含了Binder；
        最终WMS在添加窗口的时候，就需要将这个Binder和之前AMS保存在里面的Binder做比较，对得上说明是合法的，否则，就会抛出BadTokenException这个异常。（这里插一句，如果client端调用server端，server端出了异常，传回给client端的Parcel是带有异常信息的）从上面一段描述中可以看出，Binder在系统中确实是起到一种信息验证的作用，能够起到一种令牌的作用。

也就是说，Activity和Window是两码事，Activity持有一个Window，Activity有生命，Window复杂装载View，那WMS在展示Window时就需要知道它对应的Activity是否还活着，这个判断就是基于appToken这个Binder的。

show Dialog的时候需要一个Context，这个Context需要有token。一般都是Activity作为Dialog的Context，Activity是有token的。然而在上述情况下，系统检测到Activity处于正在Destroy的状态，认为它无法完成show Dialog的任务，此刻对Dialog来讲，它是一个BadToken，所以抛出了BadTokenException。

另外一种情况：Dialog使用Application作为context为什么会报错？
因为activity这类context是带有Token信息的，而Application没有，所以在WMS中会得到区别对待。Dialog窗口没有token，其目的就是依附于Activity的窗口，所以需要其token。
```
android.view.WindowManager$BadTokenException: Unable to add window -- token null is not for an application
at android.view.ViewRoot.setView(ViewRoot.java:509)
```

源码中抛这个异常的地方:

```
//ViewRootImpl.java
public void setView(View view, WindowManager.LayoutParams attrs, View panelParentView) {
        res = mWindowSession.addToDisplay(mWindow, mSeq, mWindowAttributes,
            getHostVisibility(), mDisplay.getDisplayId(),
            mAttachInfo.mContentInsets, mAttachInfo.mStableInsets,
            mAttachInfo.mOutsets, mInputChannel);

        if (res < WindowManagerGlobal.ADD_OKAY) {
            switch (res) {
                case WindowManagerGlobal.ADD_BAD_APP_TOKEN:
                case WindowManagerGlobal.ADD_BAD_SUBWINDOW_TOKEN:
                    throw new WindowManager.BadTokenException(  
                            "Unable to add window -- token " + attrs.token
                            + " is not valid; is your activity running?");
        
                case WindowManagerGlobal.ADD_NOT_APP_TOKEN:
                        throw new WindowManager.BadTokenException(
                                "Unable to add window -- token " + attrs.token
                                + " is not for an application");
```


### 老死不相往来
解决方案很简单：

```
if (!isFinishing()) {
        showDialog(MY_DIALOG);
        }
```

### 备注
1 在sdk26的源码中，Google在WindowManagerGlobal中把这个异常给try catch了。有日志，不crush。
2 token 是Binder，可以跨进程使用，也是一个window的标识。


https://stackoverflow.com/questions/7811993/error-binderproxy45d459c0-is-not-valid-is-your-activity-running

http://dimitar.me/android-displaying-dialogs-from-background-threads/ 
http://blog.desmondyao.com/android-bad-window-token/ 

https://zhuanlan.zhihu.com/p/20376790
https://zhuanlan.zhihu.com/p/20377272
http://blog.csdn.net/u010375364/article/details/51866330