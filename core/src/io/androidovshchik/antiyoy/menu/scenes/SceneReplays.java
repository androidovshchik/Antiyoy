package io.androidovshchik.antiyoy.menu.scenes;

import io.androidovshchik.antiyoy.menu.MenuControllerYio;
import io.androidovshchik.antiyoy.menu.behaviors.Reaction;
import io.androidovshchik.antiyoy.menu.replay_selector.ReplaySelector;
import io.androidovshchik.antiyoy.menu.scenes.editor.SceneEditorInstruments;
import io.androidovshchik.antiyoy.stuff.LongTapDetector;

public class SceneReplays extends AbstractScene {
    ReplaySelector replaySelector = null;

    public SceneReplays(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
    }

    public void create() {
        this.menuControllerYio.beginMenuCreation();
        changeBackground();
        createBackButton();
        createReplaySelector();
        this.menuControllerYio.endMenuCreation();
    }

    private void createReplaySelector() {
        if (this.replaySelector == null) {
            this.replaySelector = new ReplaySelector(this.menuControllerYio, -1);
            this.replaySelector.setPosition(generateRectangle(0.1d, SceneEditorInstruments.ICON_SIZE, 0.8d, 0.75d));
            this.menuControllerYio.addElementToScene(this.replaySelector);
        }
        this.replaySelector.appear();
    }

    private void createBackButton() {
        this.menuControllerYio.spawnBackButton(LongTapDetector.LONG_TAP_DELAY, Reaction.rbLoadGame);
    }

    private void changeBackground() {
        this.menuControllerYio.getYioGdxGame().beginBackgroundChange(2, false, true);
    }
}
