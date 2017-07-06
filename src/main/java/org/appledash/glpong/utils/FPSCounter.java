package org.appledash.glpong.utils;

/**
 * Created by appledash on 7/5/17.
 * Blackjack is best pony.
 */
public class FPSCounter {
    private int framesRendered;
    private double startTime;
    private final Timer timer;
    private float fps;

    public FPSCounter(Timer timer) {
        this.startTime = timer.getSecondsPassed();
        this.timer = timer;
    }

    public void update() {
        if (this.timer.getSecondsPassed() - this.startTime > 0.25f && this.framesRendered > 10) {
            this.fps = (float) (this.framesRendered / (this.timer.getSecondsPassed() - this.startTime));
            this.startTime = this.timer.getSecondsPassed();
            this.framesRendered = 0;
        }
    }

    public void incrementFramesRendered() {
        this.framesRendered++;
    }

    public float getFps() {
        return this.fps;
    }
}
