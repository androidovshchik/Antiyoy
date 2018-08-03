package io.androidovshchik.antiyoy.factor_yio;

public class MoveBehaviorApproach extends MoveBehavior {
    void alertAboutSpawning(FactorYio fy) {
        super.alertAboutSpawning(fy);
        fy.speedMultiplier /= 0.3d;
    }

    boolean needsToMove(FactorYio fy) {
        if (fy.gravity > 0.0d && fy.f91f < 1.0d) {
            return true;
        }
        if (fy.gravity >= 0.0d || fy.f91f <= 0.0d) {
            return false;
        }
        return true;
    }

    void move(FactorYio fy) {
        if (!needsToMove(fy)) {
            return;
        }
        if (fy.gravity > 0.0d) {
            fy.f91f += Math.max((fy.speedMultiplier * 0.15d) * (1.0d - fy.f91f), 0.01d);
            if (fy.f91f > 0.99d) {
                fy.f91f = 1.0d;
                return;
            }
            return;
        }
        fy.f91f += Math.min((fy.speedMultiplier * 0.15d) * (0.0d - fy.f91f), -0.01d);
        if (fy.f91f < 0.01d) {
            fy.f91f = 0.0d;
        }
    }
}
