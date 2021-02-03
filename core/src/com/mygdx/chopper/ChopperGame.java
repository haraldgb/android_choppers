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
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import java.text.DecimalFormat;

public class ChopperGame extends ApplicationAdapter {
	public final static int DESKTOP_START_WIDTH = 480;
	public final static int DESKTOP_START_HEIGHT = 800;
	public static final String TITLE = "GET TO THA' CHOPPA";
	public static final int MAXSPEED = 4;

	OrthographicCamera camera;
	ExtendViewport viewport;

	TextureAtlas textureAtlas;
	Sprite chopper;
	Vector2 chopperVelocity = new Vector2(-5,5);
	SpriteBatch batch;
	char lastDirection;
	BitmapFont font;
	DecimalFormat decFormatter;

	@Override
	public void create () {
		camera = new OrthographicCamera();
		viewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
		batch = new SpriteBatch();
		textureAtlas = new TextureAtlas("sprites.txt");
		chopper = textureAtlas.createSprite("heli1");
		chopper.flip(true, false);
		if (Gdx.input.getX() >= 0) {
			lastDirection = 'r';
		} else {
			lastDirection = 'l';
		}
		font = new BitmapFont();
		decFormatter = new DecimalFormat("#.00");
		Gdx.gl.glClearColor(0.57f, 0.77f, 0.85f, 1);
	}

	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		update();
		font.draw(batch, "Horizontal position: " + decFormatter.format(chopper.getX())
						+ "\n    Vertical position: " + decFormatter.format(chopper.getY())
						+ "\n    " + Gdx.input.getX() + "     " + Gdx.input.getY(),
				viewport.getWorldWidth() - 180, viewport.getWorldHeight() - 10);
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

	private void updateSprite (Sprite sprite) {
		float mouseX = Gdx.input.getX();
		if (mouseX < sprite.getWidth() / 2) {
			mouseX = sprite.getWidth() / 2;
		} else if (mouseX > viewport.getWorldWidth() - sprite.getWidth() / 2) {
			mouseX = viewport.getWorldWidth() - sprite.getWidth() / 2;
		}
		// Gdx.input.getY() gives value from the top of the bottom, which differs from the other y
		// values.
		float mouseY = viewport.getWorldHeight() - Gdx.input.getY();
		if (mouseY < sprite.getHeight() / 2) {
			mouseY = sprite.getHeight() / 2;
		} else if (mouseY > viewport.getWorldHeight() - sprite.getHeight() / 2) {
			mouseY = viewport.getWorldHeight() - sprite.getHeight() / 2;
		}

		float xCenter = sprite.getX() + sprite.getWidth() / 2;
		float yCenter = sprite.getY() + sprite.getHeight() / 2;
		Vector2 mouseVector = new Vector2(mouseX - xCenter, mouseY - yCenter);
		float vectorLength = mouseVector.len();
		float scale = MAXSPEED / vectorLength;
		if (scale > 2) {
			sprite.setCenter(mouseX, mouseY);
			mouseVector.set(0, 0);
		} else {
			mouseVector.scl(scale);
			sprite.setPosition(sprite.getX() + mouseVector.x, sprite.getY() + mouseVector.y);
		}
		char direction;
		if (mouseVector.x < 0) {
			direction = 'l';
		} else if (mouseVector.x > 0) {
			direction = 'r';
		} else {
			direction = lastDirection;
		}
		if (direction != lastDirection) {
			sprite.flip(true, false);
		}
		lastDirection = direction;
	}

	private void update() {
		updateSprite(chopper);
		chopper.draw(batch);
	}


}
