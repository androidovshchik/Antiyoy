package io.androidovshchik.antiyoy.stuff;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class GraphicsYio {
    public static final float borderThickness = (0.003f * ((float) Gdx.graphics.getHeight()));
    private static GlyphLayout glyphLayout = new GlyphLayout();
    public static float height = ((float) Gdx.graphics.getHeight());
    public static float screenRatio = (height / width);
    public static float width = ((float) Gdx.graphics.getWidth());

    public static TextureRegion loadTextureRegion(String name, boolean antialias) {
        Texture texture = new Texture(Gdx.files.internal(name));
        if (antialias) {
            texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        }
        return new TextureRegion(texture);
    }

    public static float getTextWidth(BitmapFont font, String text) {
        glyphLayout.setText(font, text);
        return glyphLayout.width;
    }

    public static float getTextHeight(BitmapFont font, String text) {
        glyphLayout.setText(font, text);
        return glyphLayout.height;
    }

    public static void drawFromCenter(SpriteBatch batch, TextureRegion textureRegion, double cx, double cy, double r) {
        batch.draw(textureRegion, (float) (cx - r), (float) (cy - r), (float) (2.0d * r), (float) (2.0d * r));
    }

    public static void drawFromCenterRotated(Batch batch, TextureRegion textureRegion, double cx, double cy, double r, double rotationAngle) {
        batch.draw(textureRegion, (float) (cx - r), (float) (cy - r), (float) r, (float) r, (float) (2.0d * r), (float) (2.0d * r), 1.0f, 1.0f, 57.29f * ((float) rotationAngle));
    }

    public static void renderBorder(RectangleYio viewPos, SpriteBatch batch, TextureRegion pixel) {
        renderBorder(viewPos, borderThickness, batch, pixel);
    }

    public static void renderBorder(RectangleYio viewPos, float thickness, SpriteBatch batch, TextureRegion pixel) {
        drawLine(viewPos.f146x, viewPos.f147y, viewPos.f146x, viewPos.f147y + viewPos.height, (double) thickness, batch, pixel);
        drawLine(viewPos.f146x, viewPos.f147y, viewPos.f146x + viewPos.width, viewPos.f147y, (double) thickness, batch, pixel);
        drawLine(viewPos.f146x, viewPos.f147y + viewPos.height, viewPos.f146x + viewPos.width, viewPos.f147y + viewPos.height, (double) thickness, batch, pixel);
        drawLine(viewPos.f146x + viewPos.width, viewPos.f147y, viewPos.f146x + viewPos.width, viewPos.f147y + viewPos.height, (double) thickness, batch, pixel);
    }

    public static void drawByRectangle(Batch batch, TextureRegion textureRegion, RectangleYio rectangleYio) {
        batch.draw(textureRegion, (float) rectangleYio.f146x, (float) rectangleYio.f147y, (float) rectangleYio.width, (float) rectangleYio.height);
    }

    public static void drawLine(PointYio p1, PointYio p2, double thickness, SpriteBatch spriteBatch, TextureRegion texture) {
        drawLine((double) p1.f144x, (double) p1.f145y, (double) p2.f144x, (double) p2.f145y, thickness, spriteBatch, texture);
    }

    public static double convertToHeight(double width) {
        return width / ((double) screenRatio);
    }

    public static double convertToWidth(double height) {
        return ((double) screenRatio) * height;
    }

    public static void setBatchAlpha(SpriteBatch spriteBatch, double alpha) {
        Color color = spriteBatch.getColor();
        spriteBatch.setColor(color.f39r, color.f38g, color.f37b, (float) alpha);
    }

    public static void setFontAlpha(BitmapFont font, double alpha) {
        Color color = font.getColor();
        font.setColor(color.f39r, color.f38g, color.f37b, (float) alpha);
    }

    public static void drawLine(double x1, double y1, double x2, double y2, double thickness, SpriteBatch spriteBatch, TextureRegion texture) {
        spriteBatch.draw(texture, (float) x1, (float) (y1 - (0.5d * thickness)), 0.0f, ((float) thickness) * 0.5f, (float) Yio.distance(x1, y1, x2, y2), (float) thickness, 1.0f, 1.0f, (float) (57.29577951308232d * Yio.angle(x1, y1, x2, y2)));
    }
}
