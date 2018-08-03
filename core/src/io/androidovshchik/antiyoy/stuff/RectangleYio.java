package io.androidovshchik.antiyoy.stuff;

import yio.tro.antiyoy.stuff.object_pool.ReusableYio;

public class RectangleYio implements ReusableYio {
    public double height;
    public double width;
    public double f146x;
    public double f147y;

    public RectangleYio() {
        this(0.0d, 0.0d, 0.0d, 0.0d);
    }

    public void reset() {
        set(0.0d, 0.0d, 0.0d, 0.0d);
    }

    public RectangleYio(double x, double y, double width, double height) {
        set(x, y, width, height);
    }

    public RectangleYio(RectangleYio src) {
        setBy(src);
    }

    public void set(double x, double y, double width, double height) {
        this.f146x = x;
        this.f147y = y;
        this.width = width;
        this.height = height;
    }

    public void setBy(RectangleYio src) {
        set(src.f146x, src.f147y, src.width, src.height);
    }

    public boolean isPointInside(PointYio pointYio, float offset) {
        if (((double) pointYio.f144x) >= this.f146x - ((double) offset) && ((double) pointYio.f145y) >= this.f147y - ((double) offset) && ((double) pointYio.f144x) <= (this.f146x + this.width) + ((double) offset) && ((double) pointYio.f145y) <= (this.f147y + this.height) + ((double) offset)) {
            return true;
        }
        return false;
    }

    public String toString() {
        return "(" + Yio.roundUp(this.f146x, 3) + ", " + Yio.roundUp(this.f147y, 3) + ", " + Yio.roundUp(this.width, 3) + ", " + Yio.roundUp(this.height, 3) + ")";
    }
}
