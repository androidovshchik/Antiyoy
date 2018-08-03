package io.androidovshchik.antiyoy.gameplay.user_levels;

public abstract class AbstractUserLevel {
    public abstract String getAuthor();

    public abstract String getFullLevelString();

    public abstract String getKey();

    public abstract String getMapName();

    public int getColorOffset() {
        return 0;
    }

    public boolean getFogOfWar() {
        return false;
    }
}
