package io.androidovshchik.antiyoy.menu.render;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import yio.tro.antiyoy.menu.InterfaceElement;
import yio.tro.antiyoy.menu.LevelSelector;
import yio.tro.antiyoy.stuff.GraphicsYio;
import yio.tro.antiyoy.stuff.Masking;
import yio.tro.antiyoy.stuff.RectangleYio;

public class RenderLevelSelector extends MenuRender {
    TextureRegion blackPixel;
    private float selX;
    private float selY;
    private LevelSelector selector;

    public void loadTextures() {
        this.blackPixel = GraphicsYio.loadTextureRegion("pixels/black_pixel.png", false);
    }

    public void renderFirstLayer(InterfaceElement element) {
    }

    public void renderSecondLayer(InterfaceElement element) {
        this.selector = (LevelSelector) element;
        if (this.selector.getFactor().get() != 0.0f) {
            this.c = this.batch.getColor();
            renderShadows();
            this.batch.end();
            Masking.begin();
            drawShapeRendererStuff();
            this.batch.begin();
            Masking.continueAfterBatchBegin();
            this.batch.setColor(this.c.f39r, this.c.f38g, this.c.f37b, this.selector.getFactor().get());
            for (int i = 0; i < this.selector.textures.length; i++) {
                RectangleYio pos = this.selector.positions[i];
                this.batch.draw(this.selector.textures[i], (float) pos.f146x, (float) pos.f147y, (float) pos.width, (float) pos.height);
                checkToRenderSelection(this.selector, pos, i);
            }
            Masking.end(this.batch);
            this.batch.setColor(this.c.f39r, this.c.f38g, this.c.f37b, 1.0f);
        }
    }

    private void drawShapeRendererStuff() {
        this.menuViewYio.shapeRenderer.begin(ShapeType.Filled);
        if (this.selector.getFactor().get() == 1.0f) {
            for (RectangleYio pos : this.selector.positions) {
                this.menuViewYio.drawRoundRect(pos);
            }
        } else {
            this.menuViewYio.drawCircle(this.selector.getCircleX(), this.selector.getCircleY(), this.selector.getCircleR());
        }
        this.menuViewYio.shapeRenderer.end();
    }

    public void renderThirdLayer(InterfaceElement element) {
    }

    public void renderLevelSelector(LevelSelector selector) {
    }

    private void renderShadows() {
        if (((double) this.selector.getFactor().get()) > 0.95d) {
            for (RectangleYio pos : this.selector.positions) {
                this.menuViewYio.renderShadow(pos, this.selector.getFactor().get(), this.batch);
            }
        }
    }

    private void checkToRenderSelection(LevelSelector selector, RectangleYio pos, int i) {
        if (selector.selectionFactor.get() > 0.0f && i == selector.selectedPanelIndex) {
            GraphicsYio.setBatchAlpha(this.batch, (double) (0.5f * selector.selectionFactor.get()));
            this.selX = ((float) (selector.selIndexX * 2)) * selector.iconRadius;
            this.selY = ((float) (selector.selIndexY * 2)) * selector.iconRadius;
            this.selX = (float) (((double) this.selX) * (pos.width / selector.defPos.width));
            this.batch.draw(getGameView().blackPixel, (((float) pos.f146x) + selector.horOffset) + this.selX, (((float) pos.f147y) + selector.verOffset) + this.selY, selector.iconRadius * 2.0f, selector.iconRadius * 2.0f);
            GraphicsYio.setBatchAlpha(this.batch, (double) selector.getFactor().get());
        }
    }
}
