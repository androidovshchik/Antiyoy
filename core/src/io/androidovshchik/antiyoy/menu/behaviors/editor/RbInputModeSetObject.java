package io.androidovshchik.antiyoy.menu.behaviors.editor;

import yio.tro.antiyoy.menu.ButtonYio;
import yio.tro.antiyoy.menu.behaviors.Reaction;

public class RbInputModeSetObject extends Reaction {
    public void perform(ButtonYio buttonYio) {
        getGameController(buttonYio).getLevelEditor().setInputMode(2);
        getGameController(buttonYio).getLevelEditor().setInputObject(buttonYio.id - 162);
    }
}
