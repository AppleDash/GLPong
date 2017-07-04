package org.appledash.lwjgltest;

import me.jordin.deltoid.vector.Vec3;
import me.jordin.deltoid.world.PhysicsObject;
import org.lwjgl.opengl.GL11;

/**
 * Created by appledash on 7/4/17.
 * Blackjack is best pony.
 */
public class Ball {
    private static final int RADIUS = 15;
    private long prevTime = System.currentTimeMillis();

    private PhysicsObject physicsObject;

    public Ball(Vec3 origin) {
        this.physicsObject = new PhysicsObject(origin, new Vec3(-100, 0, 0), Vec3.ORIGIN);
    }

    public void draw() {
        int num_segments = 300;
        double theta = -2 * 3.1415926 / (float)(num_segments);
        double tangetial_factor = Math.tan(theta);//calculate the tangential factor

        double radial_factor = Math.cos(theta);//calculate the radial factor

        float x = RADIUS;//we start at angle = 0

        float y = 0;

        GL11.glTranslated(physicsObject.getPosition().x, physicsObject.getPosition().y, 0);

        GL11.glColor3d(1.0, 0.0, 0.0);
        GL11.glBegin(GL11.GL_TRIANGLE_FAN);

        for(int ii = 0; ii < num_segments; ii++)
        {
            GL11.glVertex2f(x, y);//output vertex

            //calculate the tangential vector
            //remember, the radial vector is (x, y)
            //to get the tangential vector we flip those coordinates and negate one of them

            float tx = -y;
            float ty = x;

            //add the tangential vector

            x += tx * tangetial_factor;
            y += ty * tangetial_factor;

            //correct using the radial factor

            x *= radial_factor;
            y *= radial_factor;
        }

        GL11.glEnd();

        GL11.glTranslated(-physicsObject.getPosition().x, -physicsObject.getPosition().y, 0);
    }

    public void update() {

        Vec3 pos = physicsObject.getPosition();
        Vec3 vel = physicsObject.getVelocity();

        for (Paddle paddle : new Paddle[] { Main.leftPaddle }) {

        }

        this.physicsObject.update((System.currentTimeMillis() - prevTime) / 1000D);
        this.prevTime = System.currentTimeMillis();
    }
}
