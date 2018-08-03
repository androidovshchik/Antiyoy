package io.androidovshchik.antiyoy.menu.scenes.editor;

import com.badlogic.gdx.Input.Keys;
import io.androidovshchik.antiyoy.YioGdxGame;
import io.androidovshchik.antiyoy.menu.ButtonYio;
import io.androidovshchik.antiyoy.menu.MenuControllerYio;
import io.androidovshchik.antiyoy.menu.behaviors.Reaction;
import io.androidovshchik.antiyoy.menu.behaviors.editor.EditorReactions;
import io.androidovshchik.antiyoy.menu.scenes.AbstractScene;
import io.androidovshchik.antiyoy.menu.scenes.Scenes;
import io.androidovshchik.antiyoy.stuff.RectangleYio;

public class SceneEditorInstruments extends AbstractScene {
    public static final double ICON_SIZE = 0.07d;
    private ButtonYio optionsButtons;
    private Reaction rbShowEditorChecks = new C01421();
    boolean readyToShrink = true;

    class C01421 extends Reaction {
        C01421() {
        }

        public void perform(ButtonYio buttonYio) {
            Scenes.sceneEditorChecks.onTumblerButtonPressed();
        }
    }

    public SceneEditorInstruments(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
    }

    public void create() {
        beginCreation();
        createMenuButton();
        createBasePanel();
        createIcons();
        endCreation();
    }

    private void beginCreation() {
        this.menuControllerYio.beginMenuCreation();
        cachePanels();
    }

    private void endCreation() {
        for (int i = 141; i <= Keys.NUMPAD_5; i++) {
            ButtonYio buttonYio = this.menuControllerYio.getButtonById(i);
            if (buttonYio != null) {
                buttonYio.appearFactor.appear(MenuControllerYio.SPAWN_ANIM, MenuControllerYio.SPAWN_SPEED);
                buttonYio.enableRectangularMask();
                buttonYio.disableTouchAnimation();
                buttonYio.setAnimation(2);
            }
        }
        this.menuControllerYio.endMenuCreation();
    }

    private void createIcons() {
        createIcon(142, 0, "hex_black.png", EditorReactions.rbShowHexPanel);
        createIcon(143, 1, "icon_move.png", Reaction.rbInputModeMove);
        createIcon(Keys.NUMPAD_0, 2, "field_elements/man0_low.png", EditorReactions.rbShowObjectPanel);
        createIconFromRight(Keys.NUMPAD_4, 2, "menu/editor/chk.png", this.rbShowEditorChecks);
        createIconFromRight(Keys.NUMPAD_2, 1, "menu/editor/automation_icon.png", EditorReactions.rbShowAutomationPanel);
        createIconFromRight(Keys.NUMPAD_1, 0, "menu/editor/params_icon.png", EditorReactions.rbShowEditorParams);
        this.readyToShrink = false;
    }

    private void createBasePanel() {
        ButtonYio basePanel = this.buttonFactory.getButton(generateRectangle(0.0d, 0.0d, 1.0d, ICON_SIZE), 141, null);
        this.menuControllerYio.loadButtonOnce(basePanel, "gray_pixel.png");
        basePanel.setTouchable(false);
    }

    private void createMenuButton() {
        ButtonYio menuButton = this.buttonFactory.getButton(generateSquare(1.0d - (ICON_SIZE / ((double) YioGdxGame.screenRatio)), 0.93d, ICON_SIZE), 140, null);
        this.menuControllerYio.loadButtonOnce(menuButton, "menu_icon.png");
        menuButton.setReaction(EditorReactions.rbEditorActionsMenu);
        menuButton.setAnimation(1);
        menuButton.enableRectangularMask();
        menuButton.disableTouchAnimation();
    }

    private void cachePanels() {
        Scenes.sceneEditorHexPanel.create();
        Scenes.sceneEditorObjectPanel.create();
        Scenes.sceneEditorParams.create();
        Scenes.sceneEditorAutomationPanel.create();
        Scenes.sceneEditorChecks.create();
        this.menuControllerYio.hideAllEditorPanels();
    }

    private void createIcon(int id, int place, String texturePath, Reaction rb) {
        ButtonYio iconButton = this.buttonFactory.getButton(generateSquare((((double) place) * ICON_SIZE) / ((double) YioGdxGame.screenRatio), 0.0d, ICON_SIZE), id, null);
        this.menuControllerYio.loadButtonOnce(iconButton, texturePath);
        iconButton.setReaction(rb);
        shrinkIcon(iconButton);
    }

    private void createIconFromRight(int id, int place, String texturePath, Reaction rb) {
        ButtonYio iconButton = this.buttonFactory.getButton(generateSquare(1.0d - ((((double) (place + 1)) * ICON_SIZE) / ((double) YioGdxGame.screenRatio)), 0.0d, ICON_SIZE), id, null);
        this.menuControllerYio.loadButtonOnce(iconButton, texturePath);
        iconButton.setReaction(rb);
        shrinkIcon(iconButton);
    }

    private void shrinkIcon(ButtonYio buttonYio) {
        if (this.readyToShrink) {
            RectangleYio pos = buttonYio.position;
            float delta = (float) (0.11999999731779099d * pos.width);
            pos.f146x += (double) delta;
            pos.f147y += (double) delta;
            pos.width -= (double) (2.0f * delta);
            pos.height -= (double) (2.0f * delta);
            buttonYio.setPosition(pos);
            buttonYio.setTouchOffset(delta);
        }
    }
}
