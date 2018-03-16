package org.angry.view;


import org.angry.Model.Vector;
/**
 * Created by MyBaby on 12/18/2015.
 */
public class Polygon extends Shape
{

	public static final int MAX_POLY_VERTEX_COUNT = 64;

	public int vertexCount;
	public float width ,height;
	public Vector[] vertices = Vector.arrayOf( MAX_POLY_VERTEX_COUNT );
	public Vector[] normals = Vector.arrayOf( MAX_POLY_VERTEX_COUNT );

	public Polygon( float hw, float hh )
	{
		this.width=hw;
		this.height=hh;
		setBox( hw, hh );
	}



	@Override
	public void initialize()
	{
		computeMass(0.1f);
	}

	@Override
	public void computeMass( float density )
	{

		Vector c = new Vector( 0.0f, 0.0f );
		float area = 0.0f;
		float I = 0.0f;
		final float k = 1.0f / 3.0f;

		for (int i = 0; i < vertexCount; ++i)
		{
			Vector p1 = vertices[i];
			Vector p2 = vertices[(i + 1) % vertexCount];

			float D = Vector.cross( p1, p2 );
			float triangleArea = 0.5f * D;

			area += triangleArea;

			float weight = triangleArea * k;
			c.addsi( p1, weight );
			c.addsi( p2, weight );

			float intx2 = p1.x * p1.x + p2.x * p1.x + p2.x * p2.x;
			float inty2 = p1.y * p1.y + p2.y * p1.y + p2.y * p2.y;
			I += (0.25f * k* D) * (intx2 + inty2);
		}

		c.muli( 1.0f / area );

		body.mass = density * area;
		body.invMass = (body.mass != 0.0f) ? 1.0f / body.mass : 0.0f;
		body.inertia = I * density;
		body.invInertia = (body.inertia != 0.0f) ? 1.0f / body.inertia : 0.0f;
	}

	@Override
	public void setOrient( float radians )
	{
		u.set( radians );
	}

	@Override
	public Type getType()
	{
		return Type.Poly;
	}

	public void setBox( float hw, float hh )
	{
		vertexCount = 4;
		vertices[0].set( -hw, -hh );
		vertices[1].set( hw, -hh );
		vertices[2].set( hw, hh );
		vertices[3].set( -hw, hh );
		normals[0].set( 0.0f, -1.0f );
		normals[1].set( 1.0f, 0.0f );
		normals[2].set( 0.0f, 1.0f );
		normals[3].set( -1.0f, 0.0f );
	}


	public Vector getSupport( Vector dir )
	{
		float bestProjection = -Float.MAX_VALUE;
		Vector bestVertex = null;

		for (int i = 0; i < vertexCount; ++i)
		{
			Vector v = vertices[i];
			float projection = Vector.dot( v, dir );

			if (projection > bestProjection)
			{
				bestVertex = v;
				bestProjection = projection;
			}
		}

		return bestVertex;
	}

}
