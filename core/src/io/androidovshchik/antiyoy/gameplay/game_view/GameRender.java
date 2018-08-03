package io.androidovshchik.antiyoy.gameplay.game_view;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.androidovshchik.antiyoy.gameplay.GameController;

public abstract class GameRender {
    protected final SpriteBatch batchMovable = this.gameView.batchMovable;
    protected final GameController gameController = this.gameView.gameController;
    protected GameView gameView;
    protected final GrManager grManager;

    public abstract void disposeTextures();

    public abstract void loadTextures();

    public abstract void render();

    public GameRender(GrManager grManager) {
        this.grManager = grManager;
        this.gameView = grManager.gameView;
        grManager.gameRenderList.add(this);
    }
}
