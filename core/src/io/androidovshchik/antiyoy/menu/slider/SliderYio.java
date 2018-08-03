package io.androidovshchik.antiyoy.menu.slider;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import java.util.ArrayList;
import yio.tro.antiyoy.factor_yio.FactorYio;
import yio.tro.antiyoy.menu.ButtonYio;
import yio.tro.antiyoy.menu.InterfaceElement;
import yio.tro.antiyoy.menu.MenuControllerYio;
import yio.tro.antiyoy.menu.render.MenuRender;
import yio.tro.antiyoy.stuff.Fonts;
import yio.tro.antiyoy.stuff.GraphicsYio;
import yio.tro.antiyoy.stuff.LanguagesManager;
import yio.tro.antiyoy.stuff.PointYio;
import yio.tro.antiyoy.stuff.RectangleYio;

public class SliderYio extends InterfaceElement implements SliderListener {
    boolean accentVisible = false;
    private float animDistance;
    int animType;
    public FactorYio appearFactor = new FactorYio();
    SliderBehavior behavior = new SbDefault();
    float circleDefaultSize = (0.012f * ((float) Gdx.graphics.getHeight()));
    public float circleSize = this.circleDefaultSize;
    float circleSizeDelta = (0.005f * ((float) Gdx.graphics.getHeight()));
    public float currentVerticalPos;
    boolean internalSegmentsHidden = false;
    boolean isCurrentlyPressed;
    public ButtonYio linkedButton;
    float linkedDelta;
    ArrayList<SliderListener> listeners = new ArrayList();
    boolean listenersEnabled = true;
    MenuControllerYio menuControllerYio;
    int minNumber;
    public int numberOfSegments;
    RectangleYio pos = new RectangleYio(0.0d, 0.0d, 0.0d, 0.0d);
    public float runnerValue;
    public float segmentSize;
    public FactorYio sizeFactor = new FactorYio();
    boolean solidWidth = true;
    public float textWidth;
    public String title = null;
    public BitmapFont titleFont = Fonts.gameFont;
    float titleOffset = (0.125f * GraphicsYio.width);
    public PointYio titlePosition = new PointYio();
    private RectangleYio touchRectangle = new RectangleYio();
    boolean touchable = true;
    public BitmapFont valueFont = Fonts.smallerMenuFont;
    float valueOffset = (0.065f * GraphicsYio.width);
    String valueString;
    public PointYio valueStringPosition = new PointYio();
    float verticalTouchOffset = (0.1f * ((float) Gdx.graphics.getHeight()));
    float viewMagnifier;
    float viewWidth;
    float viewX;

    public SliderYio(MenuControllerYio menuControllerYio, int id) {
        super(id);
        this.menuControllerYio = menuControllerYio;
    }

    boolean isCoorInsideSlider(float x, float y) {
        return ((double) x) > this.pos.f146x - ((double) (((float) Gdx.graphics.getWidth()) * 0.05f)) && ((double) x) < (this.pos.f146x + this.pos.width) + ((double) (((float) Gdx.graphics.getWidth()) * 0.05f)) && y > this.currentVerticalPos - this.verticalTouchOffset && y < this.currentVerticalPos + this.verticalTouchOffset;
    }

    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (!this.touchable || !isCoorInsideSlider((float) screenX, (float) screenY) || this.appearFactor.get() != 1.0f) {
            return false;
        }
        this.sizeFactor.appear(3, 2.0d);
        this.isCurrentlyPressed = true;
        setValueByX((float) screenX);
        return true;
    }

    public boolean touchDrag(int screenX, int screenY, int pointer) {
        if (!this.isCurrentlyPressed) {
            return false;
        }
        setValueByX((float) screenX);
        return true;
    }

    public void setTouchable(boolean touchable) {
        this.touchable = touchable;
    }

    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (!this.isCurrentlyPressed) {
            return false;
        }
        this.sizeFactor.destroy(1, 1.0d);
        this.isCurrentlyPressed = false;
        updateValueString();
        return true;
    }

    public boolean isVisible() {
        return this.appearFactor.get() > 0.0f;
    }

    public MenuRender getRenderSystem() {
        return MenuRender.renderSlider;
    }

    void setValueByX(float x) {
        int lastIndex = getCurrentRunnerIndex();
        this.runnerValue = (float) (((double) ((float) (((double) x) - this.pos.f146x))) / this.pos.width);
        if (this.runnerValue < 0.0f) {
            this.runnerValue = 0.0f;
        }
        if (this.runnerValue > 1.0f) {
            this.runnerValue = 1.0f;
        }
        if (lastIndex != getCurrentRunnerIndex()) {
            this.behavior.onValueChanged(this);
            updateValueString();
        }
    }

    void pullRunnerToCenterOfSegment() {
        this.runnerValue = (float) (((double) this.runnerValue) + (0.2d * (((double) (((float) getCurrentRunnerIndex()) * this.segmentSize)) - ((double) this.runnerValue))));
        limitRunnerValue();
    }

    private void limitRunnerValue() {
        if (this.runnerValue > 1.0f) {
            this.runnerValue = 1.0f;
        }
        if (this.runnerValue < 0.0f) {
            this.runnerValue = 0.0f;
        }
    }

    private void updateVerticalPos() {
        if (this.linkedButton != null) {
            updateVerticalPosByLinkedButton();
            return;
        }
        switch (this.animType) {
            case 1:
                this.currentVerticalPos = (float) ((((double) (1.0f - this.appearFactor.get())) * (((double) (1.1f * ((float) Gdx.graphics.getHeight()))) - this.pos.f147y)) + this.pos.f147y);
                return;
            case 2:
                this.currentVerticalPos = (float) ((((double) this.appearFactor.get()) * (this.pos.f147y + ((double) (((float) Gdx.graphics.getHeight()) * 0.1f)))) - ((double) (((float) Gdx.graphics.getHeight()) * 0.1f)));
                return;
            case 5:
                this.currentVerticalPos = (float) (((double) (((float) Gdx.graphics.getHeight()) * 0.5f)) + ((this.pos.f147y - ((double) (((float) Gdx.graphics.getHeight()) * 0.5f))) * ((double) this.appearFactor.get())));
                return;
            default:
                animNone();
                return;
        }
    }

    private void animNone() {
        this.currentVerticalPos = (float) this.pos.f147y;
    }

    private void updateVerticalPosByLinkedButton() {
        this.currentVerticalPos = (float) (this.linkedButton.animPos.f147y + ((double) this.linkedDelta));
    }

    public void move() {
        if (this.appearFactor.hasToMove()) {
            this.appearFactor.move();
            updateViewValues();
        }
        if (this.sizeFactor.hasToMove()) {
            this.sizeFactor.move();
        }
        if (this.appearFactor.get() != 0.0f) {
            updateCircleSize();
            updateVerticalPos();
            updateTitlePosition();
            updateValueStringPosition();
            if (!this.isCurrentlyPressed) {
                pullRunnerToCenterOfSegment();
            }
        }
    }

    private void updateValueStringPosition() {
        this.valueStringPosition.f144x = (this.viewX + this.viewWidth) - this.textWidth;
        this.valueStringPosition.f145y = this.currentVerticalPos + this.valueOffset;
    }

    private void updateTitlePosition() {
        this.titlePosition.f144x = this.viewX - (0.015f * GraphicsYio.width);
        this.titlePosition.f145y = this.currentVerticalPos + this.titleOffset;
    }

    private void updateCircleSize() {
        this.circleSize = this.circleDefaultSize + (this.circleSizeDelta * this.sizeFactor.get());
    }

    private void updateViewValues() {
        if (this.solidWidth) {
            this.viewWidth = (float) this.pos.width;
            this.viewX = (float) this.pos.f146x;
            return;
        }
        this.viewWidth = (float) (this.pos.width * ((double) this.appearFactor.get()));
        this.viewX = (float) ((this.pos.f146x + (0.5d * this.pos.width)) - ((double) (0.5f * this.viewWidth)));
    }

    public float getViewX() {
        return this.viewX;
    }

    public float getViewWidth() {
        return this.viewWidth;
    }

    public float getRunnerValueViewX() {
        return getViewX() + (this.runnerValue * getViewWidth());
    }

    public float getRunnerValue() {
        return this.runnerValue;
    }

    public FactorYio getFactor() {
        return this.appearFactor;
    }

    public void setPosition(RectangleYio position) {
        this.pos.setBy(position);
    }

    public RectangleYio getPosition() {
        return this.pos;
    }

    public void appear() {
        this.appearFactor.appear(3, 1.8d);
        this.appearFactor.setValues(0.001d, 0.001d);
    }

    public void destroy() {
        this.appearFactor.destroy(2, 2.0d);
        this.appearFactor.setDy(0.0d);
    }

    public boolean checkToPerformAction() {
        return false;
    }

    public boolean isTouchable() {
        return this.touchable;
    }

    public boolean isButton() {
        return false;
    }

    public void setValues(double runnerValue, int minNumber, int maxNumber, int animType) {
        setRunnerValue((float) runnerValue);
        setNumberOfSegments(maxNumber - minNumber);
        this.animType = animType;
        this.minNumber = minNumber;
        this.animDistance = -1.0f;
        updateValueString();
    }

    public void setTitle(String titleKey) {
        this.title = LanguagesManager.getInstance().getString(titleKey);
    }

    public void setRunnerValue(float runnerValue) {
        if (runnerValue > 1.0f) {
            runnerValue = 1.0f;
        }
        this.runnerValue = runnerValue;
    }

    public void setCurrentRunnerIndex(int index) {
        setRunnerValue(((float) index) / ((float) this.numberOfSegments));
        updateValueString();
    }

    public int getCurrentRunnerIndex() {
        return (int) (((double) (this.runnerValue / this.segmentSize)) + 0.5d);
    }

    public void setNumberOfSegments(int numberOfSegments) {
        if (this.numberOfSegments != numberOfSegments) {
            this.numberOfSegments = numberOfSegments;
            this.segmentSize = 1.0f / ((float) numberOfSegments);
            this.viewMagnifier = (((float) numberOfSegments) + 1.0f) / ((float) numberOfSegments);
        }
    }

    public void addListener(SliderListener listener) {
        if (!this.listeners.contains(listener)) {
            this.listeners.add(listener);
        }
    }

    void notifyListeners() {
        if (this.listenersEnabled) {
            for (int i = 0; i < this.listeners.size(); i++) {
                ((SliderListener) this.listeners.get(i)).onSliderChange(this);
            }
        }
    }

    public void setListenersEnabled(boolean listenersEnabled) {
        this.listenersEnabled = listenersEnabled;
    }

    public void onSliderChange(SliderYio sliderYio) {
        this.behavior.onAnotherSliderValueChanged(this, sliderYio);
        updateValueString();
    }

    public float getSegmentCircleSize() {
        return 0.4f * this.circleSize;
    }

    public void setLinkedButton(ButtonYio linkedButton, double linkedDelta) {
        this.linkedButton = linkedButton;
        this.linkedDelta = ((float) linkedDelta) * GraphicsYio.height;
    }

    public float getSegmentLeftSidePos(int index) {
        return (float) (this.pos.f146x + (((double) (((float) index) * this.segmentSize)) * this.pos.width));
    }

    public boolean isInternalSegmentsHidden() {
        return this.internalSegmentsHidden;
    }

    public void setInternalSegmentsHidden(boolean internalSegmentsHidden) {
        this.internalSegmentsHidden = internalSegmentsHidden;
    }

    public RectangleYio getTouchRectangle() {
        this.touchRectangle.f146x = this.pos.f146x - ((double) (0.05f * ((float) Gdx.graphics.getWidth())));
        this.touchRectangle.f147y = (double) (this.currentVerticalPos - this.verticalTouchOffset);
        this.touchRectangle.width = this.pos.width + ((double) (0.1f * ((float) Gdx.graphics.getWidth())));
        this.touchRectangle.height = (double) (2.0f * this.verticalTouchOffset);
        return this.touchRectangle;
    }

    public void updateValueString() {
        this.valueString = this.behavior.getValueString(this);
        this.textWidth = GraphicsYio.getTextWidth(this.valueFont, this.valueString);
        notifyListeners();
    }

    public void setVerticalTouchOffset(float verticalTouchOffset) {
        this.verticalTouchOffset = verticalTouchOffset;
    }

    public String getValueString() {
        return this.valueString;
    }

    public boolean isAccentVisible() {
        return this.accentVisible;
    }

    public void setAccentVisible(boolean accentVisible) {
        this.accentVisible = accentVisible;
    }

    public void setSolidWidth(boolean solidWidth) {
        this.solidWidth = solidWidth;
    }

    public void setTitleOffset(float titleOffset) {
        this.titleOffset = titleOffset;
    }

    public int getMinNumber() {
        return this.minNumber;
    }

    public void setValueOffset(float valueOffset) {
        this.valueOffset = valueOffset;
    }

    public void setBehavior(SliderBehavior behavior) {
        this.behavior = behavior;
        updateValueString();
    }

    public float getLinkedDelta() {
        return this.linkedDelta;
    }

    public String toString() {
        return "[Slider: " + this.title + "]";
    }
}
