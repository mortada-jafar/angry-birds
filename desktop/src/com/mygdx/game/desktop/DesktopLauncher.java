package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import org.angry.controller.AngryBirds;

public class DesktopLauncher {
		// test update
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Angry brids / Mortadda Jafar";
		config.width = 1280;
		config.height = 560;
		new LwjglApplication(new AngryBirds(), config);
	}
}
