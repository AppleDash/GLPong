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
    private final int speedMultiplier = 15;

    public GLPong() {
        try {
            this.setupDisplay();
        } catch (LWJGLException e) {
            throw new RuntimeException("Failed to setup LWJGL Display!");
        }

        this.ball = new Ball(new Vec3(Display.getWidth() / 2, Display.getHeight() / 2, 0));
        this.leftPaddle = new Paddle(new Vec3(50, 50, 0));
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
        while (!Display.isCloseRequested()) {
            this.draw();

            ball.update(this);

            Display.update();
            this.handleKeyboardEvents();
        }
    }

    private void draw() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        leftPaddle.draw();
        ball.draw();
    }

    private void handleKeyboardEvents() {
        while (Keyboard.next()) {
            if (Keyboard.getEventKey() == Keyboard.KEY_S) {
                leftPaddle.move(new Vec3(0, speedMultiplier, 0));
            } else if (Keyboard.getEventKey() == Keyboard.KEY_W) {
                leftPaddle.move(new Vec3(0, -speedMultiplier, 0));
            }
        }
    }

    public Paddle[] getPaddles() {
        return new Paddle[]{leftPaddle};
    }
}
