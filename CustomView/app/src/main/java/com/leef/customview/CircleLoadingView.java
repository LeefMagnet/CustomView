package com.leef.customview;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by Leef on 2015/8/9.
 * 加载进度条控件
 */
public class CircleLoadingView extends View {

    private Bitmap mCenterBmp;

    private Paint mPaint;
    private float degree;
    private float maxDegree = 30;

    private int height;
    private int width;
    private RectF rectF;

    public CircleLoadingView(Context context) {
        this(context, null);
    }

    public CircleLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleLoadingView);
        //  获取自定义的属性
        int centerRes = a.getResourceId(R.styleable.CircleLoadingView_centerDrawable, android.R.drawable.screen_background_light_transparent);
        mCenterBmp = BitmapFactory.decodeResource(getResources(), centerRes);
        //  设置画笔参数
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        rectF = new RectF();

        a.recycle();
    }

    boolean inc = true;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int centerLeft = getPaddingLeft();
        int centerTop = getPaddingTop();

        canvas.save();
        canvas.drawBitmap(mCenterBmp, centerLeft, centerTop, mPaint);
        canvas.restore();
        canvas.save();

        if (inc) {
            maxDegree = maxDegree + 3;
            degree = (degree + 3) % 360;
            if (maxDegree >= 330) {
                inc = false;
            }
        } else {
            maxDegree = maxDegree - 3;
            degree = (degree + 6) % 360;
            if (maxDegree <= 30) {
                inc = true;
            }
        }

        float size = mPaint.getStrokeWidth() / 2;
        rectF.set(size, size, width - size, height - size);

        canvas.drawArc(rectF, degree, maxDegree, false, mPaint);

        canvas.restore();
        if (isShown()) {
            invalidate();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w;
        int h;
        if (mCenterBmp == null) {
            w = getPaddingLeft() + getPaddingRight();
            h = getPaddingTop() + getPaddingBottom();
        } else {
            w = mCenterBmp.getWidth() + getPaddingLeft() + getPaddingRight();
            h = mCenterBmp.getHeight() + getPaddingTop() + getPaddingBottom();
        }
        width = resolveContentSize(w, widthMeasureSpec);
        height = resolveContentSize(h, heightMeasureSpec);
        mCenterBmp = scaleBitmap(mCenterBmp);
        setMeasuredDimension(width, height);
    }

    /**
     * 测量控件实际大小
     *
     * @param size
     * @param measureSpec
     * @return
     */
    private int resolveContentSize(int size, int measureSpec) {
        int result = size;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                result = size;
                break;
            case MeasureSpec.AT_MOST:
                result = Math.min(size, specSize);
                break;
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
        }
        return result;
    }

    /**
     * 缩放图片到指定画布大小
     *
     * @param bitmap 被缩放的图片
     * @return 缩放后的图片
     */
    private Bitmap scaleBitmap(Bitmap bitmap) {
        int size = Math.min(
                width - getPaddingLeft() - getPaddingRight(),
                height - getPaddingTop() - getPaddingBottom());
        Bitmap b = Bitmap.createScaledBitmap(bitmap, size, size, false);
        if (b != bitmap) {
            bitmap.recycle();
        }
        return b;
    }


    /**
     * 设置Progress的间图片
     *
     * @param res 图片资源
     */
    public void setCenterBitmap(int res) {
        mCenterBmp = BitmapFactory.decodeResource(getResources(), res);
    }

    /**
     * 设置Progress的颜色
     *
     * @param color 圆弧颜色
     */
    public void setArcColor(int color) {
        mPaint.setColor(color);
    }


    /**
     * 设置Progress的圆弧宽度
     *
     * @param sizeInDip 宽度的dp值
     */
    public void setArcWidth(float sizeInDip) {
        Resources res;
        res = getContext().getResources();

        float size = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, sizeInDip, res.getDisplayMetrics());
        mPaint.setStrokeWidth(size);
    }
}
