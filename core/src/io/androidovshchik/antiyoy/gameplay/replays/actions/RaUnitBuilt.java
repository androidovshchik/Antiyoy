package io.androidovshchik.antiyoy.gameplay.replays.actions;

import java.util.ArrayList;
import io.androidovshchik.antiyoy.gameplay.FieldController;
import io.androidovshchik.antiyoy.gameplay.GameController;
import io.androidovshchik.antiyoy.gameplay.Hex;
import io.androidovshchik.antiyoy.gameplay.Province;

public class RaUnitBuilt extends RepAction {
    Hex dst;
    Hex src;
    int strength;

    public RaUnitBuilt(Hex src, Hex dst, int strength) {
        this.src = src;
        this.dst = dst;
        this.strength = strength;
    }

    public void initType() {
        this.type = 0;
    }

    public String saveInfo() {
        return convertHexToTwoTokens(this.src) + convertHexToTwoTokens(this.dst) + " " + this.strength;
    }

    public void loadInfo(FieldController fieldController, String source) {
        ArrayList<String> strings = convertSourceStringToList(source);
        this.src = getHexByTwoTokens(fieldController, (String) strings.get(0), (String) strings.get(1));
        this.dst = getHexByTwoTokens(fieldController, (String) strings.get(2), (String) strings.get(3));
        this.strength = Integer.valueOf((String) strings.get(4)).intValue();
    }

    public void perform(GameController gameController) {
        FieldController fieldController = gameController.fieldController;
        Province provinceByHex = fieldController.getProvinceByHex(this.src);
        if (!fieldController.buildUnit(provinceByHex, this.dst, this.strength)) {
            System.out.println();
            System.out.println("Problem in RaUnitBuilt.perform()");
            System.out.println("src = " + this.src);
            System.out.println("dst = " + this.dst);
            System.out.println("strength = " + this.strength);
            System.out.println("provinceByHex = " + provinceByHex);
        }
    }

    public String toString() {
        return "[Unit built: " + this.src + " to " + this.dst + " with strength " + this.strength + "]";
    }
}
