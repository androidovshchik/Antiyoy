package io.androidovshchik.antiyoy.ai;

import yio.tro.antiyoy.gameplay.GameController;

public class AiHardSlayRules extends ArtificialIntelligence {
    public AiHardSlayRules(GameController gameController, int color) {
        super(gameController, color);
    }

    public void makeMove() {
        moveUnits();
        spendMoneyAndMergeUnits();
        moveAfkUnits();
    }
}
