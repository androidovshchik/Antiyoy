package io.androidovshchik.antiyoy.menu.scrollable_list;

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
import io.androidovshchik.antiyoy.stuff.PointYio;
import io.androidovshchik.antiyoy.stuff.RectangleYio;
import io.androidovshchik.antiyoy.stuff.scroll_engine.ScrollEngineYio;

public class ScrollableListYio extends InterfaceElement {
    protected boolean alphaTriggered;
    public FactorYio appearFactor = new FactorYio();
    public RectangleYio bottomEdge;
    ClickDetector clickDetector = new ClickDetector();
    ListItemYio clickedItem;
    PointYio currentTouch = new PointYio();
    public BitmapFont descFont = Fonts.smallerMenuFont;
    float hook;
    private float itemHeight;
    public ArrayList<ListItemYio> items;
    public String label = "default label";
    public PointYio labelPosition = new PointYio();
    protected float labelWidth;
    PointYio lastTouch = new PointYio();
    ListBehaviorYio listBehaviorYio;
    protected MenuControllerYio menuControllerYio;
    public RectangleYio position = new RectangleYio();
    ScrollEngineYio scrollEngineYio;
    public FactorYio textAlphaFactor;
    public BitmapFont titleFont = Fonts.gameFont;
    public RectangleYio topEdge;
    private float topLabelOffset;
    protected boolean touched = false;
    public RectangleYio viewPosition = new RectangleYio();

    public ScrollableListYio(MenuControllerYio menuControllerYio) {
        super(-1);
        this.menuControllerYio = menuControllerYio;
        updateLabelWidth();
        this.items = new ArrayList();
        this.clickedItem = null;
        this.textAlphaFactor = new FactorYio();
        this.alphaTriggered = false;
        this.topEdge = new RectangleYio();
        this.bottomEdge = new RectangleYio();
        this.listBehaviorYio = null;
        initMetrics();
        initScrollEngine();
    }

    private void updateLabelWidth() {
        this.labelWidth = GraphicsYio.getTextWidth(this.titleFont, this.label);
    }

    private void initScrollEngine() {
        this.scrollEngineYio = new ScrollEngineYio();
        this.scrollEngineYio.setSlider(0.0d, 0.0d);
        updateScrollEngineLimits();
        this.scrollEngineYio.setFriction(0.02d);
        this.scrollEngineYio.setSoftLimitOffset((double) (0.05f * GraphicsYio.width));
    }

    public void updateScrollEngineLimits() {
        this.scrollEngineYio.setLimits(0.0d, getScrollLimit());
    }

    private double getScrollLimit() {
        return (double) ((((float) this.items.size()) * this.itemHeight) - (this.itemHeight / 2.0f));
    }

    private void initMetrics() {
        this.itemHeight = getItemHeight();
        this.topLabelOffset = 0.18f * GraphicsYio.height;
    }

    protected float getItemHeight() {
        return 0.115f * GraphicsYio.height;
    }

    public void setTitle(String label) {
        this.label = label;
        updateLabelWidth();
    }

    public void addDebugItems() {
        this.items.clear();
        for (int i = 0; i < 10; i++) {
            addItem("key" + i, "item " + i, "description");
        }
    }

    public void clearItems() {
        this.items.clear();
        updateMetrics();
        updateScrollEngineLimits();
    }

    public ListItemYio addItem(String key, String title, String description) {
        ListItemYio newItem = new ListItemYio(this);
        newItem.setKey(key);
        newItem.setTitle(title);
        newItem.setDescription(description);
        newItem.setBckViewType(this.items.size() % 3);
        this.items.add(newItem);
        updateMetrics();
        updateScrollEngineLimits();
        return newItem;
    }

    public void updateMetrics() {
        float currentY = ((float) this.position.height) - this.topLabelOffset;
        Iterator it = this.items.iterator();
        while (it.hasNext()) {
            ListItemYio item = (ListItemYio) it.next();
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

    protected void updateEdgeRectangles() {
        if (this.items.size() != 0) {
            ListItemYio firstItem = (ListItemYio) this.items.get(0);
            this.topEdge.setBy(firstItem.position);
            RectangleYio rectangleYio = this.topEdge;
            rectangleYio.f147y += firstItem.position.height;
            ListItemYio lastItem = (ListItemYio) this.items.get(this.items.size() - 1);
            this.bottomEdge.setBy(lastItem.position);
            rectangleYio = this.bottomEdge;
            rectangleYio.f147y -= lastItem.position.height;
        }
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

    protected void updateLabelPosition() {
        this.labelPosition.f144x = (float) ((this.viewPosition.f146x + (this.viewPosition.width / 2.0d)) - ((double) (this.labelWidth / 2.0f)));
        this.labelPosition.f145y = ((float) ((this.viewPosition.f147y + this.viewPosition.height) - ((double) (0.02f * GraphicsYio.width)))) + this.hook;
    }

    private void updateHook() {
        this.hook = (float) this.scrollEngineYio.getSlider().f107a;
        this.hook -= ((1.0f - this.appearFactor.get()) * 0.2f) * GraphicsYio.width;
    }

    protected void moveItems() {
        Iterator it = this.items.iterator();
        while (it.hasNext()) {
            ListItemYio item = (ListItemYio) it.next();
            item.move();
            if (!this.touched) {
                item.moveSelection();
            }
        }
    }

    protected void updateViewPosition() {
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

    protected void onAppear() {
        this.alphaTriggered = false;
        this.scrollEngineYio.resetToBottom();
    }

    public boolean isVisible() {
        return this.appearFactor.get() > 0.0f;
    }

    public boolean checkToPerformAction() {
        if (this.clickedItem == null) {
            return false;
        }
        applyItem();
        this.clickedItem = null;
        return true;
    }

    private void applyItem() {
        if (this.listBehaviorYio != null) {
            this.listBehaviorYio.applyItem(this.clickedItem);
        }
    }

    public void setListBehavior(ListBehaviorYio listBehaviorYio) {
        this.listBehaviorYio = listBehaviorYio;
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
        onTouchDown();
        return this.touched;
    }

    protected void onTouchDown() {
    }

    private void checkToSelectItems() {
        Iterator it = this.items.iterator();
        while (it.hasNext()) {
            ListItemYio item = (ListItemYio) it.next();
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
            ListItemYio item = (ListItemYio) it.next();
            if (item.isTouched(this.currentTouch)) {
                onItemClicked(item);
            }
        }
    }

    private void onItemClicked(ListItemYio item) {
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
        return MenuRender.renderScrollableList;
    }
}
