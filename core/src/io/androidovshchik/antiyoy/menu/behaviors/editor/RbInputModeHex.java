package io.androidovshchik.antiyoy.menu.behaviors.editor;

import yio.tro.antiyoy.menu.ButtonYio;
import yio.tro.antiyoy.menu.behaviors.Reaction;
import yio.tro.antiyoy.menu.scenes.Scenes;

public class RbInputModeHex extends Reaction {
    public void perform(ButtonYio buttonYio) {
        getGameController(buttonYio).getLevelEditor().setInputMode(1);
        getGameController(buttonYio).getLevelEditor().setInputColor(buttonYio.id - 150);
        Scenes.sceneEditorHexPanel.hide();
    }
}
