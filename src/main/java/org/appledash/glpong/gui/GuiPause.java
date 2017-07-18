package org.appledash.glpong.gui;

import org.appledash.glpong.GLPong;
import org.lwjgl.opengl.Display;

/**
 * Created by appledash on 7/18/17.
 * Blackjack is best pony.
 */
public class GuiPause extends Gui {
    public GuiPause(GLPong game) {
        super(game);
    }

    @Override
    public void drawGui() {
        int width = Display.getWidth();
        int height = Display.getHeight();

        this.game.drawString(width / 2, height / 2, "Game paused", 40);
    }

    @Override
    public void handleInput() {

    }
}
