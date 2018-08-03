package io.androidovshchik.antiyoy.stuff;

import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;

public class FrameBufferYio extends FrameBuffer {
    public float f150f;

    public static FrameBufferYio getInstance(Format format, int width, int height, boolean hasDepth) {
        try {
            return new FrameBufferYio(format, width, height, hasDepth, 1.0f);
        } catch (Exception e) {
            try {
                return new FrameBufferYio(Format.RGBA8888, width, height, hasDepth, 1.0f);
            } catch (Exception e2) {
                try {
                    return new FrameBufferYio(Format.RGB565, width, height, true, 1.0f);
                } catch (Exception e3) {
                    return new FrameBufferYio(format, width / 2, height / 2, hasDepth, 0.5f);
                }
            }
        }
    }

    public FrameBufferYio(Format format, int width, int height, boolean hasDepth, float f) {
        super(format, width, height, hasDepth);
        this.f150f = f;
    }

    public FrameBufferYio(Format format, int width, int height, boolean hasDepth, boolean hasStencil) {
        super(format, width, height, hasDepth, hasStencil);
    }

    protected void disposeColorTexture(Texture colorTexture) {
    }
}
