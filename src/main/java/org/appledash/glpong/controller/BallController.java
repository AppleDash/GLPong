package org.appledash.glpong.controller;

import org.appledash.glpong.structures.Ball;

/**
 * Created by appledash on 7/18/17.
 * Blackjack is best pony.
 */
public abstract class BallController {
    protected final Ball ball;

    public BallController(Ball ball) {
        this.ball = ball;
    }

    public abstract void controlBall(double deltaTime);
}
