package com.gamesbykevin.nonogram;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gamesbykevin.nonogram.assets.Assets;
import com.gamesbykevin.nonogram.automation.Automation;
import com.gamesbykevin.nonogram.screen.Splash;
import com.gamesbykevin.nonogram.services.Services;
import com.gamesbykevin.nonogram.util.FileHelper;

import de.golfgl.gdxgamesvcs.IGameServiceClient;

public class MyGdxGame extends Game {

	//size of the game
	public static int WIDTH = 480;
	public static int HEIGHT = 800;

	//skin for styling our game
	private Skin skin;

	//where our skin is located
	public static final String SKIN_PATH = "skin/skin.json";

	//speed of the game
	public static final float FPS = 30.0f;

	//duration of each frame
	public static final float DURATION = (1000f / FPS);

	//is this platform android?
	private static boolean ANDROID;

	//is this google play or amazon?
	public static boolean GOOGLE_PLAY = false;

	//reference to share content via mobile
	private Services services;

	//our game services client
	private IGameServiceClient client;

	//object containing our game assets
	private Assets assets;

	//use this to take screenshots and record video
	private Automation automation;

	public MyGdxGame() {
		this(null, null);
	}

	public MyGdxGame(IGameServiceClient client, Services services) {
		setClient(client);
		this.services = services;
		getAssets();
		//this.automation = new Automation();
	}

	public void setClient(IGameServiceClient client) {
		this.client = client;
	}

	public Automation getAutomation() {
		return this.automation;
	}

	public Assets getAssets() {

		if (this.assets == null) {
			this.assets = new Assets();
		}

		return this.assets;
	}

	@Override
	public void create () {

		//create game skin here
		getSkin();

		//determine if this is an Android platform
		switch (Gdx.app.getType()) {
			case Android:
				ANDROID = true;
				break;

			default:
				ANDROID = false;
				break;
		}

		//we start with our splash screen
		setScreen(new Splash(this));
	}

	public static final boolean isGooglePlay() {
		return GOOGLE_PLAY;
	}

	public static final boolean isAndroid() {
		return ANDROID;
	}

	public Services getServices() {
		return this.services;
	}

	public IGameServiceClient getClient() {
		return this.client;
	}

	public Skin getSkin() {

		if (this.skin == null)
			this.skin = new Skin(FileHelper.load(SKIN_PATH));

		return this.skin;
	}

	@Override
	public void render () {

		//call parent
		super.render();
	}
	
	@Override
	public void dispose () {

		//call parent
		super.dispose();

		if (this.skin != null) {
			this.skin.dispose();
			this.skin = null;
		}
	}
}