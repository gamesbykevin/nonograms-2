package com.gamesbykevin.nonogram;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.widget.Toast;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.gamesbykevin.nonogram.services.Services;
import de.golfgl.gdxgamesvcs.GpgsClient;
import de.golfgl.gdxgamesvcs.NoGameServiceClient;

import static com.gamesbykevin.nonogram.MyGdxGame.isGooglePlay;

public class AndroidLauncher extends AndroidApplication implements Services {

	private GpgsClient client;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//set whether this is for google play
		MyGdxGame.GOOGLE_PLAY = BuildConfig.GOOGLE_PLAY;

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		if (isGooglePlay()) {

			//create our game services client
			this.client = new GpgsClient().initialize(this, false);

			//start app with services and google game client
			initialize(new MyGdxGame(getClient(),this), config);

		} else {

			//start app with services and fake game client
			initialize(new MyGdxGame(new NoGameServiceClient(),this), config);
		}
	}

	public GpgsClient getClient() {
		return this.client;
	}

	@Override
	public void share(String shareSubject, String shareBody, String shareTitle) {

		Intent sharingIntent = new Intent(Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		sharingIntent.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name);

		final String packageName = getPackageName();
		sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody + packageName);
		startActivity(Intent.createChooser(sharingIntent, shareTitle));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (getClient() != null)
			getClient().onGpgsActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void rate(String ratingActivity, String ratingUrl, String ratingFailure) {
		final String packageName = getPackageName();
		try {
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(ratingActivity + packageName)));
		} catch (Exception ex1) {
			try {
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(ratingUrl + packageName)));
			} catch (Exception ex2) {
				Toast.makeText(this, ratingFailure, Toast.LENGTH_SHORT).show();
			}
		}
	}
}