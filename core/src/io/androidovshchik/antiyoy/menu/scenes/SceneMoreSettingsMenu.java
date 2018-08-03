package io.androidovshchik.antiyoy.menu.scenes;

import java.util.ArrayList;
import java.util.Iterator;
import io.androidovshchik.antiyoy.Settings;
import io.androidovshchik.antiyoy.menu.ButtonYio;
import io.androidovshchik.antiyoy.menu.CheckButtonYio;
import io.androidovshchik.antiyoy.menu.InterfaceElement;
import io.androidovshchik.antiyoy.menu.MenuControllerYio;
import io.androidovshchik.antiyoy.menu.behaviors.Reaction;
import io.androidovshchik.antiyoy.menu.scenes.editor.SceneEditorInstruments;
import io.androidovshchik.antiyoy.menu.slider.SliderBehavior;
import io.androidovshchik.antiyoy.menu.slider.SliderYio;
import io.androidovshchik.antiyoy.stuff.GraphicsYio;
import io.androidovshchik.antiyoy.stuff.LanguagesManager;
import io.androidovshchik.antiyoy.stuff.RectangleYio;

public class SceneMoreSettingsMenu extends AbstractScene {
    private Reaction backReaction = new C01251();
    private double checkButtonSize;
    public CheckButtonYio chkFastConstruction;
    public CheckButtonYio chkLeftHanded;
    public CheckButtonYio chkLongTapToMove;
    public CheckButtonYio chkReplays;
    private double chkVerticalDelta;
    public CheckButtonYio chkWaterTexture;
    private double chkX;
    private double chkY;
    private double hSize;
    private double hTouchSize;
    private double offset;
    private double panelHeight;
    private ButtonYio replaysButton;
    public SliderYio sensitivitySlider;
    public SliderYio skinSlider;
    ArrayList<SliderYio> sliders = null;
    private ButtonYio topLabel;

    class C01251 extends Reaction {
        C01251() {
        }

        public void perform(ButtonYio buttonYio) {
            Settings.getInstance().saveMoreSettings();
            Scenes.sceneSettingsMenu.create();
        }
    }

    class C01262 extends SliderBehavior {
        C01262() {
        }

        public String getValueString(SliderYio sliderYio) {
            return SceneMoreSettingsMenu.this.getSkinStringBySliderIndex(sliderYio.getCurrentRunnerIndex());
        }
    }

    class C01273 extends SliderBehavior {
        C01273() {
        }

        public String getValueString(SliderYio sliderYio) {
            return "" + (sliderYio.getCurrentRunnerIndex() + 1);
        }
    }

    public SceneMoreSettingsMenu(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
    }

    public void create() {
        this.menuControllerYio.beginMenuCreation();
        changeBackground();
        createBackButton();
        createResetButton();
        initMetrics();
        createTopLabel();
        createSliders();
        createCheckPanel();
        createCheckButtons();
        createChooseLanguageButton();
        loadValues();
        this.menuControllerYio.endMenuCreation();
    }

    private void loadValues() {
        this.skinSlider.setCurrentRunnerIndex(Settings.skinIndex);
        this.sensitivitySlider.setCurrentRunnerIndex((int) (6.0f * Settings.sensitivity));
    }

    private void createSliders() {
        if (this.sliders == null) {
            initSliders();
        }
        Iterator it = this.sliders.iterator();
        while (it.hasNext()) {
            ((SliderYio) it.next()).appear();
        }
    }

    private void initSliders() {
        this.sliders = new ArrayList();
        RectangleYio pos = generateRectangle((1.0d - 0.6d) / 2.0d, 0.0d, 0.6d, 0.0d);
        this.skinSlider = new SliderYio(this.menuControllerYio, -1);
        this.skinSlider.setValues(0.0d, 0, 3, 1);
        this.skinSlider.setPosition(pos);
        this.skinSlider.setLinkedButton(this.topLabel, 0.05d);
        this.skinSlider.setTitle("skin");
        this.skinSlider.setBehavior(new C01262());
        this.sliders.add(this.skinSlider);
        this.sensitivitySlider = new SliderYio(this.menuControllerYio, -1);
        this.sensitivitySlider.setValues(0.5d, 0, 9, 1);
        this.sensitivitySlider.setPosition(pos);
        this.sensitivitySlider.setLinkedButton(this.topLabel, 0.2d);
        this.sensitivitySlider.setTitle("anim_style");
        this.sensitivitySlider.setBehavior(new C01273());
        this.sliders.add(this.sensitivitySlider);
        Iterator it = this.sliders.iterator();
        while (it.hasNext()) {
            InterfaceElement slider = (SliderYio) it.next();
            this.menuControllerYio.addElementToScene(slider);
            // TODO
            //slider.setVerticalTouchOffset(0.05f * GraphicsYio.height);
        }
    }

    private String getSkinStringBySliderIndex(int sliderIndex) {
        switch (sliderIndex) {
            case 1:
                return LanguagesManager.getInstance().getString("points");
            case 2:
                return LanguagesManager.getInstance().getString("grid");
            case 3:
                return LanguagesManager.getInstance().getString("skin_shroomarts");
            default:
                return LanguagesManager.getInstance().getString("original");
        }
    }

    private void createTopLabel() {
        this.topLabel = this.buttonFactory.getButton(generateRectangle(0.1d, 0.56d, 0.8d, 0.3d), 312, " ");
        this.topLabel.setTouchable(false);
        this.topLabel.setAnimation(1);
    }

    private void createChooseLanguageButton() {
        ButtonYio chooseLanguageButton = this.buttonFactory.getButton(generateRectangle(0.1d, 0.03d, 0.8d, SceneEditorInstruments.ICON_SIZE), 315, getString("language"));
        chooseLanguageButton.setReaction(Reaction.rbLanguageMenu);
        chooseLanguageButton.setAnimation(2);
    }

    private void createCheckButtons() {
        initCheckMetrics();
        this.chkWaterTexture = CheckButtonYio.getCheckButton(this.menuControllerYio, generateSquare(this.chkX, this.chkY - (this.hSize / 2.0d), this.hSize), 10);
        this.chkWaterTexture.setTouchPosition(generateRectangle(0.1d, this.chkY - (this.hTouchSize / 2.0d), 0.8d, this.hTouchSize));
        this.chkWaterTexture.setAnimation(2);
        this.chkY -= this.chkVerticalDelta;
        this.chkLongTapToMove = CheckButtonYio.getCheckButton(this.menuControllerYio, generateSquare(this.chkX, this.chkY - (this.hSize / 2.0d), this.hSize), 7);
        this.chkLongTapToMove.setTouchPosition(generateRectangle(0.1d, this.chkY - (this.hTouchSize / 2.0d), 0.8d, this.hTouchSize));
        this.chkLongTapToMove.setAnimation(2);
        this.chkY -= this.chkVerticalDelta;
        this.chkReplays = CheckButtonYio.getCheckButton(this.menuControllerYio, generateSquare(this.chkX, this.chkY - (this.hSize / 2.0d), this.hSize), 8);
        this.chkReplays.setTouchPosition(generateRectangle(0.1d, this.chkY - (this.hTouchSize / 2.0d), 0.8d, this.hTouchSize));
        this.chkReplays.setAnimation(2);
        this.chkY -= this.chkVerticalDelta;
        this.chkFastConstruction = CheckButtonYio.getCheckButton(this.menuControllerYio, generateSquare(this.chkX, this.chkY - (this.hSize / 2.0d), this.hSize), 9);
        this.chkFastConstruction.setTouchPosition(generateRectangle(0.1d, this.chkY - (this.hTouchSize / 2.0d), 0.8d, this.hTouchSize));
        this.chkFastConstruction.setAnimation(2);
        this.chkY -= this.chkVerticalDelta;
        this.chkLeftHanded = CheckButtonYio.getCheckButton(this.menuControllerYio, generateSquare(this.chkX, this.chkY - (this.hSize / 2.0d), this.hSize), 12);
        this.chkLeftHanded.setTouchPosition(generateRectangle(0.1d, this.chkY - (this.hTouchSize / 2.0d), 0.8d, this.hTouchSize));
        this.chkLeftHanded.setAnimation(2);
    }

    private void initCheckMetrics() {
        this.checkButtonSize = 0.045d;
        this.hSize = GraphicsYio.convertToHeight(this.checkButtonSize);
        this.hTouchSize = this.hSize * 1.5d;
        this.chkX = 0.87d - this.checkButtonSize;
        this.chkY = 0.506d;
        this.chkVerticalDelta = 0.04d;
    }

    private void createCheckPanel() {
        this.panelHeight = 0.29000000000000004d;
        ButtonYio chkPanel = this.buttonFactory.getButton(generateRectangle(0.1d, 0.53d - this.panelHeight, 0.8d, this.panelHeight), 316, null);
        if (chkPanel.notRendered()) {
            chkPanel.cleatText();
            chkPanel.addTextLine(LanguagesManager.getInstance().getString("water_texture"));
            chkPanel.addTextLine(LanguagesManager.getInstance().getString("hold_to_march"));
            chkPanel.addTextLine(LanguagesManager.getInstance().getString("replays"));
            chkPanel.addTextLine(LanguagesManager.getInstance().getString("fast_construction"));
            chkPanel.addTextLine(LanguagesManager.getInstance().getString("left_handed"));
            chkPanel.addTextLine(LanguagesManager.getInstance().getString(" "));
            chkPanel.addTextLine(LanguagesManager.getInstance().getString(" "));
            this.menuControllerYio.getButtonRenderer().renderButton(chkPanel);
        }
        chkPanel.setTouchable(false);
        chkPanel.setAnimation(2);
    }

    private void changeBackground() {
        this.menuControllerYio.getYioGdxGame().beginBackgroundChange(1, false, true);
    }

    private void createBackButton() {
        this.menuControllerYio.spawnBackButton(310, this.backReaction);
    }

    private void initMetrics() {
        this.panelHeight = 0.14d;
        this.offset = 0.03d;
    }

    private void createResetButton() {
        ButtonYio resetButton = this.buttonFactory.getButton(generateRectangle(0.55d, 0.9d, 0.4d, SceneEditorInstruments.ICON_SIZE), 311, getString("menu_reset"));
        resetButton.setReaction(Reaction.rbConfirmReset);
        resetButton.setAnimation(1);
        resetButton.disableTouchAnimation();
    }
}
