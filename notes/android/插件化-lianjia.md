
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
