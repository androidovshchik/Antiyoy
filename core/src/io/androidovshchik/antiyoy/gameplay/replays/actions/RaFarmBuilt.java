package io.androidovshchik.antiyoy.gameplay.replays.actions;

import java.util.ArrayList;
import io.androidovshchik.antiyoy.gameplay.FieldController;
import io.androidovshchik.antiyoy.gameplay.GameController;
import io.androidovshchik.antiyoy.gameplay.Hex;

public class RaFarmBuilt extends RepAction {
    Hex hex;

    public RaFarmBuilt(Hex hex) {
        this.hex = hex;
    }

    public void initType() {
        this.type = 3;
    }

    public String saveInfo() {
        return convertHexToTwoTokens(this.hex);
    }

    public void loadInfo(FieldController fieldController, String source) {
        ArrayList<String> strings = convertSourceStringToList(source);
        this.hex = getHexByTwoTokens(fieldController, (String) strings.get(0), (String) strings.get(1));
    }

    public void perform(GameController gameController) {
        gameController.fieldController.addSolidObject(this.hex, 6);
    }

    public String toString() {
        return "[Farm built: " + this.hex + "]";
    }
}
