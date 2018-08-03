package io.androidovshchik.antiyoy.menu;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import io.androidovshchik.antiyoy.factor_yio.FactorYio;
import io.androidovshchik.antiyoy.menu.render.MenuRender;
import io.androidovshchik.antiyoy.stuff.Fonts;
import io.androidovshchik.antiyoy.stuff.GraphicsYio;
import io.androidovshchik.antiyoy.stuff.PointYio;
import io.androidovshchik.antiyoy.stuff.RectangleYio;

public class NotificationElement extends InterfaceElement {
    public static final int AUTO_HIDE_DELAY = 1500;
    FactorYio appearFactor = new FactorYio();
    boolean autoHide = false;
    public BitmapFont font = Fonts.smallerMenuFont;
    MenuControllerYio menuControllerYio;
    public String message = "";
    public RectangleYio position = new RectangleYio();
    public PointYio textDelta = new PointYio();
    float textOffset = (0.03f * GraphicsYio.width);
    public PointYio textPosition = new PointYio();
    private long timeToHide = 0;
    public RectangleYio viewPosition = new RectangleYio();

    public NotificationElement(MenuControllerYio menuControllerYio, int id) {
        super(id);
        this.menuControllerYio = menuControllerYio;
    }

    public void move() {
        this.appearFactor.move();
        updateViewPosition();
        updateTextPosition();
        checkToDie();
    }

    private void updateTextPosition() {
        this.textPosition.f144x = (float) (this.viewPosition.f146x + ((double) this.textDelta.f144x));
        this.textPosition.f145y = (float) (this.viewPosition.f147y + ((double) this.textDelta.f145y));
    }

    private void updateViewPosition() {
        this.viewPosition.setBy(this.position);
        RectangleYio rectangleYio = this.viewPosition;
        rectangleYio.f147y += ((double) ((1.0f - this.appearFactor.get()) * 1.5f)) * this.position.height;
    }

    private void checkToDie() {
        if (this.autoHide && System.currentTimeMillis() > this.timeToHide) {
            destroy();
        }
    }

    public FactorYio getFactor() {
        return this.appearFactor;
    }

    public void destroy() {
        this.appearFactor.destroy(1, 3.0d);
        this.autoHide = false;
    }

    public void appear() {
        this.appearFactor.setValues(0.01d, 0.0d);
        this.appearFactor.appear(3, 1.0d);
    }

    public boolean isVisible() {
        return this.appearFactor.get() > 0.0f;
    }

    public boolean checkToPerformAction() {
        return false;
    }

    public boolean isTouchable() {
        return false;
    }

    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    public boolean touchDrag(int screenX, int screenY, int pointer) {
        return false;
    }

    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    public void setTouchable(boolean touchable) {
    }

    public boolean isButton() {
        return false;
    }

    public void enableAutoHide() {
        this.autoHide = true;
        this.timeToHide = System.currentTimeMillis() + 1500;
    }

    public void setMessage(String message) {
        this.message = message;
        updateTextDelta();
    }

    private void updateTextDelta() {
        this.textDelta.f144x = this.textOffset;
        float textHeight = GraphicsYio.getTextHeight(this.font, this.message);
        this.textDelta.f145y = (float) ((this.position.height / 2.0d) + ((double) (textHeight / 2.0f)));
    }

    public void setPosition(RectangleYio position) {
        this.position.setBy(position);
    }

    public MenuRender getRenderSystem() {
        return MenuRender.renderNotificationElement;
    }
}
