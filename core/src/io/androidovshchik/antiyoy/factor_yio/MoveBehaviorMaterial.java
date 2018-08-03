package io.androidovshchik.antiyoy.factor_yio;

public class MoveBehaviorMaterial extends MoveBehavior {
    void move(FactorYio fy) {
        if (fy.gravity > 0.0d) {
            moveSpawn(fy);
        } else {
            moveDestroy(fy);
        }
    }

    private void moveDestroy(FactorYio fy) {
        if (fy.f91f > 0.99d) {
            fy.f91f = 0.99d;
        }
        if (fy.f91f < 0.01d) {
            fy.f91f = 0.0d;
        }
        if (fy.f91f > 0.5d) {
            fy.f91f -= (fy.speedMultiplier * 0.05d) * (1.0d - fy.f91f);
        } else {
            fy.f91f -= (fy.speedMultiplier * 0.05d) * fy.f91f;
        }
    }

    private void moveSpawn(FactorYio fy) {
        if (fy.f91f < 0.01d) {
            fy.f91f = 0.01d;
        }
        if (fy.f91f > 0.99d) {
            fy.f91f = 1.0d;
        }
        if (fy.f91f < 0.5d) {
            fy.f91f += (fy.speedMultiplier * 0.05d) * fy.f91f;
        } else {
            fy.f91f += (fy.speedMultiplier * 0.05d) * (1.0d - fy.f91f);
        }
    }

    void alertAboutSpawning(FactorYio fy) {
        fy.speedMultiplier *= 15.0d;
    }

    void alertAboutDestroying(FactorYio fy) {
        fy.speedMultiplier *= 15.0d;
    }
}
