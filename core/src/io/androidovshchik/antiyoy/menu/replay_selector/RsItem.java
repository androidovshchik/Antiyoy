package io.androidovshchik.antiyoy.menu.replay_selector;

import io.androidovshchik.antiyoy.factor_yio.FactorYio;
import io.androidovshchik.antiyoy.stuff.GraphicsYio;
import io.androidovshchik.antiyoy.stuff.PointYio;
import io.androidovshchik.antiyoy.stuff.RectangleYio;

public class RsItem {
    PointYio delta = new PointYio();
    float descOffset = (0.045f * GraphicsYio.height);
    public PointYio descPosition = new PointYio();
    public String description = null;
    public String key = null;
    public RectangleYio position = new RectangleYio();
    public PointYio removeIconPosition = new PointYio();
    ReplaySelector replaySelector;
    FactorYio selectionFactor = new FactorYio();
    public String title = null;
    float titleOffset = (0.02f * GraphicsYio.width);
    public PointYio titlePosition = new PointYio();

    public RsItem(ReplaySelector replaySelector) {
        this.replaySelector = replaySelector;
    }

    boolean isTouched(PointYio touchPoint) {
        return this.replaySelector.isTouchInsideRectangle(touchPoint.f144x, touchPoint.f145y, this.position, 0.0f);
    }

    public boolean isVisible() {
        if (this.position.f147y <= this.replaySelector.viewPosition.f147y + this.replaySelector.viewPosition.height && this.position.f147y + this.position.height >= this.replaySelector.viewPosition.f147y) {
            return true;
        }
        return false;
    }

    void select() {
        this.selectionFactor.setValues(1.0d, 0.0d);
        this.selectionFactor.destroy(1, 2.0d);
    }

    public boolean isSelected() {
        return this.selectionFactor.get() > 0.0f;
    }

    public FactorYio getSelectionFactor() {
        return this.selectionFactor;
    }

    void move() {
        updatePosition();
        updateTitlePosition();
        updateDescPosition();
        updateRemoveIconPosition();
    }

    private void updateRemoveIconPosition() {
        this.removeIconPosition.f144x = (float) ((this.position.f146x + this.position.width) - ((double) (this.titleOffset * 2.0f)));
        this.removeIconPosition.f145y = (float) ((this.position.f147y + this.position.height) - ((double) (this.titleOffset * 2.0f)));
    }

    private void updateDescPosition() {
        this.descPosition.f144x = (float) (this.position.f146x + ((double) this.titleOffset));
        this.descPosition.f145y = this.titlePosition.f145y - this.descOffset;
    }

    private void updateTitlePosition() {
        this.titlePosition.f144x = (float) (this.position.f146x + ((double) this.titleOffset));
        this.titlePosition.f145y = (float) ((this.position.f147y + this.position.height) - ((double) this.titleOffset));
    }

    private void updatePosition() {
        this.position.f146x = this.replaySelector.position.f146x + ((double) this.delta.f144x);
        this.position.f147y = (this.replaySelector.position.f147y + ((double) this.delta.f145y)) + ((double) this.replaySelector.hook);
    }

    void moveSelection() {
        this.selectionFactor.move();
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String toString() {
        return "[RsItem: " + this.title + " - " + this.key + "]";
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
