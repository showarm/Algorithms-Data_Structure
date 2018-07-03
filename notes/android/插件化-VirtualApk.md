
# singwhatiwanna的两个库

dynamic-load-apk原理－代理 Activity + 反射
https://github.com/singwhatiwanna/dynamic-load-apk
https://github.com/didi/VirtualAPK


https://www.jianshu.com/p/56e8465677d7 

1 主工程初始化： 动态代理AMS  hook Instrumentation  
```
// hook系统AMS（其实是它在客户端的代理），用动态代理替换成自己的 new ActivityManagerProxy
public static IActivityManager newInstance(PluginManager pluginManager, IActivityManager activityManager) {
        return (IActivityManager) Proxy.newProxyInstance(activityManager.getClass().getClassLoader(), new Class[] { IActivityManager.class }, new ActivityManagerProxy(pluginManager, activityManager));
    }
private void hookAMSForO() {
        try {
            Singleton<IActivityManager> defaultSingleton = (Singleton<IActivityManager>) ReflectUtil.getField(ActivityManager.class, null, "IActivityManagerSingleton");
            IActivityManager activityManagerProxy = ActivityManagerProxy.newInstance(this, defaultSingleton.get());
            ReflectUtil.setField(defaultSingleton.getClass().getSuperclass(), defaultSingleton, "mInstance", activityManagerProxy);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

// hook ActivityThread，In是AT的一个属性，进而得到Instrumentation
@UiThread
    public static Object getActivityThread(Context base) {
        if (sActivityThread == null) {
            try {
                Class<?> activityThreadClazz = Class.forName("android.app.ActivityThread");
                Object activityThread = null;
                try {
                    activityThread = ReflectUtil.getField(activityThreadClazz, null, "sCurrentActivityThread");
                } catch (Exception e) {
                    // ignored
                }
                if (activityThread == null) {
                    activityThread = ((ThreadLocal<?>) ReflectUtil.getField(activityThreadClazz, null, "sThreadLocal")).get();
                }
                sActivityThread = activityThread;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return sActivityThread;
    }
    public static Instrumentation getInstrumentation(Context base) {
        if (getActivityThread(base) != null) {
            try {
                sInstrumentation = (Instrumentation) ReflectUtil.invoke(
                        sActivityThread.getClass(), sActivityThread, "getInstrumentation");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return sInstrumentation;
    }
```

2 ClassLoader加载插件包

初始化插件构造器 LoadedPlugin(PluginManager pluginManager, Context context, File apk)
    这一步干了很多事：
        1 PackageParserCompat#parsePackage()解析插件apk AndroidManifest.xml so文件目录
        2 加载资源 createResources() 主要是AssetManager的addAssetPath()方法
        3 createClassLoader()
        如果要让插件工程能调用宿主工程的类的话，那么就要将插件的dex和宿主的dex进行合并放在 dexElements 中。
```

// DexClassLoader
private static ClassLoader createClassLoader(Context context, File apk, File libsDir, ClassLoader parent) {
        File dexOutputDir = context.getDir(Constants.OPTIMIZE_DIR, Context.MODE_PRIVATE);
        String dexOutputPath = dexOutputDir.getAbsolutePath();
        DexClassLoader loader = new DexClassLoader(apk.getAbsolutePath(), dexOutputPath, libsDir.getAbsolutePath(), parent);

        if (Constants.COMBINE_CLASSLOADER) {
            try {
                DexUtil.insertDex(loader);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return loader;
    }
```
        4 将插件的so文件路径添加进 nativeLibraryDirectories ，然后重新设置给 DexPathList 中。将apk中的so文件拷贝到指定的位置。
        5 缓存instrumentations，activities，services，providers，receivers动态注册广播

然后Instrumentation构造插件的 Application，并调用其onCreate()：
VAInstrumentation#newApplication(this.mClassLoader, appClass, this.getPluginContext());
这里的classLoader是 DexClassLoader

3 启动插件Activity
    1 在宿主工程的 AndroidManifest.xml 中注册一些坑位
    ```
<application>
        <!-- Stub Activities -->
        <activity android:name=".A$1" android:launchMode="standard"/>
        <activity android:name=".A$2" android:launchMode="standard"
            android:theme="@android:style/Theme.Translucent" />

        <!-- Stub Activities -->
        <activity android:name=".B$1" android:launchMode="singleTop"/>
        <activity android:name=".B$2" android:launchMode="singleTop"/>
        ...
</application>
    ```

    2 之前我们hook掉系统的 Instrumentation,所以在启动Activity的时候会调用到 VAInstrumentation#execStartActivity()
    ```
    // 给插件Activity匹配坑位（根据启动模式/window样式）
public ActivityResult execStartActivity(
            Context who, IBinder contextThread, IBinder token, Activity target,
            Intent intent, int requestCode, Bundle options) {

        // 将隐式启动转为显式启动的方式，因为插件Activity未注册
        mPluginManager.getComponentsHandler().transformIntentToExplicitAsNeeded(intent);
        
        if (intent.getComponent() != null) { // null component 才是隐式启动。不空的话
            Log.i(TAG, String.format("execStartActivity[%s : %s]", intent.getComponent().getPackageName(),intent.getComponent().getClassName()));

            // 根据Intent给要启动的插件Activity 匹配坑位Activity
            this.mPluginManager.getComponentsHandler().markIntentIfNeeded(intent);
        }

        // 反射调用系统的 execStartActivity() 方法，这样的话流程就到AMS去了
        ActivityResult result = realExecStartActivity(who, contextThread, token, target,
                    intent, requestCode, options);

        return result;
    }
    ```
    3 将StubActivity替换回TargetActivity
    执行到mInstrumentation#newActivity()时，
    ```
@Override
    public Activity newActivity(ClassLoader cl, String className, Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        try {
            // 这里的className就是之前占坑,类不存在
            cl.loadClass(className);
        } catch (ClassNotFoundException e) {
            LoadedPlugin plugin = this.mPluginManager.getLoadedPlugin(intent);
            String targetClassName = PluginUtil.getTargetActivity(intent);

            Log.i(TAG, String.format("newActivity[%s : %s]", className, targetClassName));

            if (targetClassName != null) {
                Activity activity = mBase.newActivity(plugin.getClassLoader(), targetClassName, intent);
                activity.setIntent(intent);

                try {
                    // 插件使用 插件所创建出来的 Resources 对象
                    // for 4.1+
                    ReflectUtil.setField(ContextThemeWrapper.class, activity, "mResources", plugin.getResources());
                } catch (Exception ignored) {
                    // ignored.
                }

                return activity;
            }
        }

        return mBase.newActivity(cl, className, intent);
    }

    ```

    接下来会调用到 VAInstrumentation 的 callActivityOnCreate
    ```
@Override
    public void callActivityOnCreate(Activity activity, Bundle icicle) {
        final Intent intent = activity.getIntent();
        if (PluginUtil.isIntentFromPlugin(intent)) {
            Context base = activity.getBaseContext();
            // 插件自己的 Resources 和 Context
            try {
                LoadedPlugin plugin = this.mPluginManager.getLoadedPlugin(intent);
                ReflectUtil.setField(base.getClass(), base, "mResources", plugin.getResources());
                ReflectUtil.setField(ContextWrapper.class, activity, "mBase", plugin.getPluginContext());
                ReflectUtil.setField(Activity.class, activity, "mApplication", plugin.getApplication());
                ReflectUtil.setFieldNoException(ContextThemeWrapper.class, activity, "mBase", plugin.getPluginContext());

                // set screenOrientation
                ActivityInfo activityInfo = plugin.getActivityInfo(PluginUtil.getComponent(intent));
                if (activityInfo.screenOrientation != ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED) {
                    activity.setRequestedOrientation(activityInfo.screenOrientation);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        // 调用系统 Instrumentation 的 callActivityOnCreate() 方法来启动插件TargetActivity
        mBase.callActivityOnCreate(activity, icicle);
    }
    ```

