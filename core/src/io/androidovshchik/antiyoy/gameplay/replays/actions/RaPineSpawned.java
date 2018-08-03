package io.androidovshchik.antiyoy.gameplay.replays.actions;

import java.util.ArrayList;
import yio.tro.antiyoy.gameplay.FieldController;
import yio.tro.antiyoy.gameplay.GameController;
import yio.tro.antiyoy.gameplay.Hex;

public class RaPineSpawned extends RepAction {
    Hex hex;

    public RaPineSpawned(Hex hex) {
        this.hex = hex;
    }

    public void initType() {
        this.type = 5;
    }

    public String saveInfo() {
        return convertHexToTwoTokens(this.hex);
    }

    public void loadInfo(FieldController fieldController, String source) {
        ArrayList<String> strings = convertSourceStringToList(source);
        this.hex = getHexByTwoTokens(fieldController, (String) strings.get(0), (String) strings.get(1));
    }

    public void perform(GameController gameController) {
        gameController.fieldController.addSolidObject(this.hex, 1);
    }

    public String toString() {
        return "[Pine spawned: " + this.hex + "]";
    }
}
