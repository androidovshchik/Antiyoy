package io.androidovshchik.antiyoy.factor_yio;

public class MoveBehaviorSpringy extends MoveBehavior {
    void springyBounds(FactorYio fy) {
        if (fy.gravity > 0.0d && fy.f91f > 1.0d) {
            if (fy.dy > 0.1d) {
                fy.f91f = 0.999d;
                fy.dy *= -0.25d;
            } else {
                fy.f91f = 1.0d;
                stopMoving(fy);
            }
        }
        if (fy.gravity < 0.0d && fy.f91f < 0.0d) {
            if (fy.dy < -0.1d) {
                fy.f91f = 0.001d;
                fy.dy *= -0.25d;
                return;
            }
            fy.f91f = 0.0d;
            stopMoving(fy);
        }
    }

    void move(FactorYio fy) {
        if (needsToMove(fy)) {
            fy.f91f += fy.speedMultiplier * fy.dy;
            fy.dy += fy.gravity;
        }
        springyBounds(fy);
    }
}
