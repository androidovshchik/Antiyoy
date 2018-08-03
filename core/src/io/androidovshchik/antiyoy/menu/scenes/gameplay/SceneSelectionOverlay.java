package io.androidovshchik.antiyoy.menu.scenes.gameplay;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.androidovshchik.antiyoy.Settings;
import io.androidovshchik.antiyoy.SoundControllerYio;
import io.androidovshchik.antiyoy.YioGdxGame;
import io.androidovshchik.antiyoy.gameplay.rules.GameRules;
import io.androidovshchik.antiyoy.menu.ButtonYio;
import io.androidovshchik.antiyoy.menu.MenuControllerYio;
import io.androidovshchik.antiyoy.menu.behaviors.Reaction;
import io.androidovshchik.antiyoy.menu.scenes.editor.SceneEditorInstruments;
import io.androidovshchik.antiyoy.stuff.GraphicsYio;

public class SceneSelectionOverlay extends AbstractGameplayScene {
    private ButtonYio coinButton;
    private ButtonYio diplomacyButton;
    TextureRegion flagNormal = GraphicsYio.loadTextureRegion("diplomacy/flag.png", true);
    TextureRegion flagRed = GraphicsYio.loadTextureRegion("diplomacy/flag_red.png", true);
    private ButtonYio towerButton;
    private ButtonYio unitButton;

    class C01471 extends Reaction {
        C01471() {
        }

        public void perform(ButtonYio buttonYio) {
            getGameController(buttonYio).fieldController.diplomacyManager.onDiplomacyButtonPressed();
        }
    }

    public SceneSelectionOverlay(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
    }

    public void create() {
        createUnitButton();
        createTowerButton();
        createCoinButton();
        createDiplomacyButton();
    }

    private void createDiplomacyButton() {
        this.diplomacyButton = this.menuControllerYio.getButtonById(36);
        if (this.diplomacyButton == null) {
            this.diplomacyButton = this.buttonFactory.getButton(generateSquare(0.0d, 0.1d, SceneEditorInstruments.ICON_SIZE), 36, null);
            this.diplomacyButton.setAnimation(8);
            this.diplomacyButton.enableRectangularMask();
            this.diplomacyButton.disableTouchAnimation();
            this.diplomacyButton.setTouchOffset(0.05f * GraphicsYio.width);
            this.diplomacyButton.setIgnorePauseResume(true);
            this.diplomacyButton.setReaction(new C01471());
        }
        updateDiplomacyFlagTexture();
        this.diplomacyButton.appearFactor.appear(3, 2.0d);
        this.diplomacyButton.setTouchable(GameRules.diplomacyEnabled);
        if (!GameRules.diplomacyEnabled) {
            this.diplomacyButton.destroy();
        }
    }

    private void updateDiplomacyFlagTexture() {
        if (this.menuControllerYio.yioGdxGame.gameController.fieldController.diplomacyManager.log.hasSomethingToRead()) {
            this.diplomacyButton.setTexture(this.flagRed);
        } else {
            this.diplomacyButton.setTexture(this.flagNormal);
        }
    }

    private void createCoinButton() {
        this.coinButton = this.menuControllerYio.getButtonById(37);
        if (this.coinButton == null) {
            this.coinButton = this.buttonFactory.getButton(generateSquare(0.0d, 0.93d, SceneEditorInstruments.ICON_SIZE), 37, null);
            this.coinButton.setAnimation(1);
            this.coinButton.setPressSound(SoundControllerYio.soundCoin);
            this.coinButton.enableRectangularMask();
            this.coinButton.disableTouchAnimation();
        }
        loadCoinButtonTexture();
        this.coinButton.appearFactor.appear(3, 2.0d);
        this.coinButton.setTouchable(true);
        this.coinButton.setReaction(Reaction.rbShowColorStats);
    }

    private void createTowerButton() {
        this.towerButton = this.menuControllerYio.getButtonById(38);
        if (this.towerButton == null) {
            this.towerButton = this.buttonFactory.getButton(generateSquare(0.3d, 0.0d, 0.13d * ((double) YioGdxGame.screenRatio)), 38, null);
            this.towerButton.setReaction(Reaction.rbBuildSolidObject);
            this.towerButton.setAnimation(2);
            this.towerButton.enableRectangularMask();
        }
        loadBuildObjectButton();
        this.towerButton.setTouchable(true);
        this.towerButton.setTouchOffset(0.05f * GraphicsYio.width);
        this.towerButton.appearFactor.appear(3, 2.0d);
    }

    private void createUnitButton() {
        this.unitButton = this.menuControllerYio.getButtonById(39);
        if (this.unitButton == null) {
            this.unitButton = this.buttonFactory.getButton(generateSquare(0.57d, 0.0d, 0.13d * ((double) YioGdxGame.screenRatio)), 39, null);
            this.unitButton.setReaction(Reaction.rbBuildUnit);
            this.unitButton.setAnimation(2);
            this.unitButton.enableRectangularMask();
        }
        loadUnitButtonTexture();
        this.unitButton.setTouchable(true);
        this.unitButton.setTouchOffset(0.05f * GraphicsYio.width);
        this.unitButton.appearFactor.appear(3, 2.0d);
    }

    void loadBuildObjectButton() {
        if (GameRules.slayRules) {
            loadTowerButtonTexture();
        } else {
            loadFarmButtonTexture();
        }
    }

    public void onSkinChanged() {
        if (this.coinButton != null) {
            this.coinButton.resetTexture();
        }
        if (this.unitButton != null) {
            this.unitButton.resetTexture();
        }
        if (this.towerButton != null) {
            this.towerButton.resetTexture();
        }
    }

    void loadFarmButtonTexture() {
        if (Settings.isShroomArtsEnabled()) {
            this.menuControllerYio.loadButtonOnce(this.towerButton, "skins/ant/field_elements/house.png");
        } else {
            this.menuControllerYio.loadButtonOnce(this.towerButton, "field_elements/house.png");
        }
    }

    void loadTowerButtonTexture() {
        if (Settings.isShroomArtsEnabled()) {
            this.menuControllerYio.loadButtonOnce(this.towerButton, "skins/ant/field_elements/tower.png");
        } else {
            this.menuControllerYio.loadButtonOnce(this.towerButton, "field_elements/tower.png");
        }
    }

    void loadUnitButtonTexture() {
        if (Settings.isShroomArtsEnabled()) {
            this.menuControllerYio.loadButtonOnce(this.unitButton, "skins/ant/field_elements/man0.png");
        } else {
            this.menuControllerYio.loadButtonOnce(this.unitButton, "field_elements/man0.png");
        }
    }

    void loadCoinButtonTexture() {
        if (Settings.isShroomArtsEnabled()) {
            this.menuControllerYio.loadButtonOnce(this.coinButton, "skins/ant/coin.png");
        } else {
            this.menuControllerYio.loadButtonOnce(this.coinButton, "coin.png");
        }
    }

    public void hide() {
        this.menuControllerYio.destroyButton(39);
        this.menuControllerYio.destroyButton(38);
        this.menuControllerYio.destroyButton(37);
        this.menuControllerYio.destroyButton(36);
        this.menuControllerYio.getYioGdxGame().gameController.selectionController.getSelMoneyFactor().destroy(2, 8.0d);
    }
}
