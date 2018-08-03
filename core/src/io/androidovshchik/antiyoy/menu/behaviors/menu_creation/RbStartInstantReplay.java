package io.androidovshchik.antiyoy.menu.behaviors.menu_creation;

import yio.tro.antiyoy.menu.ButtonYio;
import yio.tro.antiyoy.menu.behaviors.Reaction;

public class RbStartInstantReplay extends Reaction {
    public void perform(ButtonYio buttonYio) {
        buttonYio.getMenuControllerYio().yioGdxGame.gameController.replayManager.startInstantReplay();
    }
}
