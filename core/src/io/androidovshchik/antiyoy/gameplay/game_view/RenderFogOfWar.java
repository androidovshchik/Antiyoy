package io.androidovshchik.antiyoy.gameplay.game_view;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import java.util.Iterator;
import io.androidovshchik.antiyoy.Storage3xTexture;
import io.androidovshchik.antiyoy.gameplay.FieldController;
import io.androidovshchik.antiyoy.gameplay.fog_of_war.FogOfWarManager;
import io.androidovshchik.antiyoy.gameplay.fog_of_war.FogPoint;
import io.androidovshchik.antiyoy.gameplay.fog_of_war.FogSlice;
import io.androidovshchik.antiyoy.gameplay.rules.GameRules;
import io.androidovshchik.antiyoy.menu.MenuViewYio;
import io.androidovshchik.antiyoy.stuff.AtlasLoader;
import io.androidovshchik.antiyoy.stuff.GraphicsYio;
import io.androidovshchik.antiyoy.stuff.Masking;
import io.androidovshchik.antiyoy.stuff.PointYio;
import io.androidovshchik.antiyoy.stuff.RectangleYio;

public class RenderFogOfWar extends GameRender {
    public static boolean FOG_AS_MASK = true;
    private FieldController fieldController;
    private FogOfWarManager fogOfWarManager;
    private Storage3xTexture fogTexture;
    private float hexSize;
    private float hexStep1;
    private ShapeRenderer shapeRenderer;
    float size;
    private AtlasLoader smallAtlasLoader;
    private TextureRegion whitePixel;

    public RenderFogOfWar(GrManager grManager) {
        super(grManager);
    }

    public void loadTextures() {
        this.whitePixel = GraphicsYio.loadTextureRegion("pixels/white_pixel.png", false);
        if (!FOG_AS_MASK) {
            this.smallAtlasLoader = createAtlasLoader();
            this.fogTexture = new Storage3xTexture(this.smallAtlasLoader, "fog.png");
        }
    }

    public void render() {
        if (GameRules.fogOfWarEnabled) {
            updateReferences();
            this.size = 1.25f * this.fieldController.hexSize;
            renderFogPoints();
            renderBlocks();
            endFog();
        }
    }

    private void updateReferences() {
        this.fieldController = this.gameView.gameController.fieldController;
        this.fogOfWarManager = this.fieldController.fogOfWarManager;
    }

    public void beginFog() {
        if (GameRules.fogOfWarEnabled && FOG_AS_MASK) {
            updateReferences();
            this.batchMovable.begin();
            GraphicsYio.drawByRectangle(this.batchMovable, this.gameView.blackPixel, this.gameController.cameraController.frame);
            this.batchMovable.end();
            Masking.begin();
            MenuViewYio menuViewYio = this.gameController.yioGdxGame.menuViewYio;
            this.shapeRenderer = menuViewYio.shapeRenderer;
            this.shapeRenderer.setProjectionMatrix(this.gameView.orthoCam.combined);
            this.shapeRenderer.begin(ShapeType.Filled);
            drawShapeRendererStuff();
            this.shapeRenderer.end();
            this.shapeRenderer.setProjectionMatrix(menuViewYio.orthoCam.combined);
        }
    }

    private void drawShapeRendererStuff() {
        this.hexSize = this.fogOfWarManager.fieldController.hexSize;
        this.hexStep1 = this.fogOfWarManager.fieldController.hexStep1;
        Iterator it = this.fogOfWarManager.viewSlices.iterator();
        while (it.hasNext()) {
            Iterator it2 = ((FogSlice) it.next()).points.iterator();
            while (it2.hasNext()) {
                PointYio pos = ((FogPoint) it2.next()).position;
                this.shapeRenderer.rect(pos.f144x - (this.hexSize / 2.0f), pos.f145y - (this.hexStep1 / 2.0f), this.hexSize, this.hexStep1);
                this.shapeRenderer.triangle(pos.f144x + (this.hexSize / 2.0f), pos.f145y - (this.hexStep1 / 2.0f), pos.f144x + (this.hexSize / 2.0f), pos.f145y + (this.hexStep1 / 2.0f), pos.f144x + this.hexSize, pos.f145y);
                this.shapeRenderer.triangle(pos.f144x - (this.hexSize / 2.0f), pos.f145y - (this.hexStep1 / 2.0f), pos.f144x - (this.hexSize / 2.0f), pos.f145y + (this.hexStep1 / 2.0f), pos.f144x - this.hexSize, pos.f145y);
            }
        }
    }

    public void continueFog() {
        if (GameRules.fogOfWarEnabled && FOG_AS_MASK) {
            Masking.continueAfterBatchBegin();
        }
    }

    public void endFog() {
        if (FOG_AS_MASK) {
            Masking.end(this.batchMovable);
        }
    }

    private void renderFogPoints() {
        if (!FOG_AS_MASK) {
            for (FogPoint fogPoint : this.fogOfWarManager.fogMap.values()) {
                if (fogPoint.isVisible()) {
                    renderSingleFogPoint(fogPoint);
                }
            }
        }
    }

    private AtlasLoader createAtlasLoader() {
        String path = "fog_of_war/";
        return new AtlasLoader(path + "atlas_texture.png", path + "atlas_structure.txt", false);
    }

    private void renderBlocks() {
        if (!FOG_AS_MASK) {
            renderSingleBlock(this.fogOfWarManager.topBlock);
            renderSingleBlock(this.fogOfWarManager.rightBlock);
            renderSingleBlock(this.fogOfWarManager.bottomBlock);
            renderSingleBlock(this.fogOfWarManager.leftBlock);
        }
    }

    private void renderSingleBlock(RectangleYio block) {
        GraphicsYio.drawByRectangle(this.batchMovable, this.gameView.blackPixel, block);
    }

    private void renderDebug() {
        renderVisibleFogPoints();
        renderVisibleArea();
        renderDebugSlices();
    }

    private void renderDebugSlices() {
        Iterator it = this.fogOfWarManager.viewSlices.iterator();
        while (it.hasNext()) {
            FogSlice viewSlice = (FogSlice) it.next();
            GraphicsYio.drawLine(viewSlice.bottomPoint.position, viewSlice.topPoint.position, (double) GraphicsYio.borderThickness, this.batchMovable, this.whitePixel);
        }
        MenuViewYio menuViewYio = this.gameController.yioGdxGame.menuViewYio;
        this.shapeRenderer = menuViewYio.shapeRenderer;
        this.shapeRenderer.setProjectionMatrix(this.gameView.orthoCam.combined);
        this.shapeRenderer.begin(ShapeType.Line);
        this.hexSize = this.fogOfWarManager.fieldController.hexSize;
        this.hexStep1 = this.fogOfWarManager.fieldController.hexStep1;
        Iterator it2 = this.fogOfWarManager.viewSlices.iterator();
        while (it2.hasNext()) {
            Iterator it3 = ((FogSlice) it2.next()).points.iterator();
            while (it3.hasNext()) {
                PointYio pos = ((FogPoint) it3.next()).position;
                this.shapeRenderer.rect(pos.f144x - (this.hexSize / 2.0f), pos.f145y - (this.hexStep1 / 2.0f), this.hexSize, this.hexStep1);
                this.shapeRenderer.triangle(pos.f144x + (this.hexSize / 2.0f), pos.f145y - (this.hexStep1 / 2.0f), pos.f144x + (this.hexSize / 2.0f), pos.f145y + (this.hexStep1 / 2.0f), pos.f144x + this.hexSize, pos.f145y);
                this.shapeRenderer.triangle(pos.f144x - (this.hexSize / 2.0f), pos.f145y - (this.hexStep1 / 2.0f), pos.f144x - (this.hexSize / 2.0f), pos.f145y + (this.hexStep1 / 2.0f), pos.f144x - this.hexSize, pos.f145y);
            }
        }
        this.shapeRenderer.end();
        this.shapeRenderer.setProjectionMatrix(menuViewYio.orthoCam.combined);
    }

    private void renderVisibleFogPoints() {
        for (FogPoint fogPoint : this.fogOfWarManager.fogMap.values()) {
            if (!fogPoint.status) {
                GraphicsYio.drawFromCenter(this.batchMovable, this.whitePixel, (double) fogPoint.position.f144x, (double) fogPoint.position.f145y, (double) (this.size / 15.0f));
            }
        }
    }

    private void renderVisibleArea() {
        GraphicsYio.renderBorder(this.fogOfWarManager.visibleArea, this.batchMovable, this.whitePixel);
    }

    private void renderSingleFogPoint(FogPoint fogPoint) {
        GraphicsYio.drawFromCenter(this.batchMovable, this.fogTexture.getTexture(this.gameView.currentZoomQuality), (double) fogPoint.position.f144x, (double) fogPoint.position.f145y, (double) this.size);
    }

    public void disposeTextures() {
        this.whitePixel.getTexture().dispose();
        if (!FOG_AS_MASK) {
            this.smallAtlasLoader.disposeAtlasRegion();
        }
    }
}
