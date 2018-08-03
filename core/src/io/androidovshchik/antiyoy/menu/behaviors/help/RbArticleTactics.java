package io.androidovshchik.antiyoy.menu.behaviors.help;

import yio.tro.antiyoy.menu.ButtonYio;
import yio.tro.antiyoy.menu.behaviors.Reaction;
import yio.tro.antiyoy.menu.scenes.Scenes;

public class RbArticleTactics extends Reaction {
    public void perform(ButtonYio buttonYio) {
        Scenes.sceneInfoMenu.create("help_tactics_article", Reaction.rbHelpIndex, 18);
    }
}
