package io.androidovshchik.antiyoy.menu;

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
import com.badlogic.gdx.utils.StringBuilder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;
import yio.tro.antiyoy.YioGdxGame;
import yio.tro.antiyoy.gameplay.game_view.GameView;
import yio.tro.antiyoy.stuff.Fonts;
import yio.tro.antiyoy.stuff.FrameBufferYio;
import yio.tro.antiyoy.stuff.GraphicsYio;
import yio.tro.antiyoy.stuff.RectangleYio;

public class ButtonRenderer {
    private final SpriteBatch batch = new SpriteBatch();
    private TextureRegion bigButtonBackground;
    private TextureRegion buttonBackground1 = GameView.loadTextureRegion("button_background_1.png", true);
    private TextureRegion buttonBackground2;
    private TextureRegion buttonBackground3;
    private FrameBuffer frameBuffer;
    private int horizontalOffset;
    private RectangleYio pos;
    private ArrayList<String> text;

    public ButtonRenderer() {
        this.buttonBackground1.flip(false, true);
        this.buttonBackground2 = GameView.loadTextureRegion("button_background_2.png", true);
        this.buttonBackground2.flip(false, true);
        this.buttonBackground3 = GameView.loadTextureRegion("button_background_3.png", true);
        this.buttonBackground3.flip(false, true);
        this.bigButtonBackground = GameView.loadTextureRegion("big_button_background.png", true);
        this.bigButtonBackground.flip(false, true);
    }

    TextureRegion getButtonBackground(ButtonYio buttonYio) {
        switch (buttonYio.id % 3) {
            case 0:
                return this.buttonBackground1;
            case 1:
                return this.buttonBackground2;
            case 2:
                return this.buttonBackground3;
            default:
                return this.buttonBackground1;
        }
    }

    private void beginRender(ButtonYio buttonYio, BitmapFont font, int FONT_SIZE) {
        this.horizontalOffset = (int) (0.3f * ((float) FONT_SIZE));
        this.frameBuffer = FrameBufferYio.getInstance(Format.RGB565, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        this.frameBuffer.begin();
        Gdx.gl.glClearColor(buttonYio.backColor.f39r, buttonYio.backColor.f38g, buttonYio.backColor.f37b, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Matrix4 matrix4 = new Matrix4();
        int orthoWidth = getExpectedOrthoWidth(buttonYio, font);
        int orthoHeight = (orthoWidth / Gdx.graphics.getWidth()) * Gdx.graphics.getHeight();
        matrix4.setToOrtho2D(0.0f, 0.0f, (float) orthoWidth, (float) orthoHeight);
        this.batch.setProjectionMatrix(matrix4);
        this.batch.begin();
        drawButtonBackground(buttonYio, orthoWidth, orthoHeight);
        this.batch.end();
        this.pos = new RectangleYio(buttonYio.position);
        initText(buttonYio, font);
    }

    private void drawButtonBackground(ButtonYio buttonYio, int orthoWidth, int orthoHeight) {
        if (buttonYio.hasCustomBackground()) {
            this.batch.draw(buttonYio.getCustomBackgroundForText(), 0.0f, 0.0f, (float) Gdx.graphics.getWidth(), (float) Gdx.graphics.getHeight());
        } else if (buttonYio.position.height < 0.12d * ((double) Gdx.graphics.getHeight())) {
            this.batch.draw(getButtonBackground(buttonYio), 0.0f, 0.0f, (float) Gdx.graphics.getWidth(), (float) Gdx.graphics.getHeight());
        } else {
            this.batch.draw(this.bigButtonBackground, 0.0f, 0.0f, (float) orthoWidth, (float) orthoHeight);
        }
    }

    private void initText(ButtonYio buttonYio, BitmapFont font) {
        this.text = new ArrayList();
        if (buttonYio.text.size() == 1) {
            this.text = buttonYio.text;
            return;
        }
        StringBuilder builder = new StringBuilder();
        Iterator it = buttonYio.text.iterator();
        while (it.hasNext()) {
            double currentX = (double) this.horizontalOffset;
            Iterator it2 = convertSourceLineToTokens((String) it.next()).iterator();
            while (it2.hasNext()) {
                String token = (String) it2.next();
                double currentWidth = (double) GraphicsYio.getTextWidth(font, token);
                if (currentX + currentWidth > ((double) Gdx.graphics.getWidth())) {
                    this.text.add(builder.toString());
                    builder = new StringBuilder();
                    currentX = 0.0d;
                }
                builder.append(token);
                currentX += currentWidth;
            }
            this.text.add(builder.toString());
            builder = new StringBuilder();
        }
        while (this.text.size() > buttonYio.text.size()) {
            if (((String) this.text.get(this.text.size() - 1)).length() <= 2) {
                this.text.remove(this.text.size() - 1);
            } else {
                return;
            }
        }
    }

    private ArrayList<String> convertSourceLineToTokens(String line) {
        ArrayList<String> tokens = new ArrayList();
        StringTokenizer tokenizer = new StringTokenizer(line, " ");
        while (tokenizer.hasMoreTokens()) {
            tokens.add(tokenizer.nextToken() + " ");
        }
        return tokens;
    }

    void endRender(ButtonYio buttonYio) {
        Texture texture = (Texture) this.frameBuffer.getColorBufferTexture();
        texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        float f = ((FrameBufferYio) this.frameBuffer).f150f;
        buttonYio.textureRegion = new TextureRegion(texture, (int) (this.pos.width * ((double) f)), (int) (this.pos.height * ((double) f)));
        this.frameBuffer.end();
        this.frameBuffer.dispose();
    }

    private int getExpectedOrthoWidth(ButtonYio buttonYio, BitmapFont font) {
        return Gdx.graphics.getWidth();
    }

    private int getTextWidth(String text, BitmapFont font) {
        return (int) YioGdxGame.getTextWidth(font, text);
    }

    public void renderButton(ButtonYio buttonYio, BitmapFont font, int FONT_SIZE) {
        beginRender(buttonYio, font, FONT_SIZE);
        float ratio = (float) (this.pos.width / this.pos.height);
        int lineHeight = (int) (1.2f * ((float) FONT_SIZE));
        if (this.text.size() == 1) {
            this.horizontalOffset = (int) (0.5d * ((double) (((1.35f * ((float) FONT_SIZE)) * ratio) - ((float) getTextWidth((String) this.text.get(0), font)))));
            if (this.horizontalOffset < 0) {
                this.horizontalOffset = (int) (0.3f * ((float) FONT_SIZE));
            }
        }
        if (buttonYio.getTextOffset() != 0.0f) {
            this.horizontalOffset = (int) buttonYio.getTextOffset();
        }
        int verticalOffset = (int) (0.3f * ((float) FONT_SIZE));
        int lineNumber = 0;
        float longestLineLength = 0.0f;
        this.batch.begin();
        font.setColor(0.0f, 0.0f, 0.0f, 1.0f);
        Iterator it = this.text.iterator();
        while (it.hasNext()) {
            CharSequence line = (String) it.next();
            font.draw(this.batch, line, (float) this.horizontalOffset, (float) ((lineNumber * lineHeight) + verticalOffset));
            float currentLineLength = (float) getTextWidth(line, font);
            if (currentLineLength > longestLineLength) {
                longestLineLength = currentLineLength;
            }
            lineNumber++;
        }
        this.batch.end();
        this.pos.height = (double) ((this.text.size() * lineHeight) + (verticalOffset / 2));
        this.pos.width = this.pos.height * ((double) ratio);
        if (((double) longestLineLength) > this.pos.width - ((double) (0.3f * ((float) lineHeight)))) {
            this.pos.width = (double) (((float) (this.horizontalOffset * 2)) + longestLineLength);
        }
        endRender(buttonYio);
    }

    public void renderButton(ButtonYio buttonYio) {
        renderButton(buttonYio, Fonts.buttonFont, Fonts.FONT_SIZE);
    }
}
