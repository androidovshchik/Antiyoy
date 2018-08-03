package io.androidovshchik.antiyoy.menu.scenes;

import java.util.Iterator;
import io.androidovshchik.antiyoy.gameplay.loading.LoadingManager;
import io.androidovshchik.antiyoy.gameplay.loading.LoadingParameters;
import io.androidovshchik.antiyoy.gameplay.user_levels.AbstractUserLevel;
import io.androidovshchik.antiyoy.gameplay.user_levels.UserLevelFactory;
import io.androidovshchik.antiyoy.gameplay.user_levels.UserLevelProgressManager;
import io.androidovshchik.antiyoy.menu.ButtonYio;
import io.androidovshchik.antiyoy.menu.MenuControllerYio;
import io.androidovshchik.antiyoy.menu.behaviors.Reaction;
import io.androidovshchik.antiyoy.menu.scenes.editor.SceneEditorInstruments;
import io.androidovshchik.antiyoy.menu.scrollable_list.ListBehaviorYio;
import io.androidovshchik.antiyoy.menu.scrollable_list.ListItemYio;
import io.androidovshchik.antiyoy.menu.scrollable_list.ScrollableListYio;
import io.androidovshchik.antiyoy.stuff.LanguagesManager;

public class SceneUserLevels extends AbstractScene {
    private ButtonYio backButton;
    ScrollableListYio scrollableListYio = null;

    class C01391 implements ListBehaviorYio {
        C01391() {
        }

        public void applyItem(ListItemYio item) {
            SceneUserLevels.this.OnItemClicked(item);
        }
    }

    class C01402 extends Reaction {
        C01402() {
        }

        public void perform(ButtonYio buttonYio) {
            Scenes.sceneUserLevels.create();
        }
    }

    public SceneUserLevels(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
    }

    public void create() {
        this.menuControllerYio.beginMenuCreation();
        this.menuControllerYio.getYioGdxGame().beginBackgroundChange(1, false, true);
        this.backButton = this.menuControllerYio.spawnBackButton(910, Reaction.rbChooseGameModeMenu);
        createList();
        this.menuControllerYio.endMenuCreation();
    }

    private void createList() {
        if (this.scrollableListYio == null) {
            initListOnce();
        }
        updateList();
        this.scrollableListYio.appear();
    }

    private void updateList() {
        this.scrollableListYio.clearItems();
        Iterator it = UserLevelFactory.getInstance().getLevels().iterator();
        while (it.hasNext()) {
            AbstractUserLevel userLevel = (AbstractUserLevel) it.next();
            this.scrollableListYio.addItem(userLevel.getKey(), getMapTitle(userLevel), userLevel.getAuthor());
        }
        checkToCreateAddMapItem();
    }

    private void checkToCreateAddMapItem() {
        if (isAddMapItemEnabled()) {
            this.scrollableListYio.addItem("add_my_map", LanguagesManager.getInstance().getString("add_my_map"), " ");
        }
    }

    private boolean isAddMapItemEnabled() {
        return ((double) (((float) UserLevelProgressManager.getInstance().getNumberOfCompletedLevels()) / ((float) UserLevelFactory.getInstance().getLevels().size()))) > 0.8d;
    }

    private String getMapTitle(AbstractUserLevel userLevel) {
        if (UserLevelProgressManager.getInstance().isLevelCompleted(userLevel.getKey())) {
            return "[+] " + userLevel.getMapName();
        }
        return userLevel.getMapName();
    }

    private void initListOnce() {
        this.scrollableListYio = new ScrollableListYio(this.menuControllerYio);
        this.scrollableListYio.setPosition(generateRectangle(0.05d, SceneEditorInstruments.ICON_SIZE, 0.9d, 0.75d));
        this.scrollableListYio.setTitle(LanguagesManager.getInstance().getString("user_levels"));
        this.menuControllerYio.addElementToScene(this.scrollableListYio);
        this.scrollableListYio.setListBehavior(new C01391());
    }

    private void OnItemClicked(ListItemYio item) {
        if (item.key.equals("add_my_map")) {
            onAddMyMapClicked();
            return;
        }
        AbstractUserLevel level = UserLevelFactory.getInstance().getLevel(item.key);
        LoadingParameters instance = LoadingParameters.getInstance();
        instance.mode = 9;
        instance.applyFullLevel(level.getFullLevelString());
        instance.colorOffset = level.getColorOffset();
        instance.fogOfWar = level.getFogOfWar();
        instance.ulKey = level.getKey();
        LoadingManager.getInstance().startGame(instance);
    }

    private void onAddMyMapClicked() {
        Scenes.sceneInfoMenu.create("how_add_my_map", new C01402(), 911);
    }
}
