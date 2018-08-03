package io.androidovshchik.antiyoy.menu.behaviors;

import yio.tro.antiyoy.gameplay.rules.GameRules;
import yio.tro.antiyoy.menu.ButtonYio;

public class RbTutorialSlay extends Reaction {
    public void perform(ButtonYio buttonYio) {
        GameRules.setSlayRules(true);
        getGameController(buttonYio).initTutorial();
    }
}
