package io.androidovshchik.antiyoy.menu;

import io.androidovshchik.antiyoy.stuff.RectangleYio;

public class ButtonFactory {
    private final ButtonRenderer buttonRenderer = new ButtonRenderer();
    private final MenuControllerYio menuControllerYio;

    public ButtonFactory(MenuControllerYio menuControllerYio) {
        this.menuControllerYio = menuControllerYio;
    }

    public ButtonYio getButton(RectangleYio position, int id, String text) {
        ButtonYio buttonYio = this.menuControllerYio.getButtonById(id);
        if (buttonYio == null) {
            buttonYio = new ButtonYio(position, id, this.menuControllerYio);
            if (text != null) {
                buttonYio.addTextLine(text);
                this.buttonRenderer.renderButton(buttonYio);
            }
            this.menuControllerYio.addButtonToArray(buttonYio);
        }
        buttonYio.setVisible(true);
        buttonYio.setTouchable(true);
        buttonYio.appearFactor.appear(MenuControllerYio.SPAWN_ANIM, MenuControllerYio.SPAWN_SPEED);
        buttonYio.appearFactor.setValues(0.0d, 0.001d);
        buttonYio.touchAnimation = true;
        return buttonYio;
    }
}
