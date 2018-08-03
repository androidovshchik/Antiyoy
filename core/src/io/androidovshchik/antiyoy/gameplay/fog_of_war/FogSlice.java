package io.androidovshchik.antiyoy.gameplay.fog_of_war;

import java.util.ArrayList;
import io.androidovshchik.antiyoy.stuff.object_pool.ReusableYio;

public class FogSlice implements ReusableYio {
    public FogPoint bottomPoint;
    public ArrayList<FogPoint> points = new ArrayList();
    public FogPoint topPoint;

    public void reset() {
        this.topPoint = null;
        this.bottomPoint = null;
        this.points.clear();
    }

    public void setBottomPoint(FogPoint bottomPoint) {
        this.bottomPoint = bottomPoint;
    }

    public void setTopPoint(FogPoint topPoint) {
        this.topPoint = topPoint;
    }
}
