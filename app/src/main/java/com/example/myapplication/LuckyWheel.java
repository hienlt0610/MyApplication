package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

class LuckyWheel extends View implements GestureDetector.OnGestureListener {
    Paint paint;
    GestureDetector gestureDetector;


    public LuckyWheel(Context context) {
        super(context);
        paint = new Paint();
        paint.setStrokeWidth(5);
        paint.setAntiAlias(true);
        paint.setColor(Color.RED);
        gestureDetector = new GestureDetector(getContext(), this);
        setLongClickable(true);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < 10; i++) {
            float sweepItemAngel = 360 / 10f;
            if (i == 0) {
                paint.setColor(Color.BLACK);
            } else {
                paint.setColor(getRandomColor());
            }
            canvas.drawArc(new RectF(0, 0, getWidth(), getWidth()), 270 + i * sweepItemAngel - (sweepItemAngel / 2f), sweepItemAngel, true, paint);
        }
    }

    public int getRandomColor() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.d("hienlt0610", "velocityX: " + velocityX);
        Log.d("hienlt0610", "velocityY: " + velocityY);
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}
