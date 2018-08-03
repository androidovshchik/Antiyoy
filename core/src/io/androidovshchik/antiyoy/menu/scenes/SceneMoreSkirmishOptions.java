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

public class SceneMoreSkirmishOptions extends AbstractScene {
    public CheckButtonYio chkDiplomacy;
    public CheckButtonYio chkFogOfWar;
    public CheckButtonYio chkSlayRules;
    public SliderYio colorOffsetSlider = null;
    private ButtonYio label;

    class C01281 extends SliderBehavior {
        C01281() {
        }

        public String getValueString(SliderYio sliderYio) {
            return SceneSkirmishMenu.getColorStringBySliderIndex(sliderYio.getCurrentRunnerIndex());
        }
    }

    public SceneMoreSkirmishOptions(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
    }

    public void create() {
        this.menuControllerYio.beginMenuCreation();
        this.menuControllerYio.getYioGdxGame().beginBackgroundChange(2, true, true);
        this.menuControllerYio.spawnBackButton(231, Reaction.rbSaveMoreSkirmishOptions);
        createLabel();
        createCheckButtons();
        createColorSlider();
        loadValues();
        this.menuControllerYio.endMenuCreation();
    }

    private void createColorSlider() {
        if (this.colorOffsetSlider == null) {
            initColorSlider();
        }
        this.colorOffsetSlider.setNumberOfSegments(getNumberOfSegmentsForColorOffset());
        this.colorOffsetSlider.appear();
    }

    private int getNumberOfSegmentsForColorOffset() {
        return Scenes.sceneSkirmishMenu.colorsSlider.getMinNumber() + 5;
    }

    private void loadValues() {
        Preferences prefs = Gdx.app.getPreferences("skirmish");
        this.colorOffsetSlider.setCurrentRunnerIndex(prefs.getInteger("color_offset", 0));
        this.chkSlayRules.setChecked(prefs.getBoolean("slay_rules", false));
        this.chkFogOfWar.setChecked(prefs.getBoolean("fog_of_war", false));
        this.chkDiplomacy.setChecked(prefs.getBoolean("diplomacy", false));
    }

    public void saveValues() {
        Preferences prefs = Gdx.app.getPreferences("skirmish");
        prefs.putInteger("color_offset", this.colorOffsetSlider.getCurrentRunnerIndex());
        prefs.putBoolean("slay_rules", this.chkSlayRules.isChecked());
        prefs.putBoolean("fog_of_war", this.chkFogOfWar.isChecked());
        prefs.putBoolean("diplomacy", this.chkDiplomacy.isChecked());
        prefs.flush();
    }

    private void initColorSlider() {
        RectangleYio pos = generateRectangle((1.0d - 0.7d) / 2.0d, 0.0d, 0.7d, 0.0d);
        this.colorOffsetSlider = new SliderYio(this.menuControllerYio, -1);
        this.colorOffsetSlider.setValues(0.0d, 0, 6, 1);
        this.colorOffsetSlider.setPosition(pos);
        this.colorOffsetSlider.setLinkedButton(this.label, 0.37d);
        this.colorOffsetSlider.setTitle("player_color");
        this.colorOffsetSlider.setBehavior(new C01281());
        this.menuControllerYio.addElementToScene(this.colorOffsetSlider);
        this.colorOffsetSlider.setVerticalTouchOffset(0.05f * GraphicsYio.height);
        this.colorOffsetSlider.setTitleOffset(0.125f * GraphicsYio.width);
    }

    private void createCheckButtons() {
        double hSize = GraphicsYio.convertToHeight(0.045d);
        double chkX = 0.88d - 0.045d;
        double delta = hSize + 0.062d;
        this.chkSlayRules = CheckButtonYio.getCheckButton(this.menuControllerYio, generateSquare(chkX, 0.6d - (hSize / 2.0d), hSize), 16);
        this.chkSlayRules.setTouchPosition(generateRectangle(0.05d, 0.6d - (1.5d * hSize), 0.9d, hSize * 3.0d));
        this.chkSlayRules.setAnimation(7);
        double chkY = 0.6d - delta;
        this.chkFogOfWar = CheckButtonYio.getCheckButton(this.menuControllerYio, generateSquare(chkX, chkY - (hSize / 2.0d), hSize), 19);
        this.chkFogOfWar.setTouchPosition(generateRectangle(0.05d, chkY - (1.5d * hSize), 0.9d, hSize * 3.0d));
        this.chkFogOfWar.setAnimation(7);
        chkY -= delta;
        this.chkDiplomacy = CheckButtonYio.getCheckButton(this.menuControllerYio, generateSquare(chkX, chkY - (hSize / 2.0d), hSize), 20);
        this.chkDiplomacy.setTouchPosition(generateRectangle(0.05d, chkY - (1.5d * hSize), 0.9d, hSize * 3.0d));
        this.chkDiplomacy.setAnimation(7);
        chkY -= delta;
    }

    private void createLabel() {
        this.label = this.buttonFactory.getButton(generateRectangle(0.05d, 0.35d, 0.9d, 0.5d), 232, null);
        if (this.label.notRendered()) {
            this.label.cleatText();
            this.label.addTextLine(" ");
            this.label.addTextLine(" ");
            this.label.addTextLine(" ");
            this.label.addTextLine(" ");
            this.label.addTextLine(" ");
            this.label.addTextLine(getString("slay_rules"));
            this.label.addTextLine(" ");
            this.label.addTextLine(getString("fog_of_war"));
            this.label.addTextLine(" ");
            this.label.addTextLine(getString("diplomacy"));
            this.label.addTextLine(" ");
            this.label.setTextOffset(0.09f * GraphicsYio.width);
            this.menuControllerYio.getButtonRenderer().renderButton(this.label);
        }
        this.label.setTouchable(false);
        this.label.setAnimation(7);
    }
}
