package io.androidovshchik.antiyoy.menu.diplomatic_dialogs;

import java.util.ArrayList;
import java.util.Iterator;
import io.androidovshchik.antiyoy.gameplay.diplomacy.DiplomacyManager;
import io.androidovshchik.antiyoy.gameplay.diplomacy.DiplomaticEntity;
import io.androidovshchik.antiyoy.menu.MenuControllerYio;
import io.androidovshchik.antiyoy.stuff.Fonts;
import io.androidovshchik.antiyoy.stuff.LanguagesManager;

public class ConfirmBlackMarkDialog extends AbstractDiplomaticDialog {
    ArrayList<String> lines = new ArrayList();
    DiplomaticEntity selectedEntity;

    public ConfirmBlackMarkDialog(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
    }

    protected void makeLabels() {
        convertSourceLineToList(LanguagesManager.getInstance().getString("black_mark_description"), this.lines);
        float y = (float) (this.position.height - ((double) this.topOffset));
        addLabel(LanguagesManager.getInstance().getString("confirm_black_mark"), Fonts.gameFont, this.leftOffset, y);
        y -= this.titleOffset;
        Iterator it = this.lines.iterator();
        while (it.hasNext()) {
            addLabel((String) it.next(), Fonts.smallerMenuFont, this.leftOffset, y);
            y -= this.lineOffset;
        }
    }

    protected void onYesButtonPressed() {
        getDiplomacyManager().onUserRequestedBlackMark(this.selectedEntity);
        destroy();
    }

    public void setSelectedEntity(DiplomaticEntity selectedEntity) {
        this.selectedEntity = selectedEntity;
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
}
