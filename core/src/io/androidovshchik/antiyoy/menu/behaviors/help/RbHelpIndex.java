package io.androidovshchik.antiyoy.menu.behaviors.help;

import yio.tro.antiyoy.menu.ButtonYio;
import yio.tro.antiyoy.menu.behaviors.Reaction;
import yio.tro.antiyoy.menu.scenes.Scenes;

public class RbHelpIndex extends Reaction {
    public void perform(ButtonYio buttonYio) {
        Scenes.sceneHelpIndex.create();
    }
}
