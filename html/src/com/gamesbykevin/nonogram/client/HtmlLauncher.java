package com.gamesbykevin.nonogram.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.gamesbykevin.nonogram.MyGdxGame;
import de.golfgl.gdxgamesvcs.NoGameServiceClient;

import static com.gamesbykevin.nonogram.MyGdxGame.HEIGHT;
import static com.gamesbykevin.nonogram.MyGdxGame.WIDTH;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {

                //use for mobile html browser
                //return new GwtApplicationConfiguration();

                //use for desktop html browser
                return new GwtApplicationConfiguration(WIDTH, HEIGHT);
        }

        @Override
        public ApplicationListener createApplicationListener () {
                return new MyGdxGame(new NoGameServiceClient(), null);
        }
}