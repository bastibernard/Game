package com.angrynerds.gameobjects.creatures;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.Animation;
import com.esotericsoftware.spine.Event;

/**
 * User: Franjo
 * Date: 07.11.13
 * Time: 22:23
 * Project: GameDemo
 */
public class Boy extends Creature {

    Array<Event> events;
    Animation walkAnimation;

    public Boy(String name, TextureAtlas atlas, String skin, float scale) {
        super(name, atlas, skin, scale);

        walkAnimation = skeletonData.findAnimation("walk");
        showBounds = false;

        x = 160;
        y = 30;
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        walkAnimation.apply(skeleton, skeleton.getTime(), skeleton.getTime(), true, events);
    }


}