package io.androidovshchik.antiyoy.menu.render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.antiyoy.menu.InterfaceElement;
import yio.tro.antiyoy.menu.NotificationElement;
import yio.tro.antiyoy.stuff.GraphicsYio;

public class RenderNotificationElement extends MenuRender {
    private TextureRegion backgroundTexture;
    private BitmapFont font;
    private NotificationElement notificationElement;

    public void loadTextures() {
        this.backgroundTexture = GraphicsYio.loadTextureRegion("button_background_3.png", false);
    }

    public void renderFirstLayer(InterfaceElement element) {
    }

    public void renderSecondLayer(InterfaceElement element) {
        this.notificationElement = (NotificationElement) element;
        this.font = this.notificationElement.font;
        renderShadow();
        renderBackground();
        renderMessage();
    }

    private void renderShadow() {
        this.menuViewYio.renderShadow(this.notificationElement.viewPosition, 1.0f, this.batch);
    }

    private void renderMessage() {
        Color color = this.font.getColor();
        this.font.setColor(Color.BLACK);
        GraphicsYio.setFontAlpha(this.font, (double) this.notificationElement.getFactor().get());
        this.font.draw(this.batch, this.notificationElement.message, this.notificationElement.textPosition.f144x, this.notificationElement.textPosition.f145y);
        GraphicsYio.setFontAlpha(this.font, 1.0d);
        this.font.setColor(color);
    }

    private void renderBackground() {
        GraphicsYio.drawByRectangle(this.batch, this.backgroundTexture, this.notificationElement.viewPosition);
    }

    public void renderThirdLayer(InterfaceElement element) {
    }
}
