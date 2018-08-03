package io.androidovshchik.antiyoy.menu.render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import java.util.Iterator;
import io.androidovshchik.antiyoy.menu.InterfaceElement;
import io.androidovshchik.antiyoy.menu.save_slot_selector.SaveSlotSelector;
import io.androidovshchik.antiyoy.menu.save_slot_selector.SsItem;
import io.androidovshchik.antiyoy.stuff.GraphicsYio;
import io.androidovshchik.antiyoy.stuff.Masking;
import io.androidovshchik.antiyoy.stuff.RectangleYio;

public class RenderSaveSlotSelector extends MenuRender {
    private TextureRegion backgroundTexture;
    private TextureRegion bck1;
    private TextureRegion bck2;
    private TextureRegion bck3;
    private float factor;
    private TextureRegion selectionPixel;
    private SaveSlotSelector selector;
    private RectangleYio viewPosition;

    public void loadTextures() {
        this.backgroundTexture = GraphicsYio.loadTextureRegion("menu/background.png", false);
        this.selectionPixel = GraphicsYio.loadTextureRegion("pixels/black_pixel.png", false);
        this.bck1 = GraphicsYio.loadTextureRegion("button_background_1.png", false);
        this.bck2 = GraphicsYio.loadTextureRegion("button_background_2.png", false);
        this.bck3 = GraphicsYio.loadTextureRegion("button_background_3.png", false);
    }

    public void renderFirstLayer(InterfaceElement element) {
    }

    public void renderSecondLayer(InterfaceElement element) {
        this.selector = (SaveSlotSelector) element;
        this.viewPosition = this.selector.viewPosition;
        this.factor = this.selector.getFactor().get();
        if (((double) this.factor) >= 0.25d) {
            renderShadow();
            this.batch.end();
            Masking.begin();
            this.menuViewYio.shapeRenderer.begin(ShapeType.Filled);
            this.menuViewYio.drawRoundRect(this.viewPosition);
            this.menuViewYio.shapeRenderer.end();
            this.batch.begin();
            Masking.continueAfterBatchBegin();
            renderInternals();
            Masking.end(this.batch);
        }
    }

    private void renderShadow() {
        if (((double) this.factor) > 0.5d) {
            this.menuViewYio.renderShadow(this.viewPosition, 1.0f, this.batch);
        }
    }

    private void renderInternals() {
        GraphicsYio.setBatchAlpha(this.batch, (double) this.factor);
        renderEdges();
        renderItems();
        renderLabel();
        GraphicsYio.setBatchAlpha(this.batch, 1.0d);
    }

    private void renderEdges() {
        GraphicsYio.drawByRectangle(this.batch, this.backgroundTexture, this.selector.topEdge);
        GraphicsYio.drawByRectangle(this.batch, this.backgroundTexture, this.selector.bottomEdge);
    }

    private void renderLabel() {
        if (((double) this.factor) >= 0.5d) {
            GraphicsYio.setFontAlpha(this.selector.titleFont, (double) this.selector.textAlphaFactor.get());
            this.selector.titleFont.draw(this.batch, this.selector.label, this.selector.labelPosition.f144x, this.selector.labelPosition.f145y);
            GraphicsYio.setFontAlpha(this.selector.titleFont, 1.0d);
        }
    }

    private void renderItems() {
        this.selector.descFont.setColor(Color.BLACK);
        Color titleColor = this.selector.titleFont.getColor();
        this.selector.titleFont.setColor(Color.BLACK);
        Iterator it = this.selector.items.iterator();
        while (it.hasNext()) {
            SsItem item = (SsItem) it.next();
            if (item.isVisible()) {
                renderItemBackground(item);
                renderItemTitle(item);
                renderItemDescription(item);
                renderItemSelection(item);
            }
        }
        this.selector.descFont.setColor(Color.WHITE);
        this.selector.titleFont.setColor(titleColor);
    }

    private void renderItemBackground(SsItem item) {
        GraphicsYio.setBatchAlpha(this.batch, (double) this.factor);
        GraphicsYio.drawByRectangle(this.batch, getItemBackgroundTexture(item), item.position);
    }

    private TextureRegion getItemBackgroundTexture(SsItem item) {
        switch (item.bckViewType) {
            case 0:
                return this.bck1;
            case 1:
                return this.bck2;
            case 2:
                return this.bck3;
            default:
                return null;
        }
    }

    private void renderItemDescription(SsItem item) {
        if (this.selector.textAlphaFactor.get() != 0.0f) {
            GraphicsYio.setFontAlpha(this.selector.descFont, (double) this.selector.textAlphaFactor.get());
            this.selector.descFont.draw(this.batch, item.description, item.descPosition.f144x, item.descPosition.f145y);
            GraphicsYio.setFontAlpha(this.selector.descFont, 1.0d);
        }
    }

    private void renderItemSelection(SsItem item) {
        if (item.isSelected()) {
            RectangleYio pos = item.position;
            GraphicsYio.setBatchAlpha(this.batch, 0.5d * ((double) item.getSelectionFactor().get()));
            GraphicsYio.drawByRectangle(this.batch, this.selectionPixel, pos);
            GraphicsYio.setBatchAlpha(this.batch, 1.0d);
        }
    }

    private void renderItemTitle(SsItem item) {
        if (this.selector.textAlphaFactor.get() != 0.0f) {
            GraphicsYio.setFontAlpha(this.selector.titleFont, (double) this.selector.textAlphaFactor.get());
            this.selector.titleFont.draw(this.batch, item.title, item.titlePosition.f144x, item.titlePosition.f145y);
            GraphicsYio.setFontAlpha(this.selector.titleFont, 1.0d);
        }
    }

    public void renderThirdLayer(InterfaceElement element) {
    }
}
