package io.androidovshchik.antiyoy.menu.render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.Iterator;
import io.androidovshchik.antiyoy.menu.InterfaceElement;
import io.androidovshchik.antiyoy.menu.diplomatic_log.DiplomaticLogPanel;
import io.androidovshchik.antiyoy.menu.scrollable_list.ListItemYio;
import io.androidovshchik.antiyoy.stuff.GraphicsYio;
import io.androidovshchik.antiyoy.stuff.RectangleYio;

public class RenderDiplomaticLogPanel extends MenuRender {
    protected TextureRegion backgroundTexture;
    private float factor;
    DiplomaticLogPanel panel;
    private TextureRegion selectionPixel;
    private RectangleYio viewPosition;

    public void loadTextures() {
        this.backgroundTexture = GraphicsYio.loadTextureRegion("menu/background.png", false);
        this.selectionPixel = GraphicsYio.loadTextureRegion("pixels/black_pixel.png", false);
    }

    public void renderFirstLayer(InterfaceElement element) {
    }

    public void renderSecondLayer(InterfaceElement element) {
        this.panel = (DiplomaticLogPanel) element;
        this.viewPosition = this.panel.viewPosition;
        this.factor = this.panel.appearFactor.get();
        if (((double) this.factor) >= 0.05d) {
            GraphicsYio.setBatchAlpha(this.batch, (double) this.factor);
            renderBackground();
            renderItems();
            renderLabel();
            GraphicsYio.setBatchAlpha(this.batch, 1.0d);
        }
    }

    private void renderBackground() {
        GraphicsYio.drawByRectangle(this.batch, this.backgroundTexture, this.viewPosition);
    }

    private void renderEdges() {
        GraphicsYio.drawByRectangle(this.batch, this.backgroundTexture, this.panel.topEdge);
        GraphicsYio.drawByRectangle(this.batch, this.backgroundTexture, this.panel.bottomEdge);
    }

    private void renderLabel() {
        if (((double) this.factor) >= 0.5d) {
            GraphicsYio.drawByRectangle(this.batch, this.backgroundTexture, this.panel.titleBackground);
            GraphicsYio.setFontAlpha(this.panel.titleFont, (double) this.panel.textAlphaFactor.get());
            this.panel.titleFont.draw(this.batch, this.panel.label, this.panel.labelPosition.f144x, this.panel.labelPosition.f145y);
            GraphicsYio.setFontAlpha(this.panel.titleFont, 1.0d);
        }
    }

    protected void renderItems() {
        this.panel.descFont.setColor(Color.BLACK);
        Color titleColor = this.panel.titleFont.getColor();
        this.panel.titleFont.setColor(Color.BLACK);
        Iterator it = this.panel.items.iterator();
        while (it.hasNext()) {
            ListItemYio item = (ListItemYio) it.next();
            if (isItemVisible(item)) {
                renderItemBackground(item);
                renderItemTitle(item);
                renderItemSelection(item);
            }
        }
        this.panel.descFont.setColor(Color.WHITE);
        this.panel.titleFont.setColor(titleColor);
    }

    private boolean isItemVisible(ListItemYio itemYio) {
        RectangleYio position = itemYio.position;
        if (position.f147y <= this.panel.titleBackground.f147y && position.f147y + position.height >= this.panel.viewPosition.f147y) {
            return true;
        }
        return false;
    }

    protected void renderItemBackground(ListItemYio item) {
        if (item.bckViewType != -1) {
            GraphicsYio.setBatchAlpha(this.batch, (double) this.factor);
            GraphicsYio.drawByRectangle(this.batch, getItemBackgroundTexture(item), item.position);
        }
    }

    protected TextureRegion getItemBackgroundTexture(ListItemYio item) {
        return MenuRender.renderDiplomacyElement.getBackgroundPixel(item.bckViewType);
    }

    protected void renderItemDescription(ListItemYio item) {
        if (this.panel.textAlphaFactor.get() != 0.0f) {
            GraphicsYio.setFontAlpha(this.panel.descFont, (double) this.panel.textAlphaFactor.get());
            this.panel.descFont.draw(this.batch, item.description, item.descPosition.f144x, item.descPosition.f145y);
            GraphicsYio.setFontAlpha(this.panel.descFont, 1.0d);
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
        if (this.panel.textAlphaFactor.get() != 0.0f) {
            GraphicsYio.setFontAlpha(this.panel.titleFont, (double) this.panel.textAlphaFactor.get());
            this.panel.titleFont.draw(this.batch, item.title, item.titlePosition.f144x, item.titlePosition.f145y);
            GraphicsYio.setFontAlpha(this.panel.titleFont, 1.0d);
        }
    }

    public void renderThirdLayer(InterfaceElement element) {
    }
}
