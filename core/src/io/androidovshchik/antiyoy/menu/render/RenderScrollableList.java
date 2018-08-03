package io.androidovshchik.antiyoy.menu.render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import java.util.Iterator;
import yio.tro.antiyoy.menu.InterfaceElement;
import yio.tro.antiyoy.menu.scrollable_list.ListItemYio;
import yio.tro.antiyoy.menu.scrollable_list.ScrollableListYio;
import yio.tro.antiyoy.stuff.GraphicsYio;
import yio.tro.antiyoy.stuff.Masking;
import yio.tro.antiyoy.stuff.RectangleYio;

public class RenderScrollableList extends MenuRender {
    protected TextureRegion backgroundTexture;
    private TextureRegion bck1;
    private TextureRegion bck2;
    private TextureRegion bck3;
    private float factor;
    ScrollableListYio scrollableList;
    private TextureRegion selectionPixel;
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
        this.scrollableList = (ScrollableListYio) element;
        this.viewPosition = this.scrollableList.viewPosition;
        this.factor = this.scrollableList.getFactor().get();
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
        GraphicsYio.drawByRectangle(this.batch, this.backgroundTexture, this.scrollableList.topEdge);
        GraphicsYio.drawByRectangle(this.batch, this.backgroundTexture, this.scrollableList.bottomEdge);
    }

    private void renderLabel() {
        if (((double) this.factor) >= 0.5d) {
            GraphicsYio.setFontAlpha(this.scrollableList.titleFont, (double) this.scrollableList.textAlphaFactor.get());
            this.scrollableList.titleFont.draw(this.batch, this.scrollableList.label, this.scrollableList.labelPosition.f144x, this.scrollableList.labelPosition.f145y);
            GraphicsYio.setFontAlpha(this.scrollableList.titleFont, 1.0d);
        }
    }

    protected void renderItems() {
        this.scrollableList.descFont.setColor(Color.BLACK);
        Color titleColor = this.scrollableList.titleFont.getColor();
        this.scrollableList.titleFont.setColor(Color.BLACK);
        Iterator it = this.scrollableList.items.iterator();
        while (it.hasNext()) {
            ListItemYio item = (ListItemYio) it.next();
            if (item.isVisible()) {
                renderItemBackground(item);
                renderItemTitle(item);
                renderItemDescription(item);
                renderItemSelection(item);
            }
        }
        this.scrollableList.descFont.setColor(Color.WHITE);
        this.scrollableList.titleFont.setColor(titleColor);
    }

    protected void renderItemBackground(ListItemYio item) {
        GraphicsYio.setBatchAlpha(this.batch, (double) this.factor);
        GraphicsYio.drawByRectangle(this.batch, getItemBackgroundTexture(item), item.position);
    }

    protected TextureRegion getItemBackgroundTexture(ListItemYio item) {
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

    protected void renderItemDescription(ListItemYio item) {
        if (this.scrollableList.textAlphaFactor.get() != 0.0f) {
            GraphicsYio.setFontAlpha(this.scrollableList.descFont, (double) this.scrollableList.textAlphaFactor.get());
            this.scrollableList.descFont.draw(this.batch, item.description, item.descPosition.f144x, item.descPosition.f145y);
            GraphicsYio.setFontAlpha(this.scrollableList.descFont, 1.0d);
        }
    }

    protected void renderItemSelection(ListItemYio item) {
        if (item.isSelected()) {
            RectangleYio pos = item.position;
            GraphicsYio.setBatchAlpha(this.batch, 0.5d * ((double) item.getSelectionFactor().get()));
            GraphicsYio.drawByRectangle(this.batch, this.selectionPixel, pos);
            GraphicsYio.setBatchAlpha(this.batch, 1.0d);
        }
    }

    private void renderItemTitle(ListItemYio item) {
        if (this.scrollableList.textAlphaFactor.get() != 0.0f) {
            GraphicsYio.setFontAlpha(this.scrollableList.titleFont, (double) this.scrollableList.textAlphaFactor.get());
            this.scrollableList.titleFont.draw(this.batch, item.title, item.titlePosition.f144x, item.titlePosition.f145y);
            GraphicsYio.setFontAlpha(this.scrollableList.titleFont, 1.0d);
        }
    }

    public void renderThirdLayer(InterfaceElement element) {
    }
}
