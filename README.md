#自定义View之ToggleView
**Android自定义View一般就2中方式，第一种是自定义某个控件，就是写一个类继承View，还有一种就是自定义某个布局，就是写一个类继承Viewgroup，这篇文章主要学习一下如何自定义滑动块（ToggleView）**

![image](/Users/xwf/Desktop/v7.gif)

##1、写一个类继承View，重写里面的3个构造方法。
```
public class ToggleView extends View {

    private static final String TAG = "Hsia";
    private Bitmap toggleBackgroudBitmap;
    private Bitmap toggleSlideBitmap;
    private boolean toggleState = false;
    private OnToggleStateListener onToggleStateListener;
    private int currentX;
    private boolean isSlideing = false;
    private int midCurrentX;

    //当创建对象的时候调用
    public ToggleView(Context context) {
        super(context);
    }

    public ToggleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ToggleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
```
##2、设置自定义View的基本属性（背景，状态）

```
//设置toggle背景图片
    public void setToggleBackgroundResource(int toggleBackgroundResource) {
        toggleBackgroudBitmap = BitmapFactory.decodeResource(getResources(), toggleBackgroundResource);
    }
    //设置滑动块的图片
    public void setSlideResource(int slideResource) {
        toggleSlideBitmap = BitmapFactory.decodeResource(getResources(), slideResource);
    }
    //滑动块的状态
    public void setToggleState(boolean toggleState) {
        this.toggleState = toggleState;
    }
    //滑动块的状态监听
    public void setonToggleStateListener(OnToggleStateListener onToggleStateListener) {
        this.onToggleStateListener = onToggleStateListener;
    }
    public interface OnToggleStateListener {
        public void onState(boolean state);
    }

```

##3、在layout布局中引用，并设置相关属性

```
布局：
<com.example.testview.ToggleView
        android:id="@+id/tg"
        android:layout_width="wrap_content"
        android:layout_centerInParent="true"
        android:layout_height="wrap_content" />
   
Activi：        
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ToggleView mTG = (ToggleView) findViewById(R.id.tg);
        //设置基本属性
        mTG.setToggleBackgroundResource(R.drawable.slide_background);
        mTG.setSlideResource(R.drawable.slide_button);
        //设置状态
        mTG.setToggleState(false);
        //Toggle的状态监听
        mTG.setonToggleStateListener(new ToggleView.OnToggleStateListener() {
            @Override
            public void onState(boolean state) {
                if (state){
                    Toast.makeText(MainActivity.this, "当前为开", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MainActivity.this, "当前为关", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
```
##4、自定义控件一般必须重写 onMeasure onDraw onLayout 方法

> 重写onMeasure，用于测量控件的大小，并设置。

```
 /**
     * 自定义控件一般必须重写 onMeasure onDraw onLayout 方法，这里继承自View就不用重写onLayout了
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //设置Toggle的大小，根据背景图片大小设置
        setMeasuredDimension(toggleBackgroudBitmap.getWidth(),toggleBackgroudBitmap.getHeight());
    }
    
```

> 重写onDraw，用于画自定义View的布局。主要是对滑动块的边界处理。

```

@Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /**
         * 开始画背景
         * @bitmap 背景的Bitmap
         * @left X轴位置
         * @top Y轴位置
         * @paint 画笔
         */
        canvas.drawBitmap(toggleBackgroudBitmap,0,0,null);
        //画滑动块 根据滑动位置画
        if (isSlideing) {
            //将X位置设置到滑动块中间
            midCurrentX = currentX - toggleSlideBitmap.getWidth()/2;
            int maxCurrentX = toggleBackgroudBitmap.getWidth() - toggleSlideBitmap.getWidth();
            //不让滑动块超出背景的边界
            if (midCurrentX <= 0 ) {
                midCurrentX = 0;
                //调用toggleState让滑动块靠边对齐
                toggleState = false;
            }else if (midCurrentX >=maxCurrentX){
                midCurrentX = maxCurrentX;
                toggleState = true;
            }
            canvas.drawBitmap(toggleSlideBitmap, midCurrentX,0,null);
        }else {
            //画滑动块 根据状态去画
            if (toggleState) {
                //计算X轴画笔的位置
                int canvasX = toggleBackgroudBitmap.getWidth()-toggleSlideBitmap.getWidth();
                //开
                canvas.drawBitmap(toggleSlideBitmap,canvasX,0,null);
            }else {
                //关
                canvas.drawBitmap(toggleSlideBitmap,0,0,null);
//                Log.d(TAG, "toggleState: "+toggleState);
            }
        }

    }
```

> onLayout一般在继承Viewgroup的时候才会重写。

##5、重写onTouchEvent方法，监听3种基本动作事件

```
/**
     * 重写点击事件，用于监听滑动块的位置
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                currentX = (int) event.getX();
                //定义一个状态，当我按下的时候，通过滑动来画
                isSlideing = true;
                break;
            case MotionEvent.ACTION_UP:
                currentX = (int) event.getX();
                //当我松开鼠标，canvas按状态去画
                isSlideing = false;
                //判断滑动结束后X轴的位置，如果超过背景的一半，就画到另一边
                if (currentX <= toggleBackgroudBitmap.getWidth()/2) {
                    toggleState = false;
                }else {
                    toggleState = true;
                }
                //当鼠标抬起手更新回调事件
                if (onToggleStateListener != null) {
                    onToggleStateListener.onState(toggleState);
                }


                break;
            case MotionEvent.ACTION_MOVE:
                currentX = (int) event.getX();
                break;
        }
        //刷新Draw
        invalidate();
        return true;
    }
```

**关于作者**
	- 个人网站：[北京互联科技](http://shop.zbj.com/14622657/)
	- Email：[xiaweifeng@live.cn](https://login.live.com)
	- 项目地址:[https://github.com/swordman20/CustomizedToggleView](https://github.com/swordman20/CustomizedToggleView)