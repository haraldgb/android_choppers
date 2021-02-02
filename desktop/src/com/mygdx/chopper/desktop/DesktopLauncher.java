package com.mygdx.chopper.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.chopper.ChopperGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = ChopperGame.DESKTOP_START_WIDTH;
		config.height = ChopperGame.DESKTOP_START_HEIGHT;
		config.title = ChopperGame.TITLE;
		new LwjglApplication(new ChopperGame(), config);
	}
}
