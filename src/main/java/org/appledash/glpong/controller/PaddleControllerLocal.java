package org.appledash.glpong.controller;

import org.appledash.glpong.GLPong;
import org.appledash.glpong.structures.Paddle;
import org.lwjgl.input.Keyboard;

/**
 * Created by appledash on 7/18/17.
 * Blackjack is best pony.
 */
public class PaddleControllerLocal extends PaddleController {
    private final GLPong pong;
    private final int upKey;
    private final int downKey;

    public PaddleControllerLocal(GLPong pong, Paddle paddle, int upKey, int downKey) {
        super(paddle);
        this.pong = pong;
        this.upKey = upKey;
        this.downKey = downKey;
    }

    @Override
    public void controlPaddle(double deltaTime) {
        int leftDirection = 0;

        if (Keyboard.isKeyDown(this.upKey)) {
            leftDirection++;
        }
        if (Keyboard.isKeyDown(this.downKey)) {
            leftDirection--;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_B)) {
            double deltaY1 = this.pong.getPrimaryBall().getPosition().y - this.paddle.getPosition().y;
            // double deltaY2 = ball.getPosition().y - this.paddles[RIGHT].getPosition().y;
            double minDeltaY = 50;
            if (Math.abs(deltaY1) > minDeltaY && this.pong.getPrimaryBall().getVelocity().x < 0) {
                leftDirection = (deltaY1) < 0 ? 1 : -1;
            }

            //if (Math.abs(deltaY2) > minDeltaY && ball.getVelocity().x > 0) {
            //    rightDirection = (deltaY2) < 0 ? 1 : -1;
            //}
        }

        this.paddle.move(leftDirection, deltaTime);
    }
}
