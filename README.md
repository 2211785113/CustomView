# CustomView
## 自定义View大合集

### 示例

圆形ImageView：

* [CircleView（分类3）](https://github.com/2211785113/CustomView/blob/master/app/src/main/java/com/example/ruru/customview/view/CircleView.java)

* [CircleView1（分类3）](https://github.com/2211785113/CustomView/blob/master/app/src/main/java/com/example/ruru/customview/view/CircleView1.java)

* [CircleView2（分类1）](https://github.com/2211785113/CustomView/blob/master/app/src/main/java/com/example/ruru/customview/view/CircleView2.java)

自定义时钟：

* [ClockView（分类1）](https://github.com/2211785113/CustomView/blob/master/app/src/main/java/com/example/ruru/customview/view/ClockView.java)

水平滚动View：

* [HorizontalScrollViewEx（分类2）](https://github.com/2211785113/CustomView/blob/master/app/src/main/java/com/example/ruru/customview/view/HorizontalScrollViewEx.java)

View的滑动：

* [MoveView（layout/offsetTopAndBottom/LayoutParams）](https://github.com/2211785113/CustomView/blob/master/app/src/main/java/com/example/ruru/customview/view/MoveView.java)

* [MoveView1（动画）](https://github.com/2211785113/CustomView/blob/master/app/src/main/java/com/example/ruru/customview/view/MoveView1.java)

下拉刷新控件：

* [PullRefreshLayout（仿SwipeRefreshLayout）](https://github.com/2211785113/CustomView/blob/master/app/src/main/java/com/example/ruru/customview/view/PullRefreshLayout.java)

圆形进度条：

* [RoundProgressBar](https://github.com/2211785113/CustomView/blob/master/app/src/main/java/com/example/ruru/customview/view/RoundProgressBar.java)

搜索框：

* [SearchEditText（仿微信ios的搜索框）](https://github.com/2211785113/CustomView/blob/master/app/src/main/java/com/example/ruru/customview/view/SearchEditText.java)

字体移动：

* [TextMoveView](https://github.com/2211785113/CustomView/blob/master/app/src/main/java/com/example/ruru/customview/view/TextMoveView.java)

* [TextMoveView1](https://github.com/2211785113/CustomView/blob/master/app/src/main/java/com/example/ruru/customview/view/TextMoveView1.java)

波浪控件：

* [WaveView](https://github.com/2211785113/CustomView/blob/master/app/src/main/java/com/example/ruru/customview/view/WaveView.java)

<br/>

****

### View知识大盘点目录

* View的定义

* View的坐标系

* MotionEvent

* mTouchSlop

* VelocityTracker

* GestureDetector

* View的滑动

* View的动画

* 自定义View

* 滑动冲突

* 进阶学习


### 1.View的定义

View：Android中所有控件的基类。单个控件或多个控件组成的一组控件。

ViewGroup：控件组，包含多个控件。ViewGroup内部可以有子View，子View还可以是ViewGroup。

View树结构类似于Web前端DOM树

部分继承关系：(见图viewExtends.jpg)


### 2.View的坐标系（见图viewLocation.png）

Android坐标系：

是什么：屏幕左上角为坐标原点；

怎么样：触控事件MotionEvent中获取坐标-getRawX()和getRawY()。

<br/>

View坐标系：

是什么：父控件左上角为坐标原点；

怎么样：

* 获取坐标-getTop/getBottom/getLeft/getRight；

* 获取坐标-getX/getY(Android3.0)(见源码getX=getLeft+translationX)

* 获取宽高-getWidth/getHeight或者getRight()-getLeft()/getBottom()-getTop()(见view源码两者效果同)。


### 3.MotionEvent

是什么：触摸事件

为什么：可以用作点击事件

怎么样：

* 点击屏幕离开松开：事件序列为DOWN->UP;

* 点击屏幕滑动一会松开：事件序列为DOWN->MOVE->...->MOVE->UP.

* 获取坐标：event.getX/getY/getRawX/getRawY

<br/>

应用：

防止Android过快点击造成多次事件的三种方法：

https://blog.csdn.net/zhyxuexijava/article/details/51611037?locationNum=11


### 4.mTouchSlop

是什么：最小滑动距离

为什么：

* 过滤，小于这个值不认为在滑动，提升用户体验。

* 当大于最小滑动距离时，可用于拦截事件。

怎么样：获取-见源码


### 5.VelocityTracker

是什么：速度追踪器，onTouchEvent中追踪触摸事件滑动过程中的速度，实现滑动fling或其他手势。

为什么：HorizontalScrollViewEx根据MOVE时x方向的速度判断得出子view的索引，根据子view的索引得到移动的距离，然后调用Scroller.startScroll来移动view

怎么样：
* 例子：ScrollView源码，HorizontalScrollViewEx

* 使用：见源码对类的解释

* computeCurrentVelocity：参数单位为s，1s移动200像素，则速度为200

* recycle：回收，里边调用clear

* clear：重置为最初的状态


### 6.GestureDetector

是什么：手势监测器，监听用户的单击，滚动，长按，滑动，双击行为

为什么：因为view的onTouch只能实现一些简单的按下，移动，抬起手势，于是Android sdk就提供了复杂的手势单击，滚动，长按，滑动，双击行为。

怎么样：

<br/>

第一步：实现listener

OnGestureListener接口：

* onDown：手指轻轻触摸屏幕的一瞬间，1个ACTION_DOWN触发。

* onShwoPress：手指轻轻触摸屏幕，尚未松开或拖动，1个ACTION_DOWN触发。区分onDown：强调没有松开或拖动的状态。

* onSingleTapUp：手指轻轻触摸屏幕后松开，伴随1个ACTION_UP而触发，单击行为。

* onScroll：手指按下屏幕并拖动，1个ACTION_DOWN，多个ACTION_MOVE，拖动行为。

* onLongPress：长按。

* onFling：用户按下触摸屏，快速滑动后松开，1个ACTION_DOWN，多个ACTION_MOVE，1个ACTION_UP触发，快速滑动行为。

<br/>

OnDoubleTapListener接口：监听双击行为

* onDoubleTap：双击，连续2次单击组成，不和onSingleTapConfirmed共存。

* onSingleTapConfirmed：严格的单击行为。区分onSingleTapUp：onSingleTapConfirmed后面不可能再紧跟另一个单击行为，只可能是单击，不可能是双击中的一次单击。onSingleTapUp可能会紧跟一次单击？

* onDoubleTapEvent：发生双击行为，双击期间，ACTION_DOWN，ACTION_MOVE，ACTION_UP都可触发回调。

<br/>

注意：
```
GestureDetector gestureDetector = new GestureDetector(this);

//解决长按屏幕后无法拖动的现象
gestureDetector.setIsLongpressEnabled(false);
```

<br/>

第二步：

接管目标View的onTouchEvent方法，在待监听View的onTouchEvent方法中添加实现：
```
boolean b = gestureDetector.onTouchEvent(event);

return b;
```

<br/>

区分：

MotionEvent：手势事件：按下，移动，抬起，onTouchEvent方法参数的三个状态。

GestureDetector：手势事件：单击，双击，滑动，长按，onTouchEvent方法中。

<br/>

实际：

可以不使用GestureDetector，完全可以自己在View的onTouchEvent方法中实现所需的监听。

<br/>

建议：

滑动相关：onTouchEvent中实现；监听双击行为：GestureDetector


### 7.View的滑动

基本思想：当点击事件传到View时，系统记下触摸点的坐标，手指移动时系统记下移动后触摸的坐标并算出偏移量，并通过偏移量来修改View的坐标。

滑动场景：下拉刷新

滑动原因：手机屏幕小，滑动来隐藏和显示内容

滑动效果：滑动+特效

滑动方法：

#### 第一种：layout()

为什么：

* 因为：onLayout中可以调用view.layout()来设置view显示的位置。比如HorizontalScrollViewEx，PullRefreshLayout。

* 所以：MOVE中可以通过view的left，top，right，bottom4种属性来控制view的坐标。

<br/>

怎么样：

例子：MoveView

效果：正方形会随手指的滑动改变自己的位置。

#### 第二种：offsetLeftAndRight()与offsetTopAndBottom()

怎么样：

和layout()效果和使用方式都差不多。

例子：MoveView

#### 第三种：LayoutParams

是什么：LayoutParams：保存了View的布局参数。所以可以改变布局参数从而改变位置。

* 例子1：MoveView

* 例子2：《Android开发艺术探索》

需求：Button向右平移100px

解决一：Button的LayoutParams的marginLeft参数值增加100px

解决二：Button左边放一个空View，默认宽度为0，向右移动Button，重新设置空View的宽度。
```
Button button = findViewById(R.id.button);
ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) button.getLayoutParams();
params.width += 100;
params.leftMargin += 100;
button.requestLayout();
```

#### 第四种：动画

怎么样：View平移，操作View的translationX和translationY属性。

<br/>

传统View动画：对View影像做操作，不能真正改变View的位置参数，包括宽高。

如果希望动画后状态保留：fillAfter属性设置为true，否则动画完成的一刹那，View会瞬间恢复到动画前的状态。

res-右键选第二个-选择animation-move.xml。
```
<?xml version="1.0" encoding="utf-8"?>
<set xmlns:android="http://schemas.android.com/apk/res/android"
    android:fillAfter="true">
    <translate
        android:fromXDelta="0"
        android:fromYDelta="0"
        android:toXDelta="100"
        android:toYDelta="100">
    </translate>
</set>
```
然后Java代码中调用：
```
mTextView.setAnimation(AnimationUtils.loadAnimation(this,R.anim.move));
```
缺点：不能真正改变View的位置。如果Button按钮向右移100像素，新位置无法触发点击事件，原来位置可以。

解决：新位置预先创建一个一模一样的Button，外观和onClick事件都一样，原Button完成平移隐藏，新Button显示。

<br/>

属性动画：兼容3.0以上的版本，采用开源动画库nineoldandroids
```
ObjectAnimator.ofFloat(mTextView, "translationX", 0, 100).setDuration(100).start();
```
属性动画解决了上述问题。3.0以下无法使用属性动画，可以使用动画兼容库nineoldandroids实现属性动画，本质上是View动画。

<br/>

属性动画的优点：

* 不仅可以执行动画；

* 还能够改变View的位置参数。

<br/>

例子：MoveView1

#### 第五种：scrollBy，scrollTo

scrollBy(dx,dy)：表示移动的增量为dx，dy。里边调用了scrollTo。

scrollTo(x,y)：表示移动到一个具体的坐标点。

<br/>

注意：
scrollTo，scrollBy：移动的是View的内容，如果在ViewGroup中使用，则是移动其所有的子View。

scrollTo，scrollBy：只能改变View内容的位置而不能改变View在布局中的位置。

scrollTo，scrollBy：View边缘不动，mScrollX和mScrollY是View内容边缘相对于View边缘的距离。滑动过程，mScrollX和mScrollY不断变化。

<br/>

Move事件滑动过程：

mScrollX：值=View左边缘和View内容左边缘在水平方向的距离

mScrollY：值=View上边缘和View内容上边缘在竖直方向的距离

<br/>

* View边缘：View的位置，由四个顶点组成

* View内容边缘：View中的内容的边缘。

<br/>

向右滑动：View内容向右滑，相当于View向左滑，所以mScrollX为负值。

向下滑动：View内容向下滑，相当于View向上滑，所以mScrollY为负值。

<br/>

例子：view右滑：((View)getParent()).scrollBy(-offsetX,-offsetY);

#### 第六种：Scroller。

怎么样：

第一步：调用new Scroller()。

* 参数2：插值器Interpolator，不传采用默认插值器ViscousFluidInterpolator。

<br/>

第二步：调用startScroll（）方法。

startScroll中没有调用类似开启滑动的方法，而是保存了传进来的各种参数：

* startX和startY表示滑动开始的起点，

* dx和dy表示滑动的距离，

* duration表示滑动持续的时间。

所以 startScroll 方法只是用来做前期准备的，并不能使 View 进行滑动。

关键：在 startScroll 方法后调用了 invalidate 方法，这个方法会导致View的重绘，而View的重绘会调用View的 draw 方法， draw 方法又会调用 View 的 computeScroll 方法。

<br/>

第三步：重写 computeScroll 方法：

computeScroll 方法中通过 Scroller 来获取当前的 ScrollX 和 ScrollY，

然后调用 scrollTo 方法进行View的滑动，

接着调用 invalidate 方法让View进行重绘，

重绘就会调用 computeScroll 方法来实现View的滑动。

这样通过不断地移动一个小的距离并连贯起来就实现了平滑移动的效果。

<br/>

Scroller中如何获取当前位置的ScrollX和ScrollY？

在调用 scrollTo 方法前会调用 Scroller 的 computeScrollOffset（ ） 方法。

computeScrollOffset 方法源码：自行查看。

* 首先会计算动画持续的时间 timePassed。

* 如果动画持续时间小于我们设置的滑动持续时间mDuration，则执行Switch语句。

* 因为在startScroll 方法中的mMode值为SCROLL_MODE，

* 所以执行分支语句SCROLL_MODE，

* 然后根据插值器 Interpolator 来计算出在该时间段内移动的距离，

* 赋值给mCurrX和mCurrY，

* 这样我们就能通过Scroller来获取当前的ScrollX和ScrollY了。

另外， computeScrollOffset 的返回值如果为true则表示滑动未结束， 为false则表示滑动结束。

所以， 如果滑动未结束， 我们就得持续调用scrollTo（ ） 方法和 invalidate（ ） 方法来进行 View的滑动。

<br/>

总结一下Scroller的原理：

* Scroller并不能直接实现View的滑动， 它需要配合View的computeScroll 方法。

* 在computeScroll 中不断让View进行重绘， 每次重绘都会计算滑动持续的时间，

* 根据这个持续时间就能算出这次View滑动的位置，

* 我们根据每次滑动的位置调用scrollTo 方法进行滑动， 这样不断地重复上述过程就形成了弹性滑动。

<br/>

#### 总：滑动方式的对比：

scrollTo/scrollBy：专门用于View的滑动。

优点：方便实现滑动效果而不影响内部元素的单击事件。

缺点：只能滑动View的内容，不能滑动View本身。

<br/>

动画：

Android3.0属性动画：没有缺点。

View动画/Android3.0以下属性动画：

缺点：不能改变View本身的属性。

优点：动画元素不需要响应用户的交互比较合适，否则不适合。复杂的效果必须通过动画实现。

<br/>

改变布局参数：

缺点：使用起来麻烦无明显缺点。

优点：主要适用对象是一些具有交互性的View，需要和用户交互。

用动画会有问题，用改变布局参数没问题。

<br/>

#### 总：

scrollTo/scrollBy：操作简单，适合对View内容的滑动

动画：操作简单，主要适用于没有交互的View和实现复杂的动画效果。

改变布局参数：操作稍微复杂，适用于有交互的View。


### 8.View的动画

分类：

* TranslateAnimation，RotateAnimation，ScaleAnimation，AlphaAnimation。

* AnimationSet动画集合：混合使用多种动画。

<br/>

分析：

* 优点：效率高，使用方便。

* 缺点：不具有交互性。当某个元素发生View动画后，其响应事件的位置依然在动画进行前的地方。

* 综合：只能做普通的动画效果，避免涉及交互操作。

<br/>

比较：

* Android 3.0之前：动画框架Animation存在局限性。动画只改变显示，View位置不发生变化，移动后不能响应事件。

* Android 3.0之后：推出了新的动画框架Animator。

<br/>

Animator框架使用最多：

* AnimatorSet和ObjectAnimator配合。

* ObjectAnimator：进行更精细化的控制，控制一个对象和一个属性值。

* 使用多个ObjectAnimator组合到AnimatorSet形成一个动画。

<br/>

属性动画通过调用属性get/set方法真实控制一个View的属性值。

所以：强大的属性动画框架基本可以实现所有的动画效果。

<br/>

例子：贝塞尔曲线动画的实现

### 9.自定义View

View的生命周期：https://blog.csdn.net/jyw935478490/article/details/69397248

<br/>

#### 工作流程：

measure：测量-确定View的测量宽/高

layout：布局-确定View的最终宽/高和四个顶点的位置（确定View在父容器中的位置）

draw：绘制-将View绘制到屏幕上

<br/>

#### 自定义属性：

第一步：res-values目录下创建自定义属性的xml，比如attrs.xml，或以attrs_开头的文件名(如attrs_circle_view.xml)，会自动生成R.attr类，有属性id.
declare-styleable：自定义属性集合CircleView，集合里可以有很多自定义属性。

属性：如circle_color

属性格式：color(颜色)，reference(资源id)，dimension(尺寸)，string/integer/boolean(基本数据类型)。

<br/>

第二步：

View的构造方法中获取并解析自定义属性的值并做相应处理。

* 加载自定义属性集合CircleView.(TypeArray：简化流程，直接得到并解析)

* 解析自定义属性：解析CircleView属性集合中的circle_color属性，id为R.styleable.CircleView_circle_color(得到属性id并解析id)

* 如果在使用时没有指定circle_color这个属性，就会选择红色作为默认的颜色值。

* recycle方法实现资源。

<br/>

第三步：布局文件中使用自定义属性。

app:circle_color=“@color/light_green”

注意：布局文件中需要添加schemas声明：(更喜欢这种。)

xmlns:app=http://schemas.android.com/apk/res-auto

或者：

xmlns:app=http://schemas.android.com/apk/res/[应用包名]

app：自定义属性的前缀，也可以换其他名字。一致即可。

<br/>

#### 思考：自定义属性怎么获取xml中的值的？

https://blog.csdn.net/lmj623565791/article/details/45022631

https://blog.csdn.net/wzy_1988/article/details/49619773

<br/>

#### 分类：

分类1：继承View，重写onDraw

场景：动态或静态显示不规则效果。

例子：CircleView2

margin效果正常，不需要处理。padding在onDraw中处理。wrap_content在onMeasure中处理。

<br/>

刚开始：效果正常

<br/>

margin不需要处理。

操作：设置margin为16dp，效果正常。

理论：margin属性由父容器控制，不需要在CircleView中做特殊处理。

<br/>

padding需要处理。

操作：设置padding为16dp，没有生效。

理论：直接继承自View和ViewGroup的控件，padding无法生效，需要自己处理。

* onDraw时考虑padding即可。

* 可以直接用getPaddingLeft/Right/Top/Bottom。

* 也可以自定义一个padding属性。

<br/>

wrap_content需要处理。

解决：见onMeasure。

<br/>

分类2：继承ViewGroup，重写onLayout

场景：布局组合。

例子：HorizontalScrollViewEx

<br/>

注意：

分类1和2：onMeasure中处理wrap_content，否则外界在布局中使用wrap_content时无法达到预期的效果。

例子：TextView/ImageView源码中都进行了处理

因为：如果View在布局中使用wrap_content，那么它的specMode是AT_MOST模式，在这种模式下，它的宽高等于specSize。

查表(见table.jpg)：

这种情况下View的specSize是parentSize，而parentSize是父容器中目前可以使用的大小，也就是父容器当前剩余的空间大小。很显然，View的宽高就等于父容器当前剩余的空间大小。这种效果和在布局中使用match_parent完全一致。

解决：

给View指定一个默认的内部宽高，并在wrap_content时设置此宽高即可。

代码：
```

@Override
protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    int widthMode = MeasureSpec.getMode(widthMeasureSpec);
    int widthSize = MeasureSpec.getSize(widthMeasureSpec);
    int heightMode = MeasureSpec.getMode(heightMeasureSpec);
    int heightSize = MeasureSpec.getSize(heightMeasureSpec);
    if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
        setMeasuredDimension(mWidth, mHeight);
    } else if (widthMode == MeasureSpec.AT_MOST) {
        setMeasuredDimension(mWidth, heightSize);
    } else if (heightMode == MeasureSpec.AT_MOST) {
        setMeasuredDimension(widthSize, mHeight);
    }
}
```

<br/>

分类1：onDraw方法中处理padding。

分类2：onMeasure和onLayout中考虑padding和子元素的margin。否则padding和子元素的margin无效。

<br/>

分类3：继承特定的View（如TextView）

场景：一般用于扩展某种已有的View的功能，比较容易实现。

附注：不需要自己支持wrap_content和padding。

<br/>

分类4：继承特定的ViewGroup（如LinearLayout）

场景：效果看起来像几种View组合在一起。

附注：不需要自己处理ViewGroup的测量，布局过程。

<br/>

分类1/2和分类3/4的区别：

分类2能实现的效果分类4也能实现。

分类2更接近于底层。

<br/>

分类5：继承自定义布局,inflate布局,然后addView（遇到再补充）

<br/>

#### 注意事项：

后果：注意事项处理不好可能会影响View的正常使用或内存泄漏。

1.尽量不要在View中使用Handler。

因为view内部本身就提供了post系列的方法，完全可以替代Handler的作用，除非非常明确要使用Handler来发送消息。

<br/>

2.View中如果有线程或者动画，需要及时停止，参考View#onDetachedFromWindow

  onDetachedFromWindow的调用场景：
  
  * 线程或动画需要停止时；
  
  * 包含此View的Activity退出；
  
  * 当前View被remove时。
  
  * View变得不可见需要停止线程和动画。（不及时处理会造成内存泄漏）
  
  相对应方法：
  
  * onAttachedToWindow。

<br/>

画笔画布

绘制渲染优化

过度渲染优化

<br/>

Canvas源码解析：https://www.kancloud.cn/alex_wsc/heros/522899

Canvas绘图讲解：https://blog.csdn.net/bigconvience/article/details/26697645

<br/>

Canvas绘图三要素：Canvas，绘图坐标系，Paint。

drawXXX：绘制图形或颜色等。

<br/>

区别：

Canvas坐标系：只有一个，在View左上角。

绘图坐标系：Canvas.drawXXX中传入的坐标。可以一直变。

* Canvas.translate(dx,dy)：平移。

* Canvas.rotate(degree)：旋转。

* Canvas.scale(sx,sy)：缩放。

<br/>

OpenGL开发模式：

调用save，保存当前坐标系，将当前坐标系的矩阵Matrix入栈保存；

然后通过translate或rotate对坐标系进行变换；

然后绘图，绘图完成，调用canvas.restore将之前保存的Matrix出栈，这样就将当前绘图坐标系恢复到了canvas.save执行时候的状态。


### 10.滑动冲突

参考：https://www.jianshu.com/p/916a7bab7ef1

### 11.进阶学习

书籍：自定义控件很简单

选择：分类并选择合适的。一种效果可能多种方法可以实现，但是要找代价最小，最高效的方法实现。

方法：多积累，逐渐做到融会贯通。




