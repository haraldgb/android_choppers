package com.mygdx.chopper;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Chopper {
    private SpriteBatch batch;
    private Vector2 velocity;
    private Vector2 position;
    private Rectangle boundingRectangle;
    private Animation<TextureRegion> chopperAnimation;
    private int frequency = 60;


    public Chopper(SpriteBatch batch, TextureAtlas atlas,
        float px, float py, float vx, float vy) {
        // Should probably see if there are other choppers that are spawning at the same position.
        this.batch = batch;
        this.position = new Vector2(px, py);
        this.velocity = new Vector2(vx*frequency, vy*frequency);
        Array<TextureAtlas.AtlasRegion> regions = atlas.findRegions("heli");
        this.chopperAnimation = new Animation<TextureRegion>(0.1f, regions, Animation.PlayMode.LOOP);
        if (vx > 0) {
            flipTextures();
        }
        this.boundingRectangle = new Rectangle(position.x, position.y, regions.get(0).packedWidth, regions.get(0).packedHeight);
    }

    public void update(float dt) {
        stepOnce(dt);
    }

    public void stepOnce(float dt) {
        velocity.scl(dt);
        position.add(velocity);
        boundingRectangle.setPosition(position);
        velocity.scl(1/dt);
    }

    public void draw(float st) {
        TextureRegion currentFrame = chopperAnimation.getKeyFrame(st, true);
        batch.draw(currentFrame, position.x, position.y);
    }

    public void swapDirection(boolean x, boolean y) {
        if (x) {
            setVelocity(-velocity.x, velocity.y);
            flipTextures();
        }
        if (y) {
            setVelocity(velocity.x, -velocity.y);
        }
    }

    private void flipTextures() {
        for (TextureRegion frame: chopperAnimation.getKeyFrames()) {
            frame.flip(true, false);
        }
    }

    public Rectangle getBoundingRectangle() {
        return boundingRectangle;
    }

    public Vector2 getPosition() {
        return this.position;
    }

    public void setPosition(float px, float py) {
        this.position.set(px, py);
    }

    public void setPosition(Vector2 positionVector) {
        this.position.set(positionVector);
    }

    public Vector2 getVelocity() {
        return this.velocity;
    }

    public void setVelocity(float vx, float vy) {
        this.velocity.set(vx, vy);
    }

    public void setVelocity(Vector2 velocityVector) {
        this.velocity.set(velocityVector);
    }

    public float getWidth() {
        return this.boundingRectangle.width;
    }

    public float getHeight() {
        return this.boundingRectangle.height;
    }

}
