package io.androidovshchik.antiyoy.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import io.androidovshchik.antiyoy.Settings;
import io.androidovshchik.antiyoy.YioGdxGame;
import io.androidovshchik.antiyoy.stuff.GraphicsYio;
import io.androidovshchik.antiyoy.stuff.PointYio;
import io.androidovshchik.antiyoy.stuff.RectangleYio;
import io.androidovshchik.antiyoy.stuff.Yio;

public class CameraController {
    public static final double ZOOM_CATCH_DISTANCE = 0.002d;
    PointYio actualDragBounds = new PointYio();
    PointYio backVisBounds = new PointYio();
    boolean blockDragMovement;
    float boundHeight;
    float boundWidth;
    public float camDx;
    public float camDy;
    public float camDz;
    RectangleYio currentMultiTouch = new RectangleYio();
    PointYio defaultDragBounds = new PointYio();
    PointYio delta = new PointYio();
    public RectangleYio field = new RectangleYio();
    public RectangleYio frame = new RectangleYio();
    GameController gameController;
    int f93h = ((int) GraphicsYio.height);
    private PointYio initialTouch = new PointYio();
    PointYio kinetics = new PointYio();
    boolean kineticsEnabled = false;
    double kineticsSpeed = (0.01d * ((double) this.f94w));
    RectangleYio lastMultiTouch = new RectangleYio();
    long lastTapTime;
    OrthographicCamera orthoCam;
    PointYio position = new PointYio();
    private float sensitivityModifier;
    public float targetZoomLevel;
    long touchDownTime;
    PointYio touchPos = new PointYio();
    boolean touched;
    PointYio viewPosition = new PointYio();
    public float viewZoomLevel;
    int f94w = ((int) GraphicsYio.width);
    YioGdxGame yioGdxGame;
    float zoomMaximum;
    float zoomMinimum = 0.5f;
    double[][] zoomValues;

    public CameraController(GameController gameController) {
        this.gameController = gameController;
        this.yioGdxGame = gameController.yioGdxGame;
    }

    public void initLevels(int levelSize) {
        this.zoomValues = new double[][]{new double[]{0.8d, 1.3d, 1.1d}, new double[]{0.8d, 1.3d, 2.0d}, new double[]{0.8d, 1.3d, 2.1d}};
        updateUpperZoomLimit(levelSize);
    }

    public void updateUpperZoomLimit(int levelSize) {
        int zIndex = 0;
        switch (levelSize) {
            case 1:
                zIndex = 0;
                break;
            case 2:
                zIndex = 1;
                break;
            case 4:
                zIndex = 2;
                break;
        }
        setZoomMaximum(this.zoomValues[zIndex][2]);
    }

    public void init(int levelSize) {
        initLevels(levelSize);
        if (this.orthoCam != null) {
            forceCameraMovementToRealPosition();
        }
    }

    private void forceCameraMovementToRealPosition() {
        for (int i = 0; i < 20; i++) {
            move();
        }
    }

    private void updateCurrentMultiTouch() {
        this.currentMultiTouch.set(0.0d, 0.0d, (double) (Gdx.input.getX(1) - Gdx.input.getX(0)), (double) (Gdx.input.getY(1) - Gdx.input.getY(0)));
    }

    private void updateLastMultiTouch() {
        this.lastMultiTouch.setBy(this.currentMultiTouch);
    }

    void touchDown(int x, int y) {
        this.touched = this.gameController.currentTouchCount > 0;
        if (this.gameController.currentTouchCount == 1) {
            this.touchDownTime = this.gameController.currentTime;
            this.blockDragMovement = false;
            this.initialTouch.set((double) x, (double) y);
            this.touchPos.set((double) x, (double) y);
            this.delta.set(0.0d, 0.0d);
        }
        if (this.gameController.currentTouchCount >= 2) {
            this.blockDragMovement = true;
            updateCurrentMultiTouch();
            updateLastMultiTouch();
        }
    }

    void touchDrag(int x, int y) {
        this.sensitivityModifier = 1.4f * Settings.sensitivity;
        this.delta.f144x = (this.sensitivityModifier * (this.touchPos.f144x - ((float) x))) * this.viewZoomLevel;
        this.delta.f145y = (this.sensitivityModifier * (this.touchPos.f145y - ((float) y))) * this.viewZoomLevel;
        if (!this.blockDragMovement) {
            PointYio pointYio = this.position;
            pointYio.f144x += this.delta.f144x;
            pointYio = this.position;
            pointYio.f145y += this.delta.f145y;
            applyBoundsToPosition();
        }
        this.touchPos.set((double) x, (double) y);
        if (this.gameController.currentTouchCount == 2) {
            updateCurrentMultiTouch();
            changeZoomLevel(0.004d * (Yio.distance(0.0d, 0.0d, this.lastMultiTouch.width, this.lastMultiTouch.height) - Yio.distance(0.0d, 0.0d, this.currentMultiTouch.width, this.currentMultiTouch.height)));
            updateLastMultiTouch();
        }
    }

    public void setTargetZoomLevel(float targetZoomLevel) {
        this.targetZoomLevel = targetZoomLevel;
    }

    public void setTargetZoomToMax() {
        setTargetZoomLevel(0.9f * this.zoomMaximum);
    }

    public void changeZoomLevel(double zoomDelta) {
        this.targetZoomLevel = (float) (((double) this.targetZoomLevel) + zoomDelta);
        if (this.targetZoomLevel < this.zoomMinimum) {
            this.targetZoomLevel = this.zoomMinimum;
        }
        if (this.targetZoomLevel > this.zoomMaximum) {
            this.targetZoomLevel = this.zoomMaximum;
        }
    }

    private void applyBoundsToPosition() {
        if (this.position.f144x > this.actualDragBounds.f144x) {
            this.position.f144x = this.actualDragBounds.f144x;
        }
        if (this.position.f144x < (-this.actualDragBounds.f144x)) {
            this.position.f144x = -this.actualDragBounds.f144x;
        }
        if (this.position.f145y > this.actualDragBounds.f145y) {
            this.position.f145y = this.actualDragBounds.f145y;
        }
        if (this.position.f145y < (-this.actualDragBounds.f145y)) {
            this.position.f145y = -this.actualDragBounds.f145y;
        }
    }

    void touchUp(int x, int y) {
        if (this.touched) {
            this.touched = this.gameController.currentTouchCount > 0;
            double speed = Yio.distance(0.0d, 0.0d, (double) this.delta.f144x, (double) this.delta.f145y);
            if (!this.blockDragMovement && (speed > this.kineticsSpeed || touchWasQuick())) {
                this.kineticsEnabled = true;
                this.kinetics.f144x = this.delta.f144x;
                this.kinetics.f145y = this.delta.f145y;
            }
            this.touchPos.set((double) x, (double) y);
        }
    }

    private boolean touchWasQuick() {
        return System.currentTimeMillis() - this.touchDownTime < 200;
    }

    void move() {
        updateDragBounds();
        updateField();
        moveKinetics();
        moveDrag();
        moveZoom();
        updateFrame();
        updateBackgroundVisibility();
    }

    private void updateDragBounds() {
        this.actualDragBounds.setBy(this.defaultDragBounds);
        PointYio pointYio = this.actualDragBounds;
        pointYio.f144x = (float) (((double) pointYio.f144x) - ((0.4d * ((double) this.f94w)) * ((double) this.viewZoomLevel)));
        pointYio = this.actualDragBounds;
        pointYio.f145y = (float) (((double) pointYio.f145y) - ((0.45d * ((double) this.f93h)) * ((double) this.viewZoomLevel)));
        if (this.actualDragBounds.f144x < 0.0f) {
            this.actualDragBounds.f144x = 0.0f;
        }
        if (this.actualDragBounds.f145y < 0.0f) {
            this.actualDragBounds.f145y = 0.0f;
        }
    }

    private void moveKinetics() {
        if (this.kineticsEnabled) {
            if (Yio.distance(0.0d, 0.0d, (double) this.kinetics.f144x, (double) this.kinetics.f145y) < 0.5d * this.kineticsSpeed) {
                this.kineticsEnabled = false;
            }
            PointYio pointYio = this.position;
            pointYio.f144x += this.kinetics.f144x;
            pointYio = this.position;
            pointYio.f145y += this.kinetics.f145y;
            applyBoundsToPosition();
            pointYio = this.kinetics;
            pointYio.f144x = (float) (((double) pointYio.f144x) * 0.85d);
            pointYio = this.kinetics;
            pointYio.f145y = (float) (((double) pointYio.f145y) * 0.85d);
        }
    }

    private void updateBackgroundVisibility() {
        this.backVisBounds.setBy(this.defaultDragBounds);
        PointYio pointYio = this.backVisBounds;
        pointYio.f144x = (float) (((double) pointYio.f144x) - ((((double) this.f94w) * 0.5d) * ((double) this.viewZoomLevel)));
        pointYio = this.backVisBounds;
        pointYio.f145y = (float) (((double) pointYio.f145y) - ((((double) this.f93h) * 0.5d) * ((double) this.viewZoomLevel)));
        if (Math.abs(this.position.f144x) > this.backVisBounds.f144x || Math.abs(this.position.f145y) > this.backVisBounds.f145y) {
            this.gameController.setBackgroundVisible(true);
        } else {
            this.gameController.setBackgroundVisible(false);
        }
    }

    private void updateField() {
        this.field.f146x = (double) ((((float) this.f94w) * 0.5f) - (this.orthoCam.position.x / this.orthoCam.zoom));
        this.field.f147y = (double) ((((float) this.f93h) * 0.5f) - (this.orthoCam.position.y / this.orthoCam.zoom));
        this.field.width = (double) (this.boundWidth / this.orthoCam.zoom);
        this.field.height = (double) (this.boundHeight / this.orthoCam.zoom);
    }

    private void moveZoom() {
        this.camDz = 0.2f * (this.targetZoomLevel - this.viewZoomLevel);
        if (((double) Math.abs(this.targetZoomLevel - this.viewZoomLevel)) >= ZOOM_CATCH_DISTANCE) {
            OrthographicCamera orthographicCamera = this.yioGdxGame.gameView.orthoCam;
            orthographicCamera.zoom += this.camDz;
            this.viewZoomLevel += this.camDz;
            this.yioGdxGame.gameView.updateCam();
            applyBoundsToPosition();
        }
    }

    public boolean isPosInViewFrame(PointYio pos, float offset) {
        if (((double) pos.f144x) >= this.frame.f146x - ((double) offset) && ((double) pos.f144x) <= (this.frame.f146x + this.frame.width) + ((double) offset) && ((double) pos.f145y) >= this.frame.f147y - ((double) offset) && ((double) pos.f145y) <= (this.frame.f147y + this.frame.height) + ((double) offset)) {
            return true;
        }
        return false;
    }

    public void stop() {
        this.position.setBy(this.viewPosition);
        this.camDx = 0.0f;
        this.camDy = 0.0f;
        this.kinetics.set(0.0d, 0.0d);
    }

    void moveDrag() {
        this.camDx = (this.position.f144x - this.viewPosition.f144x) * 0.5f;
        this.camDy = (this.position.f145y - this.viewPosition.f145y) * 0.5f;
        PointYio pointYio = this.viewPosition;
        pointYio.f144x += this.camDx;
        pointYio = this.viewPosition;
        pointYio.f145y += this.camDy;
        this.yioGdxGame.gameView.orthoCam.translate(this.camDx, this.camDy);
        this.yioGdxGame.gameView.updateCam();
    }

    public void setBounds(float width, float height) {
        this.boundWidth = width;
        this.boundHeight = height;
        this.defaultDragBounds.set((double) (this.boundWidth / 2.0f), (double) (this.boundHeight / 2.0f));
    }

    public void setZoomMaximum(double zoomMaximum) {
        this.zoomMaximum = (float) zoomMaximum;
    }

    void createCamera() {
        this.gameController.yioGdxGame.gameView.createOrthoCam();
        this.orthoCam = this.gameController.yioGdxGame.gameView.orthoCam;
        this.orthoCam.translate((this.gameController.boundWidth - GraphicsYio.width) / 2.0f, (this.gameController.boundHeight - GraphicsYio.height) / 2.0f);
        this.gameController.yioGdxGame.gameView.updateCam();
        updateFrame();
        forceCameraMovementToRealPosition();
    }

    void defaultValues() {
        this.viewZoomLevel = 1.0f;
        this.targetZoomLevel = 1.0f;
        this.position.set(0.0d, 0.0d);
        this.viewPosition.setBy(this.position);
    }

    void updateFrame() {
        this.frame.f146x = (double) (((0.0f - (((float) this.f94w) * 0.5f)) * this.orthoCam.zoom) + this.orthoCam.position.x);
        this.frame.f147y = (double) (((0.0f - (((float) this.f93h) * 0.5f)) * this.orthoCam.zoom) + this.orthoCam.position.y);
        this.frame.width = (double) (((float) this.f94w) * this.orthoCam.zoom);
        this.frame.height = (double) (((float) this.f93h) * this.orthoCam.zoom);
    }

    public double[][] getZoomValues() {
        return this.zoomValues;
    }

    public void forgetAboutLastTap() {
        this.lastTapTime = 0;
    }

    public float getTargetZoomLevel() {
        return this.targetZoomLevel;
    }

    public void focusOnPoint(PointYio position) {
        focusOnPoint((double) position.f144x, (double) position.f145y);
    }

    boolean checkConditionsToEndTurn() {
        if (((double) this.camDx) <= 0.01d && ((double) this.camDy) <= 0.01d && ((double) Math.abs(this.targetZoomLevel - this.viewZoomLevel)) <= ZOOM_CATCH_DISTANCE) {
            return true;
        }
        return false;
    }

    public void onEndTurnButtonPressed() {
        stop();
        this.gameController.resetCurrentTouchCount();
    }

    public boolean touchedAsClick() {
        return this.gameController.touchPoint.distanceTo(this.initialTouch) < 0.03d * ((double) GraphicsYio.width) && ((double) Math.abs(this.camDx)) < ((double) GraphicsYio.width) * 0.01d && ((double) Math.abs(this.camDy)) < ((double) GraphicsYio.width) * 0.01d;
    }

    public void focusOnPoint(double x, double y) {
        this.position.f144x = (float) (x - ((double) (this.gameController.boundWidth / 2.0f)));
        this.position.f145y = (float) (y - ((double) (this.gameController.boundHeight / 2.0f)));
    }
}
