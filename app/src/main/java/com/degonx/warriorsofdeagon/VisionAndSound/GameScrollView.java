package com.degonx.warriorsofdeagon.VisionAndSound;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

public class GameScrollView extends ScrollView {

    private int lastScrollX = 0, lastScrollY = 0;
    private static final int SCROLL_DURATION = 500; // Duration for smooth scrolling in milliseconds
    private static final int STOP_THRESHOLD = 20;   // Pixels threshold to stop scrolling


    public GameScrollView(Context context) {
        super(context);
    }

    public GameScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GameScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        scrollTo(lastScrollX, lastScrollY);
    }

    @Override
    public void requestLayout() {
        //save the current scroll position
        lastScrollX = getScrollX();
        lastScrollY = getScrollY();
        super.requestLayout();
    }

    public void smoothScrollToPosition(int targetX, int targetY) {
        final int startX = getScrollX();
        final int startY = getScrollY();
        final int dx = targetX - startX;
        final int dy = targetY - startY;

        final long startTime = System.currentTimeMillis();

        post(new Runnable() {
            @Override
            public void run() {
                long elapsed = System.currentTimeMillis() - startTime;
                float progress = Math.min(1.0f, (float) elapsed / SCROLL_DURATION);
                int currentX = (int) (startX + dx * progress);
                int currentY = (int) (startY + dy * progress);

                // Scroll to the computed position
                scrollTo(currentX, currentY);

                // Check if we are close enough to stop scrolling
                boolean isCloseEnoughX = Math.abs(targetX - currentX) <= STOP_THRESHOLD;
                boolean isCloseEnoughY = Math.abs(targetY - currentY) <= STOP_THRESHOLD;

                if (isCloseEnoughX && isCloseEnoughY) {
                    // Snap to the final target position and stop scrolling
                    scrollTo(targetX, targetY);
                } else {
                    // Continue scrolling if not close enough
                    post(this);
                }
            }
        });
    }
}