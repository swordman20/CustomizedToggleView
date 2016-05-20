package com.example.xwf.customizedtoggleview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Hsia on 16/5/20.
 * E-mail: xiaweifeng@live.cn
 * //TODO:自定义的一个ToggleView
 */
public class ToggleView extends View {

    private static final String TAG = "Hsia";
    private Bitmap toggleBackground;//toggle的背景图片
    private Bitmap toggleSlidground;//toggle的滑动块图片
    private boolean toggleState = false; //滑动块的状态
    private int currentX;
    private boolean isSliding = false;//定义是否是滑动状态
    private int positionX; //画X轴的位置
    private int midX;
    private OnToggleStateListener mOnToggleStateListener;

    //创建这个View对象的时候调用
    public ToggleView(Context context) {
        super(context);
    }
    // 当这个控件 在布局中使用的时候 attrs就是我们在xml中自定义属性
    public ToggleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    //可以自定一种样式
    public ToggleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 开关的背景
     * @param toggleBackgroundResource
     */
    public void setToggleBackgroundResource(int toggleBackgroundResource) {
        toggleBackground = BitmapFactory.decodeResource(getResources(), R.drawable.switch_background);
    }

    /**
     * 开关滑动块的背景
     * @param toggleSlidgroundResource
     */
    public void setToggleSlidgroundResource(int toggleSlidgroundResource) {
        toggleSlidground = BitmapFactory.decodeResource(getResources(), R.drawable.slide_button_background);
    }

    /**
     * 滑动块的默认状态
     * @param toggleState
     */
    public void setToggleState(boolean toggleState) {
        this.toggleState = toggleState;
    }

    /**
     * 监听滑动块状态的回调
     */
    public void setOnToggleStateListener(OnToggleStateListener listener) {
        mOnToggleStateListener = listener;
    }

    public interface OnToggleStateListener{
        public void onToggleChange(boolean state);
    }

    /**
     * 自定义View必须重写onMeasure onDraw 方法，onLayout在继承Viewgroup的时候需要重写
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //设置当前View的大小，这里使用默认图片的大小
        setMeasuredDimension(toggleBackground.getWidth(),toggleBackground.getHeight());
    }

    /**
     * 画笔的工具
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画背景
        canvas.drawBitmap(toggleBackground,0,0,null);
        if (isSliding){
            //将画笔位置调整到toggle滑动块中间
            positionX = currentX - toggleSlidground.getWidth()/2;
            //判断滑动块位置，不让它超出边界
            int rightmostX = toggleSlidground.getWidth()-(toggleSlidground.getWidth()/2)+26;
            midX = toggleSlidground.getWidth()/4;
//            Log.d(TAG, "rightmostX: "+rightmostX);
            if (positionX <= 0) {
                toggleState = false;
                positionX = 0;
            }else if (positionX > rightmostX){
                toggleState = true;
                positionX = rightmostX;
            }
            canvas.drawBitmap(toggleSlidground,positionX,0,null);
        }else {
            //画滑动块,通过state状态来画
            if (toggleState) {
                positionX = toggleBackground.getWidth()-toggleSlidground.getWidth();
                //滑动块为 开
                canvas.drawBitmap(toggleSlidground,positionX,0,null);
            }else {
                //滑动块为 关
                canvas.drawBitmap(toggleSlidground,0,0,null);
            }
        }
    }

    /**
     * 处理滑动块的事件
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        //三种基本事件
        switch (action){
            case MotionEvent.ACTION_DOWN:
                currentX = (int) event.getX();
                isSliding = true;
//                Log.d(TAG, "当前按下的X:"+currentX);
                break;
            case MotionEvent.ACTION_UP:
                currentX = (int) event.getX();
                if (positionX < midX) {
                    toggleState = false;
                }else if (positionX > midX){
                    toggleState = true;
                }
                isSliding = false;

                //触发回调事件
                if (mOnToggleStateListener != null) {
                    mOnToggleStateListener.onToggleChange(toggleState);
                }


                break;
            case MotionEvent.ACTION_MOVE:
                currentX = (int) event.getX();
                break;
        }
        // 刷新当前控件 ondraw方法会被执行
        invalidate();
        //消费当前事件
        return true;
    }
}

