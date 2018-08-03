package io.androidovshchik.antiyoy.stuff;

import yio.tro.antiyoy.stuff.object_pool.ReusableYio;

public class PointYio implements ReusableYio {
    public float f144x;
    public float f145y;

    public PointYio() {
        reset();
    }

    public void reset() {
        set(0.0d, 0.0d);
    }

    public PointYio(double x, double y) {
        set(x, y);
    }

    public void set(double x, double y) {
        this.f144x = (float) x;
        this.f145y = (float) y;
    }

    public void setBy(PointYio p) {
        this.f144x = p.f144x;
        this.f145y = p.f145y;
    }

    public double distanceTo(PointYio pointYio) {
        return Yio.distance((double) this.f144x, (double) this.f145y, (double) pointYio.f144x, (double) pointYio.f145y);
    }

    public double fastDistanceTo(PointYio pointYio) {
        return (double) Yio.fastDistance((double) this.f144x, (double) this.f145y, (double) pointYio.f144x, (double) pointYio.f145y);
    }

    public double angleTo(PointYio pointYio) {
        return Yio.angle((double) this.f144x, (double) this.f145y, (double) pointYio.f144x, (double) pointYio.f145y);
    }

    public void relocateRadial(double distance, double angle) {
        this.f144x = (float) (((double) this.f144x) + (Math.cos(angle) * distance));
        this.f145y = (float) (((double) this.f145y) + (Math.sin(angle) * distance));
    }

    public String toString() {
        return "[Point: " + Yio.roundUp((double) this.f144x, 3) + ", " + Yio.roundUp((double) this.f145y, 3) + "]";
    }
}
