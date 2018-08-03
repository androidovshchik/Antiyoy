package io.androidovshchik.antiyoy.stuff;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Masking {
    public static final void begin() {
        Gdx.gl.glClearDepthf(1.0f);
        Gdx.gl.glClear(256);
        Gdx.gl.glDepthFunc(GL20.GL_LESS);
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glDepthMask(true);
        Gdx.gl.glColorMask(false, false, false, false);
    }

    public static final void continueAfterBatchBegin() {
        Gdx.gl.glColorMask(true, true, true, true);
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glDepthFunc(GL20.GL_EQUAL);
    }

    public static final void end(SpriteBatch batch) {
        batch.end();
        batch.begin();
        Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
    }
}
