package io.androidovshchik.antiyoy.menu.scenes;

import yio.tro.antiyoy.menu.MenuControllerYio;
import yio.tro.antiyoy.menu.NotificationElement;
import yio.tro.antiyoy.stuff.LanguagesManager;

public class SceneNotification extends AbstractScene {
    public NotificationElement notificationElement = null;

    public SceneNotification(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
    }

    public void create() {
        checkToCreateNotificationElement();
        this.notificationElement.appear();
    }

    private void checkToCreateNotificationElement() {
        if (this.notificationElement == null) {
            this.notificationElement = new NotificationElement(this.menuControllerYio, -1);
            this.notificationElement.setPosition(generateRectangle(0.0d, 1.0d - 0.05d, 1.0d, 0.05d));
            this.menuControllerYio.addElementToScene(this.notificationElement);
        }
    }

    public void setValues(String messageKey, boolean autoHide) {
        this.notificationElement.setMessage(LanguagesManager.getInstance().getString(messageKey));
        if (autoHide) {
            this.notificationElement.enableAutoHide();
        }
    }

    public void showNotification(String messageKey, boolean autoHide) {
        create();
        setValues(messageKey, autoHide);
    }

    public void showNotification(String messageKey) {
        showNotification(messageKey, true);
    }

    public void hideNotification() {
        if (this.notificationElement != null) {
            this.notificationElement.destroy();
        }
    }
}
