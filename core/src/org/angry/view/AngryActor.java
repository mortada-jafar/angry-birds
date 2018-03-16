package org.angry.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import org.angry.controller.AngryBirds;
import org.angry.Model.ControllerLogic;
import org.angry.controller.AngryPhysics;

/**
 * Created by MyBaby on 12/5/2015.
 */
public class AngryActor extends Actor {
    private Texture slingBlack, targetTexture;
    private Sprite blackSprite, trajectorySprite;
    private ShapeRenderer shapeRenderer;
    private AngryBirds game;
    private AngryPhysics physics;
    private Bird bird;
    private Sound streach;
    private boolean startMusic;

    public void setBird(Bird bird) {
        this.bird = bird;
    }

    public AngryActor(AngryBirds game, AngryPhysics physics, Bird bird) {
        this.game = game;
        this.physics = physics;
        this.bird = bird;
        this.shapeRenderer = new ShapeRenderer();
        this.slingBlack = new Texture(Gdx.files.internal("angrybirds/slingblack.png"));
        this.blackSprite = new Sprite(slingBlack);

        targetTexture = new Texture(Gdx.files.internal("test/white-circle.png"));
        trajectorySprite = new Sprite(targetTexture);
        {
            streach = Gdx.audio.newSound(Gdx.files.internal("sounds/Slingshot_Stretche.mp3"));
            streach.setLooping(100, true);
        }
        startMusic=false;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (ControllerLogic.charging) {
            if(startMusic){
                streach.play();
            }
            startMusic=false;
            blackSprite.setOrigin(0, bird.getHeight() / 2);
            blackSprite.setPosition((bird.b2body.position.x), (bird.b2body.position.y - bird.getHeight() / 2));
            blackSprite.setRotation(physics.getVelocityVector().angle());
            blackSprite.setSize(bird.getWidth() / 2, bird.getHeight());

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(0.2f, 0f, 0f, 1f);
            shapeRenderer.rectLine(147, 157, bird.b2body.position.x, bird.b2body.position.y, ControllerLogic.SLINGWIDTH);
            shapeRenderer.rectLine(171, 157, bird.b2body.position.x, bird.b2body.position.y, ControllerLogic.SLINGWIDTH);
            shapeRenderer.end();

            game.batch.setProjectionMatrix(PlayScreen.gameCam.combined);
            game.batch.begin();
            blackSprite.draw(game.batch);
            for (int i = 0; i < 600; i += 20) {
                Vector2 trajectoryPoint = physics.getTrajectoryPoint(new Vector2(bird.b2body.position.x, bird.b2body.position.y), physics.getVelocityVector(), i);
                game.batch.draw(targetTexture, trajectoryPoint.x, trajectoryPoint.y, targetTexture.getWidth(), targetTexture.getHeight());
            }
            game.batch.end();

        } else {
            startMusic=true;
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(0.2f, 0f, 0f, 1f);
            shapeRenderer.rectLine(147, 157, 171, 155, ControllerLogic.SLINGWIDTH);
            shapeRenderer.end();
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }
}
