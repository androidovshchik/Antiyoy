package io.androidovshchik.antiyoy.stuff.tabs_engine;

import com.badlogic.gdx.net.HttpStatus;
import java.util.ArrayList;
import java.util.Iterator;
import yio.tro.antiyoy.factor_yio.FactorYio;
import yio.tro.antiyoy.stuff.GraphicsYio;
import yio.tro.antiyoy.stuff.RepeatYio;
import yio.tro.antiyoy.stuff.scroll_engine.SegmentYio;

public class TabsEngineYio {
    private double correction;
    private double cutOffset = 1.0d;
    private double cutSpeed = 1.0d;
    private double friction = 0.05d;
    FactorYio ignoreFactor = new FactorYio();
    private boolean inTouchMode = false;
    TeMagnet leftMagnet = null;
    private SegmentYio limits = new SegmentYio();
    private double magnetCatchDistance;
    private double magnetMaxDistance = 0.0d;
    private double magnetMaxPower = (0.01d * ((double) GraphicsYio.width));
    private double magnetPullDistance;
    ArrayList<TeMagnet> magnets = new ArrayList();
    private double maxSpeed = (0.15d * ((double) GraphicsYio.width));
    private int numberOfTabs = 1;
    RepeatYio<TabsEngineYio> repeatUpdateNearbyMagnets;
    TeMagnet rightMagnet = null;
    private SegmentYio slider = new SegmentYio();
    private double sliderCenter;
    private double softLimitOffset = 0.0d;
    private double speed;
    int swipeDelay = HttpStatus.SC_MULTIPLE_CHOICES;
    private double tabWidth = 0.0d;
    private long touchDownPos = 0;
    private long touchDownTime;

    public TabsEngineYio() {
        resetToBottom();
        this.repeatUpdateNearbyMagnets = new RepeatYio<TabsEngineYio>(this, 4) {
            public void performAction() {
                ((TabsEngineYio) this.parent).updateNearbyMagnets();
            }
        };
    }

    public void resetToBottom() {
        resetSpeed();
        this.correction = this.limits.f107a - this.slider.f107a;
        if (this.correction != 0.0d) {
            relocate(this.correction);
        }
    }

    public void resetToTop() {
        resetSpeed();
        this.correction = this.limits.f108b - this.slider.f108b;
        if (this.correction != 0.0d) {
            relocate(this.correction);
        }
    }

    public void giveImpulse(double impulse) {
        if (impulse > 0.0d && isOverTop()) {
            return;
        }
        if (impulse >= 0.0d || !isBelowBottom()) {
            this.speed += impulse;
            if (impulse > 0.0d) {
                relocate(1.1d * this.magnetCatchDistance);
            } else {
                relocate(-1.1d * this.magnetCatchDistance);
            }
        }
    }

    private void relocate(double delta) {
        SegmentYio segmentYio = this.slider;
        segmentYio.f107a += delta;
        segmentYio = this.slider;
        segmentYio.f108b += delta;
    }

    public void move() {
        this.ignoreFactor.move();
        applyNearbyMagnetsPower();
        if (this.speed == 0.0d) {
            softCorrection();
            return;
        }
        limitSpeed();
        relocate(this.speed);
        updateSpeed();
        hardCorrection();
        this.repeatUpdateNearbyMagnets.move();
    }

    private void applyNearbyMagnetsPower() {
        if (!this.inTouchMode && this.ignoreFactor.get() <= 0.0f) {
            updateSliderCenter();
            applyMagnetPower(this.leftMagnet);
            applyMagnetPower(this.rightMagnet);
        }
    }

    private void applyMagnetPower(TeMagnet magnet) {
        if (magnet != null) {
            double distance = Math.abs(((double) magnet.f109x) - this.sliderCenter);
            if (distance > this.magnetMaxDistance) {
                return;
            }
            if (this.speed != 0.0d || distance != 0.0d) {
                if (distance > this.magnetPullDistance) {
                    double ratio = 1.0d - (distance / this.magnetMaxDistance);
                    if (ratio < 0.25d) {
                        ratio = 0.25d;
                    }
                    double power = this.magnetMaxPower * ratio;
                    if (((double) magnet.f109x) < this.sliderCenter) {
                        this.speed -= power;
                    } else {
                        this.speed += power;
                    }
                } else if (distance > this.magnetCatchDistance) {
                    relocate(0.2d * (((double) magnet.f109x) - this.sliderCenter));
                    if ((this.sliderCenter - ((double) magnet.f109x)) * this.speed < 0.0d) {
                        resetSpeed();
                    }
                } else {
                    relocate(((double) magnet.f109x) - this.sliderCenter);
                    resetSpeed();
                }
            }
        }
    }

    public int getCurrentTabIndex() {
        updateSliderCenter();
        return (int) (this.sliderCenter / this.slider.getLength());
    }

    private void updateNearbyMagnets() {
        this.leftMagnet = null;
        this.rightMagnet = null;
        updateSliderCenter();
        Iterator it = this.magnets.iterator();
        while (it.hasNext()) {
            TeMagnet magnet = (TeMagnet) it.next();
            if (((double) magnet.f109x) < this.sliderCenter) {
                this.leftMagnet = magnet;
            } else {
                this.rightMagnet = magnet;
                return;
            }
        }
    }

    public void swipeTab(int direction) {
        this.ignoreFactor.setValues(1.0d, 0.0d);
        this.ignoreFactor.destroy(1, 5.0d);
        giveImpulse(((double) (direction * 7)) * this.magnetMaxPower);
        setSpeed(((double) (direction * 11)) * this.magnetMaxPower);
    }

    private void updateSliderCenter() {
        this.sliderCenter = (this.slider.f107a + this.slider.f108b) / 2.0d;
    }

    public TeMagnet getLeftMagnet() {
        return this.leftMagnet;
    }

    public TeMagnet getRightMagnet() {
        return this.rightMagnet;
    }

    private void limitSpeed() {
        if (this.speed > this.maxSpeed) {
            this.speed = this.maxSpeed;
        }
        if (this.speed < (-this.maxSpeed)) {
            this.speed = -this.maxSpeed;
        }
    }

    private void updateSpeed() {
        this.speed *= 1.0d - this.friction;
        if (this.speed == 0.0d) {
            return;
        }
        if (this.inTouchMode || Math.abs(this.speed) < this.cutSpeed) {
            resetSpeed();
        }
    }

    public void hardCorrection() {
        this.correction = (this.limits.f107a - this.softLimitOffset) - this.slider.f107a;
        if (this.correction > 0.0d) {
            relocate(this.correction);
            resetSpeed();
            return;
        }
        this.correction = (this.limits.f108b + this.softLimitOffset) - this.slider.f108b;
        if (this.correction < 0.0d) {
            relocate(this.correction);
            resetSpeed();
        }
    }

    public boolean isInSoftCorrectionMode() {
        if (this.slider.f107a >= this.limits.f107a && this.slider.f108b <= this.limits.f108b) {
            return false;
        }
        return true;
    }

    private boolean softCorrection() {
        if (this.inTouchMode || this.speed != 0.0d) {
            return false;
        }
        this.correction = this.limits.f107a - this.slider.f107a;
        if (this.correction <= 0.0d) {
            this.correction = this.limits.f108b - this.slider.f108b;
            if (this.correction >= 0.0d) {
                return false;
            }
            if (this.correction > (-this.cutOffset)) {
                resetToTop();
                return true;
            }
            relocate(this.correction * 0.1d);
            return true;
        } else if (this.correction < this.cutOffset) {
            resetToBottom();
            return true;
        } else {
            relocate(this.correction * 0.1d);
            return true;
        }
    }

    private void resetSpeed() {
        setSpeed(0.0d);
    }

    public SegmentYio getSlider() {
        return this.slider;
    }

    public double getSpeed() {
        return this.speed;
    }

    public void setFriction(double friction) {
        this.friction = friction;
    }

    public void setLimits(double a, double b) {
        this.limits.set(a, b);
    }

    public SegmentYio getLimits() {
        return this.limits;
    }

    public void setSoftLimitOffset(double softLimitOffset) {
        this.softLimitOffset = softLimitOffset;
    }

    public void setCutSpeed(double cutSpeed) {
        this.cutSpeed = cutSpeed;
    }

    public boolean isBelowBottom() {
        return this.slider.f107a < this.limits.f107a;
    }

    public boolean isOverTop() {
        return this.slider.f108b > this.limits.f108b;
    }

    public void onTouchDown() {
        this.touchDownTime = System.currentTimeMillis();
        updateSliderCenter();
        this.touchDownPos = (long) this.sliderCenter;
        this.inTouchMode = true;
    }

    public void onTouchUp() {
        if (this.inTouchMode) {
            this.inTouchMode = false;
            if (System.currentTimeMillis() - this.touchDownTime < ((long) this.swipeDelay)) {
                swipe();
            }
        }
    }

    private void swipe() {
        updateSliderCenter();
        if (this.sliderCenter - ((double) this.touchDownPos) > 0.0d) {
            setSpeed(6.0d * this.magnetMaxPower);
        } else {
            setSpeed(-6.0d * this.magnetMaxPower);
        }
    }

    public void setSpeed(double speed) {
        if (this.ignoreFactor.get() <= 0.0f) {
            this.speed = speed;
        }
    }

    public void setSlider(double a, double b) {
        this.slider.set(a, b);
    }

    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public void setNumberOfTabs(int numberOfTabs) {
        this.numberOfTabs = numberOfTabs;
        this.tabWidth = this.limits.getLength() / ((double) numberOfTabs);
        createMagnets();
    }

    private void createMagnets() {
        float x = (float) (this.limits.f107a + (this.tabWidth / 2.0d));
        for (int i = 0; i < this.numberOfTabs; i++) {
            TeMagnet teMagnet = new TeMagnet();
            teMagnet.f109x = x;
            this.magnets.add(teMagnet);
            x = (float) (((double) x) + this.tabWidth);
        }
        this.magnetMaxDistance = this.tabWidth / 2.0d;
        this.magnetPullDistance = this.magnetMaxDistance / 5.0d;
        this.magnetCatchDistance = Math.max(this.magnetPullDistance / 25.0d, 1.0d);
        updateNearbyMagnets();
    }

    public ArrayList<TeMagnet> getMagnets() {
        return this.magnets;
    }

    public void setInTouchMode(boolean inTouchMode) {
        this.inTouchMode = inTouchMode;
    }

    public void setMagnetMaxPower(double magnetMaxPower) {
        this.magnetMaxPower = magnetMaxPower;
    }

    public void teleportToTab(int tabIndex) {
        resetToBottom();
        relocate(((double) tabIndex) * this.tabWidth);
    }

    public void setSwipeDelay(int swipeDelay) {
        this.swipeDelay = swipeDelay;
    }

    public String toString() {
        return "[TabsEngine limit" + this.limits + ", slider" + this.slider + "]";
    }
}
