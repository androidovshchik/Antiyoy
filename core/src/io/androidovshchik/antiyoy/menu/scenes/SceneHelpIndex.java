package io.androidovshchik.antiyoy.menu.scenes;

import com.badlogic.gdx.Input.Keys;
import io.androidovshchik.antiyoy.menu.ButtonYio;
import io.androidovshchik.antiyoy.menu.MenuControllerYio;
import io.androidovshchik.antiyoy.menu.behaviors.Reaction;
import io.androidovshchik.antiyoy.menu.scenes.editor.SceneEditorInstruments;

public class SceneHelpIndex extends AbstractScene {
    private Reaction rbDiplomacy1 = new C01202();
    private Reaction rbDiplomacy2 = new C01213();
    private Reaction rbMyGames = new C01191();
    private double tHeight;
    private double top;
    private int topicsNumber;
    private double f141y;

    class C01191 extends Reaction {
        C01191() {
        }

        public void perform(ButtonYio buttonYio) {
            Scenes.sceneInfoMenu.create("article_my_games", Reaction.rbHelpIndex, 18);
        }
    }

    class C01202 extends Reaction {
        C01202() {
        }

        public void perform(ButtonYio buttonYio) {
            Scenes.sceneInfoMenu.create("article_diplomacy_1", Reaction.rbHelpIndex, 18);
        }
    }

    class C01213 extends Reaction {
        C01213() {
        }

        public void perform(ButtonYio buttonYio) {
            Scenes.sceneInfoMenu.create("article_diplomacy_2", Reaction.rbHelpIndex, 18);
        }
    }

    public SceneHelpIndex(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
    }

    public void create() {
        this.menuControllerYio.beginMenuCreation();
        this.menuControllerYio.getYioGdxGame().setGamePaused(true);
        this.menuControllerYio.getYioGdxGame().beginBackgroundChange(1, false, true);
        this.topicsNumber = 9;
        this.tHeight = SceneEditorInstruments.ICON_SIZE;
        this.top = 0.7d;
        createBasePanel();
        this.f141y = this.top - this.tHeight;
        createTopicButton(127, "help_about_rules", Reaction.rbArticleRules);
        createTopicButton(122, "help_about_units", Reaction.rbArticleUnits);
        createTopicButton(123, "help_about_trees", Reaction.rbArticleTrees);
        createTopicButton(124, "help_about_towers", Reaction.rbArticleTowers);
        createTopicButton(125, "help_about_money", Reaction.rbArticleMoney);
        createTopicButton(126, "help_about_tactics", Reaction.rbArticleTactics);
        createTopicButton(931, getString("diplomacy") + " " + 1, this.rbDiplomacy1);
        createTopicButton(932, getString("diplomacy") + " " + 2, this.rbDiplomacy2);
        createTopicButton(933, "my_games", this.rbMyGames);
        this.menuControllerYio.spawnBackButton(Keys.CONTROL_LEFT, Reaction.rbMainMenu);
        this.menuControllerYio.endMenuCreation();
    }

    private void createBasePanel() {
        ButtonYio basePanel = this.buttonFactory.getButton(generateRectangle(0.1d, this.top - (((double) this.topicsNumber) * this.tHeight), 0.8d, 0.1d + (((double) this.topicsNumber) * this.tHeight)), 120, null);
        if (basePanel.notRendered()) {
            basePanel.addTextLine(getString("help") + ":");
            for (int i = 0; i < this.topicsNumber + 1; i++) {
                basePanel.addTextLine(" ");
            }
            this.menuControllerYio.getButtonRenderer().renderButton(basePanel);
        }
        basePanel.setTouchable(false);
        basePanel.setAnimation(5);
    }

    private void createTopicButton(int id, String key, Reaction reaction) {
        ButtonYio topicButton = this.buttonFactory.getButton(generateRectangle(0.1d, this.f141y, 0.8d, this.tHeight), id, getString(key));
        topicButton.setReaction(reaction);
        topicButton.setShadow(false);
        topicButton.setAnimation(5);
        this.f141y -= this.tHeight;
    }
}
