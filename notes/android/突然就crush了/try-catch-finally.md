https://www.tuicool.com/articles/iiyIj2U

1 一个方法，异常捕获块中，不同的地方的 return 语句，谁会生效？
假设 finally 代码块中存在 return 语句，则直接返回，它是优先级最高的。
一般我们不建议在 finally 代码块中添加 return 语句，因为这会破坏并阻止异常的抛出，导致不宜排查的崩溃。

2 catch 和 finally 中出现异常，会如何处理？

3 try-catch 是否影响效率？
在没有发生异常的情况下，try-catch 对性能的影响微乎其微。但是一旦发生异常，性能上则是灾难性的。

4 Java 异常捕获的原理？

5 try 语句里的方法，如果允许在另外一个线程中，其中抛出的异常，是无法在调用者这个线程中捕获的。


## UncaughtExceptionHandler
https://www.tuicool.com/wx/3yU3IvA