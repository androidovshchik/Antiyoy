package io.androidovshchik.antiyoy.gameplay.game_view;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.androidovshchik.antiyoy.gameplay.GameController;

public abstract class GameRender {
    protected GameView gameView;
    protected SpriteBatch batchMovable;
    protected GameController gameController;
    protected final GrManager grManager;

    public abstract void disposeTextures();

    public abstract void loadTextures();

    public abstract void render();

    public GameRender(GrManager grManager) {
        this.grManager = grManager;
        this.gameView = grManager.gameView;
        this.batchMovable = gameView.batchMovable;
        this.gameController = gameView.gameController;
        grManager.gameRenderList.add(this);
    }
}
