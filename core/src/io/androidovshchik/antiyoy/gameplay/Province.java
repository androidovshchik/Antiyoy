package io.androidovshchik.antiyoy.gameplay;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import io.androidovshchik.antiyoy.YioGdxGame;
import io.androidovshchik.antiyoy.gameplay.name_generator.CityNameGenerator;
import io.androidovshchik.antiyoy.gameplay.rules.GameRules;
import io.androidovshchik.antiyoy.stuff.Fonts;

public class Province {
    public static final int DEFAULT_MONEY = 10;
    private GameController gameController;
    public ArrayList<Hex> hexList;
    public int money = 10;
    public String name;
    public float nameWidth;
    public ArrayList<Hex> tempList = new ArrayList();

    public Province(GameController gameController, ArrayList<Hex> hexList) {
        this.gameController = gameController;
        this.hexList = new ArrayList(hexList);
    }

    void placeCapitalInRandomPlace(Random random) {
        if (!GameRules.replayMode) {
            Hex randomPlace = getFreeHex(random);
            if (randomPlace == null) {
                randomPlace = getAnyHexExceptTowers();
            }
            if (randomPlace == null) {
                randomPlace = getRandomHex();
            }
            this.gameController.cleanOutHex(randomPlace);
            this.gameController.addSolidObject(randomPlace, 3);
            this.gameController.replayManager.onCitySpawned(randomPlace);
            this.gameController.addAnimHex(randomPlace);
            this.gameController.updateCacheOnceAfterSomeTime();
            randomPlace.lastColorIndex = randomPlace.colorIndex;
            randomPlace.animFactor.setValues(0.0d, 0.0d);
            randomPlace.animFactor.appear(1, 2.0d);
            updateName();
        }
    }

    boolean hasCapital() {
        Iterator it = this.hexList.iterator();
        while (it.hasNext()) {
            if (((Hex) it.next()).objectInside == 3) {
                return true;
            }
        }
        return false;
    }

    public Hex getStrongTower() {
        Iterator it = this.hexList.iterator();
        while (it.hasNext()) {
            Hex hex = (Hex) it.next();
            if (hex.objectInside == 7) {
                return hex;
            }
        }
        return null;
    }

    public Hex getCapital() {
        Iterator it = this.hexList.iterator();
        while (it.hasNext()) {
            Hex hex = (Hex) it.next();
            if (hex.objectInside == 3) {
                return hex;
            }
        }
        return (Hex) this.hexList.get(0);
    }

    private Hex getRandomHex() {
        return (Hex) this.hexList.get(this.gameController.random.nextInt(this.hexList.size()));
    }

    private Hex getAnyHexExceptTowers() {
        this.tempList.clear();
        Iterator it = this.hexList.iterator();
        while (it.hasNext()) {
            Hex hex = (Hex) it.next();
            if (!hex.containsTower()) {
                this.tempList.add(hex);
            }
        }
        if (this.tempList.size() == 0) {
            return null;
        }
        return (Hex) this.tempList.get(YioGdxGame.random.nextInt(this.tempList.size()));
    }

    Province getSnapshotCopy() {
        Province copy = new Province(this.gameController, this.hexList);
        copy.money = this.money;
        return copy;
    }

    private Hex getFreeHex(Random random) {
        this.tempList.clear();
        Iterator it = this.hexList.iterator();
        while (it.hasNext()) {
            Hex hex = (Hex) it.next();
            if (hex.isFree()) {
                this.tempList.add(hex);
            }
        }
        if (this.tempList.size() == 0) {
            return null;
        }
        return (Hex) this.tempList.get(random.nextInt(this.tempList.size()));
    }

    public int getBalance() {
        return (getIncome() - getTaxes()) + getDotations();
    }

    String getBalanceString() {
        int balance = getBalance();
        if (balance > 0) {
            return "+" + balance;
        }
        return "" + balance;
    }

    public int getIncome() {
        int income = 0;
        Iterator it = this.hexList.iterator();
        while (it.hasNext()) {
            income += this.gameController.ruleset.getHexIncome((Hex) it.next());
        }
        return income;
    }

    int getTaxes() {
        int taxes = 0;
        Iterator it = this.hexList.iterator();
        while (it.hasNext()) {
            taxes += this.gameController.ruleset.getHexTax((Hex) it.next());
        }
        return taxes;
    }

    public int getDotations() {
        if (GameRules.diplomacyEnabled) {
            return this.gameController.fieldController.diplomacyManager.getProvinceDotations(this);
        }
        return 0;
    }

    public float getIncomeCoefficient() {
        int n = 0;
        int color = getColor();
        Iterator it = this.gameController.fieldController.provinces.iterator();
        while (it.hasNext()) {
            if (((Province) it.next()).getColor() == color) {
                n++;
            }
        }
        return 1.0f / ((float) n);
    }

    private void clearFromHouses() {
        Iterator it = this.hexList.iterator();
        while (it.hasNext()) {
            Hex hex = (Hex) it.next();
            if (hex.objectInside == 3) {
                this.gameController.cleanOutHex(hex);
            }
        }
    }

    public boolean isSelected() {
        if (this.hexList.size() == 0) {
            return false;
        }
        return ((Hex) this.hexList.get(0)).isSelected();
    }

    public String getName() {
        if (this.name == null) {
            updateName();
        }
        return this.name;
    }

    public void updateName() {
        setName(CityNameGenerator.getInstance().generateName(getCapital()));
    }

    public void setName(String name) {
        this.name = name;
        this.nameWidth = (0.5f * YioGdxGame.getTextWidth(Fonts.microFont, name)) + (0.1f * this.gameController.yioGdxGame.gameView.hexViewSize);
    }

    void setCapital(Hex hex) {
        clearFromHouses();
        this.gameController.addSolidObject(hex, 3);
        updateName();
    }

    boolean hasSomeoneReadyToMove() {
        Iterator it = this.hexList.iterator();
        while (it.hasNext()) {
            Hex hex = (Hex) it.next();
            if (hex.containsUnit() && hex.unit.isReadyToMove()) {
                return true;
            }
        }
        return false;
    }

    public boolean canAiAffordUnit(int strength) {
        return canAiAffordUnit(strength, 2);
    }

    public boolean canAiAffordUnit(int strength, int turnsToSurvive) {
        if (GameRules.diplomacyEnabled && !this.gameController.fieldController.diplomacyManager.isProvinceAllowedToBuildUnit(this, strength)) {
            return false;
        }
        if (this.money + (turnsToSurvive * (getBalance() - this.gameController.ruleset.getUnitTax(strength))) >= 0) {
            return true;
        }
        return false;
    }

    public boolean canBuildUnit(int strength) {
        if (GameRules.replayMode) {
            return true;
        }
        return this.gameController.ruleset.canBuildUnit(this, strength);
    }

    public boolean hasMoneyForTower() {
        return this.money >= 15;
    }

    public boolean hasMoneyForFarm() {
        return this.money >= getCurrentFarmPrice();
    }

    public int getCurrentFarmPrice() {
        return getExtraFarmCost() + 12;
    }

    public boolean hasMoneyForStrongTower() {
        return this.money >= 35;
    }

    public boolean hasMoneyForTree() {
        return this.money >= 10;
    }

    public int getExtraFarmCost() {
        int c = 0;
        Iterator it = this.hexList.iterator();
        while (it.hasNext()) {
            if (((Hex) it.next()).objectInside == 6) {
                c += 2;
            }
        }
        return c;
    }

    public boolean equals(Province province) {
        Iterator it = this.hexList.iterator();
        while (it.hasNext()) {
            if (!province.containsHex((Hex) it.next())) {
                return false;
            }
        }
        it = province.hexList.iterator();
        while (it.hasNext()) {
            if (!containsHex((Hex) it.next())) {
                return false;
            }
        }
        return true;
    }

    boolean containsHex(Hex hex) {
        return this.hexList.contains(hex);
    }

    public int getColor() {
        if (this.hexList.size() == 0) {
            return -1;
        }
        return ((Hex) this.hexList.get(0)).colorIndex;
    }

    void addHex(Hex hex) {
        if (!containsHex(hex)) {
            this.hexList.listIterator().add(hex);
        }
    }

    void setHexList(ArrayList<Hex> list) {
        this.hexList = new ArrayList(list);
    }

    void close() {
        this.gameController = null;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[Province(").append(this.gameController.fieldController.getColorName(getColor())).append(")").append(":");
        Iterator it = this.hexList.iterator();
        while (it.hasNext()) {
            builder.append(" ").append((Hex) it.next());
        }
        builder.append("]");
        return builder.toString();
    }
}
