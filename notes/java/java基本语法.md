https://github.com/Mr-YangCheng/ForAndroidInterview/tree/master/java
https://github.com/francistao/LearningNotes/tree/master/Part1/Android
https://github.com/singwhatiwanna/android-art-res
https://github.com/YoungPeanut/ApiDemos
https://www.jianshu.com/u/f9fbc7a39b36
http://blog.csdn.net/harvic880925/article/details/50995268

# Object的8个方法
 为什么重写equals方法必须也重写hashCode方法？
== 比较对象的地址

equals计较对象的内容，是判断两个对象相等的标尺。

两个对象相等 <=>  equals()相等  => hashCode()相等   为了满足这个约定，必须也重写hashCode()

hasCode()不相等 => equals（）不相等 <=> 两个对象不相等


* 接口 抽象类  内部类 的对比  

http://www.cnblogs.com/chenssy/p/3388487.html  

 1、内部类可以用多个实例，每个实例都有自己的状态信息，并且与其他外围对象的信息相互独立。

      2、让多个内部类以不同的方式实现同一个接口，或者继承同一个类。－－多继承

      3、创建内部类对象的时刻并不依赖于外围类对象的创建。

      4、内部类并没有令人迷惑的“is-a”关系，他就是一个独立的实体。

      5、内部类提供了更好的封装，除了该外围类，其他类都不能访问。

非静态内部类在编译完成之后会隐含地保存着一个引用，该引用是指向创建它的外部类，但是静态内部类却没有

volatile  transient



String 是不可变的对象，每次改变都等同于生成了一个新的 String 对象。

stringbuffer每次结果都会对 StringBuffer 对象本身进行操作，而不是生成新的对象，具有字符串缓冲区。

stringbuilder，非同步，优先使用它。



Error 和 Exception的区别 http://www.tuicool.com/articles/UNvyqae 

Throwable派生出两大类：Error和Exception。

Error是程序中的严重错误，不应该用try…catch

Exception分两类：Checked Exception和Unchecked Exception(RuntimeException)。


https://blog.csdn.net/zhangjg_blog/article/details/16116613#t1 Java中的数组是对象吗？