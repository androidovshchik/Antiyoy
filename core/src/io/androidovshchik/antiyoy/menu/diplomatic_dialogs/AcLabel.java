package io.androidovshchik.antiyoy.menu.diplomatic_dialogs;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import yio.tro.antiyoy.stuff.PointYio;
import yio.tro.antiyoy.stuff.object_pool.ReusableYio;

public class AcLabel implements ReusableYio {
    PointYio delta = new PointYio();
    AbstractDiplomaticDialog dialog;
    public BitmapFont font;
    public PointYio position = new PointYio();
    public String text;

    public AcLabel(AbstractDiplomaticDialog dialog) {
        this.dialog = dialog;
    }

    public void reset() {
        this.position.reset();
        this.delta.reset();
        this.font = null;
        this.text = null;
    }

    void updatePosition() {
        this.position.f144x = (float) (this.dialog.viewPosition.f146x + ((double) this.delta.f144x));
        this.position.f145y = (float) (this.dialog.viewPosition.f147y + ((double) this.delta.f145y));
    }

    void setDelta(float x, float y) {
        this.delta.set((double) x, (double) y);
    }

    public void setFont(BitmapFont font) {
        this.font = font;
    }

    public void setText(String text) {
        this.text = text;
    }
}
