package io.androidovshchik.antiyoy.gameplay;

public class MatchStatistics {
    public int moneySpent;
    public int timeCount;
    public int turnsMade;
    public int unitsDied;
    public int unitsProduced;

    void defaultValues() {
        this.turnsMade = 0;
        this.unitsDied = 0;
        this.unitsProduced = 0;
        this.moneySpent = 0;
        this.timeCount = 0;
    }

    public void copyFrom(MatchStatistics source) {
        this.turnsMade = source.turnsMade;
        this.unitsDied = source.unitsDied;
        this.unitsProduced = source.unitsProduced;
        this.moneySpent = source.moneySpent;
        this.timeCount = source.timeCount;
    }

    void moneyWereSpent(int amount) {
        this.moneySpent += amount;
    }

    void unitWasProduced() {
        this.unitsProduced++;
    }

    void unitWasKilled() {
        this.unitsDied++;
    }

    void turnWasMade() {
        this.turnsMade++;
    }

    public String getTimeString() {
        int currentCountDown = this.timeCount / 60;
        int min = 0;
        while (currentCountDown >= 60) {
            min++;
            currentCountDown -= 60;
        }
        String zero = "";
        if (currentCountDown < 10) {
            zero = "0";
        }
        return min + ":" + zero + currentCountDown;
    }

    public void showInConsole() {
        System.out.println();
        System.out.println("Statistics:");
        System.out.println("turnsMade: " + this.turnsMade);
        System.out.println("unitsDied: " + this.unitsDied);
        System.out.println("unitsProduced: " + this.unitsProduced);
        System.out.println("moneySpent: " + this.moneySpent);
        System.out.println();
    }

    void increaseTimeCount() {
        this.timeCount++;
    }

    public void onUnitsMerged() {
        this.unitsDied -= 2;
        this.unitsProduced--;
    }
}
