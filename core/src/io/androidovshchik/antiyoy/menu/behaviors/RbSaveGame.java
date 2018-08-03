package io.androidovshchik.antiyoy.menu.behaviors;

import io.androidovshchik.antiyoy.menu.ButtonYio;
import io.androidovshchik.antiyoy.menu.scenes.Scenes;

public class RbSaveGame extends Reaction {
    public void perform(ButtonYio buttonYio) {
        Scenes.sceneSaveLoad.create();
        Scenes.sceneSaveLoad.setOperationType(false);
    }
}
