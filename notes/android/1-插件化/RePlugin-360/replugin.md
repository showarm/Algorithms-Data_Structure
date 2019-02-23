

https://github.com/Qihoo360/RePlugin
https://github.com/Jarrywell/RePluginDemo
https://www.jianshu.com/p/d43e1fb426f3


进程：
https://mp.weixin.qq.com/s/OVnYtKOsHJXoDZEqCc55Wg




资源文件：
Error inflating mobilesafe class
   Caused by: android.view.InflateException: Binary XML file line #274: Binary XML file line #274: Error inflating mobilesafe class com.homelink.android.newhouse.house.freehouse.orderlist.view.ComplaintButtonView
   Caused by: android.view.InflateException: Binary XML file line #274: Error inflating mobilesafe class com.homelink.android.newhouse.house.freehouse.orderlist.view.ComplaintButtonView
    at com.ke.loader2.PluginContext.handleCreateView(PluginContext.java:421)
    at com.ke.loader2.PluginContext.access$000(PluginContext.java:54)
    at com.ke.loader2.PluginContext$1.onCreateView(PluginContext.java:80)
    at android.view.LayoutInflater.createViewFromTag(LayoutInflater.java:774)
    at android.view.LayoutInflater.createViewFromTag(LayoutInflater.java:730)
    at android.view.LayoutInflater.rInflate(LayoutInflater.java:863)
    at android.view.LayoutInflater.rInflateChildren(LayoutInflater.java:824)
    at android.view.LayoutInflater.inflate(LayoutInflater.java:515)
    at android.view.LayoutInflater.inflate(LayoutInflater.java:423)
    at android.view.LayoutInflater.inflate(LayoutInflater.java:374)
    at com.homelink.android.newhouse.libcore.base.BaseActivity.setContentView(BaseActivity.java:218)
    at com.homelink.android.newhouse.house.freehouse.orderresult.FreeCarOrderActivity.onCreate(FreeCarOrderActivity.java:141)
    at android.app.Activity.performCreate(Activity.java:7183)
1 这个异常是从源码PluginContext抛出的，看源码
2 class ComplaintButtonView extends android.support.v7.widget.AppCompatImageView 解析失败
改成 class ComplaintButtonView extends ImageView   解析成功


http://www.infoq.com/cn/presentations/the-realization-principle-and-application-of-droidplugin
http://www.infoq.com/cn/news/2015/09/droidplugin-zhangyong-interview
源码阅读整体思路：Demo功能入手，直达源码。

0 PluginApplication

0-0  宿主App启动，hook并模拟android framework 运行环境，宿主App就是framework，插件就是上面对app

PluginHelper.getInstance().applicationOnCreate(getBaseContext());

```

try {
   PluginPatchManager.getInstance().init(baseContext);
   PluginProcessManager.installHook(baseContext);
} catch (Throwable e) {
   Log.e(TAG, "installHook has error", e);
}

try {
   if (PluginProcessManager.isPluginProcess(baseContext)) {
       PluginProcessManager.setHookEnable(true);
   } else {
       PluginProcessManager.setHookEnable(false);
   }
} catch (Throwable e) {
   Log.e(TAG, "setHookEnable has error", e);
}

try {
   PluginManager.getInstance().addServiceConnection(PluginHelper.this);
   PluginManager.getInstance().init(baseContext);
} catch (Throwable e) {
   Log.e(TAG, "installHook has error", e);
}

```

初始化了 PluginPatchManager ：插件管理类，检查插件的activity是否可以启动；

1 ApkFragment

1-0 apk插件安装过程

PluginManager.getInstance().installPackage(item.apkfile, 0);




