package io.androidovshchik.antiyoy.menu.behaviors.editor;

import yio.tro.antiyoy.menu.ButtonYio;
import yio.tro.antiyoy.menu.behaviors.Reaction;

public class RbEditorExpandProvinces extends Reaction {
    public void perform(ButtonYio buttonYio) {
        getGameController(buttonYio).getLevelEditor().expandProvinces();
    }
}
