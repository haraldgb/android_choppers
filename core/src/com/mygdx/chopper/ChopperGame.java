package com.mygdx.chopper;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class ChopperGame extends ApplicationAdapter {
	public final static int DESKTOP_START_WIDTH = 480;
	public final static int DESKTOP_START_HEIGHT = 800;
	public static final String TITLE = "GET TO THA' CHOPPA";

	OrthographicCamera camera;
	ExtendViewport viewport;

	TextureAtlas textureAtlas;
	Sprite chopper;
	Vector2 chopper_velocity = new Vector2(-5,7);
	SpriteBatch batch;

	@Override
	public void create () {
		camera = new OrthographicCamera();
		viewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
		batch = new SpriteBatch();
		textureAtlas = new TextureAtlas("sprites.txt");
		chopper = textureAtlas.createSprite("heli1");
		Gdx.gl.glClearColor(0.57f, 0.77f, 0.85f, 1);
	}

	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		update();
		batch.end();
	}

	@Override
	public void dispose () {
		batch.dispose();
		textureAtlas.dispose();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height, true);
		batch.setProjectionMatrix(camera.combined);
	}

	private void updateSprite (Sprite sprite, Vector2 sprite_velocity) {
		sprite.setPosition(sprite.getX() + sprite_velocity.x, sprite.getY() + sprite_velocity.y);
		if (sprite.getX() < 0) {
			sprite.setX(0);
			sprite_velocity.set(-sprite_velocity.x, sprite_velocity.y);
			sprite.flip(true, false);
		}
		if (sprite.getY() < 0) {
			sprite.setY(0);
			sprite_velocity.set(sprite_velocity.x, -sprite_velocity.y);
		}
		if (sprite.getX() + sprite.getWidth() > viewport.getWorldWidth()) {
			sprite.setX(viewport.getWorldWidth() - sprite.getWidth());
			sprite_velocity.set(-sprite_velocity.x, sprite_velocity.y);
			sprite.flip(true, false);
		}
		if (sprite.getY() + sprite.getHeight() > viewport.getWorldHeight()) {
			sprite.setY(viewport.getWorldHeight() - sprite.getHeight());
			sprite_velocity.set(sprite_velocity.x, -sprite_velocity.y);
		}
	}

	private void update() {
		updateSprite(chopper, chopper_velocity);
		chopper.draw(batch);
	}


}
