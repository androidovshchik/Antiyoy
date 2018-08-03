package io.androidovshchik.antiyoy.factor_yio;

public class FactorYio {
    double dy;
    double f91f;
    double gravity;
    boolean itsTimeToStop;
    MoveBehavior moveBehavior = MoveBehavior.moveBehaviorLighty;
    double speedMultiplier;

    public void move() {
        this.moveBehavior.move(this);
    }

    public void appear(int moveMode, double speed) {
        setMoveBehaviorByMoveMode(moveMode);
        this.gravity = 0.01d;
        this.speedMultiplier = 0.3d * speed;
        this.moveBehavior.alertAboutSpawning(this);
    }

    public void destroy(int moveMode, double speed) {
        setMoveBehaviorByMoveMode(moveMode);
        this.gravity = -0.01d;
        this.speedMultiplier = 0.3d * speed;
        this.moveBehavior.alertAboutDestroying(this);
    }

    private MoveBehavior getMoveBehaviorByIndex(int index) {
        switch (index) {
            case 1:
                return MoveBehavior.moveBehaviorLighty;
            case 2:
                return MoveBehavior.moveBehaviorMaterial;
            case 3:
                return MoveBehavior.moveBehaviorApproach;
            case 4:
                return MoveBehavior.moveBehaviorPlayful;
            default:
                return MoveBehavior.moveBehaviorSimple;
        }
    }

    private void setMoveBehaviorByMoveMode(int moveMode) {
        this.moveBehavior = getMoveBehaviorByIndex(moveMode);
    }

    public void setValues(double f, double dy) {
        this.f91f = f;
        this.dy = dy;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }

    public double getDy() {
        return this.dy;
    }

    public double getGravity() {
        return this.gravity;
    }

    public void stopMoving() {
        this.moveBehavior.stopMoving(this);
    }

    public boolean hasToMove() {
        return this.moveBehavior.needsToMove(this);
    }

    public float get() {
        return (float) this.f91f;
    }
}
