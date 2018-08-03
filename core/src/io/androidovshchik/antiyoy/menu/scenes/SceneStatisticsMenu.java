package io.androidovshchik.antiyoy.menu.scenes;

import com.badlogic.gdx.Input.Keys;
import yio.tro.antiyoy.Settings;
import yio.tro.antiyoy.gameplay.MatchStatistics;
import yio.tro.antiyoy.menu.ButtonYio;
import yio.tro.antiyoy.menu.MenuControllerYio;
import yio.tro.antiyoy.menu.behaviors.Reaction;
import yio.tro.antiyoy.menu.scenes.editor.SceneEditorInstruments;
import yio.tro.antiyoy.stuff.GraphicsYio;

public class SceneStatisticsMenu extends AbstractScene {
    private Reaction backReaction = new C01361();
    private ButtonYio replayButton;

    class C01361 extends Reaction {
        C01361() {
        }

        public void perform(ButtonYio buttonYio) {
            Scenes.sceneAfterGameMenu.create();
        }
    }

    public SceneStatisticsMenu(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
    }

    public void create(MatchStatistics matchStatistics) {
        this.menuControllerYio.beginMenuCreation();
        this.menuControllerYio.getYioGdxGame().beginBackgroundChange(0, false, true);
        this.menuControllerYio.spawnBackButton(111, this.backReaction);
        ButtonYio textPanel = this.buttonFactory.getButton(generateRectangle(0.05d, 0.1d, 0.9d, 0.7d), Keys.FORWARD_DEL, null);
        textPanel.cleatText();
        textPanel.addTextLine(getString("statistics") + ":");
        textPanel.addTextLine(getString("turns_made") + " " + matchStatistics.turnsMade);
        textPanel.addTextLine(getString("units_died") + " " + matchStatistics.unitsDied);
        textPanel.addTextLine(getString("units_produced") + " " + matchStatistics.unitsProduced);
        textPanel.addTextLine(getString("money_spent") + " " + matchStatistics.moneySpent);
        textPanel.addTextLine(getString("time") + " " + matchStatistics.getTimeString());
        for (int i = 0; i < 10; i++) {
            textPanel.addTextLine("");
        }
        this.menuControllerYio.getButtonRenderer().renderButton(textPanel);
        textPanel.setTouchable(false);
        textPanel.setAnimation(5);
        createReplayButton();
        this.menuControllerYio.endMenuCreation();
    }

    private void createReplayButton() {
        if (Settings.replaysEnabled) {
            this.replayButton = this.buttonFactory.getButton(generateRectangle(0.55d, 0.9d, 0.4d, SceneEditorInstruments.ICON_SIZE), 113, getString("replay"));
            this.replayButton.setReaction(Reaction.rbStartInstantReplay);
            this.replayButton.setAnimation(1);
            this.replayButton.setTouchOffset(0.05f * GraphicsYio.width);
            this.replayButton.disableTouchAnimation();
        }
    }

    public void create() {
    }
}
