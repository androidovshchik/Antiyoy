package io.androidovshchik.antiyoy.menu.behaviors.gameplay;

import yio.tro.antiyoy.gameplay.rules.GameRules;
import yio.tro.antiyoy.menu.ButtonYio;
import yio.tro.antiyoy.menu.behaviors.Reaction;

public class RbBuildSolidObject extends Reaction {
    int[] chain = new int[]{5, 0, 6};

    public void perform(ButtonYio buttonYio) {
        if (GameRules.slayRules) {
            getGameController(buttonYio).selectionController.awakeTip(0);
            return;
        }
        int tipType = getGameController(buttonYio).selectionController.getTipType();
        int newTipType = -1;
        for (int i = 0; i < this.chain.length; i++) {
            if (tipType == this.chain[i]) {
                if (i == this.chain.length - 1) {
                    newTipType = this.chain[0];
                } else {
                    newTipType = this.chain[i + 1];
                }
            }
        }
        if (newTipType == -1) {
            newTipType = this.chain[0];
        }
        tipType = newTipType;
        getGameController(buttonYio).selectionController.awakeTip(tipType);
        if (tipType == 5) {
            getGameController(buttonYio).detectAndShowMoveZoneForFarm();
        }
    }
}
