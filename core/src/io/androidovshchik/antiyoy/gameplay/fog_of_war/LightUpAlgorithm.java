package io.androidovshchik.antiyoy.gameplay.fog_of_war;

import java.util.ArrayList;
import yio.tro.antiyoy.gameplay.Hex;

public class LightUpAlgorithm {
    FogOfWarManager fogOfWarManager;
    ArrayList<Hex> queue = new ArrayList();
    ArrayList<Hex> used = new ArrayList();

    public LightUpAlgorithm(FogOfWarManager fogOfWarManager) {
        this.fogOfWarManager = fogOfWarManager;
    }

    void perform(Hex start, int radius) {
        begin(start, radius);
        while (this.queue.size() > 0) {
            Hex hex = (Hex) this.queue.get(0);
            this.queue.remove(0);
            deactivateFogPoint(hex);
            tagAsUsed(hex);
            if (hex.active && hex.moveZoneNumber != 0) {
                for (int dir = 0; dir < 6; dir++) {
                    Hex adjacentHex = hex.getAdjacentHex(dir);
                    if (!(adjacentHex == null || isUsed(adjacentHex) || this.queue.contains(adjacentHex))) {
                        adjacentHex.moveZoneNumber = hex.moveZoneNumber - 1;
                        this.queue.add(adjacentHex);
                    }
                }
            }
        }
    }

    private boolean isUsed(Hex hex) {
        return this.used.contains(hex);
    }

    private void tagAsUsed(Hex hex) {
        this.used.add(hex);
    }

    private void deactivateFogPoint(Hex hex) {
        FogPoint fogPoint = (FogPoint) this.fogOfWarManager.fogMap.get(hex);
        if (fogPoint != null) {
            fogPoint.setStatus(false);
        }
    }

    private void begin(Hex start, int radius) {
        this.queue.clear();
        this.used.clear();
        this.queue.add(start);
        start.moveZoneNumber = radius;
    }
}
