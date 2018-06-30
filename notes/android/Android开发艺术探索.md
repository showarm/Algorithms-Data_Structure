https://github.com/singwhatiwanna/android-art-res


http://sparkyuan.me/categories/Android/Android%E5%BC%80%E5%8F%91%E8%89%BA%E6%9C%AF%E6%8E%A2%E7%B4%A2%E7%AC%94%E8%AE%B0/
http://blog.csdn.net/column/details/artandroid.html
http://blog.csdn.net/amurocrash/article/details/48858353
http://www.jianshu.com/p/68c45fa0668d?utm_source=tuicool&utm_medium=referral


http://www.csdn.net/tag/%E4%BB%BB%E7%8E%89%E5%88%9A/download

http://hujiaweibujidao.github.io/blog/2015/12/05/Art-of-Android-Development-Reading-Notes/
第6章 Android的Drawable
http://keeganlee.me/post/android/20150916
Drawable表示的是一种可以在Canvas上进行绘制的概念.
(0)ColorDrawable没有宽高,
内部大小getIntrinsicWidth和getIntrinsicHeight返回－1
实际区域大小，getBounds，一般和view的尺寸相同
(1)BitmapDrawable和NinePatchDrawable
<?xml version="1.0" encoding="utf-8"?>
<bitmap / nine-patch
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:src="@[package:]drawable/drawable_resource"
    android:antialias=["true" | "false"]抗锯齿
    android:dither=["true" | "false"]兼容低质量屏幕
    android:filter=["true" | "false"]兼容拉伸
    android:gravity=["top" | "bottom" | "left" | "right" | "center_vertical" |"fill_vertical" | "center_horizontal" | "fill_horizontal" |"center" | "fill" | "clip_vertical" | "clip_horizontal"]图小容器大
    android:tileMode=["disabled" | "clamp" | "repeat" | "mirror"] />开启平铺模式后，gravity属性会被忽略｜四周的像素会扩展｜平铺效果｜镜面投影
(2)ShapeDrawable
<?xml version="1.0" encoding="utf-8"?>
<shape    
    xmlns:android="http://schemas.android.com/apk/res/android"    
    android:shape=["rectangle" | "oval" | "line" | "ring"] >    
    <corners        //当shape为rectangle时使用
        android:radius="integer"        //半径值会被后面的单个半径属性覆盖，默认为1dp
        android:topLeftRadius="integer"        
        android:topRightRadius="integer"        
        android:bottomLeftRadius="integer"        
        android:bottomRightRadius="integer" />    
    <gradient       //渐变
        android:angle="integer"  渐变的角度，默认为0，其值必须是45的倍数，0表示从左到右，90表示从下到上      
        android:centerX="integer"        
        android:centerY="integer"        
        android:centerColor="integer"        
        android:endColor="color"        
        android:gradientRadius="integer"        
        android:startColor="color"        
        android:type=["linear" | "radial" | "sweep"]        
        android:useLevel=["true" | "false"] />    
    <padding        //内边距
        android:left="integer"        
        android:top="integer"        
        android:right="integer"        
        android:bottom="integer" /> 
ShapeDrawable默认情况下是没有宽高的概念，作为view的背景时，shape还是会被拉伸或者缩小为view的大小。   
    <size           //指定大小，一般用在imageview配合scaleType属性使用
        android:width="integer"        
        android:height="integer" />    
    <solid          //填充颜色
        android:color="color" />    
   	<stroke         //边框
      	android:width="integer"        
        android:color="color"        
        android:dashWidth="integer"        
        android:dashGap="integer" />
</shape>
Drawable集合：
(3)LayerDrawble
对应标签<layer-list>
(4)StateListDrawable
对应标签<selector>，默认的item都应该放在selector的最后一条并且不附带任何的状态。
(5)LevelListDrawable
对应标签<level-list>，根据不同的level，LevelListDrawable会切换不同的Drawable
(6)TransitionDrawable
对应标签<transition>，两个Drawable之间的淡入淡出效果
<transition xmlns:android="http://schemas.android.com/apk/res/android" >
    <item android:drawable="@drawable/shape_drawable_gradient_linear"/>
    <item android:drawable="@drawable/shape_drawable_gradient_radius"/>
</transition>
TransitionDrawable drawable = (TransitionDrawable) v.getBackground();
drawable.startTransition(5000);
(7)InsetDrawable
对应标签<inset>，将其他drawable内嵌到自己，四周留出一定的间距。一个view希望自己的背景比自己的实际区域小。
(8)ScaleDrawable
对应标签<scale>缩放，level越大，drawable看起来就越大
(9)ClipDrawable
对应标签<clip>，根据自己当前的level来裁剪另一个drawable
第8章 理解Window和WindowManager
class PhoneWindow extends Window 
interface WindowManager extends ViewManager
WindowManagerService
每个Window都对应着一个View和一个ViewRootImpl，Window和View通过ViewRootImpl来建立联系
mFloatingButton = new Button(this);
mFloatingButton.setText("test button");
mLayoutParams = new WindowManager.LayoutParams(
        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 0, 0,
        PixelFormat.TRANSPARENT);//0,0 分别是type和flags参数，在后面分别配置了
mLayoutParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
        | LayoutParams.FLAG_NOT_FOCUSABLE
        | LayoutParams.FLAG_SHOW_WHEN_LOCKED;
mLayoutParams.type = LayoutParams.TYPE_SYSTEM_ERROR;
mLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
mLayoutParams.x = 100;
mLayoutParams.y = 300;
mFloatingButton.setOnTouchListener(this);
mWindowManager.addView(mFloatingButton, mLayoutParams);
window类型和分层：
应用window，对应着一个Activity，1~99 
子window, Dialog，1000~1999 
系统window, 2000~2999  
Toast和系统状态栏,android.permission.SYSTEM_ALERT_WINDOW
1 Activity的window
class Activity extends ContextThemeWrapper
        implements LayoutInflater.Factory2,
        Window.Callback, KeyEvent.Callback,
        OnCreateContextMenuListener, ComponentCallbacks2,
        Window.OnWindowDismissedCallback 
Activity实现了Window的Callback接口: onAttachedToWindow
Activity的DecorView 和PhoneWindow：
ViewManager vm = getWindowManager();
vm.addView(mDecor, getWindow().getAttributes());
mWindowAdded = true;
2 Dialog的Window
与Activity的过程类似。
必须采用Activity的Context，原因是Application没有应用token，应用token一般是Activity拥有的
3 Toast的Window
Toast的显示和隐藏是IPC过程，NotificationManagerService
NMS会跨进程回调Toast中的TN类中的方法，TN类是一个Binder类，运行在Binder线程池中，所以需要通过Handler将其切换到当前发送Toast请求所在的线程，所以Toast无法在没有Looper的线程中弹出。
第9章 四大组件的工作过程
ABCS
Activity是一种展示型组件
ActivityThread的performLaunchActivity
BroadcastReceiver是一种消息型组件,工作在系统内部.
只有BroadcastReceiver既可以在AndroidManifest文件中注册，也可以在代码中注册
静态注册是在AndroidManifest中注册，在应用安装的时候会被系统解析，这种广播不需要应用启动就可以收到相应的广播。动态需要。
ContentProvider是一种数据共享型组件
ContentProvider的调用不需要借助Intent，其他三个组件都需要借助Intent。
insert、delete、update、query方法需要处理好线程同步，因为这几个方法是在Binder线程池中被调用的
Service是一种计算型组件
尽管service是用于后台执行计算的，但是它本身是运行在主线程中的
AMS－－Binder
第12章 Bitmap的加载和Cache
1 Bitmap
BitmapFactory类提供了四类方法：decodeFile、decodeResource、decodeStream和decodeByteArray
BitmapFactory.Options的inSampleSize参数，即采样率
public Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
    // First decode with inJustDecodeBounds=true to check dimensions
    final BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;
    BitmapFactory.decodeResource(res, resId, options);
    // Calculate inSampleSize
    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

    // Decode bitmap with inSampleSize set
    options.inJustDecodeBounds = false;
    return BitmapFactory.decodeResource(res, resId, options);
}
//计算采样率
public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
    if (reqWidth == 0 || reqHeight == 0) {
        return 1;
    }

    // Raw height and width of image
    final int height = options.outHeight;
    final int width = options.outWidth;
    Log.d(TAG, "origin, w= " + width + " h=" + height);
    int inSampleSize = 1;

    if (height > reqHeight || width > reqWidth) {
        final int halfHeight = height / 2;
        final int halfWidth = width / 2;

        // Calculate the largest inSampleSize value that is a power of 2 and
        // keeps both height and width larger than the requested height and width.
        while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
            inSampleSize *= 2;
        }
    }

    Log.d(TAG, "sampleSize:" + inSampleSize);
    return inSampleSize;
}
2 缓存策略
优先淘汰那些近期最少使用的缓存对象
LruCache(内存缓存)和DiskLruCache(磁盘缓存)
https://android.googlesource.com/platform/libcore/+/android-4.1.1_r1/luni/src/main/java/libcore/io/DiskLruCache.java
ImageLoader
https://github.com/singwhatiwanna/android-art-res/blob/master/Chapter_12/src/com/ryg/chapter_12/loader/ImageLoader.java
第13章 综合技术
1 setDefaultUncaughtExceptionHandler(UncaughtExceptionHandler handler) 当前进程所有线程未catch异常交给这个handler
https://github.com/singwhatiwanna/android-art-res/blob/master/Chapter_13/CrashTest/src/com/ryg/crashtest/CrashHandler.java
2 Android中单个dex文件所能够包含的最大方法数是65536，超过会DexIndexOverflowException
解决：android-support-multidex.jar
3 动态加载技术又称插件化技术
三个基础性问题：资源访问，Activity生命周期管理和插件ClassLoader的管理。
https://github.com/singwhatiwanna/dynamic-load-apk
第14章 JNI和NDK编程
http://hujiaweibujidao.github.io/blog/2013/11/18/android-ndk-and-opencv-developement/
第15章 Android性能优化
2015年Google关于Android性能优化典范
https://www.youtube.com/playlist?list=PLWz5rJ2EKKc9CBxr3BVjPTPoDPLdPIFCE
1 布局优化
<include>、<merge>、<viewstub>
ViewStub可以做到在需要的时候再加载,如网络异常页。
2 绘制优化
onDraw会被频繁调用，不要，创建新的布局对象／耗时／上万次循环。。
3 ANR
一个进程发生了ANR之后，系统会在/data/anr目录下创建一个文件traces.txt
4 其他优化建议
1.不要过多使用枚举，枚举占用的内存空间要比整型大；
2.常量请使用static final来修饰；
3.使用一些Android特有的数据结构，比如SparseArray和Pair等，他们都具有更好的性能；
4.适当使用软引用和弱引用；
5.采用内存缓存和磁盘缓存；
6.尽量采用静态内部类，这样可以避免潜在的由于内部类而导致的内存泄露。
(9)MAT是功能强大的内存分析工具，主要有Histograms和Dominator Tree等功能
http://www.jianshu.com/users/e1b0e2c62ee6/latest_articles
第1章 Activity的生命周期和启动模式
onStart和onStop是从Activity是否可见这个角度来回调的，
而onResume和onPause是从Activity是否位于前台这个角度来回调的。
onConfigurationChanged
onNewIntent
adb shell dumpsys activity
<category android:name="android.intent.category.DEFAULT”/>
第2章 IPC机制
Socket实现任意两个终端之间的通信，分为流式套接字和用户数据包套接字两种，分别对应网络的传输控制层的TCP和UDP协议。
IPC为进程间通讯Internet Process Connection
android:process多进程
序列化 Serializable／Parcelable
Bundle  implements Cloneable, Parcelable
在Android开发中，Binder主要用于Service中，包括AIDL和Messenger
transient不参与序列化
Messenger，底层实现就是AIDL
class Binder implements IBinder 
不用通过AIDL而直接实现Binder
https://github.com/singwhatiwanna/android-art-res/blob/master/Chapter_2/src/com/ryg/chapter_2/manualbinder/BookManagerImpl.java
Binder连接池BinderPool
https://github.com/singwhatiwanna/android-art-res/blob/master/Chapter_2/src/com/ryg/chapter_2/binderpool/BinderPool.java
android— IPC— Bunder— AIDL 

第3章 View事件体系
View平移的过程中，top和left表示原始左上角的位置信息；其值不会改变.
改变的是x、y、translationX和translationY。
x = left + translationX； y = top + translationY；
==移动
1 scrollTo/scrollBy,适合对View内容的滑动
getScrollX/getScrollY方法获取mScrollX/mScrollY总是等于View边缘与View内容边缘的距离，
2 使用动画来移动View,主要是操作View的translationX和translationY属性,适合没有交互的View
属性动画兼容Android 3.0以下，ViewHelper类
http://nineoldandroids.com/
3 改变布局参数实现滑动,适合有交互的View
ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mbtn1.getLayoutParams();
params.leftMargin += 100;
mbtn1.requestLayout();
//或者mbtn1.setLayoutParams(params);
4 Scroller用来实现View的弹性滑动，View的scrollTo/scrollBy是瞬间完成的，使用Scroller配合View的computeScroll方法配合使用达到弹性滑动的效果.
5 延时策略完成滑动 ,Handler或View的postDelayed方法，postDelayed发送延时消息，然后消息中进行View滑动
＝＝联通
dispatchTouchEvent(MotionEvent event) 分发，返回结果受当前View的onTouchEvent和下级View的dispatchTouchEvent方法影响。
onInterceptTouchEvent(MotionEvent event)拦截，在dispatchTouchEvent方法内部调用。
onTouchEvent(MotionEvent event)处理，在dispatchTouchEvent方法中调用
public boolean dispatchTouchEvent(MotionEvent ev){    
    boolean consume = false;
    if(onInterceptTouchEvent(ev)){
        consume = onTouchEvent(ev);
    }else{
        consume = child.dispatchTouchEvent(ev);   
    }
    return consume;
}
OnTouchListener优先级高于onTouchEvent
if(OnTouchListener.onTouch() == true){ 
	//给监听器了
} else{
	view.onTouchEvent();
}
向下传递：Activity->Window->View
向上传递：View ->Window->Activity
if(child.onTouchEvent() == false){//false向上
	parent.onTouchEvent();
}
MotionEvent动作
down->move...move->up
滑动冲突：
父View onInterceptTouchEvent，决定是否拦截。
https://github.com/singwhatiwanna/android-art-res/blob/master/Chapter_3/src/com/ryg/chapter_3/ui/HorizontalScrollViewEx.java
先到子View，决定是否处理，不处理交给父，parent.requestDisallowInterceptTouchEvent(false)。
https://github.com/singwhatiwanna/android-art-res/blob/master/Chapter_3/src/com/ryg/chapter_3/ui/ListViewEx.java
getRawX/getRawY获取相对手机屏幕左上角的x和y坐标
TouchSlop是系统能识别滑动的最小距离，是系统常量；
ViewConfiguration.get（getContext()).getScaledTouchSlop();
VelocityTracker用于追踪手指在滑动过程中的速度。
速度 = （终点位置 - 起点位置）/ 时间段
使用前在View的onTouchEvent方法中
VelocityTracker velocityTracker = VelocityTracker.obtain(); 
velocityTracker.addMovement(event); //在onTouchEvent方法中
//计算速度  参数 时间间隔 单位ms
velocityTracker.computeCurrentVelocity(1000); 像素/100ms 
//获取速度int xVelocity = (int)velocityTracker.getXVelocity(); 
int yVelocity = (int)velocityTracker.getYVelocity();
//重置和回收
mVelocityTracker.clear(); //一般在MotionEvent.ACTION_UP的时候调用
mVelocityTracker.recycle(); //一般在onDetachedFromWindow中调用
第4章 View的工作原理
https://github.com/singwhatiwanna/android-art-res/blob/master/Chapter_4/src/com/ryg/chapter_4/ui/CircleView.java
ViewRootImpl. performTraversals()
performMeasure、performLayout和performDraw.
performMeasure()— measure()— onMeasure()— 子measure()— 子onMeasure()
完成了整个View树的遍历
MeasureSpec代表一个32位的int值，高2位为SpecMode，低30位为SpecSize
MpecMode有三类：
1.UNSPECIFIED 父容器不对View进行任何限制，要多大给多大，一般用于系统内部
2.EXACTLY 父容器检测到View所需要的精确大小，这时候View的最终大小就是SpecSize所指定的值。LayoutParams.match_parent和具体数值是这个模式。
3.AT_MOST 父容器指定了一个可用大小即SpecSize，子View的大小不能大于这个值，不同View实现不同，对应LayoutParams.wrap_content。
1 View的measure
protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {          
     setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(),  widthMeasureSpec),     
     getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));}
父子：view的MeasureSpec由父容器的MeasureSpec和自身的LayoutParams来共同决定。
子				父		
固定宽/高		whatever MeasureSpec 		子EXACTLY
match_parent    EXACTLY					子EXACTLY&大小等于父剩余
match_parent	AT_MOST					子AT_MOST&大小不超过父剩余
wrap_content	whatever					子AT_MOST&大小不超过父剩余
1 ViewGroup的measure
measureChildren（）这个方法：
遍历子元素，调用measureChild方法；
取出子元素的LayoutParams，再通过getChildMeasureSpec方法来创建子元素的MeasureSpec，接着将MeasureSpec传递给View的measure方法进行测量。
ViewGroup抽象类没有实现测量具体过程，由具体子类实现。
解决获得宽高为0:
Activity/View # onWindowFocusChanged
view.post(runnable)
ViewTreeObserver的众多回调方法
view.measure(int widthMeasureSpec, int heightMeasureSpec)主动，但match_parent无法测出。
2 layout
最终宽/高形成于View的layout过程。
系统可能要多次measure才能确定最终的测量宽/高，比较好的习惯是在onLayout方法中去获取测量宽/高或者最终宽/高。
getMeasuredWidth/getMeasureHeight
getTop／getBotton、getLeft和getRight
3 Draw
将View绘制到屏幕上，大概的几个步骤：
1.绘制背景background.draw(canvas)
2.绘制自己（onDraw）
3.绘制children(dispatchDraw)
4.绘制装饰（onDrawScrollBars）
直接继承View的自定义控件，（继承特定的不需要，TextView，Button）
需要在onMeasure处理wrap_content，
否则在布局中wrap_content相当于match_parent。
解决方法：wrap_content时指定宽高，其他情况沿用系统的测量值即可。
需要在onDraw处理padding
View中有线程和动画，需要在View的onDetachedFromWindow中停止。
View中不需要Handler，直接post
DecorView作为顶级View
//获取内容栏
ViewGroup content = findViewById(R.android.id.content);
//获取我们设置的Viewcontext.getChildAt(0);
第5章 理解RemoteViews
https://github.com/singwhatiwanna/android-art-res/blob/master/Chapter_5/src/com/ryg/chapter_5/MainActivity.java
RemoteViews运行在其他进程，比如通知，小部件在SystemService进程。
1 RemoteViews在通知栏
Notification notification = new Notification();
    notification.icon = R.mipmap.ic_launcher;
    notification.tickerText = "hello notification";
    notification.when = System.currentTimeMillis();
    notification.flags = Notification.FLAG_AUTO_CANCEL;
    Intent intent = new Intent(this, RemoteViewsActivity.class);
    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.layout_notification);//RemoveViews所加载的布局文件
    remoteViews.setTextViewText(R.id.tv, "这是一个Test");//设置文本内容
    remoteViews.setTextColor(R.id.tv, Color.parseColor("#abcdef"));//设置文本颜色
    remoteViews.setImageViewResource(R.id.iv, R.mipmap.ic_launcher);//设置图片
    PendingIntent openActivity2Pending = PendingIntent.getActivity
            (this, 0, new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);//设置RemoveViews点击后启动界面
    remoteViews.setOnClickPendingIntent(R.id.tv, openActivity2Pending);
    notification.contentView = remoteViews;
    notification.contentIntent = pendingIntent;
    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    manager.notify(2, notification);
2 RemoveViews在桌面小部件，其实是广播
public class ImgAppWidgetProvider extends AppWidgetProvider {
RemoteViews中只支持发起PendingIntent，不支持onClickListener那种模式。setOnClickPendingIntent用于给普通的View设置单击事件
3 PendingIntent
PendingIntent的匹配规则是：如果两个PendingIntent他们内部的Intent相同并且requestCode也相同，那么这两个PendingIntent就是相同的。那么什么情况下Intent相同呢？Intent的匹配规则是，如果两个Intent的ComponentName和intent-filter都相同；那么这两个Intent也是相同的。
4 RemoteViews最大的意义在于方便的跨进程更新UI。
加载其他App的布局
 final String pkg = "cn.hudp.remoteviews";//需要加载app的包名
  Resources resources = null;
  try {
      resources = getPackageManager().getResourcesForApplication(pkg);
  } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
  }
  if (resources != null) {
      int layoutId = resources.getIdentifier("activity_main", "layout", pkg); //获取对于布局文件的id
      RemoteViews remoteViews = new RemoteViews(pkg, layoutId);
      View view = remoteViews.apply(this, llRemoteViews);//llRemoteViews是View所在的父容器
      llRemoteViews.addView(view);
  }
第7章 动画深入分析
1 View动画
http://keeganlee.me/post/android/20151003
View动画是对View的影像做动画，并不是真正的改变了View的状态。
setVisibility(View.GONE）失效，view.clearAnimation()清理View动画。
TranslateAnimation（平移动画）、ScaleAnimation（缩放动画）、RotateAnimation（旋转动画）和AlphaAnimation（透明度动画）
<set>标签表示动画集合，对应AnimationSet类
自定义，继承Animation这个抽象类，并重写initialize和applyTransformation方法
两个特殊场景：LayoutAnimation作用于ViewGroup＋Activity/Fragment转场动画。
overridePendingTransition(int enterAnim,int exitAnim)
FragmentTransaction中的setCustomAnimations()
LayoutAnimation作用于ViewGroup,为ViewGroup指定一个动画，当他的子元素出场的时候都会具有这种动画，ListView上用的多，LayoutAnimation也是一个View动画。
<layoutAnimation xmlns:android="http://schemas.android.com/apk/res/android"android:animationOrder="normal"android:delay="0.3" android:animation="@anim/anim_item"/>
//--- delay 表示动画开始的时间延迟，比如子元素入场动画的时间周期为300ms,
//那么0.5表示每个子元素都需要延迟150ms才开始播放入场动画。
//--- animationOrder 表示子元素的动画的顺序，有三种选项：
//normal(顺序显示）、reverse（逆序显示）和random（随机显示）。
//---1. android:animation 为子元素指定具体的入场动画
//----------------------
<?xml version="1.0" encoding="utf-8"?><set xmlns:android="http://schemas.android.com/apk/res/android"android:duration="300"android:shareInterpolator="true"><alphaandroid:fromAlpha="0.0"android:toAlpha="1.0" /><translateandroid:fromXDelta="300"android:toXDelta="0" /></set>
//--- 第一种方法、为需要的ViewGroup指定android:layoutAnimation属性//---这样ViewGroup的子View就有出场动画了
 <ListView
     android:id="@+id/lv"
     android:layout_width="match_parent"
     android:layout_height="0dp"
     android:layout_weight="1"
     android:layoutAnimation="@anim/anim_layout"/>
////--- 第二种方法、通过LayoutAnimationController来实现
Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_item);
LayoutAnimationController controller = new LayoutAnimationController(animation);
controller.setDelay(0.5f);
controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
listview.setLayoutAnimation(controller);
2 属性动画是API11新引入的
属性动画可以对任意对象的属性进行动画而不仅仅是View，动画默认间隔300ms，默认帧率10ms/帧。
属性动画的工作原理
通过反射调用get/set方法；属性动画需要运行在有Looper的线程中。
如果没有get和set:
//包装View类 用于给属性动画调用 从而包装了set getpublic class ViewWrapper {
 private View target;
 public ViewWrapper(View target) {
     this.target = target;
 }
 public int getWidth() {
     return target.getLayoutParams().width;
 }
 public void setWidth(int width) {
     target.getLayoutParams().width = width;
     target.requestLayout();
 }
}
监听器：
AnimatorListenerAdapter implement AnimatorListener;
AnimatorUpdateListener,每一帧都会调。
时间插值器（TimeInterpolator）的作用是根据时间流逝的百分比来计算出当前属性值改变的百分比，系统预置的有LinearInterpolator（线性插值器：匀速动画），AccelerateDecelerateInterpolator（加速减速插值器：动画两头慢中间快），DecelerateInterpolator(减速插值器：动画越来越慢）。
估值器（TypeEvaluator）的作用是根据当前属性改变的百分比来计算改变后的属性值。系统预置有IntEvaluator 、FloatEvaluator 、ArgbEvaluator。
activity退出，立即停止无线循环的属性动画。
px会导致不同设备上有不同的效果
使用硬件加速可以提高动画的流畅度
第10章 Android的消息机制
MessageQueue是通过单链表的数据结构来维护消息列表的。
ThreadLocal原理：不同线程访问同一个ThreadLoacl的get方法，ThreadLocal的get方法会从各自的线程中取出一个数组，然后再从数组中根据当前ThreadLocal的索引去查找对应的Value值。
如果退出Looper，这个线程就会立刻终止...
1 UI线程
ActivityThread的main方法，
public static void main(String[] args) {
	Looper.prepareMainLooper();
	Looper.loop();
｝
创建了UI线程的MessageQueue，然后loop()方法是一个while(true)，
然后UI线程的操作：启动activity，Button点击等之类，都是主线程的Handler把消息添加到了MessageQueue,然后在loop()种执行。
主线程Looper不能终止。
如果子线程访问UI,程序会抛出异常；ViewRootImpl在checkThread方法中做了判断。
高效的单线程模型来处理UI操作。
2 其他线程
Looper.obtion();
prepare();
loop();//不断从MessageQueue取
new Handler(looper);//指定某个线程的looper，每个线程有自己的Looper
然后handler不断往MessageQueue添加
所有事情done后，
Looper.quit/quitSafely
第11章 线程与线程池
1 AsyncTask
两个线程池（SerialExecutor和THREAD_POOL_EXECUTOR）和一个InternalHandler,
2 
HandlerThread 
一种可以使用Handler的Thread
IntentService. onHandlerIntent
IntentService封装了HandlerThread和Handler，onCreate方法自动创建一个HandlerThread,然后用它的Looper构造了一个Handler对象mServiceHandler，这样通过mServiceHandler发送的消息都会在HandlerThread执行；
IntentServiced的onHandlerIntent方法是一个抽象方法，需要在子类实现，onHandlerIntent方法执行后，stopSelt(int startId)就会停止服务，如果存在多个后台任务，执行完最后一个stopSelf(int startId)才会停止服务
IntentService是一个服务，它内部采用HandlerThread来执行任务，当任务执行完毕后就会自动退出。因为它是服务的缘故，所以和后台线程相比，它比较不容易被系统杀死。
ThreadPoolExecutor
常见的4个线程池
FixedThreadPool：线程数量固定的线程池，当所有线程都处于活动状态时，新任务会处于等待状态，只有核心线程并且不会回收（无超时机制），能快速的响应外界请求。
CachedThreadPool：线程数量不定的线程池，最大线程数为Integer.MAX_VALUE(相当于任意大),当所有线程都处于活动状态时，会创建新线程来处理任务；线程池的空闲进程超时时长为60秒，超过就会被回收；任何任务都会被立即执行，适合执行大量的耗时较少的任务。
ScheduledThreadPool：核心线程数量固定，非核心线程数量无限制，非核心线程闲置时会被立刻回收，用于执行定时任务和具有固定周期的重复任务。
SingleThreadExecutor 
一个线程，串行

