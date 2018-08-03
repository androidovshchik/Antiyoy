package io.androidovshchik.antiyoy.gameplay.replays.actions;

import java.util.ArrayList;
import yio.tro.antiyoy.gameplay.FieldController;
import yio.tro.antiyoy.gameplay.GameController;
import yio.tro.antiyoy.gameplay.Hex;

public class RaTowerBuilt extends RepAction {
    Hex hex;
    boolean strong;

    public RaTowerBuilt(Hex hex, boolean strong) {
        this.hex = hex;
        this.strong = strong;
    }

    public void initType() {
        this.type = 2;
    }

    public String saveInfo() {
        return convertHexToTwoTokens(this.hex) + this.strong;
    }

    public void loadInfo(FieldController fieldController, String source) {
        ArrayList<String> strings = convertSourceStringToList(source);
        this.hex = getHexByTwoTokens(fieldController, (String) strings.get(0), (String) strings.get(1));
        this.strong = Boolean.valueOf((String) strings.get(2)).booleanValue();
    }

    public void perform(GameController gameController) {
        if (this.strong) {
            gameController.fieldController.addSolidObject(this.hex, 7);
        } else {
            gameController.fieldController.addSolidObject(this.hex, 4);
        }
    }

    public String toString() {
        if (this.strong) {
            return "[Strong tower built: " + this.hex + "]";
        }
        return "[Tower built: " + this.hex + "]";
    }
}
