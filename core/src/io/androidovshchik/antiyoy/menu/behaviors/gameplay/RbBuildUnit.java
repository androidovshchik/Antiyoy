package io.androidovshchik.antiyoy.menu.behaviors.gameplay;

import yio.tro.antiyoy.YioGdxGame;
import yio.tro.antiyoy.menu.ButtonYio;
import yio.tro.antiyoy.menu.behaviors.Reaction;

public class RbBuildUnit extends Reaction {
    int[] chain = new int[]{1, 2, 3, 4};

    public void perform(ButtonYio buttonYio) {
        if (getGameController(buttonYio).selectionController.isSomethingSelected()) {
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
            getGameController(buttonYio).detectAndShowMoveZoneForBuildingUnit(tipType);
            return;
        }
        YioGdxGame.say("detected strange bug in RbBuildUnit");
    }
}
