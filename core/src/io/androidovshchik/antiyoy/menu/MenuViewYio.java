package io.androidovshchik.antiyoy.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import java.util.ArrayList;
import java.util.Iterator;
import yio.tro.antiyoy.YioGdxGame;
import yio.tro.antiyoy.gameplay.game_view.GameView;
import yio.tro.antiyoy.menu.render.MenuRender;
import yio.tro.antiyoy.stuff.GraphicsYio;
import yio.tro.antiyoy.stuff.RectangleYio;
import yio.tro.antiyoy.stuff.Yio;

public class MenuViewYio {
    public SpriteBatch batch;
    TextureRegion blackCircle = GameView.loadTextureRegion("anim_circle_high_res.png", false);
    TextureRegion buttonPixel = GameView.loadTextureRegion("button_pixel.png", false);
    private Color f101c;
    private int cornerSize = ((int) (0.02d * ((double) Gdx.graphics.getHeight())));
    TextureRegion grayTransCircle = GameView.loadTextureRegion("gray_transition_circle.png", false);
    public float f102h = ((float) Gdx.graphics.getHeight());
    private final MenuControllerYio menuControllerYio;
    public OrthographicCamera orthoCam;
    TextureRegion scrollerCircle = GameView.loadTextureRegion("scroller_circle.png", false);
    public TextureRegion shadowCorner = GameView.loadTextureRegion("corner_shadow.png", true);
    public TextureRegion shadowSide = GameView.loadTextureRegion("side_shadow.png", true);
    public ShapeRenderer shapeRenderer;
    public float f103w = ((float) Gdx.graphics.getWidth());
    private float x1;
    private float x2;
    private float y1;
    private float y2;
    public final YioGdxGame yioGdxGame;

    public MenuViewYio(YioGdxGame yioGdxGame) {
        this.yioGdxGame = yioGdxGame;
        this.menuControllerYio = yioGdxGame.menuControllerYio;
        this.shapeRenderer = yioGdxGame.shapeRenderer;
        createOrthoCam();
        this.batch = yioGdxGame.batch;
        if (this.batch == null) {
            System.out.println("fuck....");
        }
        MenuRender.updateRenderSystems(this);
    }

    private void createOrthoCam() {
        this.orthoCam = new OrthographicCamera((float) this.yioGdxGame.f149w, (float) this.yioGdxGame.f148h);
        this.orthoCam.position.set(this.orthoCam.viewportWidth / 2.0f, this.orthoCam.viewportHeight / 2.0f, 0.0f);
        this.orthoCam.update();
    }

    public void drawRoundRect(RectangleYio pos) {
        drawRoundRect(pos, this.cornerSize);
    }

    public void drawRoundRect(RectangleYio pos, int cornerSize) {
        this.shapeRenderer.rect(((float) pos.f146x) + ((float) cornerSize), (float) pos.f147y, ((float) pos.width) - ((float) (cornerSize * 2)), (float) pos.height);
        this.shapeRenderer.rect((float) pos.f146x, ((float) pos.f147y) + ((float) cornerSize), (float) pos.width, ((float) pos.height) - ((float) (cornerSize * 2)));
        this.shapeRenderer.circle(((float) pos.f146x) + ((float) cornerSize), ((float) pos.f147y) + ((float) cornerSize), (float) cornerSize, 16);
        this.shapeRenderer.circle((((float) pos.f146x) + ((float) pos.width)) - ((float) cornerSize), ((float) pos.f147y) + ((float) cornerSize), (float) cornerSize, 16);
        this.shapeRenderer.circle(((float) pos.f146x) + ((float) cornerSize), (((float) pos.f147y) + ((float) pos.height)) - ((float) cornerSize), (float) cornerSize, 16);
        this.shapeRenderer.circle((((float) pos.f146x) + ((float) pos.width)) - ((float) cornerSize), (((float) pos.f147y) + ((float) pos.height)) - ((float) cornerSize), (float) cornerSize, 16);
    }

    private void drawRect(RectangleYio pos) {
        this.shapeRenderer.rect((float) pos.f146x, (float) pos.f147y, (float) pos.width, (float) pos.height);
    }

    public void drawCircle(float x, float y, float r) {
        this.shapeRenderer.circle(x, y, r);
    }

    public void renderShadow(ButtonYio buttonYio, SpriteBatch batch) {
        this.x1 = buttonYio.x1;
        this.x2 = buttonYio.x2;
        this.y1 = buttonYio.y1;
        this.y2 = buttonYio.y2;
        if (buttonYio.appearFactor.get() <= 1.0f) {
            batch.setColor(this.f101c.f39r, this.f101c.f38g, this.f101c.f37b, buttonYio.appearFactor.get());
        } else {
            batch.setColor(this.f101c.f39r, this.f101c.f38g, this.f101c.f37b, 1.0f);
        }
        batch.draw(this.shadowSide, ((float) this.cornerSize) + this.x1, this.y2 - ((float) this.cornerSize), (buttonYio.hor - ((float) this.cornerSize)) * 2.0f, (float) (this.cornerSize * 2));
        batch.draw(this.shadowSide, ((float) this.cornerSize) + this.x1, ((float) this.cornerSize) + this.y1, 0.0f, 0.0f, (buttonYio.ver - ((float) this.cornerSize)) * 2.0f, (float) (this.cornerSize * 2), 1.0f, 1.0f, 90.0f);
        batch.draw(this.shadowSide, this.x2 - ((float) this.cornerSize), ((float) this.cornerSize) + this.y1, 0.0f, 0.0f, (buttonYio.hor - ((float) this.cornerSize)) * 2.0f, (float) (this.cornerSize * 2), 1.0f, 1.0f, 180.0f);
        batch.draw(this.shadowSide, this.x2 - ((float) this.cornerSize), this.y2 - ((float) this.cornerSize), 0.0f, 0.0f, (buttonYio.ver - ((float) this.cornerSize)) * 2.0f, (float) (this.cornerSize * 2), 1.0f, 1.0f, 270.0f);
        batch.draw(this.shadowCorner, this.x1 - ((float) this.cornerSize), this.y2 - ((float) this.cornerSize), (float) (this.cornerSize * 2), (float) (this.cornerSize * 2));
        batch.draw(this.shadowCorner, ((float) this.cornerSize) + this.x1, this.y1 - ((float) this.cornerSize), 0.0f, 0.0f, (float) (this.cornerSize * 2), (float) (this.cornerSize * 2), 1.0f, 1.0f, 90.0f);
        batch.draw(this.shadowCorner, ((float) this.cornerSize) + this.x2, ((float) this.cornerSize) + this.y1, 0.0f, 0.0f, (float) (this.cornerSize * 2), (float) (this.cornerSize * 2), 1.0f, 1.0f, 180.0f);
        batch.draw(this.shadowCorner, this.x2 - ((float) this.cornerSize), ((float) this.cornerSize) + this.y2, 0.0f, 0.0f, (float) (this.cornerSize * 2), (float) (this.cornerSize * 2), 1.0f, 1.0f, 270.0f);
    }

    public void renderShadow(RectangleYio rectangle, float factor, SpriteBatch batch) {
        float hor = (0.5f * factor) * ((float) rectangle.width);
        float ver = (0.5f * factor) * ((float) rectangle.height);
        float cx = ((float) rectangle.f146x) + (0.5f * ((float) rectangle.width));
        float cy = ((float) rectangle.f147y) + (0.5f * ((float) rectangle.height));
        this.x1 = cx - hor;
        this.x2 = cx + hor;
        this.y1 = cy - ver;
        this.y2 = cy + ver;
        GraphicsYio.setBatchAlpha(batch, (double) factor);
        batch.draw(this.shadowSide, ((float) this.cornerSize) + this.x1, this.y2 - ((float) this.cornerSize), (hor - ((float) this.cornerSize)) * 2.0f, (float) (this.cornerSize * 2));
        batch.draw(this.shadowSide, ((float) this.cornerSize) + this.x1, ((float) this.cornerSize) + this.y1, 0.0f, 0.0f, (ver - ((float) this.cornerSize)) * 2.0f, (float) (this.cornerSize * 2), 1.0f, 1.0f, 90.0f);
        batch.draw(this.shadowSide, this.x2 - ((float) this.cornerSize), ((float) this.cornerSize) + this.y1, 0.0f, 0.0f, (hor - ((float) this.cornerSize)) * 2.0f, (float) (this.cornerSize * 2), 1.0f, 1.0f, 180.0f);
        batch.draw(this.shadowSide, this.x2 - ((float) this.cornerSize), this.y2 - ((float) this.cornerSize), 0.0f, 0.0f, (ver - ((float) this.cornerSize)) * 2.0f, (float) (this.cornerSize * 2), 1.0f, 1.0f, 270.0f);
        batch.draw(this.shadowCorner, this.x1 - ((float) this.cornerSize), this.y2 - ((float) this.cornerSize), (float) (this.cornerSize * 2), (float) (this.cornerSize * 2));
        batch.draw(this.shadowCorner, ((float) this.cornerSize) + this.x1, this.y1 - ((float) this.cornerSize), 0.0f, 0.0f, (float) (this.cornerSize * 2), (float) (this.cornerSize * 2), 1.0f, 1.0f, 90.0f);
        batch.draw(this.shadowCorner, ((float) this.cornerSize) + this.x2, ((float) this.cornerSize) + this.y1, 0.0f, 0.0f, (float) (this.cornerSize * 2), (float) (this.cornerSize * 2), 1.0f, 1.0f, 180.0f);
        batch.draw(this.shadowCorner, this.x2 - ((float) this.cornerSize), ((float) this.cornerSize) + this.y2, 0.0f, 0.0f, (float) (this.cornerSize * 2), (float) (this.cornerSize * 2), 1.0f, 1.0f, 270.0f);
        GraphicsYio.setBatchAlpha(batch, 1.0d);
    }

    private boolean checkForSpecialMask(ButtonYio buttonYio) {
        switch (buttonYio.id) {
            case 3:
                if (((double) buttonYio.appearFactor.get()) > 0.1d) {
                    this.shapeRenderer.circle(buttonYio.cx, buttonYio.cy, ((float) (0.8d + (0.2d * ((double) buttonYio.selectionFactor.get())))) * buttonYio.hor);
                }
                return true;
            default:
                return false;
        }
    }

    private boolean checkForSpecialAnimationMask(ButtonYio buttonYio) {
        RectangleYio pos = buttonYio.animPos;
        switch (buttonYio.id) {
            case Keys.f16M /*41*/:
                this.shapeRenderer.rect((float) pos.f146x, (float) (pos.f147y + (0.5d * pos.height)), (float) pos.width, ((float) pos.height) * 0.5f);
                return true;
            case Keys.f17N /*42*/:
                this.shapeRenderer.rect((float) pos.f146x, (float) pos.f147y, (float) pos.width, ((float) pos.height) * 0.5f);
                return true;
            case Keys.f18O /*43*/:
                this.shapeRenderer.rect((float) pos.f146x, (float) pos.f147y, (float) pos.width, (float) pos.height);
                return true;
            case Keys.f19P /*44*/:
                this.shapeRenderer.rect((float) pos.f146x, (float) pos.f147y, (float) pos.width, (float) pos.height);
                return true;
            default:
                return false;
        }
    }

    private boolean checkForSpecialAlpha(ButtonYio buttonYio) {
        int i = buttonYio.id;
        return false;
    }

    public void render(boolean renderAliveButtons, boolean renderDyingButtons) {
        ArrayList buttons = this.menuControllerYio.buttons;
        this.f101c = this.batch.getColor();
        renderShadow(renderAliveButtons, renderDyingButtons, buttons);
        drawMasks(renderAliveButtons, renderDyingButtons, buttons);
        renderButtons(renderAliveButtons, renderDyingButtons, buttons);
        renderSelection(renderAliveButtons, renderDyingButtons, buttons);
        renderCheckButtons();
        renderInterfaceElements();
    }

    private void renderInterfaceElements() {
        ArrayList<InterfaceElement> interfaceElements = this.menuControllerYio.interfaceElements;
        this.batch.begin();
        Iterator it = interfaceElements.iterator();
        while (it.hasNext()) {
            InterfaceElement element = (InterfaceElement) it.next();
            if (element.isVisible()) {
                element.getRenderSystem().renderFirstLayer(element);
            }
        }
        it = interfaceElements.iterator();
        while (it.hasNext()) {
            element = (InterfaceElement) it.next();
            if (element.isVisible()) {
                element.getRenderSystem().renderSecondLayer(element);
            }
        }
        it = interfaceElements.iterator();
        while (it.hasNext()) {
            element = (InterfaceElement) it.next();
            if (element.isVisible()) {
                element.getRenderSystem().renderThirdLayer(element);
            }
        }
        this.batch.end();
    }

    private void renderCheckButtons() {
        this.batch.begin();
        Iterator it = this.menuControllerYio.checkButtons.iterator();
        while (it.hasNext()) {
            CheckButtonYio checkButton = (CheckButtonYio) it.next();
            if (checkButton.isVisible()) {
                MenuRender.renderCheckButton.renderCheckButton(checkButton);
            }
        }
        this.batch.end();
    }

    private void renderSelection(boolean renderAliveButtons, boolean renderDyingButtons, ArrayList<ButtonYio> buttons) {
        Iterator it = buttons.iterator();
        while (it.hasNext()) {
            ButtonYio buttonYio = (ButtonYio) it.next();
            if (buttonYio.isVisible() && buttonYio.isCurrentlyTouched() && buttonYio.touchAnimation && buttonYio.selectionFactor.get() < 1.0f) {
                if ((renderAliveButtons && buttonYio.appearFactor.getDy() >= 0.0d) || ((renderDyingButtons && buttonYio.appearFactor.getDy() < 0.0d) || buttonYio.selectionFactor.hasToMove())) {
                    YioGdxGame.maskingBegin();
                    this.shapeRenderer.begin(ShapeType.Filled);
                    checkForSpecialAnimationMask(buttonYio);
                    drawRoundRect(buttonYio.animPos);
                    this.shapeRenderer.end();
                    this.batch.begin();
                    YioGdxGame.maskingContinue();
                    this.batch.setColor(this.f101c.f39r, this.f101c.f38g, this.f101c.f37b, 0.7f * buttonYio.selAlphaFactor.get());
                    float r = buttonYio.selectionFactor.get() * buttonYio.animR;
                    this.batch.draw(this.blackCircle, buttonYio.touchX - r, buttonYio.touchY - r, 2.0f * r, 2.0f * r);
                    this.batch.end();
                    this.batch.setColor(this.f101c.f39r, this.f101c.f38g, this.f101c.f37b, 1.0f);
                    YioGdxGame.maskingEnd();
                }
            }
        }
    }

    private void renderButtons(boolean renderAliveButtons, boolean renderDyingButtons, ArrayList<ButtonYio> buttons) {
        this.batch.begin();
        if (this.yioGdxGame.useMenuMasks) {
            YioGdxGame.maskingContinue();
        }
        Iterator it = buttons.iterator();
        while (it.hasNext()) {
            renderSingleButton(renderAliveButtons, renderDyingButtons, (ButtonYio) it.next());
        }
        this.batch.setColor(this.f101c.f39r, this.f101c.f38g, this.f101c.f37b, 1.0f);
        this.batch.end();
        if (this.yioGdxGame.useMenuMasks) {
            YioGdxGame.maskingEnd();
        }
    }

    private void renderSingleButton(boolean renderAliveButtons, boolean renderDyingButtons, ButtonYio buttonYio) {
        if (buttonYio.isVisible() && !buttonYio.onlyShadow) {
            if ((renderAliveButtons && buttonYio.appearFactor.getGravity() >= 0.0d) || (renderDyingButtons && buttonYio.appearFactor.getGravity() <= 0.0d)) {
                if (buttonYio.mandatoryShadow) {
                    renderShadow(buttonYio, this.batch);
                }
                if (!checkForSpecialAlpha(buttonYio)) {
                    if (buttonYio.appearFactor.get() <= 1.0f) {
                        this.batch.setColor(this.f101c.f39r, this.f101c.f38g, this.f101c.f37b, buttonYio.appearFactor.get());
                    } else {
                        this.batch.setColor(this.f101c.f39r, this.f101c.f38g, this.f101c.f37b, 1.0f);
                    }
                }
                RectangleYio ap = buttonYio.animPos;
                this.batch.draw(buttonYio.textureRegion, (float) ap.f146x, (float) ap.f147y, (float) ap.width, (float) ap.height);
                if (!buttonYio.isCurrentlyTouched()) {
                    return;
                }
                if (!buttonYio.touchAnimation || ((double) buttonYio.selectionFactor.get()) > 0.99d) {
                    this.batch.setColor(this.f101c.f39r, this.f101c.f38g, this.f101c.f37b, 0.7f * buttonYio.selAlphaFactor.get());
                    this.batch.draw(this.buttonPixel, (float) ap.f146x, (float) ap.f147y, (float) ap.width, (float) ap.height);
                }
            }
        }
    }

    private void drawMasks(boolean renderAliveButtons, boolean renderDyingButtons, ArrayList<ButtonYio> buttons) {
        if (this.yioGdxGame.useMenuMasks) {
            YioGdxGame.maskingBegin();
            this.shapeRenderer.begin(ShapeType.Filled);
            Iterator it = buttons.iterator();
            while (it.hasNext()) {
                ButtonYio buttonYio = (ButtonYio) it.next();
                if (buttonYio.isVisible() && !checkForSpecialMask(buttonYio)) {
                    if (!buttonYio.rectangularMask || ((!renderAliveButtons || buttonYio.appearFactor.getGravity() < 0.0d) && (!renderDyingButtons || buttonYio.appearFactor.getGravity() > 0.0d))) {
                        drawRoundRect(buttonYio.animPos);
                    } else {
                        drawRect(buttonYio.animPos);
                    }
                }
            }
            this.shapeRenderer.end();
        }
    }

    private void renderShadow(boolean renderAliveButtons, boolean renderDyingButtons, ArrayList<ButtonYio> buttons) {
        this.batch.begin();
        Iterator it = buttons.iterator();
        while (it.hasNext()) {
            ButtonYio buttonYio = (ButtonYio) it.next();
            if (buttonYio.isVisible() && buttonYio.hasShadow && !buttonYio.mandatoryShadow && ((double) buttonYio.appearFactor.get()) > 0.1d) {
                if ((renderAliveButtons && buttonYio.appearFactor.getGravity() >= 0.0d) || (renderDyingButtons && buttonYio.appearFactor.getGravity() <= 0.0d)) {
                    renderShadow(buttonYio, this.batch);
                }
            }
        }
        this.batch.end();
    }

    void specialInfoPanelRenderPiece() {
        ButtonYio infoPanel = this.menuControllerYio.getButtonById(11);
        if (infoPanel != null && this.menuControllerYio.getButtonById(11).isVisible()) {
            YioGdxGame.maskingBegin();
            this.shapeRenderer.begin(ShapeType.Filled);
            drawRoundRect(infoPanel.animPos);
            this.shapeRenderer.end();
            YioGdxGame.maskingContinue();
            renderTransitionCircle(this.grayTransCircle, this.menuControllerYio.infoPanelFactor.get() * this.menuControllerYio.infoPanelFactor.get(), infoPanel.animPos, this.batch, (((infoPanel.appearFactor.get() * 0.5f) + 0.5f) * ((float) this.yioGdxGame.f149w)) / 2.0f, (float) ((infoPanel.animPos.f147y + (0.95d * infoPanel.animPos.height)) - ((0.65d * Math.sqrt((double) infoPanel.appearFactor.get())) * infoPanel.animPos.height)));
            YioGdxGame.maskingEnd();
        }
    }

    private static float maxDistanceToCorners(float x, float y, RectangleYio frame) {
        if (((double) x) > frame.f146x + (frame.width * 0.5d)) {
            if (((double) y) > frame.f147y + (frame.height * 0.5d)) {
                return (float) Yio.distance((double) x, (double) y, frame.f146x, frame.f147y);
            }
            return (float) Yio.distance((double) x, (double) y, frame.f146x, frame.f147y + frame.height);
        } else if (((double) y) > frame.f147y + (frame.height * 0.5d)) {
            return (float) Yio.distance((double) x, (double) y, frame.f146x + frame.width, frame.f147y);
        } else {
            return (float) Yio.distance((double) x, (double) y, frame.f146x + frame.width, frame.f147y + frame.height);
        }
    }

    private static void renderTransitionCircle(TextureRegion circleTexture, float factor, RectangleYio frame, SpriteBatch batch, float x, float y) {
        Color c = batch.getColor();
        if (((double) factor) < 0.5d) {
            batch.setColor(c.f39r, c.f38g, c.f37b, 1.0f);
        } else {
            batch.setColor(c.f39r, c.f38g, c.f37b, 1.0f - (2.0f * factor));
        }
        float r = ((float) Math.sqrt((double) (2.0f * factor))) * maxDistanceToCorners(x, y, frame);
        batch.begin();
        batch.draw(circleTexture, x - r, y - r, 2.0f * r, 2.0f * r);
        batch.end();
        batch.setColor(c.f39r, c.f38g, c.f37b, 1.0f);
    }

    public int getCornerSize() {
        return this.cornerSize;
    }
}
