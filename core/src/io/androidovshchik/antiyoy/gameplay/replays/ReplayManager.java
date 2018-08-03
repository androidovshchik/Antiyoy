package io.androidovshchik.antiyoy.gameplay.replays;

import io.androidovshchik.antiyoy.Settings;
import io.androidovshchik.antiyoy.gameplay.FieldController;
import io.androidovshchik.antiyoy.gameplay.GameController;
import io.androidovshchik.antiyoy.gameplay.Hex;
import io.androidovshchik.antiyoy.gameplay.Province;
import io.androidovshchik.antiyoy.gameplay.campaign.CampaignProgressManager;
import io.androidovshchik.antiyoy.gameplay.loading.LoadingManager;
import io.androidovshchik.antiyoy.gameplay.loading.LoadingParameters;
import io.androidovshchik.antiyoy.gameplay.replays.actions.RaCitySpawned;
import io.androidovshchik.antiyoy.gameplay.replays.actions.RaFarmBuilt;
import io.androidovshchik.antiyoy.gameplay.replays.actions.RaPalmSpawned;
import io.androidovshchik.antiyoy.gameplay.replays.actions.RaPineSpawned;
import io.androidovshchik.antiyoy.gameplay.replays.actions.RaTowerBuilt;
import io.androidovshchik.antiyoy.gameplay.replays.actions.RaTurnEnded;
import io.androidovshchik.antiyoy.gameplay.replays.actions.RaUnitBuilt;
import io.androidovshchik.antiyoy.gameplay.replays.actions.RaUnitDiedFromStarvation;
import io.androidovshchik.antiyoy.gameplay.replays.actions.RaUnitMoved;
import io.androidovshchik.antiyoy.gameplay.rules.GameRules;
import io.androidovshchik.antiyoy.menu.scenes.Scenes;

public class ReplayManager {
    GameController gameController;
    Replay replay = null;

    public ReplayManager(GameController gameController) {
        this.gameController = gameController;
    }

    public void defaultValues() {
        this.replay = new Replay(this.gameController);
    }

    public void onEndCreation() {
        this.replay.updateInitialLevelString();
        this.replay.prepare();
        if (!GameRules.replayMode) {
            this.replay.actions.clear();
            this.replay.buffer.clear();
        }
    }

    public void onPineSpawned(Hex hex) {
        if (canAddAction()) {
            this.replay.addAction(new RaPineSpawned(hex));
        }
    }

    public void onLoadingFromSlotFinished(FieldController fieldController) {
        if (this.replay != null) {
            this.replay.updateActionsFromString(fieldController);
        }
    }

    public void performStep() {
        if (this.replay != null) {
            this.replay.performStep();
        }
    }

    private boolean canAddAction() {
        if (this.replay == null || GameRules.replayMode || !Settings.replaysEnabled) {
            return false;
        }
        return true;
    }

    public void onPalmSpawned(Hex hex) {
        if (canAddAction()) {
            this.replay.addAction(new RaPalmSpawned(hex));
        }
    }

    public void onUnitBuilt(Province src, Hex dst, int strength) {
        if (canAddAction()) {
            this.replay.addAction(new RaUnitBuilt(src.getCapital(), dst, strength));
        }
    }

    public void onUnitMoved(Hex src, Hex dst) {
        if (canAddAction()) {
            this.replay.addAction(new RaUnitMoved(src, dst));
        }
    }

    public void onTowerBuilt(Hex hex, boolean strong) {
        if (canAddAction()) {
            this.replay.addAction(new RaTowerBuilt(hex, strong));
        }
    }

    public void onFarmBuilt(Hex hex) {
        if (canAddAction()) {
            this.replay.addAction(new RaFarmBuilt(hex));
        }
    }

    public void onStopButtonPressed() {
        this.replay.recreateInitialSituation();
        this.replay.prepare();
        this.gameController.onInitialSnapshotRecreated();
    }

    public void startInstantReplay() {
        Replay copyReplay = new Replay(this.gameController);
        this.replay.saveToPreferences("temp");
        copyReplay.loadFromPreferences("temp");
        LoadingParameters loadingParameters = new LoadingParameters();
        loadingParameters.mode = 8;
        loadingParameters.applyFullLevel(copyReplay.initialLevelString);
        loadingParameters.replay = copyReplay;
        loadingParameters.playersNumber = 0;
        loadingParameters.colorOffset = this.gameController.colorIndexViewOffset;
        loadingParameters.slayRules = GameRules.slayRules;
        if (GameRules.campaignMode) {
            loadingParameters.campaignLevelIndex = CampaignProgressManager.getInstance().currentLevelIndex;
        } else {
            loadingParameters.campaignLevelIndex = -1;
        }
        LoadingManager.getInstance().startGame(loadingParameters);
        Scenes.sceneReplayOverlay.speedPanel.showSaveIcon();
    }

    public void saveCurrentReplay() {
        ReplaySaveSystem.getInstance().saveReplay(this.replay);
    }

    public void onTurnEnded() {
        if (canAddAction()) {
            this.replay.addAction(new RaTurnEnded());
        }
    }

    public void onCitySpawned(Hex hex) {
        if (canAddAction()) {
            this.replay.addAction(new RaCitySpawned(hex));
        }
    }

    public void onUnitDiedFromStarvation(Hex hex) {
        if (canAddAction()) {
            this.replay.addAction(new RaUnitDiedFromStarvation(hex));
        }
    }

    public void setReplay(Replay replay) {
        this.replay = replay;
    }

    public Replay getReplay() {
        return this.replay;
    }

    public void showInConsole() {
        if (this.replay != null) {
            this.replay.showInConsole();
        } else {
            System.out.println("replay is null");
        }
    }
}
