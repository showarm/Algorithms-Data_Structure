
## APK的安装过程
https://blog.csdn.net/bupt073114/article/details/42298337

/data/app：安装时，把APK拷贝于此。
/data/data：安装完成后，在/data/data目录下自动生成和应用包名一样的文件夹，用于存放应用程序的数据。
/data/dalvik-cache：存放APK的odex文件，便于应用启动时直接执行。

## 65535

当Android系统安装一个应用的时候，有一步是对Dex进行优化，这个过程有一个专门的工具来处理，叫DexOpt。DexOpt的执行过程是在第一次加载Dex文件的时候执行的。这个过程会生成一个ODEX文件，即Optimised Dex。执行ODex的效率会比直接执行Dex文件的效率要高很多。 但是在早期的Android系统中，DexOpt有一个问题，DexOpt会把每一个类的方法id检索起来，存在一个链表结构里面。但是这个链表的长度是用一个short类型来保存的，导致了方法id的数目不能够超过65536个。当一个项目足够大的时候，显然这个方法数的上限是不够的。尽管在新版本的Android系统中，DexOpt修复了这个问题，但是我们仍然需要对低版本的Android系统做兼容。 为了解决方法数超限的问题，需要将该dex文件拆成两个或多个，为此谷歌官方推出了multidex兼容包
