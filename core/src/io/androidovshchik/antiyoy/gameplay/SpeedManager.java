package io.androidovshchik.antiyoy.gameplay;

import yio.tro.antiyoy.gameplay.replays.Replay;

public class SpeedManager {
    public static final int SPEED_FAST_FORWARD = 2;
    public static final int SPEED_NORMAL = 1;
    public static final int SPEED_PAUSED = 0;
    GameController gameController;
    int speed;

    public SpeedManager(GameController gameController) {
        this.gameController = gameController;
    }

    public void stop() {
        this.gameController.replayManager.onStopButtonPressed();
        setSpeed(0);
    }

    public void setSpeed(int speed) {
        if (this.speed != speed) {
            onSpeedChange(this.speed, speed);
        }
        this.speed = speed;
    }

    private void onSpeedChange(int last, int next) {
        Replay replay = this.gameController.replayManager.getReplay();
        if (replay != null) {
            if (next == 0) {
                replay.onTacticalPause();
            } else {
                replay.onResumeNormalSpeed();
            }
        }
    }

    public void defaultValues() {
        resetSpeed();
    }

    private void applyTacticalPause() {
        setSpeed(0);
    }

    private void resetSpeed() {
        setSpeed(2);
    }

    public void onPlayPauseButtonPressed() {
        if (this.speed == 0) {
            setSpeed(1);
        } else {
            applyTacticalPause();
        }
    }

    public void onFastForwardButtonPressed() {
        if (this.speed == 2) {
            setSpeed(1);
        } else {
            setSpeed(2);
        }
    }

    public int getSpeed() {
        return this.speed;
    }
}
