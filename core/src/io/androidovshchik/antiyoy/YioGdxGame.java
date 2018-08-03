package io.androidovshchik.antiyoy;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.StringTokenizer;
import io.androidovshchik.antiyoy.factor_yio.FactorYio;
import io.androidovshchik.antiyoy.gameplay.DebugFlags;
import io.androidovshchik.antiyoy.gameplay.GameController;
import io.androidovshchik.antiyoy.gameplay.RefuseStatistics;
import io.androidovshchik.antiyoy.gameplay.campaign.CampaignLevelFactory;
import io.androidovshchik.antiyoy.gameplay.campaign.CampaignProgressManager;
import io.androidovshchik.antiyoy.gameplay.diplomacy.DiplomacyInfoCondensed;
import io.androidovshchik.antiyoy.gameplay.game_view.GameView;
import io.androidovshchik.antiyoy.gameplay.loading.LoadingManager;
import io.androidovshchik.antiyoy.gameplay.loading.LoadingParameters;
import io.androidovshchik.antiyoy.gameplay.name_generator.CityNameGenerator;
import io.androidovshchik.antiyoy.gameplay.replays.ReplaySaveSystem;
import io.androidovshchik.antiyoy.gameplay.rules.GameRules;
import io.androidovshchik.antiyoy.gameplay.user_levels.UserLevelFactory;
import io.androidovshchik.antiyoy.gameplay.user_levels.UserLevelProgressManager;
import io.androidovshchik.antiyoy.menu.ButtonYio;
import io.androidovshchik.antiyoy.menu.MenuControllerYio;
import io.androidovshchik.antiyoy.menu.MenuViewYio;
import io.androidovshchik.antiyoy.menu.SingleMessages;
import io.androidovshchik.antiyoy.menu.save_slot_selector.SaveSystem;
import io.androidovshchik.antiyoy.menu.scenes.Scenes;
import io.androidovshchik.antiyoy.stuff.Fonts;
import io.androidovshchik.antiyoy.stuff.FrameBufferYio;
import io.androidovshchik.antiyoy.stuff.GraphicsYio;
import io.androidovshchik.antiyoy.stuff.LanguagesManager;
import io.androidovshchik.antiyoy.stuff.Yio;

public class YioGdxGame extends ApplicationAdapter implements InputProcessor {
    public static boolean ANDROID = false;
    private static GlyphLayout glyphLayout = new GlyphLayout();
    public static final Random random = new Random();
    public static float screenRatio;
    static boolean screenVerySmall;
    private boolean alreadyShownErrorMessageOnce;
    public float animRadius;
    public float animX;
    public float animY;
    ArrayList<Integer> backButtonIds;
    public int[] balanceIndicator;
    public SpriteBatch batch;
    double bubbleGravity;
    public CampaignLevelFactory campaignLevelFactory;
    TextureRegion currentBackground;
    private int currentBackgroundIndex;
    public int currentBubbleIndex;
    private int currentFrameCount;
    boolean debugFactorModel;
    ArrayList<Float> debugValues;
    public float defaultBubbleRadius;
    private int fps;
    private FrameBuffer frameBuffer;
    private int frameSkipCount;
    public GameController gameController;
    public boolean gamePaused;
    public GameView gameView;
    public int f148h;
    boolean ignoreDrag;
    boolean ignoreNextTimeCorrection;
    TextureRegion infoBackground;
    TextureRegion lastBackground;
    private long lastTimeButtonPressed;
    boolean loadedResources;
    TextureRegion mainBackground;
    public MenuControllerYio menuControllerYio;
    public MenuViewYio menuViewYio;
    private final OnKeyActions onKeyActions = new OnKeyActions(this);
    TextureRegion pauseBackground;
    public float pressX;
    public float pressY;
    public boolean readyToUnPause;
    public SaveSystem saveSystem;
    public int selectedLevelIndex;
    TextureRegion settingsBackground;
    public ShapeRenderer shapeRenderer;
    public boolean simpleTransitionAnimation;
    TextureRegion splash;
    public int splashCount;
    final SplatController splatController = new SplatController(this);
    public boolean startedExitProcess;
    private long timeToUnPause;
    long timeToUpdateFpsInfo;
    private FactorYio transitionFactor;
    public boolean useMenuMasks;
    public int f149w;

    public void create() {
        this.loadedResources = false;
        this.splashCount = 0;
        this.batch = new SpriteBatch();
        this.shapeRenderer = new ShapeRenderer();
        this.splash = GameView.loadTextureRegion("splash.png", true);
        this.f149w = Gdx.graphics.getWidth();
        this.f148h = Gdx.graphics.getHeight();
        this.pressX = ((float) this.f149w) * 0.5f;
        this.pressY = ((float) this.f148h) * 0.5f;
        this.frameSkipCount = 50;
        screenRatio = ((float) this.f149w) / ((float) this.f148h);
        this.frameBuffer = FrameBufferYio.getInstance(Format.RGB565, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        this.balanceIndicator = new int[GameRules.colorNumber];
        initDebugValues();
        this.backButtonIds = new ArrayList();
        this.useMenuMasks = true;
    }

    private void initDebugValues() {
        this.debugFactorModel = false;
        this.debugValues = new ArrayList();
        if (this.debugFactorModel) {
            FactorYio factorYio = new FactorYio();
            factorYio.setValues(0.0d, 0.0d);
            factorYio.appear(4, 1.0d);
            int c = 100;
            while (factorYio.hasToMove() && c > 0) {
                this.debugValues.add(Float.valueOf(factorYio.get()));
                factorYio.move();
                c--;
            }
        }
    }

    private void generalInitialization() {
        long time1 = System.currentTimeMillis();
        this.loadedResources = true;
        this.startedExitProcess = false;
        screenVerySmall = ((double) Gdx.graphics.getDensity()) < 1.2d;
        initializeSingletons();
        loadSomeTextures();
        SoundControllerYio.loadAllSounds();
        MusicManager.getInstance().load();
        this.transitionFactor = new FactorYio();
        this.splatController.splatTransparencyFactor = new FactorYio();
        this.splatController.initSplats();
        Fonts.initFonts();
        CityNameGenerator.getInstance().load();
        DiplomacyInfoCondensed.onGeneralInitialization();
        this.gamePaused = true;
        this.alreadyShownErrorMessageOnce = false;
        this.fps = 0;
        this.timeToUpdateFpsInfo = System.currentTimeMillis() + 1000;
        loadProgress();
        SingleMessages.load();
        this.menuControllerYio = new MenuControllerYio(this);
        this.menuViewYio = new MenuViewYio(this);
        this.gameController = new GameController(this);
        this.saveSystem = new SaveSystem(this.gameController);
        this.gameView = new GameView(this);
        this.gameView.factorModel.destroy(1, 1.0d);
        this.campaignLevelFactory = new CampaignLevelFactory(this.gameController);
        this.currentBackgroundIndex = -1;
        this.currentBackground = this.gameView.blackPixel;
        beginBackgroundChange(0, true, false);
        this.defaultBubbleRadius = 0.02f * ((float) this.f149w);
        this.bubbleGravity = 2.5E-4d * ((double) this.f149w);
        this.splatController.revealSplats();
        Gdx.input.setInputProcessor(this);
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Settings.getInstance().setYioGdxGame(this);
        Settings.getInstance().loadSettings();
        checkTemporaryFlags();
        ReplaySaveSystem.resetInstance();
        ReplaySaveSystem.getInstance().setGameController(this.gameController);
        say("full loading time: " + (System.currentTimeMillis() - time1));
        checkForSingleMessageOnStart();
    }

    private void checkForSingleMessageOnStart() {
        if (OneTimeInfo.getInstance().bleentoroRelease) {
            OneTimeInfo.getInstance().bleentoroRelease = false;
            OneTimeInfo.getInstance().save();
            this.currentBackground = this.gameView.blackPixel;
            this.currentBackgroundIndex = 4;
            Scenes.sceneBleentoroRelease.create();
        }
    }

    private void initializeSingletons() {
        CampaignProgressManager.initialize();
        CityNameGenerator.initialize();
        LanguagesManager.initialize();
        LoadingManager.initialize();
        LoadingParameters.initialize();
        MusicManager.initialize();
        OneTimeInfo.initialize();
        RefuseStatistics.initialize();
        Settings.initialize();
        UserLevelFactory.initialize();
        UserLevelProgressManager.initialize();
    }

    private void loadSomeTextures() {
        this.mainBackground = GameView.loadTextureRegion("main_menu_background.png", true);
        this.infoBackground = GameView.loadTextureRegion("info_background.png", true);
        this.settingsBackground = GameView.loadTextureRegion("settings_background.png", true);
        this.pauseBackground = GameView.loadTextureRegion("pause_background.png", true);
        this.splatController.splatTexture = GameView.loadTextureRegion("splat.png", true);
    }

    private void loadProgress() {
        this.selectedLevelIndex = Gdx.app.getPreferences("main").getInteger("progress", 1);
        if (this.selectedLevelIndex > CampaignProgressManager.getIndexOfLastLevel()) {
            this.selectedLevelIndex = CampaignProgressManager.getIndexOfLastLevel();
        }
    }

    private void checkTemporaryFlags() {
        Preferences prefs = Gdx.app.getPreferences("temporary_flags");
    }

    private void initSplats() {
        this.splatController.initSplats();
    }

    public void setGamePaused(boolean gamePaused) {
        if (gamePaused && !this.gamePaused) {
            this.gamePaused = true;
            this.gameController.selectionController.deselectAll();
            this.splatController.revealSplats();
            Fonts.gameFont.setColor(Color.BLACK);
            this.menuControllerYio.forceDyingButtonsToEnd();
        } else if (!gamePaused && this.gamePaused) {
            unPauseAfterSomeTime();
            beginBackgroundChange(4, true, true);
            this.splatController.hideSplats();
            Fonts.gameFont.setColor(Color.WHITE);
        }
    }

    public void beginBackgroundChange(int index, boolean updateAnimPos, boolean simpleTransition) {
        if (this.currentBackgroundIndex != index || index != 4) {
            this.simpleTransitionAnimation = simpleTransition;
            this.currentBackgroundIndex = index;
            this.lastBackground = this.currentBackground;
            if (updateAnimPos) {
                this.animX = this.pressX;
                this.animY = this.pressY;
                float r2 = (float) Yio.distance((double) this.animX, (double) this.animY, (double) this.f149w, 0.0d);
                float r3 = (float) Yio.distance((double) this.animX, (double) this.animY, 0.0d, (double) this.f148h);
                float r4 = (float) Yio.distance((double) this.animX, (double) this.animY, (double) this.f149w, (double) this.f148h);
                this.animRadius = (float) Yio.distance((double) this.animX, (double) this.animY, 0.0d, 0.0d);
                if (r2 > this.animRadius) {
                    this.animRadius = r2;
                }
                if (r3 > this.animRadius) {
                    this.animRadius = r3;
                }
                if (r4 > this.animRadius) {
                    this.animRadius = r4;
                }
            }
            switch (index) {
                case 0:
                    this.currentBackground = this.mainBackground;
                    break;
                case 1:
                    this.currentBackground = this.infoBackground;
                    break;
                case 2:
                    this.currentBackground = this.settingsBackground;
                    break;
                case 3:
                    this.currentBackground = this.pauseBackground;
                    break;
                case 4:
                    this.currentBackground = this.gameView.blackPixel;
                    break;
            }
            this.transitionFactor.setValues(0.02d, 0.01d);
            this.transitionFactor.appear(0, 0.8d);
        }
    }

    private void checkToUseMenuMasks() {
        if (this.useMenuMasks) {
            if (this.gameView.factorModel.get() == 1.0f && this.gameView.factorModel.getDy() == 0.0d) {
                this.useMenuMasks = false;
            }
        } else if (this.gameView.factorModel.get() < 1.0f) {
            this.useMenuMasks = true;
        } else {
            ButtonYio buttonYio = this.menuControllerYio.getButtonById(30);
            if (buttonYio != null && buttonYio.isCurrentlyTouched()) {
                this.useMenuMasks = true;
            }
        }
    }

    private void move() {
        if (this.loadedResources) {
            this.transitionFactor.move();
            this.gameController.selectionController.getSelMoneyFactor().move();
            checkToUnPause();
            if (!this.gamePaused) {
                this.gameView.moveInsideStuff();
                this.gameController.move();
                if (((double) this.gameView.factorModel.get()) < 0.95d) {
                    say("game not paused but game view is not visible");
                }
            }
            this.gameView.moveFactors();
            this.menuControllerYio.move();
            if (this.loadedResources) {
                checkToUseMenuMasks();
                this.splatController.moveSplats();
            }
        }
    }

    private void checkToUnPause() {
        if (this.readyToUnPause && System.currentTimeMillis() > this.timeToUnPause && this.gameView.coversAllScreen()) {
            this.gamePaused = false;
            this.readyToUnPause = false;
            this.gameController.resetCurrentTouchCount();
            this.frameSkipCount = 10;
        }
    }

    private void moveSplats() {
        this.splatController.moveSplats();
    }

    private void renderDebugValues() {
    }

    private void drawBackground(TextureRegion textureRegion) {
        this.batch.begin();
        this.batch.draw(textureRegion, 0.0f, 0.0f, (float) this.f149w, (float) this.f148h);
        this.batch.end();
    }

    private void renderMenuLayersWhenNothingIsMoving() {
        Color c = this.batch.getColor();
        this.batch.setColor(c.f39r, c.f38g, c.f37b, 1.0f);
        this.batch.begin();
        this.batch.draw(this.currentBackground, 0.0f, 0.0f, (float) this.f149w, (float) this.f148h);
        this.splatController.renderSplats(c);
        this.batch.end();
    }

    private void renderMenuLayersWhenUsualAnimation() {
        Color c = this.batch.getColor();
        this.batch.setColor(c.f39r, c.f38g, c.f37b, 1.0f);
        drawBackground(this.lastBackground);
        if (this.simpleTransitionAnimation) {
            this.batch.setColor(c.f39r, c.f38g, c.f37b, 0.0f + this.transitionFactor.get());
            drawBackground(this.currentBackground);
        } else {
            float f = 0.0f + this.transitionFactor.get();
            maskingBegin();
            this.shapeRenderer.begin(ShapeType.Filled);
            this.shapeRenderer.circle(this.animX, this.animY, this.animRadius * f, 32);
            this.shapeRenderer.end();
            maskingContinue();
            drawBackground(this.currentBackground);
            maskingEnd();
        }
        this.batch.begin();
        this.splatController.renderSplats(c);
        this.batch.end();
        this.menuViewYio.render(false, true);
    }

    private void renderMenuWhenGameViewNotVisible() {
        if (this.transitionFactor.get() == 1.0f) {
            renderMenuLayersWhenNothingIsMoving();
        } else {
            renderMenuLayersWhenUsualAnimation();
        }
    }

    private void renderInternals() {
        this.currentFrameCount++;
        if (DebugFlags.showFpsInfo && System.currentTimeMillis() > this.timeToUpdateFpsInfo) {
            this.timeToUpdateFpsInfo = System.currentTimeMillis() + 1000;
            this.fps = this.currentFrameCount;
            this.currentFrameCount = 0;
        }
        if (this.debugFactorModel) {
            renderDebugValues();
            return;
        }
        if (!this.gameView.coversAllScreen()) {
            renderMenuWhenGameViewNotVisible();
        }
        this.gameView.render();
        if (this.gamePaused) {
            this.menuViewYio.render(true, false);
        } else {
            this.menuViewYio.render(true, true);
        }
        if (DebugFlags.showFpsInfo) {
            this.batch.begin();
            Fonts.gameFont.draw(this.batch, "" + Math.min(this.fps, 60), 0.02f * GraphicsYio.width, 0.85f * GraphicsYio.height);
            this.batch.end();
        }
    }

    private void renderSplats(Color c) {
        this.splatController.renderSplats(c);
    }

    public static void maskingBegin() {
        Gdx.gl.glClearDepthf(1.0f);
        Gdx.gl.glClear(256);
        Gdx.gl.glDepthFunc(GL20.GL_LESS);
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glDepthMask(true);
        Gdx.gl.glColorMask(false, false, false, false);
    }

    public static void maskingContinue() {
        Gdx.gl.glColorMask(true, true, true, true);
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glDepthFunc(GL20.GL_EQUAL);
    }

    public static void maskingEnd() {
        Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
    }

    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (this.loadedResources) {
            try {
                move();
            } catch (Exception exception) {
                if (!this.alreadyShownErrorMessageOnce) {
                    exception.printStackTrace();
                    this.alreadyShownErrorMessageOnce = true;
                    Scenes.sceneExceptionReport.create(exception);
                }
            }
            if (this.gamePaused) {
                renderInternals();
                return;
            }
            if (((double) Gdx.graphics.getDeltaTime()) < 0.025d || this.frameSkipCount >= 2) {
                this.frameSkipCount = 0;
                this.frameBuffer.begin();
                renderInternals();
                this.frameBuffer.end();
            } else {
                this.frameSkipCount++;
            }
            this.batch.begin();
            this.batch.draw((Texture) this.frameBuffer.getColorBufferTexture(), 0.0f, 0.0f, (float) Gdx.graphics.getWidth(), (float) Gdx.graphics.getHeight(), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false, true);
            this.batch.end();
            return;
        }
        this.batch.begin();
        this.batch.draw(this.splash, 0.0f, 0.0f, (float) this.f149w, (float) this.f148h);
        this.batch.end();
        if (this.splashCount == 2) {
            generalInitialization();
        }
        this.splashCount++;
    }

    public static String getDifficultyNameByPower(int difficulty) {
        LanguagesManager languagesManager = LanguagesManager.getInstance();
        switch (difficulty) {
            case 0:
                return languagesManager.getString("easy");
            case 1:
                return languagesManager.getString("normal");
            case 2:
                return languagesManager.getString("hard");
            case 3:
                return languagesManager.getString("expert");
            case 4:
                return languagesManager.getString("balancer");
            default:
                return null;
        }
    }

    private void unPauseAfterSomeTime() {
        this.readyToUnPause = true;
        this.timeToUnPause = System.currentTimeMillis() + 450;
    }

    public void setAnimToPlayButtonSpecial() {
        ButtonYio buttonYio = this.menuControllerYio.getButtonById(3);
        if (buttonYio != null) {
            this.animX = buttonYio.cx;
            this.animY = buttonYio.cy;
        } else {
            this.animX = (float) (this.f149w / 2);
            this.animY = (float) (this.f148h / 2);
        }
        this.transitionFactor.setValues(0.15d, 0.0d);
    }

    public void setAnimToResumeButtonSpecial() {
        this.animX = (float) this.f149w;
        this.animY = (float) this.f148h;
        this.animRadius = (float) Yio.distance(0.0d, 0.0d, (double) this.f149w, (double) this.f148h);
    }

    public void setAnimToStartButtonSpecial() {
        this.animX = 0.5f * ((float) this.f149w);
        this.animY = 0.65f * ((float) this.f148h);
        this.animRadius = this.animY;
    }

    public static boolean isScreenVerySmall() {
        return screenVerySmall;
    }

    public static boolean isScreenVeryWide() {
        return ((double) (((float) Gdx.graphics.getHeight()) / ((float) Gdx.graphics.getWidth()))) < 1.51d;
    }

    public void forceBackgroundChange() {
        this.transitionFactor.setValues(1.0d, 0.0d);
        this.simpleTransitionAnimation = true;
    }

    public static void say(String text) {
        System.out.println(text);
    }

    public void restartGame() {
        if (GameRules.campaignMode) {
            this.campaignLevelFactory.createCampaignLevel(CampaignProgressManager.getInstance().currentLevelIndex);
            return;
        }
        this.gameController.restartGame();
    }

    void increaseLevelSelection() {
        setSelectedLevelIndex(this.selectedLevelIndex + 1);
    }

    public static ArrayList<String> decodeStringToArrayList(String string, String delimiters) {
        ArrayList<String> res = new ArrayList();
        StringTokenizer tokenizer = new StringTokenizer(string, delimiters);
        while (tokenizer.hasMoreTokens()) {
            res.add(tokenizer.nextToken());
        }
        return res;
    }

    public int getSelectedLevelIndex() {
        return this.selectedLevelIndex;
    }

    public void setSelectedLevelIndex(int selectedLevelIndex) {
        if (selectedLevelIndex >= 0 && selectedLevelIndex <= CampaignProgressManager.getIndexOfLastLevel()) {
            this.selectedLevelIndex = selectedLevelIndex;
        }
    }

    public void pressButtonIfVisible(int id) {
        ButtonYio button = this.menuControllerYio.getButtonById(id);
        if (button != null && button.isVisible() && button.appearFactor.get() == 1.0f) {
            button.press();
        }
    }

    public void registerBackButtonId(int id) {
        Iterator it = this.backButtonIds.iterator();
        while (it.hasNext()) {
            if (((Integer) it.next()).intValue() == id) {
                return;
            }
        }
        this.backButtonIds.add(Integer.valueOf(id));
    }

    public void onEndCreation() {
        this.gameView.updateCacheLevelTextures();
        this.menuControllerYio.removeButtonById(38);
        this.gameView.beginSpawnProcess();
        setGamePaused(false);
    }

    public boolean keyDown(int keycode) {
        return this.onKeyActions.onKeyPressed(keycode);
    }

    public boolean keyUp(int keycode) {
        return false;
    }

    public boolean keyTyped(char character) {
        return false;
    }

    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        this.ignoreDrag = true;
        this.pressX = (float) screenX;
        this.pressY = (float) (this.f148h - screenY);
        try {
            if (this.gameView.isInMotion() || ((double) this.transitionFactor.get()) <= 0.99d || !this.menuControllerYio.touchDown(screenX, Gdx.graphics.getHeight() - screenY, pointer, button)) {
                this.ignoreDrag = false;
                if (!this.gamePaused) {
                    this.gameController.touchDown(screenX, Gdx.graphics.getHeight() - screenY, pointer, button);
                }
                return false;
            }
            this.lastTimeButtonPressed = System.currentTimeMillis();
            return false;
        } catch (Exception exception) {
            if (!this.alreadyShownErrorMessageOnce) {
                exception.printStackTrace();
                this.alreadyShownErrorMessageOnce = true;
                Scenes.sceneExceptionReport.create(exception);
            }
        }
    }

    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        try {
            this.menuControllerYio.touchUp(screenX, Gdx.graphics.getHeight() - screenY, pointer, button);
            if (!this.gamePaused && this.gameView.coversAllScreen()) {
                this.gameController.touchUp(screenX, Gdx.graphics.getHeight() - screenY, pointer, button);
            }
        } catch (Exception exception) {
            if (!this.alreadyShownErrorMessageOnce) {
                exception.printStackTrace();
                this.alreadyShownErrorMessageOnce = true;
                Scenes.sceneExceptionReport.create(exception);
            }
        }
        return false;
    }

    public boolean touchDragged(int screenX, int screenY, int pointer) {
        this.menuControllerYio.touchDragged(screenX, Gdx.graphics.getHeight() - screenY, pointer);
        if (!(this.ignoreDrag || this.gamePaused || !this.gameView.coversAllScreen())) {
            this.gameController.touchDragged(screenX, Gdx.graphics.getHeight() - screenY, pointer);
        }
        return false;
    }

    public int gamesPlayed() {
        int s = 0;
        for (int i : this.balanceIndicator) {
            s += i;
        }
        return s;
    }

    private String getBalanceIndicatorAsString(int[] array) {
        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append("[");
        for (int i = 0; i < GameRules.colorNumber; i++) {
            stringBuffer.append(" ").append(array[i]);
        }
        stringBuffer.append(" ]");
        return stringBuffer.toString();
    }

    public String getBalanceIndicatorString() {
        double D = 0.0d;
        int max = this.balanceIndicator[0];
        int min = this.balanceIndicator[0];
        for (int i = 0; i < GameRules.colorNumber; i++) {
            if (this.balanceIndicator[i] > max) {
                max = this.balanceIndicator[i];
            }
            if (this.balanceIndicator[i] < min) {
                min = this.balanceIndicator[i];
            }
        }
        if (max > 0) {
            D = 1.0d - (((double) min) / ((double) max));
        }
        String dStr = Double.toString(D);
        if (dStr.length() > 4) {
            dStr = dStr.substring(0, 4);
        }
        return getBalanceIndicatorAsString(this.balanceIndicator) + " = " + dStr;
    }

    public static float getTextWidth(BitmapFont font, String text) {
        glyphLayout.setText(font, text);
        return glyphLayout.width;
    }

    static float maxElement(ArrayList<Float> list) {
        if (list.size() == 0) {
            return 0.0f;
        }
        float max = ((Float) list.get(0)).floatValue();
        for (int i = 1; i < list.size(); i++) {
            if (((Float) list.get(i)).floatValue() > max) {
                max = ((Float) list.get(i)).floatValue();
            }
        }
        return max;
    }

    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    public boolean scrolled(int amount) {
        if (!this.menuControllerYio.onMouseWheelScrolled(amount) && ((double) this.gameView.factorModel.get()) > 0.1d) {
            this.gameController.scrolled(amount);
        }
        return true;
    }

    public void pause() {
        super.pause();
        if (!this.startedExitProcess) {
            if (this.menuControllerYio != null) {
                this.menuControllerYio.onPause();
            }
            if (this.gameView != null) {
                this.gameView.onPause();
            }
        }
    }

    public void resume() {
        super.resume();
        if (!this.startedExitProcess) {
            if (this.menuControllerYio != null) {
                this.menuControllerYio.onResume();
            }
            if (this.gameView != null) {
                this.gameView.onResume();
            }
        }
    }

    public void close() {
    }
}
