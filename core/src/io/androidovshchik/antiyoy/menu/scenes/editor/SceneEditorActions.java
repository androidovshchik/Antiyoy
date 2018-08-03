package io.androidovshchik.antiyoy.menu.scenes.editor;

import io.androidovshchik.antiyoy.gameplay.editor.LevelEditor;
import io.androidovshchik.antiyoy.menu.ButtonYio;
import io.androidovshchik.antiyoy.menu.MenuControllerYio;
import io.androidovshchik.antiyoy.menu.behaviors.Reaction;
import io.androidovshchik.antiyoy.menu.behaviors.editor.EditorReactions;
import io.androidovshchik.antiyoy.menu.scenes.AbstractScene;

public class SceneEditorActions extends AbstractScene {
    double bHeight = 0.075d;
    private ButtonYio basePanel;
    int lastSlotNumber = -1;
    double f143y;

    public SceneEditorActions(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
    }

    public void create() {
        this.menuControllerYio.beginMenuCreation();
        this.menuControllerYio.getYioGdxGame().setGamePaused(true);
        this.menuControllerYio.getYioGdxGame().beginBackgroundChange(3, true, true);
        this.menuControllerYio.spawnBackButton(189, EditorReactions.rbEditorSlotMenu);
        this.f143y = 0.24d;
        this.basePanel = this.buttonFactory.getButton(generateRectangle(0.1d, this.f143y, 0.8d, 0.4d), 181, null);
        updateBaseText();
        this.basePanel.setTouchable(false);
        this.basePanel.setIgnorePauseResume(true);
        addInternalButton(182, "play", EditorReactions.rbEditorPlay);
        addInternalButton(183, "export", EditorReactions.rbEditorExport);
        addInternalButton(184, "import", EditorReactions.rbEditorImportConfirmMenu);
        addInternalButton(185, "edit", EditorReactions.rbStartEditorMode);
        for (int i = 181; i <= 185; i++) {
            ButtonYio buttonYio = this.menuControllerYio.getButtonById(i);
            buttonYio.setAnimation(5);
            buttonYio.disableTouchAnimation();
        }
        this.menuControllerYio.endMenuCreation();
    }

    private void updateBaseText() {
        this.basePanel.cleatText();
        int currentSlotNumber = this.menuControllerYio.yioGdxGame.gameController.getLevelEditor().getCurrentSlotNumber();
        if (this.lastSlotNumber != currentSlotNumber) {
            this.basePanel.addTextLine(getString(LevelEditor.SLOT_NAME) + " " + currentSlotNumber);
            this.basePanel.addEmptyLines(7);
            this.menuControllerYio.buttonRenderer.renderButton(this.basePanel);
            System.out.println("SceneEditorActions.updateBaseText");
            this.lastSlotNumber = currentSlotNumber;
        }
    }

    private void addInternalButton(int id, String key, Reaction reaction) {
        ButtonYio button = this.buttonFactory.getButton(generateRectangle(0.1d, this.f143y, 0.8d, this.bHeight), id, getString(key));
        button.setReaction(reaction);
        button.setShadow(false);
        this.f143y += this.bHeight;
    }
}
