package org.appledash.glpong.structures;

import me.jordin.deltoid.vector.Vec3;
import me.jordin.deltoid.world.PhysicsObject;
import org.appledash.glpong.utils.RenderUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

/**
 * Created by appledash on 7/4/17.
 * Blackjack is best pony.
 */
public class Ball {
    private static final int RADIUS = 15;
    private static final int SEGMENTS = 300;
    private static final double MAX_VELOCITY = 300;
    private static final double RESISTANCE = 0.99;

    private PhysicsObject physicsObject;
    private Vec3 origin;

    public Ball(Vec3 origin) {
        this.origin = origin;
        this.respawn();
    }

    private void respawn() {
        // Vec3 randomVel = Vec3.X_AXIS.scale(MAX_VELOCITY).rotateAboutZ(Math.random() * TAU);

        Vec3 randomVel = new Vec3((Math.random() - 0.5) * 3, Math.random() - 0.5);
        this.physicsObject = new PhysicsObject(origin, randomVel.normalize(MAX_VELOCITY), Vec3.ORIGIN);

        // this.physicsObject = new PhysicsObject(origin, new Vec3(-100, 0, 0), Vec3.ORIGIN);
        // this.physicsObject = new PhysicsObject(origin, Vec3.X_AXIS.scale(-200), Vec3.ORIGIN);
    }

    public void draw() {
        RenderUtils.renderCircle(physicsObject.getPosition(), RADIUS, SEGMENTS);
    }

    public void updateLocally(Paddle[] paddles, double deltaTime) {
        this.physicsObject.update(deltaTime);

        Vec3 pos = physicsObject.getPosition();
        Vec3 vel = physicsObject.getVelocity();

        for (Paddle paddle : paddles) {
            double halfHeight = paddle.getHeight() / 2;
            Vec3 deltaPos = paddle.getPosition().subtract(pos);

            boolean correctX = ((pos.x - RADIUS) < paddle.getPosition().x)
                    && ((pos.x + RADIUS) > paddle.getPosition().x);

            boolean correctY = ((pos.y + RADIUS) > (paddle.getPosition().y - halfHeight))
                    && ((pos.y - RADIUS) < (paddle.getPosition().y + halfHeight));

            boolean collisionDetected = correctX && correctY;

            if (collisionDetected) {
                double deltaY = deltaPos.y;
                double additionalMotionY = vel.length() * (-deltaY * 0.01);
                Vec3 newVelocity = new Vec3(-vel.x * 1.4, vel.y + additionalMotionY).add(paddle.getVelocity().scale(0.01));
                physicsObject.setVelocity(newVelocity);
            }
        }

        if (pos.x < 0 || pos.x > Display.getWidth() || Keyboard.isKeyDown(Keyboard.KEY_0)) {
            this.respawn();
        }

        if ((pos.y - RADIUS < 0 && vel.y < 0) || (pos.y + RADIUS > Display.getHeight() && vel.y > 0)) {
            physicsObject.setVelocity(new Vec3(vel.x, -vel.y));
        }

        if (physicsObject.getVelocity().length() > MAX_VELOCITY) {
            physicsObject.setVelocity(physicsObject.getVelocity().scale(RESISTANCE));
        }
    }

    public Vec3 getPosition() {
        return physicsObject.getPosition();
    }

    public Vec3 getVelocity() {
        return physicsObject.getVelocity();
    }

    public PhysicsObject getPhysicsObject() {
        return physicsObject;
    }

    public double getRadius() {
        return RADIUS;
    }
}
