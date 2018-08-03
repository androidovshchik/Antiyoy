package io.androidovshchik.antiyoy.menu.speed_panel;

import java.util.ArrayList;
import java.util.Iterator;
import yio.tro.antiyoy.factor_yio.FactorYio;
import yio.tro.antiyoy.gameplay.SpeedManager;
import yio.tro.antiyoy.gameplay.rules.GameRules;
import yio.tro.antiyoy.menu.InterfaceElement;
import yio.tro.antiyoy.menu.MenuControllerYio;
import yio.tro.antiyoy.menu.render.MenuRender;
import yio.tro.antiyoy.menu.scenes.Scenes;
import yio.tro.antiyoy.stuff.GraphicsYio;
import yio.tro.antiyoy.stuff.PointYio;
import yio.tro.antiyoy.stuff.RectangleYio;

public class SpeedPanel extends InterfaceElement {
    FactorYio appearFactor = new FactorYio();
    public ArrayList<SpItem> centerItems = new ArrayList();
    PointYio currentTouch = new PointYio();
    private float height;
    private float itemTouchOffset;
    public ArrayList<SpItem> items;
    MenuControllerYio menuControllerYio;
    public RectangleYio position = new RectangleYio();
    public RectangleYio viewPosition = new RectangleYio();

    public SpeedPanel(MenuControllerYio menuControllerYio, int id) {
        super(id);
        this.menuControllerYio = menuControllerYio;
        initMetrics();
        initPosition();
        initItems();
    }

    private void initItems() {
        this.itemTouchOffset = 0.05f * GraphicsYio.width;
        this.items = new ArrayList();
        SpItem item = new SpItem(this);
        item.setAction(0);
        item.setDelta(0.0d, (double) (this.height / 2.0f));
        this.items.add(item);
        this.centerItems.add(item);
        item = new SpItem(this);
        item.setAction(1);
        item.setDelta(0.0d, (double) (this.height / 2.0f));
        this.items.add(item);
        this.centerItems.add(item);
        item = new SpItem(this);
        item.setAction(2);
        item.setDelta(0.0d, (double) (this.height / 2.0f));
        this.items.add(item);
        this.centerItems.add(item);
        item = new SpItem(this);
        item.setAction(3);
        item.setDelta(this.position.width - ((double) (this.height / 2.0f)), (double) (this.height / 2.0f));
        this.items.add(item);
        Iterator it = this.items.iterator();
        while (it.hasNext()) {
            SpItem spItem = (SpItem) it.next();
            spItem.setRadius(this.height / 2.0f);
            spItem.setTouchOffset(this.itemTouchOffset);
        }
    }

    private void initPosition() {
        this.position.f146x = 0.0d;
        this.position.f147y = 0.0d;
        this.position.width = (double) GraphicsYio.width;
        this.position.height = (double) this.height;
    }

    private void initMetrics() {
        this.height = 0.05f * GraphicsYio.height;
    }

    public void move() {
        this.appearFactor.move();
        updateViewPosition();
        Iterator it = this.items.iterator();
        while (it.hasNext()) {
            ((SpItem) it.next()).move();
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
        this.appearFactor.setValues(0.01d, 0.0d);
        this.appearFactor.appear(3, 0.8d);
        onAppear();
    }

    private void onAppear() {
        hideSomeItems();
        applyCenterItems();
    }

    private void applyCenterItems() {
        int n = 0;
        Iterator it = this.centerItems.iterator();
        while (it.hasNext()) {
            if (((SpItem) it.next()).isVisible()) {
                n++;
            }
        }
        float stepDelta = 4.0f * this.itemTouchOffset;
        float cx = (float) ((this.position.width / 2.0d) - ((double) ((stepDelta * ((float) (n - 1))) / 2.0f)));
        it = this.centerItems.iterator();
        while (it.hasNext()) {
            SpItem centerItem = (SpItem) it.next();
            if (centerItem.isVisible()) {
                centerItem.delta.f144x = cx;
                cx += stepDelta;
            }
        }
    }

    private void hideSomeItems() {
        Iterator it = this.items.iterator();
        while (it.hasNext()) {
            SpItem item = (SpItem) it.next();
            item.appearFactor.setValues(1.0d, 0.0d);
            item.appearFactor.appear(1, 1.0d);
            if (item.action == 3) {
                item.appearFactor.setValues(0.0d, 0.0d);
                item.appearFactor.destroy(1, 1.0d);
            }
            if (item.action == 0 && !GameRules.replayMode) {
                item.appearFactor.setValues(0.0d, 0.0d);
                item.appearFactor.destroy(1, 1.0d);
            }
        }
    }

    private void hideSaveButton() {
        Iterator it = this.items.iterator();
        while (it.hasNext()) {
            SpItem item = (SpItem) it.next();
            if (item.action == 3) {
                item.destroy();
                return;
            }
        }
    }

    public void showSaveIcon() {
        Iterator it = this.items.iterator();
        while (it.hasNext()) {
            SpItem item = (SpItem) it.next();
            if (item.action == 3) {
                item.defaultAppearFactorState();
                return;
            }
        }
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
            SpItem item = (SpItem) it.next();
            if (item.appearFactor.get() >= 1.0f && item.isTouched(this.currentTouch)) {
                item.select();
                onItemClicked(item);
                return true;
            }
        }
        return false;
    }

    private void onItemClicked(SpItem item) {
        SpeedManager speedManager = this.menuControllerYio.yioGdxGame.gameController.speedManager;
        switch (item.action) {
            case 0:
                speedManager.stop();
                return;
            case 1:
                onPlayPauseButtonPressed(speedManager);
                return;
            case 2:
                speedManager.onFastForwardButtonPressed();
                return;
            case 3:
                saveCurrentReplay();
                return;
            default:
                return;
        }
    }

    public void onPlayPauseButtonPressed(SpeedManager speedManager) {
        speedManager.onPlayPauseButtonPressed();
    }

    private void saveCurrentReplay() {
        hideSaveButton();
        this.menuControllerYio.yioGdxGame.gameController.replayManager.saveCurrentReplay();
        Scenes.sceneNotification.showNotification("replay_saved");
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
        return MenuRender.renderSpeedPanel;
    }
}
