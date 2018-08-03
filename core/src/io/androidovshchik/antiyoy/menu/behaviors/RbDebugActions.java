package io.androidovshchik.antiyoy.menu.behaviors;

import io.androidovshchik.antiyoy.menu.ButtonYio;

public class RbDebugActions extends Reaction {
    public void perform(ButtonYio buttonYio) {
        getGameController(buttonYio).debugActions();
    }
}
