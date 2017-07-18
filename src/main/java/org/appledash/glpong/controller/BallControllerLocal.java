package org.appledash.glpong.controller;

import me.jordin.deltoid.vector.Vec3;
import org.appledash.glpong.GLPong;
import org.appledash.glpong.structures.Ball;
import org.appledash.glpong.structures.Paddle;

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
        for (Paddle paddle : pong.getPaddles()) {
            for (Ball ball : pong.getBalls()) {
                Vec3 pos = ball.getPosition();
                Vec3 vel = ball.getVelocity();
                if (!paddle.motionValidForCollision.test(vel)) {
                    continue;
                }
                double radius = ball.getRadius();

                double halfHeight = paddle.getHeight() / 2;
                Vec3 deltaPos = paddle.getPosition().subtract(pos);

                boolean correctX = ((pos.x - radius) < paddle.getPosition().x)
                        && ((pos.x + radius) > paddle.getPosition().x);

                boolean correctY = ((pos.y + radius) > (paddle.getPosition().y - halfHeight))
                        && ((pos.y - radius) < (paddle.getPosition().y + halfHeight));

                boolean collisionDetected = correctX && correctY;
                if (collisionDetected) {
                    double deltaY = deltaPos.y;
                    double additionalMotionY = vel.length() * (-deltaY * 0.01);
                    Vec3 newVelocity = new Vec3(-vel.x * 1.4, vel.y + additionalMotionY).add(paddle.getVelocity().scale(0.01));
                    ball.getPhysicsObject().setVelocity(newVelocity);
                }
            }
        }
        this.ball.updateLocally(this.pong.getPaddles(), deltaTime);
    }
}
