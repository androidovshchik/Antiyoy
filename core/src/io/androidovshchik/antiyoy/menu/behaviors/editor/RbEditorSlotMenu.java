package io.androidovshchik.antiyoy.menu.behaviors.editor;

import yio.tro.antiyoy.menu.ButtonYio;
import yio.tro.antiyoy.menu.behaviors.Reaction;
import yio.tro.antiyoy.menu.scenes.Scenes;

public class RbEditorSlotMenu extends Reaction {
    public void perform(ButtonYio buttonYio) {
        Scenes.sceneEditorSlots.create();
        getGameController(buttonYio).turnOffEditorMode();
    }
}