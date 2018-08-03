package io.androidovshchik.antiyoy.menu.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import java.util.ArrayList;
import yio.tro.antiyoy.menu.ButtonYio;
import yio.tro.antiyoy.menu.MenuControllerYio;
import yio.tro.antiyoy.menu.behaviors.Reaction;
import yio.tro.antiyoy.stuff.Fonts;

public class SceneExceptionReport extends AbstractScene {
    public SceneExceptionReport(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
    }

    public void create(Exception exception) {
        int i;
        this.menuControllerYio.beginMenuCreation();
        this.menuControllerYio.getYioGdxGame().setGamePaused(true);
        ArrayList<String> text = new ArrayList();
        String title = "Error : " + exception.toString();
        if (title.length() > 44) {
            title = title.substring(0, 44);
        }
        text.add(title);
        for (StackTraceElement stackTraceElement : exception.getStackTrace()) {
            String temp = stackTraceElement.toString();
            int start = 0;
            boolean go = true;
            while (go) {
                int end = start + 44;
                if (end > temp.length() - 1) {
                    go = false;
                    end = temp.length() - 1;
                }
                try {
                    text.add(temp.substring(start, end));
                } catch (ArrayIndexOutOfBoundsException e) {
                }
                start = end + 1;
                if (text.size() > 25) {
                    go = false;
                }
            }
        }
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font.otf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        int FONT_SIZE = (int) (0.021d * ((double) Gdx.graphics.getHeight()));
        parameter.size = FONT_SIZE;
        parameter.characters = Fonts.getAllCharacters();
        parameter.flip = true;
        BitmapFont font = generator.generateFont(parameter);
        ButtonYio textPanel = this.buttonFactory.getButton(generateRectangle(0.1d, 0.2d, 0.8d, 0.7d), 6731267, null);
        if (textPanel.notRendered()) {
            textPanel.addManyLines(text);
            for (i = 0; i < 25 - text.size(); i++) {
                textPanel.addTextLine(" ");
            }
            this.menuControllerYio.getButtonRenderer().renderButton(textPanel, font, FONT_SIZE);
        }
        textPanel.setTouchable(false);
        this.buttonFactory.getButton(generateRectangle(0.1d, 0.1d, 0.8d, 0.1d), 73612321, "Ok").setReaction(Reaction.rbMainMenu);
        this.menuControllerYio.endMenuCreation();
    }

    public void create() {
    }
}
