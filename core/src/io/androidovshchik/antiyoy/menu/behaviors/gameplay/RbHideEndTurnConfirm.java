package io.androidovshchik.antiyoy.menu.behaviors.gameplay;

import yio.tro.antiyoy.menu.ButtonYio;
import yio.tro.antiyoy.menu.behaviors.Reaction;
import yio.tro.antiyoy.menu.scenes.Scenes;

public class RbHideEndTurnConfirm extends Reaction {
    public void perform(ButtonYio buttonYio) {
        Scenes.sceneConfirmEndTurn.hide();
    }
}
