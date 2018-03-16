package org.angry.view;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.angry.controller.AngryBirds;
import org.angry.Model.GetDb;
import org.angry.Model.InsertDb;
import org.angry.controller.*;
import org.angry.Model.World;
import org.angry.controller.AngryPhysics;
import org.angry.Model.B2WorldCreator;
import org.angry.Model.ControllerLogic;
import org.angry.Model.ImpulseMath;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by MyBaby on 12/3/2015.
 */
public class PlayScreen extends InputAdapter implements Screen {


    public static OrthographicCamera gameCam;
    private Viewport gamePort;
    private AngryBirds game;
    private GameController gameController;
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer maprenderer;
    private World world;
    public Bird bird;
    private AngryPhysics physics;
    private Stage stage;
    private Texture slingShotPart;
    private ShapeRenderer shapeRenderer;
    private AngryActor angryActor;
    public static InputMultiplexer inputMultiplexer;
    private static final int NTHREDS = 25;
    public static TextureAtlas atlas;
    private Sound music;
    private Animation record;
    private Sprite rSprite;
    private float stateTime = 0;
    private ExecutorService executor;

    public PlayScreen(AngryBirds game) {
        Array<TextureRegion> frames = new Array<TextureRegion>();
        frames.add(new TextureRegion(new Texture(Gdx.files.internal("record/record.png"))));
        frames.add(new TextureRegion(new Texture(Gdx.files.internal("record/blink.png"))));
        frames.add(new TextureRegion(new Texture(Gdx.files.internal("record/record.png"))));
        frames.add(new TextureRegion(new Texture(Gdx.files.internal("record/blink.png"))));
        record = new Animation(0.3f, frames);
        rSprite = new Sprite();
        rSprite.setRegion(record.getKeyFrame(0, true));

        shapeRenderer = new ShapeRenderer();
        this.game = game;
        stage = new Stage();
        atlas = new TextureAtlas("ui/angr_birds.pack");
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(AngryBirds.V_WIDTH, AngryBirds.V_HIEGHT, gameCam);

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("map.tmx");
        maprenderer = new OrthogonalTiledMapRenderer(map, 1);
        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        //Phiscs
        world = new World(ImpulseMath.DT, 60);

        bird = new Bird(world, 0);

        gameController = new GameController(world, game.batch, bird);

        physics = new AngryPhysics(bird);
        angryActor = new AngryActor(this.game, this.physics, this.bird);
        stage.addActor(angryActor);

        executor = Executors.newFixedThreadPool(NTHREDS);
        slingShotPart = new Texture("angrybirds/slingpart.png");

    }

    public TextureAtlas getAtlas() {
        return this.atlas;
    }

    @Override
    public void show() {
        {

            inputMultiplexer = new InputMultiplexer();
            inputMultiplexer.addProcessor(gameController.stage);
            inputMultiplexer.addProcessor(new org.angry.controller.InputProcessor(physics));
            inputMultiplexer.addProcessor(this);
            new B2WorldCreator(world, map);   // just floor
            Gdx.input.setInputProcessor(inputMultiplexer);

            ControllerLogic.boxArray.clear();
            ControllerLogic.circleArray.clear();
            new GetDb(world);
            {
                Music s = Gdx.audio.newMusic(Gdx.files.internal("sounds/game.wav"));
                s.setLooping(true);
                s.setVolume(10);
                s.play();

            }
        }
        if(ControllerLogic.ISRERUN){
            bird.b2body.position.set(ControllerLogic.POS.x,ControllerLogic.POS.y);
            bird.b2body.velocity.set(ControllerLogic.vel.x,ControllerLogic.vel.y);
            bird.b2body.shape.initialize();
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 255, 255, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        maprenderer.render();
        maprenderer.setView(gameCam);
        update(delta);
        game.batch.setProjectionMatrix(gameCam.combined);

        game.batch.begin();

        bird.draw(game.batch);
        for (Box box : ControllerLogic.boxArray) {
            box.update(delta);
            box.draw(game.batch);
        }
        for (Pig pig : ControllerLogic.circleArray) {
            pig.update(delta);
            pig.draw(game.batch);
        }
        game.batch.end();


        game.batch.setProjectionMatrix(gameController.stage.getCamera().combined);
        gameController.setState(bird.getState().toString());
        gameController.stage.draw();

        game.batch.setProjectionMatrix(stage.getCamera().combined);
        stage.draw();
        game.batch.begin();
        game.batch.draw(slingShotPart, 130, 104);
        if (ControllerLogic.RECORDING) {
            game.batch.draw(record.getKeyFrame(stateTime, true), 0, 520);
        }
        game.batch.end();
        world.step();
        stateTime += delta;
    }

    public void update(float dt) {
        bird.update(dt);
        if (!ControllerLogic.isBird) {
            ControllerLogic.isBird = true;
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    newBird();
                }

                private void newBird() {

                    Timer.instance().clear();
                    world.bodies.remove(bird.b2body);
                    bird = new Bird(world, 0);
                    physics.setBird(bird);
                    angryActor.setBird(bird);
                    ControllerLogic.RECORDING = false;
                    if(ControllerLogic.ISRERUN){
                        ControllerLogic.ISRERUN=false;
                        ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu(game));
                    }
                }
            }, 3);
        }

        gameCam.update();

    }


    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
        gameCam.update();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        maprenderer.dispose();
        map.dispose();
        gameController.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.SPACE) {
            ControllerLogic.boxArray.clear();
            ControllerLogic.circleArray.clear();
            ImpulseMath.DT = 1.0f / 20.0f;
            ((Game) Gdx.app.getApplicationListener()).setScreen(new PlayScreen(game));
        }

        if (keycode == Input.Keys.S) {
            new InsertDb();
        }
        if (keycode == Input.Keys.ESCAPE) {
            ControllerLogic.boxArray.clear();
            ControllerLogic.circleArray.clear();
            ImpulseMath.DT = 1.0f / 20.0f;
            ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu(game));

        }

        if (keycode == Input.Keys.R) {
            ControllerLogic.RECORDING = true;
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

}
