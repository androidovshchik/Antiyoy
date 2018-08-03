package io.androidovshchik.antiyoy.menu.scenes.gameplay;

import io.androidovshchik.antiyoy.Settings;
import io.androidovshchik.antiyoy.SoundControllerYio;
import io.androidovshchik.antiyoy.menu.ButtonYio;
import io.androidovshchik.antiyoy.menu.MenuControllerYio;
import io.androidovshchik.antiyoy.menu.behaviors.Reaction;
import io.androidovshchik.antiyoy.menu.fast_construction.FastConstructionPanel;
import io.androidovshchik.antiyoy.menu.scenes.editor.SceneEditorInstruments;

public class SceneFastConstructionPanel extends AbstractGameplayScene {
    public FastConstructionPanel fastConstructionPanel = null;

    public SceneFastConstructionPanel(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
    }

    public void create() {
        checkToCreateFastConstructionPanel();
        this.fastConstructionPanel.appear();
        createCoinButton();
    }

    private void createCoinButton() {
        ButtonYio coinButton = this.menuControllerYio.getButtonById(610);
        if (coinButton == null) {
            coinButton = this.buttonFactory.getButton(generateSquare(0.0d, 0.93d, SceneEditorInstruments.ICON_SIZE), 610, null);
            coinButton.setAnimation(1);
            coinButton.setPressSound(SoundControllerYio.soundCoin);
            coinButton.enableRectangularMask();
            coinButton.disableTouchAnimation();
        }
        loadCoinButtonTexture(coinButton);
        coinButton.appearFactor.appear(3, 2.0d);
        coinButton.setTouchable(true);
        coinButton.setReaction(Reaction.rbShowColorStats);
    }

    public void checkToReappear() {
        if (Settings.fastConstruction && this.fastConstructionPanel.getFactor().get() != 1.0f) {
            create();
        }
    }

    void loadCoinButtonTexture(ButtonYio coinButton) {
        if (Settings.isShroomArtsEnabled()) {
            this.menuControllerYio.loadButtonOnce(coinButton, "skins/ant/coin.png");
        } else {
            this.menuControllerYio.loadButtonOnce(coinButton, "coin.png");
        }
    }

    private void checkToCreateFastConstructionPanel() {
        if (this.fastConstructionPanel == null) {
            this.fastConstructionPanel = new FastConstructionPanel(this.menuControllerYio, -1);
            this.menuControllerYio.addElementToScene(this.fastConstructionPanel);
        }
    }

    public void hide() {
        this.menuControllerYio.destroyButton(610);
        if (this.fastConstructionPanel != null) {
            this.fastConstructionPanel.destroy();
        }
    }
}
