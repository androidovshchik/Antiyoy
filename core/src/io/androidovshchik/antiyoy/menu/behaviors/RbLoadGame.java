package io.androidovshchik.antiyoy.menu.behaviors;

import yio.tro.antiyoy.menu.ButtonYio;
import yio.tro.antiyoy.menu.scenes.Scenes;

public class RbLoadGame extends Reaction {
    public void perform(ButtonYio buttonYio) {
        Scenes.sceneSaveLoad.create();
        Scenes.sceneSaveLoad.setOperationType(true);
    }
}
