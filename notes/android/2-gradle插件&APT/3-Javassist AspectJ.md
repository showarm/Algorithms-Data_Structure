
## AspectJ
https://www.jianshu.com/p/d43807ac33bd
    implementation 'org.aspectj:aspectjrt:1.8.9'
import org.aspectj.lang.annotation.Around;

1.定义注解
2.添加入口plugin或者直接写在gradle里
3.定义切片，设置@Pointcut使用execution来设置方法的切入点为com.app.annotation.aspect包下的CheckLogin
4.编写切片处理逻辑在Advice里，Advice就是我们插入的代码可以以何种方式插入，有Before 还有 After、Around
5.在项目里使用切片，达到在指定位置插入代码的目的，可以在具体项目里面同一种场景使用该注解达到处理切面的问题，大大减少了代码的书写，更加是AOP的具体体现，对OOP的一种弥补

## Javassist demo1
https://www.jianshu.com/u/1c329c6da2b7
https://www.jianshu.com/p/a6be7cdcfc65
1 Javassist作用是在编译器间修改class文件，首先我们得知道什么时候编译完成，赶在class文件被转化为dex文件之前去修改编译后的class文件。
在Transfrom这个api出来之前，想要在项目被打包成dex之前对class进行操作，必须自定义一个Task，然后插入到predex或者dex之前，在自定义的Task中可以使用javassist或者asm对class进行操作。
而Transform则更为方便，Transfrom会有他自己的执行时机，不需要我们插入到某个Task前面。Tranfrom一经注册便会自动添加到Task执行序列中，并且正好是项目被打包成dex之前。
```
// 注册Transform,固定写法。
        def classTransform = new JavassistTransform(project);
        android.registerTransform(classTransform);
// 注入代码
import com.android.build.api.transform.Transform;
public class JavassistTransform extends Transform 
    @Override
    public void transform(Context context,
                          Collection<TransformInput> inputs,
                          Collection<TransformInput> referencedInputs,
                          TransformOutputProvider outputProvider,
                          boolean isIncremental) throws IOException, TransformException, InterruptedException {
    CtMethod ctMethod = ctClass.getDeclaredMethod("onCreate")
    String insetBeforeStr = """ android.widget.Toast.makeText(this,"WTF emmmmmmm.....我是被插入的Toast代码~!!",android.widget.Toast.LENGTH_LONG).show();
    ctMethod.insertBefore(insetBeforeStr);
                          }

```
2 理解BuildConfig.java的生成过程。

## 二者对比
AspectJ借助注解找到切入点，Javassist直接遍历classpath中的文件找方法名
