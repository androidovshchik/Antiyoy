package io.androidovshchik.antiyoy.menu.behaviors.gameplay;

import yio.tro.antiyoy.gameplay.RefuseStatistics;
import yio.tro.antiyoy.menu.ButtonYio;
import yio.tro.antiyoy.menu.behaviors.Reaction;

public class RbWinGame extends Reaction {
    public void perform(ButtonYio buttonYio) {
        getGameController(buttonYio).forceGameEnd();
        RefuseStatistics.getInstance().onEarlyGameEndAccept();
    }
}
