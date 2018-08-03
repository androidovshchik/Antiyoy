package io.androidovshchik.antiyoy.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import yio.tro.antiyoy.gameplay.GameController;
import yio.tro.antiyoy.gameplay.Hex;
import yio.tro.antiyoy.gameplay.Province;
import yio.tro.antiyoy.gameplay.Unit;

public class AiBalancerGenericRules extends AiExpertGenericRules implements Comparator<Hex> {
    private int[] playerHexCount;
    private ArrayList<Hex> propagationList;
    private ArrayList<Hex> result = new ArrayList();

    public AiBalancerGenericRules(GameController gameController, int color) {
        super(gameController, color);
    }

    private void updateSortConditions() {
        this.playerHexCount = this.gameController.fieldController.getPlayerHexCount();
    }

    public void makeMove() {
        moveUnits();
        spendMoneyAndMergeUnits();
        checkToKillRedundantUnits();
        moveAfkUnits();
    }

    private void checkToKillRedundantUnits() {
        Iterator it = this.gameController.fieldController.provinces.iterator();
        while (it.hasNext()) {
            checkToKillRedundantUnits((Province) it.next());
        }
    }

    private void checkToKillRedundantUnits(Province province) {
        boolean detectedStrong = false;
        Iterator it = province.hexList.iterator();
        while (it.hasNext()) {
            Hex hex = (Hex) it.next();
            if (hex.containsUnit()) {
                if (!hex.unit.isReadyToMove()) {
                    return;
                }
                if (hex.unit.strength >= 3) {
                    detectedStrong = true;
                }
            }
        }
        if (detectedStrong) {
            killRedundantUnits(province);
        }
    }

    private void killRedundantUnits(Province province) {
        while (province.money >= 10 && province.getBalance() >= 0) {
            Unit unitWithMaxStrengh = findUnitWithMaxStrenghExceptKnight(province);
            if (unitWithMaxStrengh != null) {
                this.gameController.fieldController.buildUnit(province, unitWithMaxStrengh.currentHex, 1);
            } else {
                return;
            }
        }
    }

    protected boolean isOkToBuildNewFarm(Province srcProvince) {
        if (srcProvince.money > srcProvince.getCurrentFarmPrice() * 2) {
            return true;
        }
        int srcArmyStrength = getArmyStrength(srcProvince);
        updateNearbyProvinces(srcProvince);
        Iterator it = this.nearbyProvinces.iterator();
        while (it.hasNext()) {
            Province province = (Province) it.next();
            if (province != srcProvince && srcArmyStrength < getArmyStrength(province) / 2) {
                return false;
            }
        }
        if (findHexThatNeedsTower(srcProvince) != null) {
            return false;
        }
        return true;
    }

    private Unit findUnitWithMaxStrenghExceptKnight(Province province) {
        Unit result = null;
        Iterator it = province.hexList.iterator();
        while (it.hasNext()) {
            Hex hex = (Hex) it.next();
            if (hex.containsUnit()) {
                Unit unit = hex.unit;
                if (unit.strength != 4 && (result == null || unit.strength > result.strength)) {
                    result = unit;
                }
            }
        }
        return result;
    }

    void decideAboutUnit(Unit unit, ArrayList<Hex> moveZone, Province province) {
        if (!unit.isReadyToMove()) {
            System.out.println("AiBalancerGenericRules.decideAboutUnit: received unit that is not ready to move");
        } else if ((unit.strength > 2 || !checkToCleanSomePalms(unit, moveZone, province)) && !checkToCleanSomeTrees(unit, moveZone, province)) {
            ArrayList<Hex> attackableHexes = findAttackableHexes(unit.getColor(), moveZone);
            if (attackableHexes.size() > 0) {
                tryToAttackSomething(unit, province, attackableHexes);
            } else if (unit.currentHex.isInPerimeter()) {
                pushUnitToBetterDefense(unit, province);
            }
        }
    }

    void pushUnitToBetterDefense(Unit unit, Province province) {
        if (unit.isReadyToMove()) {
            for (int i = 0; i < 6; i++) {
                Hex adjHex = unit.currentHex.getAdjacentHex(i);
                if (adjHex.active && adjHex.sameColor(unit.currentHex) && adjHex.isFree() && predictDefenseGainWithUnit(adjHex, unit) >= 3) {
                    this.gameController.moveUnit(unit, adjHex, province);
                    return;
                }
            }
        }
    }

    protected int predictDefenseGainWithUnit(Hex hex, Unit unit) {
        int defenseGain = (0 - hex.getDefenseNumber()) + unit.strength;
        for (int i = 0; i < 6; i++) {
            Hex adjHex = unit.currentHex.getAdjacentHex(i);
            if (adjHex.active && adjHex.sameColor(unit.currentHex)) {
                defenseGain = (defenseGain - adjHex.getDefenseNumber()) + unit.strength;
            }
        }
        return defenseGain;
    }

    private void checkToSwapUnitForTower(Unit unit, ArrayList<Hex> moveZone, Province province) {
        if (unit.isReadyToMove() && province.hasMoneyForTower() && !unit.currentHex.hasThisObjectNearby(4)) {
            int x = unit.currentHex.index1;
            int y = unit.currentHex.index2;
            this.gameController.moveUnit(unit, (Hex) moveZone.get(this.random.nextInt(moveZone.size())), province);
            this.gameController.fieldController.buildTower(province, this.gameController.fieldController.field[x][y]);
        }
    }

    protected void tryToAttackSomething(Unit unit, Province province, ArrayList<Hex> attackableHexes) {
        if (unitCanMoveSafely(unit)) {
            Hex mostAttackableHex = findMostAttractiveHex(attackableHexes, unit, unit.strength);
            if (mostAttackableHex != null) {
                this.gameController.moveUnit(unit, mostAttackableHex, province);
            }
        }
    }

    Hex findMostAttractiveHex(ArrayList<Hex> attackableHexes, Unit unit, int strength) {
        if (strength == 3 || strength == 4) {
            Hex hex = findHexAttractiveToBaron(attackableHexes, strength);
            if (hex != null) {
                return hex;
            }
        }
        Hex result = null;
        int currMax = -1;
        Iterator it = attackableHexes.iterator();
        while (it.hasNext()) {
            Hex attackableHex = (Hex) it.next();
            int currNum = getAttackAllure(attackableHex, unit.getColor());
            if (currNum > currMax) {
                currMax = currNum;
                result = attackableHex;
            }
        }
        return result;
    }

    protected Hex getNearbyHexWithColor(Hex src, int color) {
        for (int i = 0; i < 6; i++) {
            Hex adjHex = src.getAdjacentHex(i);
            if (adjHex.active && adjHex.sameColor(color) && adjHex.numberOfFriendlyHexesNearby() != 0) {
                return adjHex;
            }
        }
        return null;
    }

    int getAttackAllure(Hex hex, int color) {
        int c = 0;
        for (int i = 0; i < 6; i++) {
            Hex adjHex = hex.getAdjacentHex(i);
            if (adjHex.active && adjHex.sameColor(color)) {
                c++;
            }
            if (adjHex.active && adjHex.sameColor(color) && adjHex.objectInside == 3) {
                c += 5;
            }
        }
        return c;
    }

    void tryToBuildUnits(Province province) {
        tryToBuildUnitsOnPalms(province);
        tryToReinforceUnits(province);
        int i = 1;
        while (i <= 4 && province.canAiAffordUnit(i, 5)) {
            while (province.canBuildUnit(i)) {
                if (!tryToAttackWithStrength(province, i)) {
                    break;
                }
            }
            i++;
        }
        if (province.canBuildUnit(1) && howManyUnitsInProvince(province) <= 1) {
            tryToAttackWithStrength(province, 1);
        }
    }

    private void tryToReinforceUnits(Province province) {
        Iterator it = province.hexList.iterator();
        while (it.hasNext()) {
            Hex hex = (Hex) it.next();
            if (hex.containsUnit()) {
                Unit unit = hex.unit;
                if (unitHasToBeReinforced(unit) && province.canAiAffordUnit(unit.strength + 1)) {
                    this.gameController.fieldController.buildUnit(province, hex, 1);
                }
            }
        }
    }

    private boolean unitHasToBeReinforced(Unit unit) {
        if (unit.strength == 4) {
            return false;
        }
        ArrayList<Hex> moveZone = this.gameController.detectMoveZone(unit.currentHex, unit.strength);
        if (!moveZoneContainsEnemyHexes(moveZone, unit.getColor()) || findAttackableHexes(unit.getColor(), moveZone).size() > 0) {
            return false;
        }
        return true;
    }

    private boolean moveZoneContainsEnemyHexes(ArrayList<Hex> moveZone, int unitColor) {
        Iterator it = moveZone.iterator();
        while (it.hasNext()) {
            if (!((Hex) it.next()).sameColor(unitColor)) {
                return true;
            }
        }
        return false;
    }

    protected boolean isHexDefendedBySomethingElse(Hex hex, Unit unit) {
        if (hex.getDefenseNumber(unit) != 0 && hex.getDefenseNumber() - hex.getDefenseNumber(unit) < 2) {
            return true;
        }
        return false;
    }

    protected int predictDefenseLossWithoutUnit(Unit unit) {
        int defenseLoss = 0 + (unit.currentHex.getDefenseNumber() - unit.currentHex.getDefenseNumber(unit));
        for (int i = 0; i < 6; i++) {
            Hex adjHex = unit.currentHex.getAdjacentHex(i);
            if (adjHex.active && adjHex.sameColor(unit.currentHex)) {
                defenseLoss += adjHex.getDefenseNumber() - adjHex.getDefenseNumber(unit);
            }
        }
        return defenseLoss;
    }

    protected boolean hasSafePathToTown(Hex startHex, Unit attackUnit) {
        this.propagationList.clear();
        Iterator it = this.gameController.getProvinceByHex(startHex).hexList.iterator();
        while (it.hasNext()) {
            ((Hex) it.next()).flag = false;
        }
        this.propagationList.add(startHex);
        while (this.propagationList.size() > 0) {
            Hex hex = (Hex) this.propagationList.get(0);
            this.propagationList.remove(hex);
            if (hex.objectInside == 3) {
                return true;
            }
            for (int i = 0; i < 6; i++) {
                Hex adjHex = hex.getAdjacentHex(i);
                if (adjHex.active && adjHex.sameColor(startHex) && !adjHex.flag && adjHex.getDefenseNumber(attackUnit) != 0) {
                    adjHex.flag = true;
                    this.propagationList.add(adjHex);
                }
            }
        }
        return false;
    }

    ArrayList<Hex> findAttackableHexes(int attackerColor, ArrayList<Hex> moveZone) {
        this.result.clear();
        Iterator it = moveZone.iterator();
        while (it.hasNext()) {
            Hex hex = (Hex) it.next();
            if (hex.colorIndex != attackerColor) {
                this.result.add(hex);
            }
        }
        updateSortConditions();
        Collections.sort(this.result, this);
        return this.result;
    }

    private int unitsNearby(Hex hex) {
        int c = 0;
        for (int i = 0; i < 6; i++) {
            Hex adjHex = hex.getAdjacentHex(i);
            if (adjHex.active && adjHex.sameColor(this.color) && adjHex.containsUnit() && adjHex.containsTower()) {
                c++;
            }
        }
        return c;
    }

    public int compare(Hex a, Hex b) {
        int aDefense = unitsNearby(a);
        int bDefense = unitsNearby(b);
        if (aDefense == bDefense) {
            return getHexCount(b.colorIndex) - getHexCount(a.colorIndex);
        }
        return bDefense - aDefense;
    }

    protected int getHexCount(int index) {
        if (index >= 0 && index < this.playerHexCount.length) {
            return this.playerHexCount[index];
        }
        return 0;
    }

    boolean needTowerOnHex(Hex hex) {
        if (!hex.active || !hex.isFree()) {
            return false;
        }
        updateNearbyProvinces(hex);
        if (this.nearbyProvinces.size() == 0 || getPredictedDefenseGainByNewTower(hex) < 3) {
            return false;
        }
        return true;
    }
}