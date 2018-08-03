package io.androidovshchik.antiyoy.menu.render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.Iterator;
import yio.tro.antiyoy.menu.InterfaceElement;
import yio.tro.antiyoy.menu.diplomacy_element.DeIcon;
import yio.tro.antiyoy.menu.diplomacy_element.DeItem;
import yio.tro.antiyoy.menu.diplomacy_element.DeLabel;
import yio.tro.antiyoy.menu.diplomacy_element.DiplomacyElement;
import yio.tro.antiyoy.stuff.GraphicsYio;
import yio.tro.antiyoy.stuff.RectangleYio;

public class RenderDiplomacyElement extends MenuRender {
    private TextureRegion backgroundTexture;
    private TextureRegion[] bckColors;
    private TextureRegion deadIcon;
    DiplomacyElement diplomacyElement;
    private TextureRegion dislikeIcon;
    private TextureRegion enemyIcon;
    private float factor;
    private TextureRegion friendIcon;
    private TextureRegion infoIcon;
    private TextureRegion likeIcon;
    private TextureRegion neutralIcon;
    private TextureRegion selectionPixel;
    private final float shadowThickness = (0.08f * GraphicsYio.width);
    private TextureRegion skullIcon;
    private RectangleYio viewPosition;

    public void loadTextures() {
        this.backgroundTexture = GraphicsYio.loadTextureRegion("diplomacy/background.png", false);
        this.selectionPixel = GraphicsYio.loadTextureRegion("pixels/black_pixel.png", false);
        this.neutralIcon = GraphicsYio.loadTextureRegion("diplomacy/face_neutral.png", true);
        this.friendIcon = GraphicsYio.loadTextureRegion("diplomacy/face_friend.png", true);
        this.enemyIcon = GraphicsYio.loadTextureRegion("diplomacy/face_enemy.png", true);
        this.deadIcon = GraphicsYio.loadTextureRegion("diplomacy/face_dead.png", true);
        this.likeIcon = GraphicsYio.loadTextureRegion("diplomacy/like_icon.png", true);
        this.dislikeIcon = GraphicsYio.loadTextureRegion("diplomacy/dislike_icon.png", true);
        this.skullIcon = GraphicsYio.loadTextureRegion("diplomacy/skull_icon.png", true);
        this.infoIcon = GraphicsYio.loadTextureRegion("diplomacy/info_icon.png", true);
        this.bckColors = new TextureRegion[7];
        for (int i = 0; i < this.bckColors.length; i++) {
            this.bckColors[i] = GraphicsYio.loadTextureRegion("diplomacy/color" + (i + 1) + ".png", false);
        }
    }

    public void renderFirstLayer(InterfaceElement element) {
    }

    public void renderSecondLayer(InterfaceElement element) {
        this.diplomacyElement = (DiplomacyElement) element;
        this.viewPosition = this.diplomacyElement.viewPosition;
        this.factor = this.diplomacyElement.getFactor().get();
        renderShadow();
        renderInternals();
    }

    private void renderShadow() {
        this.batch.draw(this.menuViewYio.shadowSide, (float) this.viewPosition.f146x, (float) ((this.viewPosition.f147y + this.viewPosition.height) - ((double) (this.shadowThickness / 2.0f))), (float) this.viewPosition.width, this.shadowThickness);
    }

    private void renderInternals() {
        renderBackground();
        renderItems();
        renderCover();
        renderLabel();
        renderIcons();
    }

    private void renderIcons() {
        Iterator it = this.diplomacyElement.icons.iterator();
        while (it.hasNext()) {
            DeIcon icon = (DeIcon) it.next();
            if (icon.isVisible()) {
                GraphicsYio.setBatchAlpha(this.batch, (double) icon.appearFactor.get());
                GraphicsYio.drawFromCenter(this.batch, getIconTexture(icon), (double) icon.position.f144x, (double) icon.position.f145y, (double) icon.radius);
                GraphicsYio.setBatchAlpha(this.batch, 1.0d);
                if (icon.isSelected()) {
                    GraphicsYio.setBatchAlpha(this.batch, 0.4d * ((double) icon.selectionFactor.get()));
                    GraphicsYio.drawFromCenter(this.batch, getGameView().blackPixel, (double) icon.position.f144x, (double) icon.position.f145y, (double) icon.radius);
                    GraphicsYio.setBatchAlpha(this.batch, 1.0d);
                }
            }
        }
    }

    private TextureRegion getIconTexture(DeIcon deIcon) {
        switch (deIcon.action) {
            case 0:
                return this.likeIcon;
            case 1:
                return this.dislikeIcon;
            case 2:
                return this.skullIcon;
            case 3:
                return this.infoIcon;
            default:
                return null;
        }
    }

    private void renderCover() {
        GraphicsYio.drawByRectangle(this.batch, this.backgroundTexture, this.diplomacyElement.topCover);
    }

    private void renderBackground() {
        if (this.diplomacyElement.needToRenderInternalBackground()) {
            GraphicsYio.drawByRectangle(this.batch, this.backgroundTexture, this.diplomacyElement.internalBackground);
        }
    }

    private void renderLabel() {
        DeLabel label = this.diplomacyElement.label;
        if (label.visible) {
            GraphicsYio.setFontAlpha(this.diplomacyElement.titleFont, (double) label.appearFactor.get());
            this.diplomacyElement.titleFont.draw(this.batch, label.text, label.position.f144x, label.position.f145y);
            GraphicsYio.setFontAlpha(this.diplomacyElement.titleFont, 1.0d);
        }
    }

    private void renderItems() {
        this.diplomacyElement.descFont.setColor(Color.BLACK);
        Color titleColor = this.diplomacyElement.titleFont.getColor();
        this.diplomacyElement.titleFont.setColor(Color.BLACK);
        Iterator it = this.diplomacyElement.items.iterator();
        while (it.hasNext()) {
            renderSingleItem((DeItem) it.next());
        }
        this.diplomacyElement.descFont.setColor(Color.WHITE);
        this.diplomacyElement.titleFont.setColor(titleColor);
    }

    private void renderSingleItem(DeItem item) {
        if (item.isTopVisible()) {
            renderItemBackground(item, item.position);
            renderItemTitle(item);
            renderItemDescription(item);
            renderBlackMark(item);
            renderItemStatus(item);
            renderItemSelection(item, item.position);
        } else if (item.isBottomVisible()) {
            renderItemBackground(item, item.bottomRectangle);
            renderItemDescription(item);
            renderItemSelection(item, item.bottomRectangle);
        }
    }

    private void renderBlackMark(DeItem item) {
        if (item.blackMarkEnabled) {
            GraphicsYio.drawFromCenter(this.batch, this.skullIcon, (double) item.blackMarkPosition.f144x, (double) item.blackMarkPosition.f145y, (double) item.blackMarkRadius);
        }
    }

    private void renderItemDescription(DeItem item) {
        this.diplomacyElement.descFont.draw(this.batch, item.descriptionString, item.descPosition.f144x, item.descPosition.f145y);
    }

    private void renderItemBackground(DeItem item, RectangleYio pos) {
        GraphicsYio.drawByRectangle(this.batch, this.bckColors[this.menuViewYio.yioGdxGame.gameController.getColorIndexWithOffset(item.colorIndex)], pos);
    }

    private void renderItemStatus(DeItem item) {
        GraphicsYio.drawFromCenter(this.batch, getItemStatusIcon(item), (double) item.statusPosition.f144x, (double) item.statusPosition.f145y, (double) item.statusRadius);
    }

    private TextureRegion getItemStatusIcon(DeItem item) {
        switch (item.status) {
            case 0:
                return this.neutralIcon;
            case 1:
                return this.friendIcon;
            case 2:
                return this.enemyIcon;
            case 3:
                return this.deadIcon;
            default:
                return null;
        }
    }

    private void renderItemSelection(DeItem item, RectangleYio pos) {
        if (item.isSelected()) {
            GraphicsYio.setBatchAlpha(this.batch, 0.5d * ((double) item.getSelectionFactor().get()));
            GraphicsYio.drawByRectangle(this.batch, this.selectionPixel, pos);
            GraphicsYio.setBatchAlpha(this.batch, 1.0d);
        }
    }

    private void renderItemTitle(DeItem item) {
        if (this.diplomacyElement.appearFactor.get() != 0.0f) {
            this.diplomacyElement.titleFont.draw(this.batch, item.title, item.titlePosition.f144x, item.titlePosition.f145y);
        }
    }

    public TextureRegion getBackgroundPixel(int colorIndex) {
        return this.bckColors[colorIndex];
    }

    public void renderThirdLayer(InterfaceElement element) {
    }
}
