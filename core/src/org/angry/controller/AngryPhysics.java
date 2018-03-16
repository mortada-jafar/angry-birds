package org.angry.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import org.angry.Model.ControllerLogic;
import org.angry.Model.ImpulseMath;
import org.angry.Model.InsertDb;
import org.angry.view.Bird;

/**
 * Created by MyBaby on 12/5/2015.
 */
public class AngryPhysics {

    private float distance, angle;
    private Vector2 velocityVector;
    private Bird bird;

    public void setBird(Bird bird) {
        this.bird = bird;
    }

    public AngryPhysics(Bird bird) {
        this.bird = bird;
    }

    public Vector2 getTrajectoryPoint(Vector2 start, Vector2 velocity, float n) {
        float t = 1 / 60f;
        float tt = t * t;
        float stepVelocityX = t * -velocity.x;
        float stepVelocityY = t * -velocity.y;
        float stepGravityX = tt * ImpulseMath.GRAVITY.x;
        float stepGravityY = tt * (-9.8f);
        float tpx = start.x + n * stepVelocityX + 0.5f * (n * n + n) * stepGravityX;
        float tpy = start.y + n * stepVelocityY + 0.5f * (n * n + n) * stepGravityY;
        return new Vector2(tpx, tpy);
    }

    public void pressed(int screenX, int screenY) {
        Circle cc = new Circle(bird.b2body.position.x, bird.b2body.position.y, 24);
        if (cc.contains(screenX, Gdx.graphics.getHeight() - screenY) &&
                bird.getState() == Bird.State.STADNG) {
            float x = screenX;
            float y = Gdx.graphics.getHeight() - screenY;
            bird.b2body.position.set(x <= 25 ? 25 : x, y < 85 ? 85 : y);
        }
    }

    public void dragged(int screenX, int screenY) {
        Circle cc = new Circle(bird.b2body.position.x, bird.b2body.position.y, 16);
        if (cc.contains(screenX, Gdx.graphics.getHeight() - screenY) && bird.getState() == Bird.State.STADNG) {
            ControllerLogic.charging = true;
            Vector2 b = new Vector2();
            Vector2 c = new Vector2();
            velocityVector = new Vector2();

            b.set(new Vector2(bird.b2body.position.x, bird.b2body.position.y));
            c.set(162, 157);

            velocityVector.set(b).sub(c);

            distance = velocityVector.len();

            angle = MathUtils.atan2(velocityVector.y, velocityVector.x);
            angle %= 2 * MathUtils.PI;

            float x = screenX;
            float y = Gdx.graphics.getHeight() - screenY;

            bird.b2body.position.set(x <= 25 ? 25 : x, y < 85 ? 85 : y);

            if (distance > 100) {
                ControllerLogic.SLINGWIDTH = 7;
            } else if (distance > 60) {
                ControllerLogic.SLINGWIDTH = 8;
            } else {
                ControllerLogic.SLINGWIDTH = 13;
            }
        }
    }

    public void mouseUp() {
        if (bird.getState() == Bird.State.STADNG && ControllerLogic.charging) {
            float velX = (2.25f * -MathUtils.cos(angle) * (distance));
            float velY = (2.25f * -MathUtils.sin(angle) * (distance));
            if(ControllerLogic.RECORDING){
                ControllerLogic.vel.set(velX,velY);
                ControllerLogic.POS.set(bird.b2body.position.x,bird.b2body.position.y);
                ControllerLogic.RECORDING=true;
                new InsertDb();
            }
            bird.b2body.velocity.set(velX, velY);
            bird.b2body.shape.initialize();

            ControllerLogic.charging = false;
        }

    }

    public Vector2 getVelocityVector() {
        return velocityVector;
    }

}
