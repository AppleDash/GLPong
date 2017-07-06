package org.appledash.glpong.utils;

/**
 * Created by appledash on 7/5/17.
 * Blackjack is best pony.
 */
public class Timer {
    private final double startTime; // System.currentTimeMillis() in seconds that the timer started at.
    private double secondsPassed; // Seconds passed since the game started.

    public Timer() {
        this.startTime = System.currentTimeMillis() / 1000;
    }

    public void update() {
        double curTime = System.currentTimeMillis() / 1000;
        this.secondsPassed = curTime - this.startTime;
    }

    public double getSecondsPassed() {
        return this.secondsPassed;
    }
}
