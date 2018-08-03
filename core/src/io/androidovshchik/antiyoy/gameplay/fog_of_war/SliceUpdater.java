package io.androidovshchik.antiyoy.gameplay.fog_of_war;

import yio.tro.antiyoy.gameplay.Hex;

public class SliceUpdater {
    int downDir = 3;
    FogOfWarManager fogOfWarManager;
    int upDir = 0;

    public SliceUpdater(FogOfWarManager fogOfWarManager) {
        this.fogOfWarManager = fogOfWarManager;
    }

    void perform() {
        this.fogOfWarManager.clearSlices();
        for (FogPoint fogPoint : this.fogOfWarManager.fogMap.values()) {
            if (isPointGoodToStartSlice(fogPoint)) {
                makeSlice(fogPoint);
            }
        }
    }

    void makeSlice(FogPoint startPoint) {
        FogSlice next = (FogSlice) this.fogOfWarManager.poolSlices.getNext();
        FogPoint endPoint = startPoint;
        while (true) {
            next.points.add(endPoint);
            FogPoint adjacentFogPoint = getAdjacentFogPoint(endPoint, this.upDir);
            if (!(adjacentFogPoint == null || adjacentFogPoint.status)) {
                endPoint = adjacentFogPoint;
            }
        }
        next.setBottomPoint(startPoint);
        next.setTopPoint(endPoint);
        this.fogOfWarManager.viewSlices.add(next);
    }

    FogPoint getAdjacentFogPoint(FogPoint src, int dir) {
        Hex hex = src.hex;
        if (hex == null) {
            return null;
        }
        Hex adjacentHex = hex.getAdjacentHex(dir);
        if (adjacentHex != this.fogOfWarManager.fieldController.emptyHex) {
            return (FogPoint) this.fogOfWarManager.fogMap.get(adjacentHex);
        }
        return null;
    }

    boolean isPointGoodToStartSlice(FogPoint fogPoint) {
        if (fogPoint.status) {
            return false;
        }
        FogPoint belowPoint = getBelowPoint(fogPoint);
        if (belowPoint == null) {
            return true;
        }
        return belowPoint.status;
    }

    FogPoint getBelowPoint(FogPoint srcPoint) {
        return getAdjacentFogPoint(srcPoint, this.downDir);
    }
}
