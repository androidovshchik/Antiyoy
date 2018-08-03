package io.androidovshchik.antiyoy.ai;

import io.androidovshchik.antiyoy.gameplay.GameController;
import io.androidovshchik.antiyoy.gameplay.Hex;
import io.androidovshchik.antiyoy.gameplay.Province;

public class AiHardGenericRules extends ArtificialIntelligenceGeneric {
    public AiHardGenericRules(GameController gameController, int color) {
        super(gameController, color);
    }

    public void makeMove() {
        moveUnits();
        spendMoneyAndMergeUnits();
        moveAfkUnits();
    }

    void tryToBuildTowers(Province province) {
        while (province.hasMoneyForTower()) {
            Hex hex = findHexThatNeedsTower(province);
            if (hex != null) {
                if (province.hasMoneyForStrongTower()) {
                    this.gameController.fieldController.buildStrongTower(province, hex);
                } else {
                    this.gameController.fieldController.buildTower(province, hex);
                }
            } else {
                return;
            }
        }
    }
}
