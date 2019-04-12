package com.yqw.hotheart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/***
 *
 *  抖音点击出现爱心的效果
 *  Created by YQW on 2019/4/11.
 */
public class HeartViewGroup extends ViewGroup {
    DoubleClickListener mDoubleClickListener;

    List<HeartBean> list;
    int MaxAlpha = 255;//
    boolean START = true;//true为开始动画，false为结束动画
    int refreshRate = 16;//动画刷新频率
    int degreesMin = -30;//最小旋转角度
    int degreesMax = 30;//最大旋转角度
    MyHandler handler = new MyHandler();
    Bitmap bitmap;//初始图片
    Matrix matrix = new Matrix();//控制bitmap旋转角度和缩放的矩阵
    int timeout = 400;//双击间格毫秒延时
    long singleClickTime;

    @SuppressLint("HandlerLeak")
    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    Refresh();
                    invalidate();
                    if (list != null && list.size() > 0) {
                        sendEmptyMessageDelayed(0, refreshRate);// 延时
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public HeartViewGroup(Context context) {
        super(context);
    }

    public HeartViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.HeartViewGroup);
        bitmap = BitmapFactory.decodeResource(getResources(), typedArray.getResourceId(R.styleable.HeartViewGroup_swipe_image, R.drawable.ic_heart));
        refreshRate = typedArray.getInt(R.styleable.HeartViewGroup_refresh_rate, refreshRate);
        degreesMin = typedArray.getInt(R.styleable.HeartViewGroup_degrees_interval_min, degreesMin);
        degreesMax = typedArray.getInt(R.styleable.HeartViewGroup_degrees_interval_max, degreesMax);
        typedArray.recycle();
    }

    {
        //初始化
        list = new ArrayList<>();
//        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_heart);
        singleClickTime = System.currentTimeMillis();
    }

    /**
     * 确定ViewGroup的宽高
     *
     * @param widthMeasureSpec  宽参数
     * @param heightMeasureSpec 高参数
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //ViewGroup主要是一个容器，当ViewGroup的宽高是确切的值的时候，控件的宽高就是它本身设置的值
        //主要是考虑ViewGroup Wrap_content的时，需要计算控件的宽高，控件的宽高根据子View的布局来计算
        int width = 0;
        int height = 0;
        int mWidthMeasureMode = MeasureSpec.getMode(widthMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);//初始化所有子View的宽高

        if (mWidthMeasureMode == MeasureSpec.AT_MOST) {//Wrap_content的情况
            //测量子View的宽  怎么测量子View的宽
            View childView = getChildAt(0);//获取到这个控件
            if (childView != null)
                width = childView.getMeasuredWidth();
        } else {
            width = MeasureSpec.getSize(widthMeasureSpec);
        }

        int mHeightMeasureMode = MeasureSpec.getMode(heightMeasureSpec);
        if (mHeightMeasureMode == MeasureSpec.AT_MOST) {
            View childView = getChildAt(0);
            if (childView != null)
                height = childView.getMeasuredHeight();
        } else {
            height = MeasureSpec.getSize(heightMeasureSpec);
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        //将子布局显示出来
        View childView = getChildAt(0);
        if (childView != null)
            childView.layout(0, 0, childView.getMeasuredWidth(), childView.getMeasuredHeight());
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        for (int i = 0; i < list.size(); i++) {
            HeartBean heartBean = list.get(i);
            // 重置
            matrix.reset();
            // 缩放原图
            matrix.postScale(heartBean.scanle,
                    heartBean.scanle,
                    heartBean.X + bitmap.getWidth() / 2,
                    heartBean.Y + bitmap.getHeight() / 2);
            // 旋转
            matrix.postRotate(heartBean.degrees,
                    heartBean.X + bitmap.getWidth() / 2,
                    heartBean.Y + bitmap.getHeight() / 2);

            canvas.save();
            canvas.concat(matrix);
            canvas.drawBitmap(bitmap,
                    heartBean.X - bitmap.getWidth() / 2,
                    heartBean.Y - bitmap.getHeight() / 2,
                    heartBean.paint);
            canvas.restore();
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        super.onTouchEvent(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                long newClickTime = System.currentTimeMillis();
                //双击以上事件都会调用心动动画
                if (newClickTime - singleClickTime < timeout) {
                    //开始心动动画
                    startSwipe(event);
                    //调用双击事件
                    if (mDoubleClickListener != null)
                        mDoubleClickListener.onDoubleClick(this);
                }
                singleClickTime = newClickTime;
                break;
        }
        return true;
    }

    /**
     * 初始化paint
     */
    private Paint initPaint(int alpha) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);// 抗锯齿
        paint.setAlpha(alpha);// 透明度
        return paint;
    }

    /**
     * 开始心动动画
     *
     * @param event 点击事件
     */
    private void startSwipe(MotionEvent event) {
        //
        HeartBean bean = new HeartBean();
        bean.scanle = 1; //
        bean.alpha = MaxAlpha; //
        bean.X = (int) event.getX(); //
        bean.Y = (int) event.getY(); //
        bean.paint = initPaint(bean.alpha);
        bean.degrees = degrees(degreesMin, degreesMax);

        if (list.size() == 0) {
            START = true;
        }
        list.add(bean);
        invalidate();
        if (START) {
            handler.sendEmptyMessage(0);
        }
    }

    /***
     * 刷新
     */
    private void Refresh() {
        for (int i = 0; i < list.size(); i++) {
            HeartBean bean = list.get(i);
            if (!START && bean.alpha == 0) {
                list.remove(i);
                bean.paint = null;
                continue;
            } else if (START) {
                START = false;
            }
            bean.scanle += refreshRate > 16 ? 0.03 : 0.1;//放大倍数 默认0.1
            bean.alpha -= 10;//透明度
            if (bean.alpha < 0) {
                bean.alpha = 0;
            }
            bean.paint.setAlpha(bean.alpha);
        }
    }

    /**
     * 生成一个随机整数
     *
     * @param min 最小值
     * @param max 最大值
     * @return 整数
     */
    private int degrees(int min, int max) {
        //若最小值大于最大值，则重新赋值正位
        if (min > max) {
            int x = min;
            min = max;
            max = x;
        }
        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
    }

    /**
     * 接口
     */
    public interface DoubleClickListener {
        void onDoubleClick(View view);
    }

    /**
     * 给Button监听接口的方法
     *
     * @param mDoubleClickListener 双击监听
     */
    public void setOnDoubleClickListener(
            final DoubleClickListener mDoubleClickListener) {
        this.mDoubleClickListener = mDoubleClickListener;
    }

    /**
     * 设置跳动的图片
     *
     * @param id 图片资源id
     */
    public void setSwipeImage(int id) {
        bitmap = BitmapFactory.decodeResource(getResources(), id);
    }

    /**
     * 设置动画刷新频率
     * 默认16ms
     *
     * @param refreshRate 刷新频率，单位：毫秒
     */
    public void setRefreshRate(int refreshRate) {
        this.refreshRate = refreshRate;
    }

    /**
     * 图片旋转角度区间
     * 0-360
     *
     * @param min 最小旋转角度
     * @param max 最大旋转角度
     */
    public void setDegreesInterval(int min, int max) {
        degreesMin = min;
        degreesMax = max;
    }

    /**
     * viewGroup销毁时释放资源
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        handler = null;
        if (bitmap != null)
            bitmap.recycle();
        bitmap = null;
        matrix = null;
        list = null;
    }

}
