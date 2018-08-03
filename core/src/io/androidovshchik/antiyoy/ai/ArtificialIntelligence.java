package io.androidovshchik.antiyoy.ai;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import yio.tro.antiyoy.gameplay.GameController;
import yio.tro.antiyoy.gameplay.Hex;
import yio.tro.antiyoy.gameplay.Province;
import yio.tro.antiyoy.gameplay.Unit;

public abstract class ArtificialIntelligence {
    public static final int DIFFICULTY_BALANCER = 4;
    public static final int DIFFICULTY_EASY = 0;
    public static final int DIFFICULTY_EXPERT = 3;
    public static final int DIFFICULTY_HARD = 2;
    public static final int DIFFICULTY_NORMAL = 1;
    protected final int color;
    final GameController gameController;
    private ArrayList<Hex> junkList = new ArrayList();
    protected ArrayList<Province> nearbyProvinces = new ArrayList();
    final Random random;
    private ArrayList<Hex> tempResultList = new ArrayList();
    protected ArrayList<Unit> unitsReadyToMove = new ArrayList();

    public abstract void makeMove();

    ArtificialIntelligence(GameController gameController, int color) {
        this.gameController = gameController;
        this.color = color;
        this.random = gameController.random;
    }

    void updateUnitsReadyToMove() {
        this.unitsReadyToMove.clear();
        Iterator it = this.gameController.fieldController.provinces.iterator();
        while (it.hasNext()) {
            Province province = (Province) it.next();
            if (province.getColor() == this.color) {
                searchForUnitsReadyToMoveInProvince(province);
            }
        }
    }

    private void searchForUnitsReadyToMoveInProvince(Province province) {
        for (int k = province.hexList.size() - 1; k >= 0; k--) {
            Hex hex = (Hex) province.hexList.get(k);
            if (hex.containsUnit() && hex.unit.isReadyToMove()) {
                this.unitsReadyToMove.add(hex.unit);
            }
        }
    }

    void moveUnits() {
        updateUnitsReadyToMove();
        Iterator it = this.unitsReadyToMove.iterator();
        while (it.hasNext()) {
            Unit unit = (Unit) it.next();
            if (unit.isReadyToMove()) {
                ArrayList<Hex> moveZone = this.gameController.detectMoveZone(unit.currentHex, unit.strength, 4);
                excludeFriendlyBuildingsFromMoveZone(moveZone);
                excludeFriendlyUnitsFromMoveZone(moveZone);
                if (moveZone.size() != 0) {
                    decideAboutUnit(unit, moveZone, this.gameController.getProvinceByHex(unit.currentHex));
                }
            } else {
                System.out.println("Problem in ArtificialIntelligence.moveUnits()");
            }
        }
    }

    void spendMoneyAndMergeUnits() {
        for (int i = 0; i < this.gameController.fieldController.provinces.size(); i++) {
            Province province = (Province) this.gameController.fieldController.provinces.get(i);
            if (province.getColor() == this.color) {
                spendMoney(province);
                mergeUnits(province);
            }
        }
    }

    void moveAfkUnits() {
        updateUnitsReadyToMove();
        Iterator it = this.unitsReadyToMove.iterator();
        while (it.hasNext()) {
            Unit unit = (Unit) it.next();
            if (unit.isReadyToMove()) {
                Province province = this.gameController.getProvinceByHex(unit.currentHex);
                if (province.hexList.size() > 20) {
                    moveAfkUnit(province, unit);
                }
            }
        }
    }

    void moveAfkUnit(Province province, Unit unit) {
        ArrayList<Hex> moveZone = this.gameController.detectMoveZone(unit.currentHex, unit.strength, 4);
        excludeFriendlyUnitsFromMoveZone(moveZone);
        excludeFriendlyBuildingsFromMoveZone(moveZone);
        if (moveZone.size() != 0) {
            this.gameController.moveUnit(unit, (Hex) moveZone.get(this.random.nextInt(moveZone.size())), province);
        }
    }

    void mergeUnits(Province province) {
        for (int i = 0; i < province.hexList.size(); i++) {
            Hex hex = (Hex) province.hexList.get(i);
            if (hex.containsUnit() && hex.unit.isReadyToMove()) {
                tryToMergeWithSomeone(province, hex.unit);
            }
        }
    }

    private void tryToMergeWithSomeone(Province province, Unit unit) {
        ArrayList<Hex> moveZone = this.gameController.detectMoveZone(unit.currentHex, unit.strength, 4);
        if (moveZone.size() != 0) {
            Iterator it = moveZone.iterator();
            while (it.hasNext()) {
                Hex hex = (Hex) it.next();
                if (mergeConditions(province, unit, hex)) {
                    this.gameController.moveUnit(unit, hex, province);
                    return;
                }
            }
        }
    }

    protected boolean mergeConditions(Province province, Unit unit, Hex hex) {
        return hex.sameColor(unit.currentHex) && hex.containsUnit() && hex.unit.isReadyToMove() && unit != hex.unit && province.canAiAffordUnit(this.gameController.mergedUnitStrength(unit, hex.unit));
    }

    protected void spendMoney(Province province) {
        tryToBuildTowers(province);
        tryToBuildUnits(province);
    }

    void tryToBuildTowers(Province province) {
        while (province.hasMoneyForTower()) {
            Hex hex = findHexThatNeedsTower(province);
            if (hex != null) {
                this.gameController.fieldController.buildTower(province, hex);
            } else {
                return;
            }
        }
    }

    protected Hex findHexThatNeedsTower(Province province) {
        Iterator it = province.hexList.iterator();
        while (it.hasNext()) {
            Hex hex = (Hex) it.next();
            if (needTowerOnHex(hex)) {
                return hex;
            }
        }
        return null;
    }

    boolean needTowerOnHex(Hex hex) {
        if (hex.active && hex.isFree() && getPredictedDefenseGainByNewTower(hex) >= 5) {
            return true;
        }
        return false;
    }

    protected int getPredictedDefenseGainByNewTower(Hex hex) {
        int c = 0;
        if (hex.active && !hex.isDefendedByTower()) {
            c = 0 + 1;
        }
        for (int i = 0; i < 6; i++) {
            Hex adjHex = hex.getAdjacentHex(i);
            if (adjHex.active && hex.sameColor(adjHex) && !adjHex.isDefendedByTower()) {
                c++;
            }
            if (adjHex.containsTower()) {
                c--;
            }
        }
        return c;
    }

    protected void updateNearbyProvinces(Province srcProvince) {
        this.nearbyProvinces.clear();
        Iterator it = srcProvince.hexList.iterator();
        while (it.hasNext()) {
            Hex hex = (Hex) it.next();
            for (int i = 0; i < 6; i++) {
                checkToAddNearbyProvince(hex, hex.getAdjacentHex(i));
            }
        }
    }

    protected void updateNearbyProvinces(Hex srcHex) {
        this.nearbyProvinces.clear();
        for (int i = 0; i < 6; i++) {
            Hex adjacentHex = srcHex.getAdjacentHex(i);
            if (adjacentHex.active) {
                Hex adjacentHex2 = adjacentHex.getAdjacentHex(i);
                int j = i + 1;
                if (j >= 6) {
                    j = 0;
                }
                Hex adjacentHex3 = adjacentHex.getAdjacentHex(j);
                checkToAddNearbyProvince(srcHex, adjacentHex);
                checkToAddNearbyProvince(srcHex, adjacentHex2);
                checkToAddNearbyProvince(srcHex, adjacentHex3);
            }
        }
    }

    private void checkToAddNearbyProvince(Hex srcHex, Hex adjacentHex) {
        if (adjacentHex.active && !adjacentHex.isNeutral() && !adjacentHex.sameColor(srcHex)) {
            addProvinceToNearbyProvines(this.gameController.fieldController.getProvinceByHex(adjacentHex));
        }
    }

    private void addProvinceToNearbyProvines(Province province) {
        if (province != null && !this.nearbyProvinces.contains(province)) {
            this.nearbyProvinces.listIterator().add(province);
        }
    }

    boolean tryToBuiltUnitInsideProvince(Province province, int strength) {
        Iterator it = province.hexList.iterator();
        while (it.hasNext()) {
            Hex hex = (Hex) it.next();
            if (hex.nothingBlocksWayForUnit()) {
                this.gameController.fieldController.buildUnit(province, hex, strength);
                return true;
            }
        }
        return false;
    }

    boolean tryToAttackWithStrength(Province province, int strength) {
        ArrayList<Hex> attackableHexes = findAttackableHexes(province.getColor(), this.gameController.detectMoveZone(province.getCapital(), strength));
        if (attackableHexes.size() == 0) {
            return false;
        }
        this.gameController.fieldController.buildUnit(province, findMostAttractiveHex(attackableHexes, province, strength), strength);
        return true;
    }

    void tryToBuildUnitsOnPalms(Province province) {
        if (province.canAiAffordUnit(1)) {
            while (province.canBuildUnit(1)) {
                boolean killedPalm = false;
                Iterator it = this.gameController.detectMoveZone(province.getCapital(), 1).iterator();
                while (it.hasNext()) {
                    Hex hex = (Hex) it.next();
                    if (hex.objectInside == 2 && hex.sameColor(province)) {
                        this.gameController.fieldController.buildUnit(province, hex, 1);
                        killedPalm = true;
                    }
                }
                if (!killedPalm) {
                    return;
                }
            }
        }
    }

    void tryToBuildUnits(Province province) {
        tryToBuildUnitsOnPalms(province);
        int i = 1;
        while (i <= 4 && province.canAiAffordUnit(i)) {
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

    boolean checkToCleanSomeTrees(Unit unit, ArrayList<Hex> moveZone, Province province) {
        Iterator it = moveZone.iterator();
        while (it.hasNext()) {
            Hex hex = (Hex) it.next();
            if (hex.containsTree() && hex.sameColor(unit.currentHex)) {
                this.gameController.moveUnit(unit, hex, province);
                return true;
            }
        }
        return false;
    }

    boolean checkToCleanSomePalms(Unit unit, ArrayList<Hex> moveZone, Province province) {
        Iterator it = moveZone.iterator();
        while (it.hasNext()) {
            Hex hex = (Hex) it.next();
            if (hex.objectInside == 2 && hex.sameColor(unit.currentHex)) {
                this.gameController.moveUnit(unit, hex, province);
                return true;
            }
        }
        return false;
    }

    void decideAboutUnit(Unit unit, ArrayList<Hex> moveZone, Province province) {
        if (unit.strength > 2 || !checkToCleanSomePalms(unit, moveZone, province)) {
            ArrayList<Hex> attackableHexes = findAttackableHexes(unit.getColor(), moveZone);
            if (attackableHexes.size() > 0) {
                this.gameController.moveUnit(unit, findMostAttractiveHex(attackableHexes, province, unit.strength), province);
            } else if (!checkToCleanSomeTrees(unit, moveZone, province) && unit.currentHex.isInPerimeter()) {
                pushUnitToBetterDefense(unit, province);
            }
        }
    }

    boolean checkChance(double chance) {
        return this.random.nextDouble() < chance;
    }

    void pushUnitToBetterDefense(Unit unit, Province province) {
        for (int i = 0; i < 6; i++) {
            Hex adjHex = unit.currentHex.getAdjacentHex(i);
            if (adjHex.active && adjHex.sameColor(unit.currentHex) && adjHex.isFree() && adjHex.howManyEnemyHexesNear() == 0) {
                this.gameController.moveUnit(unit, adjHex, province);
                return;
            }
        }
    }

    int getAttackAllure(Hex hex, int color) {
        int c = 0;
        for (int i = 0; i < 6; i++) {
            Hex adjHex = hex.getAdjacentHex(i);
            if (adjHex.active && adjHex.sameColor(color)) {
                c++;
            }
        }
        return c;
    }

    Hex findHexAttractiveToBaron(ArrayList<Hex> attackableHexes, int strength) {
        Iterator it = attackableHexes.iterator();
        while (it.hasNext()) {
            Hex attackableHex = (Hex) it.next();
            if (attackableHex.objectInside == 4) {
                return attackableHex;
            }
            if (strength == 4 && attackableHex.objectInside == 7) {
                return attackableHex;
            }
        }
        it = attackableHexes.iterator();
        while (it.hasNext()) {
            attackableHex = (Hex) it.next();
            if (attackableHex.isDefendedByTower()) {
                return attackableHex;
            }
        }
        return null;
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

    ArrayList<Hex> findAttackableHexes(int attackerColor, ArrayList<Hex> moveZone) {
        this.tempResultList.clear();
        Iterator it = moveZone.iterator();
        while (it.hasNext()) {
            Hex hex = (Hex) it.next();
            if (hex.colorIndex != attackerColor) {
                this.tempResultList.add(hex);
            }
        }
        return this.tempResultList;
    }

    private void excludeFriendlyBuildingsFromMoveZone(ArrayList<Hex> moveZone) {
        this.junkList.clear();
        Iterator it = moveZone.iterator();
        while (it.hasNext()) {
            Hex hex = (Hex) it.next();
            if (hex.sameColor(this.color) && hex.containsBuilding()) {
                this.junkList.add(hex);
            }
        }
        moveZone.removeAll(this.junkList);
    }

    private void excludeFriendlyUnitsFromMoveZone(ArrayList<Hex> moveZone) {
        this.junkList.clear();
        Iterator it = moveZone.iterator();
        while (it.hasNext()) {
            Hex hex = (Hex) it.next();
            if (hex.sameColor(this.color) && hex.containsUnit()) {
                this.junkList.add(hex);
            }
        }
        moveZone.removeAll(this.junkList);
    }

    int numberOfFriendlyHexesNearby(Hex hex) {
        return hex.numberOfFriendlyHexesNearby();
    }

    int howManyUnitsInProvince(Province province) {
        int c = 0;
        Iterator it = province.hexList.iterator();
        while (it.hasNext()) {
            if (((Hex) it.next()).containsUnit()) {
                c++;
            }
        }
        return c;
    }
}
