
package org.angry.Model;

import com.badlogic.gdx.math.MathUtils;

public class ImpulseMath {

    public static final float PI = (float)StrictMath.PI;
    public static final float EPSILON = 0.0001f;
    public static final float EPSILON_SQ = EPSILON * EPSILON;
    public static final float BIAS_RELATIVE = 0.95f;
    public static final float BIAS_ABSOLUTE = 0.01f;
    public static  float DT = 1.0f / 20.0f;
    public static Vector GRAVITY = new Vector(0.0f, -50.0f);
    public static final float RESTING = GRAVITY.mul(DT).lengthSq() + EPSILON;
    public static final float PENETRATION_ALLOWANCE = 0.6f;
    public static final float PENETRATION_CORRETION = 0.9f;


    public static boolean equal(float a, float b) {
        return StrictMath.abs(a - b) <= EPSILON;
    }


    public static float random(float min, float max) {
        return MathUtils.random(min, max);
    }

    public static boolean gt(float a, float b) {
        return a >= (b * BIAS_RELATIVE + a * BIAS_ABSOLUTE);
    }

}
