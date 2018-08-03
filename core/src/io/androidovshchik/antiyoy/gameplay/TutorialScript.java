package io.androidovshchik.antiyoy.gameplay;

import yio.tro.antiyoy.menu.MenuControllerYio;

public abstract class TutorialScript {
    GameController gameController;
    MenuControllerYio menuControllerYio;
    boolean tipIsCurrentlyShown;

    public abstract void createTutorialGame();

    public abstract void move();

    public TutorialScript(GameController gameController) {
        this.gameController = gameController;
    }

    public void setTipIsCurrentlyShown(boolean tipIsCurrentlyShown) {
        this.tipIsCurrentlyShown = tipIsCurrentlyShown;
    }
}
