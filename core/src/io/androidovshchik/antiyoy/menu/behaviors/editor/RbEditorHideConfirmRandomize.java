package io.androidovshchik.antiyoy.menu.behaviors.editor;

import io.androidovshchik.antiyoy.menu.ButtonYio;
import io.androidovshchik.antiyoy.menu.behaviors.Reaction;
import io.androidovshchik.antiyoy.menu.scenes.Scenes;

public class RbEditorHideConfirmRandomize extends Reaction {
    public void perform(ButtonYio buttonYio) {
        Scenes.sceneEditorConfirmRandomize.hide();
    }
}
