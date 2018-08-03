package io.androidovshchik.antiyoy.stuff.scroll_engine;

import io.androidovshchik.antiyoy.stuff.Yio;

public class SegmentYio {
    public double f107a = 0.0d;
    public double f108b = 0.0d;

    public void set(double a, double b) {
        this.f107a = a;
        this.f108b = b;
    }

    public double getLength() {
        return this.f108b - this.f107a;
    }

    public String toString() {
        return "(" + Yio.roundUp(this.f107a, 2) + ", " + Yio.roundUp(this.f108b, 2) + ")";
    }
}
