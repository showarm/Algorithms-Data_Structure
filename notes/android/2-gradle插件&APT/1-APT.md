## 1 注解 http://www.cnblogs.com/swiftma/p/6838654.html
三个元注解：
@Target表示注解的目标,默认所有类型，@Override的目标是方法(ElementType.METHOD)，ElementType是一个枚举，其他可选值有：
TYPE：表示类、接口（包括注解），或者枚举声明
FIELD：字段，包括枚举常量
METHOD：方法
PARAMETER：方法中的参数
CONSTRUCTOR：构造方法
LOCAL_VARIABLE：本地变量
ANNOTATION_TYPE：注解类型
PACKAGE：包

@Retention表示注解信息保留到什么时候，取值只能有一个，默认为CLASS，类型为RetentionPolicy，它是一个枚举，有三个取值：
SOURCE：只在源代码中保留，编译器将代码编译为字节码文件后就会丢掉
CLASS：保留到字节码文件中，但Java虚拟机将class文件加载到内存时不一定会在内存中保留
RUNTIME：一直保留到运行时

@Inherited 元注解  可保留到子类

Annotation是一个接口，它表示注解。所有的注解类型，内部实现时，都是扩展的Annotation

#### 一个DI的例子： http://www.cnblogs.com/swiftma/p/6838654.html
```
public class ServiceA {

    @SimpleInject
    ServiceB b; // A依赖B
    
    public void callB(){
        b.action();
    }
}
public class ServiceB {
    public void action(){
        System.out.println("I'm B");
    }
}
// 使用 不能直接new A，通过SimpleContainer搞一层做点文章
ServiceA a = SimpleContainer.getInstance(ServiceA.class);
a.callB();

public static <T> T getInstance(Class<T> cls) {
    try {
        T obj = cls.newInstance();
        Field[] fields = cls.getDeclaredFields();
        for (Field f : fields) {
            if (f.isAnnotationPresent(SimpleInject.class)) {
                if (!f.isAccessible()) {
                    f.setAccessible(true);
                }
                Class<?> fieldCls = f.getType();// 这里是ServiceB
                f.set(obj, getInstance(fieldCls)); //又调了getInstance()拿到B对象
            }
        }
        return obj;
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
}
```

## 2 注解处理器 
https://www.race604.com/annotation-processing/
https://github.com/sockeqwe/annotationprocessing101 
API : javax.annotation.processing.AbstractProcessor
编译时注解处理器（Annotation Processor）是javac的一个工具。javac启动一个完整Java虚拟机来运行注解处理器。
在你的jar中，你需要打包一个特定的文件javax.annotation.processing.Processor到META-INF/services路径下。javac会自动检查和读取javax.annotation.processing.Processor中的内容，里面是你自定义的注解处理器。 
AutoService 注解处理器是Google开发的，用来生成META-INF/services/javax.annotation.processing.Processor文件的。

```
@AutoService(Processor.class)
public class FactoryProcessor extends AbstractProcessor {
    //空构造器  必须
    public MyProcessor MyProcessor(){
    }

    // 接收ProcessingEnvironment的工具
    @Override
    public synchronized void init(ProcessingEnvironment env){ 
        super.init(processingEnv);
        typeUtils = processingEnv.getTypeUtils();
        elementUtils = processingEnv.getElementUtils(); // 类的结构元素
        filer = processingEnv.getFiler(); // 生成文件用的
        messager = processingEnv.getMessager(); // 接收异常信息
    }

    // 支持自己定义的注解
    @Override
    public Set<String> getSupportedAnnotationTypes() { 
        Set<String> annotataions = new LinkedHashSet<String>();
        annotataions.add(Factory.class.getCanonicalName());
        return annotataions;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() { 
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annoations, RoundEnvironment env) {
            try {
      // Scan classes  遍历所有被注解了@Factory的元素
      for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(Factory.class)) {

        // Check if a class has been annotated with @Factory 保证元素是类
        if (annotatedElement.getKind() != ElementKind.CLASS) {
          throw new ProcessingException(annotatedElement, "Only classes can be annotated with @%s",
              Factory.class.getSimpleName());
        }

        // TypeElement 类
        TypeElement typeElement = (TypeElement) annotatedElement;
        // FactoryAnnotatedClass，封装单个被注解的类
        FactoryAnnotatedClass annotatedClass = new FactoryAnnotatedClass(typeElement);

        checkValidClass(annotatedClass);

        // FactoryGroupedClasses，注意是用Map映射单个类名和单个类FactoryAnnotatedClass
        FactoryGroupedClasses factoryClass =
            factoryClasses.get(annotatedClass.getQualifiedFactoryGroupName());
        if (factoryClass == null) {
          String qualifiedGroupName = annotatedClass.getQualifiedFactoryGroupName();
          factoryClass = new FactoryGroupedClasses(qualifiedGroupName);
          factoryClasses.put(qualifiedGroupName, factoryClass);
        }

        // Checks if id is conflicting with another @Factory annotated class with the same id
        factoryClass.add(annotatedClass);
      }

      // filer生成Java类，利用JavaPoet https://github.com/square/javapoet
      for (FactoryGroupedClasses factoryClass : factoryClasses.values()) {
        factoryClass.generateCode(elementUtils, filer);
      }
      factoryClasses.clear();
    } catch (ProcessingException e) {
      error(e.getElement(), e.getMessage());
    } catch (IOException e) {
      error(null, e.getMessage());
    }

    return true;
     }
}
```

## 3 APT: Annotation Process Tool 一个早于官方产生的非官方用于处理注解的Gradle plugin
（注解库ButterKnife／Dagger等等源码都靠它）
https://bitbucket.org/hvisser/android-apt/wiki/Migration  
http://www.littlerobots.nl/blog/Whats-next-for-android-apt/ 作者不再维护，建议使用官方版
之后 官方出了gradle的注解处理器插件 annotationProcessor
dependencies {
    compile 'com.google.dagger:dagger:2.0'
    annotationProcessor 'com.google.dagger:dagger-compiler:2.0' // 不用apt了  也不用apply apt插件了
}
另外，参数的配置方式：
apt  {
    arguments {
        eventBusIndex "org.greenrobot.eventbusperf.MyEventBusIndex"
    }
}
修改后 annotationProcessor 方式
defaultConfig {
        javaCompileOptions {
            annotationProcessorOptions {
                arguments = [ eventBusIndex : 'org.greenrobot.eventbusperf.MyEventBusIndex' ]
            }
        }
    }
    
## 4 abstractProcessor debug
https://www.tuicool.com/articles/eay6Zf6 

## 5 demo  生成findViewById()
https://gitee.com/charliec/ProcessorDemo/tree/master/app/build/generated/source/apt/debug
1 这个例子中API类ViewInjector都是生成的。
生成了两个类 
    private static final String INJECTOR_NAME = "ViewInjector";// API，ViewInjector.inject(this);
    private static final String GEN_CLASS_SUFFIX = "Injector";// activity对应生成类的后缀
    activity有几个，第二个类就对应几个
```
package linjw.demo.injector;
public class ViewInjector {
	public static void inject(linjw.demo.processordemo.SecondActivity arg) {
		linjw.demo.processordemo.SecondActivityInjector.inject(arg);
	}
	public static void inject(linjw.demo.processordemo.MainActivity arg) {
		linjw.demo.processordemo.MainActivityInjector.inject(arg);
	}
}
```
2 这是最原始的生成代码方式,直接拼的，然后JavaFileObject写文件。
StringBuilder append()
import javax.tools.JavaFileObject;
JavaFileObject file = mFiler.createSourceFile(className);

3 anroid-apt不再支持，官方annotationProcessor

## 6 demo  javapoet生成findViewById()  
https://github.com/brucezz/ViewFinder 
apply plugin: 'com.neenbedankt.android-apt'
dependencies {
    apt project(':viewfinder-compiler')  //android-apt是gradle插件
}
viewfinder-annotation 定义注解
viewfinder-compiler 注解处理器
    compile 'com.squareup:javapoet:1.7.0'
    compile 'com.google.auto.service:auto-service:1.0-rc2'
    利用这俩 根据自己定义的注解 生成Java类，比如这个demo里是XXActivity$$Finder 内部类有个inject方法
    JavaFile.builder(packageName, finderClass).build()
viewfinder 基于生成的Java类 写调用逻辑 对外提供API

1 API类ViewFinder是写好的，不是生成的。
调用生成类的 (className + "$$Finder").inject()
2 com.squareup.javapoet.TypeSpec生成类mClassElement.getSimpleName() + "$$Finder"，
里面有个inject()，进行findViewById();



