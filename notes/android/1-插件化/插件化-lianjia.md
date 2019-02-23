
# lianjia_plugin_demo  plugin_framework

com.lianjia.plugin.LJPluginApplication //主工程继承
com.lianjia.loader2.PMF
com.lianjia.loader2.PmBase // 两个管理类
com.lianjia.loader2.PluginContainers // 启动Activity 分配坑

com.lianjia.loader2.FinderBuiltin // 读取asset下的plugins-builtin.json文件加载所有插件

com.lianjia.loader2.PluginInfo // 构造插件时需要的信息类
com.lianjia.loader2.Plugin  //代表插件类  queryObject(String name)反射得到实例,比如主工程获取插件Fragment
com.lianjia.loader2.Loader  //插件的classLoader管理类. loadDex()真正构造ClassLoader加载dex的地方


com.lianjia.i.Factory
com.lianjia.i.Factory2 // 
com.lianjia.i.a.LoaderActivity


插件文件名规范：barcode-1-10-2.jar
最小支持版本 ：例如 1，低于该版本的host/adapter是无法使用
当前接口版本：例如 10，host/adapter可选择插件
插件自身版本：例如 2

LJPluginApplication #attachBaseContext（）初始化所有插件，并load第一个默认插件

插件调主工程 (主工程自己的Activity是一定注册过的)
```

  /** 主工程地图主页activity名字 */
  public static final String MAP_SHOW_HOUSE_ACTIVITY =  
               "com.homelink.android.map.MapShowHouseActivity";
   SecondPluginLaunchHelper.goToMain(SecondPluginLaunchHelper.MAP_SHOW_HOUSE_ACTIVITY, bundle);

  public static void goToMain(String activity, Intent intent) {
    if (null == intent) {
      return;
    }
    // 主工程初始化插件时，传给插件的context
    Context context = EntryHelper.getPluginContext();
    ComponentName componentName = new ComponentName(context.getPackageName(), activity);
    intent.setComponent(componentName);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(intent);
  }
```

主工程调插件
```
//方式1
  public static final String MY_FOLLOW_HOUSE_ACTIVITY =     
        "com.homelink.android.newhouse.user.followlist.NewHouseFollowListActivity";
    PluginHelper.goToOthers(this, PluginHelper.MY_FOLLOW_HOUSE_ACTIVITY, bundle);

  public static void goToOthers(Context context, String plugin, String activity, Bundle bundle) {
    Intent intent = new Intent();
    intent.putExtra(BaseActivity.PARAM_INTENT, bundle);
    Factory.startActivity(context, intent, plugin, activity, IPluginManager.PROCESS_AUTO);
  }

// 方式2

  private void goToMyFollowHouseListActivity(Bundle bundle) {
    new RouterBus.Builder(APPConfigHelper.getContext(),
        ModuleUri.Customer.URL_MY_FOLLOW_SECOND).setBundle(bundle).build().startActivity();
    finish();
  }


```



----
1 PluginContainers 插件容器类
init() mainfest中占坑骗过组件检查

2 PatchClassLoaderUtils#patchAppContextPackageInfoClassLoader() 
把LoadedApk对象的mClassLoaderhook成了自定义的LocalClassLoader

dalvik.system.PathClassLoader[DexPathList[[zip file "/data/app/com.lianjia.plugindemo-1/base.apk", zip file "/data/app/com.lianjia.plugindemo-1/split_lib_dependencies_apk.apk", zip file "/data/app/com.lianjia.plugindemo-1/split_lib_slice_0_apk.apk", zip file "/data/app/com.lianjia.plugindemo-1/split_lib_slice_1_apk.apk", zip file "/data/app/com.lianjia.plugindemo-1/split_lib_slice_2_apk.apk", zip file "/data/app/com.lianjia.plugindemo-1/split_lib_slice_3_apk.apk", zip file "/data/app/com.lianjia.plugindemo-1/split_lib_slice_4_apk.apk", zip file "/data/app/com.lianjia.plugindemo-1/split_lib_slice_5_apk.apk", zip file "/data/app/com.lianjia.plugindemo-1/split_lib_slice_6_apk.apk", zip file "/data/app/com.lianjia.plugindemo-1/split_lib_slice_7_apk.apk", zip file "/data/app/com.lianjia.plugindemo-1/split_lib_slice_8_apk.apk", zip file "/data/app/com.lianjia.plugindemo-1/split_lib_slice_9_apk.apk"],nativeLibraryDirectories=[/data/app/com.lianjia.plugindemo-1/lib/arm64, /vendor/lib64, /system/lib64]]]


com.lianjia.loader.utils.LocalClassLoader[mBase=dalvik.system.PathClassLoader[DexPathList[[zip file "/data/app/com.lianjia.plugindemo-1/base.apk", zip file "/data/app/com.lianjia.plugindemo-1/split_lib_dependencies_apk.apk", zip file "/data/app/com.lianjia.plugindemo-1/split_lib_slice_0_apk.apk", zip file "/data/app/com.lianjia.plugindemo-1/split_lib_slice_1_apk.apk", zip file "/data/app/com.lianjia.plugindemo-1/split_lib_slice_2_apk.apk", zip file "/data/app/com.lianjia.plugindemo-1/split_lib_slice_3_apk.apk", zip file "/data/app/com.lianjia.plugindemo-1/split_lib_slice_4_apk.apk", zip file "/data/app/com.lianjia.plugindemo-1/split_lib_slice_5_apk.apk", zip file "/data/app/com.lianjia.plugindemo-1/split_lib_slice_6_apk.apk", zip file "/data/app/com.lianjia.plugindemo-1/split_lib_slice_7_apk.apk", zip file "/data/app/com.lianjia.plugindemo-1/split_lib_slice_8_apk.apk", zip file "/data/app/com.lianjia.plugindemo-1/split_lib_slice_9_apk.apk"],nativeLibraryDirectories=[/data/app/com.lianjia.plugindemo-1/lib/arm64, /vendor/lib64, /system/lib64]]]]

3 PMF.callAttach();加载默认插件




----


Caused by java.lang.ClassNotFoundException: com.homelink.android.newhouse.libcore.global.api.bean.NewHouseFilterInfo
       at java.lang.ClassLoader.findClass(ClassLoader.java:408)
       at com.lianjia.loader.utils.LocalClassLoader.findClass(LocalClassLoader.java:141)
       at java.lang.ClassLoader.loadClass(ClassLoader.java:380)
       at com.lianjia.loader.utils.LocalClassLoader.loadClass(LocalClassLoader.java:103)
       at java.lang.ClassLoader.loadClass(ClassLoader.java:312)
       at java.lang.Class.classForName(Class.java)
       at java.lang.Class.forName(Class.java:400)
       at android.os.Parcel$2.resolveClass(Parcel.java:2601)
       at java.io.ObjectInputStream.readNonProxyDesc(ObjectInputStream.java:1613)
       at java.io.ObjectInputStream.readClassDesc(ObjectInputStream.java:1518)
       at java.io.ObjectInputStream.readOrdinaryObject(ObjectInputStream.java:1772)
       at java.io.ObjectInputStream.readObject0(ObjectInputStream.java:1351)
       at java.io.ObjectInputStream.defaultReadFields(ObjectInputStream.java:1992)
       at java.io.ObjectInputStream.readSerialData(ObjectInputStream.java:1916)
       at java.io.ObjectInputStream.readOrdinaryObject(ObjectInputStream.java:1799)
       at java.io.ObjectInputStream.readObject0(ObjectInputStream.java:1351)
       at java.io.ObjectInputStream.readObject(ObjectInputStream.java:373)
       at android.os.Parcel.readSerializable(Parcel.java:2609)
       at android.os.Parcel.readValue(Parcel.java:2415)
       at android.os.Parcel.readArrayMapInternal(Parcel.java:2717)
       at android.os.BaseBundle.unparcel(BaseBundle.java:271)
       at android.os.BaseBundle.getInt(BaseBundle.java:865)
       at com.homelink.android.map.MapShowHouseActivity.onActivityResult(MapShowHouseActivity.java:971)
       at android.app.Activity.dispatchActivityResult(Activity.java:7116)
       at android.app.ActivityThread.deliverResults(ActivityThread.java:4192)
       at android.app.ActivityThread.handleSendResult(ActivityThread.java:4239)
       at android.app.ActivityThread.-wrap20(ActivityThread.java)
       at android.app.ActivityThread$H.handleMessage(ActivityThread.java:1599)
       at android.os.Handler.dispatchMessage(Handler.java:102)
       at android.os.Looper.loop(Looper.java:165)
       at android.app.ActivityThread.main(ActivityThread.java:6365)
       at java.lang.reflect.Method.invoke(Method.java)
       at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:883)
       at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:773)
https://www.fabric.io/lianjia2/android/apps/com.lianjia.beike/issues/5a6eed7b8cb3c2fa63d61680?time=last-thirty-days



PatchClassLoaderUtils:
            ClassLoader cl = new LocalClassLoader(oClassLoader.getParent(), oClassLoader);

bundle持有的classLoader
com.lianjia.loader.utils.LocalClassLoader[mBase=dalvik.system.PathClassLoader[DexPathList[[zip file "/data/app/com.lianjia.beike-YdyZVmI27Vpn-uXXaABxkA==/base.apk"],nativeLibraryDirectories=[/data/app/com.lianjia.beike-YdyZVmI27Vpn-uXXaABxkA==/lib/arm, /system/fake-libs, /data/app/com.lianjia.beike-YdyZVmI27Vpn-uXXaABxkA==/base.apk!/lib/armeabi, /system/lib, /vendor/lib]]]]

LocalClassLoader的成员：
mOrig = dalvik.system.PathClassLoader[DexPathList[[zip file "/data/app/com.lianjia.beike-YdyZVmI27Vpn-uXXaABxkA==/base.apk"],nativeLibraryDirectories=[/data/app/com.lianjia.beike-YdyZVmI27Vpn-uXXaABxkA==/lib/arm, /system/fake-libs, /data/app/com.lianjia.beike-YdyZVmI27Vpn-uXXaABxkA==/base.apk!/lib/armeabi, /system/lib, /vendor/lib]]]



PluginPathClassLoader:  插件的
com.lianjia.loader.utils.PluginPathClassLoader[mainClassLoaderdalvik.system.PathClassLoader[DexPathList[[zip file "/data/app/com.lianjia.beike-FJ55Yt_Qg4fo-7jkifSFZg==/base.apk"],nativeLibraryDirectories=[/data/app/com.lianjia.beike-FJ55Yt_Qg4fo-7jkifSFZg==/lib/arm, /system/fake-libs, /data/app/com.lianjia.beike-FJ55Yt_Qg4fo-7jkifSFZg==/base.apk!/lib/armeabi, /system/lib, /vendor/lib]]]]