package io.androidovshchik.antiyoy.menu.FireworksElement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.net.HttpStatus;
import java.util.ArrayList;
import java.util.Iterator;
import yio.tro.antiyoy.YioGdxGame;
import yio.tro.antiyoy.factor_yio.FactorYio;
import yio.tro.antiyoy.menu.InterfaceElement;
import yio.tro.antiyoy.menu.MenuControllerYio;
import yio.tro.antiyoy.menu.render.MenuRender;
import yio.tro.antiyoy.stuff.GraphicsYio;
import yio.tro.antiyoy.stuff.PointYio;
import yio.tro.antiyoy.stuff.RectangleYio;
import yio.tro.antiyoy.stuff.RepeatYio;
import yio.tro.antiyoy.stuff.Yio;
import yio.tro.antiyoy.stuff.object_pool.ObjectPoolYio;

public class FireworksElement extends InterfaceElement {
    private float accelX;
    public FactorYio appearFactor = new FactorYio();
    double defGravityAngle = -1.5707963267948966d;
    private double gravityAngle;
    double gravityAngleDelta;
    MenuControllerYio menuControllerYio;
    public ArrayList<FeParticle> particles = new ArrayList();
    ObjectPoolYio<FeParticle> poolParticles;
    ObjectPoolYio<FeSequence> poolSequences;
    public RectangleYio position = new RectangleYio();
    RepeatYio<FireworksElement> repeatCheckForDeadParticles;
    RepeatYio<FireworksElement> repeatExplode;
    RepeatYio<FireworksElement> repeatMakeSequence;
    RepeatYio<FireworksElement> repeatUpdateAccelerometer;
    ArrayList<FeSequence> sequences = new ArrayList();
    PointYio tempPoint = new PointYio();
    PointYio touchPoint = new PointYio();

    class C01071 extends ObjectPoolYio<FeParticle> {
        C01071() {
        }

        public FeParticle makeNewObject() {
            return new FeParticle();
        }
    }

    class C01082 extends ObjectPoolYio<FeSequence> {
        C01082() {
        }

        public FeSequence makeNewObject() {
            return new FeSequence();
        }
    }

    public FireworksElement(MenuControllerYio menuControllerYio, int id) {
        super(id);
        this.menuControllerYio = menuControllerYio;
        initPools();
        initRepeats();
    }

    private void initPools() {
        this.poolParticles = new C01071();
        this.poolSequences = new C01082();
    }

    private void initRepeats() {
        this.repeatCheckForDeadParticles = new RepeatYio<FireworksElement>(this, 60) {
            public void performAction() {
                ((FireworksElement) this.parent).checkForDeadParticles();
            }
        };
        this.repeatExplode = new RepeatYio<FireworksElement>(this, 30) {
            public void performAction() {
                ((FireworksElement) this.parent).makeExplosionInRandomPlace();
                FireworksElement.this.repeatExplode.setCountDown(YioGdxGame.random.nextInt(45) + 15);
            }
        };
        this.repeatUpdateAccelerometer = new RepeatYio<FireworksElement>(this, 6) {
            public void performAction() {
                ((FireworksElement) this.parent).updateAccelerometer();
            }
        };
        this.repeatMakeSequence = new RepeatYio<FireworksElement>(this, 2, HttpStatus.SC_MULTIPLE_CHOICES) {
            public void performAction() {
                ((FireworksElement) this.parent).makeSequence();
            }
        };
    }

    public void move() {
        this.appearFactor.move();
        moveParticles();
        this.repeatCheckForDeadParticles.move();
        this.repeatExplode.move();
        this.repeatMakeSequence.move();
        moveSequences();
    }

    private void moveSequences() {
        if (this.sequences.size() != 0) {
            FeSequence feSequence = (FeSequence) this.sequences.get(0);
            if (feSequence.isReady()) {
                this.sequences.remove(0);
                this.poolSequences.add(feSequence);
                makeExplosion(feSequence.position, 3);
            }
        }
    }

    private void makeSequence() {
        this.repeatMakeSequence.setCountDown((YioGdxGame.random.nextInt(8) + 7) * 60);
        int n = YioGdxGame.random.nextInt(6) + 5;
        putTempPointInRandomPlace();
        for (int i = 0; i < n; i++) {
            FeSequence next = (FeSequence) this.poolSequences.getNext();
            next.time = System.currentTimeMillis() + ((long) (i * 100));
            next.position.setBy(this.tempPoint);
            this.sequences.add(next);
        }
    }

    private void makeExplosionInRandomPlace() {
        if (this.appearFactor.get() >= 1.0f) {
            putTempPointInRandomPlace();
            makeExplosion(this.tempPoint);
        }
    }

    private void putTempPointInRandomPlace() {
        this.tempPoint.f144x = YioGdxGame.random.nextFloat() * GraphicsYio.width;
        this.tempPoint.f145y = (YioGdxGame.random.nextFloat() * 0.9f) * GraphicsYio.height;
    }

    private void makeExplosion(PointYio pos) {
        makeExplosion(pos, YioGdxGame.random.nextInt(10) + 10);
    }

    private void makeExplosion(PointYio pos, int n) {
        for (int i = 0; i < n; i++) {
            spawnParticle(pos);
        }
    }

    private void checkForDeadParticles() {
        for (int i = this.particles.size() - 1; i >= 0; i--) {
            FeParticle feParticle = (FeParticle) this.particles.get(i);
            if (!feParticle.isAlive()) {
                Yio.removeByIterator(this.particles, feParticle);
                this.poolParticles.add(feParticle);
            }
        }
    }

    private void moveParticles() {
        this.repeatUpdateAccelerometer.move();
        this.gravityAngleDelta = (double) ((-this.accelX) / 5.0f);
        if (this.gravityAngleDelta > 0.5d) {
            this.gravityAngleDelta = 0.5d;
        }
        if (this.gravityAngleDelta < -0.5d) {
            this.gravityAngleDelta = -0.5d;
        }
        if (Math.abs(this.gravityAngleDelta) < 0.3d) {
            this.gravityAngleDelta = 0.0d;
        }
        this.gravityAngle = this.defGravityAngle + this.gravityAngleDelta;
        Iterator it = this.particles.iterator();
        while (it.hasNext()) {
            FeParticle particle = (FeParticle) it.next();
            particle.move();
            particle.applyGravity(this.gravityAngle);
        }
    }

    private void updateAccelerometer() {
        this.accelX = Gdx.input.getAccelerometerX();
    }

    private void spawnParticle(PointYio spawnPos) {
        FeParticle next = (FeParticle) this.poolParticles.getNext();
        next.position.setBy(spawnPos);
        next.speed.relocateRadial((double) (0.02f * GraphicsYio.width), Yio.getRandomAngle());
        PointYio pointYio = next.speed;
        pointYio.f145y += 0.01f * GraphicsYio.width;
        next.viewType = YioGdxGame.random.nextInt(9);
        next.appear();
        Yio.addByIterator(this.particles, next);
    }

    public FactorYio getFactor() {
        return this.appearFactor;
    }

    public void destroy() {
        this.appearFactor.destroy(1, 1.0d);
        Iterator it = this.particles.iterator();
        while (it.hasNext()) {
            ((FeParticle) it.next()).kill();
        }
        it = this.sequences.iterator();
        while (it.hasNext()) {
            this.poolSequences.add((FeSequence) it.next());
        }
        this.sequences.clear();
    }

    public void appear() {
        this.appearFactor.setValues(0.01d, 0.0d);
        this.appearFactor.appear(3, 1.0d);
        clearParticles();
    }

    private void clearParticles() {
        Iterator it = this.particles.iterator();
        while (it.hasNext()) {
            this.poolParticles.add((FeParticle) it.next());
        }
        this.particles.clear();
    }

    public boolean isVisible() {
        return this.appearFactor.get() > 0.0f;
    }

    public boolean checkToPerformAction() {
        return false;
    }

    public boolean isTouchable() {
        return true;
    }

    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        this.touchPoint.set((double) screenX, (double) screenY);
        if (this.touchPoint.f145y > 0.89f * GraphicsYio.height) {
            return false;
        }
        makeExplosion(this.touchPoint);
        return true;
    }

    public boolean touchDrag(int screenX, int screenY, int pointer) {
        return false;
    }

    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    public void setTouchable(boolean touchable) {
    }

    public boolean isButton() {
        return false;
    }

    public void setPosition(RectangleYio position) {
    }

    public MenuRender getRenderSystem() {
        return MenuRender.renderFireworksElement;
    }
}
