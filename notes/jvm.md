* 垃圾回收机制

http://www.tuicool.com/articles/6VRBr2A 

http://www.tuicool.com/articles/YBzUvqI 

GC对jvm的堆内存进行分代回收，年轻代、老年代、持久代分别处理，对应不同算法。存活时间短的年轻代是最常见的。（所以这种有点哈夫曼数的思想）

软引用：内存不够的时候，遇到了就清了。

弱引用：每GC可以回收
