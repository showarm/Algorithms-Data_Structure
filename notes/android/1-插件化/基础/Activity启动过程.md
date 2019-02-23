
## Activity的启动过程

http://pingguohe.net/2017/12/25/android-plugin-practice-part-1.html


https://www.jianshu.com/p/c6b04b3f911d
ActivityThread的performLaunchActivity方法

```
// Instrumentation
public Activity newActivity(ClassLoader cl, String className,
            Intent intent)
            throws InstantiationException, IllegalAccessException,
            ClassNotFoundException {
        return (Activity)cl.loadClass(className).newInstance();
    }

// ActivityThread
private Activity performLaunchActivity(ActivityClientRecord r, Intent customIntent) {
        Activity activity = null;
        try {
//这里调用Instrumentation的newActivity来创建一个新的Activity
            java.lang.ClassLoader cl = r.packageInfo.getClassLoader();
            activity = mInstrumentation.newActivity(
                    cl, component.getClassName(), r.intent);
        } catch (Exception e) {
        }

        try {
            Application app = r.packageInfo.makeApplication(false, mInstrumentation);

                activity.attach(appContext, this, getInstrumentation(), r.token,
                        r.ident, app, r.intent, r.activityInfo, title, r.parent,
                        r.embeddedID, r.lastNonConfigurationInstances, config,
                        r.referrer, r.voiceInteractor, window);

                if (customIntent != null) {
                    activity.mIntent = customIntent;
                }
                r.lastNonConfigurationInstances = null;
                activity.mStartedActivity = false;
                int theme = r.activityInfo.getThemeResource();
                if (theme != 0) {
                    activity.setTheme(theme);
                }

                activity.mCalled = false;
                if (r.isPersistable()) {
//这个方法里面会调用目标Activity的onCreate方法
                    mInstrumentation.callActivityOnCreate(activity, r.state, r.persistentState);
                } else {
                    mInstrumentation.callActivityOnCreate(activity, r.state);
                }
                if (!activity.mCalled) {
                    throw new SuperNotCalledException(
                        "Activity " + r.intent.getComponent().toShortString() +
                        " did not call through to super.onCreate()");
                }
                r.activity = activity;
                r.stopped = true;
.......
        } catch (SuperNotCalledException e) {
            throw e;

        } catch (Exception e) {
......
        }

        return activity;
    }

```
