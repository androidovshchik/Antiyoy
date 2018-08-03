package io.androidovshchik.antiyoy.menu.fast_construction;

import io.androidovshchik.antiyoy.factor_yio.FactorYio;
import io.androidovshchik.antiyoy.stuff.PointYio;

public class FcpItem {
    public static final int ACTION_DIPLOMACY = 10;
    public static final int ACTION_END_TURN = 9;
    public static final int ACTION_FARM = 5;
    public static final int ACTION_STRONG_TOWER = 7;
    public static final int ACTION_TOWER = 6;
    public static final int ACTION_UNDO = 8;
    public static final int ACTION_UNIT_1 = 1;
    public static final int ACTION_UNIT_2 = 2;
    public static final int ACTION_UNIT_3 = 3;
    public static final int ACTION_UNIT_4 = 4;
    public int action = -1;
    public PointYio animDelta = new PointYio();
    public PointYio delta = new PointYio();
    FastConstructionPanel fastConstructionPanel;
    public PointYio position = new PointYio();
    public float radius = 0.0f;
    public FactorYio selectionFactor = new FactorYio();
    public PointYio touchDelta = new PointYio();
    public float touchOffset = 0.0f;
    boolean visible = false;

    public FcpItem(FastConstructionPanel fastConstructionPanel) {
        this.fastConstructionPanel = fastConstructionPanel;
    }

    public boolean isTouched(PointYio touchPoint) {
        if (isVisible()) {
            return this.fastConstructionPanel.isTouchInsideRectangle(touchPoint.f144x, touchPoint.f145y, (this.position.f144x - this.radius) + this.touchDelta.f144x, (this.position.f145y - this.radius) + this.touchDelta.f145y, this.radius * 2.0f, this.radius * 2.0f, this.touchOffset);
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
        this.position.f144x = (float) (this.fastConstructionPanel.viewPosition.f146x + ((double) this.delta.f144x));
        this.position.f145y = (float) (this.fastConstructionPanel.viewPosition.f147y + ((double) this.delta.f145y));
        if (this.fastConstructionPanel.appearFactor.get() < 1.0f) {
            PointYio pointYio = this.position;
            pointYio.f144x += (1.0f - this.fastConstructionPanel.appearFactor.get()) * this.animDelta.f144x;
            pointYio = this.position;
            pointYio.f145y += (1.0f - this.fastConstructionPanel.appearFactor.get()) * this.animDelta.f145y;
        }
    }

    public boolean isVisible() {
        return this.visible;
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
