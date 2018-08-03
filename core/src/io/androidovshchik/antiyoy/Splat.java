package io.androidovshchik.antiyoy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Splat {
    float dx;
    float dy;
    float f88r;
    float speedMultiplier;
    final TextureRegion textureRegion;
    float wind;
    float f89x;
    float f90y;

    public Splat(TextureRegion textureRegion, float x, float y) {
        this.textureRegion = textureRegion;
        this.f89x = x;
        this.f90y = y;
    }

    void move() {
        this.f89x += this.dx;
        this.f90y += this.dy * this.speedMultiplier;
        this.dx += this.wind;
        if (Math.abs(this.dx) > 0.001f * ((float) Gdx.graphics.getWidth())) {
            this.wind = -this.wind;
        }
    }

    void set(float x, float y) {
        this.f89x = x;
        this.f90y = y;
    }

    void setSpeed(float sdx, float sdy) {
        this.dx = sdx;
        this.dy = sdy;
        this.wind = -0.01f * this.dx;
    }

    public void setRadius(float r) {
        this.f88r = r;
        this.speedMultiplier = (0.05f * ((float) Gdx.graphics.getHeight())) / r;
    }

    boolean isVisible() {
        return this.f90y < ((float) Gdx.graphics.getHeight()) + this.f88r;
    }
}
