package io.androidovshchik.antiyoy.stuff;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;
import io.androidovshchik.antiyoy.gameplay.game_view.GameView;

public class AtlasLoader {
    private boolean antialias;
    private TextureRegion atlasRegion;
    private ArrayList<String> fileNames;
    private ArrayList<RectangleYio> imageSpecs;
    private int rows;
    private String srcName;
    private String txtFileName;

    public AtlasLoader(String srcName, String txtFileName, boolean antialias) {
        this.srcName = srcName;
        this.txtFileName = txtFileName;
        this.antialias = antialias;
        loadEverything();
    }

    private AtlasLoader() {
    }

    private void loadEverything() {
        this.atlasRegion = GameView.loadTextureRegion(this.srcName, this.antialias);
        String atlasStructure = Gdx.files.internal(this.txtFileName).readString();
        ArrayList<String> lines = new ArrayList();
        StringTokenizer stringTokenizer = new StringTokenizer(atlasStructure, "\n");
        while (stringTokenizer.hasMoreTokens()) {
            String token = stringTokenizer.nextToken();
            if (token.contains("rows=")) {
                this.rows = Integer.valueOf(token.substring(5, token.length() - 1)).intValue();
            }
            if (token.length() > 5) {
                if (!token.contains("compression=")) {
                    if (!token.contains("rows=")) {
                        lines.add(token);
                    }
                }
            }
        }
        this.fileNames = new ArrayList();
        this.imageSpecs = new ArrayList();
        Iterator it = lines.iterator();
        while (it.hasNext()) {
            String line = (String) it.next();
            int charPos = line.indexOf("#");
            this.fileNames.add(line.substring(0, charPos));
            int[] array = getArrayFromString(line.substring(charPos + 1, line.length() - 1), 4);
            this.imageSpecs.add(new RectangleYio((double) array[0], (double) array[1], (double) array[2], (double) array[3]));
        }
    }

    private int[] getArrayFromString(String str, int size) {
        StringTokenizer stringTokenizer = new StringTokenizer(str, " ");
        int[] array = new int[size];
        int i = 0;
        while (stringTokenizer.hasMoreTokens()) {
            array[i] = Integer.valueOf(stringTokenizer.nextToken()).intValue();
            i++;
        }
        return array;
    }

    public TextureRegion getTexture(String fileName) {
        int index = this.fileNames.indexOf(fileName);
        return new TextureRegion(this.atlasRegion, (int) ((RectangleYio) this.imageSpecs.get(index)).f146x, (int) ((RectangleYio) this.imageSpecs.get(index)).f147y, (int) ((RectangleYio) this.imageSpecs.get(index)).width, (int) ((RectangleYio) this.imageSpecs.get(index)).height);
    }

    public void disposeAtlasRegion() {
        this.atlasRegion.getTexture().dispose();
    }
}
