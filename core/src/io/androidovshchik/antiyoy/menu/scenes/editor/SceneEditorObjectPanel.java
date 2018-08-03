package io.androidovshchik.antiyoy.menu.scenes.editor;

import io.androidovshchik.antiyoy.YioGdxGame;
import io.androidovshchik.antiyoy.menu.ButtonYio;
import io.androidovshchik.antiyoy.menu.MenuControllerYio;
import io.androidovshchik.antiyoy.menu.behaviors.editor.EditorReactions;

public class SceneEditorObjectPanel extends AbstractEditorPanel {
    private ButtonYio basePanel;

    public SceneEditorObjectPanel(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
    }

    public void create() {
        int i;
        this.basePanel = this.buttonFactory.getButton(generateRectangle(0.0d, SceneEditorInstruments.ICON_SIZE, 1.0d, SceneEditorInstruments.ICON_SIZE), 160, null);
        this.menuControllerYio.loadButtonOnce(this.basePanel, "gray_pixel.png");
        this.basePanel.setTouchable(false);
        ButtonYio cancelButton = this.buttonFactory.getButton(generateSquare(0.0d, SceneEditorInstruments.ICON_SIZE, SceneEditorInstruments.ICON_SIZE), 162, null);
        this.menuControllerYio.loadButtonOnce(cancelButton, "cancel_icon.png");
        cancelButton.setReaction(EditorReactions.rbInputModeSetObject);
        for (i = 0; i < 6; i++) {
            ButtonYio objectButton = this.buttonFactory.getButton(generateSquare(((((double) i) * SceneEditorInstruments.ICON_SIZE) + SceneEditorInstruments.ICON_SIZE) / ((double) YioGdxGame.screenRatio), SceneEditorInstruments.ICON_SIZE, SceneEditorInstruments.ICON_SIZE), i + 163, null);
            objectButton.setReaction(EditorReactions.rbInputModeSetObject);
            switch (i) {
                case 0:
                    this.menuControllerYio.loadButtonOnce(objectButton, "field_elements/pine_low.png");
                    break;
                case 1:
                    this.menuControllerYio.loadButtonOnce(objectButton, "field_elements/palm_low.png");
                    break;
                case 2:
                    this.menuControllerYio.loadButtonOnce(objectButton, "field_elements/house_low.png");
                    break;
                case 3:
                    this.menuControllerYio.loadButtonOnce(objectButton, "field_elements/tower_low.png");
                    break;
                case 4:
                    this.menuControllerYio.loadButtonOnce(objectButton, "field_elements/man0_low.png");
                    break;
                case 5:
                    this.menuControllerYio.loadButtonOnce(objectButton, "field_elements/man1_low.png");
                    break;
                default:
                    break;
            }
        }
        for (i = 160; i <= 168; i++) {
            ButtonYio buttonYio = this.menuControllerYio.getButtonById(i);
            if (buttonYio != null) {
                buttonYio.appearFactor.appear(MenuControllerYio.SPAWN_ANIM, MenuControllerYio.SPAWN_SPEED);
                buttonYio.enableRectangularMask();
                buttonYio.disableTouchAnimation();
                buttonYio.setAnimation(2);
            }
        }
    }

    public void hide() {
        for (int i = 160; i <= 168; i++) {
            ButtonYio buttonYio = this.menuControllerYio.getButtonById(i);
            if (buttonYio != null) {
                buttonYio.destroy();
            }
        }
    }

    public boolean isCurrentlyOpened() {
        return this.basePanel != null && this.basePanel.appearFactor.get() == 1.0f;
    }
}
