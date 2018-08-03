package io.androidovshchik.antiyoy.menu.diplomatic_dialogs;

import io.androidovshchik.antiyoy.gameplay.diplomacy.DiplomacyManager;
import io.androidovshchik.antiyoy.gameplay.diplomacy.DiplomaticEntity;
import io.androidovshchik.antiyoy.menu.MenuControllerYio;
import io.androidovshchik.antiyoy.menu.scenes.Scenes;
import io.androidovshchik.antiyoy.stuff.Fonts;
import io.androidovshchik.antiyoy.stuff.GraphicsYio;
import io.androidovshchik.antiyoy.stuff.LanguagesManager;

public class ConfirmDislikeDialog extends AbstractDiplomaticDialog {
    DiplomaticEntity selectedEntity;

    public ConfirmDislikeDialog(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
    }

    protected void makeLabels() {
        String messageKey;
        LanguagesManager instance = LanguagesManager.getInstance();
        float y = (float) (this.position.height - ((double) this.topOffset));
        int relation = getDiplomacyManager().getMainEntity().getRelation(this.selectedEntity);
        switch (relation) {
            case 0:
                messageKey = "confirm_start_war";
                break;
            case 1:
                messageKey = "confirm_cancel_friendship";
                break;
            case 2:
                messageKey = "-";
                break;
            default:
                messageKey = "-";
                break;
        }
        addLabel(instance.getString(messageKey), Fonts.gameFont, this.leftOffset, y);
        if (relation == 1) {
            addTraitorFineLabel();
        }
    }

    private void addTraitorFineLabel() {
        DiplomacyManager diplomacyManager = getDiplomacyManager();
        float y = (float) ((this.position.height - ((double) this.topOffset)) - ((double) (0.05f * GraphicsYio.height)));
        addLabel(LanguagesManager.getInstance().getString("fine") + ": " + (diplomacyManager.calculateTraitorFine(diplomacyManager.getMainEntity()) * 20), Fonts.smallerMenuFont, this.leftOffset, y);
    }

    protected void onYesButtonPressed() {
        getDiplomacyManager().onUserRequestedToMakeRelationsWorse(this.selectedEntity);
        Scenes.sceneConfirmDislike.hide();
    }

    private DiplomacyManager getDiplomacyManager() {
        return this.menuControllerYio.yioGdxGame.gameController.fieldController.diplomacyManager;
    }

    protected void onNoButtonPressed() {
        Scenes.sceneConfirmDislike.hide();
    }

    public boolean areButtonsEnabled() {
        return true;
    }

    public void setSelectedEntity(DiplomaticEntity selectedEntity) {
        this.selectedEntity = selectedEntity;
        updateAll();
    }
}
