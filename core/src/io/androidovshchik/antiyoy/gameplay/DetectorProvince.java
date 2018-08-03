package io.androidovshchik.antiyoy.gameplay;

import java.util.ArrayList;

public class DetectorProvince {
    private Hex adjHex;
    private ArrayList<Hex> propagationList = new ArrayList();
    private ArrayList<Hex> provinceList = new ArrayList();
    private Hex tempHex;

    public ArrayList<Hex> detectProvince(Hex startHex) {
        this.provinceList.clear();
        this.propagationList.clear();
        this.propagationList.add(startHex);
        if (startHex.colorIndex == FieldController.NEUTRAL_LANDS_INDEX) {
            this.provinceList.add(startHex);
            return this.provinceList;
        }
        while (this.propagationList.size() > 0) {
            this.tempHex = (Hex) this.propagationList.get(0);
            this.provinceList.add(this.tempHex);
            this.propagationList.remove(0);
            for (int i = 0; i < 6; i++) {
                this.adjHex = this.tempHex.getAdjacentHex(i);
                if (belongsToSameProvince(this.adjHex)) {
                    this.propagationList.add(this.adjHex);
                }
            }
        }
        return this.provinceList;
    }

    private boolean belongsToSameProvince(Hex adjHex) {
        if (adjHex == null || !adjHex.active || !adjHex.sameColor(this.tempHex) || this.propagationList.contains(adjHex) || this.provinceList.contains(adjHex)) {
            return false;
        }
        return true;
    }
}
