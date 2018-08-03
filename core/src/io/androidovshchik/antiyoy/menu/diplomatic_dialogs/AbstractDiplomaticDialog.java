package io.androidovshchik.antiyoy.menu.diplomatic_dialogs;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;
import io.androidovshchik.antiyoy.factor_yio.FactorYio;
import io.androidovshchik.antiyoy.menu.InterfaceElement;
import io.androidovshchik.antiyoy.menu.MenuControllerYio;
import io.androidovshchik.antiyoy.menu.render.MenuRender;
import io.androidovshchik.antiyoy.stuff.Fonts;
import io.androidovshchik.antiyoy.stuff.GraphicsYio;
import io.androidovshchik.antiyoy.stuff.LanguagesManager;
import io.androidovshchik.antiyoy.stuff.PointYio;
import io.androidovshchik.antiyoy.stuff.RectangleYio;
import io.androidovshchik.antiyoy.stuff.object_pool.ObjectPoolYio;

public abstract class AbstractDiplomaticDialog extends InterfaceElement {
    public FactorYio appearFactor = new FactorYio();
    protected float buttonHeight;
    public ArrayList<AcButton> buttons;
    AcButton clickedButton;
    PointYio currentTouch = new PointYio();
    boolean factorMoved = false;
    public ArrayList<AcLabel> labels = new ArrayList();
    protected float leftOffset;
    protected float lineOffset;
    protected MenuControllerYio menuControllerYio;
    protected AcButton noButton;
    ObjectPoolYio<AcLabel> poolLabels;
    public RectangleYio position = new RectangleYio();
    int tagColor;
    public RectangleYio tagPosition;
    protected float titleOffset;
    protected float topOffset;
    boolean touched = false;
    public RectangleYio viewPosition = new RectangleYio();
    protected AcButton yesButton;

    class C01141 extends ObjectPoolYio<AcLabel> {
        C01141() {
        }

        public AcLabel makeNewObject() {
            return new AcLabel(AbstractDiplomaticDialog.this);
        }
    }

    public abstract boolean areButtonsEnabled();

    protected abstract void makeLabels();

    protected abstract void onNoButtonPressed();

    protected abstract void onYesButtonPressed();

    public AbstractDiplomaticDialog(MenuControllerYio menuControllerYio) {
        super(-1);
        this.menuControllerYio = menuControllerYio;
        resetClickedButton();
        this.tagColor = -1;
        this.tagPosition = new RectangleYio();
        initPools();
        initMetrics();
        initButtons();
    }

    protected void resetClickedButton() {
        this.clickedButton = null;
    }

    protected void initButtons() {
        this.buttons = new ArrayList();
        this.yesButton = new AcButton(this);
        this.yesButton.setFont(Fonts.smallerMenuFont);
        this.yesButton.setText(LanguagesManager.getInstance().getString("yes"));
        this.yesButton.setTouchOffset(GraphicsYio.width * 0.05f);
        this.yesButton.setAction(0);
        this.buttons.add(this.yesButton);
        this.noButton = new AcButton(this);
        this.noButton.setFont(Fonts.smallerMenuFont);
        this.noButton.setText(LanguagesManager.getInstance().getString("no"));
        this.noButton.setTouchOffset(GraphicsYio.width * 0.05f);
        this.noButton.setAction(1);
        this.buttons.add(this.noButton);
    }

    protected void updateButtonMetrics() {
        this.yesButton.position.set(0.0d, 0.0d, this.position.width / 2.0d, (double) this.buttonHeight);
        this.yesButton.delta.set(this.position.width / 2.0d, 0.0d);
        this.noButton.position.set(0.0d, 0.0d, this.position.width / 2.0d, (double) this.buttonHeight);
        this.noButton.delta.set(0.0d, 0.0d);
        Iterator it = this.buttons.iterator();
        while (it.hasNext()) {
            ((AcButton) it.next()).updateTextDelta();
        }
    }

    protected void initMetrics() {
        this.leftOffset = 0.08f * GraphicsYio.width;
        this.lineOffset = 0.09f * GraphicsYio.width;
        this.titleOffset = 0.16f * GraphicsYio.width;
        this.topOffset = 0.04f * GraphicsYio.width;
        this.buttonHeight = 0.1f * GraphicsYio.width;
    }

    protected void initPools() {
        this.poolLabels = new C01141();
    }

    public void move() {
        moveAppearFactor();
        updateViewPosition();
        moveLabels();
        moveButtons();
        if (this.factorMoved) {
            updateTagPosition();
        }
    }

    protected void updateTagPosition() {
    }

    protected void moveButtons() {
        if (areButtonsEnabled()) {
            Iterator it = this.buttons.iterator();
            while (it.hasNext()) {
                AcButton button = (AcButton) it.next();
                if (this.factorMoved) {
                    button.updatePosition();
                }
                if (!this.touched) {
                    button.moveSelection();
                }
            }
        }
    }

    protected void moveLabels() {
        if (this.factorMoved) {
            Iterator it = this.labels.iterator();
            while (it.hasNext()) {
                ((AcLabel) it.next()).updatePosition();
            }
        }
    }

    protected void moveAppearFactor() {
        if (this.appearFactor.hasToMove()) {
            this.appearFactor.move();
            this.factorMoved = true;
            return;
        }
        this.factorMoved = false;
    }

    protected void updateViewPosition() {
        if (this.factorMoved) {
            this.viewPosition.setBy(this.position);
            RectangleYio rectangleYio = this.viewPosition;
            rectangleYio.f147y -= ((double) (1.0f - this.appearFactor.get())) * ((this.position.f147y + this.position.height) + ((double) (0.1f * GraphicsYio.width)));
        }
    }

    public FactorYio getFactor() {
        return this.appearFactor;
    }

    public void destroy() {
        this.appearFactor.destroy(1, 2.2d);
    }

    public void appear() {
        this.appearFactor.setValues(0.01d, 0.0d);
        this.appearFactor.appear(3, 1.25d);
        onAppear();
    }

    protected void onAppear() {
        resetClickedButton();
        updateAll();
    }

    protected void clearLabels() {
        Iterator it = this.labels.iterator();
        while (it.hasNext()) {
            this.poolLabels.add((AcLabel) it.next());
        }
        this.labels.clear();
    }

    protected void updateAll() {
        clearLabels();
        makeLabels();
    }

    protected void addLabel(String text, BitmapFont font, float dx, float dy) {
        AcLabel next = (AcLabel) this.poolLabels.getNext();
        next.setText(text);
        next.setFont(font);
        next.setDelta(dx, dy);
        this.labels.add(next);
    }

    public boolean isVisible() {
        return this.appearFactor.get() > 0.0f;
    }

    public boolean checkToPerformAction() {
        if (this.clickedButton == null) {
            return false;
        }
        switch (this.clickedButton.action) {
            case 0:
                onYesButtonPressed();
                break;
            case 1:
                onNoButtonPressed();
                break;
        }
        this.clickedButton = null;
        return true;
    }

    public boolean isTouchable() {
        return true;
    }

    public void convertSourceLineToList(String source, ArrayList<String> list) {
        list.clear();
        StringTokenizer tokenizer = new StringTokenizer(source, " ");
        float x = this.leftOffset;
        float cut = (float) (this.position.width - (1.8d * ((double) this.leftOffset)));
        StringBuilder builder = new StringBuilder();
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            float textWidth = GraphicsYio.getTextWidth(Fonts.smallerMenuFont, token);
            if (x + textWidth > cut) {
                list.add(builder.toString());
                builder.delete(0, builder.length());
                x = this.leftOffset;
            }
            builder.append(token).append(" ");
            x += textWidth;
        }
        list.add(builder.toString());
    }

    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        this.currentTouch.set((double) screenX, (double) screenY);
        if (!(checkToClickButtons() || this.viewPosition.isPointInside(this.currentTouch, 0.0f))) {
            destroy();
        }
        return true;
    }

    boolean checkToClickButtons() {
        if (!areButtonsEnabled()) {
            return false;
        }
        Iterator it = this.buttons.iterator();
        while (it.hasNext()) {
            AcButton button = (AcButton) it.next();
            if (button.isTouched(this.currentTouch)) {
                button.select();
                onButtonClicked(button);
                return true;
            }
        }
        return false;
    }

    public int getTagColor() {
        return this.tagColor;
    }

    public void setTagColor(int tagColor) {
        this.tagColor = tagColor;
    }

    protected void onButtonClicked(AcButton button) {
        this.clickedButton = button;
    }

    public boolean touchDrag(int screenX, int screenY, int pointer) {
        this.currentTouch.set((double) screenX, (double) screenY);
        return true;
    }

    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        this.currentTouch.set((double) screenX, (double) screenY);
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

    protected void onPositionChanged() {
        updateButtonMetrics();
    }

    public MenuRender getRenderSystem() {
        return MenuRender.renderDiplomaticDialog;
    }
}
