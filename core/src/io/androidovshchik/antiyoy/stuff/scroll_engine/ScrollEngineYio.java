package io.androidovshchik.antiyoy.stuff.scroll_engine;

import yio.tro.antiyoy.stuff.GraphicsYio;

public class ScrollEngineYio {
    private boolean blockMovement;
    private boolean canSoftCorrect;
    private double correction;
    private double cutOffset;
    private double cutSpeed;
    private double friction;
    private SegmentYio limits = new SegmentYio();
    private double maxSpeed;
    private SegmentYio slider = new SegmentYio();
    private double softLimitOffset;
    private double speed;

    public ScrollEngineYio() {
        resetToBottom();
        this.friction = 0.0d;
        this.softLimitOffset = 0.0d;
        this.cutSpeed = 1.0d;
        this.cutOffset = 1.0d;
        this.canSoftCorrect = true;
        this.blockMovement = false;
        this.maxSpeed = 0.15d * ((double) GraphicsYio.width);
    }

    public void resetToBottom() {
        resetSpeed();
        this.correction = this.limits.f107a - this.slider.f107a;
        if (this.correction != 0.0d) {
            boolean b = this.blockMovement;
            this.blockMovement = false;
            relocate(this.correction);
            this.blockMovement = b;
        }
    }

    public void resetToTop() {
        resetSpeed();
        this.correction = this.limits.f108b - this.slider.f108b;
        if (this.correction != 0.0d) {
            boolean b = this.blockMovement;
            this.blockMovement = false;
            relocate(this.correction);
            this.blockMovement = b;
        }
    }

    public void giveImpulse(double impulse) {
        if (impulse > 0.0d && isOverTop()) {
            return;
        }
        if (impulse >= 0.0d || !isBelowBottom()) {
            this.speed += impulse;
        }
    }

    private void relocate(double delta) {
        if (!this.blockMovement) {
            SegmentYio segmentYio = this.slider;
            segmentYio.f107a += delta;
            segmentYio = this.slider;
            segmentYio.f108b += delta;
        }
    }

    public void move() {
        if (!this.blockMovement) {
            if (this.speed == 0.0d) {
                softCorrection();
                return;
            }
            limitSpeed();
            relocate(this.speed);
            updateSpeed();
            hardCorrection();
        }
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
        if (Math.abs(this.speed) < this.cutSpeed) {
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

    private boolean softCorrection() {
        if (!this.canSoftCorrect || this.speed != 0.0d) {
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
        this.speed = 0.0d;
    }

    public SegmentYio getSlider() {
        return this.slider;
    }

    public void setFriction(double friction) {
        this.friction = friction;
    }

    public void setLimits(double a, double b) {
        this.limits.set(a, b);
        updateBlockMovement();
    }

    private void updateBlockMovement() {
        this.blockMovement = this.slider.getLength() > this.limits.getLength();
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

    public void updateCanSoftCorrect(boolean canSoftCorrect) {
        this.canSoftCorrect = canSoftCorrect;
    }

    public boolean isBelowBottom() {
        return this.slider.f107a < this.limits.f107a;
    }

    public boolean isOverTop() {
        return this.slider.f108b > this.limits.f108b;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setSlider(double a, double b) {
        this.slider.set(a, b);
        updateBlockMovement();
    }

    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public String toString() {
        return "[ScrollEngine limit" + this.limits + ", slider" + this.slider + "]";
    }
}
