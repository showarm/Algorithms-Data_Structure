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


## Drawable  

// Canvas
Bitmap newImage = Bitmap.createBitmap(input.getWidth(), input.getHeight(), Bitmap.Config.ARGB_8888);
Canvas canvas = new Canvas(newImage);
canvas.drawPath(path, paint);//然后再往上画点啥


https://github.com/mzule/FantasySlide/blob/master/library/src/main/java/com/github/mzule/fantasyslide/SideBarBgView.java 


paint.setShader(createShader());
new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);

## Matrix

android中拥有Matrix属性的API列表（或者说，其变换是操作Matrix的API）：

最终的目的地：着色器／画布

android.graphics.Shader    画笔的颜色着色器或图像印章 #setLocalMatrix(Matrix localM)  ..

android.graphics.Canvas   画布#setMatrix(Matrix matrix)哈哈，明显不

android.graphics.Camera   #applyToCanvas(Canvas canvas) camera依附于画布，它让画布进行矩阵变换

android.view.View  View的各种属性变换，大小位置scroll之类，都是在进行Matrix操作,它其实是canvas的变换

android.widget.ImageView  #setImageMatrix(Matrix matrix)各种ScaleType就是在操作这个mDrawMatrix，画布变换。

android.animation.Transformation  所有view动画都在操作Transformation的Matrix，最后这个矩阵作用到哪了？view



