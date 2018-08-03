package io.androidovshchik.antiyoy.gameplay.diplomacy;

import io.androidovshchik.antiyoy.stuff.object_pool.ReusableYio;

public class DiplomaticCooldown implements ReusableYio {
    public static final int TYPE_STOP_WAR = 0;
    public int counter;
    public DiplomaticEntity one;
    public DiplomaticEntity two;
    public int type;

    public DiplomaticCooldown() {
        reset();
    }

    public String toString() {
        return "[Cooldown: type: " + this.type + " other: " + this.counter + " " + getOneColor() + " " + getTwoColor() + "]";
    }

    public void decreaseCounter() {
        if (this.counter > 0) {
            this.counter--;
        }
    }

    public boolean isReady() {
        return this.counter == 0;
    }

    public boolean contains(DiplomaticEntity entity) {
        return entity == this.one || entity == this.two;
    }

    public int getOneColor() {
        if (this.one == null) {
            return -1;
        }
        return this.one.color;
    }

    public int getTwoColor() {
        if (this.two == null) {
            return -1;
        }
        return this.two.color;
    }

    public void reset() {
        this.type = -1;
        this.counter = 0;
        this.one = null;
        this.two = null;
    }

    public void setOne(DiplomaticEntity one) {
        this.one = one;
    }

    public void setTwo(DiplomaticEntity two) {
        this.two = two;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }
}
