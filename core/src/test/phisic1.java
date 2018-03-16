package test;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

/**
 * Created by MyBaby on 11/28/2015.
 */
public class phisic1 extends InputAdapter implements Screen {

    private World world;
    private Box2DDebugRenderer renderer;
    private OrthographicCamera camera;
    Body ball, ground, box;
    private Vector2 movment = new Vector2();
    private Texture birdTexture, slingshotTexture, groundTexture, backgroundTexture;
    private Sprite bird, slingshotSprite, groundSprite, backgroundSprite;
    private SpriteBatch spriteBatch;
    private static final int SPEED = 20;
    final float PPM = 32;

    @Override
    public void show() {
        spriteBatch = new SpriteBatch();
        //top background
        {
            backgroundTexture = new Texture(Gdx.files.internal("angrybirds/background.png"));
            backgroundSprite = new Sprite(backgroundTexture);
            backgroundSprite.setPosition(0, 70);
        }
        //bottom background
        {
            groundTexture = new Texture(Gdx.files.internal("angrybirds/ground.png"));
            groundSprite = new Sprite(groundTexture);
            groundSprite.setPosition(0, 0);
        }
        //signal-shot background
        {
            slingshotTexture = new Texture(Gdx.files.internal("angrybirds/slingshot.png"));
            slingshotSprite = new Sprite(slingshotTexture);
            slingshotSprite.setPosition(128 - slingshotSprite.getWidth() * 0.5f, 100);
        }
        //Angry-bird background
        {
            birdTexture = new Texture(Gdx.files.internal("angrybirds/angry.png"));
            bird = new Sprite(birdTexture);

        }
        world = new World(new Vector2(0, -9.8f), true);
        renderer = new Box2DDebugRenderer();

        camera = new OrthographicCamera();
        camera.setToOrtho(false,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        //bodyDiff
        BodyDef bodyDeff = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef();

        //Box

        bodyDeff.type = BodyDef.BodyType.DynamicBody;
        bodyDeff.position.set(-50/PPM, 20/PPM);

        PolygonShape boxshape = new PolygonShape();
        boxshape.setAsBox(20 / PPM, 20 / PPM);

        fixtureDef.shape = boxshape;
        fixtureDef.friction = 0.1f;
//        fixtureDef.restitution = .1f;
        fixtureDef.density = 6f;

        box = world.createBody(bodyDeff);
        box.createFixture(fixtureDef);

        //ball
        bodyDeff.type = BodyDef.BodyType.DynamicBody;
        bodyDeff.position.set(10 / PPM, 20 / PPM);

        CircleShape shape = new CircleShape();
        shape.setPosition(new Vector2(0, 20/PPM));
        shape.setRadius(0.2f);

        fixtureDef.shape = shape;
        fixtureDef.density = 2.5f;    //mass
        fixtureDef.friction = 0.2f; //ahtkak
        fixtureDef.restitution = 1;  //jump 0 to 1

        ball = world.createBody(bodyDeff);
        ball.createFixture(fixtureDef);


        //Ground
        //ground Diff
        bodyDeff.type = BodyDef.BodyType.StaticBody;
        bodyDeff.position.set(0, 0);

        //ground Shape
        ChainShape groundShap = new ChainShape();
        //groundShap.createChain(new Vector2[]{new Vector2(-Gdx.graphics.getWidth() / 2 / PPM, -Gdx.graphics.getHeight() / 2 / PPM), new Vector2(Gdx.graphics.getWidth() / 2 / PPM, -Gdx.graphics.getHeight() / 2 / PPM)});
        groundShap.createChain(new Vector2[]{new Vector2(-100/PPM, 0/PPM), new Vector2(100/ PPM, 0/ PPM)});

        fixtureDef.shape = groundShap;
        fixtureDef.density = 10;
        fixtureDef.friction = 0.2f;
        fixtureDef.restitution = 0;

        ground = world.createBody(bodyDeff);
        ground.createFixture(fixtureDef);

        groundShap.dispose();
        shape.dispose();


        Gdx.input.setInputProcessor(this);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spriteBatch.begin();
        groundSprite.draw(spriteBatch);
        spriteBatch.end();

        renderer.render(world, camera.combined);
        world.step(1 / 60f, 8, 3);
        //System.out.println(box.getPosition());
        box.applyForceToCenter(movment, true);

        //camera.position.set(box.getPosition().x, box.getPosition().y, 0);
        camera.update();

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        world.dispose();
        renderer.dispose();
    }

    @Override
    public boolean scrolled(int amount) {
        camera.zoom += amount / 25f;
        camera.update();
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.RIGHT:
            case Input.Keys.LEFT:
                movment.x = 0;
                break;
            case Input.Keys.UP:
            case Input.Keys.DOWN:
                movment.y = 0;
                break;
        }
        return true;
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.SPACE:
                ((Game) Gdx.app.getApplicationListener()).setScreen(new phisic1());
                break;
            case Input.Keys.RIGHT:
                movment.x = SPEED;
                break;
            case Input.Keys.LEFT:
                movment.x = -SPEED;
                break;
            case Input.Keys.UP:
                movment.y = SPEED;
                break;
            case Input.Keys.DOWN:
                movment.y = -SPEED;
                break;
        }
        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
//        camera.unproject(tmp.set(screenX, screenY, 0));
//        world.QueryAABB(querycallback, tmp.x, tmp.y, tmp.x, tmp.y);
        float x = screenX - ball.getPosition().x;
        float y = Gdx.graphics.getHeight() - screenY - ball.getPosition().y;
        ball.setTransform(x / PPM - Gdx.graphics.getWidth() / PPM / 2, y / PPM - Gdx.graphics.getHeight() / PPM / 2, 0);
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
//        if(joint==null)
//            return false;
//        camera.unproject(tmp.set(screenX, screenY, 0));
//        joint.setTarget(tmp2.set(tmp.x,tmp.y));
//        ball.getWorld().destroyJoint(joint);
        //  ball.setType(BodyDef.BodyType.DynamicBody);
        ball.setLinearVelocity(new Vector2(-d*MathUtils.cos(a)/4,-d*MathUtils.sin(a)/4));
        return true;
    }

    Vector2 s = new Vector2();
    float d;
    float a;

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
//        if(joint==null)
//            return false;
//        world.destroyJoint(joint);
//        joint=null;
        s.x=screenX-ball.getPosition().x/PPM;
        s.y=screenX-ball.getPosition().y/PPM;
        d=s.x*s.x+s.y*s.y;
        a= MathUtils.atan2(s.y,s.x);
//
        float x = (screenX - ball.getPosition().x )/ PPM - Gdx.graphics.getWidth() / PPM / 2;
        float y = (Gdx.graphics.getHeight() - screenY - ball.getPosition().y)/ PPM - Gdx.graphics.getHeight() / PPM / 2;
        ball.setTransform(x , y , 0);
        System.out.println(ball.getPosition());
        System.out.println(screenX + "   " + screenY);


        return true;
    }

}
