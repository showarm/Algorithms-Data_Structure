
https://www.tuicool.com/articles/mQ73qaB

/**
* Interface to global information about an application environment.  This is
* an abstract class whose implementation is provided by
* the Android system.  It
* allows access to application-specific resources and classes, as well as
* up-calls for application-level operations such as launching activities,
* broadcasting and receiving intents, etc.
*/
public abstract class Context {
    
    // 四大组件相关
    public abstract void startActivity(@RequiresPermission Intent intent);
    public abstract void sendBroadcast(@RequiresPermission Intent intent);
    public abstract Intent registerReceiver(@Nullable BroadcastReceiver receiver,
                                            IntentFilter filter);
    public abstract void unregisterReceiver(BroadcastReceiver receiver);
    public abstract ComponentName startService(Intent service);
    public abstract boolean stopService(Intent service);
    public abstract boolean bindService(@RequiresPermission Intent service,
            @NonNull ServiceConnection conn, @BindServiceFlags int flags);
    public abstract void unbindService(@NonNull ServiceConnection conn);
    public abstract ContentResolver getContentResolver();
    
    // 获取系统/应用资源
    public abstract AssetManager getAssets();
    public abstract Resources getResources();
    public abstract PackageManager getPackageManager();
    public abstract Context getApplicationContext();
    public abstract ClassLoader getClassLoader();
    public final @Nullable <T> T getSystemService(@NonNull Class<T> serviceClass) { ... }
    
    public final String getString(@StringRes int resId) { ... }
    public final int getColor(@ColorRes int id) { ... }
    public final Drawable getDrawable(@DrawableRes int id) { ... }
    public abstract Resources.Theme getTheme();
    public abstract void setTheme(@StyleRes int resid);
    public final TypedArray obtainStyledAttributes(@StyleableRes int[] attrs) { ... }
    
    // 获取应用相关信息
    public abstract ApplicationInfo getApplicationInfo();
    public abstract String getPackageName();
    public abstract Looper getMainLooper();
    public abstract int checkPermission(@NonNull String permission, int pid, int uid);
    
    // 文件相关
    public abstract File getSharedPreferencesPath(String name);
    public abstract File getDataDir();
    public abstract boolean deleteFile(String name);
    public abstract File getExternalFilesDir(@Nullable String type);
    public abstract File getCacheDir();
    ...
    public abstract SharedPreferences getSharedPreferences(String name, @PreferencesMode int mode);
    public abstract boolean deleteSharedPreferences(String name);
    
    // 数据库相关
    public abstract SQLiteDatabase openOrCreateDatabase(...);
    public abstract boolean deleteDatabase(String name);
    public abstract File getDatabasePath(String name);
    ...
    
    // 其它
    public void registerComponentCallbacks(ComponentCallbacks callback) { ... }
    public void unregisterComponentCallbacks(ComponentCallbacks callback) { ... }
    ...
}

public interface ComponentCallbacks {
    void onConfigurationChanged(Configuration newConfig);
    void onLowMemory();
}

Context 就相当于 Application 的大管家，主要负责：

1 四大组件的交互，包括启动 Activity、Broadcast、Service，获取 ContentResolver 等
2 获取系统/应用资源，包括 AssetManager、PackageManager、Resources、System Service 以及 color、string、drawable 等
3 文件，包括获取缓存文件夹、删除文件、SharedPreference 相关等
4 数据库（SQLite）相关，包括打开数据库、删除数据库、获取数据库路径等
5 其它辅助功能，比如设置 ComponentCallbacks，即监听配置信息改变、内存不足等事件的发生