package io.androidovshchik.antiyoy.menu.scenes.gameplay;

import io.androidovshchik.antiyoy.YioGdxGame;
import io.androidovshchik.antiyoy.menu.ButtonYio;
import io.androidovshchik.antiyoy.menu.MenuControllerYio;
import io.androidovshchik.antiyoy.menu.behaviors.Reaction;
import io.androidovshchik.antiyoy.menu.scenes.AbstractScene;
import io.androidovshchik.antiyoy.menu.scenes.editor.SceneEditorInstruments;
import io.androidovshchik.antiyoy.menu.speed_panel.SpeedPanel;

public class SceneAiOnlyOverlay extends AbstractScene {
    public ButtonYio inGameMenuButton;
    public SpeedPanel speedPanel = null;

    public SceneAiOnlyOverlay(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
    }

    public void create() {
        this.menuControllerYio.beginMenuCreation();
        createInGameMenuButton();
        createSpeedPanel();
        this.speedPanel.appear();
        this.menuControllerYio.endMenuCreation();
    }

    private void createInGameMenuButton() {
        this.inGameMenuButton = this.buttonFactory.getButton(generateSquare(1.0d - (SceneEditorInstruments.ICON_SIZE / ((double) YioGdxGame.screenRatio)), 0.93d, SceneEditorInstruments.ICON_SIZE), 530, null);
        this.menuControllerYio.loadButtonOnce(this.inGameMenuButton, "menu_icon.png");
        this.inGameMenuButton.setReaction(Reaction.rbPauseMenu);
        this.inGameMenuButton.setAnimation(1);
        this.inGameMenuButton.enableRectangularMask();
        this.inGameMenuButton.disableTouchAnimation();
    }

    private void createSpeedPanel() {
        if (this.speedPanel == null) {
            this.speedPanel = new SpeedPanel(this.menuControllerYio, -1);
            this.menuControllerYio.addElementToScene(this.speedPanel);
        }
    }
}
