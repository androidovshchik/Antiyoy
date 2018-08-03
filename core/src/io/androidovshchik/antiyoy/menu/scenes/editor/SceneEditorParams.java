package io.androidovshchik.antiyoy.menu.scenes.editor;

import java.util.ArrayList;
import java.util.Iterator;
import io.androidovshchik.antiyoy.gameplay.GameController;
import io.androidovshchik.antiyoy.gameplay.rules.GameRules;
import io.androidovshchik.antiyoy.menu.ButtonYio;
import io.androidovshchik.antiyoy.menu.MenuControllerYio;
import io.androidovshchik.antiyoy.menu.behaviors.editor.EditorReactions;
import io.androidovshchik.antiyoy.menu.scenes.SceneSkirmishMenu;
import io.androidovshchik.antiyoy.menu.slider.SliderBehavior;
import io.androidovshchik.antiyoy.menu.slider.SliderYio;
import io.androidovshchik.antiyoy.stuff.GraphicsYio;

public class SceneEditorParams extends AbstractEditorPanel {
    private double bSize;
    private ButtonYio basePanel;
    private double bottom;
    private SliderYio colorSlider;
    private SliderYio difficultySlider;
    private double pHeight;
    private SliderYio playersSlider;
    ArrayList<SliderYio> sliders = null;
    private double yOffset;

    class C01431 extends SliderBehavior {
        C01431() {
        }

        public String getValueString(SliderYio sliderYio) {
            return SceneSkirmishMenu.getHumansString(sliderYio.getCurrentRunnerIndex());
        }

        public void onValueChanged(SliderYio sliderYio) {
            SceneEditorParams.this.onPlayersNumberChanged();
        }
    }

    class C01442 extends SliderBehavior {
        C01442() {
        }

        public String getValueString(SliderYio sliderYio) {
            return SceneSkirmishMenu.getDifficultyStringBySliderIndex(sliderYio.getCurrentRunnerIndex());
        }

        public void onValueChanged(SliderYio sliderYio) {
            SceneEditorParams.this.onDifficultyChanged();
        }
    }

    class C01453 extends SliderBehavior {
        C01453() {
        }

        public String getValueString(SliderYio sliderYio) {
            return SceneSkirmishMenu.getColorStringBySliderIndex(sliderYio.getCurrentRunnerIndex());
        }

        public void onValueChanged(SliderYio sliderYio) {
            SceneEditorParams.this.onColorOffsetChanged();
        }
    }

    public SceneEditorParams(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
        initMetrics();
    }

    private void initMetrics() {
        this.bottom = SceneEditorInstruments.ICON_SIZE;
        this.pHeight = 0.6d;
        this.yOffset = 0.02d;
        this.bSize = 0.06d;
    }

    public void create() {
        createBasePanel();
        createClearLevelButton();
        createRandomizeButton();
        createSliders();
        loadValues();
    }

    private void createRandomizeButton() {
        ButtonYio randomizeButton = this.buttonFactory.getButton(generateRectangle(0.1d, (this.bottom + this.pHeight) - (2.0d * (this.yOffset + this.bSize)), 0.8d, this.bSize), 593, getString("randomize"));
        randomizeButton.setReaction(EditorReactions.rbEditorShowConfirmRandomize);
        randomizeButton.setAnimation(2);
        randomizeButton.enableRectangularMask();
        randomizeButton.setShadow(false);
    }

    private void createClearLevelButton() {
        ButtonYio clearLevelButton = this.buttonFactory.getButton(generateRectangle(0.1d, ((this.bottom + this.pHeight) - this.yOffset) - this.bSize, 0.8d, this.bSize), 591, getString("editor_clear"));
        clearLevelButton.setReaction(EditorReactions.rbEditorConfirmClearLevelMenu);
        clearLevelButton.setAnimation(2);
        clearLevelButton.enableRectangularMask();
        clearLevelButton.setShadow(false);
    }

    private void createBasePanel() {
        this.basePanel = this.buttonFactory.getButton(generateRectangle(0.0d, this.bottom, 1.0d, this.pHeight), 590, null);
        this.menuControllerYio.loadButtonOnce(this.basePanel, "gray_pixel.png");
        this.basePanel.setTouchable(false);
        this.basePanel.setAnimation(2);
        this.basePanel.disableTouchAnimation();
        this.basePanel.enableRectangularMask();
        this.basePanel.setShadow(true);
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

    private void loadValues() {
        this.playersSlider.setCurrentRunnerIndex(getGameController().playersNumber);
        this.difficultySlider.setCurrentRunnerIndex(GameRules.difficulty);
        this.colorSlider.setCurrentRunnerIndex(GameRules.editorChosenColor);
    }

    private GameController getGameController() {
        return this.menuControllerYio.yioGdxGame.gameController;
    }

    private void initSliders() {
        this.sliders = new ArrayList();
        double curSlY = this.pHeight - 0.27d;
        this.playersSlider = new SliderYio(this.menuControllerYio, -1);
        this.playersSlider.setValues(0.0d, 0, 7, 2);
        this.playersSlider.setPosition(generateRectangle(0.1d, 0.0d, 0.8d, 0.0d));
        this.playersSlider.setLinkedButton(this.basePanel, curSlY);
        this.playersSlider.setTitle("player_number");
        this.playersSlider.setBehavior(new C01431());
        this.sliders.add(this.playersSlider);
        curSlY -= 0.13d;
        this.difficultySlider = new SliderYio(this.menuControllerYio, -1);
        this.difficultySlider.setValues(1.0d, 0, 4, 2);
        this.difficultySlider.setPosition(generateRectangle(0.1d, 0.0d, 0.8d, 0.0d));
        this.difficultySlider.setLinkedButton(this.basePanel, curSlY);
        this.difficultySlider.setTitle("difficulty");
        this.difficultySlider.setBehavior(new C01442());
        this.sliders.add(this.difficultySlider);
        curSlY -= 0.13d;
        this.colorSlider = new SliderYio(this.menuControllerYio, -1);
        this.colorSlider.setValues(0.0d, 0, 7, 2);
        this.colorSlider.setPosition(generateRectangle(0.1d, 0.0d, 0.8d, 0.0d));
        this.colorSlider.setLinkedButton(this.basePanel, curSlY);
        this.colorSlider.setTitle("player_color");
        this.colorSlider.setBehavior(new C01453());
        this.sliders.add(this.colorSlider);
        curSlY -= 0.13d;
        Iterator it = this.sliders.iterator();
        while (it.hasNext()) {
            SliderYio slider = (SliderYio) it.next();
            this.menuControllerYio.addElementToScene(slider);
            slider.setTitleOffset(0.11f * GraphicsYio.width);
            slider.setVerticalTouchOffset(0.05f * GraphicsYio.height);
        }
    }

    void onPlayersNumberChanged() {
        getGameController().setPlayersNumber(this.playersSlider.getCurrentRunnerIndex());
    }

    void onDifficultyChanged() {
        GameRules.setDifficulty(this.difficultySlider.getCurrentRunnerIndex());
    }

    void onColorOffsetChanged() {
        GameRules.setEditorChosenColor(this.colorSlider.getCurrentRunnerIndex());
    }

    public void hide() {
        for (int id = 590; id < 599; id++) {
            this.menuControllerYio.destroyButton(id);
        }
        Iterator it = this.sliders.iterator();
        while (it.hasNext()) {
            ((SliderYio) it.next()).destroy();
        }
    }

    public boolean isCurrentlyOpened() {
        if (this.sliders != null && this.basePanel.appearFactor.get() == 1.0f) {
            return true;
        }
        return false;
    }
}
