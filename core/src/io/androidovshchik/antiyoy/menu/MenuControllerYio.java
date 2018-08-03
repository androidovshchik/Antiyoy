package io.androidovshchik.antiyoy.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.StringTokenizer;
import io.androidovshchik.antiyoy.YioGdxGame;
import io.androidovshchik.antiyoy.factor_yio.FactorYio;
import io.androidovshchik.antiyoy.gameplay.game_view.GameView;
import io.androidovshchik.antiyoy.menu.behaviors.Reaction;
import io.androidovshchik.antiyoy.menu.scenes.Scenes;
import io.androidovshchik.antiyoy.menu.scenes.editor.SceneEditorInstruments;
import io.androidovshchik.antiyoy.stuff.LanguagesManager;
import io.androidovshchik.antiyoy.stuff.RectangleYio;

public class MenuControllerYio {
    public static int DESTROY_ANIM = 2;
    public static double DESTROY_SPEED = 1.5d;
    public static int SPAWN_ANIM = 2;
    public static double SPAWN_SPEED = 1.5d;
    private final ButtonFactory buttonFactory = new ButtonFactory(this);
    public ButtonRenderer buttonRenderer = new ButtonRenderer();
    public final ArrayList<ButtonYio> buttons = new ArrayList();
    public ArrayList<CheckButtonYio> checkButtons;
    public FactorYio infoPanelFactor = new FactorYio();
    public ArrayList<InterfaceElement> interfaceElements = new ArrayList();
    TextureRegion lockedLevelIcon = GameView.loadTextureRegion("locked_level_icon.png", true);
    TextureRegion openedLevelIcon = GameView.loadTextureRegion("opened_level_icon.png", true);
    public SpecialActionController specialActionController = new SpecialActionController(this);
    TextureRegion unlockedLevelIcon = GameView.loadTextureRegion("unlocked_level_icon.png", true);
    public final YioGdxGame yioGdxGame;

    public MenuControllerYio(YioGdxGame yioGdxGame) {
        this.yioGdxGame = yioGdxGame;
        initCheckButtons();
        applyAnimStyle();
        Scenes.createScenes(this);
        Scenes.sceneMainMenu.create();
        checkToCreateSingleMessage();
    }

    private void checkToCreateSingleMessage() {
    }

    private void initCheckButtons() {
        this.checkButtons = new ArrayList();
        for (int i = 0; i < 30; i++) {
            CheckButtonYio.getCheckButton(this, generateRectangle(0.0d, 0.0d, 0.0d, 0.0d), i + 1);
            getCheckButtonById(i + 1).destroy();
        }
    }

    public void move() {
        this.infoPanelFactor.move();
        this.specialActionController.move();
        Iterator it = this.checkButtons.iterator();
        while (it.hasNext()) {
            ((CheckButtonYio) it.next()).move();
        }
        it = this.buttons.iterator();
        while (it.hasNext()) {
            ((ButtonYio) it.next()).move();
        }
        it = this.interfaceElements.iterator();
        while (it.hasNext()) {
            InterfaceElement interfaceElement = (InterfaceElement) it.next();
            if (interfaceElement.isVisible()) {
                interfaceElement.move();
            }
        }
        checkToPerformAction();
    }

    private void checkToPerformAction() {
        int i = this.interfaceElements.size() - 1;
        while (i >= 0) {
            InterfaceElement interfaceElement = (InterfaceElement) this.interfaceElements.get(i);
            if (!interfaceElement.isVisible() || !interfaceElement.checkToPerformAction()) {
                i--;
            } else {
                return;
            }
        }
        i = this.buttons.size() - 1;
        while (i >= 0 && !((ButtonYio) this.buttons.get(i)).checkToPerformAction()) {
            i--;
        }
    }

    public void addButtonToArray(ButtonYio buttonYio) {
        ListIterator iterator = this.buttons.listIterator();
        while (iterator.hasNext()) {
            iterator.next();
        }
        iterator.add(buttonYio);
    }

    public void removeInterfaceElementFromArray(ButtonYio buttonYio) {
        ListIterator iterator = this.buttons.listIterator();
        while (iterator.hasNext()) {
            if (((ButtonYio) iterator.next()) == buttonYio) {
                iterator.remove();
                return;
            }
        }
    }

    public ButtonYio getButtonById(int id) {
        Iterator it = this.buttons.iterator();
        while (it.hasNext()) {
            ButtonYio buttonYio = (ButtonYio) it.next();
            if (buttonYio.id == id) {
                return buttonYio;
            }
        }
        return null;
    }

    public void onPause() {
        Iterator it = this.buttons.iterator();
        while (it.hasNext()) {
            ((ButtonYio) it.next()).onPause();
        }
        it = this.interfaceElements.iterator();
        while (it.hasNext()) {
            ((InterfaceElement) it.next()).onPause();
        }
    }

    public void onResume() {
        Iterator it = this.buttons.iterator();
        while (it.hasNext()) {
            ((ButtonYio) it.next()).onResume();
        }
        it = this.interfaceElements.iterator();
        while (it.hasNext()) {
            ((InterfaceElement) it.next()).onResume();
        }
    }

    public void loadButtonOnce(ButtonYio buttonYio, String fileName) {
        if (buttonYio.notRendered()) {
            buttonYio.loadTexture(fileName);
        }
    }

    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        int i;
        for (i = this.interfaceElements.size() - 1; i >= 0; i--) {
            InterfaceElement interfaceElement = (InterfaceElement) this.interfaceElements.get(i);
            if (interfaceElement.isTouchable() && interfaceElement.isVisible() && interfaceElement.touchDown(screenX, screenY, pointer, button)) {
                return true;
            }
        }
        Iterator it = this.checkButtons.iterator();
        while (it.hasNext()) {
            CheckButtonYio checkButton = (CheckButtonYio) it.next();
            if (checkButton.isTouchable() && checkButton.checkTouch(screenX, screenY, pointer, button)) {
                return true;
            }
        }
        for (i = this.buttons.size() - 1; i >= 0; i--) {
            ButtonYio buttonYio = (ButtonYio) this.buttons.get(i);
            if (buttonYio.isTouchable() && buttonYio.checkTouch(screenX, screenY, pointer, button)) {
                return true;
            }
        }
        return false;
    }

    public void touchDragged(int screenX, int screenY, int pointer) {
        Iterator it = this.interfaceElements.iterator();
        while (it.hasNext()) {
            InterfaceElement interfaceElement = (InterfaceElement) it.next();
            if (interfaceElement.isTouchable() && interfaceElement.isVisible()) {
                interfaceElement.touchDrag(screenX, screenY, pointer);
            }
        }
    }

    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        for (int i = this.interfaceElements.size() - 1; i >= 0; i--) {
            InterfaceElement interfaceElement = (InterfaceElement) this.interfaceElements.get(i);
            if (interfaceElement.isTouchable() && interfaceElement.isVisible() && interfaceElement.touchUp(screenX, screenY, pointer, button)) {
                return true;
            }
        }
        return false;
    }

    public boolean onMouseWheelScrolled(int amount) {
        Iterator it = this.interfaceElements.iterator();
        while (it.hasNext()) {
            InterfaceElement interfaceElement = (InterfaceElement) it.next();
            if (interfaceElement.isTouchable() && interfaceElement.isVisible() && interfaceElement.onMouseWheelScrolled(amount)) {
                return true;
            }
        }
        return false;
    }

    public void beginMenuCreation() {
        this.infoPanelFactor.setValues(1.0d, 0.0d);
        this.infoPanelFactor.destroy(1, 3.0d);
        Iterator it = this.interfaceElements.iterator();
        while (it.hasNext()) {
            ((InterfaceElement) it.next()).destroy();
        }
        it = this.checkButtons.iterator();
        while (it.hasNext()) {
            ((CheckButtonYio) it.next()).destroy();
        }
        it = this.buttons.iterator();
        while (it.hasNext()) {
            ButtonYio buttonYio = (ButtonYio) it.next();
            buttonYio.destroy();
            if (buttonYio.id == 3 && buttonYio.isVisible()) {
                buttonYio.appearFactor.setValues(1.0d, 0.0d);
                buttonYio.appearFactor.destroy(1, 2.0d);
            }
            if (buttonYio.id >= 22 && buttonYio.id <= 29 && buttonYio.isVisible()) {
                buttonYio.appearFactor.destroy(1, 2.1d);
            }
            if (buttonYio.id == 30 && buttonYio.appearFactor.get() > 0.0f) {
                buttonYio.appearFactor.setValues(1.0d, 0.0d);
                buttonYio.appearFactor.destroy(1, 1.0d);
            }
        }
        if (this.yioGdxGame.gameView != null) {
            this.yioGdxGame.gameView.beginDestroyProcess();
        }
    }

    public void endMenuCreation() {
    }

    void forceSpawningButtonsToTheEnd() {
        Iterator it = this.buttons.iterator();
        while (it.hasNext()) {
            ButtonYio buttonYio = (ButtonYio) it.next();
            if (buttonYio.appearFactor.getGravity() > 0.0d) {
                buttonYio.appearFactor.setValues(1.0d, 0.0d);
            }
        }
    }

    public ArrayList<String> getArrayListFromString(String src) {
        ArrayList<String> list = new ArrayList();
        StringTokenizer tokenizer = new StringTokenizer(src, "#");
        while (tokenizer.hasMoreTokens()) {
            list.add(tokenizer.nextToken());
        }
        return list;
    }

    public RectangleYio generateRectangle(double x, double y, double width, double height) {
        return new RectangleYio(((double) Gdx.graphics.getWidth()) * x, ((double) Gdx.graphics.getHeight()) * y, ((double) Gdx.graphics.getWidth()) * width, ((double) Gdx.graphics.getHeight()) * height);
    }

    public RectangleYio generateSquare(double x, double y, double size) {
        return generateRectangle(x, y, size / ((double) YioGdxGame.screenRatio), size);
    }

    public String getString(String key) {
        return LanguagesManager.getInstance().getString(key);
    }

    public void renderTextAndSomeEmptyLines(ButtonYio buttonYio, String text, int emptyLines) {
        if (buttonYio.notRendered()) {
            buttonYio.addTextLine(text);
            for (int i = 0; i < emptyLines; i++) {
                buttonYio.addTextLine(" ");
            }
            this.buttonRenderer.renderButton(buttonYio);
        }
    }

    public void hideAllEditorPanels() {
        Scenes.sceneEditorHexPanel.hide();
        Scenes.sceneEditorObjectPanel.hide();
        Scenes.sceneEditorParams.hide();
        Scenes.sceneEditorAutomationPanel.hide();
        Scenes.sceneEditorMoneyPanel.hide();
        Scenes.sceneEditorChecks.hide();
        this.yioGdxGame.gameController.getLevelEditor().onAllPanelsHide();
    }

    public void destroyButton(int id) {
        ButtonYio buttonYio = getButtonById(id);
        if (buttonYio != null) {
            buttonYio.destroy();
        }
    }

    public void applyAnimStyle() {
        SPAWN_ANIM = 2;
        SPAWN_SPEED = 1.5d;
        DESTROY_ANIM = 2;
        DESTROY_SPEED = 1.5d;
    }

    public String getColorNameByIndexWithOffset(int index, String keyModifier) {
        return getColorNameWithoutOffset(this.yioGdxGame.gameController.getColorIndexWithOffset(index), keyModifier);
    }

    public String getColorNameWithoutOffset(int index, String keyModifier) {
        switch (index) {
            case 1:
            case 5:
                return getString("red" + keyModifier);
            case 2:
                return getString("magenta" + keyModifier);
            case 3:
                return getString("cyan" + keyModifier);
            case 4:
                return getString("yellow" + keyModifier);
            case 7:
                return getString("gray" + keyModifier);
            default:
                return getString("green" + keyModifier);
        }
    }

    public void forceDyingButtonsToEnd() {
        Iterator it = this.buttons.iterator();
        while (it.hasNext()) {
            ButtonYio button = (ButtonYio) it.next();
            if (button.appearFactor.getGravity() < 0.0d) {
                button.appearFactor.setValues(0.0d, 0.0d);
            }
        }
    }

    public ButtonYio spawnBackButton(int id, Reaction reaction) {
        ButtonYio backButton = this.buttonFactory.getButton(generateRectangle(0.05d, 0.9d, 0.4d, SceneEditorInstruments.ICON_SIZE), id, null);
        loadButtonOnce(backButton, "back_icon.png");
        backButton.setShadow(true);
        backButton.setAnimation(1);
        backButton.setReaction(reaction);
        backButton.setTouchOffset(0.05f * ((float) Gdx.graphics.getHeight()));
        this.yioGdxGame.registerBackButtonId(id);
        return backButton;
    }

    public void addCheckButtonToArray(CheckButtonYio checkButtonYio) {
        this.checkButtons.listIterator().add(checkButtonYio);
    }

    public CheckButtonYio getCheckButtonById(int id) {
        Iterator it = this.checkButtons.iterator();
        while (it.hasNext()) {
            CheckButtonYio checkButton = (CheckButtonYio) it.next();
            if (checkButton.id == id) {
                return checkButton;
            }
        }
        return null;
    }

    public ButtonFactory getButtonFactory() {
        return this.buttonFactory;
    }

    public void removeButtonById(int id) {
        ListIterator<ButtonYio> iterator = this.buttons.listIterator();
        while (iterator.hasNext()) {
            if (((ButtonYio) iterator.next()).id == id) {
                iterator.remove();
                return;
            }
        }
    }

    public void addElementToScene(InterfaceElement element) {
        ListIterator iterator = this.interfaceElements.listIterator();
        while (iterator.hasNext()) {
            iterator.next();
        }
        iterator.add(element);
    }

    public void removeElementFromScene(InterfaceElement interfaceElement) {
        ListIterator iterator = this.interfaceElements.listIterator();
        while (iterator.hasNext()) {
            if (((InterfaceElement) iterator.next()) == interfaceElement) {
                iterator.remove();
                return;
            }
        }
    }

    public YioGdxGame getYioGdxGame() {
        return this.yioGdxGame;
    }

    public ButtonRenderer getButtonRenderer() {
        return this.buttonRenderer;
    }

    public void clear() {
        this.buttons.clear();
    }

    public void close() {
    }
}
