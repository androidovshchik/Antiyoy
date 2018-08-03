package io.androidovshchik.antiyoy.menu.scenes.gameplay;

import io.androidovshchik.antiyoy.SoundControllerYio;
import io.androidovshchik.antiyoy.YioGdxGame;
import io.androidovshchik.antiyoy.gameplay.rules.GameRules;
import io.androidovshchik.antiyoy.menu.ButtonYio;
import io.androidovshchik.antiyoy.menu.MenuControllerYio;
import io.androidovshchik.antiyoy.menu.behaviors.Reaction;
import io.androidovshchik.antiyoy.menu.scenes.AbstractScene;
import io.androidovshchik.antiyoy.menu.scenes.Scenes;
import io.androidovshchik.antiyoy.menu.scenes.editor.SceneEditorInstruments;
import io.androidovshchik.antiyoy.stuff.GraphicsYio;

public class SceneGameOverlay extends AbstractScene {
    public SceneGameOverlay(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
    }

    public void create() {
        if (GameRules.inEditorMode) {
            Scenes.sceneEditorInstruments.create();
        } else if (GameRules.aiOnlyMode) {
            Scenes.sceneReplayOverlay.create();
        } else {
            this.menuControllerYio.beginMenuCreation();
            ButtonYio inGameMenuButton = this.buttonFactory.getButton(generateSquare(1.0d - (SceneEditorInstruments.ICON_SIZE / ((double) YioGdxGame.screenRatio)), 0.93d, SceneEditorInstruments.ICON_SIZE), 30, null);
            this.menuControllerYio.loadButtonOnce(inGameMenuButton, "menu_icon.png");
            inGameMenuButton.setReaction(Reaction.rbPauseMenu);
            inGameMenuButton.setAnimation(1);
            inGameMenuButton.enableRectangularMask();
            inGameMenuButton.disableTouchAnimation();
            ButtonYio endTurnButton = this.buttonFactory.getButton(generateSquare(1.0d - (SceneEditorInstruments.ICON_SIZE / ((double) YioGdxGame.screenRatio)), 0.0d, SceneEditorInstruments.ICON_SIZE), 31, null);
            this.menuControllerYio.loadButtonOnce(endTurnButton, "end_turn.png");
            endTurnButton.setReaction(Reaction.rbEndTurn);
            endTurnButton.setAnimation(2);
            endTurnButton.enableRectangularMask();
            endTurnButton.disableTouchAnimation();
            endTurnButton.setPressSound(SoundControllerYio.soundEndTurn);
            ButtonYio undoButton = this.buttonFactory.getButton(generateSquare(0.0d, 0.0d, SceneEditorInstruments.ICON_SIZE), 32, null);
            this.menuControllerYio.loadButtonOnce(undoButton, "undo.png");
            undoButton.setReaction(Reaction.rbUndo);
            undoButton.setAnimation(2);
            undoButton.enableRectangularMask();
            undoButton.setTouchOffset(0.08f * GraphicsYio.width);
            undoButton.disableTouchAnimation();
            this.menuControllerYio.endMenuCreation();
        }
    }
}
