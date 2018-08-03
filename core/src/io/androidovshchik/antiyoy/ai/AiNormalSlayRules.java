package io.androidovshchik.antiyoy.ai;

import java.util.ArrayList;
import io.androidovshchik.antiyoy.gameplay.GameController;
import io.androidovshchik.antiyoy.gameplay.Hex;
import io.androidovshchik.antiyoy.gameplay.Province;
import io.androidovshchik.antiyoy.gameplay.Unit;

public class AiNormalSlayRules extends ArtificialIntelligence {
    public AiNormalSlayRules(GameController gameController, int color) {
        super(gameController, color);
    }

    public void makeMove() {
        moveUnits();
        spendMoneyAndMergeUnits();
    }

    void decideAboutUnit(Unit unit, ArrayList<Hex> moveZone, Province province) {
        if (!checkChance(0.5d)) {
            super.decideAboutUnit(unit, moveZone, province);
        }
    }

    void tryToBuildUnits(Province province) {
        tryToBuildUnitsOnPalms(province);
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
}
