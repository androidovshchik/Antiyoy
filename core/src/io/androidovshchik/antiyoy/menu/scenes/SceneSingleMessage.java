package io.androidovshchik.antiyoy.menu.scenes;

import com.badlogic.gdx.net.HttpStatus;
import java.util.ArrayList;
import yio.tro.antiyoy.menu.ButtonYio;
import yio.tro.antiyoy.menu.MenuControllerYio;
import yio.tro.antiyoy.menu.behaviors.Reaction;
import yio.tro.antiyoy.menu.scenes.editor.SceneEditorInstruments;

public class SceneSingleMessage extends AbstractScene {
    public SceneSingleMessage(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
    }

    public void createSingleMessageMenu(String key) {
        this.menuControllerYio.beginMenuCreation();
        this.menuControllerYio.getYioGdxGame().beginBackgroundChange(0, false, false);
        ButtonYio textPanel = this.buttonFactory.getButton(generateRectangle(0.05d, 0.25d, 0.9d, 0.5d), HttpStatus.SC_BAD_REQUEST, null);
        if (textPanel.notRendered()) {
            textPanel.cleatText();
            ArrayList<String> list = this.menuControllerYio.getArrayListFromString(getString(key));
            textPanel.addManyLines(list);
            int addedEmptyLines = 12 - list.size();
            for (int i = 0; i < addedEmptyLines; i++) {
                textPanel.addTextLine(" ");
            }
            this.menuControllerYio.getButtonRenderer().renderButton(textPanel);
        }
        textPanel.setTouchable(false);
        textPanel.setAnimation(5);
        ButtonYio okButton = this.buttonFactory.getButton(generateRectangle(0.65d, 0.25d, 0.3d, SceneEditorInstruments.ICON_SIZE), HttpStatus.SC_UNAUTHORIZED, "Ok");
        okButton.setReaction(Reaction.rbMainMenu);
        okButton.setAnimation(5);
        this.menuControllerYio.endMenuCreation();
    }

    public void create() {
    }
}
