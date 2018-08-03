package io.androidovshchik.antiyoy.menu.behaviors.gameplay;

import io.androidovshchik.antiyoy.menu.ButtonYio;
import io.androidovshchik.antiyoy.menu.behaviors.Reaction;
import io.androidovshchik.antiyoy.menu.scenes.Scenes;

public class RbHideColorStats extends Reaction {
    public void perform(ButtonYio buttonYio) {
        Scenes.sceneColorStats.hide();
    }
}
