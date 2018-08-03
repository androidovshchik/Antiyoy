package io.androidovshchik.antiyoy.menu;

import io.androidovshchik.antiyoy.factor_yio.FactorYio;
import io.androidovshchik.antiyoy.menu.render.MenuRender;
import io.androidovshchik.antiyoy.stuff.RectangleYio;

public abstract class InterfaceElement {
    public static final double DES_SPEED = 2.0d;
    public static final int DES_TYPE = 2;
    public static final double SPAWN_SPEED = 2.0d;
    public static final int SPAWN_TYPE = 2;
    public int id;

    public abstract void appear();

    public abstract boolean checkToPerformAction();

    public abstract void destroy();

    public abstract FactorYio getFactor();

    public abstract MenuRender getRenderSystem();

    public abstract boolean isButton();

    public abstract boolean isTouchable();

    public abstract boolean isVisible();

    public abstract void move();

    public abstract void setPosition(RectangleYio rectangleYio);

    public abstract void setTouchable(boolean z);

    public abstract boolean touchDown(int i, int i2, int i3, int i4);

    public abstract boolean touchDrag(int i, int i2, int i3);

    public abstract boolean touchUp(int i, int i2, int i3, int i4);

    public InterfaceElement(int id) {
        this.id = id;
    }

    public boolean onMouseWheelScrolled(int amount) {
        return false;
    }

    public void onPause() {
    }

    public void onResume() {
    }

    public boolean isTouchInsideRectangle(float touchX, float touchY, RectangleYio rectangleYio, float offset) {
        return isTouchInsideRectangle(touchX, touchY, (float) rectangleYio.f146x, (float) rectangleYio.f147y, (float) rectangleYio.width, (float) rectangleYio.height, offset);
    }

    public boolean isTouchInsideRectangle(float touchX, float touchY, float x, float y, float width, float height, float offset) {
        if (touchX >= x - offset && touchX <= (x + width) + offset && touchY >= y - offset && touchY <= (y + height) + offset) {
            return true;
        }
        return false;
    }
}
