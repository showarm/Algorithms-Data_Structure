
## JavaPoet
https://juejin.im/post/5bc598146fb9a05d330ae8b2  
https://juejin.im/entry/58fefebf8d6d810058a610de

## https://github.com/joyrun/AptPreferences 持久化框架


## https://github.com/joyrun/ActivityRouter APT的ActivityRouter
https://github.com/liujingg/ActivityRouter 加了kotlin支持
#### 1 使用
```
@RouterActivity({"second", "other2://www.thejoyrun.com/second", "test://www.thejoyrun.com/second"})
public class SecondActivity extends BaseActivity {
    @RouterField("uid")
    private int uid;
```
Router.init("test");//设置Scheme
// 方式一
RouterHelper.getSecondActivityHelper().withUid(24).start(this);
// 方式二
Router.startActivity(context, "test://second?uid=233");
// 方式三
// 如果AndroidManifest.xml注册了RouterCenterActivity，也可以通过下面的方式打开，如果是APP内部使用，不建议使用。
// startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("test://second?uid=233")));

#### 2 路由表
```
public class OtherRouterInitializer implements RouterInitializer {
  static {
    Router.register(new OtherRouterInitializer());} //调用下面init()

  @Override
  public void init(Map<String, Class<? extends Activity>> arg0) {
    arg0.put("other", OtherActivity.class);
  }
}
```
@RouterActivity -> 生成targetModuleName) + "RouterInitializer"
记录整个项目的路由表Map<String, Class<? extends Activity>> sRouter;
注解值 和 对应类

#### 3 处理参数字段
@RouterActivity -> 生成'SecondActivity'Helper
@RouterField -> 生成Helper类里面的builder方法
然后Helper类的Map<String, String> params记录了对应Activity的所有 路由参数-值

```
//根据RouterField.class生成的各种builder方法
public class SecondActivityHelper extends ActivityHelper {
  public SecondActivityHelper() {
    super("second");
  }

  public SecondActivityHelper withFormActivity(String formActivity) {
    put("formActivity",formActivity );
    return this;
  }

  public SecondActivityHelper withUid(int uid) {
    put("uid",uid );
    return this;
  }
```

#### 4 API类 RouterHelper
```
public class AppRouterHelper {
  public static ThirdActivityHelper getThirdActivityHelper() {
    return new ThirdActivityHelper();
  }

  public static SecondActivityHelper getSecondActivityHelper() {
    return new SecondActivityHelper();
  }
}
```
每个module -> 生成targetModuleName + "RouterHelper"
里面是 @RouterActivity -> 静态 getActivityHelper()
所以是跨module的。
方式一由此完成。

#### 5 方式二
```
    public void start(Context context) {
        Router.startActivity(context, getUrl());// 根据scheme,host,params拼接
    }
```
其实方式一调的还是方式二。
```
    public static boolean startActivity(Context context, String url) {
        //从路由表sRouter查询
        Class clazz = getActivityClass(url, uri);
        if (clazz != null) {
            Intent intent = new Intent(context, clazz);
            intent.setData(uri);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
    }
```
#### 6 RouterCenterActivity 
空Activity进行中央路由，内部            Router.startActivity(this, url);
主要场景是从浏览器或其他App跳转过来，分发scheme。
想象一下，使用http url从浏览器直接打开某一Activity。
```
        <activity android:name="com.thejoyrun.router.RouterCenterActivity">
            <intent-filter>
                <action android:name="android.intent.action.VIEW" />
                <category android:name="android.intent.category.DEFAULT" />
                <category android:name="android.intent.category.BROWSABLE" />
                <data android:host="www.thejoyrun.com" android:scheme="http" />
            </intent-filter>
            <intent-filter>
                <action android:name="android.intent.action.VIEW" />
                <category android:name="android.intent.category.DEFAULT" />
                <category android:name="android.intent.category.BROWSABLE" />
                <data android:scheme="test" />
            </intent-filter>

        </activity>
```
#### 7 Router.inject(this);
做了两件事，
把路由参数值赋给@RouterField变量        
把url中一级path(就是当前Activity的path)置空，用剩下的url尝试二级跳转。

#### 8 kotlin支持
https://github.com/liujingg/ActivityRouter/commit/55e4e99c303fd8b9e3f56d879648d658917ad4c1


















