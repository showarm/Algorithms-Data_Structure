
本文基于Android-23
## Framework中有哪些Manager？
// 常用
* android.app.ActivityManager
* android.app.FragmentManager
* android.content.pm.PackageManager
* android.content.res.AssetManager
* android.view.WindowManager
* android.view.ViewManager
* android.bluetooth.BluetoothManager
* android.net.wifi.WifiManager
* android.os.PowerManager
// 不常用
* android.accounts.AccountManager
* android.app.AppOpsManager
* android.app.LoaderManager
* android.app.SearchManager
* android.app.VrManager
* android.app.admin.DevicePolicyManager
* android.app.usage.NetworkStatsManager
* android.hardware.usb.UsbManager
* android.net.nsd.NsdManager
* android.nfc.NfcManager
* android.os.UserManager
* android.print.PrintManager

#### android.app.ActivityManager
管理系统中所有正在运行的Activity  

* 静态方法
void getMyMemoryState(RunningAppProcessInfo outState) //当前进程信息，pid,uid...
boolean isUserAMonkey() // 
boolean isRunningInTestHarness() // 

* 非静态方法
int getMemoryClass() //虚拟机给app分配的堆栈内存级别，也就是以前传说中的16M，现在的手机分辨率高，一般是64M
int getLargeMemoryClass() //开启android:largeHeap="true"，s虚拟机给app分配的堆栈内存级别
boolean isLowRamDevice() //是否小内存设备，比如512MB，800x480
List<ActivityManager.AppTask> getAppTasks() //此app的任务栈AppTask列表
Size getAppTaskThumbnailSize() //任务栈AppTask缩略图的尺寸
int addAppTask(@NonNull Activity activity, @NonNull Intent intent,
            @Nullable TaskDescription description, @NonNull Bitmap thumbnail) //给app添加一个AppTask，点最近按钮的时候会展示。返回task id。
List<RunningTaskInfo> getRunningTasks(int maxNum) //返回系统中活着的任务栈，涉及隐私，LOLLIPOP之后不再使用。
void moveTaskToFront(int taskId, int flags, Bundle options) //把某任务栈放到最前立即用户可见，需要权限REORDER_TASKS
List<RunningServiceInfo> getRunningServices(int maxNum) // 返回系统中活着的services
List<RunningAppProcessInfo> getRunningAppProcesses() // 返回系统中活着的进程
PendingIntent getRunningServiceControlPanel(ComponentName service) // 返回某活着的service对应的控制面板
void getMemoryInfo(MemoryInfo outInfo) // 系统内存状态
boolean clearApplicationUserData() // 清除app磁盘数据，回到刚安装的状态
List<ProcessErrorStateInfo> getProcessesInErrorState() // 返回程序错误记录
Debug.MemoryInfo[] getProcessMemoryInfo(int[] pids) // 返回进程的内存占用信息
void killBackgroundProcesses(String packageName) //杀掉包名下的后台进程，需要权限KILL_BACKGROUND_PROCESSES
ConfigurationInfo getDeviceConfigurationInfo() // app的AndroidManifest.xml配置的uses-configuration/uses-feature信息
int getLauncherLargeIconDensity() // app快捷方式图标分辨率DPI
int getLauncherLargeIconDensity() // app快捷方式图标像素
void dumpPackageState(FileDescriptor fd, String packageName) // 生成一份app状态快照，在fd目录，需要权限DUMP
setWatchHeapLimit(long pssSize) // 系统开始监控此进程的内存，超过pssSize时，自动生成一份快照。
void clearWatchHeapLimit() // 取消监控
int getLockTaskModeState() // lock task mode状态

#### android.app.FragmentManager
管理Activity中的Fragment

* 静态方法


* 非静态方法
FragmentTransaction beginTransaction() // 开始Fragment transaction。不能在onSaveInstanceState之后，onStart之前created/committed transaction。
boolean executePendingTransactions() // FragmentTransaction.commit()是异步执行的，而调这个方法会立即触发这类本来是异步的操作，只能在UI线程调用。
Fragment findFragmentById(int id) //先从当前Activity里找，找不到，从整个栈里找id一样的
findFragmentByTag(String tag) // 




#### android.accounts.AccountManager
#### android.app.AppOpsManager
#### android.app.LoaderManager
#### android.app.SearchManager
#### android.app.VrManager
#### android.app.admin.DevicePolicyManager
#### android.app.usage.NetworkStatsManager
#### android.bluetooth.BluetoothManager
#### android.content.pm.PackageManager
#### android.content.res.AssetManager
#### android.hardware.usb.UsbManager
#### android.net.nsd.NsdManager
#### android.net.wifi.WifiManager
#### android.nfc.NfcManager
#### android.os.PowerManager
#### android.os.UserManager
#### android.print.PrintManager
#### android.view.WindowManager
#### android.view.ViewManager
