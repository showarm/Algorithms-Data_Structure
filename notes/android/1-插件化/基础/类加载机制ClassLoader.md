
## 类加载机制
https://www.tuicool.com/articles/yquQvqM 
1 负责加载类的类就是类加载器，将字节码文件加载到内存，创建Class对象。它的输入是完全限定的类名，输出是Class对象。
2 双亲委派" 模型，子ClassLoader有一个变量parent指向父ClassLoader，在子ClassLoader加载类时，一般会首先通过父ClassLoader加载。为什么要先让父ClassLoader去加载呢？这样，可以避免Java类库被覆盖的问题，比如用户程序也定义了一个类java.lang.String，通过双亲委派，java.lang.String只会被Bootstrap ClassLoader加载，避免自定义的String覆盖Java类库的定义。
3 ClassLoader的loadClass方法与Class的forName方法都可以加载类，但前者不会执行类的初始化代码。

1 Android中有两个ClassLoader分别为 dalvik.system.DexClassLoader 和 dalvik.system.PathClassLoader。
2 PathClassLoader 不能直接从 zip 包中得到 dex，因此只支持直接操作 dex 文件或者已经安装过的 apk（因为安装过的 apk 在 cache 【 /data/dalvik-cache】中存在缓存的 dex 文件）。
换肤 https://github.com/cxmscb/android-skinchanged-By-PathClassLoader/ ，会发现皮肤插件有个安装过程，锤子手机换主题时要先安装主题。

3 DexClassLoader 可以加载外部的 apk、jar 或 dex文件，并且会在指定的 outpath 路径存放其 dex 文件。

http://pingguohe.net/2017/12/25/android-plugin-practice-part-2.html

