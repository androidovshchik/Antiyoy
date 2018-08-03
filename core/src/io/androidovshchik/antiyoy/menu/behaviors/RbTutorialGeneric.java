package io.androidovshchik.antiyoy.menu.behaviors;

import io.androidovshchik.antiyoy.gameplay.rules.GameRules;
import io.androidovshchik.antiyoy.menu.ButtonYio;

public class RbTutorialGeneric extends Reaction {
    public void perform(ButtonYio buttonYio) {
        GameRules.setSlayRules(false);
        getGameController(buttonYio).initTutorial();
    }
}
