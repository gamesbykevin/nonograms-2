package com.gamesbykevin.nonogram.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.gamesbykevin.nonogram.MyGdxGame;
import com.gamesbykevin.nonogram.util.FileHelper;

import de.golfgl.gdxgamesvcs.GpgsClient;
import de.golfgl.gdxgamesvcs.NoGameServiceClient;

import static com.gamesbykevin.nonogram.MyGdxGame.HEIGHT;
import static com.gamesbykevin.nonogram.MyGdxGame.WIDTH;

public class DesktopLauncher {

	public static void main (String[] arg) {

		//create the game with the specified credential
		MyGdxGame myGdxGame = new MyGdxGame() {
			@Override
			public void create() {
				//setClient(new GpgsClient().initialize("Nonograms-Desktop", FileHelper.load("json/client_secret.json"), false));
				setClient(new NoGameServiceClient());
				super.create();
			}
		};

		//set the dimensions of the screen
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = WIDTH;
		config.height = HEIGHT;

		//now create our application
		new LwjglApplication(myGdxGame, config);
	}
}
