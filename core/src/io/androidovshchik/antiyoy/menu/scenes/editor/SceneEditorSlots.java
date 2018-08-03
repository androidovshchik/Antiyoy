package io.androidovshchik.antiyoy.menu.scenes.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Preferences;
import io.androidovshchik.antiyoy.gameplay.editor.LevelEditor;
import io.androidovshchik.antiyoy.menu.MenuControllerYio;
import io.androidovshchik.antiyoy.menu.behaviors.Reaction;
import io.androidovshchik.antiyoy.menu.scenes.AbstractScene;
import io.androidovshchik.antiyoy.menu.scenes.Scenes;
import io.androidovshchik.antiyoy.menu.scrollable_list.ListBehaviorYio;
import io.androidovshchik.antiyoy.menu.scrollable_list.ListItemYio;
import io.androidovshchik.antiyoy.menu.scrollable_list.ScrollableListYio;
import io.androidovshchik.antiyoy.stuff.LanguagesManager;

public class SceneEditorSlots extends AbstractScene {
    ScrollableListYio list = null;

    class C01461 implements ListBehaviorYio {
        C01461() {
        }

        public void applyItem(ListItemYio item) {
            SceneEditorSlots.this.onListItemClicked(item);
        }
    }

    public SceneEditorSlots(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
    }

    public void create() {
        this.menuControllerYio.beginMenuCreation();
        this.menuControllerYio.getYioGdxGame().beginBackgroundChange(2, true, true);
        createList();
        this.menuControllerYio.spawnBackButton(Keys.CONTROL_RIGHT, Reaction.rbChooseGameModeMenu);
        this.menuControllerYio.endMenuCreation();
    }

    private void createList() {
        initListOnce();
        updateList();
        this.list.appear();
    }

    private void onListItemClicked(ListItemYio item) {
        getLevelEditor().setCurrentSlotNumber(getSlotNumber(item));
        Scenes.sceneEditorActions.create();
    }

    private int getSlotNumber(ListItemYio item) {
        return Integer.valueOf(item.key.substring(4)).intValue();
    }

    private LevelEditor getLevelEditor() {
        return this.menuControllerYio.yioGdxGame.gameController.getLevelEditor();
    }

    private void updateList() {
        this.list.clearItems();
        Preferences preferences = getPreferences();
        int index = 1;
        boolean atLeastOneEmpty = false;
        while (true) {
            String key = LevelEditor.SLOT_NAME + index;
            if (index > 8 && !preferences.contains(key)) {
                break;
            }
            if (isEmpty(preferences, key)) {
                this.list.addItem(key, getString(LevelEditor.SLOT_NAME) + " " + index, getString("empty"));
                atLeastOneEmpty = true;
            } else {
                this.list.addItem(key, getString(LevelEditor.SLOT_NAME) + " " + index, " ");
            }
            index++;
        }
        if (!atLeastOneEmpty) {
            this.list.addItem(LevelEditor.SLOT_NAME + index, getString(LevelEditor.SLOT_NAME) + " " + index, getString("empty"));
        }
    }

    private boolean isEmpty(Preferences preferences, String key) {
        return preferences.getString(key).length() < 10;
    }

    private Preferences getPreferences() {
        return Gdx.app.getPreferences(LevelEditor.EDITOR_PREFS);
    }

    private void initListOnce() {
        if (this.list == null) {
            this.list = new ScrollableListYio(this.menuControllerYio);
            this.list.setPosition(generateRectangle(0.05d, SceneEditorInstruments.ICON_SIZE, 0.9d, 0.75d));
            this.list.setTitle(LanguagesManager.getInstance().getString(LevelEditor.EDITOR_PREFS));
            this.menuControllerYio.addElementToScene(this.list);
            this.list.setListBehavior(new C01461());
        }
    }
}
