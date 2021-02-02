package com.mygdx.chopper;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class ChopperGame extends ApplicationAdapter {
	public final static int DESKTOP_START_WIDTH = 480;
	public final static int DESKTOP_START_HEIGHT = 800;
	public static final String TITLE = "GET TO THA' CHOPPA";

	OrthographicCamera camera;
	ExtendViewport viewport;

	float stateTime;
	TextureAtlas animationTextureAtlas;
	SpriteBatch batch;
	Array<Chopper> choppers;

	@Override
	public void create () {
		camera = new OrthographicCamera();
		viewport = new ExtendViewport(DESKTOP_START_WIDTH, DESKTOP_START_HEIGHT, camera);
		batch = new SpriteBatch();
		animationTextureAtlas = new TextureAtlas("animationframes.txt");
		Array<TextureAtlas.AtlasRegion> regions = animationTextureAtlas.findRegions("heli");
		stateTime = 0f;
		Gdx.gl.glClearColor(0.57f, 0.77f, 0.85f, 1);
		choppers = new Array<>();
		choppers.add(new Chopper(batch, animationTextureAtlas, 400, 400, -2, 3));
		choppers.add(new Chopper(batch, animationTextureAtlas, 100, 0, 2, 3));
		choppers.add(new Chopper(batch, animationTextureAtlas, 200, 200, -2, 3));
		choppers.add(new Chopper(batch, animationTextureAtlas, 100, 400, 2, -3));
	}

	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		float deltaTime = Gdx.graphics.getDeltaTime();
		stateTime += deltaTime;
		batch.begin();
		update(deltaTime, stateTime);
		batch.end();
	}

	@Override
	public void dispose () {
		batch.dispose();
		animationTextureAtlas.dispose();
	}

	private void update(float dt, float st) {
		this.updateChoppers(dt);
		this.handleCollisions(dt);
		this.drawChoppers(st);
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height, true);
		batch.setProjectionMatrix(camera.combined);
	}

	private void updateChoppers(float dt) {
		for (Chopper chopper: choppers) {
			chopper.update(dt);
		}
	}

	private void handleCollisions(float dt) {
		for (int i = 0; i < choppers.size; i++) {
			Chopper chopper1 = choppers.get(i);

			for (int j = i+1; j < choppers.size; j++) {
				Chopper chopper2 = choppers.get(j);
				if (chopper1.getBoundingRectangle().overlaps(chopper2.getBoundingRectangle())) {
					chopper1.swapDirection(true, true);
					chopper1.stepOnce(dt);
					chopper2.swapDirection(true, true);
					chopper2.stepOnce(dt);
				}
			}
			Vector2 position1 = chopper1.getPosition();
			float width = chopper1.getWidth();
			float height = chopper1.getHeight();
			if (position1.x <= 0) {
				chopper1.setPosition(0, position1.y);
				chopper1.swapDirection(true, false);
			} else if (position1.x + width >= viewport.getWorldWidth()) {
				chopper1.setPosition(viewport.getWorldWidth() - width, position1.y);
				chopper1.swapDirection(true, false);
			}
			if (position1.y <= 0) {
				chopper1.setPosition(position1.x, 0);
				chopper1.swapDirection(false, true);
			} else if (position1.y + height > viewport.getWorldHeight()) {
				chopper1.setPosition(position1.x, viewport.getWorldHeight() - height);
				chopper1.swapDirection(false, true);
			}

		}
	}

	private void drawChoppers(float st) {
		for (Chopper chopper: choppers) {
			chopper.draw(st);
		}
	}

}
