package org.appledash.glpong;

import me.jordin.deltoid.vector.Vec3;
import org.appledash.glpong.structures.Ball;
import org.appledash.glpong.structures.Paddle;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by appledash on 7/4/17.
 * Blackjack is best pony.
 */
public class GLPong {
    private Ball ball;
    private Paddle[] paddles = new Paddle[2];

    public GLPong() {
        try {
            this.setupDisplay();
        } catch (LWJGLException e) {
            throw new RuntimeException("Failed to setup LWJGL Display!");
        }

        this.ball = new Ball(new Vec3(Display.getWidth() / 2, Display.getHeight() / 2));
        this.paddles[0] = new Paddle(new Vec3(50, Display.getHeight() / 2));
        this.paddles[1] = new Paddle(new Vec3(Display.getWidth() - 50, Display.getHeight() / 2));
    }

    private void setupDisplay() throws LWJGLException {
        Properties properties = new Properties();
        try {
            properties.load(this.getClass().getClassLoader().getResourceAsStream("properties/glpong.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Display.setTitle("GLPong v" + properties.getProperty("version"));
        Display.setDisplayMode(new DisplayMode(1024, 768));
        Display.create();
        Keyboard.enableRepeatEvents(true);
        Display.setVSyncEnabled(true);

        GL11.glViewport(0, 0, 1024, 768);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, 1024, 768, 0, -1, 1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
    }

    public void enterGameLoop() {
        long prevTime = System.currentTimeMillis();
        while (!Display.isCloseRequested()) {
            double deltaTime = (System.currentTimeMillis() - prevTime) / 1000D;
            prevTime = System.currentTimeMillis();
            this.draw();

            ball.update(this, deltaTime);

            Display.update();

            this.controlPaddles(deltaTime);
        }
    }

    private void controlPaddles(double deltaTime) {
        int leftDirection = 0;
        int rightDirection = 0;

        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            leftDirection++;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            leftDirection--;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
            rightDirection++;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
            rightDirection--;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_B)) {
            double deltaY1 = ball.getPosition().y - this.paddles[0].getPosition().y;
            double deltaY2 = ball.getPosition().y - this.paddles[1].getPosition().y;
            double minDeltaY = 50;
            if (Math.abs(deltaY1) > minDeltaY && ball.getVelocity().x < 0) {
                leftDirection = (deltaY1) < 0 ? 1 : -1;
            }

            if (Math.abs(deltaY2) > minDeltaY && ball.getVelocity().x > 0) {
                rightDirection = (deltaY2) < 0 ? 1 : -1;
            }
        }

        this.paddles[0].move(leftDirection, deltaTime);
        this.paddles[1].move(rightDirection, deltaTime);
    }

    private void draw() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        for (Paddle p : this.paddles) {
            p.draw();
        }

        ball.draw();
    }

    public Paddle[] getPaddles() {
        return this.paddles;
    }

    public static void main(String[] args) {
        GLPong game = new GLPong();
        game.enterGameLoop();
    }
}
