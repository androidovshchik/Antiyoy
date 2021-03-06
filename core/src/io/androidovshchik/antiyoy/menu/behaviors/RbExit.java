package io.androidovshchik.antiyoy.menu.behaviors;

import com.badlogic.gdx.Gdx;
import io.androidovshchik.antiyoy.menu.ButtonYio;

public class RbExit extends Reaction {
    public void perform(ButtonYio buttonYio) {
        getYioGdxGame(buttonYio).close();
        buttonYio.menuControllerYio.yioGdxGame.startedExitProcess = true;
        Gdx.app.exit();
    }
}
