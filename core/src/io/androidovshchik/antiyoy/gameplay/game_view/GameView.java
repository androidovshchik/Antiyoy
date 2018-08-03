package io.androidovshchik.antiyoy.gameplay.game_view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import io.androidovshchik.antiyoy.Settings;
import io.androidovshchik.antiyoy.Storage3xTexture;
import io.androidovshchik.antiyoy.YioGdxGame;
import io.androidovshchik.antiyoy.factor_yio.FactorYio;
import io.androidovshchik.antiyoy.gameplay.FieldController;
import io.androidovshchik.antiyoy.gameplay.GameController;
import io.androidovshchik.antiyoy.gameplay.Hex;
import io.androidovshchik.antiyoy.gameplay.Province;
import io.androidovshchik.antiyoy.gameplay.Unit;
import io.androidovshchik.antiyoy.gameplay.rules.GameRules;
import io.androidovshchik.antiyoy.menu.ButtonYio;
import io.androidovshchik.antiyoy.stuff.AtlasLoader;
import io.androidovshchik.antiyoy.stuff.Fonts;
import io.androidovshchik.antiyoy.stuff.FrameBufferYio;
import io.androidovshchik.antiyoy.stuff.GraphicsYio;
import io.androidovshchik.antiyoy.stuff.PointYio;
import io.androidovshchik.antiyoy.stuff.RectangleYio;
import io.androidovshchik.antiyoy.stuff.Yio;

public class GameView {
    TextureRegion animationTextureRegion;
    public AtlasLoader atlasLoader;
    private TextureRegion backgroundRegion;
    SpriteBatch batchCache;
    SpriteBatch batchMovable = new SpriteBatch();
    SpriteBatch batchSolid;
    TextureRegion blackBorderTexture;
    public TextureRegion blackCircleTexture;
    public TextureRegion blackPixel;
    TextureRegion blackTriangle;
    float borderLineThickness;
    public OrthographicCamera cacheCam;
    public float cacheFrameX1;
    public float cacheFrameX2;
    public float cacheFrameY1;
    public float cacheFrameY2;
    TextureRegion[] cacheLevelTextures;
    double camBlurSpeed;
    public Storage3xTexture castleTexture;
    public TextureRegion currentObjectTexture;
    int currentZoomQuality;
    float cx;
    float cy;
    TextureRegion defenseIcon;
    float dh;
    float dw;
    public TextureRegion exclamationMarkTexture;
    public final FactorYio factorModel = new FactorYio();
    public Storage3xTexture[] farmTexture;
    public TextureRegion forefingerTexture;
    private final FrameBuffer frameBuffer = FrameBufferYio.getInstance(Format.RGB565, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
    FrameBuffer[] frameBufferList;
    public final GameController gameController;
    GrManager grManager;
    public TextureRegion gradientShadow;
    public Storage3xTexture graveTexture;
    public TextureRegion grayPixel;
    TextureRegion greenPixel;
    int f99h = Gdx.graphics.getHeight();
    TextureRegion hexBlue;
    TextureRegion hexColor1;
    TextureRegion hexColor2;
    TextureRegion hexColor3;
    TextureRegion hexCyan;
    TextureRegion hexGreen;
    TextureRegion hexRed;
    public float hexShadowSize;
    public float hexViewSize;
    TextureRegion hexYellow;
    public Storage3xTexture houseTexture;
    public float linkLineThickness;
    public Storage3xTexture[] manTextures;
    TextureRegion moveZonePixel;
    public OrthographicCamera orthoCam;
    public Storage3xTexture palmTexture;
    public Storage3xTexture pineTexture;
    PointYio pos;
    TextureRegion responseAnimHexTexture;
    RectangleYio screenRectangle;
    int segments;
    public TextureRegion selUnitShadow;
    TextureRegion selectionBorder;
    public TextureRegion selectionPixel;
    public TextureRegion shadowHexTexture;
    TextureRegion sideShadow;
    private float smDelta;
    private float smFactor;
    public Storage3xTexture strongTowerTexture;
    public Storage3xTexture towerTexture;
    public TextureRegion transCircle1;
    public TextureRegion transCircle2;
    int f100w = Gdx.graphics.getWidth();
    private final YioGdxGame yioGdxGame;
    double zoomLevelOne;
    double zoomLevelTwo;

    public GameView(YioGdxGame yioGdxGame) {
        this.yioGdxGame = yioGdxGame;
        this.gameController = yioGdxGame.gameController;
        this.batchSolid = yioGdxGame.batch;
        this.batchCache = new SpriteBatch();
        createOrthoCam();
        this.cacheCam = new OrthographicCamera((float) yioGdxGame.f149w, (float) yioGdxGame.f148h);
        this.cacheCam.position.set(this.orthoCam.viewportWidth / 2.0f, this.orthoCam.viewportHeight / 2.0f, 0.0f);
        this.cx = (float) (yioGdxGame.f149w / 2);
        this.cy = (float) (yioGdxGame.f148h / 2);
        this.zoomLevelOne = 0.8d;
        this.zoomLevelTwo = 1.3d;
        this.borderLineThickness = 0.006f * ((float) this.f100w);
        this.linkLineThickness = 0.01f * ((float) Gdx.graphics.getWidth());
        this.segments = Gdx.graphics.getWidth() / 75;
        if (this.segments < 12) {
            this.segments = 12;
        }
        if (this.segments > 24) {
            this.segments = 24;
        }
        this.hexViewSize = 1.04f * this.gameController.fieldController.hexSize;
        this.hexShadowSize = 1.0f * this.hexViewSize;
        initFrameBufferList();
        this.screenRectangle = new RectangleYio(0.0d, 0.0d, (double) this.f100w, (double) this.f99h);
        this.camBlurSpeed = 0.001d * ((double) this.f100w);
        this.grManager = new GrManager(this);
        this.grManager.create();
        loadTextures();
    }

    private void initFrameBufferList() {
        this.frameBufferList = new FrameBuffer[4];
        for (int i = 0; i < this.frameBufferList.length; i++) {
            this.frameBufferList[i] = FrameBufferYio.getInstance(Format.RGB565, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        }
    }

    public void createOrthoCam() {
        this.orthoCam = new OrthographicCamera((float) this.yioGdxGame.f149w, (float) this.yioGdxGame.f148h);
        this.orthoCam.position.set(this.orthoCam.viewportWidth / 2.0f, this.orthoCam.viewportHeight / 2.0f, 0.0f);
        updateCam();
    }

    public void createLevelCacheTextures() {
        this.cacheLevelTextures = new TextureRegion[this.gameController.fieldController.levelSize];
    }

    private void loadTextures() {
        loadBackgroundTexture();
        this.blackCircleTexture = loadTextureRegion("black_circle.png", true);
        this.shadowHexTexture = loadTextureRegion("shadow_hex.png", true);
        this.gradientShadow = loadTextureRegion("gradient_shadow.png", false);
        this.blackPixel = loadTextureRegion("black_pixel.png", false);
        this.transCircle1 = loadTextureRegion("transition_circle_1.png", false);
        this.transCircle2 = loadTextureRegion("transition_circle_2.png", false);
        loadFieldTextures();
        this.selUnitShadow = loadTextureRegion("sel_shadow.png", true);
        this.sideShadow = loadTextureRegion("money_shadow.png", true);
        this.moveZonePixel = loadTextureRegion("move_zone_pixel.png", false);
        this.responseAnimHexTexture = loadTextureRegion("response_anim_hex.png", false);
        this.selectionBorder = loadTextureRegion("selection_border.png", false);
        loadExclamationMark();
        this.forefingerTexture = loadTextureRegion("forefinger.png", true);
        this.defenseIcon = loadTextureRegion("defense_icon.png", true);
        this.blackBorderTexture = loadTextureRegion("pixels/black_border.png", true);
        this.blackTriangle = loadTextureRegion("triangle.png", false);
        this.greenPixel = loadTextureRegion("pixels/pixel_green.png", false);
        this.grayPixel = loadTextureRegion("pixels/gray_pixel.png", false);
        this.grManager.loadTextures();
    }

    void disposeTextures() {
        this.backgroundRegion.getTexture().dispose();
        this.blackCircleTexture.getTexture().dispose();
        this.shadowHexTexture.getTexture().dispose();
        this.gradientShadow.getTexture().dispose();
        this.blackPixel.getTexture().dispose();
        this.transCircle1.getTexture().dispose();
        this.transCircle2.getTexture().dispose();
        this.selUnitShadow.getTexture().dispose();
        this.sideShadow.getTexture().dispose();
        this.moveZonePixel.getTexture().dispose();
        this.responseAnimHexTexture.getTexture().dispose();
        this.selectionBorder.getTexture().dispose();
        this.forefingerTexture.getTexture().dispose();
        this.defenseIcon.getTexture().dispose();
        this.blackBorderTexture.getTexture().dispose();
        this.blackTriangle.getTexture().dispose();
        this.greenPixel.getTexture().dispose();
        this.atlasLoader.disposeAtlasRegion();
        this.grManager.disposeTextures();
    }

    private void loadExclamationMark() {
        if (Settings.isShroomArtsEnabled()) {
            this.exclamationMarkTexture = loadTextureRegion("skins/ant/exclamation_mark.png", true);
        } else {
            this.exclamationMarkTexture = loadTextureRegion("exclamation_mark.png", true);
        }
    }

    public void loadBackgroundTexture() {
        if (Settings.waterTexture) {
            this.backgroundRegion = loadTextureRegion("game_background_water.png", true);
        } else {
            this.backgroundRegion = loadTextureRegion("game_background.png", true);
        }
    }

    public void loadSkin(int skin) {
        switch (skin) {
            case 1:
                loadPointsSkin();
                break;
            case 2:
                loadGridSkin();
                break;
            default:
                loadOriginalSkin();
                break;
        }
        reloadTextures();
    }

    private void reloadTextures() {
        loadFieldTextures();
        loadExclamationMark();
        resetButtonTexture(37);
        resetButtonTexture(38);
        resetButtonTexture(39);
    }

    private void resetButtonTexture(int id) {
        ButtonYio button = this.yioGdxGame.menuControllerYio.getButtonById(id);
        if (button != null) {
            button.resetTexture();
        }
    }

    private void loadOriginalSkin() {
        this.hexGreen = loadTextureRegion("hex_green.png", false);
        this.hexRed = loadTextureRegion("hex_red.png", false);
        this.hexBlue = loadTextureRegion("hex_blue.png", false);
        this.hexCyan = loadTextureRegion("hex_cyan.png", false);
        this.hexYellow = loadTextureRegion("hex_yellow.png", false);
        this.hexColor1 = loadTextureRegion("hex_color1.png", false);
        this.hexColor2 = loadTextureRegion("hex_color2.png", false);
        this.hexColor3 = loadTextureRegion("hex_color3.png", false);
    }

    private void loadPointsSkin() {
        this.hexGreen = loadTextureRegion("skins/points_hex_green.png", false);
        this.hexRed = loadTextureRegion("skins/points_hex_red.png", false);
        this.hexBlue = loadTextureRegion("skins/points_hex_blue.png", false);
        this.hexCyan = loadTextureRegion("skins/points_hex_cyan.png", false);
        this.hexYellow = loadTextureRegion("skins/points_hex_yellow.png", false);
        this.hexColor1 = loadTextureRegion("skins/points_hex_color1.png", false);
        this.hexColor2 = loadTextureRegion("skins/points_hex_color2.png", false);
        this.hexColor3 = loadTextureRegion("skins/points_hex_color3.png", false);
    }

    private void loadGridSkin() {
        this.hexGreen = loadTextureRegion("skins/hex_green_grid.png", false);
        this.hexRed = loadTextureRegion("skins/hex_red_grid.png", false);
        this.hexBlue = loadTextureRegion("skins/hex_blue_grid.png", false);
        this.hexCyan = loadTextureRegion("skins/hex_cyan_grid.png", false);
        this.hexYellow = loadTextureRegion("skins/hex_yellow_grid.png", false);
        this.hexColor1 = loadTextureRegion("skins/hex_color1_grid.png", false);
        this.hexColor2 = loadTextureRegion("skins/hex_color2_grid.png", false);
        this.hexColor3 = loadTextureRegion("skins/hex_color3_grid.png", false);
    }

    private void loadFieldTextures() {
        this.atlasLoader = createAtlasLoader();
        this.selectionPixel = this.atlasLoader.getTexture("selection_pixel_lowest.png");
        this.manTextures = new Storage3xTexture[4];
        for (int i = 0; i < 4; i++) {
            this.manTextures[i] = new Storage3xTexture(this.atlasLoader, "man" + i + ".png");
        }
        this.graveTexture = new Storage3xTexture(this.atlasLoader, "grave.png");
        this.houseTexture = new Storage3xTexture(this.atlasLoader, "house.png");
        this.palmTexture = new Storage3xTexture(this.atlasLoader, "palm.png");
        this.pineTexture = new Storage3xTexture(this.atlasLoader, "pine.png");
        this.towerTexture = new Storage3xTexture(this.atlasLoader, "tower.png");
        this.castleTexture = new Storage3xTexture(this.atlasLoader, "castle.png");
        this.farmTexture = new Storage3xTexture[3];
        this.farmTexture[0] = new Storage3xTexture(this.atlasLoader, "farm1.png");
        this.farmTexture[1] = new Storage3xTexture(this.atlasLoader, "farm2.png");
        this.farmTexture[2] = new Storage3xTexture(this.atlasLoader, "farm3.png");
        this.strongTowerTexture = new Storage3xTexture(this.atlasLoader, "strong_tower.png");
    }

    private AtlasLoader createAtlasLoader() {
        String path = "field_elements/";
        if (Settings.isShroomArtsEnabled()) {
            path = "skins/ant/field_elements/";
        }
        return new AtlasLoader(path + "atlas_texture.png", path + "atlas_structure.txt", true);
    }

    public static TextureRegion loadTextureRegion(String name, boolean antialias) {
        Texture texture = new Texture(Gdx.files.internal(name));
        if (antialias) {
            texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        }
        return new TextureRegion(texture);
    }

    public void updateCacheLevelTextures() {
        this.gameController.letsUpdateCacheByAnim = false;
        for (int i = 0; i < this.cacheLevelTextures.length; i++) {
            FrameBuffer cacheLevelFrameBuffer = this.frameBufferList[i];
            cacheLevelFrameBuffer.begin();
            Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            switch (i) {
                case 0:
                    this.cacheCam.position.set(((float) this.f100w) * 0.5f, ((float) this.f99h) * 0.5f, 0.0f);
                    setCacheFrame(0.0f, 0.0f, (float) this.f100w, (float) this.f99h);
                    break;
                case 1:
                    this.cacheCam.translate((float) this.f100w, 0.0f);
                    setCacheFrame((float) this.f100w, 0.0f, (float) (this.f100w * 2), (float) this.f99h);
                    break;
                case 2:
                    this.cacheCam.translate((float) (-this.f100w), (float) this.f99h);
                    setCacheFrame(0.0f, (float) this.f99h, (float) this.f100w, (float) (this.f99h * 2));
                    break;
                case 3:
                    this.cacheCam.translate((float) this.f100w, 0.0f);
                    setCacheFrame((float) this.f100w, (float) this.f99h, (float) (this.f100w * 2), (float) (this.f99h * 2));
                    break;
                default:
                    break;
            }
            this.cacheCam.update();
            this.batchCache.setProjectionMatrix(this.cacheCam.combined);
            renderCache(this.batchCache);
            Texture texture = (Texture) cacheLevelFrameBuffer.getColorBufferTexture();
            texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
            this.cacheLevelTextures[i] = new TextureRegion(texture, this.f100w, this.f99h);
            this.cacheLevelTextures[i].flip(false, true);
            cacheLevelFrameBuffer.end();
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void updateCacheNearAnimHexes() {
        /*
        r14 = this;
        r9 = r14.gameController;
        r9 = r9.fieldController;
        r0 = r9.animHexes;
        r9 = r0.size();
        if (r9 != 0) goto L_0x000d;
    L_0x000c:
        return;
    L_0x000d:
        r9 = 0;
        r9 = r0.get(r9);
        r9 = (yio.tro.antiyoy.gameplay.Hex) r9;
        r9 = r9.getPos();
        r1 = r9.f145y;
        r8 = r1;
        r9 = 0;
        r9 = r0.get(r9);
        r9 = (yio.tro.antiyoy.gameplay.Hex) r9;
        r9 = r9.getPos();
        r5 = r9.f144x;
        r4 = r5;
        r3 = 1;
    L_0x002a:
        r9 = r0.size();
        if (r3 >= r9) goto L_0x005d;
    L_0x0030:
        r9 = r0.get(r3);
        r9 = (yio.tro.antiyoy.gameplay.Hex) r9;
        r6 = r9.getPos();
        r9 = r6.f144x;
        r9 = (r9 > r4 ? 1 : (r9 == r4 ? 0 : -1));
        if (r9 >= 0) goto L_0x0042;
    L_0x0040:
        r4 = r6.f144x;
    L_0x0042:
        r9 = r6.f144x;
        r9 = (r9 > r5 ? 1 : (r9 == r5 ? 0 : -1));
        if (r9 <= 0) goto L_0x004a;
    L_0x0048:
        r5 = r6.f144x;
    L_0x004a:
        r9 = r6.f145y;
        r9 = (r9 > r1 ? 1 : (r9 == r1 ? 0 : -1));
        if (r9 >= 0) goto L_0x0052;
    L_0x0050:
        r1 = r6.f145y;
    L_0x0052:
        r9 = r6.f145y;
        r9 = (r9 > r8 ? 1 : (r9 == r8 ? 0 : -1));
        if (r9 <= 0) goto L_0x005a;
    L_0x0058:
        r8 = r6.f145y;
    L_0x005a:
        r3 = r3 + 1;
        goto L_0x002a;
    L_0x005d:
        r9 = r14.hexViewSize;
        r5 = r5 + r9;
        r9 = r14.hexViewSize;
        r4 = r4 - r9;
        r9 = r14.hexViewSize;
        r8 = r8 + r9;
        r9 = r14.hexViewSize;
        r1 = r1 - r9;
        r3 = 0;
    L_0x006a:
        r9 = r14.cacheLevelTextures;
        r9 = r9.length;
        if (r3 >= r9) goto L_0x000c;
    L_0x006f:
        switch(r3) {
            case 0: goto L_0x00c6;
            case 1: goto L_0x00f4;
            case 2: goto L_0x011b;
            case 3: goto L_0x0146;
            default: goto L_0x0072;
        };
    L_0x0072:
        r9 = r14.frameBufferList;
        r2 = r9[r3];
        r2.begin();
        r9 = com.badlogic.gdx.Gdx.gl;
        r10 = 0;
        r11 = 0;
        r12 = 0;
        r13 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r9.glClearColor(r10, r11, r12, r13);
        r9 = com.badlogic.gdx.Gdx.gl;
        r10 = 16384; // 0x4000 float:2.2959E-41 double:8.0948E-320;
        r9.glClear(r10);
        r9 = r14.cacheCam;
        r9.update();
        r9 = r14.batchCache;
        r10 = r14.cacheCam;
        r10 = r10.combined;
        r9.setProjectionMatrix(r10);
        r9 = r14.batchCache;
        r14.renderCache(r9);
        r7 = r2.getColorBufferTexture();
        r7 = (com.badlogic.gdx.graphics.Texture) r7;
        r9 = com.badlogic.gdx.graphics.Texture.TextureFilter.Linear;
        r10 = com.badlogic.gdx.graphics.Texture.TextureFilter.Linear;
        r7.setFilter(r9, r10);
        r9 = r14.cacheLevelTextures;
        r10 = new com.badlogic.gdx.graphics.g2d.TextureRegion;
        r11 = r14.f100w;
        r12 = r14.f99h;
        r10.<init>(r7, r11, r12);
        r9[r3] = r10;
        r9 = r14.cacheLevelTextures;
        r9 = r9[r3];
        r10 = 0;
        r11 = 1;
        r9.flip(r10, r11);
        r2.end();
    L_0x00c3:
        r3 = r3 + 1;
        goto L_0x006a;
    L_0x00c6:
        r9 = r14.cacheCam;
        r9 = r9.position;
        r10 = 1056964608; // 0x3f000000 float:0.5 double:5.222099017E-315;
        r11 = r14.f100w;
        r11 = (float) r11;
        r10 = r10 * r11;
        r11 = 1056964608; // 0x3f000000 float:0.5 double:5.222099017E-315;
        r12 = r14.f99h;
        r12 = (float) r12;
        r11 = r11 * r12;
        r12 = 0;
        r9.set(r10, r11, r12);
        r9 = 0;
        r10 = 0;
        r11 = r14.f100w;
        r11 = (float) r11;
        r12 = r14.f99h;
        r12 = (float) r12;
        r14.setCacheFrame(r9, r10, r11, r12);
        r9 = r14.f100w;
        r9 = (float) r9;
        r9 = (r4 > r9 ? 1 : (r4 == r9 ? 0 : -1));
        if (r9 > 0) goto L_0x00c3;
    L_0x00ec:
        r9 = r14.f99h;
        r9 = (float) r9;
        r9 = (r1 > r9 ? 1 : (r1 == r9 ? 0 : -1));
        if (r9 <= 0) goto L_0x0072;
    L_0x00f3:
        goto L_0x00c3;
    L_0x00f4:
        r9 = r14.cacheCam;
        r10 = r14.f100w;
        r10 = (float) r10;
        r11 = 0;
        r9.translate(r10, r11);
        r9 = r14.f100w;
        r9 = (float) r9;
        r10 = 0;
        r11 = r14.f100w;
        r11 = r11 * 2;
        r11 = (float) r11;
        r12 = r14.f99h;
        r12 = (float) r12;
        r14.setCacheFrame(r9, r10, r11, r12);
        r9 = r14.f100w;
        r9 = (float) r9;
        r9 = (r5 > r9 ? 1 : (r5 == r9 ? 0 : -1));
        if (r9 < 0) goto L_0x00c3;
    L_0x0113:
        r9 = r14.f99h;
        r9 = (float) r9;
        r9 = (r1 > r9 ? 1 : (r1 == r9 ? 0 : -1));
        if (r9 <= 0) goto L_0x0072;
    L_0x011a:
        goto L_0x00c3;
    L_0x011b:
        r9 = r14.cacheCam;
        r10 = r14.f100w;
        r10 = -r10;
        r10 = (float) r10;
        r11 = r14.f99h;
        r11 = (float) r11;
        r9.translate(r10, r11);
        r9 = 0;
        r10 = r14.f99h;
        r10 = (float) r10;
        r11 = r14.f100w;
        r11 = (float) r11;
        r12 = r14.f99h;
        r12 = r12 * 2;
        r12 = (float) r12;
        r14.setCacheFrame(r9, r10, r11, r12);
        r9 = r14.f100w;
        r9 = (float) r9;
        r9 = (r4 > r9 ? 1 : (r4 == r9 ? 0 : -1));
        if (r9 > 0) goto L_0x00c3;
    L_0x013d:
        r9 = r14.f99h;
        r9 = (float) r9;
        r9 = (r8 > r9 ? 1 : (r8 == r9 ? 0 : -1));
        if (r9 >= 0) goto L_0x0072;
    L_0x0144:
        goto L_0x00c3;
    L_0x0146:
        r9 = r14.cacheCam;
        r10 = r14.f100w;
        r10 = (float) r10;
        r11 = 0;
        r9.translate(r10, r11);
        r9 = r14.f100w;
        r9 = (float) r9;
        r10 = r14.f99h;
        r10 = (float) r10;
        r11 = r14.f100w;
        r11 = r11 * 2;
        r11 = (float) r11;
        r12 = r14.f99h;
        r12 = r12 * 2;
        r12 = (float) r12;
        r14.setCacheFrame(r9, r10, r11, r12);
        r9 = r14.f100w;
        r9 = (float) r9;
        r9 = (r5 > r9 ? 1 : (r5 == r9 ? 0 : -1));
        if (r9 < 0) goto L_0x00c3;
    L_0x0169:
        r9 = r14.f99h;
        r9 = (float) r9;
        r9 = (r8 > r9 ? 1 : (r8 == r9 ? 0 : -1));
        if (r9 >= 0) goto L_0x0072;
    L_0x0170:
        goto L_0x00c3;
        */
        throw new UnsupportedOperationException("Method not decompiled: yio.tro.antiyoy.gameplay.game_view.GameView.updateCacheNearAnimHexes():void");
    }

    private void setCacheFrame(float x1, float y1, float x2, float y2) {
        this.cacheFrameX1 = x1;
        this.cacheFrameY1 = y1;
        this.cacheFrameX2 = x2;
        this.cacheFrameY2 = y2;
    }

    public void updateCam() {
        this.orthoCam.update();
        this.batchMovable.setProjectionMatrix(this.orthoCam.combined);
    }

    private void renderCache(SpriteBatch spriteBatch) {
        spriteBatch.begin();
        spriteBatch.draw(this.backgroundRegion, 0.0f, 0.0f, (float) (this.yioGdxGame.f149w * 2), (float) (this.yioGdxGame.f148h * 2));
        int actualZoomQuality = this.currentZoomQuality;
        this.currentZoomQuality = 2;
        renderHexField(spriteBatch);
        this.currentZoomQuality = actualZoomQuality;
        spriteBatch.end();
        Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
    }

    private void renderHexField(SpriteBatch spriteBatch) {
        Iterator it = this.gameController.fieldController.activeHexes.iterator();
        while (it.hasNext()) {
            this.pos = ((Hex) it.next()).getPos();
            if (isPosInCacheFrame(this.pos, this.hexViewSize)) {
                spriteBatch.draw(this.shadowHexTexture, (0.1f * this.hexViewSize) + (this.pos.f144x - this.hexViewSize), (this.pos.f145y - this.hexViewSize) - (0.15f * this.hexViewSize), 2.0f * this.hexViewSize, 2.0f * this.hexViewSize);
            }
        }
        it = this.gameController.fieldController.activeHexes.iterator();
        while (it.hasNext()) {
            Hex hex = (Hex) it.next();
            this.pos = hex.getPos();
            if (isPosInCacheFrame(this.pos, this.hexViewSize)) {
                spriteBatch.draw(getHexTextureByColor(hex.colorIndex), this.pos.f144x - (this.hexViewSize * 0.99f), this.pos.f145y - (this.hexViewSize * 0.99f), 1.98f * this.hexViewSize, 1.98f * this.hexViewSize);
            }
        }
        Iterator it2 = this.gameController.fieldController.activeHexes.iterator();
        while (it2.hasNext()) {
            hex = (Hex) it2.next();
            this.pos = hex.getPos();
            if (isPosInCacheFrame(this.pos, this.hexViewSize)) {
                int i = 0;
                while (i < 6) {
                    Hex adjacentHex = hex.getAdjacentHex(i);
                    if (!(adjacentHex == this.gameController.fieldController.emptyHex || adjacentHex == null || ((!adjacentHex.active || adjacentHex.sameColor(hex) || i < 2 || i > 4) && adjacentHex.active))) {
                        if (i >= 2 && i <= 4) {
                            renderGradientShadow(hex, adjacentHex, spriteBatch);
                        }
                        renderLineBetweenHexes(adjacentHex, hex, spriteBatch, (double) this.borderLineThickness, i);
                    }
                    i++;
                }
            }
        }
        it2 = this.gameController.fieldController.solidObjects.iterator();
        while (it2.hasNext()) {
            hex = (Hex) it2.next();
            renderSolidObject(spriteBatch, hex.getPos(), hex);
        }
    }

    private void renderCertainUnits() {
        Iterator it = this.gameController.unitList.iterator();
        while (it.hasNext()) {
            Unit unit = (Unit) it.next();
            if (isPosInViewFrame(unit.currentPos, this.hexViewSize)) {
                renderUnit(this.batchMovable, unit);
            }
        }
    }

    private TextureRegion getUnitTexture(Unit unit) {
        if (this.gameController.isPlayerTurn() || unit.moveFactor.get() >= 1.0f || ((double) unit.moveFactor.get()) <= 0.1d) {
            return this.manTextures[unit.strength - 1].getTexture(this.currentZoomQuality);
        }
        return this.manTextures[unit.strength - 1].getLowest();
    }

    private void renderUnit(SpriteBatch spriteBatch, Unit unit) {
        PointYio pos = unit.currentPos;
        SpriteBatch spriteBatch2 = spriteBatch;
        spriteBatch2.draw(getUnitTexture(unit), pos.f144x - (0.7f * this.hexViewSize), (unit.jumpPos * this.hexViewSize) + (pos.f145y - (0.5f * this.hexViewSize)), this.hexViewSize * 1.4f, this.hexViewSize * 1.6f);
    }

    private TextureRegion getSolidObjectTexture(Hex hex, int quality) {
        switch (hex.objectInside) {
            case 1:
                return this.pineTexture.getTexture(quality);
            case 2:
                return this.palmTexture.getTexture(quality);
            case 3:
                if (GameRules.slayRules) {
                    return this.houseTexture.getTexture(quality);
                }
                return this.castleTexture.getTexture(quality);
            case 4:
                return this.towerTexture.getTexture(quality);
            case 5:
                return this.graveTexture.getTexture(quality);
            case 6:
                return this.farmTexture[hex.viewDiversityIndex].getTexture(quality);
            case 7:
                return this.strongTowerTexture.getTexture(quality);
            default:
                return this.selectionPixel;
        }
    }

    private TextureRegion getSolidObjectTexture(Hex hex) {
        return getSolidObjectTexture(hex, this.currentZoomQuality);
    }

    private void renderSolidObject(SpriteBatch spriteBatch, PointYio pos, Hex hex) {
        this.currentObjectTexture = getSolidObjectTexture(hex);
        spriteBatch.draw(this.currentObjectTexture, pos.f144x - (0.7f * this.hexViewSize), pos.f145y - (0.5f * this.hexViewSize), this.hexViewSize * 1.4f, this.hexViewSize * 1.6f);
    }

    private void renderGradientShadow(Hex hex1, Hex hex2, SpriteBatch spriteBatch) {
        double a = Yio.angle((double) hex1.pos.f144x, (double) hex1.pos.f145y, (double) hex2.pos.f144x, (double) hex2.pos.f145y);
        double s = 0.5d * ((double) this.gameController.fieldController.hexSize);
        double cx = (0.5d * ((double) (hex1.pos.f144x + hex2.pos.f144x))) - ((0.2d * s) * Math.cos(a));
        double cy = (0.5d * ((double) (hex1.pos.f145y + hex2.pos.f145y))) - ((0.2d * s) * Math.sin(a));
        a += 1.5707963267948966d;
        drawLine((Math.cos(a) * s) + cx, (Math.sin(a) * s) + cy, cx - (Math.cos(a) * s), cy - (Math.sin(a) * s), 0.01d * ((double) this.f100w), spriteBatch, this.gradientShadow);
    }

    private void renderLineBetweenHexesWithOffset(Hex hex1, Hex hex2, SpriteBatch spriteBatch, double thickness, TextureRegion textureRegion, double offset, int rotation, double factor) {
        double a = Yio.angle((double) hex1.pos.f144x, (double) hex1.pos.f145y, (double) hex2.pos.f144x, (double) hex2.pos.f145y);
        double a2 = a + 1.5707963267948966d;
        double cx = 0.5d * ((double) (hex1.pos.f144x + hex2.pos.f144x));
        double cy = 0.5d * ((double) (hex1.pos.f145y + hex2.pos.f145y));
        double s = (0.5d * ((double) this.gameController.fieldController.hexSize)) * (0.7d + (0.37d * factor));
        drawSpecialHexedLine((Math.cos(a2) * s) + ((Math.cos(a) * offset) + cx), (Math.sin(a2) * s) + ((Math.sin(a) * offset) + cy), ((Math.cos(a) * offset) + cx) - (Math.cos(a2) * s), ((Math.sin(a) * offset) + cy) - (Math.sin(a2) * s), thickness, spriteBatch, textureRegion, rotation);
    }

    private void renderLineBetweenHexes(Hex hex1, Hex hex2, SpriteBatch spriteBatch, double thickness, int rotation) {
        double a = Yio.angle((double) hex1.pos.f144x, (double) hex1.pos.f145y, (double) hex2.pos.f144x, (double) hex2.pos.f145y) + 1.5707963267948966d;
        double cx = 0.5d * ((double) (hex1.pos.f144x + hex2.pos.f144x));
        double cy = 0.5d * ((double) (hex1.pos.f145y + hex2.pos.f145y));
        double s = 0.5d * ((double) this.gameController.fieldController.hexSize);
        drawSpecialHexedLine(cx + (Math.cos(a) * s), cy + (Math.sin(a) * s), cx - (Math.cos(a) * s), cy - (Math.sin(a) * s), thickness, spriteBatch, this.blackBorderTexture, rotation + 3);
    }

    private void drawSpecialHexedLine(double x1, double y1, double x2, double y2, double thickness, SpriteBatch spriteBatch, TextureRegion blackPixel, int rotation) {
        spriteBatch.draw(blackPixel, (float) x1, (float) (y1 - (0.5d * thickness)), 0.0f, ((float) thickness) * 0.5f, (float) Yio.distance(x1, y1, x2, y2), (float) thickness, 1.0f, 1.0f, (float) (180.0d * (((double) (-rotation)) / 3.0d)));
    }

    public void beginSpawnProcess() {
        this.factorModel.setValues(0.02d, 0.0d);
        this.factorModel.appear(2, 1.3d);
        updateAnimationTexture();
    }

    public void beginDestroyProcess() {
        if (!this.yioGdxGame.gamePaused) {
            if (this.factorModel.get() >= 1.0f) {
                this.factorModel.setValues(1.0d, 0.0d);
                this.factorModel.destroy(2, 1.5d);
            }
            updateAnimationTexture();
        }
    }

    public void updateAnimationTexture() {
        this.frameBuffer.begin();
        this.batchSolid.begin();
        this.batchSolid.draw(this.blackPixel, 0.0f, 0.0f, (float) this.f100w, (float) this.f99h);
        this.batchSolid.end();
        renderInternals();
        this.frameBuffer.end();
        this.animationTextureRegion = new TextureRegion((Texture) this.frameBuffer.getColorBufferTexture());
        this.animationTextureRegion.flip(false, true);
    }

    private boolean isPosInCacheFrame(PointYio pos, float offset) {
        if (pos.f144x >= this.cacheFrameX1 - offset && pos.f144x <= this.cacheFrameX2 + offset && pos.f145y >= this.cacheFrameY1 - offset && pos.f145y <= this.cacheFrameY2 + offset) {
            return true;
        }
        return false;
    }

    private boolean isPosInViewFrame(PointYio pos, float offset) {
        return this.gameController.cameraController.isPosInViewFrame(pos, offset);
    }

    private TextureRegion getHexTextureByColor(int colorIndex) {
        if (this.gameController.colorIndexViewOffset > 0 && colorIndex != FieldController.NEUTRAL_LANDS_INDEX) {
            colorIndex = this.gameController.getColorIndexWithOffset(colorIndex);
        }
        switch (colorIndex) {
            case 1:
                return this.hexRed;
            case 2:
                return this.hexBlue;
            case 3:
                return this.hexCyan;
            case 4:
                return this.hexYellow;
            case 5:
                return this.hexColor1;
            case 6:
                return this.hexColor2;
            case 7:
                return this.hexColor3;
            default:
                return this.hexGreen;
        }
    }

    private void renderAllSolidObjects() {
        Iterator it = this.gameController.fieldController.activeHexes.iterator();
        while (it.hasNext()) {
            Hex activeHex = (Hex) it.next();
            if (activeHex.containsObject()) {
                renderSolidObject(this.batchMovable, activeHex.getPos(), activeHex);
            }
        }
    }

    private void renderAnimHexes() {
        Color c = this.batchMovable.getColor();
        Iterator it = this.gameController.fieldController.animHexes.iterator();
        while (it.hasNext()) {
            Hex hex = (Hex) it.next();
            PointYio pos = hex.getPos();
            if (isPosInViewFrame(pos, this.hexViewSize)) {
                if (hex.animFactor.get() < 1.0f) {
                    TextureRegion currentHexLastTexture = getHexTextureByColor(hex.lastColorIndex);
                    this.batchMovable.setColor(c.f39r, c.f38g, c.f37b, 1.0f - hex.animFactor.get());
                    this.batchMovable.draw(currentHexLastTexture, pos.f144x - this.hexViewSize, pos.f145y - this.hexViewSize, this.hexViewSize * 2.0f, 2.0f * this.hexViewSize);
                }
                TextureRegion currentHexTexture = getHexTextureByColor(hex.colorIndex);
                this.batchMovable.setColor(c.f39r, c.f38g, c.f37b, hex.animFactor.get());
                this.batchMovable.draw(currentHexTexture, pos.f144x - this.hexViewSize, pos.f145y - this.hexViewSize, 2.0f * this.hexViewSize, 2.0f * this.hexViewSize);
            }
        }
        this.batchMovable.setColor(c.f39r, c.f38g, c.f37b, 1.0f);
        Iterator it2 = this.gameController.fieldController.animHexes.iterator();
        while (it2.hasNext()) {
            hex = (Hex) it2.next();
            pos = hex.getPos();
            if (isPosInViewFrame(pos, this.hexViewSize)) {
                int i = 0;
                while (i < 6) {
                    Hex adjacentHex = hex.getAdjacentHex(i);
                    if (adjacentHex != null && ((adjacentHex.active && !adjacentHex.sameColor(hex)) || !adjacentHex.active)) {
                        if (i >= 2 && i <= 4) {
                            renderGradientShadow(hex, adjacentHex, this.batchMovable);
                        }
                        renderLineBetweenHexes(adjacentHex, hex, this.batchMovable, (double) this.borderLineThickness, i);
                    }
                    i++;
                }
                if (hex.containsObject()) {
                    this.batchMovable.setColor(c.f39r, c.f38g, c.f37b, 1.0f);
                    renderSolidObject(this.batchMovable, pos, hex);
                }
            }
        }
        this.batchMovable.setColor(c.f39r, c.f38g, c.f37b, 1.0f);
    }

    public static void drawFromCenter(SpriteBatch batch, TextureRegion textureRegion, double cx, double cy, double r) {
        batch.draw(textureRegion, (float) (cx - r), (float) (cy - r), (float) (2.0d * r), (float) (2.0d * r));
    }

    private static void drawFromCenterRotated(Batch batch, TextureRegion textureRegion, double cx, double cy, double r, double rotationAngle) {
        batch.draw(textureRegion, (float) (cx - r), (float) (cy - r), (float) r, (float) r, (float) (2.0d * r), (float) (2.0d * r), 1.0f, 1.0f, 57.29f * ((float) rotationAngle));
    }

    private void renderResponseAnimHex() {
        if (this.gameController.fieldController.responseAnimHex != null) {
            this.pos = this.gameController.fieldController.responseAnimHex.getPos();
            Color c = this.batchMovable.getColor();
            this.batchMovable.setColor(c.f39r, c.f38g, c.f37b, 0.5f * Math.min(this.gameController.fieldController.responseAnimFactor.get(), 1.0f));
            float s = Math.max(this.hexViewSize, this.hexViewSize * this.gameController.fieldController.responseAnimFactor.get());
            this.batchMovable.draw(this.responseAnimHexTexture, this.pos.f144x - s, this.pos.f145y - s, 2.0f * s, 2.0f * s);
            this.batchMovable.setColor(c.f39r, c.f38g, c.f37b, c.f36a);
        }
    }

    private void renderExclamationMarks() {
        if (this.gameController.isPlayerTurn()) {
            Iterator it = this.gameController.fieldController.provinces.iterator();
            while (it.hasNext()) {
                Province province = (Province) it.next();
                if (this.gameController.isCurrentTurn(province.getColor()) && province.money >= 10) {
                    PointYio pos = province.getCapital().getPos();
                    if (isPosInViewFrame(pos, this.hexViewSize)) {
                        this.batchMovable.draw(this.exclamationMarkTexture, pos.f144x - (0.5f * this.hexViewSize), (pos.f145y + (0.3f * this.hexViewSize)) + (this.gameController.jumperUnit.jumpPos * this.hexViewSize), 0.35f * this.hexViewSize, 0.6f * this.hexViewSize);
                    }
                }
            }
        }
    }

    private void renderCityNames() {
        if (this.gameController.isCityNamesEnabled() && this.gameController.isPlayerTurn()) {
            Iterator it = this.gameController.fieldController.provinces.iterator();
            while (it.hasNext()) {
                Province province = (Province) it.next();
                if (this.gameController.isCurrentTurn(province.getColor()) && province.isSelected()) {
                    renderSingleCityName(province);
                }
            }
        }
    }

    private void renderSingleCityName(Province province) {
        Hex capitalHex = province.getCapital();
        PointYio pos = capitalHex.getPos();
        float factor = capitalHex.selectionFactor.get() - this.gameController.fieldController.moveZoneFactor.get();
        float pWidth = province.nameWidth;
        Color c = this.batchMovable.getColor();
        this.batchMovable.setColor(c.f39r, c.f38g, c.f37b, factor);
        this.batchMovable.draw(this.greenPixel, (pos.f144x - pWidth) - (0.05f * this.hexViewSize), (pos.f145y + (0.55f * this.hexViewSize)) + ((0.4f * factor) * this.hexViewSize), (2.0f * pWidth) + (0.1f * this.hexViewSize), 1.0f * this.hexViewSize);
        this.batchMovable.draw(this.blackTriangle, pos.f144x - (0.3f * this.hexViewSize), (pos.f145y + (0.22f * this.hexViewSize)) + ((0.2f * factor) * this.hexViewSize), 0.6f * this.hexViewSize, 0.6f * this.hexViewSize);
        this.batchMovable.setColor(c.f39r, c.f38g, c.f37b, 0.3f + (0.7f * factor));
        this.batchMovable.draw(this.blackPixel, pos.f144x - pWidth, (pos.f145y + (0.7f * this.hexViewSize)) + ((0.3f * factor) * this.hexViewSize), 2.0f * pWidth, 0.9f * this.hexViewSize);
        Fonts.microFont.draw(this.batchMovable, province.getName(), (pos.f144x - pWidth) + (0.1f * this.hexViewSize), (pos.f145y + (1.4f * this.hexViewSize)) + ((0.3f * factor) * this.hexViewSize));
        this.batchMovable.setColor(c.f39r, c.f38g, c.f37b, c.f36a);
    }

    public void onResume() {
        loadTextures();
    }

    public void onPause() {
        disposeTextures();
    }

    private void renderSelectedHexes() {
        Iterator it = this.gameController.fieldController.selectedHexes.iterator();
        while (it.hasNext()) {
            Hex hex = (Hex) it.next();
            if (((double) hex.selectionFactor.get()) >= 0.01d) {
                for (int i = 0; i < 6; i++) {
                    Hex h = hex.getAdjacentHex(i);
                    if (!(h == null || h.isEmptyHex() || (h.active && h.sameColor(hex)))) {
                        renderLineBetweenHexesWithOffset(hex, h, this.batchMovable, (((double) hex.selectionFactor.get()) * 0.01d) * ((double) this.f100w), this.selectionBorder, ((-(1.0d - ((double) hex.selectionFactor.get()))) * 0.01d) * ((double) this.f100w), i, (double) hex.selectionFactor.get());
                    }
                }
            }
        }
        Iterator it2 = this.gameController.fieldController.selectedHexes.iterator();
        while (it2.hasNext()) {
            hex = (Hex) it2.next();
            if (hex.containsObject()) {
                renderSolidObject(this.batchMovable, hex.getPos(), hex);
            }
        }
    }

    private void renderTextOnHex(Hex hex, String text) {
        Fonts.gameFont.draw(this.batchMovable, (CharSequence) text, hex.pos.f144x - (((float) this.f100w) * 0.02f), hex.pos.f145y + (((float) this.f100w) * 0.02f));
    }

    private void renderForefinger() {
        if (!GameRules.tutorialMode) {
            return;
        }
        if (this.gameController.forefinger.isPointingToHex()) {
            this.batchMovable.begin();
            this.pos = this.gameController.forefinger.animPos;
            Color c = this.batchMovable.getColor();
            this.batchMovable.setColor(c.f39r, c.f38g, c.f37b, this.gameController.forefinger.getAlpha());
            drawFromCenterRotated(this.batchMovable, this.forefingerTexture, (double) this.pos.f144x, (double) this.pos.f145y, (double) (this.hexViewSize * this.gameController.forefinger.getSize()), this.gameController.forefinger.getRotation());
            this.batchMovable.setColor(c.f39r, c.f38g, c.f37b, c.f36a);
            this.batchMovable.end();
            return;
        }
        this.batchSolid.begin();
        this.pos = this.gameController.forefinger.animPos;
        c = this.batchSolid.getColor();
        this.batchSolid.setColor(c.f39r, c.f38g, c.f37b, this.gameController.forefinger.getAlpha());
        drawFromCenterRotated(this.batchSolid, this.forefingerTexture, (double) this.pos.f144x, (double) this.pos.f145y, (double) (this.hexViewSize * this.gameController.forefinger.getSize()), this.gameController.forefinger.getRotation());
        this.batchSolid.setColor(c.f39r, c.f38g, c.f37b, c.f36a);
        this.batchSolid.end();
    }

    private void renderMoveZone() {
        int k;
        Hex h;
        Color c = this.batchMovable.getColor();
        Iterator it = this.gameController.fieldController.moveZone.iterator();
        while (it.hasNext()) {
            Hex hex = (Hex) it.next();
            PointYio pos = hex.getPos();
            if (isPosInViewFrame(pos, this.hexViewSize)) {
                if (!this.gameController.isPlayerTurn(hex.colorIndex) || hex.animFactor.get() >= 1.0f || hex.animFactor.getDy() <= 0.0d) {
                    this.batchMovable.setColor(c.f39r, c.f38g, c.f37b, 1.0f);
                    this.batchMovable.draw(getHexTextureByColor(hex.colorIndex), pos.f144x - this.hexViewSize, pos.f145y - this.hexViewSize, 2.0f * this.hexViewSize, this.hexViewSize * 2.0f);
                } else {
                    if (hex.animFactor.get() < 1.0f) {
                        TextureRegion currentHexLastTexture = getHexTextureByColor(hex.lastColorIndex);
                        this.batchMovable.setColor(c.f39r, c.f38g, c.f37b, 1.0f - hex.animFactor.get());
                        this.batchMovable.draw(currentHexLastTexture, pos.f144x - this.hexViewSize, pos.f145y - this.hexViewSize, 2.0f * this.hexViewSize, 2.0f * this.hexViewSize);
                    }
                    TextureRegion currentHexTexture = getHexTextureByColor(hex.colorIndex);
                    this.batchMovable.setColor(c.f39r, c.f38g, c.f37b, hex.animFactor.get());
                    this.batchMovable.draw(currentHexTexture, pos.f144x - this.hexViewSize, pos.f145y - this.hexViewSize, 2.0f * this.hexViewSize, this.hexViewSize * 2.0f);
                }
            }
        }
        this.batchMovable.setColor(c.f39r, c.f38g, c.f37b, 1.0f);
        Iterator it2 = this.gameController.fieldController.moveZone.iterator();
        while (it2.hasNext()) {
            int i;
            hex = (Hex) it2.next();
            pos = hex.getPos();
            if (isPosInViewFrame(pos, this.hexViewSize)) {
                for (i = 0; i < 6; i++) {
                    Hex adjacentHex = hex.getAdjacentHex(i);
                    if (adjacentHex != null && ((adjacentHex.active && !adjacentHex.sameColor(hex)) || !adjacentHex.active)) {
                        renderLineBetweenHexes(adjacentHex, hex, this.batchMovable, (double) this.borderLineThickness, i);
                    }
                }
                if (hex.containsBuilding() || hex.objectInside == 5) {
                    renderSolidObject(this.batchMovable, pos, hex);
                }
            }
        }
        renderResponseAnimHex();
        if (this.gameController.selectionController.selectedUnit != null || this.gameController.selectionController.tipFactor.get() > 0.0f || this.gameController.fieldController.moveZoneFactor.get() > 0.0f) {
            for (k = this.gameController.fieldController.moveZone.size() - 1; k >= 0; k--) {
                hex = (Hex) this.gameController.fieldController.moveZone.get(k);
                for (i = 0; i < 6; i++) {
                    h = hex.getAdjacentHex(i);
                    if (!(h == null || h.isEmptyHex() || (h.active && h.inMoveZone == hex.inMoveZone))) {
                        renderLineBetweenHexesWithOffset(hex, h, this.batchMovable, (((double) this.gameController.fieldController.moveZoneFactor.get()) * 0.02d) * ((double) this.f100w), this.moveZonePixel, ((-(1.0d - ((double) this.gameController.fieldController.moveZoneFactor.get()))) * 0.01d) * ((double) this.f100w), i, (double) this.gameController.fieldController.moveZoneFactor.get());
                    }
                }
                if (hex.containsUnit()) {
                    renderUnit(this.batchMovable, hex.unit);
                }
                if (hex.containsTree()) {
                    renderSolidObject(this.batchMovable, hex.pos, hex);
                }
            }
        }
        if (this.gameController.fieldController.selectedHexes.size() != 0) {
            this.batchMovable.setColor(c.f39r, c.f38g, c.f37b, 1.0f - this.gameController.fieldController.moveZoneFactor.get());
            for (k = this.gameController.fieldController.moveZone.size() - 1; k >= 0; k--) {
                hex = (Hex) this.gameController.fieldController.moveZone.get(k);
                for (i = 0; i < 6; i++) {
                    h = hex.getAdjacentHex(i);
                    if (!(h == null || h.isEmptyHex() || (h.active && h.sameColor(hex)))) {
                        renderLineBetweenHexesWithOffset(hex, h, this.batchMovable, (((double) hex.selectionFactor.get()) * 0.01d) * ((double) this.f100w), this.selectionBorder, ((-(1.0d - ((double) hex.selectionFactor.get()))) * 0.01d) * ((double) this.f100w), i, (double) hex.selectionFactor.get());
                    }
                }
            }
        }
        this.batchMovable.setColor(c.f39r, c.f38g, c.f37b, c.f36a);
    }

    private void renderSelectedUnit() {
        if (this.gameController.selectionController.selectedUnit != null) {
            PointYio pos = this.gameController.selectionController.selectedUnit.currentPos;
            float ar = (0.35f * this.hexViewSize) * this.gameController.selectionController.selUnitFactor.get();
            this.batchMovable.draw(this.selUnitShadow, (pos.f144x - (this.hexViewSize * 0.7f)) - (2.0f * ar), (pos.f145y - (this.hexViewSize * 0.6f)) - (2.0f * ar), (this.hexViewSize * 1.4f) + (4.0f * ar), (this.hexViewSize * 1.6f) + (4.0f * ar));
            this.batchMovable.draw(this.manTextures[this.gameController.selectionController.selectedUnit.strength - 1].getNormal(), (pos.f144x - (this.hexViewSize * 0.7f)) - ar, (pos.f145y - (this.hexViewSize * 0.6f)) - ar, (this.hexViewSize * 1.4f) + (2.0f * ar), (this.hexViewSize * 1.6f) + (2.0f * ar));
        }
    }

    private void renderDebug() {
    }

    private void renderBlackout() {
        if (((double) this.gameController.fieldController.moveZoneFactor.get()) >= 0.01d) {
            Color c = this.batchMovable.getColor();
            this.batchMovable.setColor(c.f39r, c.f38g, c.f37b, 0.5f * this.gameController.selectionController.getBlackoutFactor().get());
            GraphicsYio.drawByRectangle(this.batchMovable, this.blackPixel, this.gameController.cameraController.frame);
            this.batchMovable.setColor(c.f39r, c.f38g, c.f37b, c.f36a);
        }
    }

    private void renderCacheLevelTextures() {
        this.batchMovable.draw(this.cacheLevelTextures[0], 0.0f, 0.0f);
        if (this.gameController.fieldController.levelSize >= 2) {
            this.batchMovable.draw(this.cacheLevelTextures[1], (float) this.f100w, 0.0f);
            if (this.gameController.fieldController.levelSize >= 4) {
                this.batchMovable.draw(this.cacheLevelTextures[2], 0.0f, (float) this.f99h);
                this.batchMovable.draw(this.cacheLevelTextures[3], (float) this.f100w, (float) this.f99h);
            }
        }
    }

    private void renderDefenseTips() {
        float f = this.gameController.fieldController.defenseTipFactor.get();
        if (f != 0.0f) {
            ArrayList<Hex> defenseTips = this.gameController.fieldController.defenseTips;
            if (defenseTips.size() != 0) {
                Color c = this.batchMovable.getColor();
                this.batchMovable.setColor(c.f39r, c.f38g, c.f37b, f);
                Iterator it = defenseTips.iterator();
                while (it.hasNext()) {
                    PointYio cPos;
                    float x;
                    float y;
                    float size;
                    Hex defenseTip = (Hex) it.next();
                    PointYio tipPos = defenseTip.getPos();
                    Hex defSrcHex = this.gameController.selectionController.getDefSrcHex(defenseTip);
                    if (defSrcHex != null) {
                        cPos = defSrcHex.getPos();
                    } else {
                        cPos = this.gameController.fieldController.defTipHex.getPos();
                    }
                    if (this.gameController.fieldController.defenseTipFactor.getDy() >= 0.0d) {
                        x = cPos.f144x + ((tipPos.f144x - cPos.f144x) * f);
                        y = cPos.f145y + ((tipPos.f145y - cPos.f145y) * f);
                        size = (0.5f + (0.1f * f)) * this.hexViewSize;
                    } else {
                        x = tipPos.f144x;
                        y = tipPos.f145y;
                        size = (0.7f - (0.1f * f)) * this.hexViewSize;
                    }
                    drawFromCenter(this.batchMovable, this.defenseIcon, (double) x, (double) y, (double) size);
                }
                this.batchMovable.setColor(c.f39r, c.f38g, c.f37b, c.f36a);
            }
        }
    }

    private void renderInternals() {
        this.grManager.renderFogOfWar.beginFog();
        this.batchMovable.begin();
        this.grManager.renderFogOfWar.continueFog();
        renderCacheLevelTextures();
        if (YioGdxGame.isScreenVerySmall()) {
            renderAllSolidObjects();
        }
        renderAnimHexes();
        renderSelectedHexes();
        renderExclamationMarks();
        renderResponseAnimHex();
        renderCertainUnits();
        renderBlackout();
        renderMoveZone();
        this.grManager.render();
        renderCityNames();
        renderSelectedUnit();
        renderDefenseTips();
        renderDebug();
        this.batchMovable.end();
        renderForefinger();
    }

    public void render() {
        if (((double) this.factorModel.get()) >= 0.01d) {
            if (this.factorModel.get() < 1.0f) {
                renderTransitionFrame();
            } else {
                if (this.gameController.backgroundVisible) {
                    this.batchSolid.begin();
                    this.batchSolid.draw(this.blackPixel, 0.0f, 0.0f, (float) this.f100w, (float) this.f99h);
                    this.batchSolid.end();
                }
                renderInternals();
            }
            Fonts.gameFont.setColor(Color.WHITE);
            renderMoney();
            renderTip();
        }
    }

    private void renderMoney() {
        this.smFactor = this.gameController.selectionController.getSelMoneyFactor().get();
        this.smDelta = (((float) this.f99h) * 0.1f) * (1.0f - this.smFactor);
        if (this.smFactor > 0.0f) {
            this.batchSolid.begin();
            this.batchSolid.draw(this.sideShadow, (float) this.f100w, ((float) this.f99h) + this.smDelta, 0.0f, 0.0f, (float) this.f100w, 0.1f * ((float) this.f99h), 1.0f, 1.0f, 180.0f);
            this.batchSolid.draw(this.sideShadow, 0.0f, (-this.smDelta) + 0.0f, (float) this.f100w, 0.1f * ((float) this.f99h));
            Fonts.gameFont.draw(this.batchSolid, "" + this.gameController.fieldController.selectedProvinceMoney, 0.12f * ((float) this.f100w), (1.08f - (this.smFactor * 0.1f)) * ((float) this.f99h));
            Fonts.gameFont.draw(this.batchSolid, this.gameController.balanceString, 0.47f * ((float) this.f100w), (1.08f - (this.smFactor * 0.1f)) * ((float) this.f99h));
            this.batchSolid.end();
        }
    }

    private void renderTransitionFrame() {
        this.batchSolid.begin();
        Color c = this.batchSolid.getColor();
        float cx = (float) (this.f100w / 2);
        float cy = (float) (this.f99h / 2);
        float fw = this.factorModel.get() * cx;
        float fh = this.factorModel.get() * cy;
        this.batchSolid.setColor(c.f39r, c.f38g, c.f37b, this.factorModel.get());
        this.batchSolid.draw(getTransitionTexture(), cx - fw, cy - fh, 2.0f * fw, 2.0f * fh);
        this.batchSolid.setColor(c.f39r, c.f38g, c.f37b, c.f36a);
        this.batchSolid.end();
    }

    private TextureRegion getTransitionTexture() {
        if (this.animationTextureRegion != null) {
            return this.animationTextureRegion;
        }
        return this.backgroundRegion;
    }

    private void renderTip() {
        if (((double) this.gameController.selectionController.tipFactor.get()) > 0.01d) {
            this.batchSolid.begin();
            float s = 0.2f * ((float) this.f100w);
            this.batchSolid.draw(getTipTypeTexture(this.gameController.selectionController.tipShowType), (((float) this.f100w) * 0.5f) - (0.5f * s), getTipVerticalPos(s), s, s);
            Fonts.gameFont.draw(this.batchSolid, this.gameController.currentPriceString, (((float) this.f100w) * 0.5f) - (this.gameController.priceStringWidth * 0.5f), getTipVerticalPos(s));
            this.batchSolid.end();
        }
    }

    private float getTipVerticalPos(float s) {
        if (Settings.fastConstruction) {
            return ((this.gameController.selectionController.tipFactor.get() - 1.0f) * s) + (0.12f * ((float) this.f99h));
        }
        return ((this.gameController.selectionController.tipFactor.get() - 1.0f) * s) + (0.04f * ((float) this.f99h));
    }

    private TextureRegion getTipTypeTexture(int tipShowType) {
        switch (tipShowType) {
            case 1:
                return this.manTextures[0].getNormal();
            case 2:
                return this.manTextures[1].getNormal();
            case 3:
                return this.manTextures[2].getNormal();
            case 4:
                return this.manTextures[3].getNormal();
            case 5:
                return this.farmTexture[0].getNormal();
            case 6:
                return this.strongTowerTexture.getNormal();
            case 7:
                return this.pineTexture.getNormal();
            default:
                return this.towerTexture.getNormal();
        }
    }

    public void moveInsideStuff() {
        if (((double) this.gameController.cameraController.viewZoomLevel) < this.zoomLevelOne) {
            this.currentZoomQuality = 2;
        } else if (((double) this.gameController.cameraController.viewZoomLevel) < this.zoomLevelTwo) {
            this.currentZoomQuality = 1;
        } else {
            this.currentZoomQuality = 0;
        }
    }

    public void moveFactors() {
        this.factorModel.move();
    }

    private static void drawLine(double x1, double y1, double x2, double y2, double thickness, SpriteBatch spriteBatch, TextureRegion blackPixel) {
        spriteBatch.draw(blackPixel, (float) x1, (float) (y1 - (0.5d * thickness)), 0.0f, ((float) thickness) * 0.5f, (float) Yio.distance(x1, y1, x2, y2), (float) thickness, 1.0f, 1.0f, (float) (57.29577951308232d * Yio.angle(x1, y1, x2, y2)));
    }

    public boolean coversAllScreen() {
        return this.factorModel.get() == 1.0f;
    }

    public boolean isInMotion() {
        return this.factorModel.get() > 0.0f && this.factorModel.get() < 1.0f;
    }
}
