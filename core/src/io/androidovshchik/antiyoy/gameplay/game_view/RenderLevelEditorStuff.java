package io.androidovshchik.antiyoy.gameplay.game_view;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import yio.tro.antiyoy.gameplay.editor.LevelEditor;
import yio.tro.antiyoy.gameplay.rules.GameRules;
import yio.tro.antiyoy.stuff.Fonts;

public class RenderLevelEditorStuff extends GameRender {
    LevelEditor levelEditor = this.gameController.getLevelEditor();
    BitmapFont moneyFont = Fonts.microFont;

    public RenderLevelEditorStuff(GrManager grManager) {
        super(grManager);
    }

    public void loadTextures() {
    }

    public void render() {
        if (GameRules.inEditorMode && this.levelEditor.showMoney) {
            renderMoney();
        }
    }

    private void renderMoney() {
    }

    public void disposeTextures() {
    }
}
