package io.androidovshchik.antiyoy.menu.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import java.util.ArrayList;
import java.util.Iterator;
import io.androidovshchik.antiyoy.menu.ButtonYio;
import io.androidovshchik.antiyoy.menu.InterfaceElement;
import io.androidovshchik.antiyoy.menu.MenuControllerYio;
import io.androidovshchik.antiyoy.menu.behaviors.Reaction;
import io.androidovshchik.antiyoy.menu.scenes.editor.SceneEditorInstruments;
import io.androidovshchik.antiyoy.menu.slider.SliderBehavior;
import io.androidovshchik.antiyoy.menu.slider.SliderYio;
import io.androidovshchik.antiyoy.stuff.GraphicsYio;
import io.androidovshchik.antiyoy.stuff.LanguagesManager;
import io.androidovshchik.antiyoy.stuff.RectangleYio;

public class SceneSkirmishMenu extends AbstractScene {
    private ButtonYio bottomLabel;
    public SliderYio colorsSlider;
    public SliderYio difficultySlider;
    public SliderYio mapSizeSlider;
    public SliderYio playersSlider;
    ArrayList<SliderYio> sliders = null;
    public ButtonYio startButton;
    private ButtonYio topLabel;

    class C01321 extends SliderBehavior {
        C01321() {
        }

        public String getValueString(SliderYio sliderYio) {
            return SceneSkirmishMenu.getDifficultyStringBySliderIndex(sliderYio.getCurrentRunnerIndex());
        }
    }

    class C01332 extends SliderBehavior {
        C01332() {
        }

        public String getValueString(SliderYio sliderYio) {
            return SceneSkirmishMenu.getMapSizeStringBySliderIndex(sliderYio.getCurrentRunnerIndex());
        }
    }

    class C01343 extends SliderBehavior {
        C01343() {
        }

        public String getValueString(SliderYio sliderYio) {
            return SceneSkirmishMenu.getHumansString(sliderYio.getCurrentRunnerIndex());
        }

        public void onAnotherSliderValueChanged(SliderYio sliderYio, SliderYio anotherSlider) {
            int currentRunnerIndex = sliderYio.getCurrentRunnerIndex();
            sliderYio.setNumberOfSegments(anotherSlider.getCurrentRunnerIndex() + anotherSlider.getMinNumber());
            sliderYio.setCurrentRunnerIndex(currentRunnerIndex);
        }
    }

    class C01354 extends SliderBehavior {
        C01354() {
        }

        public String getValueString(SliderYio sliderYio) {
            int currentRunnerIndex = sliderYio.getCurrentRunnerIndex();
            if (sliderYio.getMinNumber() + currentRunnerIndex <= 4) {
                return (sliderYio.getMinNumber() + currentRunnerIndex) + " " + LanguagesManager.getInstance().getString("color");
            }
            return (sliderYio.getMinNumber() + currentRunnerIndex) + " " + LanguagesManager.getInstance().getString("colors");
        }

        public void onAnotherSliderValueChanged(SliderYio sliderYio, SliderYio anotherSlider) {
            int s = 3;
            if (anotherSlider.getCurrentRunnerIndex() == 1) {
                s = 4;
            }
            if (anotherSlider.getCurrentRunnerIndex() == 2) {
                s = 5;
            }
            int currentRunnerIndex = sliderYio.getCurrentRunnerIndex();
            sliderYio.setNumberOfSegments(s);
            sliderYio.setCurrentRunnerIndex(currentRunnerIndex);
        }
    }

    public SceneSkirmishMenu(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
    }

    public void saveValues() {
        Preferences prefs = Gdx.app.getPreferences("skirmish");
        prefs.putInteger("difficulty", this.difficultySlider.getCurrentRunnerIndex());
        prefs.putInteger("map_size", this.mapSizeSlider.getCurrentRunnerIndex());
        prefs.putInteger("player_number", this.playersSlider.getCurrentRunnerIndex());
        prefs.putInteger("color_number", this.colorsSlider.getCurrentRunnerIndex());
        prefs.flush();
    }

    public void loadValues() {
        Preferences prefs = Gdx.app.getPreferences("skirmish");
        this.difficultySlider.setCurrentRunnerIndex(prefs.getInteger("difficulty", 1));
        this.mapSizeSlider.setCurrentRunnerIndex(prefs.getInteger("map_size", 1));
        this.colorsSlider.setCurrentRunnerIndex(prefs.getInteger("color_number", 2));
        this.playersSlider.setCurrentRunnerIndex(prefs.getInteger("player_number", 1));
    }

    public void create() {
        this.menuControllerYio.beginMenuCreation();
        this.menuControllerYio.getYioGdxGame().beginBackgroundChange(2, true, true);
        createLabels();
        createSliders();
        createBackButton();
        createStartButton();
        createMoreButton();
        loadValues();
        this.menuControllerYio.endMenuCreation();
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
        this.difficultySlider = new SliderYio(this.menuControllerYio, -1);
        this.difficultySlider.setValues(0.33d, 1, 5, 4);
        this.difficultySlider.setPosition(pos);
        this.difficultySlider.setLinkedButton(this.topLabel, 0.2d);
        this.difficultySlider.setTitle("difficulty");
        this.difficultySlider.setBehavior(new C01321());
        this.sliders.add(this.difficultySlider);
        this.mapSizeSlider = new SliderYio(this.menuControllerYio, -1);
        this.mapSizeSlider.setValues(0.5d, 1, 3, 4);
        this.mapSizeSlider.setPosition(pos);
        this.mapSizeSlider.setLinkedButton(this.topLabel, 0.05d);
        this.mapSizeSlider.setTitle("map_size");
        this.mapSizeSlider.setBehavior(new C01332());
        this.sliders.add(this.mapSizeSlider);
        this.playersSlider = new SliderYio(this.menuControllerYio, -1);
        this.playersSlider.setValues(0.2d, 0, 5, 4);
        this.playersSlider.setPosition(pos);
        this.playersSlider.setLinkedButton(this.bottomLabel, 0.24d);
        this.playersSlider.setTitle("player_number");
        this.playersSlider.setBehavior(new C01343());
        this.sliders.add(this.playersSlider);
        this.colorsSlider = new SliderYio(this.menuControllerYio, -1);
        this.colorsSlider.setValues(0.6d, 2, 6, 4);
        this.colorsSlider.setPosition(pos);
        this.colorsSlider.setLinkedButton(this.bottomLabel, 0.09d);
        this.colorsSlider.setTitle("color_number");
        this.colorsSlider.setBehavior(new C01354());
        this.sliders.add(this.colorsSlider);
        Iterator it = this.sliders.iterator();
        while (it.hasNext()) {
            InterfaceElement slider = (SliderYio) it.next();
            this.menuControllerYio.addElementToScene(slider);
            slider.setVerticalTouchOffset(0.05f * GraphicsYio.height);
        }
        this.colorsSlider.addListener(this.playersSlider);
        this.mapSizeSlider.addListener(this.colorsSlider);
    }

    private void createLabels() {
        this.topLabel = this.buttonFactory.getButton(generateRectangle(0.1d, 0.5d, 0.8d, 0.32d), 88, " ");
        this.topLabel.setTouchable(false);
        this.topLabel.setAnimation(7);
        this.bottomLabel = this.buttonFactory.getButton(generateRectangle(0.1d, 0.08d, 0.8d, 0.36d), 87, " ");
        this.bottomLabel.setTouchable(false);
        this.bottomLabel.setAnimation(6);
    }

    private void createMoreButton() {
        ButtonYio moreButton = this.buttonFactory.getButton(generateRectangle(0.56d, 0.08d, 0.3d, 0.04d), 86, getString("more"));
        moreButton.setReaction(Reaction.rbMoreSkirmishOptions);
        moreButton.setAnimation(6);
        moreButton.setShadow(false);
        moreButton.setTouchOffset(0.1f * GraphicsYio.width);
        moreButton.disableTouchAnimation();
    }

    private void createStartButton() {
        this.startButton = this.buttonFactory.getButton(generateRectangle(0.55d, 0.9d, 0.4d, SceneEditorInstruments.ICON_SIZE), 83, getString("game_settings_start"));
        this.startButton.setReaction(Reaction.rbStartSkirmishGame);
        this.startButton.setAnimation(1);
        this.startButton.disableTouchAnimation();
    }

    private void createBackButton() {
        this.menuControllerYio.spawnBackButton(80, Reaction.rbBackFromSkirmish).setTouchable(true);
    }

    public static String getHumansString(int n) {
        LanguagesManager instance = LanguagesManager.getInstance();
        if (n == 0) {
            return instance.getString("ai_only");
        }
        if (n == 1) {
            return instance.getString("single_player");
        }
        return instance.getString("multiplayer") + " " + n + "x";
    }

    public static String getColorStringBySliderIndex(int sliderIndex) {
        switch (sliderIndex) {
            case 1:
                return LanguagesManager.getInstance().getString("green_menu");
            case 2:
                return LanguagesManager.getInstance().getString("red_menu");
            case 3:
                return LanguagesManager.getInstance().getString("magenta_menu");
            case 4:
                return LanguagesManager.getInstance().getString("cyan_menu");
            case 5:
                return LanguagesManager.getInstance().getString("yellow_menu");
            case 6:
                return LanguagesManager.getInstance().getString("red_menu") + "+";
            case 7:
                return LanguagesManager.getInstance().getString("green_menu") + "+";
            default:
                return LanguagesManager.getInstance().getString("random");
        }
    }

    public static String getDifficultyStringBySliderIndex(int sliderIndex) {
        switch (sliderIndex) {
            case 1:
                return LanguagesManager.getInstance().getString("normal");
            case 2:
                return LanguagesManager.getInstance().getString("hard");
            case 3:
                return LanguagesManager.getInstance().getString("expert");
            case 4:
                return LanguagesManager.getInstance().getString("balancer");
            default:
                return LanguagesManager.getInstance().getString("easy");
        }
    }

    public static String getMapSizeStringBySliderIndex(int sliderIndex) {
        switch (sliderIndex) {
            case 1:
                return LanguagesManager.getInstance().getString("medium");
            case 2:
                return LanguagesManager.getInstance().getString("big");
            default:
                return LanguagesManager.getInstance().getString("small");
        }
    }
}
