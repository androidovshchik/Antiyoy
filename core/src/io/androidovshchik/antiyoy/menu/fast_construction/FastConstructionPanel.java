package io.androidovshchik.antiyoy.menu.fast_construction;

import java.util.ArrayList;
import java.util.Iterator;
import yio.tro.antiyoy.Settings;
import yio.tro.antiyoy.factor_yio.FactorYio;
import yio.tro.antiyoy.gameplay.GameController;
import yio.tro.antiyoy.gameplay.rules.GameRules;
import yio.tro.antiyoy.menu.InterfaceElement;
import yio.tro.antiyoy.menu.MenuControllerYio;
import yio.tro.antiyoy.menu.render.MenuRender;
import yio.tro.antiyoy.menu.scenes.Scenes;
import yio.tro.antiyoy.stuff.GraphicsYio;
import yio.tro.antiyoy.stuff.PointYio;
import yio.tro.antiyoy.stuff.RectangleYio;

public class FastConstructionPanel extends InterfaceElement {
    FactorYio appearFactor = new FactorYio();
    PointYio currentTouch = new PointYio();
    private float height;
    private float itemTouchOffset;
    public ArrayList<FcpItem> items;
    MenuControllerYio menuControllerYio;
    public RectangleYio position = new RectangleYio();
    public RectangleYio viewPosition = new RectangleYio();

    public FastConstructionPanel(MenuControllerYio menuControllerYio, int id) {
        super(id);
        this.menuControllerYio = menuControllerYio;
        initMetrics();
        initPosition();
        initItems();
    }

    private void initItems() {
        this.itemTouchOffset = 0.05f * GraphicsYio.width;
        this.items = new ArrayList();
        addItem(1);
        addItem(2);
        addItem(3);
        addItem(4);
        addItem(5);
        addItem(6);
        addItem(7);
        addItem(8);
        addItem(9);
        addItem(10);
        Iterator it = this.items.iterator();
        while (it.hasNext()) {
            FcpItem spItem = (FcpItem) it.next();
            spItem.setRadius(0.37f * this.height);
            spItem.setTouchOffset(this.itemTouchOffset);
        }
    }

    private void updateTouchDeltas() {
        Iterator it = this.items.iterator();
        while (it.hasNext()) {
            ((FcpItem) it.next()).touchDelta.set(0.0d, 0.0d);
        }
        if (!GameRules.slayRules) {
            FcpItem farmItem = getItemByAction(5);
            farmItem.touchDelta.f144x = 0.5f * farmItem.radius;
            FcpItem towerItem = getItemByAction(6);
            towerItem.touchDelta.f144x = 0.3f * towerItem.radius;
        }
    }

    private void addItem(int action) {
        FcpItem fcpItem = new FcpItem(this);
        fcpItem.setAction(action);
        this.items.add(fcpItem);
    }

    private void initMetrics() {
        this.height = 0.08f * GraphicsYio.height;
    }

    private void initPosition() {
        this.position.f146x = 0.0d;
        this.position.f147y = 0.0d;
        this.position.width = (double) GraphicsYio.width;
        this.position.height = (double) this.height;
    }

    public void move() {
        this.appearFactor.move();
        updateViewPosition();
        moveItems();
    }

    private void moveItems() {
        Iterator it = this.items.iterator();
        while (it.hasNext()) {
            ((FcpItem) it.next()).move();
        }
    }

    private void updateViewPosition() {
        this.viewPosition.setBy(this.position);
        RectangleYio rectangleYio = this.viewPosition;
        rectangleYio.f147y -= ((double) (1.0f - this.appearFactor.get())) * this.position.height;
    }

    public FactorYio getFactor() {
        return this.appearFactor;
    }

    public void destroy() {
        this.appearFactor.destroy(2, 2.0d);
    }

    public void appear() {
        if (this.appearFactor.get() == 1.0f) {
            this.appearFactor.appear(3, 2.0d);
            return;
        }
        this.appearFactor.setValues(0.01d, 0.0d);
        this.appearFactor.appear(3, 2.0d);
        onAppear();
    }

    private void onAppear() {
        rearrangeItems();
        updateTouchDeltas();
    }

    private void rearrangeItems() {
        Iterator it = this.items.iterator();
        while (it.hasNext()) {
            ((FcpItem) it.next()).visible = false;
        }
        if (GameRules.slayRules) {
            rearrangeBySlayRules();
        } else {
            rearrangeByNormalRules();
        }
        rearrangeSpecialItems();
    }

    private void rearrangeSpecialItems() {
        FcpItem undoItem = getItemByAction(8);
        undoItem.visible = true;
        undoItem.delta.f144x = getSpecialItemHorPos();
        undoItem.delta.f145y = 2.2f * this.height;
        undoItem.animDelta.set((double) getSpecialItemAnimDelta(), (double) this.height);
        FcpItem endTurnItem = getItemByAction(9);
        endTurnItem.visible = true;
        endTurnItem.delta.f144x = getSpecialItemHorPos();
        endTurnItem.delta.f145y = 3.5f * this.height;
        endTurnItem.animDelta.set((double) getSpecialItemAnimDelta(), (double) this.height);
        FcpItem diplomacyItem = getItemByAction(10);
        diplomacyItem.visible = GameRules.diplomacyEnabled;
        diplomacyItem.delta.f144x = getSpecialItemHorPos();
        diplomacyItem.delta.f145y = 4.8f * this.height;
        diplomacyItem.animDelta.set((double) getSpecialItemAnimDelta(), (double) this.height);
    }

    private float getSpecialItemAnimDelta() {
        if (Settings.leftHandMode) {
            return this.itemTouchOffset;
        }
        return -this.itemTouchOffset;
    }

    private float getSpecialItemHorPos() {
        if (Settings.leftHandMode) {
            return GraphicsYio.width - this.itemTouchOffset;
        }
        return this.itemTouchOffset;
    }

    private void rearrangeByNormalRules() {
        placeItem(7, 0, false);
        placeItem(6, 1, false);
        placeItem(5, 2, false);
        placeItem(1, 3, true);
        placeItem(2, 2, true);
        placeItem(3, 1, true);
        placeItem(4, 0, true);
    }

    private void rearrangeBySlayRules() {
        placeItem(6, 0, false);
        placeItem(1, 3, true);
        placeItem(2, 2, true);
        placeItem(3, 1, true);
        placeItem(4, 0, true);
    }

    private void placeItem(int action, int place, boolean alignRight) {
        FcpItem itemByAction = getItemByAction(action);
        if (itemByAction != null) {
            itemByAction.visible = true;
            if (alignRight) {
                itemByAction.delta.f144x = (float) (this.position.width - ((1.4d * ((double) this.itemTouchOffset)) + ((double) (((float) place) * (this.itemTouchOffset * 2.5f)))));
            } else {
                itemByAction.delta.f144x = this.itemTouchOffset + (((float) place) * (this.itemTouchOffset * 2.5f));
            }
            itemByAction.delta.f145y = this.height / 2.0f;
        }
    }

    private FcpItem getItemByAction(int action) {
        Iterator it = this.items.iterator();
        while (it.hasNext()) {
            FcpItem item = (FcpItem) it.next();
            if (item.action == action) {
                return item;
            }
        }
        return null;
    }

    public boolean isVisible() {
        return this.appearFactor.get() > 0.0f;
    }

    public boolean checkToPerformAction() {
        return false;
    }

    public boolean isTouchable() {
        return true;
    }

    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        this.currentTouch.set((double) screenX, (double) screenY);
        return checkToClickItems();
    }

    private boolean checkToClickItems() {
        Iterator it = this.items.iterator();
        while (it.hasNext()) {
            FcpItem item = (FcpItem) it.next();
            if (item.isVisible() && item.isTouched(this.currentTouch)) {
                item.select();
                onItemClicked(item);
                return true;
            }
        }
        return false;
    }

    private void onItemClicked(FcpItem item) {
        if (this.menuControllerYio.yioGdxGame.gameController.fieldController.isSomethingSelected()) {
            switch (item.action) {
                case 1:
                    applyBuildUnit(1);
                    return;
                case 2:
                    applyBuildUnit(2);
                    return;
                case 3:
                    applyBuildUnit(3);
                    return;
                case 4:
                    applyBuildUnit(4);
                    return;
                case 5:
                    applyBuildSolidObject(6);
                    return;
                case 6:
                    applyBuildSolidObject(4);
                    return;
                case 7:
                    applyBuildSolidObject(7);
                    return;
                case 8:
                    applyUndoAction();
                    return;
                case 9:
                    applyEndTurn();
                    return;
                case 10:
                    applyOpenDiplomacy();
                    return;
                default:
                    return;
            }
        }
    }

    private void applyOpenDiplomacy() {
        this.menuControllerYio.yioGdxGame.gameController.fieldController.diplomacyManager.onDiplomacyButtonPressed();
    }

    private void applyEndTurn() {
        GameController gameController = this.menuControllerYio.yioGdxGame.gameController;
        if (gameController.haveToAskToEndTurn()) {
            Scenes.sceneConfirmEndTurn.create();
        } else {
            gameController.onEndTurnButtonPressed();
        }
    }

    private void applyUndoAction() {
        this.menuControllerYio.yioGdxGame.gameController.undoAction();
    }

    private void applyBuildUnit(int strength) {
        GameController gameController = this.menuControllerYio.yioGdxGame.gameController;
        int tipType = -1;
        switch (strength) {
            case 1:
                tipType = 1;
                break;
            case 2:
                tipType = 2;
                break;
            case 3:
                tipType = 3;
                break;
            case 4:
                tipType = 4;
                break;
        }
        gameController.selectionController.awakeTip(tipType);
        gameController.detectAndShowMoveZoneForBuildingUnit(tipType);
    }

    private void applyBuildSolidObject(int type) {
        GameController gameController = this.menuControllerYio.yioGdxGame.gameController;
        int tipType = -1;
        switch (type) {
            case 4:
                tipType = 0;
                break;
            case 6:
                tipType = 5;
                break;
            case 7:
                tipType = 6;
                break;
        }
        gameController.selectionController.awakeTip(tipType);
        if (tipType == 5) {
            gameController.detectAndShowMoveZoneForFarm();
        }
    }

    public void onKeyPressed(int keycode) {
        switch (keycode) {
            case 8:
                applyBuildUnit(1);
                return;
            case 9:
                applyBuildSolidObject(6);
                return;
            default:
                return;
        }
    }

    public boolean touchDrag(int screenX, int screenY, int pointer) {
        return false;
    }

    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    public void setTouchable(boolean touchable) {
    }

    public boolean isButton() {
        return false;
    }

    public void setPosition(RectangleYio position) {
        this.position.setBy(position);
    }

    public MenuRender getRenderSystem() {
        return MenuRender.renderFastConstructionPanel;
    }
}
