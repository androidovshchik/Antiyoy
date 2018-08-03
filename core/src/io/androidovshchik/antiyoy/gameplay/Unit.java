package io.androidovshchik.antiyoy.gameplay;

import java.util.ArrayList;
import java.util.Iterator;
import yio.tro.antiyoy.factor_yio.FactorYio;
import yio.tro.antiyoy.gameplay.rules.GameRules;
import yio.tro.antiyoy.stuff.PointYio;

public class Unit {
    public Hex currentHex;
    public final PointYio currentPos;
    final GameController gameController;
    public float jumpDy;
    public float jumpGravity;
    public float jumpPos;
    public float jumpStartingImpulse;
    public Hex lastHex;
    public final FactorYio moveFactor = new FactorYio();
    boolean readyToMove;
    public int strength;

    public Unit(GameController gameController, Hex currentHex, int strength) {
        this.gameController = gameController;
        this.currentHex = currentHex;
        this.strength = strength;
        this.moveFactor.setValues(1.0d, 0.0d);
        this.lastHex = currentHex;
        this.jumpStartingImpulse = 0.015f;
        this.currentPos = new PointYio();
        updateCurrentPos();
    }

    boolean canMoveToFriendlyHex(Hex hex) {
        if (hex == this.currentHex || hex.containsBuilding()) {
            return false;
        }
        if (!hex.containsUnit() || this.gameController.ruleset.canMergeUnits(this, hex.unit)) {
            return true;
        }
        return false;
    }

    boolean moveToHex(Hex destinationHex) {
        if (destinationHex.sameColor(this.currentHex) && destinationHex.containsBuilding()) {
            return false;
        }
        this.gameController.ruleset.onUnitMoveToHex(this, destinationHex);
        if (destinationHex.containsObject()) {
            this.gameController.cleanOutHex(destinationHex);
            this.gameController.updateCacheOnceAfterSomeTime();
        }
        stopJumping();
        setReadyToMove(false);
        this.lastHex = this.currentHex;
        this.currentHex = destinationHex;
        this.moveFactor.setValues(0.0d, 0.0d);
        this.moveFactor.appear(1, 4.0d);
        this.lastHex.unit = null;
        destinationHex.unit = this;
        return true;
    }

    public int getColor() {
        return this.currentHex.colorIndex;
    }

    void updateCurrentPos() {
        this.currentPos.f144x = this.lastHex.pos.f144x + (this.moveFactor.get() * (this.currentHex.pos.f144x - this.lastHex.pos.f144x));
        this.currentPos.f145y = this.lastHex.pos.f145y + (this.moveFactor.get() * (this.currentHex.pos.f145y - this.lastHex.pos.f145y));
    }

    public void setReadyToMove(boolean readyToMove) {
        this.readyToMove = readyToMove;
    }

    public boolean isReadyToMove() {
        return this.readyToMove;
    }

    public Unit getSnapshotCopy() {
        Unit copy = new Unit(this.gameController, this.currentHex, this.strength);
        copy.readyToMove = this.readyToMove;
        return copy;
    }

    public void marchToHex(Hex toWhere, Province province) {
        if (toWhere != this.currentHex) {
            ArrayList<Hex> moveZone = this.gameController.detectMoveZone(this.currentHex, this.strength, 4);
            if (moveZone.size() != 0) {
                double minDistance = (double) FieldController.distanceBetweenHexes((Hex) moveZone.get(0), toWhere);
                Hex closestHex = (Hex) moveZone.get(0);
                Iterator it = moveZone.iterator();
                while (it.hasNext()) {
                    Hex hex = (Hex) it.next();
                    if (hex.sameColor(this.currentHex) && hex.nothingBlocksWayForUnit()) {
                        double currentDistance = (double) FieldController.distanceBetweenHexes(toWhere, hex);
                        if (currentDistance < minDistance) {
                            minDistance = currentDistance;
                            closestHex = hex;
                        }
                    }
                }
                if (closestHex != null && closestHex != this.currentHex) {
                    this.gameController.moveUnit(this, closestHex, province);
                }
            }
        }
    }

    void startJumping() {
        if (!GameRules.replayMode) {
            this.jumpPos = 0.0f;
            this.jumpDy = this.jumpStartingImpulse;
            this.jumpGravity = 0.001f;
        }
    }

    public void stopJumping() {
        this.jumpPos = 0.0f;
        this.jumpDy = 0.0f;
        this.jumpGravity = 0.0f;
    }

    void move() {
        this.moveFactor.move();
        updateCurrentPos();
    }

    void moveJumpAnim() {
        this.jumpDy -= this.jumpGravity;
        this.jumpPos += this.jumpDy;
        if (this.jumpPos < 0.0f) {
            this.jumpPos = 0.0f;
            this.jumpDy = this.jumpStartingImpulse;
        }
    }
}
