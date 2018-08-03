package io.androidovshchik.antiyoy.menu.behaviors.gameplay;

import yio.tro.antiyoy.menu.ButtonYio;
import yio.tro.antiyoy.menu.behaviors.Reaction;
import yio.tro.antiyoy.menu.scenes.Scenes;

public class RbShowColorStats extends Reaction {
    public void perform(ButtonYio buttonYio) {
        Scenes.sceneColorStats.create();
        new ColorStatsRenderer(buttonYio.menuControllerYio).renderStatButton(buttonYio.menuControllerYio.getButtonById(56321), getGameController(buttonYio).fieldController.getPlayerHexCount());
    }
}
