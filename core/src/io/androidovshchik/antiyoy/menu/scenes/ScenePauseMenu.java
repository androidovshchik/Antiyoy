package io.androidovshchik.antiyoy.menu.scenes;

import yio.tro.antiyoy.menu.ButtonYio;
import yio.tro.antiyoy.menu.MenuControllerYio;
import yio.tro.antiyoy.menu.behaviors.Reaction;

public class ScenePauseMenu extends AbstractScene {
    private double bHeight;
    private double bWidth;
    private double bottomY;
    public ButtonYio resumeButton;
    private double f142x;

    public ScenePauseMenu(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
    }

    public void create() {
        this.menuControllerYio.beginMenuCreation();
        this.menuControllerYio.getYioGdxGame().beginBackgroundChange(3, true, true);
        initMetrics();
        ButtonYio basePanel = this.buttonFactory.getButton(generateRectangle(this.f142x, this.bottomY, this.bWidth, 4.0d * this.bHeight), 40, null);
        basePanel.setTouchable(false);
        basePanel.onlyShadow = true;
        basePanel.setAnimation(5);
        double y = this.bottomY;
        ButtonYio mainMenuButton = this.buttonFactory.getButton(generateRectangle(this.f142x, y, this.bWidth, this.bHeight), 42, getString("in_game_menu_main_menu"));
        mainMenuButton.setReaction(Reaction.rbMainMenu);
        mainMenuButton.setShadow(false);
        mainMenuButton.setAnimation(5);
        mainMenuButton.disableTouchAnimation();
        y += this.bHeight;
        ButtonYio chooseLevelButton = this.buttonFactory.getButton(generateRectangle(this.f142x, y, this.bWidth, this.bHeight), 43, getString("in_game_menu_save"));
        chooseLevelButton.setReaction(Reaction.rbSaveGame);
        chooseLevelButton.setShadow(false);
        chooseLevelButton.setAnimation(5);
        chooseLevelButton.disableTouchAnimation();
        y += this.bHeight;
        ButtonYio restartButton = this.buttonFactory.getButton(generateRectangle(this.f142x, y, this.bWidth, this.bHeight), 44, getString("in_game_menu_restart"));
        restartButton.setReaction(Reaction.rbRestartGame);
        restartButton.setShadow(false);
        restartButton.setAnimation(5);
        restartButton.disableTouchAnimation();
        y += this.bHeight;
        this.resumeButton = this.buttonFactory.getButton(generateRectangle(this.f142x, y, this.bWidth, this.bHeight), 45, getString("in_game_menu_resume"));
        this.resumeButton.setReaction(Reaction.rbResumeGame);
        this.resumeButton.setShadow(false);
        this.resumeButton.setAnimation(5);
        this.resumeButton.disableTouchAnimation();
        y += this.bHeight;
        this.menuControllerYio.endMenuCreation();
    }

    private void initMetrics() {
        this.bHeight = 0.09d;
        this.bottomY = 0.3d;
        this.bWidth = 0.76d;
        this.f142x = (1.0d - this.bWidth) / 2.0d;
    }
}
