package io.androidovshchik.antiyoy.menu.behaviors;

import yio.tro.antiyoy.menu.ButtonYio;

public class RbDebugActions extends Reaction {
    public void perform(ButtonYio buttonYio) {
        getGameController(buttonYio).debugActions();
    }
}
