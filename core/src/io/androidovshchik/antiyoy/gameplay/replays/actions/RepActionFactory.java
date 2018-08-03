package io.androidovshchik.antiyoy.gameplay.replays.actions;

public class RepActionFactory {
    public RepAction createAction(int actionType) {
        switch (actionType) {
            case 0:
                return new RaUnitBuilt(null, null, -1);
            case 1:
                return new RaUnitMoved(null, null);
            case 2:
                return new RaTowerBuilt(null, false);
            case 3:
                return new RaFarmBuilt(null);
            case 4:
                return new RaPalmSpawned(null);
            case 5:
                return new RaPineSpawned(null);
            case 6:
                return new RaTurnEnded();
            case 7:
                return new RaCitySpawned(null);
            case 8:
                return new RaUnitDiedFromStarvation(null);
            default:
                return null;
        }
    }
}
