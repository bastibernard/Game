package com.angrynerds.ui;

import com.angrynerds.gameobjects.Player;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

/**
 * Created with IntelliJ IDEA.
 * User: Tim
 * Date: 21.11.13
 * Time: 15:18
 * To change this template use File | Settings | File Templates.
 */
public class Lifebar extends Actor {

//    private Player player;
    private Sprite border;
    private Sprite life;
    private Sprite bubble;

    public Lifebar (String borderName, String lifeName, String bubbleName){

//        this.player = player;
        this.bubble = new Sprite(new Texture("data/bars/" + bubbleName));
        this.border = new Sprite(new Texture("data/bars/" + borderName));
        this.life = new Sprite(new Texture("data/bars/" + lifeName));


    }

    public void setLifePercent(float percent){
        life.setSize(life.getWidth()*percent, life.getHeight());
    }

    @Override
    public float getHeight() {
        return border.getHeight();
    }

    @Override
    public void draw(SpriteBatch batch, float parentAlpha) {

        batch.draw(life, getX()+bubble.getWidth()-19, getY(), life.getWidth(), life.getHeight());
        batch.draw(border, getX()+bubble.getWidth()-18, getY());
        batch.draw(bubble,getX(), getY());

        super.draw(batch, parentAlpha);    //To change body of overridden methods use File | Settings | File Templates.

    }
}