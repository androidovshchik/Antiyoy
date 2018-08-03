package io.androidovshchik.antiyoy.menu.scenes;

import yio.tro.antiyoy.menu.ButtonYio;
import yio.tro.antiyoy.menu.MenuControllerYio;
import yio.tro.antiyoy.menu.behaviors.Reaction;
import yio.tro.antiyoy.menu.scenes.editor.SceneEditorInstruments;
import yio.tro.antiyoy.menu.scrollable_list.ListBehaviorYio;
import yio.tro.antiyoy.menu.scrollable_list.ListItemYio;
import yio.tro.antiyoy.menu.scrollable_list.ScrollableListYio;

public class SceneTestScrollableList extends AbstractScene {
    private ButtonYio backButton;
    public ScrollableListYio scrollableListYio = null;

    class C01371 extends Reaction {
        C01371() {
        }

        public void perform(ButtonYio buttonYio) {
            Scenes.sceneMainMenu.create();
        }
    }

    class C01382 implements ListBehaviorYio {
        C01382() {
        }

        public void applyItem(ListItemYio item) {
            System.out.println();
            System.out.println("SceneTestScrollableList.applyItem");
            System.out.println("item = " + item.title);
        }
    }

    public SceneTestScrollableList(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
    }

    public void create() {
        this.menuControllerYio.beginMenuCreation();
        this.menuControllerYio.getYioGdxGame().beginBackgroundChange(1, false, true);
        this.backButton = this.menuControllerYio.spawnBackButton(710, new C01371());
        createList();
        this.menuControllerYio.endMenuCreation();
    }

    private void createList() {
        if (this.scrollableListYio == null) {
            this.scrollableListYio = new ScrollableListYio(this.menuControllerYio);
            this.scrollableListYio.setPosition(generateRectangle(0.05d, SceneEditorInstruments.ICON_SIZE, 0.9d, 0.75d));
            this.scrollableListYio.setTitle("Title");
            for (int i = 0; i < 10; i++) {
                this.scrollableListYio.addItem("key" + i, "item " + i, "description");
            }
            this.scrollableListYio.setListBehavior(new C01382());
            this.menuControllerYio.addElementToScene(this.scrollableListYio);
        }
        this.scrollableListYio.appear();
    }
}
