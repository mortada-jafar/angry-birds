package org.angry.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Timer;
import org.angry.Model.Body;
import org.angry.Model.World;
import org.angry.Model.ControllerLogic;

/**
 * Created by MyBaby on 12/3/2015.
 */
public class Bird extends Sprite {
    public enum State {FLAYNIG, STADNG, FALLING, ONEARTH, STOPED}

    public State currentState;
    public State previosState;


    public World world;
    public Body b2body;
    public float g;
    private TextureRegion textureRegion;
    private Sound birdMusic;
    private boolean startMusic;

    public Bird(World world, float g) {
        super(PlayScreen.atlas.findRegion("birds"));
        currentState = State.STADNG;
        previosState = State.STADNG;
        this.world = world;
        this.g = g;
        DefinBird();
        textureRegion = new TextureRegion(getTexture(), 264, 167, 47, 47);
        setBounds(0, 0, 50, 50);
        setRegion(textureRegion);
        this.b2body.invMass = 0;
        {
            birdMusic = Gdx.audio.newSound(Gdx.files.internal("sounds/flaying.mp3"));
            birdMusic.setLooping(15, false);
        }
        startMusic=true;

    }

    public void update(float dt) {
        setPosition(b2body.position.x - getWidth() / 2, b2body.position.y - getHeight() / 2);
        if (!isStoped() && currentState != State.STADNG&&startMusic) {
            birdMusic.play();
            startMusic=false;
        }
        if (isStoped()) {
            ControllerLogic.isBird = false;
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    start();
                }

                private void start() {
                    startMusic=true;
                }
            }, 3);
        }
        b2body.angularVelocity = 0;
        setOrigin(this.getWidth() / 2, this.getHeight() / 2);
        setRotation(b2body.orient * 2);
        setRegion(getTR());

    }

    private boolean isStoped() {
        return b2body.velocity.x < 1 && b2body.velocity.x > -1 && this.getState() != State.STADNG;
    }


    public TextureRegion getTR() {
        currentState = getState();
        TextureRegion region;
        switch (currentState) {
            case FLAYNIG:
            case FALLING:
                region = new TextureRegion(getTexture(), 314, 216, 47, 43);
                break;
            case ONEARTH:
            case STOPED:
                region = new TextureRegion(getTexture(), 264, 260, 47, 45);
                break;
            case STADNG:
            default:
                region = new TextureRegion(getTexture(), 264, 167, 47, 47);
                break;

        }
        return region;
    }


    public State getState() {
        if (isZero(b2body.velocity.x, b2body.velocity.y) && previosState == State.STADNG) {
            return currentState = State.STADNG;
        } else if (isZero(b2body.velocity.x, b2body.velocity.y) && previosState != State.STADNG) {
            return currentState = State.STOPED;
        } else if (b2body.velocity.x > 0 && b2body.velocity.y > 0 && previosState != State.FALLING && previosState != State.ONEARTH) {
            return currentState = State.FLAYNIG;
        } else if (b2body.velocity.x > 0 && b2body.velocity.y < 0 && previosState != State.ONEARTH) {
            return currentState = State.FALLING;
        } else {
            return previosState = currentState = State.ONEARTH;
        }
    }

    public void DefinBird() {
        Circle c = new Circle(23, 1f);
        this.b2body = world.add(c, 150, 150);
        b2body.setOrient(0);
        b2body.restitution = 1f;
        b2body.dynamicFriction = 0.1f;
        b2body.staticFriction = 0.1f;
    }

    public boolean isZero(float x, float y) {
        return x == 0 && y == 0;
    }
}
