package io.androidovshchik.antiyoy.menu.FireworksElement;

import com.badlogic.gdx.net.HttpStatus;
import io.androidovshchik.antiyoy.YioGdxGame;
import io.androidovshchik.antiyoy.factor_yio.FactorYio;
import io.androidovshchik.antiyoy.stuff.GraphicsYio;
import io.androidovshchik.antiyoy.stuff.PointYio;
import io.androidovshchik.antiyoy.stuff.RectangleYio;
import io.androidovshchik.antiyoy.stuff.Yio;
import io.androidovshchik.antiyoy.stuff.object_pool.ReusableYio;

public class FeParticle implements ReusableYio {
    public double aDelta;
    FactorYio appearFactor = new FactorYio();
    float gravity;
    int lifeEnergy;
    RectangleYio limits = new RectangleYio();
    public PointYio position = new PointYio();
    public float radius;
    PointYio speed = new PointYio();
    public double viewAngle;
    public float viewRadius;
    public int viewType;

    public void reset() {
        this.limits.set(0.0d, 0.0d, (double) GraphicsYio.width, (double) GraphicsYio.height);
        this.viewType = 0;
        this.position.set(0.0d, 0.0d);
        this.speed.set(0.0d, 0.0d);
        this.radius = 0.05f * GraphicsYio.width;
        this.lifeEnergy = HttpStatus.SC_MULTIPLE_CHOICES;
        this.gravity = this.radius / 125.0f;
        this.viewAngle = Yio.getRandomAngle();
        this.aDelta = ((2.0d * YioGdxGame.random.nextDouble()) - 1.0d) * 0.25d;
        this.viewRadius = 0.0f;
        this.appearFactor.setValues(0.0d, 0.0d);
        this.appearFactor.stopMoving();
    }

    void move() {
        this.appearFactor.move();
        updateViewRadius();
        movePosition();
        slowDown();
        applyLimits();
        moveAngle();
        moveLifeEnergy();
    }

    private void moveAngle() {
        this.aDelta *= 0.98d;
        this.viewAngle += this.aDelta;
    }

    private void moveLifeEnergy() {
        if (this.lifeEnergy > 0) {
            this.lifeEnergy--;
        } else {
            kill();
        }
    }

    void kill() {
        this.lifeEnergy = 0;
        destroy();
    }

    boolean isAlive() {
        return this.lifeEnergy > 0 || this.appearFactor.get() > 0.0f;
    }

    private void slowDown() {
        PointYio pointYio = this.speed;
        pointYio.f144x *= 0.99f;
        pointYio = this.speed;
        pointYio.f145y *= 0.99f;
    }

    private void applyLimits() {
        if (((double) this.position.f144x) > (this.limits.f146x + this.limits.width) - ((double) this.viewRadius)) {
            this.position.f144x = (float) ((this.limits.f146x + this.limits.width) - ((double) this.viewRadius));
            this.speed.f144x = -Math.abs(this.speed.f144x);
            this.aDelta *= 0.9d;
        }
        if (((double) this.position.f144x) < this.limits.f146x + ((double) this.viewRadius)) {
            this.position.f144x = (float) (this.limits.f146x + ((double) this.viewRadius));
            this.speed.f144x = Math.abs(this.speed.f144x);
            this.aDelta *= 0.9d;
        }
        if (((double) this.position.f145y) < this.limits.f147y + ((double) this.viewRadius)) {
            this.position.f145y = (float) (this.limits.f147y + ((double) this.viewRadius));
            this.speed.f145y = 0.6f * Math.abs(this.speed.f145y);
            this.lifeEnergy -= 5;
            this.aDelta *= 0.7d;
        }
    }

    void applyGravity(double gravityAngle) {
        this.speed.relocateRadial((double) this.gravity, gravityAngle);
    }

    private void movePosition() {
        PointYio pointYio = this.position;
        pointYio.f144x += this.speed.f144x;
        pointYio = this.position;
        pointYio.f145y += this.speed.f145y;
    }

    private void updateViewRadius() {
        this.viewRadius = this.appearFactor.get() * this.radius;
    }

    void appear() {
        this.appearFactor.appear(3, 1.0d);
    }

    void destroy() {
        this.appearFactor.destroy(1, 3.0d);
    }
}
