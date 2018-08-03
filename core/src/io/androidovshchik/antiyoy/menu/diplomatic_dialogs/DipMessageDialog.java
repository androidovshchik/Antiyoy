package io.androidovshchik.antiyoy.menu.diplomatic_dialogs;

import java.util.ArrayList;
import java.util.Iterator;
import yio.tro.antiyoy.menu.MenuControllerYio;
import yio.tro.antiyoy.stuff.Fonts;
import yio.tro.antiyoy.stuff.LanguagesManager;

public class DipMessageDialog extends AbstractDiplomaticDialog {
    ArrayList<String> lines = new ArrayList();
    String sourceText = null;
    String title = null;

    public DipMessageDialog(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
    }

    protected void makeLabels() {
        float y = (float) (this.position.height - ((double) this.topOffset));
        addLabel(LanguagesManager.getInstance().getString(this.title), Fonts.gameFont, this.leftOffset, y);
        y -= this.titleOffset;
        Iterator it = this.lines.iterator();
        while (it.hasNext()) {
            addLabel((String) it.next(), Fonts.smallerMenuFont, this.leftOffset, y);
            y -= this.lineOffset;
        }
    }

    public void setMessage(String title, String messageKey) {
        this.title = title;
        this.sourceText = LanguagesManager.getInstance().getString(messageKey);
        updateLines();
        updateAll();
    }

    private void updateLines() {
        convertSourceLineToList(this.sourceText, this.lines);
    }

    protected void onYesButtonPressed() {
    }

    protected void onNoButtonPressed() {
    }

    public boolean areButtonsEnabled() {
        return false;
    }
}
