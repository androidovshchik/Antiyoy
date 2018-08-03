package io.androidovshchik.antiyoy.menu.behaviors.editor;

import yio.tro.antiyoy.menu.ButtonYio;
import yio.tro.antiyoy.menu.behaviors.Reaction;
import yio.tro.antiyoy.menu.scenes.Scenes;

public class RbEditorHideConfirmRandomize extends Reaction {
    public void perform(ButtonYio buttonYio) {
        Scenes.sceneEditorConfirmRandomize.hide();
    }
}
