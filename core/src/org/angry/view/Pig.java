package org.angry.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Timer;
import org.angry.Model.Body;
import org.angry.Model.ControllerLogic;
import org.angry.Model.Handel;
import org.angry.Model.World;
import org.angry.Model.Vector;
import org.angry.controller.AngryBirds;

/**
 * Created by MyBaby on 12/6/2015.
 */
public class Pig extends Sprite implements Disposable, InputProcessor {


    public enum State {GOOD, BAD, VERYBAD, DAID}

    private World world;
    public Body pig2body;
    public Ellipse ellipse;
    private State currentState, prevState;
    private Animation pigBad, pigGood, pigVeryBad, pigDaid;
    private float stateTime = 0;
    public float health = 100;
    private boolean dragged = false;
    Sound streach;

    public Pig(World world, Ellipse ellipse) {
        super(PlayScreen.atlas.findRegion("Block"));
        currentState = State.GOOD;
        prevState = State.GOOD;
        this.world = world;
        this.ellipse = ellipse;

        DefinBox();

        Array<TextureRegion> frames = new Array<TextureRegion>();
        frames.add(new TextureRegion(getTexture(), 157, 478, 56, 52));
        frames.add(new TextureRegion(getTexture(), 157, 478, 56, 52));
        frames.add(new TextureRegion(getTexture(), 157, 478, 56, 52));
        frames.add(new TextureRegion(getTexture(), 96, 591, 56, 52));
        pigGood = new Animation(0.4f, frames);

        frames.clear();
        frames.add(new TextureRegion(getTexture(), 213, 533, 56, 52));
        frames.add(new TextureRegion(getTexture(), 213, 533, 56, 52));
        frames.add(new TextureRegion(getTexture(), 213, 533, 56, 52));
        frames.add(new TextureRegion(getTexture(), 328, 478, 56, 52));
        pigBad = new Animation(0.5f, frames);

        frames.clear();
        frames.add(new TextureRegion(getTexture(), 213, 479, 56, 52));
        frames.add(new TextureRegion(getTexture(), 213, 479, 56, 52));
        frames.add(new TextureRegion(getTexture(), 213, 479, 56, 52));
        frames.add(new TextureRegion(getTexture(), 270, 479, 56, 52));
        pigVeryBad = new Animation(0.5f, frames);

        frames.clear();


        frames.add(new TextureRegion(getTexture(), 286, 0, 76, 78));
        frames.add(new TextureRegion(getTexture(), 346, 0, 76, 78));
        frames.add(new TextureRegion(getTexture(), 420, 0, 76, 78));
        pigDaid = new Animation(0.2f, frames);

        setBounds(0, 0, ellipse.width, ellipse.width);
        setOrigin(this.getWidth() / 2, this.getHeight() / 2);
        {
            streach = Gdx.audio.newSound(Gdx.files.internal("sounds/pig.mp3"));
            streach.setLooping(100, false);
        }
    }

    public void update(float dt) {
        setRegion(getTR(dt));
//        System.out.println(pig2body.velocity.x+"    "+pig2body.velocity.y);
//        System.out.println(pig2body.angularVelocity);
        if (dragged) {
            pig2body.position.x = Gdx.input.getX();
            pig2body.position.y = Gdx.graphics.getHeight() - Gdx.input.getY();
            pig2body.velocity.set(0, 0);
        }
        if (currentState != State.DAID) {
            setPosition(pig2body.position.x - this.getWidth() / 2, pig2body.position.y - this.getHeight() / 2);
            this.setRotation(pig2body.shape.body.orient * MathUtils.radDeg);
            ellipse.setPosition(pig2body.position.x, pig2body.position.y);

            if (pig2body.angularVelocity > 0) {
                pig2body.angularVelocity = pig2body.angularVelocity - Gdx.graphics.getDeltaTime() * 0.4f;

                if (pig2body.angularVelocity - Gdx.graphics.getDeltaTime() > 0) {
                    pig2body.angularVelocity = 0;
                }


            } else if (pig2body.angularVelocity < 0) {
                pig2body.angularVelocity = pig2body.angularVelocity + Gdx.graphics.getDeltaTime() * 0.4f;
                if (pig2body.angularVelocity + Gdx.graphics.getDeltaTime() > 0) {
                    pig2body.angularVelocity = 0;
                }
            }


        } else {
            world.bodies.remove(pig2body);
            pig2body = null;
            this.dispose();
            Pig b = this;
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    ControllerLogic.circleArray.removeValue(b, false);
                    streach.play();
                }
            }, 0.2f);
        }
        if (pig2body != null) {
            colle();
        }
        PlayScreen.inputMultiplexer.addProcessor(this);
    }

    private void DefinBox() {
        Circle c = new Circle(ellipse.width / 2, 1f);
        this.pig2body = world.add(c, ((int) ellipse.x), (int) ellipse.y);
        pig2body.setOrient(0);
        pig2body.restitution = 0.2f;
        pig2body.dynamicFriction = 0.2f;
        pig2body.staticFriction = 0.5f;
    }

    private void colle() {

        for (Handel m : world.contacts) {
            for (int i = 0; i < m.contactCount; i++) {
                Vector v = m.contacts[i];
                if (v.y > pig2body.position.y && (m.B == this.pig2body || m.A == this.pig2body)) {
                    this.health -= 10;
                }
            }
        }
    }

    public State getState() {
        prevState = currentState;
        if (health >= 75) {
            return currentState = State.GOOD;
        } else if (health >= 50) {
            return currentState = State.BAD;
        } else if (health > 8) {
            return currentState = State.VERYBAD;
        } else {
            return currentState = State.DAID;
        }
    }

    public TextureRegion getTR(float dt) {
        currentState = getState();
        TextureRegion region;
        switch (currentState) {
            case GOOD:
                region = pigGood.getKeyFrame(stateTime, true);
                break;
            case BAD:
                region = pigBad.getKeyFrame(stateTime, true);
                break;
            case VERYBAD:
                region = pigVeryBad.getKeyFrame(stateTime, true);
                break;
            default:
                region = pigDaid.getKeyFrame(stateTime, false);
                break;

        }
        stateTime = currentState == prevState ? stateTime + dt : 0;
        return region;
    }

    @Override
    public void dispose() {
    }

    @Override
    public boolean keyDown(int keycode) {
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
        if (pig2body == null) {
            return false;
        }
        com.badlogic.gdx.math.Circle cc = new com.badlogic.gdx.math.Circle(pig2body.position.x, pig2body.position.y, 25);
        if (cc.contains(screenX, Gdx.graphics.getHeight() - screenY) && !AngryBirds.isOneDarged) {
            dragged = true;
            AngryBirds.isOneDarged = true;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        dragged = false;
        AngryBirds.isOneDarged = false;
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (pig2body == null) {
            return false;
        }
        com.badlogic.gdx.math.Circle c = new com.badlogic.gdx.math.Circle(pig2body.position.x, pig2body.position.y, 25);
        if (c.contains(screenX, Gdx.graphics.getHeight() - screenY) && !AngryBirds.isOneDarged) {
            dragged = true;
            AngryBirds.isOneDarged = true;
        }
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
