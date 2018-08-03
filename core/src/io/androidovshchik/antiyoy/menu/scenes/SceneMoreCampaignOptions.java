package io.androidovshchik.antiyoy.menu.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import yio.tro.antiyoy.menu.ButtonYio;
import yio.tro.antiyoy.menu.CheckButtonYio;
import yio.tro.antiyoy.menu.MenuControllerYio;
import yio.tro.antiyoy.menu.behaviors.Reaction;
import yio.tro.antiyoy.menu.slider.SliderBehavior;
import yio.tro.antiyoy.menu.slider.SliderYio;
import yio.tro.antiyoy.stuff.GraphicsYio;
import yio.tro.antiyoy.stuff.RectangleYio;

public class SceneMoreCampaignOptions extends AbstractScene {
    private CheckButtonYio chkSlayRules;
    public SliderYio colorOffsetSlider = null;
    private ButtonYio label;

    class C01241 extends SliderBehavior {
        C01241() {
        }

        public String getValueString(SliderYio sliderYio) {
            return SceneSkirmishMenu.getColorStringBySliderIndex(sliderYio.getCurrentRunnerIndex());
        }
    }

    public SceneMoreCampaignOptions(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
    }

    public void create() {
        this.menuControllerYio.beginMenuCreation();
        this.menuControllerYio.getYioGdxGame().beginBackgroundChange(2, true, true);
        this.menuControllerYio.spawnBackButton(551, Reaction.rbExitToCampaign);
        createInternals();
        this.menuControllerYio.endMenuCreation();
    }

    public void createInternals() {
        createLabel();
        createCheckButtons();
        createColorSlider();
        loadValues();
    }

    public void prepare() {
        if (this.colorOffsetSlider == null) {
            createInternals();
        }
    }

    private void createColorSlider() {
        checkToInitSlider();
        this.colorOffsetSlider.setNumberOfSegments(7);
        this.colorOffsetSlider.appear();
    }

    private void checkToInitSlider() {
        if (this.colorOffsetSlider == null) {
            initColorSlider();
        }
    }

    private void loadValues() {
        Preferences prefs = Gdx.app.getPreferences("campaign_options");
        this.colorOffsetSlider.setCurrentRunnerIndex(prefs.getInteger("color_offset", 0));
        this.chkSlayRules.setChecked(prefs.getBoolean("slay_rules", false));
    }

    public void saveValues() {
        Preferences prefs = Gdx.app.getPreferences("campaign_options");
        prefs.putInteger("color_offset", this.colorOffsetSlider.getCurrentRunnerIndex());
        prefs.putBoolean("slay_rules", this.chkSlayRules.isChecked());
        prefs.flush();
    }

    private void initColorSlider() {
        RectangleYio pos = generateRectangle((1.0d - 0.7d) / 2.0d, 0.0d, 0.7d, 0.0d);
        this.colorOffsetSlider = new SliderYio(this.menuControllerYio, -1);
        this.colorOffsetSlider.setValues(0.0d, 0, 6, 1);
        this.colorOffsetSlider.setPosition(pos);
        this.colorOffsetSlider.setLinkedButton(this.label, 0.27d);
        this.colorOffsetSlider.setTitle("player_color");
        this.colorOffsetSlider.setBehavior(new C01241());
        this.menuControllerYio.addElementToScene(this.colorOffsetSlider);
        this.colorOffsetSlider.setVerticalTouchOffset(0.05f * GraphicsYio.height);
        this.colorOffsetSlider.setTitleOffset(0.125f * GraphicsYio.width);
    }

    private void createLabel() {
        this.label = this.buttonFactory.getButton(generateRectangle(0.05d, 0.45d, 0.9d, 0.4d), 552, null);
        if (this.label.notRendered()) {
            this.label.cleatText();
            this.label.addTextLine(" ");
            this.label.addTextLine(" ");
            this.label.addTextLine(" ");
            this.label.addTextLine(" ");
            this.label.addTextLine(" ");
            this.label.addTextLine(" ");
            this.label.addTextLine(getString("slay_rules"));
            this.label.addTextLine(" ");
            this.label.setTextOffset(0.09f * GraphicsYio.width);
            this.menuControllerYio.getButtonRenderer().renderButton(this.label);
        }
        this.label.setTouchable(false);
        this.label.setAnimation(1);
    }

    private void createCheckButtons() {
        double hSize = GraphicsYio.convertToHeight(0.05d);
        this.chkSlayRules = CheckButtonYio.getCheckButton(this.menuControllerYio, generateSquare(0.88d - 0.05d, 0.53d - (hSize / 2.0d), hSize), 17);
        this.chkSlayRules.setTouchPosition(generateRectangle(0.05d, 0.53d - (1.5d * hSize), 0.9d, hSize * 3.0d));
        this.chkSlayRules.setAnimation(1);
    }
}
