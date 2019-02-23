
http://www.cplusplus.com/reference/cstring/  c&cpp文档
https://linux.die.net/man/3/   Linux的库函数
https://developer.android.google.cn/ndk/reference/ ndk Android 文档

https://developer.android.google.cn/ndk/guides/ 入门
共享库.so   静态库.a 
应用二进制界面 (ABI)  ARM,x86   https://developer.android.google.cn/ndk/guides/abis.html



https://github.com/googlesamples/android-ndk   谷歌给的 ndk demo

    jclass  clz = (*env)->FindClass(env,"com/example/hellojnicallback/JniHandler");
    //class对象
    g_ctx.jniHelperClz = (*env)->NewGlobalRef(env, clz); 
    // Java方法 签名
    jmethodID  jniHelperCtor = (*env)->GetMethodID(env, g_ctx.jniHelperClz,"<init>", "()V");
    // class对象的实例    class文件的init()方法
    jobject    handler = (*env)->NewObject(env, g_ctx.jniHelperClz,jniHelperCtor);
    g_ctx.jniHelperObj = (*env)->NewGlobalRef(env, handler);












https://github.com/googlesamples/android-audio-high-performance/


