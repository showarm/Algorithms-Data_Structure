## LruCache + DiskLruCache
http://blog.csdn.net/boyupeng/article/details/47127605
http://www.cnblogs.com/zyw-205520/p/4997863.html   volley三级


/Users/chenshao/dev/Android/android-sdk-macosx/sources/android-26/android/util/LruCache.java
/Users/chenshao/dev/Android/android-sdk-macosx/sources/android-26/java/util/LinkedHashMap.java
```
private final LinkedHashMap<K, V> map;
this.map = new LinkedHashMap<K, V>(0, 0.75f, true);

        synchronized (this) {
            putCount++;
            size += safeSizeOf(key, value);
            previous = map.put(key, value);
            if (previous != null) {
                size -= safeSizeOf(key, previous);
            }
        }
```
1 背后是LinkedHashMap，LinkedHashMap底层是： 
key-value的哈希映射 + 双链表的Entry；前者加快查找，后者加快增删。

    static class LinkedHashMapEntry<K,V> extends HashMap.Node<K,V> {
        LinkedHashMapEntry<K,V> before, after;
        LinkedHashMapEntry(int hash, K key, V value, Node<K,V> next) {
            super(hash, key, value, next);
        }
    }
2 线程安全，以当前LruCache对象为锁


## Glide

https://www.tuicool.com/articles/beQjMnz

```
GlideApp.with(this)
                .load(uri)
                .into(imageViewLookup);

```

Glide.with : 同步生命周期,Glide的生命周期与这个子fragment的声明周期绑定

加载图片，如果请求相同, 而且当前请求设置可以使用内存缓存.


https://www.tuicool.com/articles/vueaQjM

硬盘缓存 + 内存缓存

内存缓存分为两级
LruCache缓存：不在使用中的图片使用LruCache来进行缓存。LinkedHashMap（双向循环表）
弱引用缓存：把正在使用中的图片使用弱引用来进行缓存。 
【这样的目的保护正在使用的资源不会被LruCache算法回收。】

Glide用到了大量的抽象工厂类
