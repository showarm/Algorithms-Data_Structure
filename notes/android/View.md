### View-api

![api](./imgs/view_api.png)

典型的attr：

scrollX  
translationX  
elevation  ／／ViewCompat.setElevation(this, MiscUtils.dpToPixel(getContext(), 8));

### 源码浅读

0. 构造器 初始化  
```
    mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    mRenderNode = RenderNode.create(getClass().getName(), this);／／mRenderNode记录了View的渲染数据，比如matrix／scaleX／alpha／left／bottom／elevation／translationZ／attach装饰／Shadow
```
通过TypedArray得到xml里定义的属性值，比如background/padding／scrollX／translationX等等，并应用之，如setBackground(background);／scrollTo(x, y);／setTranslationX(tx);等等

－涉及到child的功能都是ViewGroup的
－任何一个View都在ViewGroup里，就形成来measure/layout/draw的层级调用
－View有这么多API方法，就有这么多维度，多维度组合变换，再加上相应的工具类，便可兴风作浪。。。
－attach，focus，draw等等都是事件，分发，到达，dispatch，on，套路都是套路

VG的构造器


1. onMeasure(int, int)
首先，View默认的大小是background的size。然后可以重写onMeasure方法自行提供大小。
onMeasure(int, int)由measure()调用，measure()由父View(就是一个ViewGroup)调用和提供宽高参数。
也就是：
父View调用子View的measure()，子View重写onMeasure(int, int)方法。
注意点：
子View必须调用setMeasuredDimension(int, int)来保存自己想要的大小。
子View提供的大小至少要getSuggestedMinimumHeight那么大。
工具：
View#getDefaultSize(); //比如重写onMeasure又懒得测量宽度，就用它了，系统就是这么干的
resolveSize／resolveSizeAndState(int size, int measureSpec, int childMeasuredState) // 第一个参数是你想要多大。返回MeasureSpec限制下的尺寸

MeasureSpec  //View的内部类，包装了父View传给子View的宽高参数。
一个MeasureSpec数值是int（to reduce object allocation），是size 和 mode的位操作（(size & ~MODE_MASK) | (mode & MODE_MASK)）。
模式有三种：UNSPECIFIED父View没限制，子View随意大小／
EXACTLY父View指定了子View的大小／
AT_MOST随意大小
int makeMeasureSpec(int size, int mode) 创建类，返回int ,MeasureSpec就是int值。。。
int getMode
int getSize

基本上重写onMeasure的注意目的就是为了处理padding，一个典型的写法：
```
@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int dx = 0;
        int dy = 0;
        dx += getPaddingLeft() + getPaddingRight() + getCurrentWidth();
        dy += getPaddingTop() + getPaddingBottom() + getCurrentHeight();
        final int measureWidth = resolveSizeAndState(dx, widthMeasureSpec, 0);
        final int measureHeight = resolveSizeAndState(dy, heightMeasureSpec, 0);
        setMeasuredDimension(Math.max(getSuggestedMinimumWidth(), measureWidth), Math.max(getSuggestedMinimumHeight(), measureHeight));
    }

    @Override
protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    //Get the width measurement
    int widthSize = View.resolveSize(getDesiredWidth(), widthMeasureSpec);

    //Get the height measurement
    int heightSize = View.resolveSize(getDesiredHeight(), heightMeasureSpec);

    //MUST call this to store the measurements
    setMeasuredDimension(widthSize, heightSize);
}

// 正方形
@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthSpecMode == MeasureSpec.AT_MOST
                && heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(200, 200);
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(200, heightSpecSize);
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, 200);
        }
    }
```

获取View宽高
VIew.post()//getWidth()
Activity//onAttachWindow
ViewTreeObserve.addOn...

也就是说，系统通过onMeasure来询问你宽高的数据

VG的onMeasure


2. onLayout(boolean changed, int left, int top, int right, int bottom)／／View的onLayout方法是空的，参数是layout()传进来的
一般是ViewGroup重写，来决定子View
layout(int l, int t, int r, int b)／／参数是相对于父View的左上右下，单位是像素
父View调用子View的的layou()，layout()会触发onLayout()

工具
addOnLayoutChangeListener(OnLayoutChangeListener listener)／／layout()执行的时候会触发监听器回调
更多参见ViewGroup的讲解
也就是说，系统会通过onLayout给你位置（相当于左上点和右下点在父View坐标系里的坐标数据）

getTop    view左上角与父view之间的距离    
相对于屏幕左上角的坐标可以用 getRawX
getPaddingLeftWithForeground()

FrameLayout 的 onLayout:
```
@Override
protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    layoutChildren(left, top, right, bottom, false /* no force left gravity */);
}

void layoutChildren(int left, int top, int right, int bottom,
                              boolean forceLeftGravity) {
    final int count = getChildCount();

    // 可使用的父 view 的左边界范围，这里的边界是综合考虑了 foreground 和 padding的
    final int parentLeft = getPaddingLeftWithForeground();
    // 右边界。right 和 left 分别是指左右两边的 X 坐标
    // 两者相减，再减去右边的 padding 可以得到父 view 的右边界范围。
    final int parentRight = right - left - getPaddingRightWithForeground();

    // 与上面类似，就不重复写了
    final int parentTop = getPaddingTopWithForeground();
    final int parentBottom = bottom - top - getPaddingBottomWithForeground();

    // 遍历每一个 view，设置每一个 view 的位置。
    for (int i = 0; i < count; i++) {
        final View child = getChildAt(i);
        // 只操作可见 view
        if (child.getVisibility() != GONE) {
            final LayoutParams lp = (LayoutParams) child.getLayoutParams();

            // width 和 height 分别是 Measure 过后的 宽和高
            final int width = child.getMeasuredWidth();
            final int height = child.getMeasuredHeight();

            int childLeft;
            int childTop;

            int gravity = lp.gravity;
            if (gravity == -1) {
                gravity = DEFAULT_CHILD_GRAVITY;
            }

            final int layoutDirection = getLayoutDirection();
            final int absoluteGravity = Gravity.getAbsoluteGravity(gravity, layoutDirection);
            final int verticalGravity = gravity & Gravity.VERTICAL_GRAVITY_MASK;

            switch (absoluteGravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
                case Gravity.CENTER_HORIZONTAL:
                    // gravity 是水平居中的情况
                    // 左坐标的计算可以分为两部分
                    // 1. 可使用的父 view 的左边界范围 + 放置view的中间位置(父view可用范围 减去 view 宽度后的一半)
                    // 2. 移除右 margin 加上 左margin
                    childLeft = parentLeft + (parentRight - parentLeft - width) / 2 +
                    lp.leftMargin - lp.rightMargin;
                    break;
                case Gravity.RIGHT:
                    // 这里主要考虑的是强制从左排列，在开发者选项中可以进行设置。
                    // 这里就先不讨论这个。
                    if (!forceLeftGravity) {
                        childLeft = parentRight - width - lp.rightMargin;
                        break;
                    }
                case Gravity.LEFT:
                default:
                    // 默认情况，加上左 margin 就行。
                    childLeft = parentLeft + lp.leftMargin;
            }

            switch (verticalGravity) {
                case Gravity.TOP:
                    childTop = parentTop + lp.topMargin;
                    break;
                case Gravity.CENTER_VERTICAL:
                    // 垂直居中的情况，与上面类似，也不重复了。
                    childTop = parentTop + (parentBottom - parentTop - height) / 2 +
                    lp.topMargin - lp.bottomMargin;
                    break;
                case Gravity.BOTTOM:
                    childTop = parentBottom - height - lp.bottomMargin;
                    break;
                default:
                    childTop = parentTop + lp.topMargin;
            }
            // 最重要的地方，将计算得出的四个位置作为参数，设置进去。
            child.layout(childLeft, childTop, childLeft + width, childTop + height);
        }
    }
}
```

3 onSizeChanged(int w, int h, int oldw, int oldh)／／View的onSizeChanged方法是空的，参数是sizeChange传入
sizeChange(int newWidth, int newHeight, int oldWidth, int oldHeight)／／参数是新旧宽高，setTop(int top)设置相对于父View的top position，单位是像素／setBottom／setLeft／setRight
也就是说，系统会通过onSizeChanged给你宽高

4 onDraw(Canvas canvas)／／View的onDraw方法是空的，参数由draw传入
这个是大块，因为大多时候控件宽高位置初始化之后是不必变动的，我们要操作其内容。

draw(Canvas canvas)／／参数是父View传入，View就在这个canvas画布上渲染。UI＃invalidate()，非UI#postInvalidate()（背后还是handler）及调用二者之一的方法都会触发，
父view（ViewGroup）的dispatchDraw(c)－－drawChild(c)调到child.draw(c),画布就传进来了
                                        dispatchDraw(c)是谁调的？？？还是分发http://www.uml.org.cn/mobiledev/201211305.asp 
所以，Draw是有顺序的：
    Draw background
    如果需要渐变，保存canvas' layers，
    Draw view's content
    Draw children
    Draw fading渐变，复原layers
    Draw decorations，比如scrollbars

然后怎么draw的问题，有了Canvas再加上Paint，那大事就来了专劈一篇讲。
也就是说，系统通过onDraw问你如何绘制


5 onKeyDown／／通过实体键点击View
6 onKeyUp／／通过实体键点击View
7 onTrackballEvent／／通过轨迹球操作View

8 onTouchEvent(MotionEvent event)／／参数是动作事件（(mouse, pen, finger, trackball)），一般是触摸事件，来自dispatchTouchEvent，
父View的dispatchTouchEvent(MotionEvent event)调用子View的dispatchTouchEvent(MotionEvent event) 走到onTouchEvent

相关监听器
OnClickListener／OnLongClickListener 这都是从onTouchEvent动作中检测出来的，mPendingCheckForTap
OnTouchListener
相关set方法
```
public boolean dispatchTouchEvent(MotionEvent ev){
    boolean consume = false;
    if (onInterceptTouchEvent(ev)) {  //ViewGroup有,View没有
        consume = onTouchEvent(ev);
    }else{
        consume = child.dispatchTouchEvent(ev);
    }
    return consume;
}
```
简单来说，系统通过onTouchEvent给你触摸数据


9 onFocusChanged(boolean gainFocus, @FocusDirection int direction,  @Nullable Rect previouslyFocusedRect)／／参数：是否也得到焦点／当前view拿到焦点时，焦点转移方向FOCUS_UP前后上下左右／之前的焦点View的Rect        参数由requestFocus传入

requestFocus－handleFocusGainInternal－onFocusChanged
这个决定了InputMethodManage的focusIn
简单来说，系统通过onFocusChanged告诉你view焦点

10 onWindowFocusChanged(boolean hasWindowFocus)／／参数，view的window是否处于焦点
view的焦点：view的window和view都有焦点，view才能获取事件
window的焦点：一个获得输入焦点的view的window，在其上添加另一个window，这时，view的window失去焦点，但view不会失去焦点。
ViewGroups可以重写来管理子View的状态
简单来说，系统通过onWindowFocusChanged告诉你window焦点

11 onAttachedToWindow()／／在onDraw前执行
dispatchAttachedToWindow(AttachInfo info, int visibility)－onAttachedToWindow

12 onDetachedFromWindow()／／空方法，这时view没有surface了，会取消Invalidate，清理mRenderNode
dispatchDetachedFromWindow－onDetachedFromWindow

13 onWindowVisibilityChanged(@Visibility int visibility) ／／dispatchAttachedToWindow和dispatchDetachedFromWindow触发

告诉你，window manager是否让你的window显示，被遮挡不算。

＝＝一维突破
0 构造
  周边类 
1 我只Measure
－－一个正方型view
－－listview嵌套listview测不准解决
周边类 MeasureSpec
一些套路：怎么拿到宽高
2 我只layout

3 我只draw
－－画一个圆角矩形
周边类
    Paint/Canvas/Rect/Path/PathMeasure/Text/Shapeshader/Matrix/SVG/Drawable／属性动画
尼玛，每个叉出去都是一个系列。
从view的角度讲，它们都是工具
从它们的角度讲，view就是舞台

4 我只touch
－－触摸分发流程
－－触摸拦截／不让父拦截
－－多点
－－区分点击 长按 双击 drag swipe


通用的工具类
ViewConfiguration

＝＝多维突破
1+2
－－自动换行的标签layout
3 
－－draw本身就是多维
2+4
－－下拉
3+4
－－支付宝咻咻

实现支付宝咻一咻的几种思路
http://www.tuicool.com/articles/6bi2auy
http://blog.csdn.net/column/details/liuguilin.html

== View-demo
https://github.com/dudu90/FreshDownloadView 
https://github.com/DmitriyZaitsev/RadarChartView 
https://www.tuicool.com/articles/VBZ3qea

组合View

https://github.com/Yalantis/Multi-Selection

you know : 这是一个ViewPager里左右各放了一个RecyclerView  ！！

2  选择动画
val initial = view.getLocationOnScreen()
3  我还纳闷左右切换。。。
class ZoomPageTransformer(val pageWidth: Float) : ViewPager.PageTransformer
ZoomPageTransformer没地方用？？？？


https://code.facebook.com/posts/531104390396423 
http://lucasr.org/2014/05/12/custom-layouts-on-android/ 总结多维
pros and cons四种自定义view： 
1 composite view布局组合控件, 
    一般是一种ViewGroup，在它的构造器里Inflate一个XML布局，然后add到自身。
2 custom composite view自定义组合控件, 
与第一种的区别看tweet_layout_view.xml，布局文件只是简单的平级罗列控件，自定义view负责onMeasure() and onLayout()，
所以，reduce the number of child views and make layout traversals a bit more efficient.
3 flat custom view自定义扁平控件, 
继承view，把第二种组合layout的所有子view，融合进这个view的空白画布上，这就涉及完整步骤measures, arranges, and draws its inner elements.
你看手机开发者选项“show layout bounds”，它是一个view，更进一步地优化了view hierarchy。
作者又撸了一套UIElement framework，把测量和绘制分开了，吊吊的。
4 async custom views异步控件.
这个需要从TweetsListView细看。https://github.com/lucasr/smoothie 


DecorView 是窗口的顶级 View。它其实是一个FrameLayout ，内部一般还会包含一个 LinearLayout ，上面是标题栏，下面是内容栏。View层的事件都先经过 DecorView ，然后才传递给我们的 View。
ViewRoot 对应的是 ViewRootImpl 类，它是连接 WindowManager 和 DecorView 的桥梁，并且 View 的三大流程都是通过 ViewRoot 来完成的。



http://hencoder.com/ui-1-1/ 

drawText(String text, float x, float y, Paint paint)

x,y在文字的左下角

path：

arcTo(RectF oval, float startAngle, float sweepAngle, boolean forceMoveTo)

水平轴正方向是0度，正下南方向是90度，sweepAngle正数按顺时针方向计算，forceMoveTo把画笔拖过去还是跳过去


View刷新机制？

由ViewRootImpl对象的performTraversals()方法,遍历View树,调用draw()方法发起绘制该View树，值得注意的是每次发起绘图时，并不会重新绘制每个View树的视图，而只会重新绘制那些“需要重绘”的视图，View类内部变量包含了一个标志位DRAWN，当该视图需要重绘时，就会为该View添加该标志位。


LinearLayout对比RelativeLayout？

1.RelativeLayout会让子View调用2次onMeasure，LinearLayout 在有weight时，也会调用子View2次onMeasure 

2.RelativeLayout的子View如果高度和RelativeLayout不同，则会引发效率问题，当子View很复杂时，这个问题会更加严重。如果可以，尽量使用padding代替margin。 3.在不影响层级深度的情况下,使用LinearLayout和FrameLayout而不是RelativeLayout。 最后再思考一下文章开头那个矛盾的问题，为什么Google给开发者默认新建了个RelativeLayout，而自己却在DecorView中用了个LinearLayout。因为DecorView的层级深度是已知而且固定的，上面一个标题栏，下面一个内容栏。采用RelativeLayout并不会降低层级深度，所以此时在根节点上用LinearLayout是效率最高的。而之所以给开发者默认新建了个RelativeLayout是希望开发者能采用尽量少的View层级来表达布局以实现性能最优，因为复杂的View嵌套对性能的影响会更大一些。

﻿