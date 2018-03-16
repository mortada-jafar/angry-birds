package test;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.physics.box2d.Box2D;

/**
 * Created by MyBaby on 12/2/2015.
 */

public class NuttyGame extends Game {
    private final AssetManager assetManager = new AssetManager();

    @Override
    public void create() {
        Box2D.init();
        assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        setScreen(new GameScreen());

    }
    public AssetManager getAssetManager() {
        return assetManager;
    }

}