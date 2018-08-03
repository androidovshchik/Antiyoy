package io.androidovshchik.antiyoy.menu.scenes.editor;

import com.badlogic.gdx.graphics.GL20;
import yio.tro.antiyoy.YioGdxGame;
import yio.tro.antiyoy.menu.ButtonYio;
import yio.tro.antiyoy.menu.MenuControllerYio;
import yio.tro.antiyoy.menu.behaviors.Reaction;
import yio.tro.antiyoy.menu.behaviors.editor.EditorReactions;

public class SceneEditorAutomationPanel extends AbstractEditorPanel {
    private ButtonYio basePanel;

    public SceneEditorAutomationPanel(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
    }

    public void create() {
        createBasePanel();
        createIcon(GL20.GL_NEVER, 0, "menu/editor/expansion_icon.png", EditorReactions.rbEditorExpandProvinces);
        createIcon(GL20.GL_LESS, 1, "menu/editor/palm_auto.png", EditorReactions.rbEditorExpandTrees);
        createIcon(GL20.GL_EQUAL, 2, "menu/editor/house_auto.png", EditorReactions.rbEditorPlaceCapitalsOrFarms);
        createIcon(GL20.GL_LEQUAL, 3, "menu/editor/tower_auto.png", EditorReactions.rbEditorPlaceRandomTowers);
        createIcon(GL20.GL_GREATER, 4, "menu/editor/scissors.png", EditorReactions.rbEditorCutExcessStuff);
        spawnAllButtons();
    }

    private void createIcon(int id, int place, String texturePath, Reaction rb) {
        ButtonYio iconButton = this.buttonFactory.getButton(generateSquare((((double) place) * SceneEditorInstruments.ICON_SIZE) / ((double) YioGdxGame.screenRatio), SceneEditorInstruments.ICON_SIZE, SceneEditorInstruments.ICON_SIZE), id, null);
        this.menuControllerYio.loadButtonOnce(iconButton, texturePath);
        iconButton.setReaction(rb);
    }

    private void createBasePanel() {
        this.basePanel = this.buttonFactory.getButton(generateRectangle(0.0d, SceneEditorInstruments.ICON_SIZE, 1.0d, SceneEditorInstruments.ICON_SIZE), 510, null);
        this.menuControllerYio.loadButtonOnce(this.basePanel, "gray_pixel.png");
        this.basePanel.setTouchable(false);
    }

    private void spawnAllButtons() {
        for (int i = 510; i <= GL20.GL_ALWAYS; i++) {
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
        destroyByIndex(510, GL20.GL_ALWAYS);
    }

    public boolean isCurrentlyOpened() {
        return this.basePanel != null && this.basePanel.appearFactor.get() == 1.0f;
    }
}
