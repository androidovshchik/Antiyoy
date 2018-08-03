package io.androidovshchik.antiyoy.menu.render;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.androidovshchik.antiyoy.menu.scrollable_list.ListItemYio;
import io.androidovshchik.antiyoy.stuff.GraphicsYio;

public class RenderSpecialThanksDialog extends RenderScrollableList {
    protected TextureRegion getItemBackgroundTexture(ListItemYio item) {
        return this.backgroundTexture;
    }

    protected void renderItemDescription(ListItemYio item) {
        if (this.scrollableList.textAlphaFactor.get() != 0.0f) {
            GraphicsYio.setFontAlpha(this.scrollableList.descFont, (double) this.scrollableList.textAlphaFactor.get());
            this.scrollableList.descFont.draw(this.batch, item.description, (float) (((item.position.f146x + item.position.width) - ((double) (0.03f * GraphicsYio.width))) - ((double) item.descWidth)), item.titlePosition.f145y);
            GraphicsYio.setFontAlpha(this.scrollableList.descFont, 1.0d);
        }
    }

    protected void renderItemSelection(ListItemYio item) {
    }
}
