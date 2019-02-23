
### Object的8个方法

对象本身的类，id，描述，GC，对比，克隆，线程的wait，notify。

public class Object {

    private static native void registerNatives();
    static {
        registerNatives();
    }

    /**
    返回当前对象的运行时class对象。
    **/
    public final native Class<?> getClass();

      /**
      HashCode原则：
      1 每次调用一个对象的hashcode都返回同一个int值
      2 如果两个对象equals()，那它们必须有相同的hashCode
      3 如果两个对象不equals()，那他们的hashcode无所谓，但建议不相同，这对Hash表的性能有帮助。
      Object的hashCode()方法返回的是对象内存地址的int值，子类可做其他实现。
      **/
      public native int hashCode();

      /**
      一段可读的代表此object的字符串。建议所有子类重写此方法。
      默认是，类名@hashCode的十六进制。hashCode默认是内存地址。
      **/
      public String toString() {
        return getClass().getName() + "@" + Integer.toHexString(hashCode());
    }

    /**
    GC时调用此方法释放空间，一个对象只会调用一次。
    子类自行实现此方法逻辑，但是如果中间发生异常，则该对象的空间释放被中断。
    */
    protected void finalize() throws Throwable { }
    
      /**
      equals原则：
      对与non-null object ：
      1 reflexive反身性，x.equals(x)为true
      2 symmetric对称性，若x.equals(y)为true，则y.equals(x)也为true
      3 transitive传递性，若x.equals(y)，y.equals(z)为true，则x.equals(z)也为true
      4 consistent一致性，x.equals(y)返回恒定值，除非对象被修改
      5 x.equals(null)为false。
      **/
      public boolean equals(Object obj) {
            return (this == obj);
      }

      /**  
      拷贝由具体类实现，一般原则是：
      1 x.clone().equals(x)    //true
      2 x.clone() != x        //true，与HashCode原则冲突。
      3 x.clone().getClass() == x.getClass()  //true 
      还有，一个类没有实现Cloneable接口，调用其对象的clone()方法会CloneNotSupportedException。
      Object类就没有实现。
      ？？copy其实就两种方法：new新对象，修改this对象的某些字段。这个方法我很少用到，自行实现确实没必要覆盖clone方法，这是Java设计的败笔吗？？
      **/
      protected native Object clone() throws CloneNotSupportedException;

    /**
    synchronized (obj)
    线程持有当前对象为锁，调用此方法阻塞自身执行。
    另一个线程notify或interrupt或过了timeout毫秒，则阻塞解除。
    wait一定要放在条件循环里，防止spurious wakeup（自动解除阻塞）。

    源码的注释里提到两本书：
     * (For more information on this topic, see Section 3.2.3 in Doug Lea's
     * "Concurrent Programming in Java (Second Edition)" (Addison-Wesley,
     * 2000), or Item 50 in Joshua Bloch's "Effective Java Programming
     * Language Guide" (Addison-Wesley, 2001).
    */
    public final void wait() throws InterruptedException {
        wait(0); //timeout为0就是一直阻塞，等着被动notify
    }
    public final native void wait(long timeout) throws InterruptedException;
    public final void wait(long timeout, int nanos) throws InterruptedException {
        if (timeout < 0) {
            throw new IllegalArgumentException("timeout value is negative");
        }
        if (nanos < 0 || nanos > 999999) {
            throw new IllegalArgumentException("nanosecond timeout value out of range");
        }
        if (nanos >= 500000 || (nanos != 0 && timeout == 0)) {
            timeout++;
        }
        wait(timeout);
    }
    /**
    当前线程发出notify信号，一个wait的线程会解除阻塞，不确定哪一个，然后等到当前线程释放锁，它开始继续抢占CPU执行。
    */
    public final native void notify();
    public final native void notifyAll();
}

