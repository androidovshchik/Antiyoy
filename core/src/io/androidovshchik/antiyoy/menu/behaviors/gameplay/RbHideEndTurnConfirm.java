package io.androidovshchik.antiyoy.menu.behaviors.gameplay;

import io.androidovshchik.antiyoy.menu.ButtonYio;
import io.androidovshchik.antiyoy.menu.behaviors.Reaction;
import io.androidovshchik.antiyoy.menu.scenes.Scenes;

public class RbHideEndTurnConfirm extends Reaction {
    public void perform(ButtonYio buttonYio) {
        Scenes.sceneConfirmEndTurn.hide();
    }
}
