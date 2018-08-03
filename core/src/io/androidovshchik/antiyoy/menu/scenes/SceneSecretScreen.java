package io.androidovshchik.antiyoy.menu.scenes;

import io.androidovshchik.antiyoy.menu.ButtonYio;
import io.androidovshchik.antiyoy.menu.MenuControllerYio;
import io.androidovshchik.antiyoy.menu.behaviors.Reaction;
import io.androidovshchik.antiyoy.menu.scenes.editor.SceneEditorInstruments;

public class SceneSecretScreen extends AbstractScene {
    double curY;
    private ButtonYio label;
    private final Reaction rbFireworks = new C01291();
    private final Reaction rbTestScrollableList = new C01302();

    class C01291 extends Reaction {
        C01291() {
        }

        public void perform(ButtonYio buttonYio) {
            Scenes.sceneFireworks.create();
        }
    }

    class C01302 extends Reaction {
        C01302() {
        }

        public void perform(ButtonYio buttonYio) {
            Scenes.sceneTestScrollableList.create();
        }
    }

    public SceneSecretScreen(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
    }

    public void create() {
        this.menuControllerYio.beginMenuCreation();
        this.menuControllerYio.getYioGdxGame().beginBackgroundChange(1, true, true);
        this.label = this.buttonFactory.getButton(generateRectangle(0.1d, 0.55d, 0.8d, 0.3d), 570, null);
        if (this.label.notRendered()) {
            this.label.cleatText();
            this.label.addTextLine("This is secret screen.");
            this.label.addTextLine("It's mostly used for debug purposes, but it also contains some stuff that can be useful for players.");
            this.label.addTextLine("For example, you can tap 'unlock levels' and you will be able to launch any campaign level.");
            this.menuControllerYio.buttonRenderer.renderButton(this.label);
        }
        this.label.setTouchable(false);
        this.label.setAnimation(1);
        this.curY = 0.42d;
        createButton(572, "Unlock levels", Reaction.rbUnlockLevels);
        createButton(573, "Show FPS", Reaction.rbShowFps);
        createButton(574, "Fireworks", this.rbFireworks);
        createButton(575, "Scrollable list", this.rbTestScrollableList);
        this.menuControllerYio.spawnBackButton(576, Reaction.rbMainMenu);
        this.menuControllerYio.endMenuCreation();
    }

    private ButtonYio createButton(int id, String key, Reaction reaction) {
        ButtonYio button = this.buttonFactory.getButton(generateRectangle(0.1d, this.curY, 0.8d, SceneEditorInstruments.ICON_SIZE), id, getString(key));
        button.setReaction(reaction);
        button.setAnimation(2);
        this.curY -= 0.09d;
        return button;
    }
}
