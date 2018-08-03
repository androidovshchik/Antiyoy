package io.androidovshchik.antiyoy.factor_yio;

class MoveBehaviorSimple extends MoveBehavior {
    void alertAboutSpawning(FactorYio fy) {
        super.alertAboutSpawning(fy);
        fy.speedMultiplier *= 20.0d;
    }

    void alertAboutDestroying(FactorYio fy) {
        super.alertAboutDestroying(fy);
        fy.speedMultiplier *= 20.0d;
    }

    boolean needsToMove(FactorYio fy) {
        if (fy.dy >= 0.0d && fy.f91f < 1.0d) {
            return true;
        }
        if (fy.dy > 0.0d || fy.f91f <= 0.0d) {
            return false;
        }
        return true;
    }

    void strictBounds(FactorYio fy) {
        if (fy.dy > 0.0d && fy.f91f > 1.0d) {
            fy.f91f = 1.0d;
        }
        if (fy.dy < 0.0d && fy.f91f < 0.0d) {
            fy.f91f = 0.0d;
        }
    }

    void move(FactorYio fy) {
        if (fy.dy == 0.0d) {
            fy.dy = fy.gravity;
        }
        if (needsToMove(fy)) {
            fy.f91f += fy.speedMultiplier * fy.dy;
        }
        strictBounds(fy);
    }
}
