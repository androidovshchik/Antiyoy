package io.androidovshchik.antiyoy.menu;

import yio.tro.antiyoy.gameplay.diplomacy.DiplomacyManager;

public class SpecialActionController {
    MenuControllerYio menuControllerYio;

    public SpecialActionController(MenuControllerYio menuControllerYio) {
        this.menuControllerYio = menuControllerYio;
    }

    public void move() {
    }

    public void perform() {
        getDiplomacyManager().onDiplomacyButtonPressed();
    }

    private DiplomacyManager getDiplomacyManager() {
        return this.menuControllerYio.yioGdxGame.gameController.fieldController.diplomacyManager;
    }
}
