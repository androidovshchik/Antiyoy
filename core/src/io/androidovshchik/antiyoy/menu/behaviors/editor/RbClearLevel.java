package io.androidovshchik.antiyoy.menu.behaviors.editor;

import io.androidovshchik.antiyoy.menu.ButtonYio;
import io.androidovshchik.antiyoy.menu.behaviors.Reaction;
import io.androidovshchik.antiyoy.menu.scenes.Scenes;

public class RbClearLevel extends Reaction {
    public void perform(ButtonYio buttonYio) {
        Scenes.sceneEditorConfirmClear.hide();
        getGameController(buttonYio).clearLevel();
    }
}
