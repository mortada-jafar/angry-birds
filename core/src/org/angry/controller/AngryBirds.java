package org.angry.controller;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.angry.view.MainMenu;
import org.angry.view.PlayScreen;
import org.angry.view.splash;


/**
 * Created by MyBaby on 12/3/2015.
 */
public class AngryBirds extends Game {

    public static boolean isOneDarged = false;
    public final static int V_WIDTH = 1280;
    public final static int V_HIEGHT = 560;
    public static MainMenu main = null;
    public static splash splash= null;
    public static PlayScreen play = null;

    public SpriteBatch batch;

    @Override
    public void create() {
        batch = new SpriteBatch();
        main = new MainMenu(this);
        splash = new splash(this);
        play = new PlayScreen(this);
        setScreen(splash);
    }

    @Override
    public void render() {
        super.render();
    }
}
