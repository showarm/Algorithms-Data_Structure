
## 
https://www.jianshu.com/p/eda0bfd692e6
利用lint自动删除无用资源：先执行lint任务，通过解析生成的xml文件，找到id为UnusedResources的文件路径，并遍历删除
打个广告，写个小玩意lint自定义规则，扫描一些开发问题
1、判断bean类必须在bean、model目录下（混淆）
2、retrofit的net接口必须定义在net目录下(混淆)
3、bean类成员、内部类等必须实现Serializable （序列化崩溃）
4、调用LJImageLoader必须执行dontAnimate方法 （图片拉伸）
5、xml布局文件引用了未知类(crash）
集成方式debugImplementation 'com.lianjia.lintrules:lib_lintrules:1.0.0-SNAPSHOT@aar' ， 执行./gradlew lint

## Plugin插件
https://juejin.im/post/5c3077d7e51d45523f04a340
buildSrc文件夹
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.DefaultTask

## 发布到本地和远程仓库
https://www.jianshu.com/p/376ae605ebf1 

## Demo1 ImplLoader 跨module加载
https://github.com/SusionSuc/AdvancedAndroid/tree/master/gradle%E6%8F%92%E4%BB%B6%E4%B8%8E%E5%AD%97%E8%8A%82%E7%A0%81%E6%B3%A8%E5%85%A5 
https://www.jianshu.com/p/099b9f7c5538
Gradle插件、注解、javapoet和asm实战 .module1想实例化一个module2的类,要怎么解决呢？

使用方式：给要暴露的类加注解和值，"reuse_fragment"
        ImplLoader.init()
        val reuseFragmet = ImplLoader.getImplInstance<Fragment>("reuse_fragment")
        
1 compiler：注解处理器库，
key-value  注解name值 - 被注解类class
给每个有注解的类生成 ImplInfo_xxxx.java 里面有个方法
```
    public static void init(){
        //调用 ImplLoader.registerImpl(key,value) k-v传进来，让ImplLoader记录
    }

```
2 interface：定义注解 Impl

3 loadercore：API库  ImplLoader
init() 调用ImplLoaderHelp.init()
registerImpl(implName: String, implClass: Class<*>) map记录 注解值-被注解类
getImplInstance(implName: String) 从map取class，返回实例
getView(context: Context, viewName: String)

4 loaderplugin: gradle插件代码
org.gradle.api.Plugin
com.android.build.api.transform.Transform
org.objectweb.asm.ClassWriter

扫描的 ImplInfo类的个数
修改 ImplLoader.init() 
生成 ImplLoaderHelp

## Demo2  handle Android's click debounce
https://github.com/SmartDengg/asm-clickdebounce


