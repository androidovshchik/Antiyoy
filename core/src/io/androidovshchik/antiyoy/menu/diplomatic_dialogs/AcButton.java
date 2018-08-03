package io.androidovshchik.antiyoy.menu.diplomatic_dialogs;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import io.androidovshchik.antiyoy.factor_yio.FactorYio;
import io.androidovshchik.antiyoy.stuff.GraphicsYio;
import io.androidovshchik.antiyoy.stuff.PointYio;
import io.androidovshchik.antiyoy.stuff.RectangleYio;

public class AcButton {
    public static final int ACTION_NO = 1;
    public static final int ACTION_YES = 0;
    public int action = -1;
    PointYio delta = new PointYio();
    AbstractDiplomaticDialog dialog;
    public BitmapFont font = null;
    public RectangleYio position = new RectangleYio();
    public FactorYio selectionFactor = new FactorYio();
    public String text = null;
    PointYio textDelta = new PointYio();
    public PointYio textPosition = new PointYio();
    float touchOffset = 0.0f;

    public AcButton(AbstractDiplomaticDialog dialog) {
        this.dialog = dialog;
    }

    void select() {
        this.selectionFactor.setValues(1.0d, 0.0d);
        this.selectionFactor.destroy(1, 2.0d);
    }

    public boolean isSelected() {
        return this.selectionFactor.get() > 0.0f;
    }

    boolean isTouched(PointYio touchPoint) {
        if (((double) touchPoint.f144x) >= this.position.f146x && ((double) touchPoint.f145y) >= this.position.f147y - ((double) this.touchOffset) && ((double) touchPoint.f144x) <= this.position.f146x + this.position.width && ((double) touchPoint.f145y) <= (this.position.f147y + this.position.height) + ((double) this.touchOffset)) {
            return true;
        }
        return false;
    }

    void updatePosition() {
        this.position.f146x = this.dialog.viewPosition.f146x + ((double) this.delta.f144x);
        this.position.f147y = this.dialog.viewPosition.f147y + ((double) this.delta.f145y);
        this.textPosition.f144x = (float) (this.position.f146x + ((double) this.textDelta.f144x));
        this.textPosition.f145y = (float) (this.position.f147y + ((double) this.textDelta.f145y));
    }

    void moveSelection() {
        this.selectionFactor.move();
    }

    public void setFont(BitmapFont font) {
        this.font = font;
    }

    public void setText(String text) {
        this.text = text;
    }

    void updateTextDelta() {
        if (this.font != null && this.text != null) {
            float textWidth = GraphicsYio.getTextWidth(this.font, this.text);
            float textHeight = GraphicsYio.getTextHeight(this.font, this.text);
            this.textDelta.f144x = (float) ((this.position.width / 2.0d) - ((double) (textWidth / 2.0f)));
            this.textDelta.f145y = (float) ((this.position.height / 2.0d) + ((double) (textHeight / 2.0f)));
        }
    }

    public void setAction(int action) {
        this.action = action;
    }

    public void setTouchOffset(float touchOffset) {
        this.touchOffset = touchOffset;
    }
}
