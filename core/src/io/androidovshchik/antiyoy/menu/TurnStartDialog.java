package io.androidovshchik.antiyoy.menu;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import yio.tro.antiyoy.factor_yio.FactorYio;
import yio.tro.antiyoy.gameplay.ClickDetector;
import yio.tro.antiyoy.menu.render.MenuRender;
import yio.tro.antiyoy.stuff.Fonts;
import yio.tro.antiyoy.stuff.GraphicsYio;
import yio.tro.antiyoy.stuff.LanguagesManager;
import yio.tro.antiyoy.stuff.PointYio;
import yio.tro.antiyoy.stuff.RectangleYio;
import yio.tro.antiyoy.stuff.Yio;

public class TurnStartDialog extends InterfaceElement {
    public FactorYio alphaFactor = new FactorYio();
    public FactorYio appearFactor = new FactorYio();
    public PointYio circleCenter = new PointYio();
    public boolean circleModeEnabled = false;
    public float circleRadius = 0.0f;
    ClickDetector clickDetector = new ClickDetector();
    PointYio clickPoint = new PointYio();
    public int color = 0;
    PointYio currentTouch = new PointYio();
    public BitmapFont descFont = Fonts.smallerMenuFont;
    public PointYio descPosition = new PointYio();
    public String descString = null;
    public boolean inDestroyState;
    public float maxCircleRadius = 0.0f;
    MenuControllerYio menuControllerYio;
    public RectangleYio position = new RectangleYio();
    boolean readyToDie = false;
    PointYio tempPoint = new PointYio();
    public BitmapFont titleFont = Fonts.gameFont;
    public PointYio titlePosition = new PointYio();
    public String titleString = null;

    public TurnStartDialog(MenuControllerYio menuControllerYio) {
        super(-1);
        this.menuControllerYio = menuControllerYio;
    }

    public void move() {
        this.appearFactor.move();
        this.alphaFactor.move();
        moveCircle();
        checkToCancelCircleMode();
        moveDestroyState();
    }

    private void moveDestroyState() {
        if (this.inDestroyState) {
            this.circleRadius = (1.0f - this.appearFactor.get()) * this.maxCircleRadius;
        }
    }

    private void moveCircle() {
        if (this.circleModeEnabled) {
            this.circleRadius = this.appearFactor.get() * this.maxCircleRadius;
        }
    }

    private void checkToCancelCircleMode() {
        if (this.circleModeEnabled && this.appearFactor.get() == 1.0f) {
            this.circleModeEnabled = false;
            this.menuControllerYio.yioGdxGame.gameController.applyReadyToEndTurn();
        }
    }

    public boolean isCircleModeEnabled() {
        return this.circleModeEnabled;
    }

    public boolean isInDestroyState() {
        return this.inDestroyState;
    }

    public FactorYio getFactor() {
        return this.appearFactor;
    }

    public void destroy() {
        this.appearFactor.destroy(2, 1.0d);
        onDestroy();
    }

    private void onDestroy() {
        this.inDestroyState = true;
    }

    public void appear() {
        this.appearFactor.setValues(0.001d, 0.0d);
        this.appearFactor.appear(3, 0.7d);
        this.alphaFactor.setValues(0.0d, 0.0d);
        this.alphaFactor.appear(3, 1.2d);
        onAppear();
    }

    public float getVerticalTextViewDelta() {
        if (this.circleModeEnabled) {
            return ((-(1.0f - this.appearFactor.get())) * 0.1f) * GraphicsYio.width;
        }
        return 0.0f;
    }

    private void onAppear() {
        this.readyToDie = false;
        this.circleModeEnabled = true;
        this.inDestroyState = false;
        updateCircleMetricsToStartFromCorner();
    }

    public boolean isVisible() {
        return this.appearFactor.get() > 0.0f;
    }

    public boolean checkToPerformAction() {
        if (!this.readyToDie) {
            return false;
        }
        destroy();
        updateCirleMetricsByClickPoint();
        return true;
    }

    private void updateCirleMetricsByClickPoint() {
        this.circleCenter.setBy(this.clickPoint);
        this.maxCircleRadius = 0.0f;
        updateMaxCircleRadiusByPoint(0.0d, 0.0d);
        updateMaxCircleRadiusByPoint(0.0d, (double) GraphicsYio.height);
        updateMaxCircleRadiusByPoint((double) GraphicsYio.width, 0.0d);
        updateMaxCircleRadiusByPoint((double) GraphicsYio.width, (double) GraphicsYio.height);
        this.maxCircleRadius += 0.1f * GraphicsYio.width;
        moveDestroyState();
    }

    private void updateMaxCircleRadiusByPoint(double x, double y) {
        this.tempPoint.set(x, y);
        this.maxCircleRadius = (float) Math.max((double) this.maxCircleRadius, this.circleCenter.distanceTo(this.tempPoint));
    }

    public boolean isTouchable() {
        return true;
    }

    public void setColor(int color) {
        this.color = color;
        this.descString = this.menuControllerYio.getColorNameWithoutOffset(color, "_player");
        float textWidth = GraphicsYio.getTextWidth(this.descFont, this.descString);
        this.descPosition.f144x = (GraphicsYio.width / 2.0f) - (textWidth / 2.0f);
        this.descPosition.f145y = 0.55f * GraphicsYio.height;
    }

    private void updateCurrentTouch(int screenX, int screenY) {
        this.currentTouch.set((double) screenX, (double) screenY);
    }

    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        updateCurrentTouch(screenX, screenY);
        this.clickDetector.touchDown(this.currentTouch);
        return true;
    }

    public boolean touchDrag(int screenX, int screenY, int pointer) {
        updateCurrentTouch(screenX, screenY);
        this.clickDetector.touchDrag(this.currentTouch);
        return true;
    }

    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        updateCurrentTouch(screenX, screenY);
        this.clickDetector.touchUp(this.currentTouch);
        if (this.clickDetector.isClicked() && !this.circleModeEnabled) {
            onClick();
        }
        return true;
    }

    private void onClick() {
        this.readyToDie = true;
        this.clickPoint.setBy(this.currentTouch);
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
        this.titleString = LanguagesManager.getInstance().getString("player_turn");
        float textWidth = GraphicsYio.getTextWidth(this.titleFont, this.titleString);
        this.titlePosition.f144x = (GraphicsYio.width / 2.0f) - (textWidth / 2.0f);
        this.titlePosition.f145y = 0.6f * GraphicsYio.height;
    }

    private void updateCircleMetricsToStartFromCorner() {
        this.maxCircleRadius = ((float) Yio.distance(0.0d, 0.0d, this.position.width, this.position.height)) + (0.1f * GraphicsYio.width);
        this.circleCenter.f144x = (float) (this.position.f146x + this.position.width);
        this.circleCenter.f145y = (float) this.position.f147y;
        moveCircle();
    }

    public MenuRender getRenderSystem() {
        return MenuRender.renderTurnStartDialog;
    }
}
