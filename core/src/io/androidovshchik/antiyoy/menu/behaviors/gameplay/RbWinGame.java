package io.androidovshchik.antiyoy.menu.behaviors.gameplay;

import io.androidovshchik.antiyoy.gameplay.RefuseStatistics;
import io.androidovshchik.antiyoy.menu.ButtonYio;
import io.androidovshchik.antiyoy.menu.behaviors.Reaction;

public class RbWinGame extends Reaction {
    public void perform(ButtonYio buttonYio) {
        getGameController(buttonYio).forceGameEnd();
        RefuseStatistics.getInstance().onEarlyGameEndAccept();
    }
}
