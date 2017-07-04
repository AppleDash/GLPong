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
public class Main {
    private static int speedMultiplier = 15;
    private static int paddleY = 0;
    public static Ball ball;
    public static Paddle leftPaddle;

    public static void main(String[] args) throws LWJGLException, InterruptedException {
        Display.setDisplayMode(new DisplayMode(1024, 768));
        Display.create();
        Keyboard.enableRepeatEvents(true);
        Display.setVSyncEnabled(true);

        ball = new Ball(new Vec3(Display.getWidth() / 2, Display.getHeight() / 2, 0));
        leftPaddle = new Paddle(new Vec3(50, 50, 0));

        GL11.glViewport(0, 0, 1024, 768);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, 1024, 768, 0, -1, 1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        while (!Display.isCloseRequested()) {
            // System.out.println("a frame is happening guys");
            while (Keyboard.next()) {
                if (Keyboard.getEventKey() == Keyboard.KEY_S) {
                    leftPaddle.move(new Vec3(0, speedMultiplier, 0));
                } else if (Keyboard.getEventKey() == Keyboard.KEY_W) {
                    leftPaddle.move(new Vec3(0, -speedMultiplier, 0));
                }
            }

            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

            leftPaddle.draw();

            ball.update();
            ball.draw();

            Display.update();
        }
    }

    private static void drawLine(double startX, double startY, double endX, double endY) {
        GL11.glVertex2d(startX, startY);
        GL11.glVertex2d(endX, endY);
    }
}
