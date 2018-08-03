package io.androidovshchik.antiyoy.menu.scenes.gameplay;

import io.androidovshchik.antiyoy.menu.MenuControllerYio;
import io.androidovshchik.antiyoy.menu.diplomatic_log.DiplomaticLogPanel;
import io.androidovshchik.antiyoy.stuff.LanguagesManager;

public class SceneDiplomaticLog extends AbstractGameplayScene {
    DiplomaticLogPanel logPanel = null;

    public SceneDiplomaticLog(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
    }

    public void create() {
        initLogPanelOnce();
        this.logPanel.appear();
        this.logPanel.updateItems();
    }

    private void initLogPanelOnce() {
        if (this.logPanel == null) {
            this.logPanel = new DiplomaticLogPanel(this.menuControllerYio);
            this.logPanel.setPosition(generateRectangle(0.0d, 0.0d, 1.0d, 0.55d));
            this.logPanel.setTitle(LanguagesManager.getInstance().getString("log"));
            this.menuControllerYio.addElementToScene(this.logPanel);
        }
    }

    public void hide() {
        this.logPanel.destroy();
    }
}
