package io.androidovshchik.antiyoy.gameplay;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import io.androidovshchik.antiyoy.gameplay.rules.GameRules;
import io.androidovshchik.antiyoy.menu.scenes.editor.SceneEditorInstruments;
import io.androidovshchik.antiyoy.stuff.Yio;

public class MapGeneratorGeneric extends MapGenerator {
    public MapGeneratorGeneric(GameController gameController) {
        super(gameController);
    }

    public void generateMap(Random random, Hex[][] field) {
        setValues(random, field);
        beginGeneration();
        createLand();
        addTrees();
        while (!hasGreenProvince()) {
            genericBalance();
        }
        endGeneration();
    }

    private boolean hasGreenProvince() {
        Iterator it = this.gameController.fieldController.activeHexes.iterator();
        while (it.hasNext()) {
            Hex activeHex = (Hex) it.next();
            if (activeHex.colorIndex == 0 && activeHex.numberOfFriendlyHexesNearby() > 2) {
                return true;
            }
        }
        return false;
    }

    private void genericBalance() {
        Iterator it = this.gameController.fieldController.activeHexes.iterator();
        while (it.hasNext()) {
            ((Hex) it.next()).colorIndex = FieldController.NEUTRAL_LANDS_INDEX;
        }
        for (int i = 0; i < numberOfProvincesByLevelSize(); i++) {
            for (int colorIndex = 0; colorIndex < GameRules.colorNumber; colorIndex++) {
                Hex hex = findGoodPlaceForNewProvince();
                if (hex != null) {
                    hex.setColorIndex(colorIndex);
                    spawnProvince(hex, 2);
                }
            }
        }
        cutProvincesToSmallSizes();
        makeSingleHexesIntoProvinces();
        giveLastPlayersSlightAdvantage();
    }

    private void makeSingleHexesIntoProvinces() {
        Iterator it = this.gameController.fieldController.activeHexes.iterator();
        while (it.hasNext()) {
            Hex activeHex = (Hex) it.next();
            if (!activeHex.isNeutral() && activeHex.numberOfFriendlyHexesNearby() <= 0) {
                int c = 3;
                for (int i = 0; i < 6; i++) {
                    Hex adjHex = activeHex.getAdjacentHex(i);
                    if (adjHex.active && adjHex.isNeutral()) {
                        adjHex.colorIndex = activeHex.colorIndex;
                        c--;
                        if (c == 0) {
                            break;
                        }
                    }
                }
            }
        }
    }

    protected void giveLastPlayersSlightAdvantage() {
        switch (this.gameController.fieldController.levelSize) {
            case 4:
                giveAdvantageToPlayer(GameRules.colorNumber - 1, 0.35d);
                giveAdvantageToPlayer(GameRules.colorNumber - 2, 0.2d);
                if (GameRules.colorNumber >= 5) {
                    giveAdvantageToPlayer(GameRules.colorNumber - 3, 0.04d);
                } else {
                    giveAdvantageToPlayer(1, 0.03d);
                    giveAdvantageToPlayer(GameRules.colorNumber - 1, 0.05d);
                }
                giveDisadvantageToPlayer(0, SceneEditorInstruments.ICON_SIZE);
                giveDisadvantageToPlayer(1, 0.17d);
                return;
            default:
                giveAdvantageToPlayer(GameRules.colorNumber - 1, 0.28d);
                giveAdvantageToPlayer(GameRules.colorNumber - 2, 0.15d);
                if (GameRules.colorNumber >= 5) {
                    giveAdvantageToPlayer(GameRules.colorNumber - 3, SceneEditorInstruments.ICON_SIZE);
                } else {
                    giveAdvantageToPlayer(1, 0.03d);
                    giveAdvantageToPlayer(GameRules.colorNumber - 1, 0.05d);
                }
                giveDisadvantageToPlayer(0, 0.17d);
                giveDisadvantageToPlayer(1, 0.1d);
                return;
        }
    }

    protected void decreaseProvince(ArrayList<Hex> provinceList, double power) {
        int num = (int) (((double) provinceList.size()) * power);
        for (int i = 0; i < num; i++) {
            Hex hex = findHexToExcludeFromProvince(provinceList);
            provinceList.remove(hex);
            hex.colorIndex = FieldController.NEUTRAL_LANDS_INDEX;
        }
    }

    protected void spawnProvince(Hex spawnHex, int startingPotential) {
        spawnHex.genPotential = startingPotential;
        ArrayList<Hex> propagationList = new ArrayList();
        propagationList.add(spawnHex);
        while (propagationList.size() > 0) {
            Hex hex = (Hex) propagationList.get(0);
            propagationList.remove(0);
            if (this.random.nextInt(startingPotential) <= hex.genPotential) {
                hex.colorIndex = spawnHex.colorIndex;
                if (hex.genPotential != 0) {
                    for (int i = 0; i < 6; i++) {
                        Hex adjHex = hex.getAdjacentHex(i);
                        if (!propagationList.contains(adjHex) && adjHex.active && adjHex.colorIndex == FieldController.NEUTRAL_LANDS_INDEX) {
                            adjHex.genPotential = hex.genPotential - 1;
                            propagationList.add(adjHex);
                        }
                    }
                }
            }
        }
    }

    protected void reduceProvinceSize(ArrayList<Hex> provinceList) {
        int provinceColor = ((Hex) provinceList.get(0)).colorIndex;
        while (provinceList.size() > SMALL_PROVINCE_SIZE) {
            Hex hex = findHexToExcludeFromProvince(provinceList);
            provinceList.remove(hex);
            hex.colorIndex = FieldController.NEUTRAL_LANDS_INDEX;
        }
    }

    protected boolean activateHex(Hex hex, int color) {
        if (hex.active) {
            return false;
        }
        hex.active = true;
        hex.setColorIndex(FieldController.NEUTRAL_LANDS_INDEX);
        this.gameController.fieldController.activeHexes.listIterator().add(hex);
        return true;
    }

    double distanceToClosestProvince(Hex hex) {
        double minDistance = -1.0d;
        Iterator it = this.gameController.fieldController.activeHexes.iterator();
        while (it.hasNext()) {
            Hex activeHex = (Hex) it.next();
            if (!activeHex.isNeutral()) {
                double currentDistance = Yio.distance((double) hex.index1, (double) hex.index2, (double) activeHex.index1, (double) activeHex.index2);
                if (minDistance == -1.0d || currentDistance < minDistance) {
                    minDistance = currentDistance;
                }
            }
        }
        return minDistance;
    }

    Hex findGoodPlaceForNewProvince() {
        if (allHexesAreNeutral()) {
            return getRandomFreeHex();
        }
        double maxDistance = 0.0d;
        Hex bestHex = null;
        Iterator it = this.gameController.fieldController.activeHexes.iterator();
        while (it.hasNext()) {
            Hex activeHex = (Hex) it.next();
            if (activeHex.isNeutral()) {
                double currentDistance = distanceToClosestProvince(activeHex);
                if (bestHex == null || currentDistance > maxDistance) {
                    bestHex = activeHex;
                    maxDistance = currentDistance;
                }
            }
        }
        return bestHex;
    }

    private boolean allHexesAreNeutral() {
        Iterator it = this.gameController.fieldController.activeHexes.iterator();
        while (it.hasNext()) {
            if (!((Hex) it.next()).isNeutral()) {
                return false;
            }
        }
        return true;
    }

    protected Hex getRandomFreeHex() {
        return (Hex) this.gameController.fieldController.activeHexes.get(this.random.nextInt(this.gameController.fieldController.activeHexes.size()));
    }

    protected int numberOfProvincesByLevelSize() {
        switch (this.gameController.fieldController.levelSize) {
            case 2:
            case 4:
                return 2;
            default:
                return 1;
        }
    }
}
