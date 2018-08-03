package io.androidovshchik.antiyoy.menu.behaviors.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Matrix4;
import io.androidovshchik.antiyoy.YioGdxGame;
import io.androidovshchik.antiyoy.gameplay.GameController;
import io.androidovshchik.antiyoy.gameplay.game_view.GameView;
import io.androidovshchik.antiyoy.menu.ButtonYio;
import io.androidovshchik.antiyoy.menu.MenuControllerYio;
import io.androidovshchik.antiyoy.stuff.Fonts;
import io.androidovshchik.antiyoy.stuff.FrameBufferYio;

public class ColorStatsRenderer {
    private SpriteBatch batch = new SpriteBatch();
    TextureRegion blackPixel = GameView.loadTextureRegion("black_pixel.png", false);
    TextureRegion bluePixel = GameView.loadTextureRegion("pixels/pixel_blue.png", false);
    TextureRegion buttonBackground = GameView.loadTextureRegion("pixels/pixel_dark_gray.png", true);
    TextureRegion cyanPixel = GameView.loadTextureRegion("pixels/pixel_cyan.png", false);
    private FrameBuffer frameBuffer;
    private GameController gameController;
    TextureRegion greenPixel = GameView.loadTextureRegion("pixels/pixel_green.png", false);
    MenuControllerYio menuControllerYio;
    TextureRegion pixelColor1 = GameView.loadTextureRegion("pixels/pixel_color1.png", false);
    TextureRegion pixelColor2 = GameView.loadTextureRegion("pixels/pixel_color2.png", false);
    TextureRegion pixelColor3 = GameView.loadTextureRegion("pixels/pixel_color3.png", false);
    TextureRegion redPixel = GameView.loadTextureRegion("pixels/pixel_red.png", false);
    TextureRegion yellowPixel = GameView.loadTextureRegion("pixels/pixel_yellow.png", false);

    public ColorStatsRenderer(MenuControllerYio menuControllerYio) {
        this.menuControllerYio = menuControllerYio;
        this.gameController = menuControllerYio.yioGdxGame.gameController;
    }

    TextureRegion getPixelByIndex(int colorIndex) {
        switch (this.gameController.getColorIndexWithOffset(colorIndex)) {
            case 1:
                return this.redPixel;
            case 2:
                return this.bluePixel;
            case 3:
                return this.cyanPixel;
            case 4:
                return this.yellowPixel;
            case 5:
                return this.pixelColor1;
            case 6:
                return this.pixelColor2;
            case 7:
                return this.pixelColor3;
            default:
                return this.greenPixel;
        }
    }

    public static void setFontColorByIndex(BitmapFont font, int colorIndex) {
        switch (colorIndex) {
            case 0:
                font.setColor(0.37f, 0.7f, 0.36f, 1.0f);
                return;
            case 1:
                font.setColor(0.7f, 0.36f, 0.46f, 1.0f);
                return;
            case 2:
                font.setColor(0.45f, 0.36f, 0.7f, 1.0f);
                return;
            case 3:
                font.setColor(0.36f, 0.7f, 0.69f, 1.0f);
                return;
            case 4:
                font.setColor(0.7f, 0.71f, 0.39f, 1.0f);
                return;
            case 5:
                font.setColor(0.68f, 0.22f, 0.0f, 1.0f);
                return;
            case 6:
                font.setColor(0.13f, 0.44f, 0.1f, 1.0f);
                return;
            case 7:
                font.setColor(0.4f, 0.4f, 0.4f, 1.0f);
                return;
            default:
                return;
        }
    }

    void renderStatButton(ButtonYio statButton, int[] playerHexCount) {
        beginRender(statButton, Fonts.gameFont);
        this.batch.begin();
        float w = (float) Gdx.graphics.getWidth();
        float h = (float) Gdx.graphics.getHeight();
        float columnWidth = 0.1f * w;
        float distanceBetweenColumns = (w - (2.0f * columnWidth)) / ((float) (playerHexCount.length - 1));
        float maxNumber = (float) GameController.maxNumberFromArray(playerHexCount);
        float columnHeight = 0.25f * h;
        for (int i = 0; i < playerHexCount.length; i++) {
            setFontColorByIndex(Fonts.buttonFont, this.gameController.getColorIndexWithOffset(i));
            float numberLineWidth = YioGdxGame.getTextWidth(Fonts.buttonFont, "" + playerHexCount[i]);
            float columnX = columnWidth + (((float) i) * distanceBetweenColumns);
            this.batch.draw(this.blackPixel, (columnX - (numberLineWidth / 2.0f)) - (0.01f * w), 0.28f * h, (0.02f * w) + numberLineWidth, 0.05f * h);
            Fonts.buttonFont.draw(this.batch, "" + playerHexCount[i], columnX - (numberLineWidth / 2.0f), 0.29f * h);
            float currentSize = (((float) playerHexCount[i]) / maxNumber) * columnHeight;
            this.batch.draw(getPixelByIndex(i), columnX - (columnWidth / 2.0f), ((0.01f * h) + columnHeight) - currentSize, columnWidth, currentSize);
        }
        this.batch.draw(this.blackPixel, 0.025f * w, (0.0125f * h) + columnHeight, 0.95f * w, 0.005f * h);
        Fonts.buttonFont.setColor(0.0f, 0.0f, 0.0f, 1.0f);
        this.batch.end();
        endRender(statButton);
    }

    private void beginRender(ButtonYio buttonYio, BitmapFont font) {
        if (this.frameBuffer != null) {
            this.frameBuffer.dispose();
        }
        this.frameBuffer = FrameBufferYio.getInstance(Format.RGB565, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / 2, false);
        this.frameBuffer.begin();
        Gdx.gl.glClearColor(buttonYio.backColor.f39r, buttonYio.backColor.f38g, buttonYio.backColor.f37b, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Matrix4 matrix4 = new Matrix4();
        int orthoWidth = Gdx.graphics.getWidth();
        int orthoHeight = Gdx.graphics.getHeight() / 2;
        matrix4.setToOrtho2D(0.0f, 0.0f, (float) orthoWidth, (float) orthoHeight);
        this.batch.setProjectionMatrix(matrix4);
        this.batch.begin();
        this.batch.draw(this.buttonBackground, 0.0f, 0.0f, (float) orthoWidth, (float) orthoHeight);
        this.batch.end();
    }

    void endRender(ButtonYio buttonYio) {
        Texture texture = (Texture) this.frameBuffer.getColorBufferTexture();
        texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        float f = ((FrameBufferYio) this.frameBuffer).f150f;
        buttonYio.textureRegion = new TextureRegion(texture, (int) (buttonYio.position.width * ((double) f)), (int) (buttonYio.position.height * ((double) f)));
        this.frameBuffer.end();
        this.frameBuffer.dispose();
    }
}
