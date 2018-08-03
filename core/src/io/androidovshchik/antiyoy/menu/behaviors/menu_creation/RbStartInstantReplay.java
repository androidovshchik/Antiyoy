package io.androidovshchik.antiyoy.menu.behaviors.menu_creation;

import io.androidovshchik.antiyoy.menu.ButtonYio;
import io.androidovshchik.antiyoy.menu.behaviors.Reaction;

public class RbStartInstantReplay extends Reaction {
    public void perform(ButtonYio buttonYio) {
        buttonYio.getMenuControllerYio().yioGdxGame.gameController.replayManager.startInstantReplay();
    }
}
