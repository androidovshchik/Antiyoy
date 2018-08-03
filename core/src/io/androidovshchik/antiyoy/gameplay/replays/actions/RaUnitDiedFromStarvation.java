package io.androidovshchik.antiyoy.gameplay.replays.actions;

import java.util.ArrayList;
import io.androidovshchik.antiyoy.gameplay.FieldController;
import io.androidovshchik.antiyoy.gameplay.GameController;
import io.androidovshchik.antiyoy.gameplay.Hex;

public class RaUnitDiedFromStarvation extends RepAction {
    Hex hex;

    public RaUnitDiedFromStarvation(Hex hex) {
        this.hex = hex;
    }

    public void initType() {
        this.type = 8;
    }

    public String saveInfo() {
        return convertHexToTwoTokens(this.hex);
    }

    public void loadInfo(FieldController fieldController, String source) {
        ArrayList<String> strings = convertSourceStringToList(source);
        this.hex = getHexByTwoTokens(fieldController, (String) strings.get(0), (String) strings.get(1));
    }

    public void perform(GameController gameController) {
        gameController.fieldController.killUnitByStarvation(this.hex);
    }

    public String toString() {
        return "[Unit dies from starvation:" + this.hex + "]";
    }
}
