package io.androidovshchik.antiyoy.menu.render;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.antiyoy.menu.CheckButtonYio;
import yio.tro.antiyoy.menu.InterfaceElement;
import yio.tro.antiyoy.stuff.GraphicsYio;

public class RenderCheckButton extends MenuRender {
    TextureRegion activeTexture;
    TextureRegion backgroundTexture;
    TextureRegion blackPixel;

    public void loadTextures() {
        this.backgroundTexture = GraphicsYio.loadTextureRegion("menu/check_button/chk_bck.png", false);
        this.activeTexture = GraphicsYio.loadTextureRegion("menu/check_button/chk_active.png", true);
        this.blackPixel = GraphicsYio.loadTextureRegion("pixels/black_pixel.png", false);
    }

    public void renderFirstLayer(InterfaceElement element) {
    }

    public void renderSecondLayer(InterfaceElement element) {
    }

    public void renderCheckButton(CheckButtonYio checkButtonYio) {
        if (checkButtonYio.getViewFactor().get() > 0.0f) {
            GraphicsYio.setBatchAlpha(this.batch, 0.2d * ((double) checkButtonYio.getViewFactor().get()));
            GraphicsYio.drawByRectangle(this.batch, this.blackPixel, checkButtonYio.getTouchPosition());
            GraphicsYio.setBatchAlpha(this.batch, 1.0d);
        }
        if (checkButtonYio.getFactor().get() < 1.0f) {
            GraphicsYio.setBatchAlpha(this.batch, (double) checkButtonYio.getFactor().get());
        }
        GraphicsYio.renderBorder(checkButtonYio.animPos, this.batch, this.blackPixel);
        if (checkButtonYio.selectionFactor.get() > 0.0f) {
            GraphicsYio.setBatchAlpha(this.batch, (double) Math.min(checkButtonYio.selectionFactor.get(), checkButtonYio.getFactor().get()));
            GraphicsYio.drawByRectangle(this.batch, this.activeTexture, checkButtonYio.animPos);
        }
        GraphicsYio.setBatchAlpha(this.batch, 1.0d);
    }

    public void renderThirdLayer(InterfaceElement element) {
    }
}
