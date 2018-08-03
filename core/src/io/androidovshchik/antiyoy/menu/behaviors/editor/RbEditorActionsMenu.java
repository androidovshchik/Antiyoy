package io.androidovshchik.antiyoy.menu.behaviors.editor;

import yio.tro.antiyoy.menu.ButtonYio;
import yio.tro.antiyoy.menu.behaviors.Reaction;
import yio.tro.antiyoy.menu.scenes.Scenes;

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
