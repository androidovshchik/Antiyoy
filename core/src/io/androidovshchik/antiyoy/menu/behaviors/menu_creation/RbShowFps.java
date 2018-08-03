package io.androidovshchik.antiyoy.menu.behaviors.menu_creation;

import io.androidovshchik.antiyoy.gameplay.DebugFlags;
import io.androidovshchik.antiyoy.menu.ButtonYio;
import io.androidovshchik.antiyoy.menu.behaviors.Reaction;

public class RbShowFps extends Reaction {
    public void perform(ButtonYio buttonYio) {
        DebugFlags.showFpsInfo = true;
    }
}
