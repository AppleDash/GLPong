package org.appledash.glpong.structures;

import me.jordin.deltoid.vector.Vec3;
import org.appledash.glpong.GLPong;
import org.appledash.glpong.utils.RenderUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import java.util.function.Predicate;

/**
 * Created by appledash on 7/4/17.
 * Blackjack is best pony.
 */
// TODO: use a PhysicsObject?
public class Paddle {
    private static final Vec3 paddleSpeed = new Vec3(0, -800);
    private double height = 100;
    private Vec3 position;
    private Vec3 velocity;

    private Predicate<Vec3> motionValidForCollision;

    public Paddle(Vec3 position, Predicate<Vec3> motionValidForCollision) {
        this.position = position;
        this.motionValidForCollision = motionValidForCollision;
    }

    public void move(int direction, double deltaTime) {
        velocity = paddleSpeed.scale(direction);
        position = position.add(velocity.scale(deltaTime));

        // This allows for half of the paddle to go off the screen, this is intentional (allows for more precise hits)
        if (position.y > Display.getHeight()) {
            position = new Vec3(position.x, Display.getHeight());
            velocity = Vec3.ORIGIN;
        } else if (position.y < 0) {
            position = new Vec3(position.x, 0);
            velocity = Vec3.ORIGIN;
        }
    }

    public void update(GLPong pong, double deltaTime) {
        for (Ball ball : pong.getBalls()) {
            Vec3 pos = ball.getPosition();
            Vec3 vel = ball.getVelocity();
            if (!motionValidForCollision.test(vel)) {
                continue;
            }
            double radius = ball.getRadius();

            double halfHeight = this.getHeight() / 2;
            Vec3 deltaPos = this.getPosition().subtract(pos);

            boolean correctX = ((pos.x - radius) < this.getPosition().x)
                    && ((pos.x + radius) > this.getPosition().x);

            boolean correctY = ((pos.y + radius) > (this.getPosition().y - halfHeight))
                    && ((pos.y - radius) < (this.getPosition().y + halfHeight));

            boolean collisionDetected = correctX && correctY;
            if (collisionDetected) {
                double deltaY = deltaPos.y;
                double additionalMotionY = vel.length() * (-deltaY * 0.01);
                Vec3 newVelocity = new Vec3(-vel.x * 1.4, vel.y + additionalMotionY).add(this.getVelocity().scale(0.01));
                ball.getPhysicsObject().setVelocity(newVelocity);
            }
        }
    }


    public void draw() {
        GL11.glPushMatrix();
        GL11.glTranslated(position.x, position.y, 0);

        double size = 5;
        GL11.glColor4d(0.2, 0.2, 0.2, 1.0);
        GL11.glLineWidth(1);
        RenderUtils.renderLine(-size, 0, size, 0);

        GL11.glColor4d(1.0, 1.0, 1.0, 1.0);
        GL11.glLineWidth(2);
        RenderUtils.renderLine(0, - height / 2, 0,  height / 2);

        GL11.glTranslated(-position.x, -position.y, 0);
        GL11.glPopMatrix();
    }

    public double getHeight() {
        return height;
    }

    public Vec3 getPosition() {
        return position;
    }

    public Vec3 getVelocity() {
        return velocity;
    }
}
