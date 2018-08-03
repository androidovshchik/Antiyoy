package io.androidovshchik.antiyoy.gameplay;

import java.util.ArrayList;
import java.util.Iterator;

public class SnapshotManager {
    public static final int FREE_SNAPSHOTS_LIMIT = 30;
    public static final int MAX_SNAPSHOTS = 25;
    private ArrayList<LevelSnapshot> freeSnapshots = new ArrayList();
    GameController gameController;
    private ArrayList<LevelSnapshot> levelSnapshots = new ArrayList();

    public SnapshotManager(GameController gameController) {
        this.gameController = gameController;
    }

    public void clear() {
        this.levelSnapshots.clear();
        this.freeSnapshots.clear();
    }

    public void onTurnStart() {
        Iterator it;
        if (this.freeSnapshots.size() < 30) {
            it = this.levelSnapshots.iterator();
            while (it.hasNext()) {
                addFreeSnapshot((LevelSnapshot) it.next());
            }
        }
        it = this.freeSnapshots.iterator();
        while (it.hasNext()) {
            ((LevelSnapshot) it.next()).reset();
        }
        this.levelSnapshots.clear();
    }

    private void addFreeSnapshot(LevelSnapshot levelSnapshot) {
        if (!this.freeSnapshots.contains(levelSnapshot) && this.freeSnapshots.size() < 30) {
            levelSnapshot.reset();
            this.freeSnapshots.add(levelSnapshot);
        }
    }

    public void takeSnapshot() {
        if (this.gameController.isPlayerTurn()) {
            LevelSnapshot snapshot = getNextSnapshot();
            snapshot.take();
            this.levelSnapshots.add(snapshot);
        }
    }

    private LevelSnapshot getNextSnapshot() {
        if (this.levelSnapshots.size() < 25) {
            return getFreeSnapshot();
        }
        LevelSnapshot levelSnapshot = (LevelSnapshot) this.levelSnapshots.get(0);
        this.levelSnapshots.remove(0);
        levelSnapshot.reset();
        return levelSnapshot;
    }

    private LevelSnapshot getFreeSnapshot() {
        Iterator it = this.freeSnapshots.iterator();
        while (it.hasNext()) {
            LevelSnapshot levelSnapshot = (LevelSnapshot) it.next();
            if (!levelSnapshot.used) {
                levelSnapshot.reset();
                return levelSnapshot;
            }
        }
        return new LevelSnapshot(this.gameController);
    }

    public boolean undoAction() {
        int lastIndex = this.levelSnapshots.size() - 1;
        if (lastIndex < 0) {
            return false;
        }
        LevelSnapshot lastSnapshot = (LevelSnapshot) this.levelSnapshots.get(lastIndex);
        lastSnapshot.recreate();
        this.levelSnapshots.remove(lastSnapshot);
        addFreeSnapshot(lastSnapshot);
        return true;
    }

    public void showInConsole() {
        System.out.println();
        System.out.println("SnapshotManager.showInConsole:");
        System.out.println("used: " + this.levelSnapshots.size());
        System.out.println("free: " + this.freeSnapshots.size());
    }
}
