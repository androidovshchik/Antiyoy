package io.androidovshchik.antiyoy.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import io.androidovshchik.antiyoy.Settings;
import io.androidovshchik.antiyoy.SoundControllerYio;
import io.androidovshchik.antiyoy.YioGdxGame;
import io.androidovshchik.antiyoy.factor_yio.FactorYio;
import io.androidovshchik.antiyoy.gameplay.diplomacy.DiplomacyManager;
import io.androidovshchik.antiyoy.gameplay.fog_of_war.FogOfWarManager;
import io.androidovshchik.antiyoy.gameplay.game_view.GameView;
import io.androidovshchik.antiyoy.gameplay.rules.GameRules;
import io.androidovshchik.antiyoy.menu.scenes.Scenes;
import io.androidovshchik.antiyoy.menu.scenes.editor.SceneEditorInstruments;
import io.androidovshchik.antiyoy.stuff.GraphicsYio;
import io.androidovshchik.antiyoy.stuff.PointYio;
import io.androidovshchik.antiyoy.stuff.Yio;

public class FieldController {
    public static int NEUTRAL_LANDS_INDEX = 7;
    public static final int SIZE_BIG = 4;
    public static final int SIZE_MEDIUM = 2;
    public static final int SIZE_SMALL = 1;
    public ArrayList<Hex> activeHexes;
    public ArrayList<Hex> animHexes;
    public float compensatoryOffset = 0.0f;
    public float cos60 = ((float) Math.cos(1.0471975511965976d));
    public Hex defTipHex;
    public FactorYio defenseTipFactor;
    public ArrayList<Hex> defenseTips;
    public DiplomacyManager diplomacyManager;
    public Hex emptyHex;
    public int fHeight;
    public int fWidth;
    public Hex[][] field;
    public PointYio fieldPos = new PointYio();
    public Hex focusedHex;
    public FogOfWarManager fogOfWarManager;
    public final GameController gameController;
    public float hexSize;
    public float hexStep1;
    public float hexStep2;
    public String initialLevelString;
    public boolean letsCheckAnimHexes;
    public int levelSize;
    public ArrayList<Hex> moveZone;
    private final MoveZoneDetection moveZoneDetection;
    public FactorYio moveZoneFactor;
    public int[] playerHexCount;
    public ArrayList<Province> provinces;
    public FactorYio responseAnimFactor;
    public Hex responseAnimHex;
    public ArrayList<Hex> selectedHexes;
    public Province selectedProvince;
    public int selectedProvinceMoney;
    public float sin60 = ((float) Math.sin(1.0471975511965976d));
    public ArrayList<Hex> solidObjects;
    public long timeToCheckAnimHexes;

    public FieldController(GameController gameController) {
        this.gameController = gameController;
        updateFieldPos();
        this.hexSize = 0.05f * ((float) Gdx.graphics.getWidth());
        this.hexStep1 = ((float) Math.sqrt(3.0d)) * this.hexSize;
        this.hexStep2 = (float) Yio.distance(0.0d, 0.0d, ((double) this.hexSize) * 1.5d, ((double) this.hexStep1) * 0.5d);
        this.fWidth = 46;
        this.fHeight = 30;
        this.activeHexes = new ArrayList();
        this.selectedHexes = new ArrayList();
        this.animHexes = new ArrayList();
        this.solidObjects = new ArrayList();
        this.moveZone = new ArrayList();
        this.field = (Hex[][]) Array.newInstance(Hex.class, new int[]{this.fWidth, this.fHeight});
        this.responseAnimFactor = new FactorYio();
        this.moveZoneFactor = new FactorYio();
        this.provinces = new ArrayList();
        this.emptyHex = new Hex(-1, -1, new PointYio(), this);
        this.emptyHex.active = false;
        this.defenseTipFactor = new FactorYio();
        this.defenseTips = new ArrayList();
        this.moveZoneDetection = new MoveZoneDetection(this);
        this.fogOfWarManager = new FogOfWarManager(this);
        this.diplomacyManager = new DiplomacyManager(this);
        this.initialLevelString = null;
    }

    private void updateFieldPos() {
        this.fieldPos.f145y = (-0.5f * GraphicsYio.height) + this.compensatoryOffset;
    }

    public void updateHexInsideLevelStatuses() {
        for (int i = 0; i < this.fWidth; i++) {
            for (int j = 0; j < this.fHeight; j++) {
                this.field[i][j].updateCanContainsObjects();
            }
        }
    }

    public void clearField() {
        this.gameController.selectionController.setSelectedUnit(null);
        this.solidObjects.clear();
        this.gameController.getUnitList().clear();
        clearProvincesList();
        this.moveZone.clear();
        clearActiveHexesList();
    }

    public void cleanOutAllHexesInField() {
        for (int i = 0; i < this.fWidth; i++) {
            for (int j = 0; j < this.fHeight; j++) {
                if (this.gameController.fieldController.field[i][j].active) {
                    this.gameController.cleanOutHex(this.gameController.fieldController.field[i][j]);
                }
            }
        }
    }

    public void clearProvincesList() {
        this.provinces.clear();
    }

    public void defaultValues() {
        this.selectedProvince = null;
        this.moveZoneFactor.setValues(0.0d, 0.0d);
        this.compensatoryOffset = 0.0f;
    }

    public void clearActiveHexesList() {
        ListIterator listIterator = this.activeHexes.listIterator();
        while (listIterator.hasNext()) {
            listIterator.next();
            listIterator.remove();
        }
    }

    public void createField() {
        clearField();
        updateFieldPos();
    }

    public void generateMap() {
        generateMap(GameRules.slayRules);
    }

    public void generateMap(boolean slayRules) {
        if (slayRules) {
            this.gameController.getMapGeneratorSlay().generateMap(this.gameController.getPredictableRandom(), this.field);
        } else {
            this.gameController.getMapGeneratorGeneric().generateMap(this.gameController.getPredictableRandom(), this.field);
        }
        detectProvinces();
        this.gameController.selectionController.deselectAll();
        detectNeutralLands();
        this.gameController.takeAwaySomeMoneyToAchieveBalance();
    }

    public void detectNeutralLands() {
        if (!GameRules.slayRules) {
            Iterator it = this.activeHexes.iterator();
            while (it.hasNext()) {
                ((Hex) it.next()).genFlag = false;
            }
            it = this.provinces.iterator();
            while (it.hasNext()) {
                Iterator it2 = ((Province) it.next()).hexList.iterator();
                while (it2.hasNext()) {
                    ((Hex) it2.next()).genFlag = true;
                }
            }
            it = this.activeHexes.iterator();
            while (it.hasNext()) {
                Hex activeHex = (Hex) it.next();
                if (!activeHex.genFlag) {
                    activeHex.setColorIndex(NEUTRAL_LANDS_INDEX);
                }
            }
        }
    }

    public void killUnitByStarvation(Hex hex) {
        cleanOutHex(hex);
        addSolidObject(hex, 5);
        hex.animFactor.appear(1, 2.0d);
        this.gameController.replayManager.onUnitDiedFromStarvation(hex);
    }

    public void killEveryoneByStarvation(Province province) {
        Iterator it = province.hexList.iterator();
        while (it.hasNext()) {
            Hex hex = (Hex) it.next();
            if (hex.containsUnit()) {
                killUnitByStarvation(hex);
            }
        }
    }

    public void moveResponseAnimHex() {
        if (this.responseAnimHex != null) {
            this.responseAnimFactor.move();
            if (((double) this.responseAnimFactor.get()) < 0.01d) {
                this.responseAnimHex = null;
            }
        }
    }

    public void moveAnimHexes() {
        Iterator it = this.animHexes.iterator();
        while (it.hasNext()) {
            Hex hex = (Hex) it.next();
            if (!hex.selected) {
                hex.move();
            }
            if (!this.letsCheckAnimHexes && ((double) hex.animFactor.get()) > 0.99d) {
                this.letsCheckAnimHexes = true;
            }
            if (hex.animFactor.get() < 1.0f) {
                hex.animFactor.setValues(1.0d, 0.0d);
            }
        }
    }

    public int numberOfActiveProvinces() {
        int c = 0;
        Iterator it = this.provinces.iterator();
        while (it.hasNext()) {
            if (!((Hex) ((Province) it.next()).hexList.get(0)).isNeutral()) {
                c++;
            }
        }
        return c;
    }

    public int[] getPlayerHexCount() {
        for (int i = 0; i < this.playerHexCount.length; i++) {
            this.playerHexCount[i] = 0;
        }
        Iterator it = this.activeHexes.iterator();
        while (it.hasNext()) {
            Hex activeHex = (Hex) it.next();
            if (!activeHex.isNeutral() && activeHex.isInProvince() && activeHex.colorIndex >= 0 && activeHex.colorIndex < this.playerHexCount.length) {
                int[] iArr = this.playerHexCount;
                int i2 = activeHex.colorIndex;
                iArr[i2] = iArr[i2] + 1;
            }
        }
        return this.playerHexCount;
    }

    public String getFullLevelString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(getBasicInfoString());
        stringBuffer.append("/");
        stringBuffer.append(this.gameController.gameSaver.getActiveHexesString());
        return stringBuffer.toString();
    }

    private String getBasicInfoString() {
        StringBuilder builder = new StringBuilder();
        builder.append(GameRules.difficulty).append(" ");
        builder.append(this.levelSize).append(" ");
        builder.append(this.gameController.playersNumber).append(" ");
        builder.append(GameRules.colorNumber).append("");
        return builder.toString();
    }

    private boolean checkRefuseStatistics() {
        RefuseStatistics instance = RefuseStatistics.getInstance();
        int sum = instance.refusedEarlyGameEnd + instance.acceptedEarlyGameEnd;
        if (sum >= 5 && ((double) instance.acceptedEarlyGameEnd) / ((double) sum) < 0.1d) {
            return false;
        }
        return true;
    }

    public int possibleWinner() {
        if (!checkRefuseStatistics()) {
            return -1;
        }
        int numberOfAllHexes = this.activeHexes.size();
        int[] playerHexCount = getPlayerHexCount();
        for (int i = 0; i < playerHexCount.length; i++) {
            if (((double) playerHexCount[i]) > 0.7d * ((double) numberOfAllHexes)) {
                return i;
            }
        }
        return -1;
    }

    public boolean hasAtLeastOneProvince() {
        return this.provinces.size() > 0;
    }

    public int numberOfProvincesWithColor(int color) {
        int count = 0;
        Iterator it = this.provinces.iterator();
        while (it.hasNext()) {
            if (((Province) it.next()).getColor() == color) {
                count++;
            }
        }
        return count;
    }

    public void transformGraves() {
        Iterator it = this.activeHexes.iterator();
        while (it.hasNext()) {
            Hex hex = (Hex) it.next();
            if (this.gameController.isCurrentTurn(hex.colorIndex) && hex.objectInside == 5) {
                spawnTree(hex);
                hex.blockToTreeFromExpanding = true;
            }
        }
    }

    public void detectProvinces() {
        if (!this.gameController.isInEditorMode()) {
            clearProvincesList();
            MoveZoneDetection.unFlagAllHexesInArrayList(this.activeHexes);
            ArrayList<Hex> tempList = new ArrayList();
            ArrayList<Hex> propagationList = new ArrayList();
            Iterator it = this.activeHexes.iterator();
            while (it.hasNext()) {
                Hex hex = (Hex) it.next();
                if (!(hex.isNeutral() || hex.flag)) {
                    tempList.clear();
                    propagationList.clear();
                    propagationList.add(hex);
                    hex.flag = true;
                    propagateHex(tempList, propagationList);
                    if (tempList.size() >= 2) {
                        addProvince(new Province(this.gameController, tempList));
                    }
                }
            }
            it = this.provinces.iterator();
            while (it.hasNext()) {
                Province province = (Province) it.next();
                if (!province.hasCapital()) {
                    province.placeCapitalInRandomPlace(this.gameController.predictableRandom);
                }
            }
        }
    }

    private void propagateHex(ArrayList<Hex> tempList, ArrayList<Hex> propagationList) {
        while (propagationList.size() > 0) {
            Hex tempHex = (Hex) propagationList.get(0);
            tempList.add(tempHex);
            propagationList.remove(0);
            for (int i = 0; i < 6; i++) {
                Hex adjHex = tempHex.getAdjacentHex(i);
                if (adjHex.active && adjHex.sameColor(tempHex) && !adjHex.flag) {
                    propagationList.add(adjHex);
                    adjHex.flag = true;
                }
            }
        }
    }

    public void forceAnimEndInHex(Hex hex) {
        hex.animFactor.setValues(1.0d, 0.0d);
    }

    public int howManyPalms() {
        int c = 0;
        Iterator it = this.activeHexes.iterator();
        while (it.hasNext()) {
            if (((Hex) it.next()).objectInside == 2) {
                c++;
            }
        }
        return c;
    }

    public void expandTrees() {
        if (!GameRules.replayMode) {
            int i;
            ArrayList<Hex> newPalmsList = getNewPalmsList();
            ArrayList<Hex> newPinesList = getNewPinesList();
            for (i = newPalmsList.size() - 1; i >= 0; i--) {
                spawnPalm((Hex) newPalmsList.get(i));
            }
            for (i = newPinesList.size() - 1; i >= 0; i--) {
                spawnPine((Hex) newPinesList.get(i));
            }
            Iterator it = this.activeHexes.iterator();
            while (it.hasNext()) {
                Hex activeHex = (Hex) it.next();
                if (activeHex.containsTree() && activeHex.blockToTreeFromExpanding) {
                    activeHex.blockToTreeFromExpanding = false;
                }
            }
        }
    }

    private ArrayList<Hex> getNewPinesList() {
        ArrayList<Hex> newPinesList = new ArrayList();
        Iterator it = this.activeHexes.iterator();
        while (it.hasNext()) {
            Hex hex = (Hex) it.next();
            if (this.gameController.ruleset.canSpawnPineOnHex(hex)) {
                newPinesList.add(hex);
            }
        }
        return newPinesList;
    }

    private ArrayList<Hex> getNewPalmsList() {
        ArrayList<Hex> newPalmsList = new ArrayList();
        Iterator it = this.activeHexes.iterator();
        while (it.hasNext()) {
            Hex hex = (Hex) it.next();
            if (this.gameController.ruleset.canSpawnPalmOnHex(hex)) {
                newPalmsList.add(hex);
            }
        }
        return newPalmsList;
    }

    private void spawnPine(Hex hex) {
        if (hex.canContainObjects) {
            addSolidObject(hex, 1);
            addAnimHex(hex);
            hex.animFactor.setValues(1.0d, 0.0d);
            this.gameController.replayManager.onPineSpawned(hex);
        }
    }

    private void spawnPalm(Hex hex) {
        if (hex.canContainObjects) {
            addSolidObject(hex, 2);
            addAnimHex(hex);
            hex.animFactor.setValues(1.0d, 0.0d);
            this.gameController.replayManager.onPalmSpawned(hex);
        }
    }

    public void createPlayerHexCount() {
        this.playerHexCount = new int[GameRules.colorNumber];
    }

    public void checkAnimHexes() {
        if (this.gameController.isSomethingMoving()) {
            this.timeToCheckAnimHexes = this.gameController.getCurrentTime() + 100;
            return;
        }
        this.letsCheckAnimHexes = false;
        ListIterator iterator = this.animHexes.listIterator();
        while (iterator.hasNext()) {
            Hex h = (Hex) iterator.next();
            if (((double) h.animFactor.get()) > 0.99d && ((!h.containsUnit() || h.unit.moveFactor.get() >= 1.0f) && System.currentTimeMillis() > h.animStartTime + 250)) {
                h.changingColor = false;
                iterator.remove();
            }
        }
    }

    public boolean atLeastOneUnitIsReadyToMove() {
        int size = this.gameController.getUnitList().size();
        Iterator it = this.gameController.getUnitList().iterator();
        while (it.hasNext()) {
            if (((Unit) it.next()).isReadyToMove()) {
                return true;
            }
        }
        return false;
    }

    public int getPredictionForWinner() {
        int[] numbers = new int[GameRules.colorNumber];
        Iterator it = this.activeHexes.iterator();
        while (it.hasNext()) {
            Hex activeHex = (Hex) it.next();
            if (!activeHex.isNeutral()) {
                int i = activeHex.colorIndex;
                numbers[i] = numbers[i] + 1;
            }
        }
        int max = numbers[0];
        int maxIndex = 0;
        for (int i2 = 0; i2 < numbers.length; i2++) {
            if (numbers[i2] > max) {
                max = numbers[i2];
                maxIndex = i2;
            }
        }
        return maxIndex;
    }

    public boolean areConditionsGoodForPlayer() {
        int[] numbers = new int[GameRules.colorNumber];
        Iterator it = this.activeHexes.iterator();
        while (it.hasNext()) {
            Hex activeHex = (Hex) it.next();
            if (!activeHex.isNeutral()) {
                int i = activeHex.colorIndex;
                numbers[i] = numbers[i] + 1;
            }
        }
        if (GameController.maxNumberFromArray(numbers) - numbers[0] < 2) {
            return true;
        }
        return false;
    }

    public void onEndCreation() {
        clearAnims();
        updateHexInsideLevelStatuses();
        this.defenseTips.clear();
        this.diplomacyManager.onEndCreation();
        this.fogOfWarManager.onEndCreation();
        updateInitialLevelString();
    }

    private void updateInitialLevelString() {
        this.initialLevelString = getFullLevelString();
    }

    public void clearAnims() {
        ListIterator iterator = this.animHexes.listIterator();
        while (iterator.hasNext()) {
            iterator.next();
            iterator.remove();
        }
    }

    public void createFieldMatrix() {
        for (int i = 0; i < this.fWidth; i++) {
            this.field[i] = new Hex[this.fHeight];
            for (int j = 0; j < this.fHeight; j++) {
                this.field[i][j] = new Hex(i, j, this.fieldPos, this);
                this.field[i][j].ignoreTouch = false;
            }
        }
    }

    public void marchUnitsToHex(Hex toWhere) {
        if (this.gameController.selectionController.isSomethingSelected() && toWhere.isSelected()) {
            if (this.selectedProvince.hasSomeoneReadyToMove()) {
                this.gameController.takeSnapshot();
                Iterator it = this.selectedProvince.hexList.iterator();
                while (it.hasNext()) {
                    Hex hex = (Hex) it.next();
                    if (hex.containsUnit() && hex.unit.isReadyToMove()) {
                        hex.unit.marchToHex(toWhere, this.selectedProvince);
                    }
                }
            }
            setResponseAnimHex(toWhere);
            SoundControllerYio.playSound(SoundControllerYio.soundHoldToMarch);
        }
    }

    public void setResponseAnimHex(Hex hex) {
        this.responseAnimHex = hex;
        this.responseAnimFactor.setValues(1.0d, SceneEditorInstruments.ICON_SIZE);
        this.responseAnimFactor.destroy(1, 2.0d);
    }

    public void hideMoveZone() {
        this.moveZoneFactor.destroy(1, 5.0d);
        this.gameController.selectionController.getBlackoutFactor().destroy(1, 5.0d);
    }

    public void selectAdjacentHexes(Hex startHex) {
        setSelectedProvince(startHex);
        ListIterator listIterator = this.selectedHexes.listIterator();
        Iterator it = this.selectedProvince.hexList.iterator();
        while (it.hasNext()) {
            Hex hex = (Hex) it.next();
            hex.select();
            if (!this.selectedHexes.contains(hex)) {
                listIterator.add(hex);
            }
        }
        showBuildOverlay();
        this.gameController.updateBalanceString();
    }

    private void showBuildOverlay() {
        if (Settings.fastConstruction) {
            Scenes.sceneFastConstructionPanel.create();
        } else {
            Scenes.sceneSelectionOverlay.create();
        }
    }

    public void setSelectedProvince(Hex hex) {
        this.selectedProvince = getProvinceByHex(hex);
        this.selectedProvinceMoney = this.selectedProvince.money;
        this.gameController.selectionController.getSelMoneyFactor().setDy(0.0d);
        this.gameController.selectionController.getSelMoneyFactor().appear(3, 2.0d);
    }

    public String getColorName(int colorIndex) {
        return this.gameController.yioGdxGame.menuControllerYio.getColorNameByIndexWithOffset(colorIndex, "_player");
    }

    public void updateHexPositions() {
        updateFieldPos();
        for (int i = 0; i < this.fWidth; i++) {
            for (int j = 0; j < this.fHeight; j++) {
                Hex hex = this.field[i][j];
                hex.updatePos();
                if (hex.containsUnit()) {
                    hex.unit.updateCurrentPos();
                }
            }
        }
    }

    public Hex getHexByPos(double x, double y) {
        int j = (int) ((x - ((double) this.fieldPos.f144x)) / ((double) (this.hexStep2 * this.sin60)));
        int i = (int) (((y - ((double) this.fieldPos.f145y)) - ((double) ((this.hexStep2 * ((float) j)) * this.cos60))) / ((double) this.hexStep1));
        if (i < 0 || i > this.fWidth - 1 || j < 0 || j > this.fHeight - 1) {
            return null;
        }
        Hex resHex = this.field[i][j];
        x -= (double) this.gameController.getYioGdxGame().gameView.hexViewSize;
        y -= (double) this.gameController.getYioGdxGame().gameView.hexViewSize;
        double minDistance = Yio.distance((double) resHex.pos.f144x, (double) resHex.pos.f145y, x, y);
        for (int k = 0; k < 6; k++) {
            Hex adjHex = adjacentHex(this.field[i][j], k);
            if (adjHex != null && adjHex.active) {
                double currentDistance = Yio.distance((double) adjHex.pos.f144x, (double) adjHex.pos.f145y, x, y);
                if (currentDistance < minDistance) {
                    minDistance = currentDistance;
                    resHex = adjHex;
                }
            }
        }
        return resHex;
    }

    public Hex getHex(int i, int j) {
        if (i < 0 || i > this.fWidth - 1 || j < 0 || j > this.fHeight - 1) {
            return null;
        }
        return this.field[i][j];
    }

    public boolean isPointInsideLevelBoundsHorizontally(PointYio pointYio) {
        if (pointYio.f144x >= this.fieldPos.f144x + (this.hexSize / 2.0f) && pointYio.f144x <= this.fieldPos.f144x + this.gameController.boundWidth) {
            return true;
        }
        return false;
    }

    public boolean isPointInsideLevelBoundsWithOffset(PointYio pointYio, float offset) {
        if (pointYio.f144x >= (this.fieldPos.f144x + (this.hexSize / 2.0f)) - offset && pointYio.f144x <= (this.fieldPos.f144x + this.gameController.boundWidth) + offset && pointYio.f145y >= (this.hexSize / 2.0f) - offset && pointYio.f145y <= this.gameController.boundHeight + offset) {
            return true;
        }
        return false;
    }

    public Hex adjacentHex(int i, int j, int direction) {
        switch (direction) {
            case 0:
                if (i >= this.fWidth - 1) {
                    return this.emptyHex;
                }
                return this.field[i + 1][j];
            case 1:
                if (j >= this.fHeight - 1) {
                    return this.emptyHex;
                }
                return this.field[i][j + 1];
            case 2:
                if (i <= 0 || j >= this.fHeight - 1) {
                    return this.emptyHex;
                }
                return this.field[i - 1][j + 1];
            case 3:
                if (i <= 0) {
                    return this.emptyHex;
                }
                return this.field[i - 1][j];
            case 4:
                if (j <= 0) {
                    return this.emptyHex;
                }
                return this.field[i][j - 1];
            case 5:
                if (i >= this.fWidth - 1 || j <= 0) {
                    return this.emptyHex;
                }
                return this.field[i + 1][j - 1];
            default:
                return this.emptyHex;
        }
    }

    public void spawnTree(Hex hex) {
        if (!hex.active) {
            return;
        }
        if (hex.isNearWater()) {
            addSolidObject(hex, 2);
        } else {
            addSolidObject(hex, 1);
        }
    }

    public void addSolidObject(Hex hex, int type) {
        if (hex != null && hex.active && hex.objectInside != type && hex.canContainObjects) {
            if (this.solidObjects.contains(hex)) {
                cleanOutHex(hex);
            }
            hex.setObjectInside(type);
            this.solidObjects.listIterator().add(hex);
        }
    }

    public void cleanOutHex(Hex hex) {
        if (hex.containsUnit()) {
            this.gameController.getMatchStatistics().unitWasKilled();
            this.gameController.getUnitList().remove(hex.unit);
            hex.unit = null;
        }
        hex.setObjectInside(0);
        addAnimHex(hex);
        ListIterator iterator = this.solidObjects.listIterator();
        while (iterator.hasNext()) {
            if (iterator.next() == hex) {
                iterator.remove();
                return;
            }
        }
    }

    public void destroyBuildingsOnHex(Hex hex) {
        boolean hadHouse = hex.objectInside == 3;
        if (hex.containsBuilding()) {
            cleanOutHex(hex);
        }
        if (hadHouse) {
            spawnTree(hex);
        }
    }

    public boolean buildUnit(Province province, Hex hex, int strength) {
        if (province == null || hex == null) {
            return false;
        }
        if (province.canBuildUnit(strength)) {
            if (hex.sameColor(province) && hex.containsUnit() && !this.gameController.canMergeUnits(strength, hex.unit.strength)) {
                return false;
            }
            this.gameController.takeSnapshot();
            province.money -= strength * 10;
            this.gameController.getMatchStatistics().moneyWereSpent(strength * 10);
            this.gameController.replayManager.onUnitBuilt(province, hex, strength);
            updateSelectedProvinceMoney();
            if (!hex.sameColor(province)) {
                setHexColor(hex, province.getColor());
                addUnit(hex, strength);
                hex.unit.setReadyToMove(false);
                hex.unit.stopJumping();
                province.addHex(hex);
                addAnimHex(hex);
                this.gameController.updateCacheOnceAfterSomeTime();
            } else if (hex.containsUnit()) {
                Unit bUnit = new Unit(this.gameController, hex, strength);
                bUnit.setReadyToMove(true);
                MatchStatistics matchStatistics = this.gameController.matchStatistics;
                matchStatistics.unitsDied++;
                this.gameController.mergeUnits(hex, bUnit, hex.unit);
            } else {
                addUnit(hex, strength);
            }
            this.gameController.updateBalanceString();
            return true;
        } else if (!this.gameController.isPlayerTurn()) {
            return false;
        } else {
            this.gameController.tickleMoneySign();
            return false;
        }
    }

    public boolean buildTower(Province province, Hex hex) {
        if (province == null) {
            return false;
        }
        if (province.hasMoneyForTower()) {
            this.gameController.takeSnapshot();
            this.gameController.replayManager.onTowerBuilt(hex, false);
            addSolidObject(hex, 4);
            addAnimHex(hex);
            province.money -= 15;
            this.gameController.getMatchStatistics().moneyWereSpent(15);
            updateSelectedProvinceMoney();
            this.gameController.updateCacheOnceAfterSomeTime();
            return true;
        } else if (!this.gameController.isPlayerTurn()) {
            return false;
        } else {
            this.gameController.tickleMoneySign();
            return false;
        }
    }

    public boolean buildStrongTower(Province province, Hex hex) {
        if (province == null) {
            return false;
        }
        if (province.hasMoneyForStrongTower()) {
            this.gameController.takeSnapshot();
            this.gameController.replayManager.onTowerBuilt(hex, true);
            addSolidObject(hex, 7);
            addAnimHex(hex);
            province.money -= 35;
            this.gameController.getMatchStatistics().moneyWereSpent(35);
            updateSelectedProvinceMoney();
            this.gameController.updateCacheOnceAfterSomeTime();
            return true;
        } else if (!this.gameController.isPlayerTurn()) {
            return false;
        } else {
            this.gameController.tickleMoneySign();
            return false;
        }
    }

    public boolean buildFarm(Province province, Hex hex) {
        if (province == null) {
            return false;
        }
        if (!hex.hasThisObjectNearby(3) && !hex.hasThisObjectNearby(6)) {
            return false;
        }
        if (province.hasMoneyForFarm()) {
            this.gameController.takeSnapshot();
            this.gameController.replayManager.onFarmBuilt(hex);
            province.money -= province.getCurrentFarmPrice();
            this.gameController.getMatchStatistics().moneyWereSpent(province.getCurrentFarmPrice());
            addSolidObject(hex, 6);
            addAnimHex(hex);
            updateSelectedProvinceMoney();
            this.gameController.updateCacheOnceAfterSomeTime();
            return true;
        } else if (!this.gameController.isPlayerTurn()) {
            return false;
        } else {
            this.gameController.tickleMoneySign();
            return false;
        }
    }

    public boolean buildTree(Province province, Hex hex) {
        if (province == null) {
            return false;
        }
        if (province.hasMoneyForTree()) {
            this.gameController.takeSnapshot();
            spawnTree(hex);
            addAnimHex(hex);
            province.money -= 10;
            this.gameController.getMatchStatistics().moneyWereSpent(10);
            updateSelectedProvinceMoney();
            this.gameController.updateCacheOnceAfterSomeTime();
            return true;
        } else if (!this.gameController.isPlayerTurn()) {
            return false;
        } else {
            this.gameController.tickleMoneySign();
            return false;
        }
    }

    public void updateSelectedProvinceMoney() {
        if (this.selectedProvince != null) {
            this.selectedProvinceMoney = this.selectedProvince.money;
        } else {
            this.selectedProvinceMoney = -1;
        }
        this.gameController.updateBalanceString();
    }

    public Unit addUnit(Hex hex, int strength) {
        if (hex == null) {
            return null;
        }
        if (hex.containsObject()) {
            this.gameController.ruleset.onUnitAdd(hex);
            cleanOutHex(hex);
            this.gameController.updateCacheOnceAfterSomeTime();
            hex.addUnit(strength);
        } else {
            hex.addUnit(strength);
            if (this.gameController.isCurrentTurn(hex.colorIndex)) {
                hex.unit.setReadyToMove(true);
                hex.unit.startJumping();
            }
        }
        return hex.unit;
    }

    public void addProvince(Province province) {
        if (!this.provinces.contains(province)) {
            if (containsEqualProvince(province)) {
                System.out.println("Problem in FieldController.addProvince()");
                Yio.printStackTrace();
                return;
            }
            this.provinces.add(province);
        }
    }

    public boolean containsEqualProvince(Province province) {
        Iterator it = this.provinces.iterator();
        while (it.hasNext()) {
            if (((Province) it.next()).equals(province)) {
                return true;
            }
        }
        return false;
    }

    public Hex adjacentHex(Hex hex, int direction) {
        return adjacentHex(hex.index1, hex.index2, direction);
    }

    public boolean hexHasSelectedNearby(Hex hex) {
        for (int i = 0; i < 6; i++) {
            if (hex.getAdjacentHex(i).selected) {
                return true;
            }
        }
        return false;
    }

    public static float distanceBetweenHexes(Hex one, Hex two) {
        return (float) one.getPos().distanceTo(two.getPos());
    }

    public void detectAndShowMoveZoneForBuildingUnit(int strength) {
        detectAndShowMoveZone((Hex) this.selectedHexes.get(0), strength);
    }

    public boolean isSomethingSelected() {
        return this.selectedHexes.size() > 0;
    }

    public void giveMoneyToPlayerProvinces(int amount) {
        Iterator it = this.provinces.iterator();
        while (it.hasNext()) {
            Province province = (Province) it.next();
            if (province.getColor() == 0) {
                province.money += amount;
            }
        }
    }

    public void detectAndShowMoveZoneForFarm() {
        this.moveZone = this.moveZoneDetection.detectMoveZoneForFarm();
        checkToForceMoveZoneAnims();
        this.moveZoneFactor.setValues(0.0d, 0.0d);
        this.moveZoneFactor.appear(3, 1.5d);
        this.gameController.selectionController.getBlackoutFactor().appear(3, 1.5d);
    }

    public ArrayList<Hex> detectMoveZoneForFarm() {
        return this.moveZoneDetection.detectMoveZoneForFarm();
    }

    public ArrayList<Hex> detectMoveZone(Hex startHex, int strength) {
        return this.moveZoneDetection.detectMoveZone(startHex, strength);
    }

    public ArrayList<Hex> detectMoveZone(Hex startHex, int strength, int moveLimit) {
        return this.moveZoneDetection.detectMoveZone(startHex, strength, moveLimit);
    }

    public void detectAndShowMoveZone(Hex startHex, int strength) {
        detectAndShowMoveZone(startHex, strength, 9001);
    }

    public void detectAndShowMoveZone(Hex startHex, int strength, int moveLimit) {
        this.moveZone = this.moveZoneDetection.detectMoveZone(startHex, strength, moveLimit);
        checkToForceMoveZoneAnims();
        this.moveZoneFactor.setValues(0.0d, 0.0d);
        this.moveZoneFactor.appear(3, 1.5d);
        this.gameController.selectionController.getBlackoutFactor().appear(3, 1.5d);
    }

    public void checkToForceMoveZoneAnims() {
        if (((Hex) this.moveZone.get(0)).selectionFactor.get() < 1.0f) {
            Iterator it = this.moveZone.iterator();
            while (it.hasNext()) {
                ((Hex) it.next()).animFactor.setValues(1.0d, 0.0d);
            }
        }
    }

    public void clearMoveZone() {
        for (int i = this.moveZone.size() - 1; i >= 0; i--) {
            ((Hex) this.moveZone.get(i)).inMoveZone = false;
        }
        this.moveZone.clear();
    }

    public boolean hexHasNeighbourWithColor(Hex hex, int color) {
        for (int i = 0; i < 6; i++) {
            Hex neighbour = hex.getAdjacentHex(i);
            if (neighbour != null && neighbour.active && neighbour.sameColor(color)) {
                return true;
            }
        }
        return false;
    }

    public void addAnimHex(Hex hex) {
        if (!this.animHexes.contains(hex)) {
            this.animHexes.listIterator().add(hex);
            hex.animFactor.setValues(0.0d, 0.0d);
            hex.animFactor.appear(1, 1.0d);
            hex.animStartTime = System.currentTimeMillis();
            this.gameController.updateCacheOnceAfterSomeTime();
        }
    }

    public Province findProvinceCopy(Province src) {
        Iterator it = src.hexList.iterator();
        while (it.hasNext()) {
            Province result = getProvinceByHex((Hex) it.next());
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    public Province getProvinceByHex(Hex hex) {
        Iterator it = this.provinces.iterator();
        while (it.hasNext()) {
            Province province = (Province) it.next();
            if (province.containsHex(hex)) {
                return province;
            }
        }
        return null;
    }

    public Hex getRandomActivehex() {
        return (Hex) this.activeHexes.get(YioGdxGame.random.nextInt(this.activeHexes.size()));
    }

    public Province getMaxProvinceFromList(ArrayList<Province> list) {
        if (list.size() == 0) {
            return null;
        }
        Province max = (Province) list.get(0);
        for (int k = list.size() - 1; k >= 0; k--) {
            Province temp = (Province) list.get(k);
            if (temp.hexList.size() > max.hexList.size()) {
                max = temp;
            }
        }
        return max;
    }

    public void splitProvince(Hex hex, int color) {
        Province oldProvince = getProvinceByHex(hex);
        if (oldProvince != null) {
            MoveZoneDetection.unFlagAllHexesInArrayList(oldProvince.hexList);
            ArrayList<Hex> tempList = new ArrayList();
            ArrayList<Hex> propagationList = new ArrayList();
            ArrayList<Province> provincesAdded = new ArrayList();
            hex.flag = true;
            this.gameController.getPredictableRandom().setSeed((long) (hex.index1 + hex.index2));
            for (int k = 0; k < 6; k++) {
                Hex startHex = hex.getAdjacentHex(k);
                if (startHex.active && startHex.colorIndex == color && !startHex.flag) {
                    tempList.clear();
                    propagationList.clear();
                    propagationList.add(startHex);
                    startHex.flag = true;
                    while (propagationList.size() > 0) {
                        Hex tempHex = (Hex) propagationList.get(0);
                        tempList.add(tempHex);
                        propagationList.remove(0);
                        for (int i = 0; i < 6; i++) {
                            Hex adjHex = tempHex.getAdjacentHex(i);
                            if (adjHex.active && adjHex.sameColor(tempHex) && !adjHex.flag) {
                                propagationList.add(adjHex);
                                adjHex.flag = true;
                            }
                        }
                    }
                    if (tempList.size() >= 2) {
                        Province province = new Province(this.gameController, tempList);
                        province.money = 0;
                        if (!province.hasCapital()) {
                            province.placeCapitalInRandomPlace(this.gameController.getPredictableRandom());
                        }
                        addProvince(province);
                        provincesAdded.add(province);
                    } else {
                        destroyBuildingsOnHex(startHex);
                    }
                }
            }
            if (provincesAdded.size() > 0 && hex.objectInside != 3) {
                getMaxProvinceFromList(provincesAdded).money = oldProvince.money;
            }
            this.provinces.remove(oldProvince);
            this.diplomacyManager.updateEntityAliveStatus(color);
        }
    }

    public void checkToUniteProvinces(Hex hex) {
        ArrayList<Province> adjacentProvinces = new ArrayList();
        for (int i = 0; i < 6; i++) {
            Province p = getProvinceByHex(hex.getAdjacentHex(i));
            if (!(p == null || !hex.sameColor(p) || adjacentProvinces.contains(p))) {
                adjacentProvinces.add(p);
            }
        }
        if (adjacentProvinces.size() >= 2) {
            int sum = 0;
            Hex capital = getMaxProvinceFromList(adjacentProvinces).getCapital();
            ArrayList<Hex> hexArrayList = new ArrayList();
            Iterator it = adjacentProvinces.iterator();
            while (it.hasNext()) {
                Province province = (Province) it.next();
                sum += province.money;
                hexArrayList.addAll(province.hexList);
                this.provinces.remove(province);
            }
            Province unitedProvince = new Province(this.gameController, hexArrayList);
            unitedProvince.money = sum;
            unitedProvince.setCapital(capital);
            addProvince(unitedProvince);
        }
    }

    public void joinHexToAdjacentProvince(Hex hex) {
        int i = 0;
        while (i < 6) {
            Province p = getProvinceByHex(hex.getAdjacentHex(i));
            if (p == null || !hex.sameColor(p)) {
                i++;
            } else {
                p.addHex(hex);
                for (int j = 0; j < 6; j++) {
                    Hex h = adjacentHex(hex, j);
                    if (h.active && h.sameColor(hex) && getProvinceByHex(h) == null) {
                        p.addHex(h);
                    }
                }
                return;
            }
        }
    }

    public void setLevelSize(int levelSize) {
        this.levelSize = levelSize;
    }

    public void updatePointByHexIndexes(PointYio pointYio, int index1, int index2) {
        pointYio.f144x = this.fieldPos.f144x + ((this.hexStep2 * ((float) index2)) * this.sin60);
        pointYio.f145y = (this.fieldPos.f145y + (this.hexStep1 * ((float) index1))) + ((this.hexStep2 * ((float) index2)) * this.cos60);
    }

    public void setHexColor(Hex hex, int color) {
        cleanOutHex(hex);
        int oldColor = hex.colorIndex;
        hex.setColorIndex(color);
        splitProvince(hex, oldColor);
        checkToUniteProvinces(hex);
        joinHexToAdjacentProvince(hex);
        ListIterator animIterator = this.animHexes.listIterator();
        for (int i = 0; i < 6; i++) {
            Hex h = hex.getAdjacentHex(i);
            if (h != null && h.active && h.sameColor(hex)) {
                if (!this.animHexes.contains(h)) {
                    animIterator.add(h);
                }
                if (!h.changingColor) {
                    h.animFactor.setValues(1.0d, 0.0d);
                }
            }
        }
        hex.changingColor = true;
        if (!this.animHexes.contains(hex)) {
            animIterator.add(hex);
        }
        hex.animFactor.setValues(0.0d, 0.0d);
        hex.animFactor.appear(1, 1.0d);
        if (!this.gameController.isPlayerTurn()) {
            forceAnimEndInHex(hex);
        }
    }

    public void updateFocusedHex() {
        updateFocusedHex(this.gameController.touchPoint.f144x, this.gameController.touchPoint.f145y);
    }

    public void updateFocusedHex(float screenX, float screenY) {
        OrthographicCamera orthoCam = this.gameController.cameraController.orthoCam;
        SelectionController selectionController = this.gameController.selectionController;
        selectionController.selectX = ((screenX - (GraphicsYio.width * 0.5f)) * orthoCam.zoom) + orthoCam.position.f136x;
        selectionController.selectY = ((screenY - (GraphicsYio.height * 0.5f)) * orthoCam.zoom) + orthoCam.position.f137y;
        GameView gameView = this.gameController.getYioGdxGame().gameView;
        this.focusedHex = getHexByPos((double) (selectionController.selectX + gameView.hexViewSize), (double) (selectionController.selectY + gameView.hexViewSize));
    }
}
