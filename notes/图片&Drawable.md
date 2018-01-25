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

## LruCache + DiskLruCache
http://blog.csdn.net/boyupeng/article/details/47127605
http://www.cnblogs.com/zyw-205520/p/4997863.html 


## Bitmap 
内存 http://bugly.qq.com/bbs/forum.php?mod=viewthread&tid=498&extra=page%3D3 

取决于：色彩格式／存放的资源目录／手机的屏幕密度

大图小用用采样，小图大用用矩阵

## Drawable  

// Canvas
Bitmap newImage = Bitmap.createBitmap(input.getWidth(), input.getHeight(), Bitmap.Config.ARGB_8888);
Canvas canvas = new Canvas(newImage);
canvas.drawPath(path, paint);//然后再往上画点啥


https://github.com/mzule/FantasySlide/blob/master/library/src/main/java/com/github/mzule/fantasyslide/SideBarBgView.java 


圆形图片：

叠加模式：mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

路径裁剪画布：path.addCircle();canvas.clipPath(path);

着色器：
paint.setShader(new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)); 
c.drawCircle(radius,radius, radius, paint); //用图片着色器画了一个圆

自定义drawable  http://www.jianshu.com/p/f3abe913c07f  


## 压缩
 https://www.tuicool.com/articles/FNVzMbj 


## 图片内存优化
https://www.jianshu.com/p/5bb8c01e2bc7
将背景图片通过SurfaceView来绘制，这样相当于是在非UI线程绘制，不会影响到UI线程做其它事情：