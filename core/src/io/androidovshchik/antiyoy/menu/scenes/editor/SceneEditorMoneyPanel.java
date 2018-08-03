package io.androidovshchik.antiyoy.menu.scenes.editor;

import yio.tro.antiyoy.YioGdxGame;
import yio.tro.antiyoy.menu.ButtonYio;
import yio.tro.antiyoy.menu.MenuControllerYio;
import yio.tro.antiyoy.menu.behaviors.Reaction;

public class SceneEditorMoneyPanel extends AbstractEditorPanel {
    private ButtonYio basePanel;

    public SceneEditorMoneyPanel(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
    }

    public void create() {
        createBasePanel();
        createIcon(542, 0, "menu/editor/minus.png", Reaction.rbNothing);
        createIcon(543, 1, "menu/editor/plus.png", Reaction.rbNothing);
        this.menuControllerYio.yioGdxGame.gameController.getLevelEditor().showMoney = true;
        spawnAllButtons();
    }

    private void createIcon(int id, int place, String texturePath, Reaction rb) {
        ButtonYio iconButton = this.buttonFactory.getButton(generateSquare((((double) place) * SceneEditorInstruments.ICON_SIZE) / ((double) YioGdxGame.screenRatio), SceneEditorInstruments.ICON_SIZE, SceneEditorInstruments.ICON_SIZE), id, null);
        this.menuControllerYio.loadButtonOnce(iconButton, texturePath);
        iconButton.setReaction(rb);
    }

    private void createBasePanel() {
        this.basePanel = this.buttonFactory.getButton(generateRectangle(0.0d, SceneEditorInstruments.ICON_SIZE, 1.0d, SceneEditorInstruments.ICON_SIZE), 540, null);
        this.menuControllerYio.loadButtonOnce(this.basePanel, "gray_pixel.png");
        this.basePanel.setTouchable(false);
    }

    private void spawnAllButtons() {
        for (int i = 540; i <= 549; i++) {
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
        destroyByIndex(540, 549);
    }

    public boolean isCurrentlyOpened() {
        return this.basePanel != null && this.basePanel.appearFactor.get() == 1.0f;
    }
}
