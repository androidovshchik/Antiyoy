package io.androidovshchik.antiyoy.gameplay;

import com.badlogic.gdx.Gdx;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import yio.tro.antiyoy.Settings;
import yio.tro.antiyoy.SoundControllerYio;
import yio.tro.antiyoy.factor_yio.FactorYio;
import yio.tro.antiyoy.gameplay.rules.GameRules;
import yio.tro.antiyoy.menu.ButtonYio;
import yio.tro.antiyoy.menu.scenes.Scenes;
import yio.tro.antiyoy.stuff.GraphicsYio;

public class SelectionController {
    public static final int TIP_INDEX_FARM = 5;
    public static final int TIP_INDEX_STRONG_TOWER = 6;
    public static final int TIP_INDEX_TOWER = 0;
    public static final int TIP_INDEX_TREE = 7;
    public static final int TIP_INDEX_UNIT_1 = 1;
    public static final int TIP_INDEX_UNIT_2 = 2;
    public static final int TIP_INDEX_UNIT_3 = 3;
    public static final int TIP_INDEX_UNIT_4 = 4;
    public FactorYio blackoutFactor = new FactorYio();
    public int defTipDelay = ButtonYio.DEFAULT_TOUCH_DELAY;
    long defTipSpawnTime;
    float defaultBubbleRadius = (0.01f * ((float) Gdx.graphics.getWidth()));
    private final GameController gameController;
    private boolean isSomethingSelected;
    public FactorYio selMoneyFactor = new FactorYio();
    public FactorYio selUnitFactor = new FactorYio();
    float selectX;
    float selectY;
    public Unit selectedUnit;
    public FactorYio tipFactor = new FactorYio();
    public int tipShowType;
    public int tipType;

    public SelectionController(GameController gameController) {
        this.gameController = gameController;
    }

    void moveSelections() {
        Iterator it = this.gameController.fieldController.selectedHexes.iterator();
        while (it.hasNext()) {
            ((Hex) it.next()).move();
        }
        if (this.selectedUnit != null && this.selUnitFactor.hasToMove()) {
            this.selUnitFactor.move();
        }
    }

    void moveDefenseTips() {
        FactorYio defenseTipFactor = this.gameController.fieldController.defenseTipFactor;
        defenseTipFactor.move();
        if (this.gameController.getCurrentTime() > this.defTipSpawnTime + ((long) this.defTipDelay)) {
            if (defenseTipFactor.getDy() >= 0.0d) {
                defenseTipFactor.destroy(1, 1.0d);
            }
            ArrayList<Hex> defenseTips = this.gameController.fieldController.defenseTips;
            if (defenseTipFactor.get() == 0.0f && defenseTips.size() > 0) {
                ListIterator iterator = defenseTips.listIterator();
                while (iterator.hasNext()) {
                    Hex hex = (Hex) iterator.next();
                    iterator.remove();
                }
            }
        }
    }

    public void awakeTip(int type) {
        this.tipFactor.setValues(0.01d, 0.0d);
        this.tipFactor.appear(3, 2.0d);
        this.tipType = type;
        this.tipShowType = type;
        this.selectedUnit = null;
        if (isTipTypeSolidObject() && this.gameController.fieldController.moveZone.size() > 0) {
            this.gameController.fieldController.hideMoveZone();
        }
        this.gameController.updateCurrentPriceString();
    }

    private boolean isTipTypeSolidObject() {
        switch (this.tipType) {
            case 0:
            case 5:
            case 6:
            case 7:
                return true;
            default:
                return false;
        }
    }

    private boolean isTipTypeUnit() {
        switch (this.tipType) {
            case 1:
            case 2:
            case 3:
            case 4:
                return true;
            default:
                return false;
        }
    }

    void hideTip() {
        this.tipFactor.destroy(1, 2.0d);
        resetTipType();
    }

    private void resetTipType() {
        this.tipType = -1;
    }

    public int getTipType() {
        return this.tipType;
    }

    public boolean isSomethingSelected() {
        return this.gameController.fieldController.isSomethingSelected();
    }

    public void deselectAll() {
        for (int i = 0; i < this.gameController.fieldController.fWidth; i++) {
            for (int j = 0; j < this.gameController.fieldController.fHeight; j++) {
                this.gameController.fieldController.field[i][j].selected = false;
            }
        }
        ListIterator listIterator = this.gameController.fieldController.selectedHexes.listIterator();
        while (listIterator.hasNext()) {
            listIterator.next();
            listIterator.remove();
        }
        this.selectedUnit = null;
        this.selMoneyFactor.destroy(3, 2.0d);
        this.tipFactor.setValues(0.0d, 0.0d);
        this.tipFactor.destroy(1, 1.0d);
        this.gameController.fieldController.hideMoveZone();
        hideBuildOverlay();
        resetTipType();
    }

    private void hideBuildOverlay() {
        if (Settings.fastConstruction) {
            Scenes.sceneFastConstructionPanel.hide();
        } else {
            Scenes.sceneSelectionOverlay.hide();
        }
    }

    void selectAdjacentHexes(Hex startHex) {
        this.gameController.fieldController.selectAdjacentHexes(startHex);
    }

    public void updateSelectedProvinceMoney() {
        this.gameController.fieldController.updateSelectedProvinceMoney();
    }

    void showDefenseTip(Hex hex) {
        FieldController fieldController = this.gameController.fieldController;
        ArrayList<Hex> defenseTips = fieldController.defenseTips;
        if (fieldController.defenseTipFactor.get() == 1.0f) {
            defenseTips.clear();
        }
        for (int i = 0; i < 6; i++) {
            Hex adjHex = hex.getAdjacentHex(i);
            if (adjHex.active && adjHex.sameColor(hex)) {
                defenseTips.add(adjHex);
            }
        }
        fieldController.defenseTipFactor.setValues(0.0d, 0.0d);
        fieldController.defenseTipFactor.appear(3, 0.7d);
        this.defTipSpawnTime = System.currentTimeMillis();
        fieldController.defTipHex = hex;
    }

    public Hex getDefSrcHex(Hex hex) {
        for (int i = 0; i < 6; i++) {
            Hex adjacentHex = hex.getAdjacentHex(i);
            if (adjacentHex != null && adjacentHex.active && adjacentHex.colorIndex == hex.colorIndex && isHexGoodForDefenseTip(adjacentHex)) {
                return adjacentHex;
            }
        }
        return null;
    }

    public void focusedHexActions(Hex focusedHex) {
        debug(focusedHex);
        if (!focusedHex.ignoreTouch && !GameRules.inEditorMode) {
            if (focusedHex.active) {
                updateIsSomethingSelected();
                if (!reactionBuildStuff(focusedHex)) {
                    reactionInsideSelection(focusedHex);
                    reactionAttackEnemy(focusedHex);
                    reactionSelectProvince(focusedHex);
                    reactionSelectOrMovePeacefully(focusedHex);
                    return;
                }
                return;
            }
            deselectAll();
        }
    }

    private void debug(Hex focusedHex) {
    }

    private void showProvinceHexListInConsole(Hex focusedHex) {
        System.out.println();
        System.out.println("Province:");
        Iterator it = this.gameController.fieldController.getProvinceByHex(focusedHex).hexList.iterator();
        while (it.hasNext()) {
            System.out.println(" - " + ((Hex) it.next()));
        }
        System.out.println();
    }

    private void showDebugHexColors(Hex focusedHex) {
        System.out.println();
        System.out.println("focusedHex.colorIndex = " + focusedHex.colorIndex);
        System.out.println("gameController.colorIndexViewOffset = " + this.gameController.colorIndexViewOffset);
        System.out.println("colorIndexWithOffset = " + this.gameController.ruleset.getColorIndexWithOffset(focusedHex.colorIndex));
    }

    private void reactionSelectOrMovePeacefully(Hex focusedHex) {
        if (this.isSomethingSelected && !checkToSelectUnit(focusedHex) && focusedHex.inMoveZone && this.gameController.isCurrentTurn(focusedHex.colorIndex) && this.selectedUnit.canMoveToFriendlyHex(focusedHex)) {
            this.gameController.takeSnapshot();
            SoundControllerYio.playSound(SoundControllerYio.soundWalk);
            this.gameController.moveUnit(this.selectedUnit, focusedHex, this.gameController.fieldController.selectedProvince);
            this.selectedUnit = null;
        }
    }

    private boolean checkToSelectUnit(Hex focusedHex) {
        if (this.selectedUnit != null) {
            return false;
        }
        if (!focusedHex.containsUnit() || focusedHex.unit.moveFactor.get() != 1.0f || !focusedHex.unit.isReadyToMove()) {
            return true;
        }
        this.selectedUnit = focusedHex.unit;
        SoundControllerYio.playSound(SoundControllerYio.soundSelectUnit);
        this.gameController.fieldController.detectAndShowMoveZone(this.selectedUnit.currentHex, this.selectedUnit.strength, 4);
        this.selUnitFactor.setValues(0.0d, 0.0d);
        this.selUnitFactor.appear(3, 2.0d);
        hideTip();
        return true;
    }

    private void reactionSelectProvince(Hex focusedHex) {
        if (this.gameController.isCurrentTurn(focusedHex.colorIndex) && this.gameController.fieldController.hexHasNeighbourWithColor(focusedHex, this.gameController.getTurn())) {
            this.gameController.fieldController.selectAdjacentHexes(focusedHex);
            this.isSomethingSelected = true;
        }
    }

    private void reactionAttackEnemy(Hex focusedHex) {
        if (focusedHex.colorIndex != this.gameController.getTurn() && focusedHex.inMoveZone && this.selectedUnit != null) {
            this.gameController.takeSnapshot();
            this.gameController.moveUnit(this.selectedUnit, focusedHex, this.gameController.fieldController.selectedProvince);
            SoundControllerYio.playSound(SoundControllerYio.soundAttack);
            this.selectedUnit = null;
        }
    }

    private void reactionInsideSelection(Hex focusedHex) {
        if (this.isSomethingSelected) {
            if (!(focusedHex.selected || focusedHex.inMoveZone)) {
                deselectAll();
            }
            if (this.gameController.fieldController.moveZone.size() > 0 && !focusedHex.inMoveZone) {
                this.selectedUnit = null;
                this.gameController.fieldController.hideMoveZone();
            }
            if (focusedHex.selected && this.gameController.fieldController.moveZone.size() == 0 && isHexGoodForDefenseTip(focusedHex)) {
                showAllDefenseTipsInProvince(focusedHex);
            }
        }
    }

    private void showAllDefenseTipsInProvince(Hex srcHex) {
        Province provinceByHex = this.gameController.fieldController.getProvinceByHex(srcHex);
        Iterator it = provinceByHex.hexList.iterator();
        while (it.hasNext()) {
            Hex hex = (Hex) it.next();
            if (isHexGoodForDefenseTip(hex) && hex.getPos().distanceTo(srcHex.getPos()) <= ((double) (0.8f * GraphicsYio.width))) {
                showDefenseTip(hex);
            }
        }
        this.gameController.fieldController.defTipHex = provinceByHex.getCapital();
    }

    private boolean isHexGoodForDefenseTip(Hex hex) {
        if (!hex.containsBuilding()) {
            return false;
        }
        if (hex.objectInside == 4) {
            return true;
        }
        if (hex.objectInside == 7) {
            return true;
        }
        if (hex.objectInside == 3) {
            return true;
        }
        return false;
    }

    private boolean reactionBuildStuff(Hex focusedHex) {
        if (this.tipFactor.get() <= 0.0f || this.tipFactor.getDy() < 0.0d) {
            return false;
        }
        if (canBuildOnHex(focusedHex, this.tipType)) {
            buildSomethingOnHex(focusedHex);
        } else if (focusedHex.isInMoveZone() && focusedHex.colorIndex != this.gameController.getTurn() && isTipTypeUnit() && this.gameController.fieldController.selectedProvince.canBuildUnit(this.tipType)) {
            this.gameController.fieldController.buildUnit(this.gameController.fieldController.selectedProvince, focusedHex, this.tipType);
            this.gameController.fieldController.selectedProvince = this.gameController.fieldController.getProvinceByHex(focusedHex);
            this.gameController.fieldController.selectAdjacentHexes(focusedHex);
            resetTipType();
            SoundControllerYio.playSound(SoundControllerYio.soundBuild);
        } else {
            this.gameController.fieldController.setResponseAnimHex(focusedHex);
        }
        hideTip();
        Scenes.sceneFastConstructionPanel.checkToReappear();
        this.gameController.fieldController.hideMoveZone();
        return true;
    }

    private void updateIsSomethingSelected() {
        this.isSomethingSelected = this.gameController.fieldController.selectedHexes.size() > 0;
    }

    public int getCurrentTipPrice() {
        switch (this.tipType) {
            case 0:
                return 15;
            case 1:
            case 2:
            case 3:
            case 4:
                return this.tipType * 10;
            case 5:
                return this.gameController.fieldController.selectedProvince.getCurrentFarmPrice();
            case 6:
                return 35;
            case 7:
                return 10;
            default:
                return -1;
        }
    }

    private void buildSomethingOnHex(Hex focusedHex) {
        Province selectedProvince = this.gameController.fieldController.selectedProvince;
        switch (this.tipType) {
            case 0:
                if (!(focusedHex.containsTree() || focusedHex.containsUnit())) {
                    this.gameController.fieldController.buildTower(selectedProvince, focusedHex);
                    break;
                }
            case 1:
            case 2:
            case 3:
            case 4:
                this.gameController.fieldController.buildUnit(selectedProvince, focusedHex, this.tipType);
                break;
            case 5:
                if (!(focusedHex.containsTree() || focusedHex.containsUnit())) {
                    this.gameController.fieldController.buildFarm(selectedProvince, focusedHex);
                    break;
                }
            case 6:
                if (!(focusedHex.containsTree() || focusedHex.containsUnit())) {
                    this.gameController.fieldController.buildStrongTower(selectedProvince, focusedHex);
                    break;
                }
            case 7:
                if (focusedHex.isFree()) {
                    this.gameController.fieldController.buildTree(selectedProvince, focusedHex);
                    break;
                }
                break;
        }
        resetTipType();
        this.gameController.fieldController.setResponseAnimHex(focusedHex);
        SoundControllerYio.playSound(SoundControllerYio.soundBuild);
    }

    private boolean canBuildOnHex(Hex focusedHex, int tipType) {
        if (tipType == 6) {
            if (!focusedHex.selected || (focusedHex.containsBuilding() && focusedHex.objectInside != 4)) {
                return false;
            }
            return true;
        } else if (!focusedHex.selected || focusedHex.containsBuilding()) {
            return false;
        } else {
            return true;
        }
    }

    public void updateFocusedHex(int screenX, int screenY) {
        this.gameController.fieldController.updateFocusedHex((float) screenX, (float) screenY);
    }

    public void setSelectedUnit(Unit selectedUnit) {
        this.selectedUnit = selectedUnit;
    }

    public FactorYio getSelMoneyFactor() {
        return this.selMoneyFactor;
    }

    public FactorYio getBlackoutFactor() {
        return this.blackoutFactor;
    }
}
