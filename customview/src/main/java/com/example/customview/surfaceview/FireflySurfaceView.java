package com.example.customview.surfaceview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


import com.example.customview.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by wenshi on 2018/7/5.
 * Description 浮点粒子控件
 */
public class FireflySurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    // 粒子的最大数量
    private static final int MAX_NUM = 400;
    // 粒子集合
    private List<FloatParticle> mListParticles;
    // 随机数
    private Random mRandom;

    private SurfaceHolder mHolder;

    // 动画线程
    private Handler mHandler;

    // 粒子半径
    private int mParticleMaxRadius;

    // 粒子数量
    private int mParticleNum;

    // 粒子移动速率
    private int mParticleMoveRate;

    private static final int EMPTY_FLAG = 1;

    public FireflySurfaceView(Context context) {
        this(context, null);
    }

    public FireflySurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FireflySurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 关闭硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        init();

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.FireflySurfaceView);
        mParticleMaxRadius = ta.getInt(R.styleable.FireflySurfaceView_firefly_max_radius, 5);
        mParticleNum = ta.getInt(R.styleable.FireflySurfaceView_firefly_num, MAX_NUM);
        mParticleMoveRate = ta.getInt(R.styleable.FireflySurfaceView_firefly_move_rate, 5);
        ta.recycle();
    }

    private void init() {
        // 设置透明
        setZOrderOnTop(true);
        // 配合清屏 canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        mHolder = getHolder();
        mHolder.setFormat(PixelFormat.TRANSLUCENT);
        mHolder.addCallback(this);
        // 初始化随机数
        mRandom = new Random();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    }

    // 初始化浮点粒子数据
    private void initParticlesData(int width, int height) {
        mListParticles = new ArrayList<>();
        for (int i = 0; i < mParticleNum; i++) {
            FloatParticle fp = new FloatParticle(width, height);
            mParticleMaxRadius = mParticleMaxRadius < 2 ? 2 : mParticleMaxRadius;
            fp.setRadius(mRandom.nextInt(mParticleMaxRadius - 1) + 1);
            mListParticles.add(fp);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        initParticlesData(width, height);
        startAnimation();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        stopAnimation();
    }

    public void stopAnimation() {
        mHandler.removeCallbacksAndMessages(null);
    }

    public void startAnimation() {
        //if (mHandler != null) return;
        HandlerThread fireThread = new HandlerThread(this.getClass().getName());
        fireThread.start();
        mHandler = new Handler(fireThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Canvas mCanvas = mHolder.lockCanvas(null);
                if (mCanvas != null) {
                    synchronized (mHolder) {
                        // 清屏
                        mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                        for (FloatParticle fp : mListParticles) {
                            fp.drawParticle(mCanvas);
                        }
                    }
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (mCanvas != null) {
                    mHolder.unlockCanvasAndPost(mCanvas);
                }
                mHandler.sendEmptyMessageDelayed(EMPTY_FLAG, mParticleMoveRate);
            }
        };
        mHandler.sendEmptyMessage(EMPTY_FLAG);
    }

    public int getParticleMoveRate() {
        return mParticleMoveRate;
    }

    public void setParticleMoveRate(int particleMoveRate) {
        mParticleMoveRate = particleMoveRate;
    }

    public int getParticleMaxRadius() {
        return mParticleMaxRadius;
    }

    public int getParticleNum() {
        return mParticleNum;
    }


    public class FloatParticle {

        // 三阶贝塞尔曲线
        private Point startPoint;
        private Point endPoint;
        private Point controlPoint1;
        private Point controlPoint2;

        private Paint mPaint;
        private Path mPath;
        private Random mRandom;

        // 圆半径
        private float mRadius = 5;

        // 控件宽度
        private int mWidth;
        // 控件高度
        private int mHeight;

        private float mCurDistance = 0;

        private static final int DISTANCE = 255;

        private static final float MOVE_PER_FRAME = 1f;

        // 火花外侧阴影大小
        private static final float BLUR_SIZE = 5.0F;

        // 路径测量
        private PathMeasure mPathMeasure;

        private float mMeasureLength;

        public FloatParticle(int width, int height) {
            mWidth = width;
            mHeight = height;
            mRandom = new Random();

            startPoint = new Point((int) (mRandom.nextFloat() * mWidth), (int) (mRandom.nextFloat() * mHeight));

            // 抗锯齿
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setColor(Color.WHITE);
            // 防抖动
            mPaint.setDither(true);
            mPaint.setStyle(Paint.Style.FILL);
            // 设置模糊效果 边缘模糊
            mPaint.setMaskFilter(new BlurMaskFilter(BLUR_SIZE, BlurMaskFilter.Blur.SOLID));

            mPath = new Path();
            mPathMeasure = new PathMeasure();

            startPoint.x = (int) (mRandom.nextFloat() * mWidth);
            startPoint.y = (int) (mRandom.nextFloat() * mHeight);
        }

        public void drawParticle(Canvas canvas) {

            // 初始化三阶贝塞尔曲线数据
            if (mCurDistance == 0) {
                endPoint = getRandomPointRange(startPoint.x, startPoint.y, DISTANCE);
                controlPoint1 = getRandomPointRange(startPoint.x, startPoint.y, mRandom.nextInt(Math.min(mWidth, mHeight) / 2));
                controlPoint2 = getRandomPointRange(endPoint.x, endPoint.y, mRandom.nextInt(Math.min(mWidth, mHeight) / 2));
                // 添加贝塞尔曲线路径
                mPath.reset();
                mPath.moveTo(startPoint.x, startPoint.y);
                mPath.cubicTo(controlPoint1.x, controlPoint1.y, controlPoint2.x, controlPoint2.y, endPoint.x, endPoint.y);
                mPathMeasure.setPath(mPath, false);
                mMeasureLength = mPathMeasure.getLength();
            }
            //计算当前坐标点
            float[] loc = new float[2];
            mPathMeasure.getPosTan(mCurDistance / DISTANCE * mMeasureLength, loc, null);
            startPoint.x = (int) loc[0];
            startPoint.y = (int) loc[1];

            // 递增1
            mCurDistance += MOVE_PER_FRAME;

            if (mCurDistance >= DISTANCE) {
                mCurDistance = 0;
            }

            canvas.drawCircle(startPoint.x, startPoint.y, mRadius, mPaint);
        }

        /**
         * @param baseX 基准坐标x
         * @param baseY 基准坐标y
         * @param range 指定范围长度
         * @return 根据基准点获取指定范围的随机点
         */
        private Point getRandomPointRange(int baseX, int baseY, int range) {
            int randomX = 0;
            int randomY = 0;
            //range指定长度为255，可以根据实际效果调整
            if (range <= 0) {
                range = 1;
            }
            //我们知道一点(baseX,baseY)求与它距离长度为range的另一点

            //两点x方向的距离(随机产生)
            int distanceX = mRandom.nextInt(range);

            //知道x方向的距离与斜边的距离求y方向的距离
            int distanceY = (int) Math.sqrt(range * range - distanceX * distanceX);

            randomX = baseX + getRandomPNValue(distanceX);
            randomY = baseY + getRandomPNValue(distanceY);

            if (randomX > mWidth) {
                randomX = mWidth - range;
            } else if (randomX < 0) {
                randomX = range;
            } else if (randomY > mHeight) {
                randomY = mHeight - range;
            } else if (randomY < 0) {
                randomY = range;
            }

            return new Point(randomX, randomY);
        }

        /**
         * 获取随机的正负值
         *
         * @return
         */
        private int getRandomPNValue(int value) {
            return mRandom.nextBoolean() ? value : 0 - value;
        }

        /**
         * 设置圆半径
         *
         * @param radius
         */
        public void setRadius(float radius) {
            mRadius = radius;
        }
    }


}

