package io.androidovshchik.antiyoy.menu.behaviors.gameplay;

import io.androidovshchik.antiyoy.menu.ButtonYio;
import io.androidovshchik.antiyoy.menu.behaviors.Reaction;
import io.androidovshchik.antiyoy.menu.scenes.Scenes;

public class RbEndTurn extends Reaction {
    public void perform(ButtonYio buttonYio) {
        if (!haveToAskToEndTurn(buttonYio)) {
            getGameController(buttonYio).onEndTurnButtonPressed();
        } else if (buttonYio.id == 321) {
            Scenes.sceneConfirmEndTurn.hide();
            getGameController(buttonYio).onEndTurnButtonPressed();
        } else {
            Scenes.sceneConfirmEndTurn.create();
        }
    }

    private boolean haveToAskToEndTurn(ButtonYio buttonYio) {
        return getGameController(buttonYio).haveToAskToEndTurn();
    }
}
