package io.androidovshchik.antiyoy.gameplay.game_view;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import io.androidovshchik.antiyoy.gameplay.editor.LevelEditor;
import io.androidovshchik.antiyoy.gameplay.rules.GameRules;
import io.androidovshchik.antiyoy.stuff.Fonts;

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
