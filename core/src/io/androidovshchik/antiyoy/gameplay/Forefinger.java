package io.androidovshchik.antiyoy.gameplay;

import io.androidovshchik.antiyoy.factor_yio.FactorYio;
import io.androidovshchik.antiyoy.stuff.GraphicsYio;
import io.androidovshchik.antiyoy.stuff.PointYio;

public class Forefinger {
    public static final int POINTING_TO_HEX = 0;
    public static final int POINTING_TO_MENU = 1;
    public PointYio animPos;
    private final GameController gameController;
    float f95h;
    private final Unit jumpingUnit;
    public PointYio pointPos;
    private Hex pointedHex;
    private int pointingTo;
    private double rotation;
    private final FactorYio sizeFactor = new FactorYio();
    float f96w;

    public Forefinger(GameController gameController) {
        this.gameController = gameController;
        this.jumpingUnit = new Unit(gameController, gameController.fieldController.emptyHex, 0);
        this.jumpingUnit.startJumping();
        Unit unit = this.jumpingUnit;
        unit.jumpGravity /= 2.0f;
        this.pointPos = new PointYio();
        this.animPos = new PointYio();
        this.f96w = GraphicsYio.width;
        this.f95h = GraphicsYio.height;
    }

    void move() {
        this.jumpingUnit.moveJumpAnim();
        if (this.sizeFactor.hasToMove()) {
            this.sizeFactor.move();
        }
        this.animPos.set(((double) this.pointPos.f144x) + (((((double) this.f96w) * 0.05d) * ((double) this.jumpingUnit.jumpPos)) * Math.cos(this.rotation - 1.5707963267948966d)), ((double) this.pointPos.f145y) + (((((double) this.f96w) * 0.05d) * ((double) this.jumpingUnit.jumpPos)) * Math.sin(this.rotation - 1.5707963267948966d)));
    }

    private void beginSpawnAnimation() {
        this.sizeFactor.setValues(0.0d, 0.0d);
        this.sizeFactor.appear(4, 2.0d);
    }

    public float getAlpha() {
        return this.sizeFactor.get() <= 1.0f ? this.sizeFactor.get() : 1.0f;
    }

    void hide() {
        this.sizeFactor.destroy(1, 3.0d);
    }

    public boolean isPointingToHex() {
        return this.pointingTo == 0;
    }

    boolean isPointingToButton() {
        return this.pointingTo == 1;
    }

    public float getSize() {
        return (this.sizeFactor.get() * 0.5f) + 0.5f;
    }

    void setPointTo(Hex hex) {
        this.pointedHex = hex;
        this.pointingTo = 0;
        beginSpawnAnimation();
        this.pointPos.set(((double) hex.pos.f144x) + (((double) this.f96w) * 0.035d), ((double) hex.pos.f145y) - (((double) this.f96w) * 0.035d));
        this.rotation = 0.5d;
    }

    void setPointTo(double x, double y, double rotation) {
        this.rotation = rotation;
        this.pointingTo = 1;
        beginSpawnAnimation();
        this.pointPos.set(x, y);
    }

    public double getRotation() {
        return this.rotation;
    }

    public PointYio getPointPos() {
        return this.pointPos;
    }
}
