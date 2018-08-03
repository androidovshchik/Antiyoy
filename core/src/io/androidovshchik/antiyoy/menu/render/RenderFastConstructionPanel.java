package io.androidovshchik.antiyoy.menu.render;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.Iterator;
import io.androidovshchik.antiyoy.Settings;
import io.androidovshchik.antiyoy.menu.InterfaceElement;
import io.androidovshchik.antiyoy.menu.fast_construction.FastConstructionPanel;
import io.androidovshchik.antiyoy.menu.fast_construction.FcpItem;
import io.androidovshchik.antiyoy.stuff.GraphicsYio;
import io.androidovshchik.antiyoy.stuff.RectangleYio;

public class RenderFastConstructionPanel extends MenuRender {
    private TextureRegion backgroundTexture;
    private TextureRegion diplomacyIcon;
    private TextureRegion diplomacyRedIcon;
    private TextureRegion endTurnIcon;
    private float factor;
    private TextureRegion house;
    private TextureRegion man0;
    private TextureRegion man1;
    private TextureRegion man2;
    private TextureRegion man3;
    private FastConstructionPanel panel;
    private RectangleYio pos;
    private TextureRegion selectionPixel;
    private TextureRegion shrHouse;
    private TextureRegion shrMan0;
    private TextureRegion shrMan1;
    private TextureRegion shrMan2;
    private TextureRegion shrMan3;
    private TextureRegion shrStrongTower;
    private TextureRegion shrTower;
    private TextureRegion sideShadow;
    private float smDelta;
    private TextureRegion strongTower;
    private TextureRegion tower;
    private TextureRegion undoIcon;

    public void loadTextures() {
        this.backgroundTexture = GraphicsYio.loadTextureRegion("pixels/gray_pixel.png", false);
        this.selectionPixel = GraphicsYio.loadTextureRegion("pixels/black_pixel.png", false);
        this.sideShadow = GraphicsYio.loadTextureRegion("money_shadow.png", true);
        this.endTurnIcon = GraphicsYio.loadTextureRegion("end_turn.png", true);
        this.undoIcon = GraphicsYio.loadTextureRegion("undo.png", true);
        this.diplomacyIcon = GraphicsYio.loadTextureRegion("diplomacy/flag.png", true);
        this.diplomacyRedIcon = GraphicsYio.loadTextureRegion("diplomacy/flag_red.png", true);
        this.man0 = GraphicsYio.loadTextureRegion("field_elements/man0.png", true);
        this.man1 = GraphicsYio.loadTextureRegion("field_elements/man1.png", true);
        this.man2 = GraphicsYio.loadTextureRegion("field_elements/man2.png", true);
        this.man3 = GraphicsYio.loadTextureRegion("field_elements/man3.png", true);
        this.tower = GraphicsYio.loadTextureRegion("field_elements/tower.png", true);
        this.strongTower = GraphicsYio.loadTextureRegion("field_elements/strong_tower.png", true);
        this.house = GraphicsYio.loadTextureRegion("field_elements/house.png", true);
        this.shrMan0 = GraphicsYio.loadTextureRegion("skins/ant/field_elements/man0.png", true);
        this.shrMan1 = GraphicsYio.loadTextureRegion("skins/ant/field_elements/man1.png", true);
        this.shrMan2 = GraphicsYio.loadTextureRegion("skins/ant/field_elements/man2.png", true);
        this.shrMan3 = GraphicsYio.loadTextureRegion("skins/ant/field_elements/man3.png", true);
        this.shrTower = GraphicsYio.loadTextureRegion("skins/ant/field_elements/tower.png", true);
        this.shrStrongTower = GraphicsYio.loadTextureRegion("skins/ant/field_elements/strong_tower.png", true);
        this.shrHouse = GraphicsYio.loadTextureRegion("skins/ant/field_elements/house.png", true);
    }

    public void renderFirstLayer(InterfaceElement element) {
    }

    public void renderSecondLayer(InterfaceElement element) {
        this.panel = (FastConstructionPanel) element;
        this.factor = this.panel.getFactor().get();
        this.pos = this.panel.viewPosition;
        GraphicsYio.setBatchAlpha(this.batch, (double) this.factor);
        renderShadow();
        renderBackground();
        renderItems();
        GraphicsYio.setBatchAlpha(this.batch, 1.0d);
    }

    private void renderShadow() {
        this.smDelta = (this.h * 0.1f) * (1.0f - this.factor);
        this.batch.draw(this.sideShadow, 0.0f, (-this.smDelta) + (0.03f * this.h), this.w, this.h * 0.1f);
    }

    private void renderItems() {
        Iterator it = this.panel.items.iterator();
        while (it.hasNext()) {
            FcpItem item = (FcpItem) it.next();
            if (item.isVisible()) {
                GraphicsYio.drawFromCenter(this.batch, getItemTexture(item), (double) item.position.f144x, (double) item.position.f145y, (double) item.radius);
                if (item.isSelected()) {
                    GraphicsYio.setBatchAlpha(this.batch, (double) (0.5f * item.selectionFactor.get()));
                    GraphicsYio.drawFromCenter(this.batch, this.selectionPixel, (double) item.position.f144x, (double) item.position.f145y, (double) item.radius);
                    GraphicsYio.setBatchAlpha(this.batch, (double) this.factor);
                }
            }
        }
    }

    private void renderBackground() {
        GraphicsYio.drawByRectangle(this.batch, this.backgroundTexture, this.pos);
    }

    private TextureRegion getItemTexture(FcpItem item) {
        if (item.action == 8) {
            return this.undoIcon;
        }
        if (item.action == 9) {
            return this.endTurnIcon;
        }
        if (item.action == 10) {
            if (this.menuViewYio.yioGdxGame.gameController.fieldController.diplomacyManager.log.hasSomethingToRead()) {
                return this.diplomacyRedIcon;
            }
            return this.diplomacyIcon;
        } else if (Settings.isShroomArtsEnabled()) {
            return getShroomItemTexture(item);
        } else {
            return getDefaultItemTexture(item);
        }
    }

    private TextureRegion getDefaultItemTexture(FcpItem item) {
        switch (item.action) {
            case 1:
                return this.man0;
            case 2:
                return this.man1;
            case 3:
                return this.man2;
            case 4:
                return this.man3;
            case 5:
                return this.house;
            case 6:
                return this.tower;
            case 7:
                return this.strongTower;
            default:
                return null;
        }
    }

    private TextureRegion getShroomItemTexture(FcpItem item) {
        switch (item.action) {
            case 1:
                return this.shrMan0;
            case 2:
                return this.shrMan1;
            case 3:
                return this.shrMan2;
            case 4:
                return this.shrMan3;
            case 5:
                return this.shrHouse;
            case 6:
                return this.shrTower;
            case 7:
                return this.shrStrongTower;
            default:
                return null;
        }
    }

    public void renderThirdLayer(InterfaceElement element) {
    }
}
