
https://github.com/xyzxqs/XLowPoly  Low poly 小多边形，JNI版  
1 

int pixels[] = new int[width * height];

bitmap.getPixels(pixels, 0, width, 0, 0, width, height); 

把bitmap的像素数据放到一个二维数组，那么数据是个啥？
```
Bitmap.Config.ARGB_8888:每个通道占一个字节，8位
int color = pixel[w * y + x]; //这就是图片矩阵里每个像素位的数据，
    int blue = color & 0xFF;
    int green = (color >> 8) & 0xFF;
    int red = (color >> 16) & 0xFF;
#ifdef ALPHA_CHANNEL
    int alpha = (color >> 24) & 0xFF;
    float a = alpha / 255.0f;
    float sum = (blue + green + red) * a;  //alpha通道的意义原来是颜色通道的比重，不是平等待遇
    return (int) (sum / 3);
#else
    return (blue + green + red) / 3;

```
2 sobel.c 索貝爾算子（Sobel operator），是圖像處理中的算子之一，主要用作邊緣檢測。
```
int const sobelOperatorX[3][3] = {{-1, 0, 1},
                                  {-2, 0, 2},
                                  {-1, 0, 1}};


    int pixelX = (
            (sobelOperatorX[0][0] * color_average(pixel, w, h, x - 1, y - 1)) +
            (sobelOperatorX[0][1] * color_average(pixel, w, h, x, y - 1)) +
            (sobelOperatorX[0][2] * color_average(pixel, w, h, x + 1, y - 1)) +

            (sobelOperatorX[1][0] * color_average(pixel, w, h, x - 1, y)) +
            (sobelOperatorX[1][1] * color_average(pixel, w, h, x, y)) +
            (sobelOperatorX[1][2] * color_average(pixel, w, h, x + 1, y)) +

            (sobelOperatorX[2][0] * color_average(pixel, w, h, x - 1, y + 1)) +
            (sobelOperatorX[2][1] * color_average(pixel, w, h, x, y + 1)) +
            (sobelOperatorX[2][2] * color_average(pixel, w, h, x + 1, y + 1))
    );

```
第一列 乘以 第一行，像素矩阵跟sobelOperatorX相乘，

3 捋一遍这几步

* lowpoly-lib.cpp :  get_triangles

* sobel(pixels, w, h, &call, collectors, &size_collectors);  得到图像的边缘像素矩阵，元素是边缘像素点坐标

* dilution(collectors, size_collectors, w, h, alpha, vertices, &size_vertices); 过滤得到顶点坐标数组 稀释,把点变少同时保留大致的轮廓，抽签点名的方法，这种缺点是有可能抽到相同的点。

* dedup(vertices, &size_vertices);  再次去重过滤

* triangulate(vertices, size_vertices, w, h, triangles); //所有三角形顶点索引的链表图状结构  三角剖分Delaunay

* 最后到LowPoly.java :
int[] triangles = getTriangles(pixels, width, height, threshold, alphaOrPointCount, lowPoly);
这个一维数组所有三角形顶点x,y坐标的顺序排列，每6个数就是一个三角形。

判断点是不是在三角形圆内：in_circumcircle



https://github.com/zzhoujay/LowPolyAndroid  Low poly 小多边形，android版

https://github.com/zzhoujay/LowPoly  Low poly 小多边形，java版


