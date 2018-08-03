package io.androidovshchik.antiyoy.gameplay.replays.actions;

import java.util.ArrayList;
import yio.tro.antiyoy.gameplay.FieldController;
import yio.tro.antiyoy.gameplay.GameController;
import yio.tro.antiyoy.gameplay.Hex;

public class RaCitySpawned extends RepAction {
    Hex hex;

    public RaCitySpawned(Hex hex) {
        this.hex = hex;
    }

    public void initType() {
        this.type = 7;
    }

    public String saveInfo() {
        return convertHexToTwoTokens(this.hex);
    }

    public void loadInfo(FieldController fieldController, String source) {
        ArrayList<String> strings = convertSourceStringToList(source);
        this.hex = getHexByTwoTokens(fieldController, (String) strings.get(0), (String) strings.get(1));
    }

    public void perform(GameController gameController) {
        if (this.hex.containsUnit()) {
            gameController.fieldController.cleanOutHex(this.hex);
        }
        gameController.addSolidObject(this.hex, 3);
    }

    public String toString() {
        return "[City spawned: " + this.hex + "]";
    }
}
