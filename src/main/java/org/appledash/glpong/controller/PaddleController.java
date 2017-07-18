package org.appledash.glpong.controller;

import org.appledash.glpong.structures.Paddle;

/**
 * Created by appledash on 7/18/17.
 * Blackjack is best pony.
 */
public abstract class PaddleController {
    protected final Paddle paddle;

    public PaddleController(Paddle paddle) {
        this.paddle = paddle;
    }
}
