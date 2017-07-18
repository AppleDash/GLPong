package org.appledash.glpong.utils;

import me.jordin.deltoid.vector.Vec3;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.TrueTypeFont;

import java.awt.*;

/**
 * Created by Jordin on 7/5/2017.
 * Jordin is still best hacker.
 */
public class RenderUtils {
    private static final double TAU = 2 * Math.PI;
    private static final int MAX_FONT_SIZE = 40; // Anything bigger glitches out
    private static final TrueTypeFont[] fontPool = new TrueTypeFont[MAX_FONT_SIZE];

    private static TrueTypeFont getFont(int size) {
        if (size > MAX_FONT_SIZE) {
            throw new IllegalArgumentException("Font size must be at most " + MAX_FONT_SIZE);
        }

        if (fontPool[size - 1] != null) {
            return fontPool[size - 1];
        }

        return fontPool[size - 1] = new TrueTypeFont(new Font("Verdana", Font.BOLD, size), true);
    }

    public static void drawString(String str, double x, double y, int size) {
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        getFont(size).drawString((float) x, (float) y, str);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    public static void drawString(String str, double x, double y) {
        drawString(str, x, y, 16);
    }

    public static int getStringWidth(String str, int fontSize) {
        return getFont(fontSize).getWidth(str);
    }

    public static void renderLine(double startX, double startY, double endX, double endY) {
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex2d(startX, startY);
        GL11.glVertex2d(endX, endY);
        GL11.glEnd();
    }

    public static void renderCircle(Vec3 position, double radius, int segments) {
        double theta = - TAU / segments;
        double tangentialFactor = Math.tan(theta);//calculate the tangential factor
        double radialFactor = Math.cos(theta);//calculate the radial factor

        float x = (float) radius; // we start at angle = 0
        float y = 0;

        GL11.glTranslated(position.x, position.y, 0);

        GL11.glColor3d(1.0, 0.0, 0.0);
        GL11.glBegin(GL11.GL_TRIANGLE_FAN);

        for(int ii = 0; ii < segments; ii++) {
            GL11.glVertex2f(x, y);//output vertex

            //calculate the tangential vector
            //remember, the radial vector is (x, y)
            //to get the tangential vector we flip those coordinates and negate one of them

            float tx = -y;
            float ty = x;

            //add the tangential vector

            x += tx * tangentialFactor;
            y += ty * tangentialFactor;

            //correct using the radial factor

            x *= radialFactor;
            y *= radialFactor;
        }
        GL11.glEnd();

        GL11.glTranslated(-position.x, -position.y, 0);
    }
}
