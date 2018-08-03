package io.androidovshchik.antiyoy.menu.behaviors.editor;

import io.androidovshchik.antiyoy.menu.ButtonYio;
import io.androidovshchik.antiyoy.menu.behaviors.Reaction;
import io.androidovshchik.antiyoy.menu.scenes.Scenes;

public class RbEditorActionsMenu extends Reaction {
    public void perform(ButtonYio buttonYio) {
        if (buttonYio.id != 140) {
            getGameController(buttonYio).getLevelEditor().setCurrentSlotNumber(buttonYio.id - 130);
        } else {
            getGameController(buttonYio).getLevelEditor().saveSlot();
        }
        Scenes.sceneEditorActions.create();
    }
}
