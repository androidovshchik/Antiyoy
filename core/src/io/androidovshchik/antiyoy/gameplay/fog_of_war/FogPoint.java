package io.androidovshchik.antiyoy.gameplay.fog_of_war;

import io.androidovshchik.antiyoy.gameplay.Hex;
import io.androidovshchik.antiyoy.stuff.PointYio;
import io.androidovshchik.antiyoy.stuff.object_pool.ReusableYio;

public class FogPoint implements ReusableYio {
    FogOfWarManager fogOfWarManager;
    Hex hex;
    public PointYio position = new PointYio();
    public boolean status;

    public FogPoint(FogOfWarManager fogOfWarManager) {
        this.fogOfWarManager = fogOfWarManager;
    }

    public void reset() {
        this.hex = null;
        this.position.reset();
        this.status = true;
    }

    public boolean isVisible() {
        return this.status && this.fogOfWarManager.visibleArea.isPointInside(this.position, this.fogOfWarManager.fieldController.hexSize);
    }

    public void setHexByIndexes(int i, int j) {
        this.hex = this.fogOfWarManager.fieldController.getHex(i, j);
        this.fogOfWarManager.fieldController.updatePointByHexIndexes(this.position, i, j);
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String toString() {
        return "[FogPoint: " + this.hex + " " + this.status + "]";
    }
}
