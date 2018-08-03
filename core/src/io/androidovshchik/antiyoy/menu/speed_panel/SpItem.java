package io.androidovshchik.antiyoy.menu.speed_panel;

import io.androidovshchik.antiyoy.factor_yio.FactorYio;
import io.androidovshchik.antiyoy.stuff.PointYio;

public class SpItem {
    public static final int ACTION_FAST_FORWARD = 2;
    public static final int ACTION_PLAY_PAUSE = 1;
    public static final int ACTION_SAVE = 3;
    public static final int ACTION_STOP = 0;
    public int action;
    public FactorYio appearFactor = new FactorYio();
    public PointYio delta = new PointYio();
    public PointYio position = new PointYio();
    public float radius = 0.0f;
    public FactorYio selectionFactor = new FactorYio();
    SpeedPanel speedPanel;
    public float touchOffset = 0.0f;

    public SpItem(SpeedPanel speedPanel) {
        this.speedPanel = speedPanel;
        defaultAppearFactorState();
        this.action = -1;
    }

    public void defaultAppearFactorState() {
        this.appearFactor.setValues(1.0d, 0.0d);
        this.appearFactor.appear(1, 1.0d);
    }

    public boolean isTouched(PointYio touchPoint) {
        if (isVisible()) {
            return this.speedPanel.isTouchInsideRectangle(touchPoint.f144x, touchPoint.f145y, this.position.f144x - this.radius, this.position.f145y - this.radius, this.radius * 2.0f, this.radius * 2.0f, this.touchOffset);
        }
        return false;
    }

    public boolean isSelected() {
        return this.selectionFactor.get() > 0.0f;
    }

    void select() {
        this.selectionFactor.setValues(1.0d, 0.0d);
        this.selectionFactor.destroy(1, 2.5d);
    }

    void move() {
        this.selectionFactor.move();
        this.appearFactor.move();
        this.position.f144x = (float) (this.speedPanel.viewPosition.f146x + ((double) this.delta.f144x));
        this.position.f145y = (float) (this.speedPanel.viewPosition.f147y + ((double) this.delta.f145y));
    }

    public boolean isVisible() {
        return this.appearFactor.get() > 0.0f;
    }

    void destroy() {
        this.appearFactor.destroy(1, 0.5d);
    }

    public void setDelta(double x, double y) {
        this.delta.set(x, y);
    }

    public void setAction(int action) {
        this.action = action;
    }

    public void setTouchOffset(float touchOffset) {
        this.touchOffset = touchOffset;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }
}
