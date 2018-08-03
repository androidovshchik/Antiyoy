package io.androidovshchik.antiyoy.menu.scenes;

import io.androidovshchik.antiyoy.menu.ButtonYio;
import io.androidovshchik.antiyoy.menu.MenuControllerYio;
import io.androidovshchik.antiyoy.menu.behaviors.Reaction;
import io.androidovshchik.antiyoy.stuff.GraphicsYio;
import io.androidovshchik.antiyoy.stuff.LanguagesManager;

public class SceneBleentoroRelease extends AbstractScene {
    ButtonYio okButton;
    ButtonYio panel;

    class C01171 extends Reaction {
        C01171() {
        }

        public void perform(ButtonYio buttonYio) {
            Scenes.sceneMainMenu.create();
        }
    }

    public SceneBleentoroRelease(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
    }

    public void create() {
        this.menuControllerYio.beginMenuCreation();
        this.menuControllerYio.getYioGdxGame().beginBackgroundChange(2, false, true);
        this.panel = this.buttonFactory.getButton(generateRectangle(0.05d, 0.33d, 0.9d, 0.34d), 830, null);
        this.panel.cleatText();
        this.panel.addManyLines(this.menuControllerYio.getArrayListFromString(LanguagesManager.getInstance().getString("bleentoro_release")));
        while (this.panel.text.size() < 8) {
            this.panel.addTextLine(" ");
        }
        this.menuControllerYio.buttonRenderer.renderButton(this.panel);
        this.panel.setTouchable(false);
        this.panel.setAnimation(5);
        this.okButton = this.buttonFactory.getButton(generateRectangle(0.55d, 0.33d, 0.4d, 0.05d), 831, LanguagesManager.getInstance().getString("end_game_ok"));
        this.okButton.setTouchOffset(0.1f * GraphicsYio.width);
        this.okButton.setShadow(false);
        this.okButton.setReaction(new C01171());
        this.okButton.setAnimation(5);
        this.menuControllerYio.endMenuCreation();
    }
}
