package io.androidovshchik.antiyoy.menu.scenes;

import com.badlogic.gdx.Gdx;
import io.androidovshchik.antiyoy.Settings;
import io.androidovshchik.antiyoy.YioGdxGame;
import io.androidovshchik.antiyoy.menu.ButtonYio;
import io.androidovshchik.antiyoy.menu.CheckButtonYio;
import io.androidovshchik.antiyoy.menu.MenuControllerYio;
import io.androidovshchik.antiyoy.menu.behaviors.Reaction;
import io.androidovshchik.antiyoy.menu.save_slot_selector.SaveSystem;
import io.androidovshchik.antiyoy.menu.scenes.editor.SceneEditorInstruments;
import io.androidovshchik.antiyoy.stuff.GraphicsYio;
import io.androidovshchik.antiyoy.stuff.LanguagesManager;

public class SceneSettingsMenu extends AbstractScene {
    private CheckButtonYio chkSound;
    private double labelHeight;
    private double labelTopY;
    Reaction soundChkReaction = new C01311();

    class C01311 extends Reaction {
        C01311() {
        }

        public void perform(ButtonYio buttonYio) {
            Settings.soundEnabled = SceneSettingsMenu.this.chkSound.isChecked();
        }
    }

    public SceneSettingsMenu(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
    }

    public void create() {
        this.menuControllerYio.beginMenuCreation();
        this.menuControllerYio.getYioGdxGame().beginBackgroundChange(1, false, true);
        this.menuControllerYio.spawnBackButton(190, Reaction.rbCloseSettingsMenu);
        createInfoButton();
        createMainLabel();
        createCheckButtons();
        createMoreSettingsButton();
        Settings.getInstance().loadSettings();
        this.menuControllerYio.endMenuCreation();
    }

    private void createMoreSettingsButton() {
        ButtonYio moreSettingsButton = this.buttonFactory.getButton(generateRectangle(0.62d, this.labelTopY - this.labelHeight, 0.3d, 0.05d), 199, getString("more"));
        moreSettingsButton.setReaction(Reaction.rbMoreSettings);
        moreSettingsButton.setAnimation(5);
        moreSettingsButton.disableTouchAnimation();
        moreSettingsButton.setTouchOffset(0.05f * ((float) Gdx.graphics.getHeight()));
    }

    private void createCheckButtons() {
        double hSize = GraphicsYio.convertToHeight(0.05d);
        double chkX = 0.9d - 0.05d;
        double chkY = this.labelTopY - SceneEditorInstruments.ICON_SIZE;
        CheckButtonYio.getCheckButton(this.menuControllerYio, generateSquare(chkX, chkY - (hSize / 2.0d), hSize), 1).setTouchPosition(generateRectangle(0.04d, chkY - (1.5d * hSize), 0.92d, hSize * 3.0d));
        chkY -= 0.086d;
        CheckButtonYio.getCheckButton(this.menuControllerYio, generateSquare(chkX, chkY - (hSize / 2.0d), hSize), 2).setTouchPosition(generateRectangle(0.04d, chkY - (1.5d * hSize), 0.92d, hSize * 3.0d));
        chkY -= 0.086d;
        CheckButtonYio.getCheckButton(this.menuControllerYio, generateSquare(chkX, chkY - (hSize / 2.0d), hSize), 3).setTouchPosition(generateRectangle(0.04d, chkY - (1.5d * hSize), 0.92d, hSize * 3.0d));
        chkY -= 0.086d;
        CheckButtonYio.getCheckButton(this.menuControllerYio, generateSquare(chkX, chkY - (hSize / 2.0d), hSize), 4).setTouchPosition(generateRectangle(0.04d, chkY - (1.5d * hSize), 0.92d, hSize * 3.0d));
        chkY -= 0.086d;
        this.chkSound = CheckButtonYio.getCheckButton(this.menuControllerYio, generateSquare(chkX, chkY - (hSize / 2.0d), hSize), 5);
        this.chkSound.setReaction(this.soundChkReaction);
        this.chkSound.setTouchPosition(generateRectangle(0.04d, chkY - (1.5d * hSize), 0.92d, hSize * 3.0d));
        for (int i = 1; i <= 5; i++) {
            this.menuControllerYio.getCheckButtonById(i).setAnimation(5);
        }
    }

    private void createMainLabel() {
        this.labelHeight = 0.52d;
        this.labelTopY = 0.7d;
        ButtonYio mainLabel = this.buttonFactory.getButton(generateRectangle(0.04d, this.labelTopY - this.labelHeight, 0.92d, this.labelHeight), 192, null);
        if (mainLabel.notRendered()) {
            mainLabel.cleatText();
            mainLabel.addTextLine(LanguagesManager.getInstance().getString(" "));
            mainLabel.addTextLine(LanguagesManager.getInstance().getString(SaveSystem.AUTOSAVE_KEY));
            mainLabel.addTextLine(LanguagesManager.getInstance().getString(" "));
            mainLabel.addTextLine(LanguagesManager.getInstance().getString("music"));
            mainLabel.addTextLine(LanguagesManager.getInstance().getString(" "));
            mainLabel.addTextLine(LanguagesManager.getInstance().getString("ask_to_end_turn"));
            mainLabel.addTextLine(LanguagesManager.getInstance().getString(" "));
            mainLabel.addTextLine(LanguagesManager.getInstance().getString("city_names"));
            mainLabel.addTextLine(LanguagesManager.getInstance().getString(" "));
            mainLabel.addTextLine(LanguagesManager.getInstance().getString("sound"));
            mainLabel.addTextLine(LanguagesManager.getInstance().getString(" "));
            mainLabel.addTextLine(LanguagesManager.getInstance().getString(" "));
            this.menuControllerYio.getButtonRenderer().renderButton(mainLabel);
        }
        mainLabel.setTouchable(false);
        mainLabel.setAnimation(5);
    }

    private void createInfoButton() {
        ButtonYio infoButton = this.buttonFactory.getButton(generateSquare(0.8d, 0.89d, 0.15d * ((double) YioGdxGame.screenRatio)), 191, null);
        this.menuControllerYio.loadButtonOnce(infoButton, "info_icon.png");
        infoButton.setShadow(true);
        infoButton.setAnimation(1);
        infoButton.setReaction(Reaction.rbInfo);
        infoButton.disableTouchAnimation();
    }
}
