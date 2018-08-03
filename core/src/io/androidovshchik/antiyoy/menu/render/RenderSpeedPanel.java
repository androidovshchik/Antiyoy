package io.androidovshchik.antiyoy.menu.render;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.Iterator;
import io.androidovshchik.antiyoy.factor_yio.FactorYio;
import io.androidovshchik.antiyoy.menu.InterfaceElement;
import io.androidovshchik.antiyoy.menu.speed_panel.SpItem;
import io.androidovshchik.antiyoy.menu.speed_panel.SpeedPanel;
import io.androidovshchik.antiyoy.stuff.GraphicsYio;
import io.androidovshchik.antiyoy.stuff.RectangleYio;

public class RenderSpeedPanel extends MenuRender {
    private TextureRegion backgroundTexture;
    private FactorYio factor;
    private TextureRegion fastForwardIcon;
    private TextureRegion pauseIcon;
    private TextureRegion playIcon;
    private TextureRegion saveIcon;
    private TextureRegion selectionPixel;
    private SpeedPanel speedPanel;
    private TextureRegion stopIcon;
    private RectangleYio viewPosition;

    public void loadTextures() {
        this.backgroundTexture = GraphicsYio.loadTextureRegion("pixels/gray_pixel.png", false);
        this.playIcon = GraphicsYio.loadTextureRegion("menu/replays/play.png", true);
        this.stopIcon = GraphicsYio.loadTextureRegion("menu/replays/stop.png", true);
        this.pauseIcon = GraphicsYio.loadTextureRegion("menu/replays/pause.png", true);
        this.fastForwardIcon = GraphicsYio.loadTextureRegion("menu/replays/fast_forward.png", true);
        this.saveIcon = GraphicsYio.loadTextureRegion("menu/replays/save_icon.png", true);
        this.selectionPixel = GraphicsYio.loadTextureRegion("pixels/black_pixel.png", false);
    }

    public void renderFirstLayer(InterfaceElement element) {
    }

    public void renderSecondLayer(InterfaceElement element) {
        this.speedPanel = (SpeedPanel) element;
        this.factor = this.speedPanel.getFactor();
        this.viewPosition = this.speedPanel.viewPosition;
        GraphicsYio.setBatchAlpha(this.batch, (double) this.factor.get());
        GraphicsYio.drawByRectangle(this.batch, this.backgroundTexture, this.viewPosition);
        renderItems();
        GraphicsYio.setBatchAlpha(this.batch, 1.0d);
    }

    private void renderItems() {
        Iterator it = this.speedPanel.items.iterator();
        while (it.hasNext()) {
            SpItem item = (SpItem) it.next();
            if (item.isVisible()) {
                GraphicsYio.setBatchAlpha(this.batch, (double) item.appearFactor.get());
                GraphicsYio.drawFromCenter(this.batch, getItemTexture(item), (double) item.position.f144x, (double) item.position.f145y, (double) item.radius);
                GraphicsYio.setBatchAlpha(this.batch, 1.0d);
                if (item.isSelected()) {
                    GraphicsYio.setBatchAlpha(this.batch, (double) (0.5f * item.selectionFactor.get()));
                    GraphicsYio.drawFromCenter(this.batch, this.selectionPixel, (double) item.position.f144x, (double) item.position.f145y, (double) item.radius);
                    GraphicsYio.setBatchAlpha(this.batch, (double) this.speedPanel.getFactor().get());
                }
            }
        }
    }

    private TextureRegion getItemTexture(SpItem item) {
        switch (item.action) {
            case 0:
                return this.stopIcon;
            case 1:
                if (getGameView().gameController.speedManager.getSpeed() != 0) {
                    return this.pauseIcon;
                }
                return this.playIcon;
            case 2:
                return this.fastForwardIcon;
            case 3:
                return this.saveIcon;
            default:
                return null;
        }
    }

    public void renderThirdLayer(InterfaceElement element) {
    }
}
