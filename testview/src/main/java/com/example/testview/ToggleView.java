package com.example.testview;

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
 * //TODO:自定义的ToggleView
 */
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
}
