java code style 
http://www.hawstein.com/posts/google-java-style.html

java8
https://zhuanlan.zhihu.com/p/23279894 
stream
https://win-man.github.io/2016/10/21/%E8%AE%B2%E8%AE%B2Java8%E4%B8%AD%E7%9A%84%E6%B5%81/ 


http://mp.weixin.qq.com/s?__biz=MzI3MzEzMDI1OQ==&mid=2651815337&idx=1&sn=8e846e11e908735a5175c9eacb642329 

http://developer.android.com/reference/java/lang/Class.html
类本身，类名等
方法
成员变量
构造器

反射Refliction
https://zhuanlan.zhihu.com/p/21423208  
http://www.tuicool.com/articles/bEJbay 

http://www.imooc.com/video/3725
1 Class类
类是对象，类是java.lang.Class类的实例对象。java class
There a class named Class.
//只有jvm才能new Class()
private Class() {
        // Prevent this class from being instantiated,
        // instances should be created by the runtime only.
    }
那么，
－－任何类都有一个隐含的静态成员	class对象
Class c1 = Foo.class;
－－通过该类的实例
Class c2 = foo1.getClass();
Class对象 c1,c2表示Foo类的类类型(class type ),就是你定义的类是哪种CLass
一个类只能是Class的一个对象，所以，c1 == c2 == c3   true
－－
Class c3 = null;
c3 = Class.forName(“com.xxx.Xxx”);
完全可以通过类类型创建类对象
Foo foo2 = c1.newInstance();	//需要有无参构造器
2	动态加载类
编译时加载类，是静态加载类
运行时加载类，是动态加载类
编译     javac  Xxx.java
new 创建对象是静态加载类，在编译时就需要加载所有可能用到的类
比如，new Xxx();但是并没有定义Xxx类，所以编译不通过，编译时就会ClassNotFound
Class c = Class.forName(“com.xxx.Xxx”);//返回类类型，是动态加载类
如果Xxx.java不存在,编译可以通过，运行时会ClassNotFound
c.newInstance();//得到对象
根据类型要得到不同的对象的场景，避免了if else类型判断
3 方法信息
double.class
Double.class
String.class 	//String类的类类型，String类字节码
void.class
http://developer.android.com/reference/java/lang/reflect/Method.html
public static void getMsg(Object obg){
	Class c = obj.getclass();	//得到的是obj类的类类型，就是Object的具体子类
	c.getMethods() ; 	//该类所有public方法的Method对象，父类的public当然也是它的
	c.getDeclaredMethods(); //该类声明的所有方法的Method对象。
}
Method		返回值／方法名／参数
5
A a = new A();
Class c = A.class;
Method add = c.getMethod(String name, Class...<?> parameterTypes) //方法名，参数类类型
c.invoke(a,10,20);	//调用者（静态方法为null）／实参
					//void方法返回null,
4 成员变量	构造器
java.lang.reflect.Field
http://developer.android.com/reference/java/lang/reflect/Field.html
getFields() //所有public的
6	通过反射了解范型的本质
ArrayList a1 = new ArrayList();
ArrayList<String> a2 = new ArrayList();
a2.add(20)		//报错
a1.getClass() == a2.getClass();	//true
－－－反射的操作都是在编译之后，即运行时，也就说明，编译之后，集合类是去范型化的。
－－－java中的范型是为了防止错误类型输入（控制输入类型），只在编译时有效，绕过编译就无效了。
－－－那就在运行时验证下，
Method add = a2.getClass.getMethod(“add”,Object.class);
add.invoke(a2,20);
printf(a2.get(0));		//haha
———
 可变参数
public Object invoke (Object receiver, Object... args)
…   即可以传 new Object{o1,o2}   也可以  o1,o1
Java注解		Annotation
http://developer.android.com/reference/java/lang/annotation/Annotation.html
http://www.imooc.com/learn/456
2 jdk中的注解
@Override	覆盖
@Deprecated	过时
@SuppressWarnings(“deprecation”)	忽略
这3个都是编译时注解，编译器会按照它们的指令来编译
3 注解分类
http://developer.android.com/reference/java/lang/annotation/RetentionPolicy.html
－－按运行机制，生命周期
源码注解	RetentionPolicy. SOURCE 注解只在源码中，编译为.class就没了
编译时	RetentionPolicy. CLASS  在源码和.class文件
运行时	RetentionPolicy. RUNTIME	源码／.class/运行时都存在
－－元注解		注解的注解
4 自定义注解
@interface
//成员类型：基本类型，String，Class，Annotation,Enumeration
//成员无参无异常
// 如果只有一个成员，则必是value(),使用时可以忽略成员名和＝
//可以没有成员，叫做标示注解
http://grepcode.com/file/repository.grepcode.com/java/root/jdk/openjdk/8u40-b25/java/lang/Override.java#Override 就是标示注解

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface Description {
	String desc();//成员无参无异常
	int age() default 18;//用default为成员指定默认值
}
＝＝＝＝＝上面一堆元注解
@Target(ElementType.METHOD)
作用域
http://developer.android.com/reference/java/lang/annotation/ElementType.html
--TYPE 	Class, interface or enum declaration
@Retention(RetentionPolicy.SOURCE)生命周期
@Inherited	允许子类继承	使用者的继承关系
@Documented生成javaDoc时会包含注解信息
4-2 使用
@名字（成员1＝值）
@Description(desc=“this is XXX”,age=9)
Class A{}
4-4 反射注解
Class c = Class.forName(“A”);
c.isAnnotationPresent(Description.class);
Description d = (Description)c.getAnnotation(Description.class);
＝＝＝＝反射完有没有值还是由@Retention(RetentionPolicy   决定。RetentionPolicy.SOURCE的话，class就没有注解了
＝＝＝＝@Inherited
1 只作用在类上。类上的注解子类也有，方法上的子类没有
2 继承有效，接口实现无效
5 实例
一个注解＋反射的DB思维（肯定耗性能，学下注解）
http://www.imooc.com/video/8876



NIO
Java NIO系列教程
http://www.importnew.com/tag/java-nio
https://zhuanlan.zhihu.com/p/23488863 


http://tutorials.jenkov.com/java-nio/index.html
http://www.importnew.com/18908.html

位操作
http://www.tuicool.com/articles/YvayAbe 
http://www.importnew.com/22378.html 

http://x-goder.iteye.com/blog/2036364?utm_source=tuicool&utm_medium=referral

进制
http://www.th7.cn/Program/java/201510/646994.shtml 
/**
 *题目：实现一个函数，输入一个整数，输出该数二进制表示中的1的个数。
 *例如把9改成二进制是1001，有2位是1.因此如果输入9，该函数输出2. 
 */
package swordForOffer;

/**
 * @author JInShuangQi
 *
 *         2015年7月30日
 */
public class E10NumberOf1InBinary {
  public int numberOf1(int num) {
    int count = 0;
    while (num != 0) {
      count++;
      num = num & (num - 1);

    }
    return count;
  }

  public static void main(String[] args) {
    E10NumberOf1InBinary test = new E10NumberOf1InBinary();
    System.out.println(test.numberOf1(9));
  }
}

相邻的两个数
xx00  xx01  xx10  xx11
大 － 小 ＝ 1
大  & 小 ，每次，大数丢了一个1




2^10 = 1024
  Integer
/**
* Constant for the maximum {@code int} value, 2<sup>31</sup>-1.
*/
public static final int MAX_VALUE = 0x7FFFFFFF; 2^31  - 1

/**
* Constant for the minimum {@code int} value, -2<sup>31</sup>.
*/
public static final int MIN_VALUE = 0x80000000;  - 2^31

http://www.importnew.com/22378.html 
＝＝android.view.ViewPropertyAnimator
private static final int TRANSLATION_X  = 0x0001;
private static final int TRANSLATION_Y  = 0x0002;
private static final int SCALE_X        = 0x0004;
private static final int SCALE_Y        = 0x0008;
private static final int ROTATION       = 0x0010;   2<<4
private static final int ROTATION_X     = 0x0020;     2<<5
private static final int ROTATION_Y     = 0x0040;
private static final int X              = 0x0080;
private static final int Y              = 0x0100;
private static final int ALPHA          = 0x0200;
／／因为上面这一堆都是2左移的数

private static final int TRANSFORM_MASK = TRANSLATION_X | TRANSLATION_Y | SCALE_X | SCALE_Y |
       ROTATION | ROTATION_X | ROTATION_Y | X | Y;

TRANSFORM_MASK能用来检测一个数是不是它们中的一个。
if ((propertyMask & TRANSFORM_MASK) != 0) {／／不等于0 那就是其中的一个了
   mView.invalidateViewProperty(false, false);
}







枚举类
Techniques.values()[position]




为什么Java中1000==1000为false而100==100为true？
http://www.codeceo.com/article/why-java-1000-100.html
http://www.cnblogs.com/fjhh/p/5370666.html
http://blog.csdn.net/abing37/article/details/5332798 
http://www.cnblogs.com/fjhh/p/5370666.html 



https://zhuanlan.zhihu.com/p/23390311 



