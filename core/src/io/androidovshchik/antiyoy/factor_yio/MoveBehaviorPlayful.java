package io.androidovshchik.antiyoy.factor_yio;

public class MoveBehaviorPlayful extends MoveBehavior {
    void move(FactorYio fy) {
        if (!fy.itsTimeToStop) {
            if (fy.f91f < 1.0d) {
                fy.f91f += fy.speedMultiplier * fy.dy;
                fy.dy += fy.gravity;
                if (fy.f91f > 1.0d) {
                    fy.speedMultiplier /= 3.0d;
                }
            } else {
                fy.f91f += fy.speedMultiplier * fy.dy;
                fy.dy -= fy.gravity * 3.0d;
            }
            if (fy.dy < 0.0d && fy.f91f < 1.0d) {
                fy.f91f = 1.0d;
                fy.itsTimeToStop = true;
            }
        }
    }

    void alertAboutSpawning(FactorYio fy) {
        super.alertAboutSpawning(fy);
        fy.speedMultiplier *= 2.0d;
        fy.itsTimeToStop = false;
    }

    boolean needsToMove(FactorYio fy) {
        return !fy.itsTimeToStop;
    }
}
