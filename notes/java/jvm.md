
https://zhuanlan.zhihu.com/p/21435008
getMethod和getDeclaredField方法会比invoke和set方法耗时

* 垃圾回收机制

http://www.tuicool.com/articles/6VRBr2A 

http://www.tuicool.com/articles/YBzUvqI 
静态内存，堆，栈内存。

GC对jvm的堆内存进行分代回收，年轻代、老年代、持久代分别处理，对应不同算法。存活时间短的年轻代是最常见的。（所以这种有点哈夫曼数的思想）

软引用：内存不够的时候，遇到了就清了。

弱引用：每GC可以回收

https://zhuanlan.zhihu.com/p/23068093 
https://www.zhihu.com/question/51920553/answer/128610039 


https://zhuanlan.zhihu.com/p/23297569 

