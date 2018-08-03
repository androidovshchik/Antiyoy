package io.androidovshchik.antiyoy.menu.scenes;

import io.androidovshchik.antiyoy.menu.ButtonYio;
import io.androidovshchik.antiyoy.menu.MenuControllerYio;
import io.androidovshchik.antiyoy.menu.SpecialThanksDialog;
import io.androidovshchik.antiyoy.menu.behaviors.Reaction;
import io.androidovshchik.antiyoy.menu.scenes.editor.SceneEditorInstruments;
import io.androidovshchik.antiyoy.stuff.LanguagesManager;

public class SceneSpecialThanks extends AbstractScene {
    ButtonYio backButton;
    SpecialThanksDialog dialog = null;

    public SceneSpecialThanks(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
    }

    public void create() {
        this.menuControllerYio.beginMenuCreation();
        this.menuControllerYio.getYioGdxGame().beginBackgroundChange(1, false, true);
        this.backButton = this.menuControllerYio.spawnBackButton(900, Reaction.rbInfo);
        createDialog();
        this.menuControllerYio.endMenuCreation();
    }

    private void createDialog() {
        if (this.dialog == null) {
            initDialogOnce();
        }
        this.dialog.appear();
    }

    private void initDialogOnce() {
        this.dialog = new SpecialThanksDialog(this.menuControllerYio);
        this.dialog.setPosition(generateRectangle(0.05d, SceneEditorInstruments.ICON_SIZE, 0.9d, 0.75d));
        this.dialog.setTitle(getString("special_thanks_title"));
        this.menuControllerYio.addElementToScene(this.dialog);
        for (String token : LanguagesManager.getInstance().getString("special_thanks").split("#")) {
            if (token.length() >= 2) {
                String[] split = token.split(":");
                String name = split[0];
                String desc = split[1];
                if (desc.length() >= 2) {
                    this.dialog.addItem("-", name, desc);
                }
            }
        }
    }
}
