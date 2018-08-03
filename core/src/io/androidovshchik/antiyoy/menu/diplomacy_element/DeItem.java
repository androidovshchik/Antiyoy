package io.androidovshchik.antiyoy.menu.diplomacy_element;

import yio.tro.antiyoy.factor_yio.FactorYio;
import yio.tro.antiyoy.stuff.GraphicsYio;
import yio.tro.antiyoy.stuff.PointYio;
import yio.tro.antiyoy.stuff.RectangleYio;
import yio.tro.antiyoy.stuff.object_pool.ReusableYio;

public class DeItem implements ReusableYio, Comparable<DeItem> {
    public static final int STATUS_DEAD = 3;
    public static final int STATUS_ENEMY = 2;
    public static final int STATUS_FRIEND = 1;
    public static final int STATUS_NEUTRAL = 0;
    public boolean blackMarkEnabled;
    public PointYio blackMarkPosition = new PointYio();
    public float blackMarkRadius;
    public RectangleYio bottomRectangle = new RectangleYio();
    public int colorIndex;
    PointYio delta = new PointYio();
    float descOffset;
    public PointYio descPosition = new PointYio();
    public String descriptionString;
    DiplomacyElement diplomacyElement;
    public boolean keepSelection;
    public RectangleYio position = new RectangleYio();
    FactorYio selectionFactor = new FactorYio();
    public int status;
    float statusOffDelta;
    float statusOffset;
    public PointYio statusPosition = new PointYio();
    public float statusRadius;
    public String title;
    float titleHeight;
    float titleOffset;
    public PointYio titlePosition = new PointYio();
    float titleWidth;

    public DeItem(DiplomacyElement diplomacyElement) {
        this.diplomacyElement = diplomacyElement;
    }

    public void reset() {
        this.position.reset();
        this.delta.reset();
        this.titlePosition.reset();
        this.statusPosition.reset();
        this.blackMarkPosition.reset();
        this.colorIndex = -1;
        this.title = null;
        this.titleHeight = 0.0f;
        this.titleWidth = 0.0f;
        this.status = -1;
        initMetrics();
        this.descPosition.reset();
        this.descriptionString = null;
        this.blackMarkEnabled = false;
        this.bottomRectangle.reset();
        setKeepSelection(false);
    }

    private void initMetrics() {
        this.titleOffset = GraphicsYio.width * 0.025f;
        this.statusOffset = GraphicsYio.width * 0.05f;
        this.statusRadius = GraphicsYio.width * 0.05f;
        this.statusOffDelta = 0.01f * GraphicsYio.width;
        this.descOffset = 0.1f * GraphicsYio.width;
        this.blackMarkRadius = GraphicsYio.width * 0.025f;
    }

    boolean isTouched(PointYio touchPoint) {
        return this.diplomacyElement.isTouchInsideRectangle(touchPoint.f144x, touchPoint.f145y, this.position, 0.0f);
    }

    public boolean isTopVisible() {
        if (this.position.f147y + (this.position.height / 2.0d) <= this.diplomacyElement.internalBackground.f147y + this.diplomacyElement.internalBackground.height && this.position.f147y + this.position.height >= this.diplomacyElement.internalBackground.f147y) {
            return true;
        }
        return false;
    }

    public boolean isBottomVisible() {
        if (this.position.f147y <= this.diplomacyElement.internalBackground.f147y + this.diplomacyElement.internalBackground.height && this.position.f147y + (this.position.height / 2.0d) >= this.diplomacyElement.internalBackground.f147y) {
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
        updateStatusPosition();
        updateDescriptionPosition();
        updateBottomRectangle();
        updateBlackMarkPosition();
    }

    private void updateBlackMarkPosition() {
        if (this.blackMarkEnabled) {
            this.blackMarkPosition.f144x = ((this.titlePosition.f144x + this.titleWidth) + this.titleOffset) + this.blackMarkRadius;
            this.blackMarkPosition.f145y = this.titlePosition.f145y - (this.titleHeight / 2.0f);
        }
    }

    private void updateBottomRectangle() {
        this.bottomRectangle.setBy(this.position);
        this.bottomRectangle.height = this.position.height / 2.0d;
    }

    private void updateDescriptionPosition() {
        this.descPosition.f144x = (float) (this.position.f146x + ((double) this.titleOffset));
        this.descPosition.f145y = (float) ((this.position.f147y + this.position.height) - ((double) this.descOffset));
    }

    private void updateStatusPosition() {
        this.statusPosition.f144x = (float) (((this.position.f146x + this.position.width) - ((double) this.statusOffset)) - ((double) this.statusRadius));
        this.statusPosition.f145y = (float) ((((this.position.f147y + this.position.height) - ((double) this.titleOffset)) - ((double) this.statusOffDelta)) - ((double) (this.titleHeight / 2.0f)));
    }

    private void updateTitlePosition() {
        this.titlePosition.f144x = (float) (this.position.f146x + ((double) this.titleOffset));
        this.titlePosition.f145y = (float) ((this.position.f147y + this.position.height) - ((double) this.titleOffset));
    }

    private void updatePosition() {
        this.position.f146x = this.diplomacyElement.viewPosition.f146x + ((double) this.delta.f144x);
        this.position.f147y = (this.diplomacyElement.viewPosition.f147y + ((double) this.delta.f145y)) + ((double) this.diplomacyElement.hook);
    }

    void moveSelection() {
        if (!this.keepSelection) {
            this.selectionFactor.move();
        }
    }

    public void setKeepSelection(boolean keepSelection) {
        this.keepSelection = keepSelection;
    }

    public void setColorIndex(int colorIndex) {
        this.colorIndex = colorIndex;
    }

    public void setTitle(String title) {
        this.title = title;
        updateTitleMetrics();
    }

    private void updateTitleMetrics() {
        this.titleHeight = GraphicsYio.getTextHeight(this.diplomacyElement.titleFont, this.title);
        this.titleWidth = GraphicsYio.getTextWidth(this.diplomacyElement.titleFont, this.title);
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setBlackMarkEnabled(boolean blackMarkEnabled) {
        this.blackMarkEnabled = blackMarkEnabled;
    }

    public void setDescriptionString(String descriptionString) {
        this.descriptionString = descriptionString;
    }

    public int compareTo(DeItem o) {
        return this.colorIndex - o.colorIndex;
    }
}
