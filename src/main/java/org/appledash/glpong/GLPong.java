package org.appledash.glpong;

import me.jordin.deltoid.vector.Vec3;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.appledash.glpong.controller.BallController;
import org.appledash.glpong.controller.BallControllerLocal;
import org.appledash.glpong.controller.PaddleController;
import org.appledash.glpong.controller.PaddleControllerLocal;
import org.appledash.glpong.gui.Gui;
import org.appledash.glpong.gui.GuiPause;
import org.appledash.glpong.structures.Ball;
import org.appledash.glpong.structures.Paddle;
import org.appledash.glpong.utils.FPSCounter;
import org.appledash.glpong.utils.RenderUtils;
import org.appledash.glpong.utils.Timer;
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
    private BallController ballController;

    private Paddle[] paddles = new Paddle[2];
    private PaddleController[] paddleControllers = new PaddleController[2];

    private TrueTypeFont[] fontPool = new TrueTypeFont[128];
    private TrueTypeFont trueTypeFont;
    private Timer timer;
    private FPSCounter fpsCounter;

    private Gui gui;

    public GLPong(DisplayMode displayMode) {
        try {
            this.setupDisplay(displayMode);
        } catch (LWJGLException e) {
            throw new RuntimeException("Failed to setup LWJGL Display!");
        }

        this.ball = new Ball(new Vec3(Display.getWidth() / 2, Display.getHeight() / 2));
        this.paddles[LEFT] = new Paddle(new Vec3(50, Display.getHeight() / 2), vel -> vel.x < 0);
        this.paddles[RIGHT] = new Paddle(new Vec3(Display.getWidth() - 50, Display.getHeight() / 2), vel -> vel.x > 0);

        this.paddleControllers[LEFT] = new PaddleControllerLocal(this, this.paddles[LEFT], Keyboard.KEY_W, Keyboard.KEY_S);
        this.paddleControllers[RIGHT] = new PaddleControllerLocal(this, this.paddles[RIGHT], Keyboard.KEY_UP, Keyboard.KEY_DOWN);


        trueTypeFont = new TrueTypeFont(new Font("Verdana", Font.PLAIN, 16), true);

        this.timer = new Timer();
        this.fpsCounter = new FPSCounter(this.timer);
        this.ballController = new BallControllerLocal(this, this.ball);
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
            prevTime = System.currentTimeMillis();
            this.timer.update();
            this.fpsCounter.update();

            this.draw();

            Display.update();

            while (Keyboard.next()) {
                if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE && Keyboard.getEventKeyState()) {
                    this.gui = this.gui == null ? new GuiPause(this) : null;
                    break;
                }
            }

            if (this.gui == null) {
                this.ballController.controlBall(deltaTime);
                this.paddleControllers[LEFT].controlPaddle(deltaTime);
                this.paddleControllers[RIGHT].controlPaddle(deltaTime);
            }
        }
    }

    private void draw() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

        if (this.gui != null) {
            this.gui.drawGui();
        } else {
            for (Paddle p : this.paddles) {
                p.draw();
            }

            ball.draw();
        }

        RenderUtils.drawString("FPS: " + this.fpsCounter.getFps(),5, 5);

        this.fpsCounter.incrementFramesRendered();
    }

    public Paddle[] getPaddles() {
        return this.paddles;
    }

    public Ball[] getBalls() {
        return new Ball[] { this.ball };
    }

    public Ball getPrimaryBall() {
        return ball;
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
