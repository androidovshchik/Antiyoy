package io.androidovshchik.antiyoy.gameplay;

import java.util.ArrayList;
import java.util.Iterator;

public class MoveZoneDetection {
    private Hex adjHex;
    private final FieldController fieldController;
    private ArrayList<Hex> propagationList = new ArrayList();
    private ArrayList<Hex> result = new ArrayList();
    private Hex tempHex;

    public MoveZoneDetection(FieldController fieldController) {
        this.fieldController = fieldController;
    }

    public static void unFlagAllHexesInArrayList(ArrayList<Hex> hexList) {
        for (int i = hexList.size() - 1; i >= 0; i--) {
            ((Hex) hexList.get(i)).flag = false;
            ((Hex) hexList.get(i)).inMoveZone = false;
        }
    }

    public ArrayList<Hex> detectMoveZoneForFarm() {
        this.fieldController.clearMoveZone();
        unFlagAllHexesInArrayList(this.fieldController.activeHexes);
        this.result.clear();
        Iterator it = this.fieldController.selectedProvince.hexList.iterator();
        while (it.hasNext()) {
            Hex hex = (Hex) it.next();
            if (canBuildFarmOnHex(hex)) {
                hex.inMoveZone = true;
                this.result.add(hex);
            }
        }
        return this.result;
    }

    public static boolean canBuildFarmOnHex(Hex hex) {
        return hex.hasThisObjectNearby(6) || hex.hasThisObjectNearby(3);
    }

    public ArrayList<Hex> detectMoveZone(Hex startHex, int strength) {
        return detectMoveZone(startHex, strength, 9001);
    }

    public ArrayList<Hex> detectMoveZone(Hex startHex, int strength, int moveLimit) {
        unFlagAllHexesInArrayList(this.fieldController.activeHexes);
        beginDetection(startHex, moveLimit);
        while (this.propagationList.size() > 0) {
            iteratePropagation(startHex, strength);
        }
        return this.result;
    }

    private void iteratePropagation(Hex startHex, int strength) {
        this.tempHex = (Hex) this.propagationList.get(0);
        this.propagationList.remove(0);
        this.tempHex.inMoveZone = true;
        this.result.add(this.tempHex);
        if (this.tempHex.sameColor(startHex) && this.tempHex.moveZoneNumber != 0) {
            for (int i = 0; i < 6; i++) {
                this.adjHex = this.tempHex.getAdjacentHex(i);
                if (this.adjHex.active && !this.adjHex.flag) {
                    if (this.adjHex.sameColor(startHex)) {
                        this.propagationList.add(this.adjHex);
                        this.adjHex.moveZoneNumber = this.tempHex.moveZoneNumber - 1;
                        this.adjHex.flag = true;
                    } else if (this.fieldController.gameController.canUnitAttackHex(strength, startHex.colorIndex, this.adjHex)) {
                        this.propagationList.add(this.adjHex);
                        this.adjHex.flag = true;
                    }
                }
            }
        }
    }

    private void beginDetection(Hex startHex, int moveLimit) {
        this.result.clear();
        this.propagationList.clear();
        this.propagationList.add(startHex);
        startHex.moveZoneNumber = moveLimit;
    }
}
