package io.androidovshchik.antiyoy.menu.scenes;

import yio.tro.antiyoy.menu.ButtonYio;
import yio.tro.antiyoy.menu.MenuControllerYio;
import yio.tro.antiyoy.menu.behaviors.Reaction;
import yio.tro.antiyoy.menu.save_slot_selector.SaveSlotSelector;
import yio.tro.antiyoy.menu.scenes.editor.SceneEditorInstruments;

public class SceneSaveLoad extends AbstractScene {
    private ButtonYio backButton;
    private ButtonYio replaysButton;
    SaveSlotSelector slotSelector = null;

    public SceneSaveLoad(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
    }

    public void create() {
        this.menuControllerYio.beginMenuCreation();
        this.menuControllerYio.getYioGdxGame().beginBackgroundChange(1, false, true);
        this.backButton = this.menuControllerYio.spawnBackButton(580, null);
        checkToCreateSlotSelector();
        this.slotSelector.appear();
        this.menuControllerYio.endMenuCreation();
    }

    private void createReplaysButton() {
        this.replaysButton = this.buttonFactory.getButton(generateRectangle(0.55d, 0.9d, 0.4d, SceneEditorInstruments.ICON_SIZE), 581, getString("replays"));
        this.replaysButton.setReaction(Reaction.rbReplaysMenu);
        this.replaysButton.setAnimation(1);
    }

    private void checkToCreateSlotSelector() {
        if (this.slotSelector == null) {
            this.slotSelector = new SaveSlotSelector(this.menuControllerYio, -1);
            this.slotSelector.setPosition(generateRectangle(0.05d, SceneEditorInstruments.ICON_SIZE, 0.9d, 0.75d));
            this.menuControllerYio.addElementToScene(this.slotSelector);
        }
    }

    public void setOperationType(boolean load) {
        this.slotSelector.setOperationType(load);
        if (load) {
            this.backButton.setReaction(Reaction.rbChooseGameModeMenu);
            createReplaysButton();
            return;
        }
        this.backButton.setReaction(Reaction.rbPauseMenu);
    }
}
