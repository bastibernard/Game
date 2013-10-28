package com.angrynerds.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;

/**
 * User: Franjo
 * Date: 26.10.13
 * Time: 10:47
 * Project: Main
 */
public class KeyboardInput extends InputAdapter implements IGameInputController {

    public KeyboardInput() {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public float get_stickX() {
        if (Gdx.input.isKeyPressed(Keys.LEFT) && Gdx.input.isKeyPressed(Keys.RIGHT)) return 0;
        else if (Gdx.input.isKeyPressed(Keys.LEFT)) return -1;
        else if (Gdx.input.isKeyPressed(Keys.RIGHT)) return 1;
        return 0;
    }

    @Override
    public float get_stickY() {
        if (Gdx.input.isKeyPressed(Keys.UP) && Gdx.input.isKeyPressed(Keys.DOWN)) return 0;
        else if (Gdx.input.isKeyPressed(Keys.UP)) return 1;
        else if (Gdx.input.isKeyPressed(Keys.DOWN)) return -1;
        return 0;
    }

    @Override
    public boolean get_isA() {
        return Gdx.input.isKeyPressed(Keys.Y);
    }

    @Override
    public boolean get_isB() {
        return Gdx.input.isKeyPressed(Keys.X);

    }
}
