package com.degonx.warriorsofdeagon.VisionAndSound;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

public class JoyStickClass {
    public static final int STICK_NONE = 0;
    public static final int STICK_UP = 1;
    public static final int STICK_UPRIGHT = 2;
    public static final int STICK_RIGHT = 3;
    public static final int STICK_DOWNRIGHT = 4;
    public static final int STICK_DOWN = 5;
    public static final int STICK_DOWNLEFT = 6;
    public static final int STICK_LEFT = 7;
    public static final int STICK_UPLEFT = 8;

    private final ViewGroup mLayout;
    private final LayoutParams params;
    private final DrawCanvas draw;
    private final Paint paint;
    private Bitmap stick;

    private int stickWidth, stickHeight;
    private int minDistance = 0;
    private float distance = 0;
    private double angle = 0;
    private int offset = 0;
    private boolean touchState = false;

    public JoyStickClass(Context context, ViewGroup layout, int stickResId) {
        stick = BitmapFactory.decodeResource(context.getResources(), stickResId);

        stickWidth = stick.getWidth();
        stickHeight = stick.getHeight();

        draw = new DrawCanvas(context);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG); // Anti-aliasing for smoother rendering.
        mLayout = layout;
        params = mLayout.getLayoutParams();
    }

    public void drawStick(MotionEvent event) {
        int centerX = params.width / 2;
        int centerY = params.height / 2;

        float posX = event.getX() - centerX;
        float posY = event.getY() - centerY;

        distance = (float) Math.sqrt(posX * posX + posY * posY);
        angle = calculateAngle(posX, posY);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (distance <= centerX - offset) {
                    updateStickPosition(event.getX(), event.getY());
                    touchState = true;
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (touchState) {
                    if (distance <= centerX - offset) {
                        updateStickPosition(event.getX(), event.getY());
                    } else {
                        float boundedX = (float) (Math.cos(Math.toRadians(angle)) * (centerX - offset)) + centerX;
                        float boundedY = (float) (Math.sin(Math.toRadians(angle)) * (centerY - offset)) + centerY;
                        updateStickPosition(boundedX, boundedY);
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                resetStick();
                touchState = false;
                break;
        }
    }

    public int getDirection() {
        if (!touchState || distance <= minDistance) {
            return STICK_NONE;
        }
        if (angle >= 247.5 && angle < 292.5) return STICK_UP;
        if (angle >= 292.5 && angle < 337.5) return STICK_UPRIGHT;
        if (angle >= 337.5 || angle < 22.5) return STICK_RIGHT;
        if (angle >= 22.5 && angle < 67.5) return STICK_DOWNRIGHT;
        if (angle >= 67.5 && angle < 112.5) return STICK_DOWN;
        if (angle >= 112.5 && angle < 157.5) return STICK_DOWNLEFT;
        if (angle >= 157.5 && angle < 202.5) return STICK_LEFT;
        if (angle >= 202.5) return STICK_UPLEFT;
        return STICK_NONE;
    }

    public void setMinimumDistance(int minDistance) {
        this.minDistance = minDistance;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setStickSize(int width, int height) {
        stick = Bitmap.createScaledBitmap(stick, width, height, false);
        stickWidth = stick.getWidth();
        stickHeight = stick.getHeight();
    }

    public void setLayoutSize(int width, int height) {
        params.width = width;
        params.height = height;
    }

    private double calculateAngle(float x, float y) {
        double angle = Math.toDegrees(Math.atan2(y, x));
        return angle < 0 ? angle + 360 : angle;
    }

    private void updateStickPosition(float posX, float posY) {
        draw.setPosition(posX - (float) stickWidth / 2, posY - (float) stickHeight / 2);
        refreshCanvas();
    }

    private void resetStick() {
        removeCanvas();
    }

    private void refreshCanvas() {
        removeCanvas();
        mLayout.addView(draw);
    }

    private void removeCanvas() {
        try {
            mLayout.removeView(draw);
        } catch (Exception ignored) {
        }
    }

    private class DrawCanvas extends View {
        private float x, y;

        public DrawCanvas(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawBitmap(stick, x, y, paint);
        }

        public void setPosition(float x, float y) {
            this.x = x;
            this.y = y;
            invalidate();
        }
    }
}
