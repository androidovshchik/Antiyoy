package io.androidovshchik.antiyoy.menu.scenes;

import yio.tro.antiyoy.gameplay.editor.LevelEditor;
import yio.tro.antiyoy.menu.ButtonYio;
import yio.tro.antiyoy.menu.MenuControllerYio;
import yio.tro.antiyoy.menu.behaviors.Reaction;
import yio.tro.antiyoy.menu.behaviors.editor.EditorReactions;
import yio.tro.antiyoy.menu.scenes.editor.SceneEditorInstruments;
import yio.tro.antiyoy.stuff.GraphicsYio;

public class SceneChoodeGameModeMenu extends AbstractScene {
    private Reaction rbUserLevels = new C01181();
    public ButtonYio skirmishButton;

    class C01181 extends Reaction {
        C01181() {
        }

        public void perform(ButtonYio buttonYio) {
            Scenes.sceneUserLevels.create();
        }
    }

    public SceneChoodeGameModeMenu(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
    }

    public void create() {
        this.menuControllerYio.beginMenuCreation();
        this.menuControllerYio.getYioGdxGame().beginBackgroundChange(1, true, true);
        createBasePanel();
        createSkirmishButton();
        createUserLevelsButton();
        createCampaignButton();
        createLoadGameButton();
        createEditorButton();
        createInvisibleButton();
        this.menuControllerYio.spawnBackButton(76, Reaction.rbMainMenu);
        this.menuControllerYio.endMenuCreation();
    }

    private void createInvisibleButton() {
        ButtonYio invisButton = this.buttonFactory.getButton(generateSquare(1.0d - SceneEditorInstruments.ICON_SIZE, 1.0d - GraphicsYio.convertToHeight(SceneEditorInstruments.ICON_SIZE), SceneEditorInstruments.ICON_SIZE), 78, null);
        invisButton.setVisible(false);
        invisButton.setReaction(Reaction.rbShowCheatSceen);
    }

    private void createEditorButton() {
        ButtonYio editorButton = this.buttonFactory.getButton(generateRectangle(0.1d, 0.54d, 0.8d, 0.08d), 77, getString(LevelEditor.EDITOR_PREFS));
        editorButton.setShadow(false);
        editorButton.setAnimation(5);
        editorButton.setReaction(EditorReactions.rbEditorSlotMenu);
    }

    private void createLoadGameButton() {
        ButtonYio loadGameButton = this.buttonFactory.getButton(generateRectangle(0.1d, 0.3d, 0.8d, 0.08d), 75, getString("choose_game_mode_load"));
        loadGameButton.setShadow(false);
        loadGameButton.setAnimation(5);
        loadGameButton.setReaction(Reaction.rbLoadGame);
        loadGameButton.disableTouchAnimation();
    }

    private void createCampaignButton() {
        ButtonYio campaignButton = this.buttonFactory.getButton(generateRectangle(0.1d, 0.38d, 0.8d, 0.08d), 74, getString("choose_game_mode_campaign"));
        campaignButton.setReaction(Reaction.rbCampaignMenu);
        campaignButton.setShadow(false);
        campaignButton.setAnimation(5);
        campaignButton.disableTouchAnimation();
    }

    private void createUserLevelsButton() {
        ButtonYio userLevelsButton = this.buttonFactory.getButton(generateRectangle(0.1d, 0.46d, 0.8d, 0.08d), 73, getString("user_levels"));
        userLevelsButton.setShadow(false);
        userLevelsButton.setReaction(this.rbUserLevels);
        userLevelsButton.setAnimation(5);
    }

    private void createSkirmishButton() {
        this.skirmishButton = this.buttonFactory.getButton(generateRectangle(0.1d, 0.62d, 0.8d, 0.08d), 72, getString("choose_game_mode_skirmish"));
        this.skirmishButton.setReaction(Reaction.rbSkirmishMenu);
        this.skirmishButton.setShadow(false);
        this.skirmishButton.setAnimation(5);
    }

    private void createBasePanel() {
        ButtonYio basePanel = this.buttonFactory.getButton(generateRectangle(0.1d, 0.3d, 0.8d, 0.4d), 70, null);
        basePanel.setTouchable(false);
        basePanel.onlyShadow = true;
        basePanel.setAnimation(5);
    }
}
