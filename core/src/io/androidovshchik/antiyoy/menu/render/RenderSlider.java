package io.androidovshchik.antiyoy.menu.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.androidovshchik.antiyoy.menu.InterfaceElement;
import io.androidovshchik.antiyoy.menu.slider.SliderYio;
import io.androidovshchik.antiyoy.stuff.GraphicsYio;

public class RenderSlider extends MenuRender {
    TextureRegion accentPixel;
    TextureRegion blackCircle;
    TextureRegion buildCircle;
    TextureRegion carryCircle;
    TextureRegion cultivateCircle;
    TextureRegion defenseCircle;
    private SliderYio slider;
    float sliderLineHeight = (0.007f * ((float) Gdx.graphics.getWidth()));
    float sliderLineHeightHalved = (this.sliderLineHeight / 2.0f);
    TextureRegion untouchablePixel;
    TextureRegion untouchableValue;

    public void loadTextures() {
        this.blackCircle = GraphicsYio.loadTextureRegion("menu/slider/black_circle.png", true);
        this.accentPixel = GraphicsYio.loadTextureRegion("pixels/slider_accent.png", false);
        this.untouchableValue = GraphicsYio.loadTextureRegion("menu/slider/untouchable_slider_value.png", true);
        this.untouchablePixel = GraphicsYio.loadTextureRegion("pixels/blue_pixel.png", false);
    }

    public void renderFirstLayer(InterfaceElement element) {
    }

    public void renderSecondLayer(InterfaceElement element) {
        this.slider = (SliderYio) element;
        checkToChangeBatchAlpha();
        renderBlackLine();
        renderAccent();
        renderSegments();
        renderValueCircle();
        renderValueText();
        renderTitle();
        GraphicsYio.setBatchAlpha(this.batch, 1.0d);
    }

    private void renderTitle() {
        Color color = this.slider.titleFont.getColor();
        this.slider.titleFont.setColor(Color.BLACK);
        GraphicsYio.setFontAlpha(this.slider.titleFont, (double) this.slider.getFactor().get());
        this.slider.titleFont.draw(this.batch, this.slider.title, this.slider.titlePosition.f144x, this.slider.titlePosition.f145y);
        GraphicsYio.setFontAlpha(this.slider.titleFont, 1.0d);
        this.slider.titleFont.setColor(color);
    }

    public void renderThirdLayer(InterfaceElement element) {
    }

    private void renderAccent() {
        if (this.slider.isAccentVisible()) {
            this.batch.draw(getAccentPixel(this.slider), this.slider.getViewX(), this.slider.currentVerticalPos - this.sliderLineHeightHalved, this.slider.runnerValue * this.slider.getViewWidth(), this.sliderLineHeight);
        }
    }

    private TextureRegion getAccentPixel(SliderYio sliderYio) {
        if (sliderYio.isTouchable()) {
            return this.accentPixel;
        }
        return this.untouchablePixel;
    }

    private void renderBorder() {
        GraphicsYio.setBatchAlpha(this.batch, 1.0d);
        GraphicsYio.renderBorder(this.slider.getTouchRectangle(), this.batch, getGameView().blackPixel);
    }

    private void renderValueText() {
        Color color = this.slider.valueFont.getColor();
        this.slider.valueFont.setColor(Color.BLACK);
        if (this.slider.getFactor().get() < 1.0f) {
            GraphicsYio.setFontAlpha(this.slider.valueFont, (double) (this.slider.getFactor().get() * this.slider.getFactor().get()));
        }
        this.slider.valueFont.draw(this.batch, this.slider.getValueString(), this.slider.valueStringPosition.f144x, this.slider.valueStringPosition.f145y);
        if (this.slider.getFactor().get() < 1.0f) {
            GraphicsYio.setFontAlpha(this.slider.valueFont, 1.0d);
        }
        this.slider.valueFont.setColor(color);
    }

    private void renderValueCircle() {
        GraphicsYio.drawFromCenter(this.batch, getValueCircle(this.slider), (double) this.slider.getRunnerValueViewX(), (double) this.slider.currentVerticalPos, (double) this.slider.circleSize);
    }

    private TextureRegion getValueCircle(SliderYio sliderYio) {
        if (sliderYio.isTouchable()) {
            return this.blackCircle;
        }
        return this.untouchableValue;
    }

    private void checkToChangeBatchAlpha() {
        if (this.slider.appearFactor.get() != 1.0f) {
            this.batch.setColor(this.c.f39r, this.c.f38g, this.c.f37b, this.slider.appearFactor.get());
        }
    }

    private void renderSegments() {
        if (this.slider.isInternalSegmentsHidden()) {
            GraphicsYio.drawFromCenter(this.batch, this.blackCircle, (double) this.slider.getViewX(), (double) this.slider.currentVerticalPos, (double) this.slider.getSegmentCircleSize());
            GraphicsYio.drawFromCenter(this.batch, this.blackCircle, (double) (this.slider.getViewX() + this.slider.getViewWidth()), (double) this.slider.currentVerticalPos, (double) this.slider.getSegmentCircleSize());
            return;
        }
        for (int i = 0; i < this.slider.numberOfSegments + 1; i++) {
            GraphicsYio.drawFromCenter(this.batch, this.blackCircle, (double) this.slider.getSegmentLeftSidePos(i), (double) this.slider.currentVerticalPos, (double) this.slider.getSegmentCircleSize());
        }
    }

    private void renderBlackLine() {
        this.batch.draw(getGameView().blackPixel, this.slider.getViewX(), this.slider.currentVerticalPos - this.sliderLineHeightHalved, this.slider.getViewWidth(), this.sliderLineHeight);
    }
}
