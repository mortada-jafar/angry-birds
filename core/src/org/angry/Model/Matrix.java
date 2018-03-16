
package org.angry.Model;

public class Matrix
{

	public float m00, m01;
	public float m10, m11;

	public Matrix()
	{
	}

	public Matrix(float radians )
	{
		set( radians );
	}

	public Matrix(float a, float b, float c, float d )
	{
		set( a, b, c, d );
	}

	public void set( float radians )
	{
		float c = (float)StrictMath.cos( radians );
		float s = (float)StrictMath.sin( radians );

		m00 = c;
		m01 = -s;
		m10 = s;
		m11 = c;
	}

	public void set( float a, float b, float c, float d )
	{
		m00 = a;
		m01 = b;
		m10 = c;
		m11 = d;
	}

	public void set( Matrix m )
	{
		m00 = m.m00;
		m01 = m.m01;
		m10 = m.m10;
		m11 = m.m11;
	}

	public void absi()
	{
		abs( this );
	}

	public Matrix abs()
	{
		return abs( new Matrix() );
	}

	public Matrix abs(Matrix out )
	{
		out.m00 = StrictMath.abs( m00 );
		out.m01 = StrictMath.abs( m01 );
		out.m10 = StrictMath.abs( m10 );
		out.m11 = StrictMath.abs( m11 );
		return out;
	}

	public Vector getAxisX(Vector out )
	{
		out.x = m00;
		out.y = m10;
		return out;
	}

	public Vector getAxisX()
	{
		return getAxisX( new Vector() );
	}

	public Vector getAxisY(Vector out )
	{
		out.x = m01;
		out.y = m11;
		return out;
	}

	public Vector getAxisY()
	{
		return getAxisY( new Vector() );
	}

	public void transposei()
	{
		float t = m01;
		m01 = m10;
		m10 = t;
	}
	public Matrix transpose(Matrix out )
	{
		out.m00 = m00;
		out.m01 = m10;
		out.m10 = m01;
		out.m11 = m11;
		return out;
	}

	public Matrix transpose()
	{
		return transpose( new Matrix() );
	}

	public Vector muli(Vector v )
	{
		return mul( v.x, v.y, v );
	}

	public Vector mul(Vector v, Vector out )
	{
		return mul( v.x, v.y, out );
	}

	public Vector mul(Vector v )
	{
		return mul( v.x, v.y, new Vector() );
	}

	public Vector mul(float x, float y, Vector out )
	{
		out.x = m00 * x + m01 * y;
		out.y = m10 * x + m11 * y;
		return out;
	}

	public void muli( Matrix x )
	{
		set(
			m00 * x.m00 + m01 * x.m10,
			m00 * x.m01 + m01 * x.m11,
			m10 * x.m00 + m11 * x.m10,
			m10 * x.m01 + m11 * x.m11 );
	}

	public Matrix mul(Matrix x, Matrix out )
	{
		out.m00 = m00 * x.m00 + m01 * x.m10;
		out.m01 = m00 * x.m01 + m01 * x.m11;
		out.m10 = m10 * x.m00 + m11 * x.m10;
		out.m11 = m10 * x.m01 + m11 * x.m11;
		return out;
	}
	public Matrix mul(Matrix x )
	{
		return mul( x, new Matrix() );
	}

}
