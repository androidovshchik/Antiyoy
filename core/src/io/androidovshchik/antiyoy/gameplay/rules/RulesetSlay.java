package io.androidovshchik.antiyoy.gameplay.rules;

import yio.tro.antiyoy.gameplay.GameController;
import yio.tro.antiyoy.gameplay.Hex;
import yio.tro.antiyoy.gameplay.Province;
import yio.tro.antiyoy.gameplay.Unit;

public class RulesetSlay extends Ruleset {
    public RulesetSlay(GameController gameController) {
        super(gameController);
    }

    public boolean canSpawnPineOnHex(Hex hex) {
        return hex.isFree() && howManyTreesNearby(hex) >= 2 && hex.hasPineReadyToExpandNearby() && this.gameController.getRandom().nextDouble() < 0.8d;
    }

    public boolean canSpawnPalmOnHex(Hex hex) {
        return hex.isFree() && hex.isNearWater() && hex.hasPalmReadyToExpandNearby();
    }

    public void onUnitAdd(Hex hex) {
    }

    public void onTurnEnd() {
    }

    public boolean canMergeUnits(Unit unit1, Unit unit2) {
        return this.gameController.mergedUnitStrength(unit1, unit2) <= 4;
    }

    public int getHexIncome(Hex hex) {
        if (hex.containsTree()) {
            return 0;
        }
        return 1;
    }

    public int getHexTax(Hex hex) {
        if (hex.containsUnit()) {
            return getUnitTax(hex.unit.strength);
        }
        return 0;
    }

    public int getUnitTax(int strength) {
        switch (strength) {
            case 2:
                return 6;
            case 3:
                return 18;
            case 4:
                return 54;
            default:
                return 2;
        }
    }

    public boolean canBuildUnit(Province province, int strength) {
        return province.money >= strength * 10;
    }

    public void onUnitMoveToHex(Unit unit, Hex hex) {
    }

    public boolean canUnitAttackHex(int unitStrength, Hex hex) {
        return unitStrength > hex.getDefenseNumber();
    }

    public int getColorIndexWithOffset(int srcIndex) {
        srcIndex += this.gameController.colorIndexViewOffset;
        if (srcIndex >= GameRules.colorNumber) {
            return srcIndex - GameRules.colorNumber;
        }
        return srcIndex;
    }
}
