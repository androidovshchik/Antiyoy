package io.androidovshchik.antiyoy.menu.behaviors.gameplay;

import io.androidovshchik.antiyoy.menu.ButtonYio;
import io.androidovshchik.antiyoy.menu.behaviors.Reaction;

public class RbUndo extends Reaction {
    public void perform(ButtonYio buttonYio) {
        getGameController(buttonYio).undoAction();
    }
}
