package io.androidovshchik.antiyoy.menu;

import yio.tro.antiyoy.factor_yio.FactorYio;
import yio.tro.antiyoy.stuff.RectangleYio;

public class CheckButtonYio extends ButtonYio {
    boolean checked;
    RectangleYio touchPosition;
    FactorYio viewFactor;

    public CheckButtonYio(RectangleYio position, int id, MenuControllerYio menuControllerYio) {
        super(position, id, menuControllerYio);
        this.checked = false;
        this.touchPosition = new RectangleYio(0.0d, 0.0d, 0.0d, 0.0d);
        this.viewFactor = new FactorYio();
        this.reaction = null;
        this.lockAction = true;
    }

    public static CheckButtonYio getCheckButton(MenuControllerYio menuControllerYio, RectangleYio position, int id) {
        CheckButtonYio checkButtonYioYio = menuControllerYio.getCheckButtonById(id);
        if (checkButtonYioYio == null) {
            checkButtonYioYio = new CheckButtonYio(position, id, menuControllerYio);
            checkButtonYioYio.setShadow(false);
            menuControllerYio.addCheckButtonToArray(checkButtonYioYio);
        }
        checkButtonYioYio.position = position;
        checkButtonYioYio.setVisible(true);
        checkButtonYioYio.setTouchable(true);
        checkButtonYioYio.appear();
        return checkButtonYioYio;
    }

    public FactorYio getFactor() {
        return this.appearFactor;
    }

    public void appear() {
        this.appearFactor.appear(MenuControllerYio.SPAWN_ANIM, MenuControllerYio.SPAWN_SPEED);
        this.appearFactor.setValues(0.0d, 0.001d);
    }

    public void move() {
        super.move();
        this.viewFactor.move();
    }

    protected boolean isTouched(int screenX, int screenY) {
        return isTouchInsideRectangle((float) screenX, (float) screenY, this.touchPosition, 0.0f);
    }

    public boolean checkTouch(int screenX, int screenY, int pointer, int button) {
        if (!this.touchable || !isTouchInsideRectangle((float) screenX, (float) screenY, this.touchPosition, 0.0f)) {
            return false;
        }
        press(screenX, screenY);
        return true;
    }

    public void press(int screenX, int screenY) {
        super.press(screenX, screenY);
        this.viewFactor.setValues(1.0d, 0.0d);
        this.viewFactor.destroy(1, 3.0d);
        if (this.checked) {
            this.checked = false;
            this.selectionFactor.setValues(1.0d, 0.0d);
            this.selectionFactor.destroy(1, 3.0d);
        } else {
            this.checked = true;
            this.selectionFactor.setValues(0.0d, 0.0d);
            this.selectionFactor.appear(1, 2.0d);
        }
        if (this.reaction != null) {
            this.reaction.perform(this);
        }
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
        if (checked) {
            this.selectionFactor.setValues(1.0d, 0.0d);
            this.selectionFactor.appear(1, 1.0d);
            return;
        }
        this.selectionFactor.setValues(0.0d, 0.0d);
        this.selectionFactor.destroy(1, 1.0d);
    }

    public void setTouchPosition(RectangleYio touchPosition) {
        this.touchPosition.set(touchPosition.f146x, touchPosition.f147y, touchPosition.width, touchPosition.height);
    }

    public void setPosition(RectangleYio position) {
        super.setPosition(position);
        this.touchPosition.set(position.f146x, position.f147y, position.width, position.height);
    }

    public FactorYio getViewFactor() {
        return this.viewFactor;
    }

    public RectangleYio getTouchPosition() {
        return this.touchPosition;
    }

    public boolean isChecked() {
        return this.checked;
    }

    boolean isTouchInsideRectangle(float touchX, float touchY, RectangleYio rectangleYio, float offset) {
        return isTouchInsideRectangle(touchX, touchY, (float) rectangleYio.f146x, (float) rectangleYio.f147y, (float) rectangleYio.width, (float) rectangleYio.height, offset);
    }

    boolean isTouchInsideRectangle(float touchX, float touchY, float x, float y, float width, float height, float offset) {
        if (touchX >= x - offset && touchX <= (x + width) + offset && touchY >= y - offset && touchY <= (y + height) + offset) {
            return true;
        }
        return false;
    }
}
