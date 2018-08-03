package io.androidovshchik.antiyoy.menu.FireworksElement;

import io.androidovshchik.antiyoy.stuff.PointYio;
import io.androidovshchik.antiyoy.stuff.object_pool.ReusableYio;

public class FeSequence implements ReusableYio {
    boolean activated;
    PointYio position = new PointYio();
    long time;

    public void reset() {
        this.position.set(0.0d, 0.0d);
        this.activated = false;
        this.time = 0;
    }

    boolean isReady() {
        return System.currentTimeMillis() > this.time;
    }
}
