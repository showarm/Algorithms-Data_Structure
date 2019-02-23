

https://www.fabric.io/lianjia2/android/apps/com.homelink.android/issues/56a2b6d2f5d3a7f76b45dd28?time=last-seven-days
Fatal Exception: java.util.concurrent.TimeoutException: android.os.BinderProxy.finalize() timed out after 10 seconds
       at android.os.BinderProxy.destroy(Binder.java)
       at android.os.BinderProxy.finalize(Binder.java:548)
       at java.lang.Daemons$FinalizerDaemon.doFinalize(Daemons.java:191)
       at java.lang.Daemons$FinalizerDaemon.run(Daemons.java:174)
       at java.lang.Thread.run(Thread.java:818)

http://stackoverflow.com/questions/24021609/how-to-handle-java-util-concurrent-timeoutexception-android-os-binderproxy-fin
https://github.com/realm/realm-java/issues/1390

