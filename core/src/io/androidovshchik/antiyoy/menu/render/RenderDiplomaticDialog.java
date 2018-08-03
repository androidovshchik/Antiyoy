package io.androidovshchik.antiyoy.menu.render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.Iterator;
import io.androidovshchik.antiyoy.menu.InterfaceElement;
import io.androidovshchik.antiyoy.menu.diplomatic_dialogs.AbstractDiplomaticDialog;
import io.androidovshchik.antiyoy.menu.diplomatic_dialogs.AcButton;
import io.androidovshchik.antiyoy.menu.diplomatic_dialogs.AcLabel;
import io.androidovshchik.antiyoy.stuff.GraphicsYio;

public class RenderDiplomaticDialog extends MenuRender {
    private TextureRegion backgroundTexture;
    AbstractDiplomaticDialog dialog;
    private TextureRegion noBckTexture;
    private TextureRegion selectionPixel;
    private TextureRegion yesBckTexture;

    public void loadTextures() {
        this.backgroundTexture = GraphicsYio.loadTextureRegion("diplomacy/background.png", false);
        this.selectionPixel = GraphicsYio.loadTextureRegion("pixels/black_pixel.png", false);
        this.yesBckTexture = GraphicsYio.loadTextureRegion("button_background_3.png", false);
        this.noBckTexture = GraphicsYio.loadTextureRegion("button_background_1.png", false);
    }

    public void renderFirstLayer(InterfaceElement element) {
    }

    public void renderSecondLayer(InterfaceElement element) {
        this.dialog = (AbstractDiplomaticDialog) element;
        renderShadow();
        renderBlackout();
        renderBackground();
        renderColorTag();
        renderLabels();
        renderButtons();
    }

    private void renderColorTag() {
        if (this.dialog.getTagColor() != -1) {
            GraphicsYio.drawByRectangle(this.batch, MenuRender.renderDiplomacyElement.getBackgroundPixel(this.menuViewYio.yioGdxGame.gameController.getColorIndexWithOffset(this.dialog.getTagColor())), this.dialog.tagPosition);
        }
    }

    private void renderButtons() {
        if (this.dialog.areButtonsEnabled()) {
            Iterator it = this.dialog.buttons.iterator();
            while (it.hasNext()) {
                AcButton button = (AcButton) it.next();
                GraphicsYio.drawByRectangle(this.batch, getButtonBackground(button), button.position);
                Color color = button.font.getColor();
                button.font.setColor(Color.BLACK);
                button.font.draw(this.batch, button.text, button.textPosition.f144x, button.textPosition.f145y);
                button.font.setColor(color);
                if (button.isSelected()) {
                    GraphicsYio.setBatchAlpha(this.batch, 0.6d * ((double) button.selectionFactor.get()));
                    GraphicsYio.drawByRectangle(this.batch, this.selectionPixel, button.position);
                    GraphicsYio.setBatchAlpha(this.batch, 1.0d);
                }
            }
        }
    }

    private TextureRegion getButtonBackground(AcButton button) {
        switch (button.action) {
            case 0:
                return this.yesBckTexture;
            case 1:
                return this.noBckTexture;
            default:
                return null;
        }
    }

    private void renderLabels() {
        Iterator it = this.dialog.labels.iterator();
        while (it.hasNext()) {
            AcLabel label = (AcLabel) it.next();
            Color color = label.font.getColor();
            label.font.setColor(Color.BLACK);
            label.font.draw(this.batch, label.text, label.position.f144x, label.position.f145y);
            label.font.setColor(color);
        }
    }

    private void renderBlackout() {
        GraphicsYio.setBatchAlpha(this.batch, 0.5d * ((double) this.dialog.appearFactor.get()));
        this.batch.draw(this.selectionPixel, 0.0f, 0.0f, GraphicsYio.width, GraphicsYio.height);
        GraphicsYio.setBatchAlpha(this.batch, 1.0d);
    }

    private void renderBackground() {
        GraphicsYio.drawByRectangle(this.batch, this.backgroundTexture, this.dialog.viewPosition);
    }

    private void renderShadow() {
        this.menuViewYio.renderShadow(this.dialog.viewPosition, 1.0f, this.batch);
    }

    public void renderThirdLayer(InterfaceElement element) {
    }
}
