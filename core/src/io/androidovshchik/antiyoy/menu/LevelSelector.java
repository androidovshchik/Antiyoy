package io.androidovshchik.antiyoy.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Matrix4;
import yio.tro.antiyoy.factor_yio.FactorYio;
import yio.tro.antiyoy.gameplay.ClickDetector;
import yio.tro.antiyoy.gameplay.campaign.CampaignLevelFactory;
import yio.tro.antiyoy.gameplay.campaign.CampaignProgressManager;
import yio.tro.antiyoy.menu.render.MenuRender;
import yio.tro.antiyoy.stuff.Fonts;
import yio.tro.antiyoy.stuff.FrameBufferYio;
import yio.tro.antiyoy.stuff.GraphicsYio;
import yio.tro.antiyoy.stuff.PointYio;
import yio.tro.antiyoy.stuff.RectangleYio;
import yio.tro.antiyoy.stuff.tabs_engine.TabsEngineYio;

public class LevelSelector extends InterfaceElement {
    public FactorYio appearFactor;
    public TextureRegion backgroundTexture;
    SpriteBatch batch;
    ClickDetector clickDetector;
    int columnSize;
    public TextureRegion completedIcon;
    PointYio currentTouch;
    public RectangleYio defPos;
    FrameBuffer frameBuffer;
    int f139h;
    public float horOffset;
    public float iconDiameter;
    public float iconRadius;
    float lastTouchX;
    int levelNumber = CampaignProgressManager.getIndexOfLastLevel();
    public TextureRegion[] lockIconTextures;
    MenuControllerYio menuControllerYio;
    public float offsetBetweenPanels;
    public RectangleYio[] positions;
    int rowSize;
    public int selIndexX;
    public int selIndexY;
    public int selectedPanelIndex;
    public FactorYio selectionFactor;
    private boolean slayRulesMode;
    TabsEngineYio tabsEngineYio;
    public TextureRegion[] textures;
    public int touchDownPanelIndex;
    long touchDownTime;
    float touchDownY;
    public TextureRegion[] unlockIconTextures;
    public float verOffset;
    int f140w;

    public LevelSelector(MenuControllerYio menuControllerYio, int id) {
        super(id);
        this.menuControllerYio = menuControllerYio;
        this.f140w = menuControllerYio.yioGdxGame.f149w;
        this.f139h = menuControllerYio.yioGdxGame.f148h;
        this.appearFactor = new FactorYio();
        this.selectionFactor = new FactorYio();
        this.batch = new SpriteBatch();
        this.defPos = new RectangleYio(0.075d * ((double) this.f140w), 0.05d * ((double) this.f139h), 0.85d * ((double) this.f140w), 0.8d * ((double) this.f139h));
        this.iconRadius = (float) (((this.defPos.width / 5.0d) - ((double) (0.01f * ((float) this.f140w)))) / 2.0d);
        this.iconDiameter = 2.0f * this.iconRadius;
        this.offsetBetweenPanels = (float) (this.defPos.width + ((double) (0.05f * ((float) this.f140w))));
        this.slayRulesMode = false;
        this.tabsEngineYio = new TabsEngineYio();
        this.tabsEngineYio.setFriction(0.0d);
        this.tabsEngineYio.setSoftLimitOffset(0.06d * ((double) GraphicsYio.width));
        this.tabsEngineYio.setMagnetMaxPower(0.01d * ((double) GraphicsYio.width));
        this.clickDetector = new ClickDetector();
        this.currentTouch = new PointYio();
        initTextures();
    }

    void initTextures() {
        this.rowSize = (int) (((float) this.defPos.width) / this.iconDiameter);
        this.columnSize = (int) (((float) this.defPos.height) / this.iconDiameter);
        this.horOffset = (((float) this.defPos.width) - (((float) this.rowSize) * this.iconDiameter)) / 2.0f;
        this.verOffset = (((float) this.defPos.height) - (((float) this.columnSize) * this.iconDiameter)) / 2.0f;
        this.selIndexY = this.columnSize - 1;
        int howManyPanels = (this.levelNumber / (this.rowSize * this.columnSize)) + 1;
        this.textures = new TextureRegion[howManyPanels];
        this.positions = new RectangleYio[howManyPanels];
        this.backgroundTexture = GraphicsYio.loadTextureRegion("menu/level_selector/level_selector_background.png", false);
        this.lockIconTextures = new TextureRegion[4];
        this.lockIconTextures[0] = GraphicsYio.loadTextureRegion("menu/level_selector/easy_base.png", true);
        this.lockIconTextures[1] = GraphicsYio.loadTextureRegion("menu/level_selector/normal_base.png", true);
        this.lockIconTextures[2] = GraphicsYio.loadTextureRegion("menu/level_selector/hard_base.png", true);
        this.lockIconTextures[3] = GraphicsYio.loadTextureRegion("menu/level_selector/expert_base.png", true);
        this.unlockIconTextures = new TextureRegion[4];
        this.unlockIconTextures[0] = GraphicsYio.loadTextureRegion("menu/level_selector/unlocked_easy.png", false);
        this.unlockIconTextures[1] = GraphicsYio.loadTextureRegion("menu/level_selector/unlocked_normal.png", false);
        this.unlockIconTextures[2] = GraphicsYio.loadTextureRegion("menu/level_selector/unlocked_hard.png", false);
        this.unlockIconTextures[3] = GraphicsYio.loadTextureRegion("menu/level_selector/unlocked_expert.png", false);
        this.completedIcon = GraphicsYio.loadTextureRegion("menu/level_selector/completed_level_base.png", false);
        for (int i = 0; i < howManyPanels; i++) {
            this.positions[i] = new RectangleYio(((double) ((float) this.defPos.f146x)) + (((double) i) * (((double) ((float) this.defPos.width)) + (0.1d * ((double) this.f140w)))), (double) ((float) this.defPos.f147y), (double) ((float) this.defPos.width), (double) ((float) this.defPos.height));
            renderPanel(i);
        }
    }

    public void renderAllPanels() {
        for (int i = 0; i < this.positions.length; i++) {
            renderPanel(i);
        }
    }

    void renderPanel(int panelIndex) {
        BitmapFont font = Fonts.smallerMenuFont;
        beginRender(panelIndex, font);
        Color color = font.getColor();
        font.setColor(Color.BLACK);
        this.batch.begin();
        for (int i = 0; i < this.rowSize; i++) {
            for (int j = 0; j < this.columnSize; j++) {
                int levelNumber = getLevelNumber(i, j, panelIndex);
                if (levelNumber <= CampaignProgressManager.getIndexOfLastLevel()) {
                    TextureRegion icon;
                    if (CampaignProgressManager.getInstance().isLevelLocked(levelNumber)) {
                        icon = this.lockIconTextures[CampaignLevelFactory.getDifficultyByIndex(levelNumber)];
                    } else if (CampaignProgressManager.getInstance().isLevelComplete(levelNumber)) {
                        icon = this.completedIcon;
                    } else {
                        icon = this.unlockIconTextures[CampaignLevelFactory.getDifficultyByIndex(levelNumber)];
                    }
                    GraphicsYio.drawFromCenter(this.batch, icon, (double) ((this.horOffset + this.iconRadius) + (((float) i) * this.iconDiameter)), (double) ((this.verOffset + this.iconRadius) + (((float) j) * this.iconDiameter)), (double) this.iconRadius);
                    String levelString = getLevelStringNumber(levelNumber);
                    CharSequence charSequence = levelString;
                    font.draw(this.batch, charSequence, ((this.horOffset + this.iconRadius) + (((float) i) * this.iconDiameter)) - (GraphicsYio.getTextWidth(font, levelString) / 2.0f), ((this.verOffset + this.iconRadius) + (((float) j) * this.iconDiameter)) + (0.25f * this.iconRadius));
                }
            }
        }
        this.batch.end();
        font.setColor(color);
        endRender(panelIndex);
    }

    public void updateTextures(int levelIndex) {
        int panelIndex = levelIndex / (this.rowSize * this.columnSize);
        renderPanel(panelIndex);
        int secondPanelIndex = (levelIndex + 1) / (this.rowSize * this.columnSize);
        if (secondPanelIndex != panelIndex) {
            renderPanel(secondPanelIndex);
        }
    }

    private String getLevelStringNumber(int number) {
        if (number == 0) {
            return "?";
        }
        return "" + number;
    }

    int getLevelNumber(int row_x, int column_y, int panelIndex) {
        return (((this.rowSize * panelIndex) * this.columnSize) + (((this.columnSize - 1) - column_y) * this.rowSize)) + row_x;
    }

    void beginRender(int panelIndex, BitmapFont font) {
        this.frameBuffer = FrameBufferYio.getInstance(Format.RGB565, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        ((Texture) this.frameBuffer.getColorBufferTexture()).setFilter(TextureFilter.Linear, TextureFilter.Linear);
        this.frameBuffer.begin();
        Matrix4 matrix4 = new Matrix4();
        int orthoWidth = Gdx.graphics.getWidth();
        int orthoHeight = (orthoWidth / Gdx.graphics.getWidth()) * Gdx.graphics.getHeight();
        matrix4.setToOrtho2D(0.0f, 0.0f, (float) orthoWidth, (float) orthoHeight);
        this.batch.setProjectionMatrix(matrix4);
        this.batch.begin();
        this.batch.draw(this.backgroundTexture, 0.0f, 0.0f, (float) orthoWidth, (float) orthoHeight);
        this.batch.end();
    }

    void endRender(int panelIndex) {
        this.frameBuffer.end();
        this.textures[panelIndex] = new TextureRegion((Texture) this.frameBuffer.getColorBufferTexture(), (int) this.defPos.width, (int) this.defPos.height);
        this.textures[panelIndex].flip(false, true);
    }

    TextureRegion getPanelTexture(int index) {
        return this.textures[index];
    }

    public void move() {
        this.appearFactor.move();
        this.tabsEngineYio.move();
        applyTabEngine();
        applyTransitionAnimation();
        if (this.appearFactor.get() == 1.0f) {
            this.selectionFactor.move();
        }
        checkToPerformAction();
    }

    private void applyTransitionAnimation() {
        if (this.appearFactor.get() != 1.0f && this.appearFactor.get() != 0.0f) {
            float delta = (float) ((0.30000001192092896d * this.defPos.height) * ((double) (1.0f - this.appearFactor.get())));
            for (int i = 0; i < this.positions.length; i++) {
                RectangleYio rectangleYio = this.positions[i];
                rectangleYio.f147y -= (double) delta;
                rectangleYio = this.positions[i];
                rectangleYio.width -= (double) delta;
                rectangleYio = this.positions[i];
                rectangleYio.f146x += (double) (delta / 2.0f);
            }
        }
    }

    private void jumpToTab(int tabIndex) {
        int jumpDelta = tabIndex - this.tabsEngineYio.getCurrentTabIndex();
        if (jumpDelta != 0) {
            if (Math.abs(jumpDelta) == 1) {
                this.tabsEngineYio.swipeTab(jumpDelta);
            }
            if (Math.abs(jumpDelta) == 2) {
                this.tabsEngineYio.swipeTab(jumpDelta);
            }
        }
    }

    private void applyTabEngine() {
        float delta = (float) (-this.tabsEngineYio.getSlider().f107a);
        for (int i = 0; i < this.positions.length; i++) {
            this.positions[i].setBy(this.defPos);
            RectangleYio rectangleYio = this.positions[i];
            rectangleYio.f146x += (double) (GraphicsYio.width * ((float) i));
            rectangleYio = this.positions[i];
            rectangleYio.f146x += (double) delta;
        }
    }

    public FactorYio getFactor() {
        return this.appearFactor;
    }

    public RectangleYio getPosition() {
        return this.defPos;
    }

    public void destroy() {
        this.appearFactor.setDy(0.0d);
        this.appearFactor.destroy(2, 1.4d);
    }

    public void appear() {
        this.appearFactor.setValues(0.03d, 0.0d);
        this.appearFactor.appear(2, 1.4d);
        this.selectionFactor.setValues(0.0d, 0.0d);
        this.selectionFactor.destroy(1, 1.0d);
    }

    public void updateTabsMetrics() {
        this.tabsEngineYio.setLimits(0.0d, (double) (GraphicsYio.width * ((float) this.positions.length)));
        this.tabsEngineYio.setSlider(0.0d, (double) GraphicsYio.width);
        this.tabsEngineYio.setNumberOfTabs(this.positions.length);
    }

    public boolean isVisible() {
        return this.appearFactor.get() > 0.0f;
    }

    public MenuRender getRenderSystem() {
        return MenuRender.renderLevelSelector;
    }

    public void checkToReloadProgress() {
    }

    public boolean checkToPerformAction() {
        if (this.selectionFactor.get() != 1.0f) {
            return false;
        }
        boolean result = this.menuControllerYio.yioGdxGame.campaignLevelFactory.createCampaignLevel(getLevelNumber(this.selIndexX, this.selIndexY, this.selectedPanelIndex));
        CampaignProgressManager.getInstance();
        if (result) {
            this.selectionFactor.setValues(0.99d, 0.0d);
            this.selectionFactor.destroy(0, 0.0d);
            return true;
        }
        this.selectionFactor.setValues(0.99d, 0.0d);
        this.selectionFactor.destroy(1, 2.0d);
        this.menuControllerYio.getButtonById(20).setTouchable(true);
        return true;
    }

    public boolean isTouchable() {
        return true;
    }

    private boolean checkTouchableConditions() {
        if (((double) this.touchDownY) >= this.defPos.f147y && ((double) this.touchDownY) <= this.defPos.f147y + this.defPos.height) {
            return true;
        }
        return false;
    }

    private void updateCurrentTouch(int screenX, int screenY) {
        this.currentTouch.set((double) screenX, (double) screenY);
    }

    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (this.appearFactor.get() < 1.0f) {
            return false;
        }
        updateCurrentTouch(screenX, screenY);
        this.touchDownY = (float) screenY;
        if (!checkTouchableConditions()) {
            return false;
        }
        this.clickDetector.touchDown(this.currentTouch);
        this.tabsEngineYio.onTouchDown();
        this.lastTouchX = (float) screenX;
        this.touchDownPanelIndex = -indexOfPanelThatContainsPoint((float) screenX, (float) screenY);
        this.touchDownTime = System.currentTimeMillis();
        return true;
    }

    public boolean touchDrag(int screenX, int screenY, int pointer) {
        if (this.appearFactor.get() < 1.0f) {
            return false;
        }
        updateCurrentTouch(screenX, screenY);
        if (!checkTouchableConditions()) {
            return false;
        }
        this.tabsEngineYio.setSpeed((double) (this.lastTouchX - ((float) screenX)));
        this.clickDetector.touchDrag(this.currentTouch);
        this.lastTouchX = (float) screenX;
        return true;
    }

    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (this.appearFactor.get() < 1.0f) {
            return false;
        }
        updateCurrentTouch(screenX, screenY);
        if (!checkTouchableConditions()) {
            return false;
        }
        this.clickDetector.touchUp(this.currentTouch);
        this.tabsEngineYio.onTouchUp();
        if (!this.clickDetector.isClicked()) {
            return true;
        }
        onClick();
        return true;
    }

    public boolean onMouseWheelScrolled(int amount) {
        if (amount == 1) {
            this.tabsEngineYio.swipeTab(-1);
        } else if (amount == -1) {
            this.tabsEngineYio.swipeTab(1);
        }
        return true;
    }

    void onClick() {
        this.tabsEngineYio.setSpeed(0.0d);
        int panelIndex = indexOfPanelThatContainsPoint(this.currentTouch.f144x, this.currentTouch.f145y);
        if (panelIndex != -1) {
            int selX = (int) ((this.currentTouch.f144x - (((float) this.positions[panelIndex].f146x) + this.horOffset)) / (this.iconRadius * 2.0f));
            int selY = (int) ((this.currentTouch.f145y - (((float) this.positions[panelIndex].f147y) + this.verOffset)) / (this.iconRadius * 2.0f));
            if (getLevelNumber(selX, selY, panelIndex) <= CampaignProgressManager.getIndexOfLastLevel() && selX >= 0 && selX < this.rowSize && selY >= 0 && selY < this.columnSize) {
                this.selIndexX = selX;
                this.selIndexY = selY;
                this.selectedPanelIndex = panelIndex;
                this.selectionFactor.setValues(0.0d, 0.0d);
                this.selectionFactor.appear(3, 3.0d);
            }
        }
    }

    int indexOfPanelThatContainsPoint(float x, float y) {
        for (int i = 0; i < this.positions.length; i++) {
            if (isPointInsideRectangle(this.positions[i], x, y)) {
                return i;
            }
        }
        return -1;
    }

    boolean isPointInsideRectangle(RectangleYio rect, float x, float y) {
        return ((double) x) > rect.f146x && ((double) x) < rect.f146x + rect.width && ((double) y) > rect.f147y && ((double) y) < rect.f147y + rect.height;
    }

    public void setTouchable(boolean touchable) {
    }

    public boolean isButton() {
        return false;
    }

    public float getCircleX() {
        return (float) (this.defPos.f146x + (this.defPos.width / 2.0d));
    }

    public float getCircleY() {
        return (float) (this.defPos.f147y + ((((double) this.appearFactor.get()) * this.defPos.height) / 2.0d));
    }

    public float getCircleR() {
        return (float) ((((double) (this.appearFactor.get() * this.appearFactor.get())) * this.defPos.height) * 0.6d);
    }

    public void onPause() {
        for (int i = 0; i < this.textures.length; i++) {
            if (this.textures[i] != null) {
                this.textures[i].getTexture().dispose();
            }
        }
    }

    public void onResume() {
        renderAllPanels();
    }

    public void setPosition(RectangleYio position) {
    }

    public void resetToBottom() {
        this.tabsEngineYio.resetToBottom();
    }
}
