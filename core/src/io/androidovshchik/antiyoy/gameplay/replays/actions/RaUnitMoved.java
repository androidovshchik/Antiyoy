package io.androidovshchik.antiyoy.gameplay.replays.actions;

import java.util.ArrayList;
import yio.tro.antiyoy.gameplay.FieldController;
import yio.tro.antiyoy.gameplay.GameController;
import yio.tro.antiyoy.gameplay.Hex;
import yio.tro.antiyoy.gameplay.Province;
import yio.tro.antiyoy.gameplay.Unit;

public class RaUnitMoved extends RepAction {
    Hex dst;
    Hex src;

    public RaUnitMoved(Hex src, Hex dst) {
        this.src = src;
        this.dst = dst;
    }

    public void initType() {
        this.type = 1;
    }

    public String saveInfo() {
        return convertHexToTwoTokens(this.src) + convertHexToTwoTokens(this.dst);
    }

    public void loadInfo(FieldController fieldController, String source) {
        ArrayList<String> strings = convertSourceStringToList(source);
        this.src = getHexByTwoTokens(fieldController, (String) strings.get(0), (String) strings.get(1));
        this.dst = getHexByTwoTokens(fieldController, (String) strings.get(2), (String) strings.get(3));
    }

    public void perform(GameController gameController) {
        Unit unit = this.src.unit;
        Province provinceByHex = gameController.fieldController.getProvinceByHex(this.src);
        if (!(this.dst.sameColor(this.src) || this.dst.isNeutral() || this.dst.canBeAttackedBy(unit))) {
            System.out.println();
            System.out.println("Problem in RaUnitMoved.perform(), forbidden attack");
            System.out.println("src = " + this.src);
            System.out.println("unit.strength = " + unit.strength);
            System.out.println("dst = " + this.dst);
            System.out.println("dst.getDefenseNumber() = " + this.dst.getDefenseNumber());
        }
        if (unit == null) {
            System.out.println();
            System.out.println("Problem in RaUnitMoved.perform(). Unit is null");
            System.out.println("src = " + this.src);
            System.out.println("dst = " + this.dst);
            return;
        }
        gameController.moveUnit(unit, this.dst, provinceByHex);
    }

    public String toString() {
        return "[Unit moved from " + this.src + " to " + this.dst + "]";
    }
}
