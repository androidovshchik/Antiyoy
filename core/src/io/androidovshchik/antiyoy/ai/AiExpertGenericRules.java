package io.androidovshchik.antiyoy.ai;

import java.util.ArrayList;
import java.util.Iterator;
import io.androidovshchik.antiyoy.gameplay.GameController;
import io.androidovshchik.antiyoy.gameplay.Hex;
import io.androidovshchik.antiyoy.gameplay.Province;
import io.androidovshchik.antiyoy.gameplay.Unit;
import io.androidovshchik.antiyoy.stuff.PointYio;

public class AiExpertGenericRules extends ArtificialIntelligenceGeneric {
    private ArrayList<Hex> hexesInPerimeter = new ArrayList();
    private final Hex tempHex;

    public AiExpertGenericRules(GameController gameController, int color) {
        super(gameController, color);
        this.tempHex = new Hex(0, 0, new PointYio(), gameController.fieldController);
    }

    public void makeMove() {
        moveUnits();
        spendMoneyAndMergeUnits();
        moveAfkUnits();
    }

    void decideAboutUnit(Unit unit, ArrayList<Hex> moveZone, Province province) {
        if (unit.strength > 2 || !checkToCleanSomePalms(unit, moveZone, province)) {
            ArrayList<Hex> attackableHexes = findAttackableHexes(unit.getColor(), moveZone);
            if (attackableHexes.size() > 0) {
                tryToAttackSomething(unit, province, attackableHexes);
            } else if (!checkToCleanSomeTrees(unit, moveZone, province) && unit.currentHex.isInPerimeter()) {
                pushUnitToBetterDefense(unit, province);
            }
        }
    }

    protected boolean isHexDefendedBySomethingElse(Hex hex, Unit unit) {
        for (int i = 0; i < 6; i++) {
            Hex adjHex = hex.getAdjacentHex(i);
            if (adjHex.active && adjHex.sameColor(hex)) {
                if (adjHex.containsBuilding()) {
                    return true;
                }
                if (adjHex.containsUnit() && adjHex.unit != unit) {
                    return true;
                }
            }
        }
        return false;
    }

    protected boolean unitCanMoveSafely(Unit unit) {
        int leftBehindNumber = 0;
        for (int i = 0; i < 6; i++) {
            Hex adjHex = unit.currentHex.getAdjacentHex(i);
            if (adjHex.active && adjHex.sameColor(unit.currentHex) && !isHexDefendedBySomethingElse(adjHex, unit) && adjHex.isInPerimeter()) {
                leftBehindNumber++;
            }
        }
        return leftBehindNumber <= 3;
    }

    protected void tryToAttackSomething(Unit unit, Province province, ArrayList<Hex> attackableHexes) {
        if (unitCanMoveSafely(unit)) {
            this.gameController.moveUnit(unit, findMostAttractiveHex(attackableHexes, province, unit.strength), province);
        }
    }

    boolean hexHasFriendlyBuildingNearby(Hex hex) {
        for (int i = 0; i < 6; i++) {
            Hex adjHex = hex.getAdjacentHex(i);
            if (adjHex.active && adjHex.sameColor(hex) && adjHex.containsBuilding()) {
                return true;
            }
        }
        return false;
    }

    Hex findMostAttractiveHex(ArrayList<Hex> attackableHexes, Province province, int strength) {
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
            int currNum = getAttackAllure(attackableHex, province.getColor());
            if (currNum > currMax) {
                currMax = currNum;
                result = attackableHex;
            }
        }
        return result;
    }

    private Hex findRandomHexInPerimeter(Province province) {
        this.hexesInPerimeter.clear();
        Iterator it = province.hexList.iterator();
        while (it.hasNext()) {
            Hex hex = (Hex) it.next();
            if (hex.isInPerimeter()) {
                this.hexesInPerimeter.add(hex);
            }
        }
        if (this.hexesInPerimeter.size() == 0) {
            return null;
        }
        return (Hex) this.hexesInPerimeter.get(this.random.nextInt(this.hexesInPerimeter.size()));
    }

    void moveAfkUnit(Province province, Unit unit) {
        Hex hexToMove = findRandomHexInPerimeter(province);
        if (hexToMove != null) {
            this.tempHex.set(unit.currentHex);
            unit.marchToHex(hexToMove, province);
            if (this.tempHex.equals(unit.currentHex)) {
                super.moveAfkUnit(province, unit);
            }
        }
    }

    private boolean provinceHasEnoughIncomeForUnit(Province province, int strength) {
        return province.canAiAffordUnit(strength);
    }

    void tryToBuildUnits(Province province) {
        tryToBuildUnitsOnPalms(province);
        int i = 1;
        while (i <= 4 && provinceHasEnoughIncomeForUnit(province, i)) {
            boolean successfullyAttacked = false;
            if (province.canBuildUnit(i)) {
                successfullyAttacked = tryToAttackWithStrength(province, i);
            }
            if (successfullyAttacked) {
                i = 0;
            }
            i++;
        }
        if (province.canBuildUnit(1) && howManyUnitsInProvince(province) <= 1) {
            tryToAttackWithStrength(province, 1);
        }
    }

    void tryToBuildTowers(Province province) {
        while (province.hasMoneyForTower()) {
            Hex hex = findHexThatNeedsTower(province);
            if (hex == null) {
                break;
            }
            this.gameController.fieldController.buildTower(province, hex);
        }
        while (provinceCanAffordStrongTower(province)) {
            Hex hex = findHexForStrongTower(province);
            if (hex != null) {
                this.gameController.fieldController.buildStrongTower(province, hex);
            } else {
                return;
            }
        }
    }

    protected Hex findHexForStrongTower(Province province) {
        Iterator it = province.hexList.iterator();
        while (it.hasNext()) {
            Hex hex = (Hex) it.next();
            if (hex.objectInside == 4 && needsStrongTowerOnHex(province, hex)) {
                return hex;
            }
        }
        return null;
    }

    protected boolean needsStrongTowerOnHex(Province province, Hex hex) {
        updateNearbyProvinces(hex);
        if (this.nearbyProvinces.size() == 0) {
            return false;
        }
        Iterator it = this.nearbyProvinces.iterator();
        while (it.hasNext()) {
            if (((Province) it.next()).hexList.size() > province.hexList.size() / 2) {
                return true;
            }
        }
        return false;
    }

    protected boolean provinceCanAffordStrongTower(Province province) {
        if (province.hasMoneyForStrongTower() && province.getBalance() - 6 >= 5) {
            return true;
        }
        return false;
    }

    boolean needTowerOnHex(Hex hex) {
        if (hex.active && hex.isFree() && getPredictedDefenseGainByNewTower(hex) >= 4) {
            return true;
        }
        return false;
    }
}
