package io.androidovshchik.antiyoy.menu.scrollable_list;

import io.androidovshchik.antiyoy.factor_yio.FactorYio;
import io.androidovshchik.antiyoy.stuff.GraphicsYio;
import io.androidovshchik.antiyoy.stuff.PointYio;
import io.androidovshchik.antiyoy.stuff.RectangleYio;

public class ListItemYio {
    public int bckViewType = -1;
    PointYio delta = new PointYio();
    float descOffset = (0.045f * GraphicsYio.height);
    public PointYio descPosition = new PointYio();
    public float descWidth = 0.0f;
    public String description = null;
    public String key = null;
    public RectangleYio position = new RectangleYio();
    ScrollableListYio scrollableListYio;
    FactorYio selectionFactor = new FactorYio();
    public String title = null;
    float titleOffset = (0.02f * GraphicsYio.width);
    public PointYio titlePosition = new PointYio();
    public float titleWidth = 0.0f;

    public ListItemYio(ScrollableListYio scrollableListYio) {
        this.scrollableListYio = scrollableListYio;
    }

    boolean isTouched(PointYio touchPoint) {
        return this.scrollableListYio.isTouchInsideRectangle(touchPoint.f144x, touchPoint.f145y, this.position, 0.0f);
    }

    public boolean isVisible() {
        if (this.position.f147y <= this.scrollableListYio.viewPosition.f147y + this.scrollableListYio.viewPosition.height && this.position.f147y + this.position.height >= this.scrollableListYio.viewPosition.f147y) {
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
        this.position.f146x = this.scrollableListYio.position.f146x + ((double) this.delta.f144x);
        this.position.f147y = (this.scrollableListYio.position.f147y + ((double) this.delta.f145y)) + ((double) this.scrollableListYio.hook);
    }

    void moveSelection() {
        this.selectionFactor.move();
    }

    public void setBckViewType(int bckViewType) {
        this.bckViewType = bckViewType;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setTitle(String title) {
        this.title = title;
        this.titleWidth = GraphicsYio.getTextWidth(this.scrollableListYio.titleFont, title);
    }

    public void setDescription(String description) {
        this.description = description;
        this.descWidth = GraphicsYio.getTextWidth(this.scrollableListYio.descFont, description);
    }

    public String toString() {
        return "[Item: " + this.key + ", " + this.title + ", " + this.description + "]";
    }
}
