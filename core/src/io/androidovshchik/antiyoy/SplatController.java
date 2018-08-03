package io.androidovshchik.antiyoy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import io.androidovshchik.antiyoy.factor_yio.FactorYio;
import io.androidovshchik.antiyoy.stuff.GraphicsYio;
import io.androidovshchik.antiyoy.stuff.Yio;

public class SplatController {
    int currentSplatIndex;
    boolean needToHideSplats;
    float splatSize;
    TextureRegion splatTexture;
    FactorYio splatTransparencyFactor;
    ArrayList<Splat> splats;
    long timeToHideSplats;
    long timeToSpawnNextSplat;
    private final YioGdxGame yioGdxGame;

    public SplatController(YioGdxGame yioGdxGame) {
        this.yioGdxGame = yioGdxGame;
    }

    void initSplats() {
        this.splats = new ArrayList();
        this.splatSize = 0.15f * ((float) Gdx.graphics.getWidth());
        ListIterator iterator = this.splats.listIterator();
        for (int i = 0; i < 100; i++) {
            float sx = YioGdxGame.random.nextFloat() * GraphicsYio.width;
            float sr = ((0.03f * YioGdxGame.random.nextFloat()) * GraphicsYio.height) + (0.02f * GraphicsYio.height);
            float sy = YioGdxGame.random.nextFloat() * GraphicsYio.height;
            float dx = ((0.02f * this.splatSize) * YioGdxGame.random.nextFloat()) - (0.01f * this.splatSize);
            float dy = 0.01f * this.splatSize;
            Splat splat = new Splat(null, sx, sy);
            if (YioGdxGame.random.nextDouble() < 0.6d || Yio.distance((double) (GraphicsYio.width / 2.0f), (double) (GraphicsYio.height / 2.0f), (double) sx, (double) sy) > ((double) (0.6f * GraphicsYio.width))) {
                splat.f90y = 2.0f * GraphicsYio.height;
            }
            splat.setSpeed(dx, dy);
            splat.setRadius(sr);
            iterator.add(splat);
        }
    }

    void moveSplats() {
        this.splatTransparencyFactor.move();
        if (this.needToHideSplats && System.currentTimeMillis() > this.timeToHideSplats) {
            this.needToHideSplats = false;
        }
        if (!this.yioGdxGame.gameView.coversAllScreen()) {
            if (System.currentTimeMillis() > this.timeToSpawnNextSplat) {
                this.timeToSpawnNextSplat = (System.currentTimeMillis() + 300) + ((long) YioGdxGame.random.nextInt(100));
                float sx = YioGdxGame.random.nextFloat() * GraphicsYio.width;
                float sr = ((0.03f * YioGdxGame.random.nextFloat()) * GraphicsYio.height) + (GraphicsYio.height * 0.02f);
                float sy = -sr;
                int c = 0;
                int size = this.splats.size();
                while (c < size) {
                    c++;
                    Splat splat = (Splat) this.splats.get(this.currentSplatIndex);
                    this.currentSplatIndex++;
                    if (this.currentSplatIndex >= size) {
                        this.currentSplatIndex = 0;
                    }
                    if (!splat.isVisible()) {
                        float dx = ((this.splatSize * 0.02f) * YioGdxGame.random.nextFloat()) - (this.splatSize * 0.01f);
                        float dy = 0.01f * this.splatSize;
                        splat.set(sx, sy);
                        splat.setSpeed(dx, dy);
                        splat.setRadius(sr);
                        break;
                    }
                }
            }
            Iterator it = this.splats.iterator();
            while (it.hasNext()) {
                ((Splat) it.next()).move();
            }
        }
    }

    void renderSplats(Color c) {
        Splat splat;
        if (this.splatTransparencyFactor.get() == 1.0f) {
            this.yioGdxGame.batch.setColor(c.f39r, c.f38g, c.f37b, this.splatTransparencyFactor.get());
            Iterator it = this.splats.iterator();
            while (it.hasNext()) {
                splat = (Splat) it.next();
                this.yioGdxGame.batch.draw(this.splatTexture, splat.f89x - (splat.f88r / 2.0f), splat.f90y - (splat.f88r / 2.0f), splat.f88r, splat.f88r);
            }
        } else if (this.splatTransparencyFactor.get() > 0.0f) {
            this.yioGdxGame.batch.setColor(c.f39r, c.f38g, c.f37b, this.splatTransparencyFactor.get());
            Iterator it2 = this.splats.iterator();
            while (it2.hasNext()) {
                splat = (Splat) it2.next();
                float a = (float) Yio.angle((double) (GraphicsYio.width / 2.0f), (double) (GraphicsYio.height / 2.0f), (double) splat.f89x, (double) splat.f90y);
                float d = ((0.5f * GraphicsYio.height) - ((float) Yio.distance((double) (GraphicsYio.width / 2.0f), (double) (GraphicsYio.height / 2.0f), (double) splat.f89x, (double) splat.f90y))) * (1.0f - this.splatTransparencyFactor.get());
                this.yioGdxGame.batch.draw(this.splatTexture, (splat.f89x - (splat.f88r / 2.0f)) + (((float) Math.cos((double) a)) * d), (splat.f90y - (splat.f88r / 2.0f)) + (((float) Math.sin((double) a)) * d), splat.f88r, splat.f88r);
            }
        }
    }

    void hideSplats() {
        this.needToHideSplats = true;
        this.timeToHideSplats = System.currentTimeMillis() + 350;
        this.splatTransparencyFactor.setDy(0.0d);
        this.splatTransparencyFactor.destroy(0, 1.0d);
    }

    void revealSplats() {
        this.needToHideSplats = false;
        this.splatTransparencyFactor.setDy(0.0d);
        this.splatTransparencyFactor.appear(0, 0.5d);
    }
}
