package io.androidovshchik.antiyoy.menu.scenes.editor;

import com.badlogic.gdx.Input.Keys;
import io.androidovshchik.antiyoy.YioGdxGame;
import io.androidovshchik.antiyoy.menu.ButtonYio;
import io.androidovshchik.antiyoy.menu.MenuControllerYio;
import io.androidovshchik.antiyoy.menu.behaviors.Reaction;
import io.androidovshchik.antiyoy.menu.behaviors.editor.EditorReactions;

public class SceneEditorHexPanel extends AbstractEditorPanel {
    private ButtonYio basePanel;

    public SceneEditorHexPanel(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
    }

    public void create() {
        createBasePanel();
        createCancelButton();
        createFilterButton();
        createHexButtons();
        spawnAllButtons();
    }

    private void spawnAllButtons() {
        int i;
        for (i = 12350; i < 12359; i++) {
            ButtonYio buttonYio = this.menuControllerYio.getButtonById(i);
            if (buttonYio != null) {
                buttonYio.appearFactor.appear(MenuControllerYio.SPAWN_ANIM, MenuControllerYio.SPAWN_SPEED);
                buttonYio.enableRectangularMask();
                buttonYio.disableTouchAnimation();
                buttonYio.setAnimation(2);
            }
        }
        for (i = Keys.NUMPAD_6; i <= 158; i++) {
            buttonYio = this.menuControllerYio.getButtonById(i);
            buttonYio.appearFactor.appear(MenuControllerYio.SPAWN_ANIM, MenuControllerYio.SPAWN_SPEED);
            buttonYio.enableRectangularMask();
            buttonYio.disableTouchAnimation();
            buttonYio.setAnimation(2);
        }
    }

    private void createHexButtons() {
        double curVerPos = 0.21d;
        double curHorPos = SceneEditorInstruments.ICON_SIZE;
        for (int i = 0; i < 9; i++) {
            if (i == 4) {
                curVerPos = 0.14d;
                curHorPos = 0.0d;
            }
            ButtonYio hexButton = this.buttonFactory.getButton(generateSquare(curHorPos / ((double) YioGdxGame.screenRatio), curVerPos, SceneEditorInstruments.ICON_SIZE), i + Keys.NUMPAD_6, null);
            curHorPos += SceneEditorInstruments.ICON_SIZE;
            hexButton.setReaction(EditorReactions.rbInputModeHex);
            loadHexButtonTexture(hexButton, i);
        }
    }

    private void loadHexButtonTexture(ButtonYio hexButton, int i) {
        switch (i) {
            case 0:
                this.menuControllerYio.loadButtonOnce(hexButton, "hex_green.png");
                return;
            case 1:
                this.menuControllerYio.loadButtonOnce(hexButton, "hex_red.png");
                return;
            case 2:
                this.menuControllerYio.loadButtonOnce(hexButton, "hex_blue.png");
                return;
            case 3:
                this.menuControllerYio.loadButtonOnce(hexButton, "hex_cyan.png");
                return;
            case 4:
                this.menuControllerYio.loadButtonOnce(hexButton, "hex_yellow.png");
                return;
            case 5:
                this.menuControllerYio.loadButtonOnce(hexButton, "hex_color1.png");
                return;
            case 6:
                this.menuControllerYio.loadButtonOnce(hexButton, "hex_color2.png");
                return;
            case 7:
                this.menuControllerYio.loadButtonOnce(hexButton, "hex_color3.png");
                return;
            case 8:
                this.menuControllerYio.loadButtonOnce(hexButton, "random_hex.png");
                return;
            default:
                return;
        }
    }

    private void createFilterButton() {
        this.buttonFactory.getButton(generateRectangle(0.0d, 0.08d, 0.5d, 0.05d), 12353, null).setReaction(Reaction.rbSwitchFilterOnlyLand);
        this.menuControllerYio.getYioGdxGame().gameController.getLevelEditor().updateFilterOnlyLandButton();
    }

    private void createCancelButton() {
        ButtonYio cancelButton = this.buttonFactory.getButton(generateSquare(0.0d, 0.21d, SceneEditorInstruments.ICON_SIZE), 12350, null);
        this.menuControllerYio.loadButtonOnce(cancelButton, "cancel_icon.png");
        cancelButton.setReaction(EditorReactions.rbInputModeDelete);
    }

    private void createBasePanel() {
        this.basePanel = this.buttonFactory.getButton(generateRectangle(0.0d, SceneEditorInstruments.ICON_SIZE, 1.0d, 0.21d), 12352, null);
        this.menuControllerYio.loadButtonOnce(this.basePanel, "gray_pixel.png");
        this.basePanel.setTouchable(false);
    }

    public void hide() {
        int i;
        for (i = Keys.NUMPAD_6; i <= 158; i++) {
            this.menuControllerYio.destroyButton(i);
        }
        for (i = 12350; i < 12359; i++) {
            this.menuControllerYio.destroyButton(i);
        }
    }

    public boolean isCurrentlyOpened() {
        return this.basePanel != null && this.basePanel.appearFactor.get() == 1.0f;
    }
}
