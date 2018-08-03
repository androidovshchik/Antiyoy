package io.androidovshchik.antiyoy.gameplay.diplomacy;

import yio.tro.antiyoy.stuff.object_pool.ReusableYio;

public class DiplomaticContract implements ReusableYio {
    public static final int DURATION_FRIEND = 12;
    public static final int DURATION_PIECE = 9;
    public static final int DURATION_TRAITOR = 20;
    public static final int TYPE_BLACK_MARK = 2;
    public static final int TYPE_FRIENDSHIP = 0;
    public static final int TYPE_PIECE = 1;
    public static final int TYPE_TRAITOR = 3;
    int dotations;
    int expireCountDown;
    DiplomaticEntity one;
    DiplomaticEntity two;
    int type;

    public void reset() {
        this.one = null;
        this.two = null;
        this.type = -1;
        this.dotations = 0;
        this.expireCountDown = 0;
    }

    void onFirstPlayerTurnEnded() {
        if (this.expireCountDown > 0) {
            this.expireCountDown--;
            if (this.expireCountDown == 0) {
                onCountDownReachedZero();
            }
        }
    }

    public static int getDurationByType(int contractType) {
        switch (contractType) {
            case 0:
                return 12;
            case 1:
                return 9;
            case 3:
                return 20;
            default:
                return -1;
        }
    }

    private void onCountDownReachedZero() {
        this.one.diplomacyManager.onContractExpired(this);
    }

    public boolean contains(DiplomaticEntity diplomaticEntity) {
        return this.one == diplomaticEntity || this.two == diplomaticEntity;
    }

    public boolean equals(DiplomaticEntity one, DiplomaticEntity two, int type) {
        if ((type == -1 || this.type == type) && contains(one) && contains(two)) {
            return true;
        }
        return false;
    }

    public String getDotationsStringFromEntityPerspective(DiplomaticEntity entity) {
        int dotations = getDotationsFromEntityPerspective(entity);
        if (dotations > 0) {
            return "+" + dotations;
        }
        return "" + dotations;
    }

    public int getDotationsFromEntityPerspective(DiplomaticEntity entity) {
        if (entity == this.one) {
            return -this.dotations;
        }
        if (entity == this.two) {
            return this.dotations;
        }
        return 0;
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

    public String toString() {
        return "[Contract: type=" + this.type + " one=" + this.one + " two=" + this.two + " dotations=" + this.dotations + "]";
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

    public void setDotations(int dotations) {
        this.dotations = dotations;
    }

    public void setExpireCountDown(int expireCountDown) {
        this.expireCountDown = expireCountDown;
    }

    public int getExpireCountDown() {
        return this.expireCountDown;
    }
}
