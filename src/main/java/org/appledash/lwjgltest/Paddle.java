package org.appledash.lwjgltest;

import me.jordin.deltoid.vector.Vec3;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

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

    public Paddle(Vec3 position) {
        this.position = position;
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

    public void draw() {
        GL11.glPushMatrix();
        GL11.glTranslated(position.x, position.y, 0);

        double size = 5;
        GL11.glLineWidth(1);
        GL11.glColor4d(0.2, 0.2, 0.2, 1.0);
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex2d(-size, 0);
        GL11.glVertex2d(size, 0);
        GL11.glEnd();

        GL11.glColor4d(1.0, 1.0, 1.0, 1.0);
        GL11.glLineWidth(2);
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex2d(0, - height / 2);
        GL11.glVertex2d(0, height / 2);
        GL11.glEnd();

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
