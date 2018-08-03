package io.androidovshchik.antiyoy.menu.diplomatic_dialogs;

import io.androidovshchik.antiyoy.gameplay.diplomacy.DiplomaticEntity;
import io.androidovshchik.antiyoy.menu.MenuControllerYio;
import io.androidovshchik.antiyoy.menu.scenes.Scenes;
import io.androidovshchik.antiyoy.stuff.Fonts;
import io.androidovshchik.antiyoy.stuff.LanguagesManager;

public class FriendshipDialog extends AbstractDiplomaticDialog {
    DiplomaticEntity recipient;
    DiplomaticEntity sender;

    public FriendshipDialog(MenuControllerYio menuControllerYio) {
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

    public void setEntities(DiplomaticEntity sender, DiplomaticEntity recipient) {
        this.sender = sender;
        this.recipient = recipient;
        updateAll();
        updateTagColor();
    }

    private void updateTagColor() {
        int color = this.sender.color;
        if (this.sender.isMain()) {
            color = this.recipient.color;
        }
        setTagColor(color);
    }

    protected void updateTagPosition() {
        this.tagPosition.f146x = this.viewPosition.f146x + ((double) this.leftOffset);
        this.tagPosition.width = this.viewPosition.width - ((double) (2.0f * this.leftOffset));
        this.tagPosition.f147y = ((((this.viewPosition.f147y + this.viewPosition.height) - ((double) this.topOffset)) - ((double) this.titleOffset)) - ((double) this.lineOffset)) + ((double) (this.lineOffset / 4.0f));
        this.tagPosition.height = (double) this.lineOffset;
    }

    protected void makeLabels() {
        if (this.sender != null && this.recipient != null) {
            LanguagesManager instance = LanguagesManager.getInstance();
            float y = (float) (this.position.height - ((double) this.topOffset));
            addLabel(instance.getString("treaty_of_friendship"), Fonts.gameFont, this.leftOffset, y);
            y -= this.titleOffset;
            String capitalName = this.sender.capitalName;
            if (this.sender.isMain()) {
                capitalName = this.recipient.capitalName;
            }
            addLabel(instance.getString("state") + ": " + capitalName, Fonts.smallerMenuFont, this.leftOffset, y);
            y -= this.lineOffset;
            addLabel(instance.getString("dotations") + ": " + getDotationsValue(), Fonts.smallerMenuFont, this.leftOffset, y);
            y -= this.lineOffset;
            addLabel(instance.getString("duration") + ": " + 12, Fonts.smallerMenuFont, this.leftOffset, y);
            y -= this.lineOffset;
        }
    }

    protected int getDotationsValue() {
        int dotations = this.menuControllerYio.yioGdxGame.gameController.fieldController.diplomacyManager.calculateDotationsForFriendship(this.sender, this.recipient);
        if (this.recipient.isMain()) {
            return dotations * -1;
        }
        return dotations;
    }

    protected void onYesButtonPressed() {
        this.menuControllerYio.yioGdxGame.gameController.fieldController.diplomacyManager.requestedFriendship(this.sender, this.recipient);
        Scenes.sceneFriendshipDialog.hide();
    }

    protected void onNoButtonPressed() {
        Scenes.sceneFriendshipDialog.hide();
    }

    public boolean areButtonsEnabled() {
        return true;
    }
}
