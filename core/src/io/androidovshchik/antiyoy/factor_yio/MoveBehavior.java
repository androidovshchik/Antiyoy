package io.androidovshchik.antiyoy.factor_yio;

abstract class MoveBehavior {
    static final MoveBehavior moveBehaviorApproach = new MoveBehaviorApproach();
    static final MoveBehavior moveBehaviorLighty = new MoveBehaviorLighty();
    static final MoveBehavior moveBehaviorMaterial = new MoveBehaviorMaterial();
    static final MoveBehavior moveBehaviorPlayful = new MoveBehaviorPlayful();
    static final MoveBehavior moveBehaviorSimple = new MoveBehaviorSimple();

    abstract void move(FactorYio factorYio);

    boolean needsToMove(FactorYio fy) {
        if (fy.gravity > 0.0d && fy.f91f < 1.0d) {
            return true;
        }
        if (fy.gravity < 0.0d && fy.f91f > 0.0d) {
            return true;
        }
        if (fy.gravity == 0.0d) {
            if (fy.dy > 0.0d && fy.f91f < 1.0d) {
                return true;
            }
            if (fy.dy < 0.0d && fy.f91f > 0.0d) {
                return true;
            }
        }
        return false;
    }

    void strictBounds(FactorYio fy) {
        if (fy.gravity > 0.0d && fy.f91f > 1.0d) {
            fy.f91f = 1.0d;
        }
        if (fy.gravity < 0.0d && fy.f91f < 0.0d) {
            fy.f91f = 0.0d;
        }
    }

    void stopMoving(FactorYio fy) {
        fy.dy = 0.0d;
        fy.gravity = 0.0d;
    }

    void alertAboutSpawning(FactorYio fy) {
    }

    void alertAboutDestroying(FactorYio fy) {
    }
}
