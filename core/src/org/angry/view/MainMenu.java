package org.angry.view;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.angry.controller.AngryBirds;
import org.angry.Model.ControllerLogic;

/**
 * Created by MyBaby on 11/27/2015.
 */
public class MainMenu implements Screen {
    private Stage stage;
    private Skin skin;
    private TextButton  showLastButton, playButton, exiteButton;
    private Table table;
    private BitmapFont white, black;
    private TextureAtlas atlas;
    private SpriteBatch batch;
    private Texture backMenu;
    private Sprite spriteBg;
    private Viewport viewport;
    private AngryBirds game;

    public MainMenu(AngryBirds game) {
        this.game = game;
    }

    @Override
    public void show() {
        viewport = new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage(viewport);
        batch = new SpriteBatch();
        backMenu = new Texture("angrybirds/backmenu.jpg");
        spriteBg = new Sprite(backMenu);
        spriteBg.setPosition(0, 0);
        spriteBg.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.input.setInputProcessor(stage);
        atlas = new TextureAtlas("ui/button.pack");
        white = new BitmapFont(Gdx.files.internal("font/w.fnt"), false);
        black = new BitmapFont(Gdx.files.internal("font/b.fnt"), false);
        skin = new Skin(atlas);

        table = new Table(skin);

        table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        TextButton.TextButtonStyle ts = new TextButton.TextButtonStyle();
        ts.up = skin.getDrawable("button-down");
        ts.down = skin.getDrawable("button-up");
        ts.pressedOffsetX = 1;
        ts.pressedOffsetY = -1;

        ts.font = white;

        exiteButton = new TextButton("EXIT", ts);
        exiteButton.pad(10, 70, 10, 70);
        exiteButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        playButton = new TextButton("New Game", ts);
        playButton.pad(10, 70, 10, 70);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new PlayScreen(game));
            }
        });
        showLastButton = new TextButton("Show Last Game", ts);
        showLastButton.pad(10, 70, 10, 70);
        showLastButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ControllerLogic.ISRERUN=true;
                ((Game) Gdx.app.getApplicationListener()).setScreen(new PlayScreen(game));

            }
        });


        table.add(playButton).space(25);
        table.row();
        table.add(showLastButton).space(25);;
        table.row();
        table.add(exiteButton);

//        table.debug();
        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        spriteBg.draw(batch);
        batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.setScreenSize(width, height);
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
        stage.dispose();
        atlas.dispose();
        white.dispose();
        black.dispose();
        skin.dispose();
    }
}
