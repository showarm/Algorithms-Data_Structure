
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


### 圆形图片：

叠加模式：mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

路径裁剪画布：path.addCircle();canvas.clipPath(path);

着色器：
paint.setShader(new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)); 
c.drawCircle(radius,radius, radius, paint); //用图片着色器画了一个圆

### 自定义drawable 
 http://www.jianshu.com/p/f3abe913c07f  


## 压缩
 https://www.tuicool.com/articles/FNVzMbj 


## 图片内存优化
https://www.jianshu.com/p/5bb8c01e2bc7
将背景图片通过SurfaceView来绘制，这样相当于是在非UI线程绘制，不会影响到UI线程做其它事情：