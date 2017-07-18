package org.appledash.glpong.gui;

import org.appledash.glpong.GLPong;

/**
 * Created by appledash on 7/18/17.
 * Blackjack is best pony.
 */
public abstract class Gui {
    protected final GLPong game;

    public Gui(GLPong game) {
        this.game = game;
    }

    public abstract void drawGui();
    public abstract void handleInput();
}
