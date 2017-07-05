package org.appledash.lwjgltest;

import me.jordin.deltoid.vector.Vec3;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

/**
 * Created by appledash on 7/4/17.
 * Blackjack is best pony.
 */
public class GLPong {
    private Ball ball;
    private Paddle leftPaddle;
    private Paddle rightPaddle;

    public GLPong() {
        try {
            this.setupDisplay();
        } catch (LWJGLException e) {
            throw new RuntimeException("Failed to setup LWJGL Display!");
        }

        this.ball = new Ball(new Vec3(Display.getWidth() / 2, Display.getHeight() / 2));
        this.leftPaddle = new Paddle(new Vec3(50, Display.getHeight() / 2));
        this.rightPaddle = new Paddle(new Vec3(Display.getWidth() - 50, Display.getHeight() / 2));
    }

    private void setupDisplay() throws LWJGLException {
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

            this.controlPadles(deltaTime);
        }
    }

    private void controlPadles(double deltaTime) {
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
            double deltaY1 = ball.getPosition().y - leftPaddle.getPosition().y;
            double deltaY2 = ball.getPosition().y - rightPaddle.getPosition().y;
            double minDeltaY = 50;
            if (Math.abs(deltaY1) > minDeltaY && ball.getVelocity().x < 0) {
                leftDirection = (deltaY1) < 0 ? 1 : -1;
            }

            if (Math.abs(deltaY2) > minDeltaY && ball.getVelocity().x > 0) {
                rightDirection = (deltaY2) < 0 ? 1 : -1;
            }
        }

        leftPaddle.move(leftDirection, deltaTime);
        rightPaddle.move(rightDirection, deltaTime);
    }

    private void draw() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        leftPaddle.draw();
        rightPaddle.draw();
        ball.draw();
    }

    public Paddle[] getPaddles() {
        return new Paddle[]{leftPaddle, rightPaddle};
    }
}
