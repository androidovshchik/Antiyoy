package io.androidovshchik.antiyoy.gameplay.editor;

import java.util.ArrayList;
import java.util.Iterator;
import io.androidovshchik.antiyoy.YioGdxGame;
import io.androidovshchik.antiyoy.gameplay.FieldController;
import io.androidovshchik.antiyoy.gameplay.Hex;
import io.androidovshchik.antiyoy.gameplay.MoveZoneDetection;

public class EditorAutomationManager {
    ArrayList<Hex> ignoredHices = new ArrayList();
    private final LevelEditor levelEditor;

    public EditorAutomationManager(LevelEditor levelEditor) {
        this.levelEditor = levelEditor;
    }

    public void expandProvinces() {
        Iterator it = getFieldController().activeHexes.iterator();
        while (it.hasNext()) {
            Hex activeHex = (Hex) it.next();
            if (!activeHex.isNeutral()) {
                for (int i = 0; i < 6; i++) {
                    Hex adjacentHex = activeHex.getAdjacentHex(i);
                    if (adjacentHex != null && adjacentHex.active && adjacentHex.isNeutral() && YioGdxGame.random.nextDouble() <= 0.1d) {
                        int objectInside = adjacentHex.objectInside;
                        getFieldController().setHexColor(adjacentHex, activeHex.colorIndex);
                        if (objectInside > 0) {
                            this.levelEditor.placeObject(adjacentHex, objectInside);
                        }
                    }
                }
            }
        }
        checkToRemoveDoubleCapitals();
    }

    private FieldController getFieldController() {
        return this.levelEditor.gameController.fieldController;
    }

    void checkToRemoveDoubleCapitals() {
        this.ignoredHices.clear();
        Iterator it = getFieldController().activeHexes.iterator();
        while (it.hasNext()) {
            Hex activeHex = (Hex) it.next();
            if (!(activeHex.isNeutral() || this.ignoredHices.contains(activeHex))) {
                ArrayList<Hex> hices = this.levelEditor.detectorProvince.detectProvince(activeHex);
                tagHicesAsIgnored(hices);
                if (howManyCapitalsInProvince(hices) > 1) {
                    removeAllCapitalsExceptOne(hices);
                }
            }
        }
    }

    void removeAllCapitalsExceptOne(ArrayList<Hex> hices) {
        boolean foundCapital = false;
        Iterator it = hices.iterator();
        while (it.hasNext()) {
            Hex hice = (Hex) it.next();
            if (hice.objectInside == 3) {
                if (foundCapital) {
                    this.levelEditor.gameController.cleanOutHex(hice);
                } else {
                    foundCapital = true;
                }
            }
        }
    }

    int howManyCapitalsInProvince(ArrayList<Hex> hices) {
        int c = 0;
        Iterator it = hices.iterator();
        while (it.hasNext()) {
            if (((Hex) it.next()).objectInside == 3) {
                c++;
            }
        }
        return c;
    }

    public void expandTrees() {
        getFieldController().expandTrees();
    }

    public void placeCapitalsOrFarms() {
        this.ignoredHices.clear();
        Iterator it = getFieldController().activeHexes.iterator();
        while (it.hasNext()) {
            Hex activeHex = (Hex) it.next();
            if (!(activeHex.isNeutral() || this.ignoredHices.contains(activeHex))) {
                ArrayList<Hex> hices = this.levelEditor.detectorProvince.detectProvince(activeHex);
                tagHicesAsIgnored(hices);
                if (provinceHasCapital(hices)) {
                    buildSomeRandomFarms(hices);
                } else {
                    placeCapitalInRandomPlace(hices);
                }
            }
        }
    }

    void tagHicesAsIgnored(ArrayList<Hex> hices) {
        Iterator it = hices.iterator();
        while (it.hasNext()) {
            this.ignoredHices.listIterator().add((Hex) it.next());
        }
    }

    void buildSomeRandomFarms(ArrayList<Hex> hices) {
        Iterator it = hices.iterator();
        while (it.hasNext()) {
            Hex hex = (Hex) it.next();
            if (!hex.containsObject() && MoveZoneDetection.canBuildFarmOnHex(hex) && YioGdxGame.random.nextDouble() <= 0.2d) {
                this.levelEditor.placeObject(hex, 6);
            }
        }
    }

    void placeCapitalInRandomPlace(ArrayList<Hex> hices) {
        this.levelEditor.placeObject((Hex) hices.get(YioGdxGame.random.nextInt(hices.size())), 3);
    }

    boolean provinceHasCapital(ArrayList<Hex> hices) {
        Iterator it = hices.iterator();
        while (it.hasNext()) {
            if (((Hex) it.next()).objectInside == 3) {
                return true;
            }
        }
        return false;
    }

    public void placeRandomTowers() {
        Iterator it = getFieldController().activeHexes.iterator();
        while (it.hasNext()) {
            Hex activeHex = (Hex) it.next();
            if (isHexGoodForRandomTower(activeHex)) {
                this.levelEditor.placeObject(activeHex, 4);
            }
        }
    }

    boolean isHexGoodForRandomTower(Hex hex) {
        if (hex.active && !hex.isNeutral() && hex.isFree() && hasProvinceNearby(hex) && getPredictedDefenseGainByNewTower(hex) >= 3) {
            return true;
        }
        return false;
    }

    boolean hasProvinceNearby(Hex hex) {
        for (int i = 0; i < 6; i++) {
            Hex adj1 = hex.getAdjacentHex(i);
            if (adj1.active) {
                for (int j = 0; j < 6; j++) {
                    Hex adj2 = adj1.getAdjacentHex(j);
                    if (adj2.active && adj2.colorIndex != hex.colorIndex) {
                        return true;
                    }
                }
                continue;
            }
        }
        return false;
    }

    protected int getPredictedDefenseGainByNewTower(Hex hex) {
        int c = 0;
        if (hex.active && !hex.isDefendedByTower()) {
            c = 0 + 1;
        }
        for (int i = 0; i < 6; i++) {
            Hex adjHex = hex.getAdjacentHex(i);
            if (adjHex.active && hex.sameColor(adjHex) && !adjHex.isDefendedByTower()) {
                c++;
            }
            if (adjHex.containsTower()) {
                c--;
            }
        }
        return c;
    }

    public void cutExcessStuff() {
        Iterator it = getFieldController().activeHexes.iterator();
        while (it.hasNext()) {
            Hex activeHex = (Hex) it.next();
            if (activeHex.containsTree()) {
                checkToCutExcessTree(activeHex);
            } else if (activeHex.objectInside == 6) {
                checkToCutExcessFarm(activeHex);
            }
        }
    }

    private void checkToCutExcessFarm(Hex activeHex) {
        if (YioGdxGame.random.nextDouble() <= 0.1d) {
            getFieldController().cleanOutHex(activeHex);
        }
    }

    private void checkToCutExcessTree(Hex activeHex) {
        if (YioGdxGame.random.nextDouble() <= 0.2d && getNumberOfAdjacentTrees(activeHex) != 0) {
            getFieldController().cleanOutHex(activeHex);
        }
    }

    private int getNumberOfAdjacentTrees(Hex hex) {
        int c = 0;
        for (int i = 0; i < 6; i++) {
            Hex adjacentHex = hex.getAdjacentHex(i);
            if (adjacentHex.active && adjacentHex.containsTree()) {
                c++;
            }
        }
        return c;
    }
}
