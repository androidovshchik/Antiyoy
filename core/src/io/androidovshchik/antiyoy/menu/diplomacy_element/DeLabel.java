package io.androidovshchik.antiyoy.menu.diplomacy_element;

import yio.tro.antiyoy.factor_yio.FactorYio;
import yio.tro.antiyoy.stuff.GraphicsYio;
import yio.tro.antiyoy.stuff.LanguagesManager;
import yio.tro.antiyoy.stuff.PointYio;
import yio.tro.antiyoy.stuff.RectangleYio;

public class DeLabel {
    public FactorYio appearFactor;
    DiplomacyElement diplomacyElement;
    public PointYio position = new PointYio();
    public String text = LanguagesManager.getInstance().getString("diplomacy");
    public float textWidth;
    public boolean visible;

    public DeLabel(DiplomacyElement diplomacyElement) {
        this.diplomacyElement = diplomacyElement;
        this.textWidth = GraphicsYio.getTextWidth(diplomacyElement.titleFont, this.text);
        this.visible = true;
        this.appearFactor = new FactorYio();
        appear();
    }

    void move() {
        this.appearFactor.move();
        updatePosition();
    }

    void appear() {
        this.appearFactor.setValues(1.0d, 0.0d);
        this.appearFactor.appear(1, 1.0d);
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
        if (visible) {
            appear();
        }
    }

    void updatePosition() {
        RectangleYio pos = this.diplomacyElement.viewPosition;
        this.position.f144x = (float) ((pos.f146x + (pos.width / 2.0d)) - ((double) (this.textWidth / 2.0f)));
        this.position.f145y = (float) ((pos.f147y + pos.height) - ((double) (0.02f * GraphicsYio.width)));
    }
}
