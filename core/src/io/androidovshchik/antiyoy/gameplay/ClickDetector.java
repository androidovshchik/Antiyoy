package io.androidovshchik.antiyoy.gameplay;

import io.androidovshchik.antiyoy.stuff.GraphicsYio;
import io.androidovshchik.antiyoy.stuff.PointYio;

public class ClickDetector {
    public static final int CLICK_DELAY = 250;
    float clickDistance = (0.02f * GraphicsYio.width);
    float currentDistance;
    float maxDistance;
    PointYio touchDownPoint = new PointYio();
    long touchDownTime;

    public boolean isClicked() {
        if (System.currentTimeMillis() - this.touchDownTime <= 250 && this.maxDistance <= this.clickDistance) {
            return true;
        }
        return false;
    }

    public boolean touchDown(PointYio touchPoint) {
        this.touchDownPoint.setBy(touchPoint);
        this.touchDownTime = System.currentTimeMillis();
        this.maxDistance = 0.0f;
        return false;
    }

    public boolean touchDrag(PointYio touchPoint) {
        checkToUpdateMaxDistance(touchPoint);
        return false;
    }

    public boolean touchUp(PointYio touchPoint) {
        checkToUpdateMaxDistance(touchPoint);
        return false;
    }

    private void checkToUpdateMaxDistance(PointYio touchPoint) {
        this.currentDistance = (float) this.touchDownPoint.distanceTo(touchPoint);
        if (this.currentDistance > this.maxDistance) {
            this.maxDistance = this.currentDistance;
        }
    }
}
