package org.appledash.glpong;

import me.jordin.deltoid.vector.Vec3;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.appledash.glpong.structures.Ball;
import org.appledash.glpong.structures.Paddle;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.TrueTypeFont;

import java.awt.*;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by appledash on 7/4/17.
 * Blackjack is best pony.
 */
public class GLPong {
    private static final Logger LOGGER = LogManager.getLogger("GLPong");
    // Array indexes for the left and right paddles.
    private static final int LEFT = 0;
    private static final int RIGHT = 1;

    private Ball ball;
    private Paddle[] paddles = new Paddle[2];

    private TrueTypeFont trueTypeFont;
    private long framesRendered;
    private double startTime;
    private double timePassed;
    private float fps;
    private double fpsStartTime;

    public GLPong(DisplayMode displayMode) {
        try {
            this.setupDisplay(displayMode);
        } catch (LWJGLException e) {
            throw new RuntimeException("Failed to setup LWJGL Display!");
        }

        this.ball = new Ball(new Vec3(Display.getWidth() / 2, Display.getHeight() / 2));
        this.paddles[LEFT] = new Paddle(new Vec3(50, Display.getHeight() / 2));
        this.paddles[RIGHT] = new Paddle(new Vec3(Display.getWidth() - 50, Display.getHeight() / 2));

        trueTypeFont = new TrueTypeFont(new Font("Verdana", Font.PLAIN, 16), true);

        this.startTime = System.currentTimeMillis() / 1000;
    }

    private void setupDisplay(DisplayMode displayMode) throws LWJGLException {
        Properties properties = new Properties();
        try {
            properties.load(this.getClass().getClassLoader().getResourceAsStream("properties/glpong.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Display.setTitle("GLPong v" + properties.getProperty("version"));
        Display.setDisplayMode(displayMode);
        Display.create();
        Keyboard.enableRepeatEvents(true);
        Display.setVSyncEnabled(true);

        GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, Display.getWidth(), Display.getHeight(), 0, -1, 1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
    }

    public void enterGameLoop() {
        long prevTime = System.currentTimeMillis();
        while (!Display.isCloseRequested()) {
            double deltaTime = (System.currentTimeMillis() - prevTime) / 1000D;
            this.timePassed += deltaTime;
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
            double deltaY1 = ball.getPosition().y - this.paddles[LEFT].getPosition().y;
            double deltaY2 = ball.getPosition().y - this.paddles[RIGHT].getPosition().y;
            double minDeltaY = 50;
            if (Math.abs(deltaY1) > minDeltaY && ball.getVelocity().x < 0) {
                leftDirection = (deltaY1) < 0 ? 1 : -1;
            }

            if (Math.abs(deltaY2) > minDeltaY && ball.getVelocity().x > 0) {
                rightDirection = (deltaY2) < 0 ? 1 : -1;
            }
        }

        this.paddles[LEFT].move(leftDirection, deltaTime);
        this.paddles[RIGHT].move(rightDirection, deltaTime);
    }

    private void draw() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        for (Paddle p : this.paddles) {
            p.draw();
        }

        ball.draw();

        double curTime = System.currentTimeMillis() / 1000;
        this.timePassed = curTime - this.startTime;

        if ((this.timePassed - this.fpsStartTime) > 0.25 && this.framesRendered > 10) {
            this.fps = (float) (this.framesRendered / (this.timePassed - this.fpsStartTime));
            this.fpsStartTime = this.timePassed;
            this.framesRendered = 0;
        }
        this.drawString(2, 2, "Frames: " + this.fps);

        this.framesRendered++;
    }

    private void drawString(int x, int y, String str) {
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        this.trueTypeFont.drawString(x, y, str);
        this.trueTypeFont.drawString(x + 1, y + 1, str);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    public Paddle[] getPaddles() {
        return this.paddles;
    }

    public static void main(String[] args) {
        DisplayMode displayMode;

        if (args.length == 2) {
            int width = Integer.parseInt(args[0]);
            int height = Integer.parseInt(args[1]);
            LOGGER.info("Using display mode " + width + "x" + height);
            displayMode = new DisplayMode(width, height);
        } else {
            displayMode = new DisplayMode(1024, 768);
            LOGGER.info("Using default display mode 1024x768");
        }

        GLPong game = new GLPong(displayMode);
        game.enterGameLoop();
    }
}
