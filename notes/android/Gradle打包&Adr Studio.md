minSdkVersion = 19
  targetSdkVersion = 21 // 动态代码 context.getApplicationInfo().targetSdkVersion
  compileSdkVersion = 25


        /**
         * The user-visible SDK version of the framework; its possible
         * values are defined in {@link Build.VERSION_CODES}.
         */
        public static final int SDK_INT = SystemProperties.getInt(
                "ro.build.version.sdk", 0);
 Build.VERSION.SDK_INT = 26 // 这是手机的android版本，26是O 奥利奥



./gradlew -v 版本号
./gradlew clean 删除build目录，移除所有构建的文件。
./gradlew -stop 杀掉进程
./gradlew build 检查依赖并编译打包
这里注意的是 ./gradlew build 命令把debug、release环境的包都打出来，如果正式发布只需要打Release的包，该怎么办呢，下面介绍一个很有用的命令 **assemble**, 如
./gradlew assemble   编译代码并打成jar包，但不会运行单元测试。
./gradlew assembleDebug 编译并打Debug包
./gradlew assembleRelease 编译并打Release的包
./gradlew installDebug  编译打包、安装
除此之外，assemble还可以和productFlavors结合使用，具体在下一篇多渠道打包进一步解释。
./gradlew installRelease Release模式打包并安装
./gradlew uninstallRelease 卸载Release模式包
./gradlew -stop会把已有的gradle daemon进程杀掉
每次编译慢了，就运行 pkill -9 "java"
./gradlew tasks  list the tasks of a project.
./gradlew properties  list the properties of a project. 

https://docs.gradle.org/current/userguide/userguide.html 
http://blog.csdn.net/column/details/gradle-translation.html 
Gradle2.0用户指南翻译

compile group: 'org.hibernate', name: 'hibernate-core', version: '3.6.7.Final'
"group:name:version"
compile 'org.hibernate:hibernate-core:3.6.7.Final'

https://www.tuicool.com/articles/yQ3IjeQ
从Android Studio3.0后compile引入库不在使用，而是通过api和implementation，api完全等同于以前的compile，用api引入的库整个项目都可以使用，用implementation引入的库只有对应的Module能使用，其他Module不能使用，由于之前的项目统一用compile依赖，导致的情况就是模块耦合性太高，不利于项目拆解，使用implementation之后虽然使用起来复杂了但是做到降低偶合兴提高安全性。

https://juejin.im/entry/59918304518825489151732d 
transitive = false

https://zhuanlan.zhihu.com/p/44371449
provided、api、apk、compileOnly、runtimeOnly、渠道名+Compile，差异主要在于构建内容和参与构建的时机.
compileOnly(provided)仅占位编译


https://blog.csdn.net/singwhatiwanna/article/details/78898113
type	task的“父类” 
// 定义一个名字为rygTask的task，属于renyugang分组，并且依赖myTask1和myTask2两个task。
project.task('rygTask', group: "renyugang", description: "我自己的Task", dependsOn: ["myTask1", "myTask2"] ).doLast {
    println "execute rygTask"
}

https://www.tuicool.com/articles/uMRni2n 
.jar 与 .aar的区别
从名称上来讲，一个是 java application resource ；一个是 android application resource 
如果aar中也引用了第三方的lib，那么打包后的aar中是没有把这些三方库加入到aar中的，这样就需要在app中除了引用.aar外还要引用其中需要的其他第三方库。


=======================================
https://www.jianshu.com/p/441c09607974 
AndroidStudio的使用

https://juejin.im/post/5bd5c42ce51d457a9b6c8387
AndroidStudio不用编译，阅读Android源码

https://developer.android.google.cn/studio/build/apk-analyzer

https://developer.android.google.cn/studio/command-line/adb#issuingcommands
adb命令
adb shell dumpsys window windows | grep -E 'mCurrentFocus'

https://developer.android.google.cn/studio/profile/android-profiler
利用 Android Profiler 测量应用性能














