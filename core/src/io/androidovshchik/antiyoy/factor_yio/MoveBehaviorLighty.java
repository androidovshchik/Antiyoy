package io.androidovshchik.antiyoy.factor_yio;

public class MoveBehaviorLighty extends MoveBehavior {
    void move(FactorYio fy) {
        if (needsToMove(fy)) {
            fy.f91f += fy.speedMultiplier * fy.dy;
            fy.dy += fy.gravity;
        }
        strictBounds(fy);
    }
}
