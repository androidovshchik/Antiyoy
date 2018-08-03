package io.androidovshchik.antiyoy.menu.diplomacy_element;

import io.androidovshchik.antiyoy.factor_yio.FactorYio;
import io.androidovshchik.antiyoy.stuff.PointYio;

public class DeIcon {
    public static final int ACTION_BLACK_MARK = 2;
    public static final int ACTION_DISLIKE = 1;
    public static final int ACTION_INFO = 3;
    public static final int ACTION_LIKE = 0;
    public int action = -1;
    public FactorYio appearFactor = new FactorYio();
    public PointYio delta = new PointYio();
    DiplomacyElement diplomacyElement;
    public PointYio position = new PointYio();
    public float radius = 0.0f;
    public FactorYio selectionFactor = new FactorYio();
    public PointYio touchDelta = new PointYio();
    public float touchOffset = 0.0f;
    boolean visible = false;

    public DeIcon(DiplomacyElement diplomacyElement) {
        this.diplomacyElement = diplomacyElement;
    }

    public boolean isTouched(PointYio touchPoint) {
        if (isVisible()) {
            return this.diplomacyElement.isTouchInsideRectangle(touchPoint.f144x, touchPoint.f145y, (this.position.f144x - this.radius) + this.touchDelta.f144x, (this.position.f145y - this.radius) + this.touchDelta.f145y, this.radius * 2.0f, this.radius * 2.0f, this.touchOffset);
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
        this.position.f144x = (float) (this.diplomacyElement.viewPosition.f146x + ((double) this.delta.f144x));
        this.position.f145y = (float) (this.diplomacyElement.viewPosition.f147y + ((double) this.delta.f145y));
    }

    void appear() {
        this.appearFactor.setValues(1.0d, 0.0d);
        this.appearFactor.appear(1, 1.0d);
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
