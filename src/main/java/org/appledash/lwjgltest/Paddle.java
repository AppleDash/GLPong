package org.appledash.lwjgltest;

import me.jordin.deltoid.vector.Vec3;
import org.lwjgl.opengl.GL11;

/**
 * Created by appledash on 7/4/17.
 * Blackjack is best pony.
 */
public class Paddle {
    private int height = 100;
    private Vec3 position;

    public Paddle(Vec3 position) {
        this.position = position;
    }

    public void move(Vec3 vec3) {
        this.position = this.position.add(vec3);
    }

    public void draw() {
        GL11.glPushMatrix();
        GL11.glTranslated(position.x, position.y, 0);
        GL11.glColor4d(1.0, 1.0, 1.0, 1.0);
        GL11.glLineWidth(2);
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex2d(0, -(height / 2));
        GL11.glVertex2d(0, (height / 2));
        GL11.glEnd();
        GL11.glTranslated(-position.x, -position.y, 0);
        GL11.glPopMatrix();
    }

    public int getHeight() {
        return height;
    }

    public Vec3 getPosition() {
        return position;
    }
}
