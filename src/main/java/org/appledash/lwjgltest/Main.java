package org.appledash.lwjgltest;

import org.lwjgl.LWJGLException;

/**
 * Created by appledash on 7/4/17.
 * Blackjack is best pony.
 */
public class Main {

    public static void main(String[] args) throws LWJGLException, InterruptedException {
        GLPong game = new GLPong();
        game.enterGameLoop();
    }
}
