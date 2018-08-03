package io.androidovshchik.antiyoy.gameplay.rules;

import yio.tro.antiyoy.gameplay.GameController;
import yio.tro.antiyoy.gameplay.Hex;
import yio.tro.antiyoy.gameplay.Province;
import yio.tro.antiyoy.gameplay.Unit;

public abstract class Ruleset {
    GameController gameController;

    public abstract boolean canBuildUnit(Province province, int i);

    public abstract boolean canMergeUnits(Unit unit, Unit unit2);

    public abstract boolean canSpawnPalmOnHex(Hex hex);

    public abstract boolean canSpawnPineOnHex(Hex hex);

    public abstract boolean canUnitAttackHex(int i, Hex hex);

    public abstract int getColorIndexWithOffset(int i);

    public abstract int getHexIncome(Hex hex);

    public abstract int getHexTax(Hex hex);

    public abstract int getUnitTax(int i);

    public abstract void onTurnEnd();

    public abstract void onUnitAdd(Hex hex);

    public abstract void onUnitMoveToHex(Unit unit, Hex hex);

    public Ruleset(GameController gameController) {
        this.gameController = gameController;
    }

    public int howManyTreesNearby(Hex hex) {
        if (!hex.active) {
            return 0;
        }
        int c = 0;
        for (int i = 0; i < 6; i++) {
            if (hex.getAdjacentHex(i).containsTree()) {
                c++;
            }
        }
        return c;
    }
}
