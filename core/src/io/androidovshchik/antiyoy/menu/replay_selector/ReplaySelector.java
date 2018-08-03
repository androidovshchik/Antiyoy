package io.androidovshchik.antiyoy.menu.replay_selector;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import java.util.ArrayList;
import java.util.Iterator;
import io.androidovshchik.antiyoy.factor_yio.FactorYio;
import io.androidovshchik.antiyoy.gameplay.ClickDetector;
import io.androidovshchik.antiyoy.gameplay.loading.LoadingManager;
import io.androidovshchik.antiyoy.gameplay.loading.LoadingParameters;
import io.androidovshchik.antiyoy.gameplay.replays.RepSlot;
import io.androidovshchik.antiyoy.gameplay.replays.Replay;
import io.androidovshchik.antiyoy.gameplay.replays.ReplaySaveSystem;
import io.androidovshchik.antiyoy.menu.InterfaceElement;
import io.androidovshchik.antiyoy.menu.MenuControllerYio;
import io.androidovshchik.antiyoy.menu.render.MenuRender;
import io.androidovshchik.antiyoy.menu.scenes.SceneSkirmishMenu;
import io.androidovshchik.antiyoy.stuff.Fonts;
import io.androidovshchik.antiyoy.stuff.GraphicsYio;
import io.androidovshchik.antiyoy.stuff.LanguagesManager;
import io.androidovshchik.antiyoy.stuff.LongTapDetector;
import io.androidovshchik.antiyoy.stuff.PointYio;
import io.androidovshchik.antiyoy.stuff.RectangleYio;
import io.androidovshchik.antiyoy.stuff.scroll_engine.ScrollEngineYio;

public class ReplaySelector extends InterfaceElement {
    boolean alphaTriggered = false;
    public FactorYio appearFactor = new FactorYio();
    ClickDetector clickDetector = new ClickDetector();
    RsItem clickedItem = null;
    PointYio currentTouch = new PointYio();
    public BitmapFont descFont = Fonts.microFont;
    float hook;
    boolean inRemoveMode;
    private float itemHeight;
    public ArrayList<RsItem> items = new ArrayList();
    public String label = LanguagesManager.getInstance().getString("replays");
    public PointYio labelPosition = new PointYio();
    float labelWidth = GraphicsYio.getTextWidth(this.titleFont, this.label);
    PointYio lastTouch = new PointYio();
    LongTapDetector longTapDetector;
    MenuControllerYio menuControllerYio;
    public RectangleYio position = new RectangleYio();
    RsItem readyToRemoveItem = null;
    ScrollEngineYio scrollEngineYio;
    public FactorYio textAlphaFactor = new FactorYio();
    public BitmapFont titleFont = Fonts.gameFont;
    private float topLabelOffset;
    boolean touched = false;
    public RectangleYio viewPosition = new RectangleYio();

    class C01161 extends LongTapDetector {
        C01161() {
        }

        public void onLongTapDetected() {
            ReplaySelector.this.onLongTapDetected();
        }
    }

    public ReplaySelector(MenuControllerYio menuControllerYio, int id) {
        super(id);
        this.menuControllerYio = menuControllerYio;
        initMetrics();
        initScrollEngine();
        initLongTapDetector();
    }

    private void initLongTapDetector() {
        this.longTapDetector = new C01161();
    }

    private void initScrollEngine() {
        this.scrollEngineYio = new ScrollEngineYio();
        this.scrollEngineYio.setSlider(0.0d, 0.0d);
        updateScrollEngineLimits();
        this.scrollEngineYio.setFriction(0.02d);
        this.scrollEngineYio.setSoftLimitOffset((double) (0.05f * GraphicsYio.width));
    }

    private void updateScrollEngineLimits() {
        this.scrollEngineYio.setLimits(0.0d, getLowerLimit());
    }

    private double getLowerLimit() {
        return (double) ((((float) this.items.size()) * this.itemHeight) - (this.itemHeight / 2.0f));
    }

    private void initMetrics() {
        this.itemHeight = 0.1f * GraphicsYio.height;
        this.topLabelOffset = 0.18f * GraphicsYio.height;
    }

    private void updateItems() {
        this.items.clear();
        ReplaySaveSystem instance = ReplaySaveSystem.getInstance();
        Iterator it = instance.getKeys().iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            RepSlot slotByKey = instance.getSlotByKey(key);
            RsItem rsItem = new RsItem(this);
            rsItem.setTitle(makeItemTitle(slotByKey));
            rsItem.setDescription(makeItemDescription(slotByKey));
            rsItem.setKey(key);
            this.items.add(rsItem);
        }
    }

    private String makeItemDescription(RepSlot repSlot) {
        return repSlot.date + ", " + SceneSkirmishMenu.getHumansString(repSlot.numberOfHumans);
    }

    private String makeItemTitle(RepSlot repSlot) {
        LanguagesManager instance = LanguagesManager.getInstance();
        if (!repSlot.campaignMode) {
            return instance.getString("choose_game_mode_skirmish");
        }
        String typeString = instance.getString("choose_game_mode_campaign");
        if (repSlot.levelIndex == -1) {
            return typeString;
        }
        return typeString + ", " + repSlot.levelIndex;
    }

    void updateItemMetrics() {
        float currentY = ((float) this.position.height) - this.topLabelOffset;
        Iterator it = this.items.iterator();
        while (it.hasNext()) {
            RsItem item = (RsItem) it.next();
            item.position.width = this.position.width;
            item.position.height = (double) this.itemHeight;
            item.delta.f144x = 0.0f;
            item.delta.f145y = currentY;
            currentY -= this.itemHeight;
        }
    }

    public void move() {
        moveFactors();
        updateViewPosition();
        moveItems();
        this.scrollEngineYio.move();
        updateHook();
        updateLabelPosition();
        this.longTapDetector.move();
    }

    private void onLongTapDetected() {
        switchRemoveMode();
    }

    private void switchRemoveMode() {
        this.inRemoveMode = !this.inRemoveMode;
        if (this.inRemoveMode) {
            onRemoveModeEnabled();
        } else {
            onRemoveModeDisabled();
        }
    }

    private void onRemoveModeDisabled() {
    }

    private void onRemoveModeEnabled() {
    }

    private void moveFactors() {
        this.textAlphaFactor.move();
        if (this.appearFactor.hasToMove()) {
            this.appearFactor.move();
            if (!this.alphaTriggered && ((double) this.appearFactor.get()) > 0.9d) {
                this.textAlphaFactor.appear(3, 0.7d);
                this.alphaTriggered = true;
            }
        }
    }

    private void updateLabelPosition() {
        this.labelPosition.f144x = (float) ((this.viewPosition.f146x + (this.viewPosition.width / 2.0d)) - ((double) (this.labelWidth / 2.0f)));
        this.labelPosition.f145y = ((float) ((this.viewPosition.f147y + this.viewPosition.height) - ((double) (0.02f * GraphicsYio.width)))) + this.hook;
    }

    private void updateHook() {
        this.hook = (float) this.scrollEngineYio.getSlider().f107a;
        this.hook -= ((1.0f - this.appearFactor.get()) * 0.2f) * GraphicsYio.width;
    }

    private void moveItems() {
        Iterator it = this.items.iterator();
        while (it.hasNext()) {
            RsItem item = (RsItem) it.next();
            item.move();
            if (!this.touched) {
                item.moveSelection();
            }
        }
    }

    private void updateViewPosition() {
        this.viewPosition.setBy(this.position);
        if (this.appearFactor.get() < 1.0f) {
            RectangleYio rectangleYio = this.viewPosition;
            rectangleYio.f146x += (double) ((float) ((((double) (1.0f - this.appearFactor.get())) * 0.5d) * this.position.width));
            rectangleYio = this.viewPosition;
            rectangleYio.f147y += (double) ((float) ((((double) (1.0f - this.appearFactor.get())) * 0.5d) * this.position.height));
            rectangleYio = this.viewPosition;
            rectangleYio.width -= (double) (((float) ((((double) (1.0f - this.appearFactor.get())) * 0.5d) * this.position.width)) * 2.0f);
            rectangleYio = this.viewPosition;
            rectangleYio.height -= (double) (((float) ((((double) (1.0f - this.appearFactor.get())) * 0.5d) * this.position.height)) * 2.0f);
        }
    }

    public FactorYio getFactor() {
        return this.appearFactor;
    }

    public void destroy() {
        this.appearFactor.destroy(2, 2.0d);
        this.textAlphaFactor.destroy(3, 4.0d);
        onDestroy();
    }

    private void onDestroy() {
        this.inRemoveMode = false;
    }

    public void appear() {
        this.appearFactor.setValues(0.01d, 0.0d);
        this.appearFactor.appear(2, 2.0d);
        onAppear();
    }

    private void onAppear() {
        updateItems();
        updateItemMetrics();
        updateScrollEngineLimits();
        this.alphaTriggered = false;
        this.scrollEngineYio.resetToBottom();
        this.inRemoveMode = false;
    }

    public boolean isVisible() {
        return this.appearFactor.get() > 0.0f;
    }

    public boolean checkToPerformAction() {
        if (this.clickedItem != null) {
            startSelectedReplay();
            this.clickedItem = null;
            return true;
        } else if (this.readyToRemoveItem == null) {
            return false;
        } else {
            removeClickedReplay();
            this.readyToRemoveItem = null;
            return true;
        }
    }

    private void removeClickedReplay() {
        ReplaySaveSystem.getInstance().removeReplay(this.readyToRemoveItem.key);
        updateItems();
        updateItemMetrics();
        updateScrollEngineLimits();
        this.scrollEngineYio.resetToBottom();
    }

    private void startSelectedReplay() {
        RepSlot slotByKey = ReplaySaveSystem.getInstance().getSlotByKey(this.clickedItem.key);
        Replay replay = slotByKey.replay;
        replay.loadFromPreferences(slotByKey.key);
        LoadingParameters loadingParameters = new LoadingParameters();
        loadingParameters.mode = 8;
        loadingParameters.applyFullLevel(replay.initialLevelString);
        loadingParameters.replay = replay;
        loadingParameters.playersNumber = 0;
        loadingParameters.colorOffset = replay.tempColorOffset;
        loadingParameters.slayRules = replay.tempSlayRules;
        LoadingManager.getInstance().startGame(loadingParameters);
    }

    public boolean isTouchable() {
        return true;
    }

    private void updateCurrentTouch(int screenX, int screenY) {
        this.lastTouch.setBy(this.currentTouch);
        this.currentTouch.set((double) screenX, (double) screenY);
    }

    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (!isVisible()) {
            return false;
        }
        updateCurrentTouch(screenX, screenY);
        this.touched = ((double) screenY) < this.position.f147y + this.position.height;
        if (this.touched) {
            this.clickDetector.touchDown(this.currentTouch);
            this.scrollEngineYio.updateCanSoftCorrect(false);
            this.longTapDetector.onTouchDown(this.currentTouch);
            checkToSelectItems();
        }
        return this.touched;
    }

    private void checkToSelectItems() {
        Iterator it = this.items.iterator();
        while (it.hasNext()) {
            RsItem item = (RsItem) it.next();
            if (item.isTouched(this.currentTouch)) {
                item.select();
            }
        }
    }

    public boolean touchDrag(int screenX, int screenY, int pointer) {
        if (this.touched) {
            updateCurrentTouch(screenX, screenY);
            this.scrollEngineYio.setSpeed((double) (this.currentTouch.f145y - this.lastTouch.f145y));
            this.clickDetector.touchDrag(this.currentTouch);
            this.longTapDetector.onTouchDrag(this.currentTouch);
        }
        return this.touched;
    }

    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        updateCurrentTouch(screenX, screenY);
        this.scrollEngineYio.updateCanSoftCorrect(true);
        if (!this.touched) {
            return false;
        }
        this.touched = false;
        this.clickDetector.touchUp(this.currentTouch);
        if (this.clickDetector.isClicked()) {
            onClick();
        }
        this.longTapDetector.onTouchUp(this.currentTouch);
        return true;
    }

    private void onClick() {
        this.scrollEngineYio.setSpeed(0.0d);
        if (this.inRemoveMode) {
            onClickInRemoveMode();
            return;
        }
        Iterator it = this.items.iterator();
        while (it.hasNext()) {
            RsItem item = (RsItem) it.next();
            if (item.isTouched(this.currentTouch)) {
                onItemClicked(item);
            }
        }
    }

    private void onClickInRemoveMode() {
        Iterator it = this.items.iterator();
        while (it.hasNext()) {
            RsItem item = (RsItem) it.next();
            if (this.currentTouch.distanceTo(item.removeIconPosition) < ((double) (0.07f * GraphicsYio.width))) {
                this.readyToRemoveItem = item;
                return;
            }
        }
    }

    private void onItemClicked(RsItem item) {
        this.clickedItem = item;
    }

    public boolean onMouseWheelScrolled(int amount) {
        if (amount == 1) {
            this.scrollEngineYio.giveImpulse(0.02d * ((double) GraphicsYio.width));
        } else if (amount == -1) {
            this.scrollEngineYio.giveImpulse(-0.02d * ((double) GraphicsYio.width));
        }
        return true;
    }

    public void setTouchable(boolean touchable) {
    }

    public boolean isButton() {
        return false;
    }

    public void setPosition(RectangleYio position) {
        this.position.setBy(position);
        onPositionChanged();
    }

    private void onPositionChanged() {
        updateItemMetrics();
        this.scrollEngineYio.setSlider(0.0d, this.position.height - ((double) this.topLabelOffset));
        updateScrollEngineLimits();
    }

    public boolean isInRemoveMode() {
        return this.inRemoveMode;
    }

    public MenuRender getRenderSystem() {
        return MenuRender.renderReplaySelector;
    }
}
