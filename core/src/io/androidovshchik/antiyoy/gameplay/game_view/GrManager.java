package io.androidovshchik.antiyoy.gameplay.game_view;

import java.util.ArrayList;
import java.util.Iterator;

public class GrManager {
    public ArrayList<GameRender> gameRenderList = new ArrayList();
    GameView gameView;
    public RenderFogOfWar renderFogOfWar;
    RenderLevelEditorStuff renderLevelEditorStuff;

    public GrManager(GameView gameView) {
        this.gameView = gameView;
    }

    public void create() {
        this.renderLevelEditorStuff = new RenderLevelEditorStuff(this);
        this.renderFogOfWar = new RenderFogOfWar(this);
    }

    public void render() {
        this.renderLevelEditorStuff.render();
        this.renderFogOfWar.render();
    }

    public void loadTextures() {
        Iterator it = this.gameRenderList.iterator();
        while (it.hasNext()) {
            ((GameRender) it.next()).loadTextures();
        }
    }

    public void disposeTextures() {
        Iterator it = this.gameRenderList.iterator();
        while (it.hasNext()) {
            ((GameRender) it.next()).disposeTextures();
        }
    }
}
