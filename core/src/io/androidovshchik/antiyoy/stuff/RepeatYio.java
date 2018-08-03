package io.androidovshchik.antiyoy.stuff;

import yio.tro.antiyoy.YioGdxGame;

public abstract class RepeatYio<ProviderType> {
    int countDown;
    int frequency;
    protected ProviderType parent;

    public abstract void performAction();

    public RepeatYio(ProviderType parent, int frequency) {
        this(parent, frequency, YioGdxGame.random.nextInt(Math.max(1, frequency)));
    }

    public RepeatYio(ProviderType parent, int frequency, int defCount) {
        this.parent = parent;
        this.frequency = frequency;
        this.countDown = defCount;
    }

    public void move() {
        if (this.countDown <= 0) {
            this.countDown = this.frequency;
            performAction();
            return;
        }
        this.countDown--;
    }

    public void move(int speed) {
        if (this.countDown <= 0) {
            this.countDown = this.frequency;
            performAction();
            return;
        }
        this.countDown -= speed;
    }

    public void setCountDown(int countDown) {
        this.countDown = countDown;
    }

    public int getCountDown() {
        return this.countDown;
    }

    public int getFrequency() {
        return this.frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }
}
