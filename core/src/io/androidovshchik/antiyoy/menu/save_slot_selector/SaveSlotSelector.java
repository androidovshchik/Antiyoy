package io.androidovshchik.antiyoy.menu.save_slot_selector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import java.util.ArrayList;
import java.util.Iterator;
import io.androidovshchik.antiyoy.SoundControllerYio;
import io.androidovshchik.antiyoy.factor_yio.FactorYio;
import io.androidovshchik.antiyoy.gameplay.ClickDetector;
import io.androidovshchik.antiyoy.menu.InterfaceElement;
import io.androidovshchik.antiyoy.menu.MenuControllerYio;
import io.androidovshchik.antiyoy.menu.render.MenuRender;
import io.androidovshchik.antiyoy.stuff.Fonts;
import io.androidovshchik.antiyoy.stuff.GraphicsYio;
import io.androidovshchik.antiyoy.stuff.LanguagesManager;
import io.androidovshchik.antiyoy.stuff.PointYio;
import io.androidovshchik.antiyoy.stuff.RectangleYio;
import io.androidovshchik.antiyoy.stuff.scroll_engine.ScrollEngineYio;

public class SaveSlotSelector extends InterfaceElement {
    public static final int EMPTY_ITEM_CUT = 9;
    public static final int MIN_ITEMS_NUMBER = 9;
    boolean alphaTriggered = false;
    public FactorYio appearFactor = new FactorYio();
    public RectangleYio bottomEdge = new RectangleYio();
    ClickDetector clickDetector = new ClickDetector();
    SsItem clickedItem = null;
    PointYio currentTouch = new PointYio();
    public BitmapFont descFont = Fonts.smallerMenuFont;
    float hook;
    private float itemHeight;
    public ArrayList<SsItem> items = new ArrayList();
    public String label = LanguagesManager.getInstance().getString("slots");
    public PointYio labelPosition = new PointYio();
    float labelWidth = GraphicsYio.getTextWidth(this.titleFont, this.label);
    PointYio lastTouch = new PointYio();
    MenuControllerYio menuControllerYio;
    boolean operationType = false;
    public RectangleYio position = new RectangleYio();
    ScrollEngineYio scrollEngineYio;
    private String slotPrefsString = SaveSystem.SAVE_SLOT_PREFS;
    public FactorYio textAlphaFactor = new FactorYio();
    public BitmapFont titleFont = Fonts.gameFont;
    public RectangleYio topEdge = new RectangleYio();
    private float topLabelOffset;
    boolean touched = false;
    public RectangleYio viewPosition = new RectangleYio();

    public SaveSlotSelector(MenuControllerYio menuControllerYio, int id) {
        super(id);
        this.menuControllerYio = menuControllerYio;
        initMetrics();
        initScrollEngine();
    }

    private void initScrollEngine() {
        this.scrollEngineYio = new ScrollEngineYio();
        this.scrollEngineYio.setSlider(0.0d, 0.0d);
        updateScrollEngineLimits();
        this.scrollEngineYio.setFriction(0.02d);
        this.scrollEngineYio.setSoftLimitOffset((double) (0.05f * GraphicsYio.width));
    }

    private void updateScrollEngineLimits() {
        this.scrollEngineYio.setLimits(0.0d, getScrollLimit());
    }

    private double getScrollLimit() {
        return (double) ((((float) this.items.size()) * this.itemHeight) - (this.itemHeight / 2.0f));
    }

    private void initMetrics() {
        this.itemHeight = 0.115f * GraphicsYio.height;
        this.topLabelOffset = 0.18f * GraphicsYio.height;
    }

    private void updateItems() {
        this.items.clear();
        SaveSystem saveSystem = this.menuControllerYio.yioGdxGame.saveSystem;
        ArrayList<String> keys = saveSystem.getKeys(this.slotPrefsString);
        int index = 0;
        if (!this.operationType) {
            addEmptyItem(saveSystem.getKeyForNewSlot(this.slotPrefsString), 0);
            index = 0 + 1;
        }
        Iterator it = keys.iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            SaveSlotInfo slotInfo = saveSystem.getSlotInfo(key, this.slotPrefsString);
            if (slotInfo.name.equals("")) {
                addEmptyItem(key, index);
                index++;
            } else {
                addItem(index, key, slotInfo);
                index++;
            }
        }
    }

    private void addItem(int index, String key, SaveSlotInfo slotInfo) {
        SsItem ssItem = new SsItem(this);
        ssItem.setKey(key);
        ssItem.setTitle(slotInfo.name);
        ssItem.setDescription(slotInfo.description);
        ssItem.setBckViewType(index % 3);
        this.items.add(ssItem);
    }

    private void updateSingleItem(String key) {
        SaveSlotInfo slotInfo = this.menuControllerYio.yioGdxGame.saveSystem.getSlotInfo(key, this.slotPrefsString);
        Iterator it = this.items.iterator();
        while (it.hasNext()) {
            SsItem item = (SsItem) it.next();
            if (item.key.equals(key)) {
                item.setTitle(slotInfo.name);
                item.setDescription(slotInfo.description);
                return;
            }
        }
    }

    private void addEmptyItem(String key, int index) {
        if (this.items.size() <= 9) {
            SsItem ssItem = new SsItem(this);
            ssItem.setTitle(LanguagesManager.getInstance().getString("empty"));
            ssItem.setDescription("");
            ssItem.setKey(key);
            ssItem.setBckViewType(index % 3);
            this.items.add(ssItem);
        }
    }

    void updateMetrics() {
        float currentY = ((float) this.position.height) - this.topLabelOffset;
        Iterator it = this.items.iterator();
        while (it.hasNext()) {
            SsItem item = (SsItem) it.next();
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
        updateEdgeRectangles();
    }

    private void updateEdgeRectangles() {
        SsItem firstItem = (SsItem) this.items.get(0);
        this.topEdge.setBy(firstItem.position);
        RectangleYio rectangleYio = this.topEdge;
        rectangleYio.f147y += firstItem.position.height;
        SsItem lastItem = (SsItem) this.items.get(this.items.size() - 1);
        this.bottomEdge.setBy(lastItem.position);
        rectangleYio = this.bottomEdge;
        rectangleYio.f147y -= lastItem.position.height;
    }

    private void moveFactors() {
        this.textAlphaFactor.move();
        if (this.appearFactor.hasToMove()) {
            this.appearFactor.move();
            if (!this.alphaTriggered && ((double) this.appearFactor.get()) > 0.95d) {
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
            SsItem item = (SsItem) it.next();
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
    }

    public void appear() {
        this.appearFactor.setValues(0.01d, 0.0d);
        this.appearFactor.appear(2, 2.0d);
        onAppear();
    }

    private void onAppear() {
        this.alphaTriggered = false;
        this.scrollEngineYio.resetToBottom();
    }

    private void updateAll() {
        updateItems();
        updateMetrics();
        updateScrollEngineLimits();
    }

    public boolean isVisible() {
        return this.appearFactor.get() > 0.0f;
    }

    public boolean checkToPerformAction() {
        if (this.clickedItem == null) {
            return false;
        }
        onSlotSelected();
        this.clickedItem = null;
        return true;
    }

    private void onSlotSelected() {
        String key = this.clickedItem.key;
        SaveSystem saveSystem = this.menuControllerYio.yioGdxGame.saveSystem;
        if (this.operationType) {
            loadSlot(key, saveSystem);
        } else {
            saveSlot(key, saveSystem);
        }
    }

    private void saveSlot(String key, SaveSystem saveSystem) {
        if (!key.equals(SaveSystem.AUTOSAVE_KEY)) {
            if (!saveSystem.containsKey(key, this.slotPrefsString)) {
                saveSystem.addKey(key, this.slotPrefsString);
            }
            saveSystem.saveGame(key);
            SaveSlotInfo saveSlotInfo = new SaveSlotInfo();
            Preferences slotPrefs = Gdx.app.getPreferences(key);
            saveSlotInfo.name = SaveSystem.getNameString(slotPrefs);
            saveSlotInfo.description = SaveSystem.getDescriptionString(slotPrefs);
            saveSlotInfo.key = key;
            saveSystem.editSlot(key, saveSlotInfo, this.slotPrefsString);
            updateSingleItem(saveSlotInfo.key);
        }
    }

    private void loadSlot(String key, SaveSystem saveSystem) {
        SaveSlotInfo slotInfo = saveSystem.getSlotInfo(key, this.slotPrefsString);
        if (slotInfo.name.length() < 3) {
            System.out.println("clicked on empty slot");
        } else {
            saveSystem.loadGame(slotInfo.key);
        }
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
            checkToSelectItems();
        }
        return this.touched;
    }

    private void checkToSelectItems() {
        Iterator it = this.items.iterator();
        while (it.hasNext()) {
            SsItem item = (SsItem) it.next();
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
        if (!this.clickDetector.isClicked()) {
            return true;
        }
        onClick();
        return true;
    }

    private void onClick() {
        this.scrollEngineYio.setSpeed(0.0d);
        Iterator it = this.items.iterator();
        while (it.hasNext()) {
            SsItem item = (SsItem) it.next();
            if (item.isTouched(this.currentTouch)) {
                onItemClicked(item);
            }
        }
    }

    private void onItemClicked(SsItem item) {
        this.clickedItem = item;
        SoundControllerYio.playSound(SoundControllerYio.soundPressButton);
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

    public void setOperationType(boolean operationType) {
        this.operationType = operationType;
        updateAll();
    }

    public boolean isButton() {
        return false;
    }

    public void setPosition(RectangleYio position) {
        this.position.setBy(position);
        onPositionChanged();
    }

    private void onPositionChanged() {
        updateMetrics();
        this.scrollEngineYio.setSlider(0.0d, this.position.height - ((double) this.topLabelOffset));
        updateScrollEngineLimits();
    }

    public MenuRender getRenderSystem() {
        return MenuRender.renderSaveSlotSelector;
    }
}
