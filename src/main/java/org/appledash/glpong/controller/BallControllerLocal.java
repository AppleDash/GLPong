package org.appledash.glpong.controller;

import org.appledash.glpong.GLPong;
import org.appledash.glpong.structures.Ball;

/**
 * Created by appledash on 7/18/17.
 * Blackjack is best pony.
 */
public class BallControllerLocal extends BallController {
    private final GLPong pong;

    public BallControllerLocal(GLPong pong, Ball ball) {
        super(ball);
        this.pong = pong;
    }

    @Override
    public void controlBall(double deltaTime) {
        this.ball.updateLocally(this.pong.getPaddles(), deltaTime);
    }
}
