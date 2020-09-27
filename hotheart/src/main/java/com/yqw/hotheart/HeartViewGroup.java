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

import com.yqw.hotheart.minterface.OnDoubleClickListener;
import com.yqw.hotheart.minterface.OnSimpleClickListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/***
 *
 *  抖音点击出现爱心的效果
 *  Created by YQW on 2019/4/12.
 */
public class HeartViewGroup extends ViewGroup {
    //    private static final String TAG = "HeartFrameLayout";
    private OnDoubleClickListener mOnDoubleClickListener;
    private OnSimpleClickListener mOnSimpleClickListener;

    private List<HeartBean> list;//存放多个心形图
    private boolean START = true;//true为开始动画，false为结束动画
    private int refreshRate = 16;//动画刷新频率
    private int degreesMin = -30;//最小旋转角度
    private int degreesMax = 30;//最大旋转角度
    private MyHandler handler;
    private Bitmap bitmap;//初始图片
    private Matrix matrix;//控制bitmap旋转角度和缩放的矩阵
    private long singleClickTime;//记录第一次点击的时间
    private boolean isShake = true;//是否需要抖动效果 默认抖动

    private int clickCount = 1;//记录连续点击次数
    private boolean isDoubleClick;

    private Context mContext;
    private AttributeSet mAttributeSet;

    @SuppressLint("HandlerLeak")
    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                Refresh();
                invalidate();
                if (list != null && list.size() > 0) {
                    sendEmptyMessageDelayed(0, refreshRate);// 延时
                }
            }
        }
    }

    public HeartViewGroup(Context context) {
        super(context);
    }

    public HeartViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mAttributeSet = attrs;

        init();
    }

    public void init() {
        TypedArray typedArray = mContext.obtainStyledAttributes(mAttributeSet, R.styleable.HeartViewGroup);
        bitmap = BitmapFactory.decodeResource(getResources(), typedArray.getResourceId(R.styleable.HeartViewGroup_heart_swipe_image, R.drawable.ic_heart));
        isShake = typedArray.getBoolean(R.styleable.HeartViewGroup_heart_shake, isShake);
        refreshRate = typedArray.getInt(R.styleable.HeartViewGroup_heart_refresh_rate, refreshRate);
        degreesMin = typedArray.getInt(R.styleable.HeartViewGroup_heart_degrees_interval_min, degreesMin);
        degreesMax = typedArray.getInt(R.styleable.HeartViewGroup_heart_degrees_interval_max, degreesMax);
        handler = new MyHandler();
        matrix = new Matrix();
        if (list == null) {
            list = new ArrayList<>();
        }

        typedArray.recycle();
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
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //初始化
        init();

        singleClickTime = System.currentTimeMillis();
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
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        //双击间格毫秒延时
        final int timeout = 200;
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            long newClickTime = System.currentTimeMillis();
            //双击以上事件都会调用心动动画
            if (newClickTime - singleClickTime < timeout) {
                //开始心动动画
                startSwipe(event);
                //调用双击事件
                if (mOnDoubleClickListener != null)
                    mOnDoubleClickListener.onDoubleClick(this);
                isDoubleClick = true;
                clickCount++;
//                Log.d(TAG, "连击次数 clickCount = " + clickCount);
            } else {
                isDoubleClick = false;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(timeout);
                            if (mOnSimpleClickListener != null && !isDoubleClick) {
                                //调用单击事件
                                mOnSimpleClickListener.onSimpleClick(getRootView());
                                clickCount = 1;
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
            singleClickTime = newClickTime;
        }
        return false;
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
        //透明度，默认为255，0为消失不可见
        int maxAlpha = 255;
        bean.alpha = maxAlpha; //
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
        if (list == null) {
            list = new ArrayList<>();
        }
        for (int i = 0; i < list.size(); i++) {
            HeartBean bean = list.get(i);
            bean.count++;
            if (!START && bean.alpha == 0) {
                //透明度减为0后，从list里清除
                list.remove(i);
                bean.paint = null;
                continue;
            } else if (START) {
                START = false;
            }
            if (bean.count <= 1 && isShake) {
                bean.scanle = 1.9f;//初始为1.9倍大小 步骤A
            } else if (bean.count <= 6 && isShake) {
                bean.scanle -= 0.2;//每次缩小0.2，缩小5帧后为0.9 步骤B
            } else if (bean.count <= 15 && isShake) {
                bean.scanle = 1;//恢复原图大小 步骤C ABC三个步骤主要实现一个初始跳动心心的效果
            } else {
                bean.scanle += 0.1;//放大倍数 每次放大0.1
                bean.alpha -= 10;//透明度
                if (bean.alpha < 0) {
                    bean.alpha = 0;
                }
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
     * 单击接口监听的方法
     *
     * @param mOnSimpleClickListener 单击监听
     */
    public void setOnSimpleClickListener(
            final OnSimpleClickListener mOnSimpleClickListener) {
        this.mOnSimpleClickListener = mOnSimpleClickListener;
    }

    /**
     * 双击接口监听的方法
     *
     * @param mOnDoubleClickListener 双击监听
     */
    public void setOnDoubleClickListener(
            final OnDoubleClickListener mOnDoubleClickListener) {
        this.mOnDoubleClickListener = mOnDoubleClickListener;
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
     * 设置是否抖动一下
     * 默认抖动
     *
     * @param isShake true为抖动
     */
    public void setShake(boolean isShake) {
        this.isShake = isShake;
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
     * 需要销毁时调用
     */
    private void destroy() {
        handler = null;
        if (bitmap != null)
            bitmap.recycle();
        bitmap = null;
        matrix = null;
        list = null;
    }

    /**
     * viewGroup销毁时释放资源
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        destroy();
    }

    @Override
    protected void detachViewFromParent(int index) {
        super.detachViewFromParent(index);
    }
}
