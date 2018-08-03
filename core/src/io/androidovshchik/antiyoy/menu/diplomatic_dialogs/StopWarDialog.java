package io.androidovshchik.antiyoy.menu.diplomatic_dialogs;

import io.androidovshchik.antiyoy.gameplay.diplomacy.DiplomacyManager;
import io.androidovshchik.antiyoy.gameplay.diplomacy.DiplomaticEntity;
import io.androidovshchik.antiyoy.menu.MenuControllerYio;
import io.androidovshchik.antiyoy.stuff.Fonts;
import io.androidovshchik.antiyoy.stuff.LanguagesManager;

public class StopWarDialog extends AbstractDiplomaticDialog {
    DiplomaticEntity recipient;
    DiplomaticEntity sender;

    public StopWarDialog(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
        resetEntities();
    }

    protected void resetEntities() {
        this.sender = null;
        this.recipient = null;
    }

    protected void onAppear() {
        super.onAppear();
        resetEntities();
    }

    protected void makeLabels() {
        if (this.sender != null && this.recipient != null) {
            LanguagesManager instance = LanguagesManager.getInstance();
            float y = (float) (this.position.height - ((double) this.topOffset));
            addLabel(instance.getString("peace_treaty"), Fonts.gameFont, this.leftOffset, y);
            y -= this.titleOffset;
            String capitalName = this.sender.capitalName;
            if (this.sender.isMain()) {
                capitalName = this.recipient.capitalName;
            }
            addLabel(instance.getString("state") + ": " + capitalName, Fonts.smallerMenuFont, this.leftOffset, y);
            y -= this.lineOffset;
            addLabel(instance.getString("pay") + " (1x) : " + getPay(), Fonts.smallerMenuFont, this.leftOffset, y);
            y -= this.lineOffset;
            addLabel(instance.getString("reparations") + ": " + getReparations(), Fonts.smallerMenuFont, this.leftOffset, y);
            y -= this.lineOffset;
            addLabel(instance.getString("duration") + ": " + 9, Fonts.smallerMenuFont, this.leftOffset, y);
            y -= this.lineOffset;
        }
    }

    private String getPay() {
        int pay = getDiplomacyManager().calculatePayToStopWar(this.sender, this.recipient);
        if (pay == 0) {
            return "0";
        }
        return "" + pay;
    }

    private String getReparations() {
        int reparations = getDiplomacyManager().calculateReparations(this.sender, this.recipient);
        if (reparations == 0) {
            return "0";
        }
        return "" + reparations;
    }

    protected void onYesButtonPressed() {
        if (this.sender.isMain()) {
            getDiplomacyManager().onUserRequestedToStopWar(this.sender, this.recipient);
        } else {
            getDiplomacyManager().onEntityRequestedToStopWar(this.sender, this.recipient);
        }
        destroy();
    }

    private DiplomacyManager getDiplomacyManager() {
        return this.menuControllerYio.yioGdxGame.gameController.fieldController.diplomacyManager;
    }

    protected void onNoButtonPressed() {
        destroy();
    }

    public boolean areButtonsEnabled() {
        return true;
    }

    public void setEntities(DiplomaticEntity sender, DiplomaticEntity recipient) {
        this.sender = sender;
        this.recipient = recipient;
        updateAll();
    }
}
