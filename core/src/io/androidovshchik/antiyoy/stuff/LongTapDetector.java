package io.androidovshchik.antiyoy.stuff;

public abstract class LongTapDetector {
    public static final int LONG_TAP_DELAY = 600;
    PointYio currentTouch = new PointYio();
    int delay = LONG_TAP_DELAY;
    PointYio initialTouch = new PointYio();
    boolean performedCheck = false;
    long touchDownTime;
    boolean touched = false;

    public abstract void onLongTapDetected();

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public void onTouchDown(PointYio touchPoint) {
        this.initialTouch.setBy(touchPoint);
        this.currentTouch.setBy(this.initialTouch);
        this.performedCheck = false;
        this.touched = true;
        this.touchDownTime = System.currentTimeMillis();
    }

    public void onTouchDrag(PointYio touchPoint) {
        this.currentTouch.setBy(touchPoint);
    }

    public void onTouchUp(PointYio touchPoint) {
        this.touched = false;
    }

    public void move() {
        if (this.touched && !this.performedCheck && System.currentTimeMillis() - this.touchDownTime > ((long) this.delay)) {
            this.performedCheck = true;
            if (this.initialTouch.distanceTo(this.currentTouch) <= ((double) (0.05f * GraphicsYio.width))) {
                onLongTapDetected();
            }
        }
    }
}
