package io.androidovshchik.antiyoy.menu.diplomatic_dialogs;

import yio.tro.antiyoy.gameplay.diplomacy.DiplomacyManager;
import yio.tro.antiyoy.gameplay.diplomacy.DiplomaticContract;
import yio.tro.antiyoy.gameplay.diplomacy.DiplomaticEntity;
import yio.tro.antiyoy.menu.MenuControllerYio;
import yio.tro.antiyoy.stuff.Fonts;
import yio.tro.antiyoy.stuff.LanguagesManager;

public class DipInfoDialog extends AbstractDiplomaticDialog {
    DiplomaticEntity mainEntity = null;
    DiplomaticEntity selectedEntity = null;

    public DipInfoDialog(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
    }

    protected void makeLabels() {
        if (this.selectedEntity != null) {
            LanguagesManager instance = LanguagesManager.getInstance();
            float y = (float) (this.position.height - ((double) this.topOffset));
            addLabel(this.selectedEntity.capitalName, Fonts.gameFont, this.leftOffset, y);
            y -= this.titleOffset;
            addLabel(instance.getString("friends") + ": " + this.selectedEntity.getNumberOfFriends(), Fonts.smallerMenuFont, this.leftOffset, y);
            y -= this.lineOffset;
            addLabel(instance.getString("mutual_friends") + ": " + this.selectedEntity.getNumberOfMutualFriends(this.mainEntity), Fonts.smallerMenuFont, this.leftOffset, y);
            y -= this.lineOffset;
            if (this.mainEntity.getRelation(this.selectedEntity) == 1) {
                addLabel(instance.getString("dotations") + ": " + getDotationsString(), Fonts.smallerMenuFont, this.leftOffset, y);
                y -= this.lineOffset;
                addLabel(instance.getString("duration") + ": " + getDurationValue(), Fonts.smallerMenuFont, this.leftOffset, y);
                y -= this.lineOffset;
            }
            DiplomaticContract contract = getDiplomacyManager().findContract(1, this.mainEntity, this.selectedEntity);
            if (contract != null) {
                addLabel(instance.getString("reparations") + ": " + getReparations(contract), Fonts.smallerMenuFont, this.leftOffset, y);
                y -= this.lineOffset;
                addLabel(instance.getString("duration") + ": " + getDurationValue(), Fonts.smallerMenuFont, this.leftOffset, y);
                y -= this.lineOffset;
            }
        }
    }

    private String getReparations(DiplomaticContract contract) {
        return contract.getDotationsStringFromEntityPerspective(this.mainEntity);
    }

    private int getDurationValue() {
        DiplomaticContract contract = getDiplomacyManager().findContract(-1, this.mainEntity, this.selectedEntity);
        if (contract == null) {
            return 0;
        }
        return contract.getExpireCountDown();
    }

    private DiplomacyManager getDiplomacyManager() {
        return this.menuControllerYio.yioGdxGame.gameController.fieldController.diplomacyManager;
    }

    private String getDotationsString() {
        DiplomaticContract contract = getDiplomacyManager().findContract(0, this.mainEntity, this.selectedEntity);
        if (contract == null) {
            return "0";
        }
        return contract.getDotationsStringFromEntityPerspective(this.mainEntity);
    }

    public void setEntities(DiplomaticEntity mainEntity, DiplomaticEntity selectedEntity) {
        this.mainEntity = mainEntity;
        this.selectedEntity = selectedEntity;
        updateAll();
    }

    protected void onYesButtonPressed() {
    }

    protected void onNoButtonPressed() {
    }

    public boolean areButtonsEnabled() {
        return false;
    }
}
