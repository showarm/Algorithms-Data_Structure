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


### 内存泄漏

https://juejin.im/entry/56d64b9e816dfa005943a55c 

什么情况导致内存泄漏？

资源对象没关闭,AttrSet，Cursor，File，Bitmap.recycle()

持有context相关的内存泄漏

注册没取消

集合中对象没清理


腾讯bugly也推出了三篇关于Android内存泄漏调优的文章：

内存泄露从入门到精通三部曲之基础知识篇
内存泄露从入门到精通三部曲之排查方法篇
内存泄露从入门到精通三部曲之常见原因与用户实践
Realm同样给出了性能优化文章：（真心佩服）
10条提升Android性能的建议