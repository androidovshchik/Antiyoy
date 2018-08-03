package io.androidovshchik.antiyoy.menu.behaviors.help;

import io.androidovshchik.antiyoy.menu.ButtonYio;
import io.androidovshchik.antiyoy.menu.behaviors.Reaction;
import io.androidovshchik.antiyoy.menu.scenes.Scenes;

public class RbArticleTactics extends Reaction {
    public void perform(ButtonYio buttonYio) {
        Scenes.sceneInfoMenu.create("help_tactics_article", Reaction.rbHelpIndex, 18);
    }
}
