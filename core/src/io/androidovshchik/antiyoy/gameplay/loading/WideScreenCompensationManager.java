package io.androidovshchik.antiyoy.gameplay.loading;

import java.util.Iterator;
import yio.tro.antiyoy.gameplay.FieldController;
import yio.tro.antiyoy.gameplay.GameController;
import yio.tro.antiyoy.gameplay.Hex;
import yio.tro.antiyoy.stuff.PointYio;

public class WideScreenCompensationManager {
    private float bottom;
    private float deltaBottom;
    private float deltaTop;
    private FieldController fieldController;
    private float fixDelta;
    GameController gameController;
    private float top;

    public void perform() {
        this.fieldController = this.gameController.fieldController;
        if (this.fieldController.activeHexes.size() != 0) {
            updateTopAndBottom();
            updateDeltas();
            if (isSomethingWrong()) {
                doFix();
            }
        }
    }

    private void doFix() {
        updateFixDelta();
        applyFixDelta();
    }

    private void applyFixDelta() {
        System.out.println("applied widescreen fix");
        this.gameController.fieldController.compensatoryOffset = this.fixDelta;
        this.gameController.fieldController.updateHexPositions();
    }

    private void updateFixDelta() {
        this.fixDelta = (this.gameController.boundHeight / 2.0f) - ((this.top + this.bottom) / 2.0f);
    }

    private boolean isSomethingWrong() {
        if (this.deltaTop >= 0.0f && this.deltaBottom >= 0.0f) {
            return false;
        }
        return true;
    }

    private void updateDeltas() {
        this.deltaTop = (this.gameController.boundHeight - (this.fieldController.hexSize * 2.0f)) - this.top;
        this.deltaBottom = this.bottom - (this.fieldController.hexSize * 2.0f);
    }

    private void updateTopAndBottom() {
        this.top = 0.0f;
        this.bottom = 0.0f;
        boolean gotTop = false;
        boolean gotBottom = false;
        Iterator it = this.fieldController.activeHexes.iterator();
        while (it.hasNext()) {
            PointYio pos = ((Hex) it.next()).getPos();
            if (!gotTop || pos.f145y > this.top) {
                this.top = pos.f145y;
                gotTop = true;
            }
            if (!gotBottom || pos.f145y < this.bottom) {
                this.bottom = pos.f145y;
                gotBottom = true;
            }
        }
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }
}
