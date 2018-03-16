package org.angry.view;

import aurelienribon.tweenengine.TweenAccessor;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by MyBaby on 11/27/2015.
 */
public class SpriteAccessor implements TweenAccessor {
    public static  final int ALPHA=1;
    @Override
    public int getValues(Object o, int i, float[] floats) {
        switch (i){
            case ALPHA:
                floats[0]=((Sprite) o).getColor().a;
                return 1;
            default:
                assert false;
                return -1;
        }
    }

    @Override
    public void setValues(Object o, int i, float[] floats) {
        switch (i){
            case ALPHA:
                Sprite t=((Sprite) o);
                t.setColor(t.getColor().r,t.getColor().g,t.getColor().b,floats[0]);
                break;
            default:
                assert false;
        }
    }
}
