package io.androidovshchik.antiyoy.menu.behaviors.gameplay;

import yio.tro.antiyoy.menu.ButtonYio;
import yio.tro.antiyoy.menu.behaviors.Reaction;

public class RbUndo extends Reaction {
    public void perform(ButtonYio buttonYio) {
        getGameController(buttonYio).undoAction();
    }
}
