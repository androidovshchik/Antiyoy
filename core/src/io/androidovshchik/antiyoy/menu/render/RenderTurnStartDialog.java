package io.androidovshchik.antiyoy.menu.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import yio.tro.antiyoy.menu.InterfaceElement;
import yio.tro.antiyoy.menu.TurnStartDialog;
import yio.tro.antiyoy.stuff.GraphicsYio;
import yio.tro.antiyoy.stuff.Masking;

public class RenderTurnStartDialog extends MenuRender {
    private TextureRegion[] bckColors;
    TurnStartDialog dialog;

    public void loadTextures() {
        this.bckColors = new TextureRegion[7];
        for (int i = 0; i < this.bckColors.length; i++) {
            this.bckColors[i] = GraphicsYio.loadTextureRegion("diplomacy/color" + (i + 1) + ".png", false);
        }
    }

    public void renderFirstLayer(InterfaceElement element) {
    }

    public void renderSecondLayer(InterfaceElement element) {
        this.dialog = (TurnStartDialog) element;
        GraphicsYio.setBatchAlpha(this.batch, (double) this.dialog.alphaFactor.get());
        renderMain();
        GraphicsYio.setBatchAlpha(this.batch, 1.0d);
    }

    private void renderMain() {
        if (this.dialog.isCircleModeEnabled()) {
            renderCircle();
        } else if (this.dialog.isInDestroyState()) {
            renderDestroyState();
        } else {
            renderInternals();
        }
    }

    private void renderDestroyState() {
        this.batch.end();
        Masking.begin();
        drawShapeRendererStuff();
        this.batch.begin();
        Gdx.gl.glColorMask(true, true, true, true);
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glDepthFunc(GL20.GL_LESS);
        renderInternals();
        Masking.end(this.batch);
    }

    private void renderInternals() {
        GraphicsYio.drawByRectangle(this.batch, this.bckColors[this.dialog.color], this.dialog.position);
        renderTitle();
        renderDescription();
    }

    private void renderDescription() {
        BitmapFont descFont = this.dialog.descFont;
        Color color = descFont.getColor();
        descFont.setColor(Color.BLACK);
        GraphicsYio.setFontAlpha(descFont, (double) this.dialog.alphaFactor.get());
        descFont.draw(this.batch, this.dialog.descString, this.dialog.descPosition.f144x, this.dialog.descPosition.f145y + this.dialog.getVerticalTextViewDelta());
        GraphicsYio.setFontAlpha(descFont, 1.0d);
        descFont.setColor(color);
    }

    private void renderTitle() {
        BitmapFont titleFont = this.dialog.titleFont;
        Color color = titleFont.getColor();
        titleFont.setColor(Color.BLACK);
        GraphicsYio.setFontAlpha(titleFont, (double) this.dialog.alphaFactor.get());
        titleFont.draw(this.batch, this.dialog.titleString, this.dialog.titlePosition.f144x, this.dialog.titlePosition.f145y + this.dialog.getVerticalTextViewDelta());
        GraphicsYio.setFontAlpha(titleFont, 1.0d);
        titleFont.setColor(color);
    }

    private void renderCircle() {
        this.batch.end();
        Masking.begin();
        drawShapeRendererStuff();
        this.batch.begin();
        Masking.continueAfterBatchBegin();
        renderInternals();
        Masking.end(this.batch);
    }

    private void drawShapeRendererStuff() {
        ShapeRenderer shapeRenderer = this.menuViewYio.shapeRenderer;
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.circle(this.dialog.circleCenter.f144x, this.dialog.circleCenter.f145y, this.dialog.circleRadius);
        shapeRenderer.end();
    }

    public void renderThirdLayer(InterfaceElement element) {
    }
}
