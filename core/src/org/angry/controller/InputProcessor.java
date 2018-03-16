package org.angry.controller;

import com.badlogic.gdx.InputAdapter;
import org.angry.view.PlayScreen;

/**
 * Created by MyBaby on 12/4/2015.
 */
public class InputProcessor extends InputAdapter {

    AngryPhysics ph;
    public InputProcessor(AngryPhysics ph) {
    this.ph=ph;
    }

    @Override
    public boolean scrolled(int amount) {
//        PlayScreen.gameCam.zoom += amount / 25f;
//        PlayScreen.gameCam.update();
        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        ph.pressed(screenX,screenY);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        ph.mouseUp();
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        ph.dragged(screenX, screenY);
        return false;
    }

}
