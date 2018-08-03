package io.androidovshchik.antiyoy.menu.scenes;

import com.badlogic.gdx.Gdx;
import java.util.ArrayList;
import yio.tro.antiyoy.menu.ButtonYio;
import yio.tro.antiyoy.menu.MenuControllerYio;
import yio.tro.antiyoy.menu.behaviors.Reaction;
import yio.tro.antiyoy.menu.scenes.editor.SceneEditorInstruments;

public class SceneInfoMenu extends AbstractScene {
    public SceneInfoMenu(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
    }

    public void create(String key, Reaction backButtonBehavior, int id_offset) {
        this.menuControllerYio.beginMenuCreation();
        this.menuControllerYio.getYioGdxGame().beginBackgroundChange(1, true, false);
        this.menuControllerYio.spawnBackButton(id_offset, backButtonBehavior);
        ButtonYio infoPanel = this.buttonFactory.getButton(generateRectangle(0.05d, 0.1d, 0.9d, 0.7d), id_offset + 1, null);
        infoPanel.cleatText();
        ArrayList<String> list = this.menuControllerYio.getArrayListFromString(getString(key));
        infoPanel.addManyLines(list);
        int addedEmptyLines = 18 - list.size();
        for (int i = 0; i < addedEmptyLines; i++) {
            infoPanel.addTextLine(" ");
        }
        this.menuControllerYio.getButtonRenderer().renderButton(infoPanel);
        infoPanel.setTouchable(false);
        infoPanel.setAnimation(5);
        infoPanel.appearFactor.appear(2, 1.5d);
        this.menuControllerYio.endMenuCreation();
    }

    public void create() {
        create("info_array", Reaction.rbMainMenu, 10);
        ButtonYio helpIndexButton = this.buttonFactory.getButton(generateRectangle(0.5d, 0.9d, 0.45d, SceneEditorInstruments.ICON_SIZE), 38123714, getString("help"));
        helpIndexButton.setReaction(Reaction.rbHelpIndex);
        helpIndexButton.setAnimation(1);
        ButtonYio moreInfoButton = this.buttonFactory.getButton(generateRectangle(0.65d, 0.1d, 0.3d, 0.04d), 38123717, getString("more"));
        moreInfoButton.setReaction(Reaction.rbSpecialThanksMenu);
        moreInfoButton.setAnimation(5);
        moreInfoButton.disableTouchAnimation();
        moreInfoButton.setTouchOffset(0.05f * ((float) Gdx.graphics.getHeight()));
    }
}
