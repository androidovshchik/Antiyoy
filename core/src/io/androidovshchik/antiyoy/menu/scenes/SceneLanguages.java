package io.androidovshchik.antiyoy.menu.scenes;

import java.util.ArrayList;
import java.util.Iterator;
import yio.tro.antiyoy.CustomLanguageLoader;
import yio.tro.antiyoy.Settings;
import yio.tro.antiyoy.menu.ButtonYio;
import yio.tro.antiyoy.menu.MenuControllerYio;
import yio.tro.antiyoy.menu.behaviors.Reaction;
import yio.tro.antiyoy.menu.scenes.editor.SceneEditorInstruments;
import yio.tro.antiyoy.menu.scrollable_list.ListBehaviorYio;
import yio.tro.antiyoy.menu.scrollable_list.ListItemYio;
import yio.tro.antiyoy.menu.scrollable_list.ScrollableListYio;
import yio.tro.antiyoy.stuff.LanguageChooseItem;
import yio.tro.antiyoy.stuff.LanguagesManager;

public class SceneLanguages extends AbstractScene {
    private ButtonYio backButton;
    ScrollableListYio scrollableListYio = null;

    class C01221 implements ListBehaviorYio {
        C01221() {
        }

        public void applyItem(ListItemYio item) {
            SceneLanguages.this.applyLanguage(item.key);
        }
    }

    public SceneLanguages(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
    }

    public void create() {
        this.menuControllerYio.beginMenuCreation();
        this.menuControllerYio.getYioGdxGame().beginBackgroundChange(1, false, true);
        this.backButton = this.menuControllerYio.spawnBackButton(330, Reaction.rbMoreSettings);
        createList();
        this.menuControllerYio.endMenuCreation();
    }

    void applyLanguage(String key) {
        this.menuControllerYio.clear();
        CustomLanguageLoader.setAndSaveLanguage(key);
        Settings.getInstance().loadSettings();
        Scenes.createScenes(this.menuControllerYio);
        Scenes.sceneMainMenu.create();
    }

    private void createList() {
        if (this.scrollableListYio == null) {
            ArrayList<LanguageChooseItem> chooseListItems = LanguagesManager.getInstance().getChooseListItems();
            this.scrollableListYio = new ScrollableListYio(this.menuControllerYio);
            this.scrollableListYio.setPosition(generateRectangle(0.05d, SceneEditorInstruments.ICON_SIZE, 0.9d, 0.75d));
            this.scrollableListYio.setTitle(getString("language"));
            this.menuControllerYio.addElementToScene(this.scrollableListYio);
            this.scrollableListYio.clearItems();
            Iterator it = chooseListItems.iterator();
            while (it.hasNext()) {
                LanguageChooseItem chooseListItem = (LanguageChooseItem) it.next();
                this.scrollableListYio.addItem(chooseListItem.name, chooseListItem.title, chooseListItem.author);
            }
            this.scrollableListYio.setListBehavior(new C01221());
        }
        this.scrollableListYio.appear();
    }
}
