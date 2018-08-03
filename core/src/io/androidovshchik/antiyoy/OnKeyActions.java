package io.androidovshchik.antiyoy;

import com.badlogic.gdx.Input.Keys;
import java.util.Iterator;
import io.androidovshchik.antiyoy.menu.ButtonYio;
import io.androidovshchik.antiyoy.menu.fast_construction.FastConstructionPanel;
import io.androidovshchik.antiyoy.menu.scenes.Scenes;

public class OnKeyActions {
    private final YioGdxGame yioGdxGame;

    public OnKeyActions(YioGdxGame yioGdxGame) {
        this.yioGdxGame = yioGdxGame;
    }

    boolean onKeyPressed(int keycode) {
        if (keycode == 4 || keycode == Keys.ESCAPE) {
            onBackButtonPressed();
        }
        if (keycode == 66) {
            onEnterPressed();
        }
        if (keycode == 45 && !this.yioGdxGame.gamePaused) {
            this.yioGdxGame.pressButtonIfVisible(32);
            this.yioGdxGame.pressButtonIfVisible(53);
        }
        if (keycode == 62) {
            onSpaceButtonPressed();
        }
        if (keycode == 8) {
            onBuildUnitButtonPressed();
        }
        if (keycode == 9) {
            onBuildObjectButtonPressed();
        }
        if (keycode == 32) {
            onDebugButtonPressed();
        }
        if (keycode == 54) {
            this.yioGdxGame.gameController.cameraController.setTargetZoomLevel(0.9f);
        }
        if (keycode == 7) {
            onEditLevelButtonPressed();
        }
        if (keycode == 31) {
            openCheatScreen();
        }
        if (keycode == 47) {
            this.yioGdxGame.menuControllerYio.specialActionController.perform();
        }
        if (keycode == 40) {
            this.yioGdxGame.saveSystem.loadTopSlot();
        }
        checkFastConstructionPanel(keycode);
        return false;
    }

    private void checkFastConstructionPanel(int keycode) {
        FastConstructionPanel fastConstructionPanel = Scenes.sceneFastConstructionPanel.fastConstructionPanel;
        if (fastConstructionPanel != null) {
            fastConstructionPanel.onKeyPressed(keycode);
        }
    }

    private void onEnterPressed() {
        pressIfVisible(Scenes.sceneMainMenu.playButton);
        pressIfVisible(Scenes.sceneChoodeGameModeMenu.skirmishButton);
        pressIfVisible(Scenes.sceneSkirmishMenu.startButton);
        pressIfVisible(Scenes.scenePauseMenu.resumeButton);
    }

    private void pressIfVisible(ButtonYio buttonYio) {
        if (buttonYio != null && buttonYio.isVisible()) {
            buttonYio.press();
        }
    }

    private void openCheatScreen() {
        this.yioGdxGame.setGamePaused(true);
        Scenes.sceneSecretScreen.create();
    }

    private void onEditLevelButtonPressed() {
        this.yioGdxGame.gameController.getLevelEditor().launchEditCampaignLevelMode();
    }

    private void onDebugButtonPressed() {
        this.yioGdxGame.gameController.debugActions();
    }

    private void onBuildObjectButtonPressed() {
        if (!this.yioGdxGame.gamePaused) {
            this.yioGdxGame.pressButtonIfVisible(38);
        }
    }

    private void onBuildUnitButtonPressed() {
        if (!this.yioGdxGame.gamePaused) {
            this.yioGdxGame.pressButtonIfVisible(39);
        }
    }

    private void onSpaceButtonPressed() {
        if (!this.yioGdxGame.gamePaused) {
            this.yioGdxGame.pressButtonIfVisible(31);
            this.yioGdxGame.pressButtonIfVisible(53);
        }
    }

    private void onBackButtonPressed() {
        if (this.yioGdxGame != null && this.yioGdxGame.menuControllerYio != null) {
            if (this.yioGdxGame.gamePaused) {
                this.yioGdxGame.pressButtonIfVisible(42);
                this.yioGdxGame.pressButtonIfVisible(1);
                Iterator it = this.yioGdxGame.backButtonIds.iterator();
                while (it.hasNext()) {
                    this.yioGdxGame.pressButtonIfVisible(((Integer) it.next()).intValue());
                }
                return;
            }
            pressIfVisible(Scenes.sceneReplayOverlay.inGameMenuButton);
            ButtonYio pauseButton = this.yioGdxGame.menuControllerYio.getButtonById(30);
            if (pauseButton == null || !pauseButton.isVisible()) {
                ButtonYio buttonById = this.yioGdxGame.menuControllerYio.getButtonById(140);
                if (buttonById != null) {
                    buttonById.press();
                    return;
                }
                return;
            }
            pauseButton.press();
        }
    }
}
