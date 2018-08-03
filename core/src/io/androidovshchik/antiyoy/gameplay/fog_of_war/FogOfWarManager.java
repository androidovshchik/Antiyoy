package io.androidovshchik.antiyoy.gameplay.fog_of_war;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import io.androidovshchik.antiyoy.gameplay.FieldController;
import io.androidovshchik.antiyoy.gameplay.Hex;
import io.androidovshchik.antiyoy.gameplay.Province;
import io.androidovshchik.antiyoy.gameplay.diplomacy.DiplomaticEntity;
import io.androidovshchik.antiyoy.gameplay.rules.GameRules;
import io.androidovshchik.antiyoy.stuff.PointYio;
import io.androidovshchik.antiyoy.stuff.RectangleYio;
import io.androidovshchik.antiyoy.stuff.object_pool.ObjectPoolYio;
import io.androidovshchik.antiyoy.stuff.object_pool.ReusableYio;

public class FogOfWarManager {
    public static final int OFFSET = 4;
    public RectangleYio bottomBlock;
    public FieldController fieldController;
    public HashMap<Hex, FogPoint> fogMap = new HashMap();
    private boolean foundPlayersProvince;
    public RectangleYio leftBlock;
    LightUpAlgorithm lightUpAlgorithm;
    ObjectPoolYio<FogPoint> poolFogPoints;
    ObjectPoolYio<Hex> poolKeys;
    ObjectPoolYio<FogSlice> poolSlices;
    public RectangleYio rightBlock;
    SliceUpdater sliceUpdater;
    public RectangleYio topBlock;
    float viewOffset;
    public ArrayList<FogSlice> viewSlices;
    public RectangleYio visibleArea;

    class C01041 extends ObjectPoolYio<FogPoint> {
        C01041() {
        }

        public FogPoint makeNewObject() {
            return new FogPoint(FogOfWarManager.this);
        }
    }

    class C01052 extends ObjectPoolYio<Hex> {
        C01052() {
        }

        public Hex makeNewObject() {
            return new Hex(-1, -1, null, null);
        }
    }

    class C01063 extends ObjectPoolYio<FogSlice> {
        C01063() {
        }

        public FogSlice makeNewObject() {
            return new FogSlice();
        }
    }

    public FogOfWarManager(FieldController fieldController) {
        this.fieldController = fieldController;
        this.viewOffset = 2.0f * fieldController.hexSize;
        this.lightUpAlgorithm = new LightUpAlgorithm(this);
        this.visibleArea = new RectangleYio();
        this.topBlock = new RectangleYio();
        this.bottomBlock = new RectangleYio();
        this.leftBlock = new RectangleYio();
        this.rightBlock = new RectangleYio();
        this.viewSlices = new ArrayList();
        this.sliceUpdater = new SliceUpdater(this);
        initPools();
    }

    private void initPools() {
        this.poolFogPoints = new C01041();
        this.poolKeys = new C01052();
        this.poolSlices = new C01063();
    }

    public void updateFog() {
        if (GameRules.fogOfWarEnabled) {
            resetStatuses();
            int playersNumber = this.fieldController.gameController.playersNumber;
            if (playersNumber == 0) {
                lightUpAllMap();
            } else if (playersNumber == 1) {
                lightUpInSingleplayerMode();
            } else {
                lightUpInMultiplayerMode();
            }
            updateVisibleArea();
            updateBlocks();
            updateSlices();
        }
    }

    private void lightUpInMultiplayerMode() {
        Iterator it = this.fieldController.provinces.iterator();
        while (it.hasNext()) {
            Province province = (Province) it.next();
            if (isCurrentPlayerProvince(province)) {
                lightUpProvince(province);
            } else if (isFriendOfCurrentPlayer(province)) {
                lightUpProvince(province);
            }
        }
    }

    private void lightUpInSingleplayerMode() {
        this.foundPlayersProvince = false;
        Iterator it = this.fieldController.provinces.iterator();
        while (it.hasNext()) {
            Province province = (Province) it.next();
            if (isFirstPlayerProvince(province)) {
                this.foundPlayersProvince = true;
                lightUpProvince(province);
            } else if (isFriendOfFirstPlayer(province)) {
                lightUpProvince(province);
            }
        }
        if (!this.foundPlayersProvince) {
            lightUpAllMap();
        }
    }

    boolean isFriendOfCurrentPlayer(Province province) {
        boolean z = true;
        if (!GameRules.diplomacyEnabled) {
            return false;
        }
        DiplomaticEntity entity = this.fieldController.diplomacyManager.getEntity(province.getColor());
        if (entity == null) {
            return false;
        }
        DiplomaticEntity currentEntity = this.fieldController.diplomacyManager.getEntity(this.fieldController.gameController.turn);
        if (currentEntity == entity) {
            return false;
        }
        if (entity.getRelation(currentEntity) != 1) {
            z = false;
        }
        return z;
    }

    boolean isFriendOfFirstPlayer(Province province) {
        boolean z = true;
        if (!GameRules.diplomacyEnabled) {
            return false;
        }
        DiplomaticEntity entity = this.fieldController.diplomacyManager.getEntity(province.getColor());
        if (entity == null) {
            return false;
        }
        DiplomaticEntity firstEntity = this.fieldController.diplomacyManager.getEntity(0);
        if (firstEntity == entity) {
            return false;
        }
        if (entity.getRelation(firstEntity) != 1) {
            z = false;
        }
        return z;
    }

    private boolean isCurrentPlayerProvince(Province province) {
        return province.getColor() == this.fieldController.gameController.turn;
    }

    private boolean isFirstPlayerProvince(Province province) {
        return province.getColor() == 0;
    }

    private void lightUpProvince(Province province) {
        Iterator it = province.hexList.iterator();
        while (it.hasNext()) {
            applyHex((Hex) it.next());
        }
    }

    public void onEndCreation() {
        init();
        updateFog();
    }

    private void updateSlices() {
        this.sliceUpdater.perform();
    }

    void clearSlices() {
        Iterator it = this.viewSlices.iterator();
        while (it.hasNext()) {
            this.poolSlices.add((FogSlice) it.next());
        }
        this.viewSlices.clear();
    }

    private void updateBlocks() {
        this.leftBlock.setBy(this.visibleArea);
        this.leftBlock.f146x = 0.0d;
        this.leftBlock.width = this.visibleArea.f146x;
        this.rightBlock.setBy(this.visibleArea);
        this.rightBlock.f146x = this.visibleArea.f146x + this.visibleArea.width;
        this.rightBlock.width = ((double) this.fieldController.gameController.boundWidth) - this.rightBlock.f146x;
        this.bottomBlock.f146x = 0.0d;
        this.bottomBlock.f147y = 0.0d;
        this.bottomBlock.width = (double) this.fieldController.gameController.boundWidth;
        this.bottomBlock.height = this.visibleArea.f147y;
        this.topBlock.f146x = 0.0d;
        this.topBlock.f147y = this.visibleArea.f147y + this.visibleArea.height;
        this.topBlock.width = (double) this.fieldController.gameController.boundWidth;
        this.topBlock.height = ((double) this.fieldController.gameController.boundHeight) - this.topBlock.f147y;
    }

    private void updateVisibleArea() {
        FogPoint anyDeactivatedFogPoint = getAnyDeactivatedFogPoint();
        if (anyDeactivatedFogPoint != null) {
            this.visibleArea.set((double) anyDeactivatedFogPoint.position.f144x, (double) anyDeactivatedFogPoint.position.f145y, 0.0d, 0.0d);
            for (FogPoint fogPoint : this.fogMap.values()) {
                if (!(fogPoint.status || fogPoint.hex == null)) {
                    double delta;
                    RectangleYio rectangleYio;
                    PointYio pos = fogPoint.position;
                    if (((double) pos.f144x) < this.visibleArea.f146x) {
                        delta = this.visibleArea.f146x - ((double) pos.f144x);
                        rectangleYio = this.visibleArea;
                        rectangleYio.f146x -= delta;
                        rectangleYio = this.visibleArea;
                        rectangleYio.width += delta;
                    }
                    if (((double) pos.f144x) > this.visibleArea.f146x + this.visibleArea.width) {
                        delta = ((double) pos.f144x) - (this.visibleArea.f146x + this.visibleArea.width);
                        rectangleYio = this.visibleArea;
                        rectangleYio.width += delta;
                    }
                    if (((double) pos.f145y) < this.visibleArea.f147y) {
                        delta = this.visibleArea.f147y - ((double) pos.f145y);
                        rectangleYio = this.visibleArea;
                        rectangleYio.f147y -= delta;
                        rectangleYio = this.visibleArea;
                        rectangleYio.height += delta;
                    }
                    if (((double) pos.f145y) > this.visibleArea.f147y + this.visibleArea.height) {
                        delta = ((double) pos.f145y) - (this.visibleArea.f147y + this.visibleArea.height);
                        rectangleYio = this.visibleArea;
                        rectangleYio.height += delta;
                    }
                }
            }
            RectangleYio rectangleYio2 = this.visibleArea;
            rectangleYio2.f146x -= (double) this.viewOffset;
            rectangleYio2 = this.visibleArea;
            rectangleYio2.f147y -= (double) this.viewOffset;
            rectangleYio2 = this.visibleArea;
            rectangleYio2.width += (double) (this.viewOffset * 2.0f);
            rectangleYio2 = this.visibleArea;
            rectangleYio2.height += (double) (this.viewOffset * 2.0f);
        }
    }

    private FogPoint getAnyDeactivatedFogPoint() {
        for (FogPoint fogPoint : this.fogMap.values()) {
            if (!fogPoint.status) {
                return fogPoint;
            }
        }
        return null;
    }

    private void lightUpAllMap() {
        for (FogPoint fogPoint : this.fogMap.values()) {
            fogPoint.setStatus(false);
        }
    }

    private void applyHex(Hex hex) {
        if (hex.containsUnit()) {
            lightUp(hex, 2);
        } else if (hex.objectInside == 4) {
            lightUp(hex, 3);
        } else if (hex.objectInside == 7) {
            lightUp(hex, 5);
        } else if (hex.objectInside == 3) {
            lightUp(hex, 4);
        } else {
            lightUp(hex, 1);
        }
    }

    private void lightUp(Hex hex, int radius) {
        this.lightUpAlgorithm.perform(hex, radius);
    }

    private void resetStatuses() {
        for (FogPoint fogPoint : this.fogMap.values()) {
            fogPoint.setStatus(true);
        }
    }

    private void clearPoints() {
        for (Entry<Hex, FogPoint> entry : this.fogMap.entrySet()) {
            this.poolFogPoints.add((ReusableYio) entry.getValue());
            this.poolKeys.add((ReusableYio) entry.getKey());
        }
        this.fogMap.clear();
    }

    public void init() {
        if (GameRules.fogOfWarEnabled) {
            clearPoints();
            for (int i = -4; i < this.fieldController.fWidth + 4; i++) {
                for (int j = -4; j < this.fieldController.fHeight + 4; j++) {
                    FogPoint next = (FogPoint) this.poolFogPoints.getNext();
                    next.setHexByIndexes(i, j);
                    if (isFogPointValid(next)) {
                        this.fogMap.put(getKey(next), next);
                    }
                }
            }
        }
    }

    private Hex getKey(FogPoint fogPoint) {
        if (fogPoint.hex == null) {
            return (Hex) this.poolKeys.getNext();
        }
        return fogPoint.hex;
    }

    private boolean isFogPointValid(FogPoint next) {
        return this.fieldController.isPointInsideLevelBoundsWithOffset(next.position, 2.0f * this.fieldController.hexSize);
    }

    public void showFogMapInConsole() {
        System.out.println();
        System.out.println("FogOfWarManager.showFogMapInConsole");
        for (Entry<Hex, FogPoint> entry : this.fogMap.entrySet()) {
            FogPoint value = (FogPoint) entry.getValue();
            System.out.println(((Hex) entry.getKey()) + " -> " + value);
        }
    }

    private int getNumberOfDeactivatedFogPoints() {
        int c = 0;
        for (FogPoint fogPoint : this.fogMap.values()) {
            if (!fogPoint.status) {
                c++;
            }
        }
        return c;
    }
}
