package io.androidovshchik.antiyoy.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.ArrayList;
import io.androidovshchik.antiyoy.SoundControllerYio;
import io.androidovshchik.antiyoy.factor_yio.FactorYio;
import io.androidovshchik.antiyoy.menu.behaviors.Reaction;
import io.androidovshchik.antiyoy.stuff.GraphicsYio;
import io.androidovshchik.antiyoy.stuff.RectangleYio;

public class ButtonYio {
    public static final int ACTION_DELAY = 50;
    public static final int DEFAULT_TOUCH_DELAY = 1000;
    public RectangleYio animPos = new RectangleYio(0.0d, 0.0d, 0.0d, 0.0d);
    public float animR;
    private int animType;
    public FactorYio appearFactor = new FactorYio();
    public final Color backColor = new Color(0.5f, 0.5f, 0.5f, 1.0f);
    public boolean currentlyTouched;
    public TextureRegion customBackgroundForText = null;
    public float cx;
    public float cy;
    public boolean deltaAnimationEnabled = false;
    private float deltaSize;
    private float deltaSizeArgument;
    public boolean hasShadow = true;
    public float hor;
    public final int id;
    boolean ignorePauseResume = false;
    private long lastTimeTouched;
    public boolean lockAction;
    public boolean mandatoryShadow = false;
    public final MenuControllerYio menuControllerYio;
    private boolean needToPerformAction;
    public boolean onlyShadow;
    public RectangleYio position;
    Sound pressSound = null;
    protected Reaction reaction;
    public boolean rectangularMask;
    public FactorYio selAlphaFactor = new FactorYio();
    public FactorYio selectionFactor = new FactorYio();
    public final ArrayList<String> text = new ArrayList();
    private float textOffset = 0.0f;
    String texturePath = null;
    public TextureRegion textureRegion;
    private long timeToPerformAction = 0;
    public boolean touchAnimation;
    private int touchDelay = DEFAULT_TOUCH_DELAY;
    private float touchOffset;
    public float touchX;
    public float touchY;
    protected boolean touchable = false;
    public float ver;
    private boolean visible = false;
    public float x1;
    public float x2;
    public float y1;
    public float y2;

    public ButtonYio(RectangleYio position, int id, MenuControllerYio menuControllerYio) {
        this.menuControllerYio = menuControllerYio;
        this.position = position;
        this.id = id;
    }

    public void move() {
        if (this.appearFactor.hasToMove()) {
            this.appearFactor.move();
        }
        if (this.selectionFactor.hasToMove()) {
            this.selectionFactor.move();
            if (this.lockAction && this.selectionFactor.get() == 1.0f) {
                this.lockAction = false;
            }
        }
        if (this.currentlyTouched) {
            this.selAlphaFactor.move();
        }
        if (this.deltaAnimationEnabled) {
            this.deltaSizeArgument += 0.1f;
            this.deltaSize = 0.98f + (0.04f * ((float) Math.cos((double) this.deltaSizeArgument)));
        }
        if (this.currentlyTouched && System.currentTimeMillis() - this.lastTimeTouched > ((long) this.touchDelay) && this.selAlphaFactor.get() == 0.0f) {
            this.currentlyTouched = false;
        }
        float factor = this.appearFactor.get();
        switch (this.animType) {
            case 0:
                this.hor = (float) ((((double) factor) * 0.5d) * this.position.width);
                this.ver = (float) ((((double) factor) * 0.5d) * this.position.height);
                this.cx = ((float) this.position.f146x) + (((float) this.position.width) * 0.5f);
                this.cy = ((float) this.position.f147y) + (((float) this.position.height) * 0.5f);
                this.x1 = this.cx - this.hor;
                this.x2 = this.cx + this.hor;
                this.y1 = this.cy - this.ver;
                this.y2 = this.cy + this.ver;
                break;
            case 1:
                this.x1 = (float) this.position.f146x;
                this.x2 = this.x1 + ((float) this.position.width);
                this.hor = ((float) this.position.width) * 0.5f;
                this.ver = ((float) this.position.height) * 0.5f;
                this.y1 = ((float) this.position.f147y) + ((float) (((double) (1.0f - factor)) * (((double) this.menuControllerYio.yioGdxGame.f148h) - this.position.f147y)));
                this.y2 = this.y1 + ((float) this.position.height);
                break;
            case 2:
                this.x1 = (float) this.position.f146x;
                this.x2 = this.x1 + ((float) this.position.width);
                this.hor = ((float) this.position.width) * 0.5f;
                this.ver = ((float) this.position.height) * 0.5f;
                this.y1 = ((float) (((double) factor) * (this.position.f147y + this.position.height))) - ((float) this.position.height);
                this.y2 = this.y1 + ((float) this.position.height);
                break;
            case 4:
                this.x1 = (float) this.position.f146x;
                this.x2 = this.x1 + ((float) this.position.width);
                this.hor = ((float) this.position.width) * 0.5f;
                this.ver = ((float) this.position.height) * 0.5f;
                this.y1 = (float) this.position.f147y;
                this.y2 = this.y1 + ((float) this.position.height);
                break;
            case 5:
                this.hor = (float) ((((double) factor) * 0.5d) * this.position.width);
                this.ver = (float) ((((double) factor) * 0.5d) * this.position.height);
                this.cx = ((float) this.position.f146x) + (((float) this.position.width) * 0.5f);
                this.cy = ((float) this.position.f147y) + (((float) this.position.height) * 0.5f);
                this.cx -= (1.0f - factor) * (this.cx - (((float) this.menuControllerYio.yioGdxGame.f149w) * 0.5f));
                this.cy -= (1.0f - factor) * (this.cy - (((float) this.menuControllerYio.yioGdxGame.f148h) * 0.5f));
                this.x1 = this.cx - this.hor;
                this.x2 = this.cx + this.hor;
                this.y1 = this.cy - this.ver;
                this.y2 = this.cy + this.ver;
                break;
            case 6:
                this.x1 = (float) this.position.f146x;
                this.x2 = this.x1 + ((float) this.position.width);
                this.hor = ((float) this.position.width) * 0.5f;
                this.ver = ((float) this.position.height) * 0.5f;
                this.y1 = (float) (this.position.f147y - ((((double) (1.0f - factor)) * 0.6d) * ((double) GraphicsYio.height)));
                this.y2 = this.y1 + ((float) this.position.height);
                break;
            case 7:
                this.x1 = (float) this.position.f146x;
                this.x2 = this.x1 + ((float) this.position.width);
                this.hor = ((float) this.position.width) * 0.5f;
                this.ver = ((float) this.position.height) * 0.5f;
                this.y1 = (float) (this.position.f147y + ((((double) (1.0f - factor)) * 0.6d) * ((double) GraphicsYio.height)));
                this.y2 = this.y1 + ((float) this.position.height);
                break;
            case 8:
                this.x1 = (float) (this.position.f146x - (((double) (1.0f - factor)) * this.position.width));
                this.x2 = this.x1 + ((float) this.position.width);
                this.hor = ((float) this.position.width) * 0.5f;
                this.ver = ((float) this.position.height) * 0.5f;
                this.y1 = (float) this.position.f147y;
                this.y2 = this.y1 + ((float) this.position.height);
                break;
        }
        if (this.deltaAnimationEnabled) {
            this.cx = this.x1 + this.hor;
            this.cy = this.y1 + this.ver;
            this.animPos.set((double) (this.cx - (this.deltaSize * this.hor)), (double) (this.cy - (this.deltaSize * this.ver)), (double) ((this.deltaSize * 2.0f) * this.hor), (double) ((this.deltaSize * 2.0f) * this.ver));
            return;
        }
        this.animPos.set((double) this.x1, (double) this.y1, (double) (this.hor * 2.0f), (double) (this.ver * 2.0f));
    }

    public boolean checkToPerformAction() {
        if (!this.needToPerformAction || System.currentTimeMillis() <= this.timeToPerformAction || this.lockAction) {
            return false;
        }
        this.needToPerformAction = false;
        this.reaction.perform(this);
        return true;
    }

    public void setTouchable(boolean touchable) {
        this.touchable = touchable;
    }

    public void setAnimation(int animType) {
        this.animType = animType;
    }

    public boolean checkTouch(int screenX, int screenY, int pointer, int button) {
        if (!this.touchable || ((double) screenX) <= this.position.f146x - ((double) this.touchOffset) || ((double) screenX) >= (this.position.f146x + this.position.width) + ((double) this.touchOffset) || ((double) screenY) <= this.position.f147y - ((double) this.touchOffset) || ((double) screenY) >= (this.position.f147y + this.position.height) + ((double) this.touchOffset)) {
            return false;
        }
        press(screenX, screenY);
        return true;
    }

    public void press() {
        press((int) (this.position.f146x + (this.position.width * 0.5d)), (int) (this.position.f147y + (this.position.height * 0.5d)));
    }

    public void press(int screenX, int screenY) {
        if (this.touchable) {
            this.currentlyTouched = true;
            this.lastTimeTouched = System.currentTimeMillis();
            playPressSound();
            this.selectionFactor.setValues(0.2d, 0.02d);
            this.selectionFactor.appear(0, 1.0d);
            this.selAlphaFactor.setValues(1.0d, 0.0d);
            this.selAlphaFactor.destroy(1, 0.5d);
            this.touchX = (float) screenX;
            this.touchY = (float) screenY;
            this.animR = Math.max(this.touchX - ((float) this.animPos.f146x), (float) ((this.animPos.f146x + this.animPos.width) - ((double) this.touchX)));
            this.lockAction = true;
            this.menuControllerYio.yioGdxGame.render();
            if (this.reaction != null && System.currentTimeMillis() - this.timeToPerformAction > 50) {
                this.needToPerformAction = true;
                this.timeToPerformAction = System.currentTimeMillis() + 100;
            }
        }
    }

    private void playPressSound() {
        if (this.pressSound == null) {
            SoundControllerYio.playSound(SoundControllerYio.soundPressButton);
        } else {
            SoundControllerYio.playSound(this.pressSound);
        }
    }

    public void setPressSound(Sound pressSound) {
        this.pressSound = pressSound;
    }

    public void setTexture(TextureRegion textureRegion) {
        this.textureRegion = textureRegion;
        this.hasShadow = false;
    }

    public void loadTexture(String path) {
        Texture texture = new Texture(Gdx.files.internal(path));
        texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        this.textureRegion = new TextureRegion(texture);
        this.texturePath = path;
        this.hasShadow = false;
    }

    public void loadCustomBackground(String path) {
        Texture texture = new Texture(Gdx.files.internal(path));
        texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        this.customBackgroundForText = new TextureRegion(texture);
        this.texturePath = path;
    }

    public boolean hasCustomBackground() {
        return this.customBackgroundForText != null;
    }

    public void resetTexture() {
        this.textureRegion = null;
    }

    public void destroy() {
        setTouchable(false);
        this.appearFactor.setDy(0.0d);
        this.appearFactor.destroy(MenuControllerYio.DESTROY_ANIM, MenuControllerYio.DESTROY_SPEED);
    }

    public void cleatText() {
        this.text.clear();
    }

    public void setTextLine(String line) {
        cleatText();
        addTextLine(line);
    }

    public ArrayList<String> getText() {
        return this.text;
    }

    public void enableDeltaAnimation() {
        this.deltaAnimationEnabled = true;
    }

    public void disableTouchAnimation() {
        this.touchAnimation = false;
    }

    public void addTextLine(String textLine) {
        this.text.add(textLine);
    }

    public void addManyLines(ArrayList<String> lines) {
        this.text.addAll(lines);
    }

    public void addEmptyLines(int quantity) {
        for (int k = 0; k < quantity; k++) {
            addTextLine(" ");
        }
    }

    public void setBackgroundColor(float r, float g, float b) {
        this.backColor.set(r, g, b, 1.0f);
    }

    public void setReaction(Reaction reaction) {
        this.reaction = reaction;
    }

    public boolean isVisible() {
        if (this.appearFactor.get() <= 0.0f || !this.visible) {
            return false;
        }
        return true;
    }

    public void enableRectangularMask() {
        this.rectangularMask = true;
    }

    public void setTouchOffset(float touchOffset) {
        this.touchOffset = touchOffset;
    }

    public void setTouchDelay(int touchDelay) {
        this.touchDelay = touchDelay;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isTouchable() {
        return this.touchable;
    }

    public MenuControllerYio getMenuControllerYio() {
        return this.menuControllerYio;
    }

    public boolean isCurrentlyTouched() {
        return this.currentlyTouched;
    }

    public boolean notRendered() {
        return this.textureRegion == null;
    }

    public void setShadow(boolean hasShadow) {
        this.hasShadow = hasShadow;
    }

    public boolean isShadowMandatory() {
        return this.mandatoryShadow;
    }

    public void setMandatoryShadow(boolean mandatoryShadow) {
        this.mandatoryShadow = mandatoryShadow;
    }

    public void onPause() {
        if (!this.ignorePauseResume) {
            if (this.textureRegion != null) {
                this.textureRegion.getTexture().dispose();
            }
            if (this.customBackgroundForText != null) {
                this.customBackgroundForText.getTexture().dispose();
            }
        }
    }

    public void onResume() {
        if (!this.ignorePauseResume) {
            if (hasCustomBackground()) {
                reloadCustomBackground();
            }
            if (hasText()) {
                this.menuControllerYio.buttonRenderer.renderButton(this);
            } else {
                reloadTexture();
            }
        }
    }

    private void reloadCustomBackground() {
        this.customBackgroundForText = null;
        loadCustomBackground(this.texturePath);
    }

    public TextureRegion getCustomBackgroundForText() {
        return this.customBackgroundForText;
    }

    private void reloadTexture() {
        resetTexture();
        boolean sh = this.hasShadow;
        loadTexture(this.texturePath);
        setShadow(sh);
    }

    private boolean hasText() {
        return this.texturePath == null;
    }

    public void setPosition(RectangleYio position) {
        this.position = position;
    }

    public void setIgnorePauseResume(boolean ignorePauseResume) {
        this.ignorePauseResume = ignorePauseResume;
    }

    public float getTextOffset() {
        return this.textOffset;
    }

    public void setTextOffset(float textOffset) {
        this.textOffset = textOffset;
    }
}
