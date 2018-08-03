package io.androidovshchik.antiyoy.gameplay.rules;

import io.androidovshchik.antiyoy.gameplay.FieldController;
import io.androidovshchik.antiyoy.gameplay.GameController;
import io.androidovshchik.antiyoy.gameplay.Hex;
import io.androidovshchik.antiyoy.gameplay.Province;
import io.androidovshchik.antiyoy.gameplay.Unit;

public class RulesetGeneric extends Ruleset {
    public RulesetGeneric(GameController gameController) {
        super(gameController);
    }

    public boolean canSpawnPineOnHex(Hex hex) {
        return hex.isFree() && howManyTreesNearby(hex) >= 2 && hex.hasPineReadyToExpandNearby() && this.gameController.getRandom().nextDouble() < 0.2d;
    }

    public boolean canSpawnPalmOnHex(Hex hex) {
        return hex.isFree() && hex.isNearWater() && hex.hasPalmReadyToExpandNearby() && this.gameController.getRandom().nextDouble() < 0.3d;
    }

    public void onUnitAdd(Hex hex) {
        if (hex.containsTree()) {
            Province provinceByHex = this.gameController.fieldController.getProvinceByHex(hex);
            provinceByHex.money += 3;
            this.gameController.fieldController.updateSelectedProvinceMoney();
        }
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
        if (hex.objectInside == 6) {
            return 5;
        }
        return 1;
    }

    public int getHexTax(Hex hex) {
        if (hex.containsUnit()) {
            return getUnitTax(hex.unit.strength);
        }
        if (hex.objectInside == 4) {
            return 1;
        }
        if (hex.objectInside == 7) {
            return 6;
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
                return 36;
            default:
                return 2;
        }
    }

    public boolean canBuildUnit(Province province, int strength) {
        return province.money >= strength * 10;
    }

    public void onUnitMoveToHex(Unit unit, Hex hex) {
        if (hex.containsTree()) {
            Province provinceByHex = this.gameController.getProvinceByHex(hex);
            provinceByHex.money += 3;
            this.gameController.selectionController.updateSelectedProvinceMoney();
        }
    }

    public boolean canUnitAttackHex(int unitStrength, Hex hex) {
        if (unitStrength != 4 && unitStrength <= hex.getDefenseNumber()) {
            return false;
        }
        return true;
    }

    public int getColorIndexWithOffset(int srcIndex) {
        srcIndex += this.gameController.colorIndexViewOffset;
        if (srcIndex >= FieldController.NEUTRAL_LANDS_INDEX) {
            return srcIndex - FieldController.NEUTRAL_LANDS_INDEX;
        }
        return srcIndex;
    }
}
