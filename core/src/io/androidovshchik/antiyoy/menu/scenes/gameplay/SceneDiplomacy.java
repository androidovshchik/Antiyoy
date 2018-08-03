package io.androidovshchik.antiyoy.menu.scenes.gameplay;

import io.androidovshchik.antiyoy.menu.MenuControllerYio;
import io.androidovshchik.antiyoy.menu.diplomacy_element.DiplomacyElement;

public class SceneDiplomacy extends AbstractGameplayScene {
    public DiplomacyElement diplomacyElement = null;

    public SceneDiplomacy(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
    }

    public void create() {
        if (this.diplomacyElement == null) {
            initDiplomacyElement();
        }
        this.diplomacyElement.appear();
    }

    private void initDiplomacyElement() {
        this.diplomacyElement = new DiplomacyElement(this.menuControllerYio, -1);
        this.diplomacyElement.setPosition(generateRectangle(0.0d, 0.0d, 1.0d, 0.65d));
        this.menuControllerYio.addElementToScene(this.diplomacyElement);
    }

    public void hide() {
        this.diplomacyElement.destroy();
    }
}
