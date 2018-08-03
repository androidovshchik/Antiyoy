package io.androidovshchik.antiyoy.menu.behaviors.editor;

import io.androidovshchik.antiyoy.menu.ButtonYio;
import io.androidovshchik.antiyoy.menu.behaviors.Reaction;

public class RbEditorPlaceCapitalsOrFarms extends Reaction {
    public void perform(ButtonYio buttonYio) {
        getGameController(buttonYio).getLevelEditor().placeCapitalsOrFarms();
    }
}
