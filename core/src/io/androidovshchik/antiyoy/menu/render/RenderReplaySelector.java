package io.androidovshchik.antiyoy.menu.render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import java.util.Iterator;
import io.androidovshchik.antiyoy.menu.InterfaceElement;
import io.androidovshchik.antiyoy.menu.replay_selector.ReplaySelector;
import io.androidovshchik.antiyoy.menu.replay_selector.RsItem;
import io.androidovshchik.antiyoy.stuff.GraphicsYio;
import io.androidovshchik.antiyoy.stuff.Masking;
import io.androidovshchik.antiyoy.stuff.RectangleYio;

public class RenderReplaySelector extends MenuRender {
    private TextureRegion backgroundTexture;
    private TextureRegion cancelIconTexture;
    private float factor;
    private ReplaySelector rs;
    private TextureRegion selectionPixel;
    private RectangleYio viewPosition;

    public void loadTextures() {
        this.backgroundTexture = GraphicsYio.loadTextureRegion("menu/background.png", false);
        this.selectionPixel = GraphicsYio.loadTextureRegion("pixels/black_pixel.png", false);
        this.cancelIconTexture = GraphicsYio.loadTextureRegion("cancel_icon.png", true);
    }

    public void renderFirstLayer(InterfaceElement element) {
    }

    public void renderSecondLayer(InterfaceElement element) {
        this.rs = (ReplaySelector) element;
        this.viewPosition = this.rs.viewPosition;
        this.factor = this.rs.getFactor().get();
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

    private void renderShadow() {
        if (((double) this.factor) > 0.5d) {
            this.menuViewYio.renderShadow(this.viewPosition, 1.0f, this.batch);
        }
    }

    private void renderInternals() {
        GraphicsYio.setBatchAlpha(this.batch, Math.sqrt((double) this.factor));
        renderBackground();
        renderItems();
        renderLabel();
        GraphicsYio.setBatchAlpha(this.batch, 1.0d);
    }

    private void renderLabel() {
        if (((double) this.factor) >= 0.5d) {
            GraphicsYio.setFontAlpha(this.rs.titleFont, (double) this.rs.textAlphaFactor.get());
            this.rs.titleFont.draw(this.batch, this.rs.label, this.rs.labelPosition.f144x, this.rs.labelPosition.f145y);
            GraphicsYio.setFontAlpha(this.rs.titleFont, 1.0d);
        }
    }

    private void renderItems() {
        if (this.rs.textAlphaFactor.get() != 0.0f) {
            this.rs.descFont.setColor(Color.BLACK);
            Color titleColor = this.rs.titleFont.getColor();
            this.rs.titleFont.setColor(Color.BLACK);
            Iterator it = this.rs.items.iterator();
            while (it.hasNext()) {
                RsItem item = (RsItem) it.next();
                if (item.isVisible()) {
                    RectangleYio pos = item.position;
                    renderItemTitle(item);
                    renderItemDescription(item);
                    renderItemSelection(item, pos);
                    renderRemoveIcon(item);
                }
            }
            this.rs.descFont.setColor(Color.WHITE);
            this.rs.titleFont.setColor(titleColor);
        }
    }

    private void renderRemoveIcon(RsItem item) {
        if (this.rs.isInRemoveMode()) {
            GraphicsYio.drawFromCenter(this.batch, this.cancelIconTexture, (double) item.removeIconPosition.f144x, (double) item.removeIconPosition.f145y, (double) (0.04f * GraphicsYio.width));
        }
    }

    private void renderItemDescription(RsItem item) {
        GraphicsYio.setFontAlpha(this.rs.descFont, (double) this.rs.textAlphaFactor.get());
        this.rs.descFont.draw(this.batch, item.description, item.descPosition.f144x, item.descPosition.f145y);
        GraphicsYio.setFontAlpha(this.rs.descFont, 1.0d);
    }

    private void renderItemSelection(RsItem item, RectangleYio pos) {
        if (item.isSelected()) {
            GraphicsYio.setBatchAlpha(this.batch, 0.5d * ((double) item.getSelectionFactor().get()));
            GraphicsYio.drawByRectangle(this.batch, this.selectionPixel, pos);
            GraphicsYio.setBatchAlpha(this.batch, 1.0d);
        }
    }

    private void renderItemTitle(RsItem item) {
        GraphicsYio.setFontAlpha(this.rs.titleFont, (double) this.rs.textAlphaFactor.get());
        this.rs.titleFont.draw(this.batch, item.title, item.titlePosition.f144x, item.titlePosition.f145y);
        GraphicsYio.setFontAlpha(this.rs.titleFont, 1.0d);
    }

    private void renderBackground() {
        GraphicsYio.drawByRectangle(this.batch, this.backgroundTexture, this.viewPosition);
    }

    public void renderThirdLayer(InterfaceElement element) {
    }
}
