package io.androidovshchik.antiyoy.menu.scenes;

import com.badlogic.gdx.net.HttpStatus;
import io.androidovshchik.antiyoy.menu.ButtonYio;
import io.androidovshchik.antiyoy.menu.MenuControllerYio;
import io.androidovshchik.antiyoy.menu.behaviors.Reaction;

public class SceneTutorialIndex extends AbstractScene {
    public SceneTutorialIndex(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
    }

    public void create() {
        this.menuControllerYio.beginMenuCreation();
        this.menuControllerYio.getYioGdxGame().beginBackgroundChange(1, false, true);
        createBasePanel();
        createTopicButton(HttpStatus.SC_ACCEPTED, 0.53d, "help", Reaction.rbHelpIndex);
        createTopicButton(HttpStatus.SC_NON_AUTHORITATIVE_INFORMATION, 0.45d, "normal_rules", Reaction.rbTutorialGeneric);
        createTopicButton(HttpStatus.SC_NO_CONTENT, 0.37d, "slay_rules", Reaction.rbTutorialSlay);
        this.menuControllerYio.spawnBackButton(209, Reaction.rbChooseGameModeMenu);
        this.menuControllerYio.endMenuCreation();
    }

    private void createTopicButton(int id, double y, String key, Reaction reaction) {
        ButtonYio topicButton = this.buttonFactory.getButton(generateRectangle(0.05d, y, 0.9d, 0.08d), id, getString(key));
        topicButton.setReaction(reaction);
        topicButton.setShadow(false);
        topicButton.setAnimation(5);
    }

    private void createBasePanel() {
        ButtonYio basePanel = this.buttonFactory.getButton(generateRectangle(0.05d, 0.37d, 0.9d, 0.34d), HttpStatus.SC_OK, null);
        if (basePanel.notRendered()) {
            basePanel.addTextLine(getString("choose_game_mode_tutorial") + ":");
            for (int i = 0; i < 5; i++) {
                basePanel.addTextLine(" ");
            }
            this.menuControllerYio.getButtonRenderer().renderButton(basePanel);
        }
        basePanel.setTouchable(false);
        basePanel.setAnimation(5);
    }
}
