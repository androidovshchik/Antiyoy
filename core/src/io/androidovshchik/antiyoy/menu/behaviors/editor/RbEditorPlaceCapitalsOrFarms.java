package io.androidovshchik.antiyoy.menu.behaviors.editor;

import yio.tro.antiyoy.menu.ButtonYio;
import yio.tro.antiyoy.menu.behaviors.Reaction;

public class RbEditorPlaceCapitalsOrFarms extends Reaction {
    public void perform(ButtonYio buttonYio) {
        getGameController(buttonYio).getLevelEditor().placeCapitalsOrFarms();
    }
}
