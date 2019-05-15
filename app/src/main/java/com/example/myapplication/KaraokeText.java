package com.example.myapplication;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;

public class KaraokeText extends AppCompatTextView implements ValueAnimator.AnimatorUpdateListener {
    private static final String TAG = "KaraokeText";
    private Paint paint;
    private float posX = 5;
    private ValueAnimator valueAnimator;

    public KaraokeText(Context context) {
        super(context);
        init();
    }

    public KaraokeText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public KaraokeText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setLayerType(LAYER_TYPE_HARDWARE, null);
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLUE);
        paint.setAntiAlias(true);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (valueAnimator == null) {
            valueAnimator = ValueAnimator.ofFloat(0, getWidth());
            valueAnimator.setDuration(10000);
            valueAnimator.addUpdateListener(this);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate(-getWidth() + posX, 0);
        canvas.drawRect(0,0, getWidth(), getHeight(), paint);
        canvas.restore();
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        float value = (float) animation.getAnimatedValue();
        Log.d(TAG, "update: " + value);
        posX = value;
        invalidate();
    }

    public void start() {
        post(new Runnable() {
            @Override
            public void run() {
                if (valueAnimator != null && !valueAnimator.isRunning()) {
                    valueAnimator.start();
                }
            }
        });
    }

    public void stop() {
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
    }
}
