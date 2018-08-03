package io.androidovshchik.antiyoy.gameplay;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import io.androidovshchik.antiyoy.Settings;
import io.androidovshchik.antiyoy.gameplay.diplomacy.DiplomacyInfoCondensed;
import io.androidovshchik.antiyoy.gameplay.diplomacy.DiplomacyManager;
import io.androidovshchik.antiyoy.gameplay.replays.actions.RepAction;
import io.androidovshchik.antiyoy.gameplay.rules.GameRules;

public class LevelSnapshot {
    private ArrayList<Hex> activeHexesCopy = new ArrayList();
    private String diplomacyInfo;
    private int fHeight;
    private int fWidth;
    private Hex[][] fieldCopy = ((Hex[][]) null);
    private final GameController gameController;
    private MatchStatistics matchStatistics = new MatchStatistics();
    private ArrayList<Province> provincesCopy = new ArrayList();
    private ArrayList<RepAction> replayBuffer;
    private Hex selectionHex;
    boolean used;

    public LevelSnapshot(GameController gameController) {
        this.gameController = gameController;
        if (Settings.replaysEnabled) {
            this.replayBuffer = new ArrayList();
        }
        this.used = false;
        this.diplomacyInfo = null;
    }

    void defaultValues() {
        for (int i = 0; i < this.fWidth; i++) {
            for (int j = 0; j < this.fHeight; j++) {
                this.fieldCopy[i][j] = null;
            }
        }
        this.provincesCopy.clear();
        this.activeHexesCopy.clear();
        this.selectionHex = null;
        this.matchStatistics.defaultValues();
        if (this.replayBuffer != null) {
            this.replayBuffer.clear();
        }
        this.fWidth = -1;
        this.fHeight = -1;
        this.diplomacyInfo = null;
    }

    public void take() {
        this.used = true;
        updateSelectionHex();
        updateMetrics();
        updateFieldCopy();
        updateProvincesCopy();
        updateActiveHexesCopy();
        updateMatchStatistics();
        updateReplayBuffer();
        updateDiplomacyInfo();
    }

    private void updateDiplomacyInfo() {
        if (GameRules.diplomacyEnabled) {
            DiplomacyManager diplomacyManager = this.gameController.fieldController.diplomacyManager;
            DiplomacyInfoCondensed instance = DiplomacyInfoCondensed.getInstance();
            instance.update(diplomacyManager);
            this.diplomacyInfo = instance.getFull();
        }
    }

    private void updateReplayBuffer() {
        if (Settings.replaysEnabled) {
            this.replayBuffer.clear();
            Iterator it = this.gameController.replayManager.getReplay().buffer.iterator();
            while (it.hasNext()) {
                this.replayBuffer.add((RepAction) it.next());
            }
        }
    }

    private void updateMatchStatistics() {
        this.matchStatistics.copyFrom(this.gameController.matchStatistics);
    }

    private void updateActiveHexesCopy() {
        Iterator it = this.gameController.fieldController.activeHexes.iterator();
        while (it.hasNext()) {
            this.activeHexesCopy.add(((Hex) it.next()).getSnapshotCopy());
        }
    }

    private void updateProvincesCopy() {
        Iterator it = this.gameController.fieldController.provinces.iterator();
        while (it.hasNext()) {
            this.provincesCopy.add(((Province) it.next()).getSnapshotCopy());
        }
    }

    private void updateFieldCopy() {
        checkToCreateFieldCopyMatrix();
        for (int i = 0; i < this.fWidth; i++) {
            for (int j = 0; j < this.fHeight; j++) {
                this.fieldCopy[i][j] = this.gameController.fieldController.field[i][j].getSnapshotCopy();
            }
        }
    }

    private void checkToCreateFieldCopyMatrix() {
        if (this.fieldCopy == null) {
            this.fieldCopy = (Hex[][]) Array.newInstance(Hex.class, new int[]{this.fWidth, this.fHeight});
        }
    }

    private void updateMetrics() {
        this.fWidth = this.gameController.fieldController.fWidth;
        this.fHeight = this.gameController.fieldController.fHeight;
    }

    private void updateSelectionHex() {
        if (this.gameController.selectionController.isSomethingSelected()) {
            this.selectionHex = (Hex) this.gameController.fieldController.selectedProvince.hexList.get(0);
        } else {
            this.selectionHex = null;
        }
    }

    private void cleanOutEveryHexInField() {
        this.gameController.fieldController.cleanOutAllHexesInField();
    }

    private Hex getHexByCopy(Hex copy) {
        return this.gameController.fieldController.field[copy.index1][copy.index2];
    }

    public void recreate() {
        this.gameController.fieldController.clearField();
        cleanOutEveryHexInField();
        this.gameController.fieldController.clearAnims();
        recreateField();
        recreateActiveHexes();
        this.gameController.fieldController.detectProvinces();
        recreateProvinces();
        recreateSelection();
        recreateStatistics();
        recreateReplayBuffer();
        recreateDiplomacy();
        this.gameController.addAnimHex(this.gameController.fieldController.field[0][0]);
        this.gameController.updateWholeCache = true;
    }

    private void recreateDiplomacy() {
        if (GameRules.diplomacyEnabled && this.diplomacyInfo != null) {
            DiplomacyManager diplomacyManager = this.gameController.fieldController.diplomacyManager;
            DiplomacyInfoCondensed instance = DiplomacyInfoCondensed.getInstance();
            instance.setFull(this.diplomacyInfo);
            instance.apply(diplomacyManager);
        }
    }

    private void recreateReplayBuffer() {
        if (Settings.replaysEnabled && !GameRules.replayMode) {
            this.gameController.replayManager.getReplay().recreateBufferFromSnapshot(this.replayBuffer);
        }
    }

    private void recreateStatistics() {
        this.gameController.matchStatistics.copyFrom(this.matchStatistics);
    }

    private void recreateSelection() {
        this.gameController.selectionController.deselectAll();
        if (this.selectionHex != null) {
            this.gameController.selectAdjacentHexes(this.selectionHex);
        }
    }

    private void recreateProvinces() {
        Iterator it = this.provincesCopy.iterator();
        while (it.hasNext()) {
            Province copy = (Province) it.next();
            Province province = this.gameController.findProvinceCopy(copy);
            if (province == null) {
                System.out.println();
                System.out.println("Problem in level snapshot.");
                System.out.println("Wasn't been able to find province by hex. Color = " + copy.getColor());
                System.out.println("copy.getCapital() = " + copy.getCapital());
            } else {
                province.money = copy.money;
                province.updateName();
            }
        }
    }

    private void recreateActiveHexes() {
        ListIterator iterator = this.gameController.fieldController.activeHexes.listIterator();
        Iterator it = this.activeHexesCopy.iterator();
        while (it.hasNext()) {
            iterator.add(getHexByCopy((Hex) it.next()));
        }
    }

    private void recreateField() {
        for (int i = 0; i < this.fWidth; i++) {
            for (int j = 0; j < this.fHeight; j++) {
                Hex currHex = this.gameController.fieldController.field[i][j];
                if (currHex.active) {
                    if (!currHex.sameColor(this.fieldCopy[i][j])) {
                        currHex.colorIndex = this.fieldCopy[i][j].colorIndex;
                        this.gameController.addAnimHex(currHex);
                    }
                    if (currHex.selected != this.fieldCopy[i][j].selected) {
                        currHex.selected = this.fieldCopy[i][j].selected;
                        if (!currHex.selected) {
                            currHex.selectionFactor.setValues(0.0d, 0.0d);
                        }
                    }
                    if (this.fieldCopy[i][j].containsObject()) {
                        this.gameController.addSolidObject(currHex, this.fieldCopy[i][j].objectInside);
                    }
                    if (this.fieldCopy[i][j].containsUnit()) {
                        this.gameController.addUnit(currHex, this.fieldCopy[i][j].unit.strength);
                        if (this.fieldCopy[i][j].unit.isReadyToMove()) {
                            currHex.unit.setReadyToMove(true);
                            currHex.unit.startJumping();
                        } else {
                            currHex.unit.setReadyToMove(false);
                            currHex.unit.stopJumping();
                        }
                    }
                }
            }
        }
    }

    public void reset() {
        this.used = false;
        defaultValues();
    }
}
