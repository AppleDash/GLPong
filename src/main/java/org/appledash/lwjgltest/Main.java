package org.appledash.lwjgltest;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.GL11;

/**
 * Created by appledash on 7/4/17.
 * Blackjack is best pony.
 */
public class Main {

    public static void main(String[] args) throws LWJGLException, InterruptedException {
        GLPong game = new GLPong();
        game.enterGameLoop();
    }

    private static void drawLine(double startX, double startY, double endX, double endY) {
        GL11.glVertex2d(startX, startY);
        GL11.glVertex2d(endX, endY);
    }
}
