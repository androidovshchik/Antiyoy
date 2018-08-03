package io.androidovshchik.antiyoy;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

class Bubble {
    private float diam;
    private float dr;
    private float dx;
    private float dy;
    private float f85r;
    private TextureRegion textureRegion;
    private float f86x;
    private float f87y;

    public Bubble() {
        setPos(0.0f, 0.0f);
        setSpeed(0.0f, 0.0f);
        setRadius(0.0f, 0.0f);
    }

    void move() {
        this.f86x += this.dx;
        this.f87y += this.dy;
        this.dx = (float) (((double) this.dx) * 0.99d);
        this.dy = (float) (((double) this.dy) * 0.99d);
        this.f85r += this.dr;
        if (this.f85r < 0.0f) {
            this.f85r = 0.0f;
        }
        this.diam = 2.0f * this.f85r;
    }

    private void setPos(float x, float y) {
        this.f86x = x;
        this.f87y = y;
    }

    private void setSpeed(float dx, float dy) {
        this.dx = dx;
        this.dy = dy;
    }

    private void setRadius(float r, float dr) {
        this.f85r = r;
        this.dr = dr;
        this.diam = 2.0f * r;
    }

    void limitByWalls(float rightLim) {
        if (this.f86x < 0.0f) {
            this.f86x = 0.0f;
            this.dx = -this.dx;
        }
        if (this.f86x > rightLim) {
            this.f86x = rightLim;
            this.dx = -this.dx;
        }
    }

    void gravity(double gravity) {
        this.dy = (float) (((double) this.dy) - gravity);
    }

    boolean isVisible() {
        return this.f85r > 1.0f;
    }

    public void setTextureRegion(TextureRegion textureRegion) {
        this.textureRegion = textureRegion;
    }
}
