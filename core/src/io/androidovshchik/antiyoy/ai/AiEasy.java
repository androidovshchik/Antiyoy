package io.androidovshchik.antiyoy.ai;

import java.util.ArrayList;
import yio.tro.antiyoy.gameplay.GameController;
import yio.tro.antiyoy.gameplay.Hex;
import yio.tro.antiyoy.gameplay.Province;
import yio.tro.antiyoy.gameplay.Unit;

public class AiEasy extends ArtificialIntelligence {
    public AiEasy(GameController gameController, int color) {
        super(gameController, color);
    }

    public void makeMove() {
        moveUnits();
        spendMoneyAndMergeUnits();
    }

    void decideAboutUnit(Unit unit, ArrayList<Hex> moveZone, Province province) {
        if (!checkToCleanSomeTrees(unit, moveZone, province)) {
            this.gameController.moveUnit(unit, (Hex) moveZone.get(this.random.nextInt(moveZone.size())), province);
        }
    }

    void tryToBuildUnits(Province province) {
        int i = 1;
        while (i <= 4 && province.canAiAffordUnit(i)) {
            while (province.canBuildUnit(i)) {
                if (!tryToBuiltUnitInsideProvince(province, i)) {
                    break;
                }
            }
            i++;
        }
        if (province.canBuildUnit(1) && howManyUnitsInProvince(province) <= 1) {
            tryToAttackWithStrength(province, 1);
        }
    }

    void tryToBuildTowers(Province province) {
    }

    protected boolean mergeConditions(Province province, Unit unit, Hex hex) {
        return super.mergeConditions(province, unit, hex) && unit.strength == 1 && hex.unit.strength == 1;
    }

    void mergeUnits(Province province) {
        if (this.random.nextDouble() < 0.25d) {
            super.mergeUnits(province);
        }
    }
}
