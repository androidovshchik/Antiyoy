package io.androidovshchik.antiyoy.menu.scenes;

import io.androidovshchik.antiyoy.menu.ButtonYio;
import io.androidovshchik.antiyoy.menu.LevelSelector;
import io.androidovshchik.antiyoy.menu.MenuControllerYio;
import io.androidovshchik.antiyoy.menu.behaviors.Reaction;
import io.androidovshchik.antiyoy.menu.scenes.editor.SceneEditorInstruments;

public class SceneCampaignMenu extends AbstractScene {
    LevelSelector levelSelector = null;
    boolean updatedSelectorMetrics = false;

    public SceneCampaignMenu(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
    }

    public void create() {
        Scenes.sceneMoreCampaignOptions.prepare();
        this.menuControllerYio.beginMenuCreation();
        this.menuControllerYio.getYioGdxGame().beginBackgroundChange(2, true, true);
        this.menuControllerYio.spawnBackButton(20, Reaction.rbChooseGameModeMenu);
        this.menuControllerYio.getButtonById(20).setTouchable(true);
        ButtonYio moreOptionsButton = this.buttonFactory.getButton(generateRectangle(0.75d, 0.9d, 0.2d, SceneEditorInstruments.ICON_SIZE), 21, getString("..."));
        moreOptionsButton.setReaction(Reaction.rbMoreCampaignOptions);
        moreOptionsButton.setAnimation(1);
        moreOptionsButton.disableTouchAnimation();
        checkToCreateLevelSelector();
        this.levelSelector.appear();
        checkToUpdateSelectorMetrics();
        this.menuControllerYio.endMenuCreation();
        this.levelSelector.checkToReloadProgress();
    }

    private void checkToCreateLevelSelector() {
        if (this.levelSelector == null) {
            this.levelSelector = new LevelSelector(this.menuControllerYio, 22);
            this.menuControllerYio.addElementToScene(this.levelSelector);
        }
    }

    private void checkToUpdateSelectorMetrics() {
        if (!this.updatedSelectorMetrics && this.levelSelector != null) {
            this.updatedSelectorMetrics = true;
            this.levelSelector.updateTabsMetrics();
        }
    }

    public void updateLevelSelector() {
        if (this.levelSelector != null) {
            this.levelSelector.renderAllPanels();
        }
    }
}
