package io.androidovshchik.antiyoy.gameplay.replays.actions;

import yio.tro.antiyoy.gameplay.FieldController;
import yio.tro.antiyoy.gameplay.GameController;

public class RaTurnEnded extends RepAction {
    public void initType() {
        this.type = 6;
    }

    public String saveInfo() {
        return "";
    }

    public void loadInfo(FieldController fieldController, String source) {
    }

    public void perform(GameController gameController) {
        gameController.applyReadyToEndTurn();
    }

    public String toString() {
        return "[Turn ended]";
    }
}
