package io.androidovshchik.antiyoy.gameplay;

import io.androidovshchik.antiyoy.factor_yio.FactorYio;
import io.androidovshchik.antiyoy.gameplay.rules.GameRules;
import io.androidovshchik.antiyoy.stuff.PointYio;
import io.androidovshchik.antiyoy.stuff.object_pool.ReusableYio;

public class Hex implements ReusableYio {
    public boolean active;
    public FactorYio animFactor;
    long animStartTime;
    boolean blockToTreeFromExpanding;
    boolean canContainObjects;
    public boolean changingColor;
    public int colorIndex;
    float cos60;
    FieldController fieldController;
    public PointYio fieldPos;
    public boolean flag;
    private GameController gameController;
    public boolean genFlag;
    public int genPotential;
    public boolean ignoreTouch;
    public boolean inMoveZone;
    public int index1;
    public int index2;
    public int lastColorIndex;
    public int moveZoneNumber;
    public int objectInside;
    public PointYio pos;
    public boolean selected;
    public FactorYio selectionFactor;
    float sin60;
    public Unit unit;
    public int viewDiversityIndex;

    public Hex(int index1, int index2, PointYio fieldPos, FieldController fieldController) {
        this.index1 = index1;
        this.index2 = index2;
        this.fieldPos = fieldPos;
        this.fieldController = fieldController;
        if (fieldController != null) {
            this.gameController = fieldController.gameController;
            this.active = false;
            this.pos = new PointYio();
            this.cos60 = (float) Math.cos(1.0471975511965976d);
            this.sin60 = (float) Math.sin(1.0471975511965976d);
            this.animFactor = new FactorYio();
            this.selectionFactor = new FactorYio();
            this.unit = null;
            this.viewDiversityIndex = (((index1 * 101) * index2) + (index2 * 7)) % 3;
            this.canContainObjects = true;
            updatePos();
        }
    }

    public void reset() {
    }

    void updateCanContainsObjects() {
        this.canContainObjects = this.fieldController.isPointInsideLevelBoundsHorizontally(this.pos);
    }

    void updatePos() {
        this.pos.f144x = this.fieldPos.f144x + ((this.fieldController.hexStep2 * ((float) this.index2)) * this.sin60);
        this.pos.f145y = (this.fieldPos.f145y + (this.fieldController.hexStep1 * ((float) this.index1))) + ((this.fieldController.hexStep2 * ((float) this.index2)) * this.cos60);
    }

    boolean isInProvince() {
        for (int i = 0; i < 6; i++) {
            Hex adjHex = getAdjacentHex(i);
            if (adjHex.active && adjHex.sameColor(this)) {
                return true;
            }
        }
        return false;
    }

    public boolean isNearWater() {
        if (!this.active) {
            return false;
        }
        for (int i = 0; i < 6; i++) {
            if (!this.gameController.fieldController.adjacentHex(this, i).active) {
                return true;
            }
        }
        return false;
    }

    public void setColorIndex(int colorIndex) {
        this.lastColorIndex = this.colorIndex;
        this.colorIndex = colorIndex;
        this.animFactor.appear(1, 1.0d);
        this.animFactor.setValues(0.0d, 0.0d);
    }

    void move() {
        this.animFactor.move();
        if (this.selected) {
            this.selectionFactor.move();
        }
    }

    void addUnit(int strength) {
        this.unit = new Unit(this.gameController, this, strength);
        this.gameController.unitList.add(this.unit);
        this.gameController.matchStatistics.unitWasProduced();
    }

    public boolean isFree() {
        return (containsObject() || containsUnit()) ? false : true;
    }

    public boolean nothingBlocksWayForUnit() {
        return (containsUnit() || containsBuilding()) ? false : true;
    }

    public boolean containsTree() {
        return this.objectInside == 2 || this.objectInside == 1;
    }

    public boolean containsObject() {
        return this.objectInside > 0;
    }

    public boolean containsTower() {
        return this.objectInside == 4 || this.objectInside == 7;
    }

    public boolean containsBuilding() {
        return this.objectInside == 3 || this.objectInside == 4 || this.objectInside == 6 || this.objectInside == 7;
    }

    public Hex getSnapshotCopy() {
        Hex record = new Hex(this.index1, this.index2, this.fieldPos, this.fieldController);
        record.active = this.active;
        record.colorIndex = this.colorIndex;
        record.objectInside = this.objectInside;
        record.selected = this.selected;
        if (this.unit != null) {
            record.unit = this.unit.getSnapshotCopy();
        }
        return record;
    }

    public void setObjectInside(int objectInside) {
        this.objectInside = objectInside;
    }

    public boolean containsUnit() {
        return this.unit != null;
    }

    public int numberOfActiveHexesNearby() {
        return numberOfFriendlyHexesNearby() + howManyEnemyHexesNear();
    }

    public boolean noProvincesNearby() {
        if (numberOfFriendlyHexesNearby() > 0) {
            return false;
        }
        for (int i = 0; i < 6; i++) {
            Hex adjHex = getAdjacentHex(i);
            if (adjHex.active && adjHex.numberOfFriendlyHexesNearby() > 0) {
                return false;
            }
        }
        return true;
    }

    public int numberOfFriendlyHexesNearby() {
        int c = 0;
        for (int i = 0; i < 6; i++) {
            Hex adjHex = getAdjacentHex(i);
            if (adjHex.colorIndex != FieldController.NEUTRAL_LANDS_INDEX && adjHex.active && adjHex.sameColor(this)) {
                c++;
            }
        }
        return c;
    }

    public int getDefenseNumber() {
        return getDefenseNumber(null);
    }

    public int getDefenseNumber(Unit ignoreUnit) {
        int defenseNumber = 0;
        if (this.objectInside == 3) {
            defenseNumber = Math.max(0, 1);
        }
        if (this.objectInside == 4) {
            defenseNumber = Math.max(defenseNumber, 2);
        }
        if (this.objectInside == 7) {
            defenseNumber = Math.max(defenseNumber, 3);
        }
        if (containsUnit() && this.unit != ignoreUnit) {
            defenseNumber = Math.max(defenseNumber, this.unit.strength);
        }
        for (int i = 0; i < 6; i++) {
            Hex neighbour = getAdjacentHex(i);
            if (neighbour.active && neighbour.sameColor(this)) {
                if (neighbour.objectInside == 3) {
                    defenseNumber = Math.max(defenseNumber, 1);
                }
                if (neighbour.objectInside == 4) {
                    defenseNumber = Math.max(defenseNumber, 2);
                }
                if (neighbour.objectInside == 7) {
                    defenseNumber = Math.max(defenseNumber, 3);
                }
                if (neighbour.containsUnit() && neighbour.unit != ignoreUnit) {
                    defenseNumber = Math.max(defenseNumber, neighbour.unit.strength);
                }
            }
        }
        return defenseNumber;
    }

    public boolean isNearHouse() {
        for (int i = 0; i < 6; i++) {
            Hex adjHex = getAdjacentHex(i);
            if (adjHex.active && adjHex.sameColor(this) && adjHex.objectInside == 3) {
                return true;
            }
        }
        return false;
    }

    public void forAdjacentHexes(HexActionPerformer hexActionPerformer) {
        for (int i = 0; i < 6; i++) {
            hexActionPerformer.doAction(this, getAdjacentHex(i));
        }
    }

    public boolean isInPerimeter() {
        for (int i = 0; i < 6; i++) {
            Hex adjHex = getAdjacentHex(i);
            if (adjHex.active && !adjHex.sameColor(this) && adjHex.isInProvince()) {
                return true;
            }
        }
        return false;
    }

    public boolean hasThisObjectNearby(int objectIndex) {
        if (this.objectInside == objectIndex) {
            return true;
        }
        for (int i = 0; i < 6; i++) {
            Hex adjHex = getAdjacentHex(i);
            if (adjHex.colorIndex == this.colorIndex && adjHex.active && adjHex.objectInside == objectIndex) {
                return true;
            }
        }
        return false;
    }

    public boolean hasPalmReadyToExpandNearby() {
        for (int i = 0; i < 6; i++) {
            Hex adjHex = getAdjacentHex(i);
            if (!adjHex.blockToTreeFromExpanding && adjHex.objectInside == 2) {
                return true;
            }
        }
        return false;
    }

    public boolean hasPineReadyToExpandNearby() {
        for (int i = 0; i < 6; i++) {
            Hex adjHex = getAdjacentHex(i);
            if (!adjHex.blockToTreeFromExpanding && adjHex.objectInside == 1) {
                return true;
            }
        }
        return false;
    }

    public boolean sameColor(int color) {
        return this.colorIndex == color;
    }

    public boolean sameColor(Province province) {
        return this.colorIndex == province.getColor();
    }

    public boolean sameColor(Hex hex) {
        return this.colorIndex == hex.colorIndex;
    }

    public int howManyEnemyHexesNear() {
        int c = 0;
        for (int i = 0; i < 6; i++) {
            Hex adjHex = getAdjacentHex(i);
            if (adjHex.active && !adjHex.sameColor(this)) {
                c++;
            }
        }
        return c;
    }

    public void set(Hex hex) {
        this.index1 = hex.index1;
        this.index2 = hex.index2;
    }

    public boolean equals(Hex hex) {
        return hex.index1 == this.index1 && hex.index2 == this.index2;
    }

    public boolean isDefendedByTower() {
        for (int i = 0; i < 6; i++) {
            Hex adjHex = getAdjacentHex(i);
            if (adjHex.active && adjHex.sameColor(this) && adjHex.containsTower()) {
                return true;
            }
        }
        return false;
    }

    public Hex getAdjacentHex(int direction) {
        return this.gameController.fieldController.adjacentHex(this, direction);
    }

    public void setIgnoreTouch(boolean ignoreTouch) {
        this.ignoreTouch = ignoreTouch;
    }

    public boolean isEmptyHex() {
        return this.index1 == -1 && this.index2 == -1;
    }

    void select() {
        if (!this.selected) {
            this.selected = true;
            this.selectionFactor.setValues(0.0d, 0.0d);
            this.selectionFactor.appear(3, 1.5d);
        }
    }

    public boolean isSelected() {
        return this.selected;
    }

    public PointYio getPos() {
        return this.pos;
    }

    public boolean isNeutral() {
        if (!GameRules.slayRules && this.colorIndex == FieldController.NEUTRAL_LANDS_INDEX) {
            return true;
        }
        return false;
    }

    public boolean canBeAttackedBy(Unit unit) {
        boolean canUnitAttackHex = this.gameController.canUnitAttackHex(unit.strength, unit.getColor(), this);
        if (!GameRules.replayMode) {
            return canUnitAttackHex;
        }
        if (!canUnitAttackHex) {
            System.out.println("Problem in Hex.canBeAttackedBy(): " + this);
        }
        return true;
    }

    public boolean isInMoveZone() {
        return this.inMoveZone;
    }

    void close() {
        this.gameController = null;
    }

    public String toString() {
        return "[Hex: c" + this.colorIndex + " (" + this.index1 + ", " + this.index2 + ")]";
    }
}
