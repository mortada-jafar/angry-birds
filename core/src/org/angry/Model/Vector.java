
package org.angry.Model;


public class Vector
{

    public float x, y;

    public Vector()
    {
    }

    public Vector(float x, float y )
    {
        set( x, y );
    }

    public Vector(Vector v )
    {
        set( v );
    }

    public void set( float x, float y )
    {
        this.x = x;
        this.y = y;
    }

    public Vector set(Vector v )
    {
        x = v.x;
        y = v.y;
        return this;
    }
    public Vector negi()
    {
        return neg( this );
    }
    public Vector neg(Vector out )
    {
        out.x = -x;
        out.y = -y;
        return out;
    }
    public Vector neg()
    {
        return neg( new Vector() );
    }
    public Vector muli(float s )
    {
        return mul( s, this );
    }

    public Vector mul(float s, Vector out )
    {
        out.x = s * x;
        out.y = s * y;
        return out;
    }

    public Vector mul(float s )
    {
        return mul( s, new Vector() );
    }

    public Vector divi(float s )
    {
        return div( s, this );
    }

    public Vector div(float s, Vector out )
    {
        out.x = x / s;
        out.y = y / s;
        return out;
    }

    public Vector div(float s )
    {
        return div( s, new Vector() );
    }

    public Vector addi(float s )
    {
        return add( s, this );
    }

    public Vector add(float s, Vector out )
    {
        out.x = x + s;
        out.y = y + s;
        return out;
    }

    public Vector add(float s )
    {
        return add( s, new Vector() );
    }

    public Vector muli(Vector v )
    {
        return mul( v, this );
    }

    public Vector mul(Vector v, Vector out )
    {
        out.x = x * v.x;
        out.y = y * v.y;
        return out;
    }

    public Vector mul(Vector v )
    {
        return mul( v, new Vector() );
    }

    public Vector divi(Vector v )
    {
        return div( v, this );
    }

    public Vector div(Vector v, Vector out )
    {
        out.x = x / v.x;
        out.y = y / v.y;
        return out;
    }

    public Vector div(Vector v )
    {
        return div( v, new Vector() );
    }

    public Vector addi(Vector v )
    {
        return add( v, this );
    }

    public Vector add(Vector v, Vector out )
    {
        out.x = x + v.x;
        out.y = y + v.y;
        return out;
    }

    public Vector add(Vector v )
    {
        return add( v, new Vector() );
    }

    public Vector addsi(Vector v, float s )
    {
        return adds( v, s, this );
    }

    public Vector adds(Vector v, float s, Vector out )
    {
        out.x = x + v.x * s;
        out.y = y + v.y * s;
        return out;
    }

    public Vector adds(Vector v, float s )
    {
        return adds( v, s, new Vector() );
    }

    public Vector subi(Vector v )
    {
        return sub( v, this );
    }

    public Vector sub(Vector v, Vector out )
    {
        out.x = x - v.x;
        out.y = y - v.y;
        return out;
    }

    public Vector sub(Vector v )
    {
        return sub( v, new Vector() );
    }

    public float lengthSq()
    {
        return x * x + y * y;
    }

    public float length()
    {
        return (float)StrictMath.sqrt( x * x + y * y );
    }

    public void rotate( float radians )
    {
        float c = (float)StrictMath.cos( radians );
        float s = (float)StrictMath.sin( radians );

        float xp = x * c - y * s;
        float yp = x * s + y * c;

        x = xp;
        y = yp;
    }

    public void normalize()
    {
        float lenSq = lengthSq();

        if (lenSq > ImpulseMath.EPSILON_SQ)
        {
            float invLen = 1.0f / (float)StrictMath.sqrt( lenSq );
            x *= invLen;
            y *= invLen;
        }
    }

    public Vector mini(Vector a, Vector b )
    {
        return min( a, b, this );
    }

    public Vector maxi(Vector a, Vector b )
    {
        return max( a, b, this );
    }

    public float dot( Vector v )
    {
        return dot( this, v );
    }

    public float distanceSq( Vector v )
    {
        return distanceSq( this, v );
    }

    public float distance( Vector v )
    {
        return distance( this, v );
    }

    public Vector cross(Vector v, float a )
    {
        return cross( v, a, this );
    }
    public Vector cross(float a, Vector v )
    {
        return cross( a, v, this );
    }

    public float cross( Vector v )
    {
        return cross( this, v );
    }

    public static Vector min(Vector a, Vector b, Vector out )
    {
        out.x = (float)StrictMath.min( a.x, b.x );
        out.y = (float)StrictMath.min( a.y, b.y );
        return out;
    }

    public static Vector max(Vector a, Vector b, Vector out )
    {
        out.x = (float)StrictMath.max( a.x, b.x );
        out.y = (float)StrictMath.max( a.y, b.y );
        return out;
    }

    public static float dot(Vector a, Vector b )
    {
        return a.x * b.x + a.y * b.y;
    }

    public static float distanceSq(Vector a, Vector b )
    {
        float dx = a.x - b.x;
        float dy = a.y - b.y;

        return dx * dx + dy * dy;
    }

    public static float distance(Vector a, Vector b )
    {
        float dx = a.x - b.x;
        float dy = a.y - b.y;

        return (float)StrictMath.sqrt( dx * dx + dy * dy );
    }

    public static Vector cross(Vector v, float a, Vector out )
    {
        out.x = v.y * a;
        out.y = v.x * -a;
        return out;
    }

    public static Vector cross(float a, Vector v, Vector out )
    {
        out.x = v.y * -a;
        out.y = v.x * a;
        return out;
    }

    public static float cross(Vector a, Vector b )
    {
        return a.x * b.y - a.y * b.x;
    }

    public static Vector[] arrayOf(int length )
    {
        Vector[] array = new Vector[length];

        while (--length >= 0)
        {
            array[length] = new Vector();
        }

        return array;
    }

}
