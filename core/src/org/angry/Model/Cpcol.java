

package org.angry.Model;

import org.angry.view.Circle;
import org.angry.view.Polygon;


/**
 * Created by MyBaby on 12/4/2015.
 */


public class Cpcol implements ColInterface
{

	public static final Cpcol instance = new Cpcol();

	@Override
	public void handleCollision(Handel m, Body a, Body b )
	{
		Circle A = (Circle)a.shape;
		Polygon B = (Polygon)b.shape;

		m.contactCount = 0;

		Vector center = B.u.transpose().muli( a.position.sub( b.position ) );

		float separation = -Float.MAX_VALUE;
		int faceNormal = 0;
		for (int i = 0; i < B.vertexCount; ++i)
		{
			float s = Vector.dot( B.normals[i], center.sub( B.vertices[i] ) );

			if (s > A.radius)
			{
				return;
			}

			if (s > separation)
			{
				separation = s;
				faceNormal = i;
			}
		}

		Vector v1 = B.vertices[faceNormal];
		int i2 = faceNormal + 1 < B.vertexCount ? faceNormal + 1 : 0;
		Vector v2 = B.vertices[i2];

		float dot1 = Vector.dot( center.sub( v1 ), v2.sub( v1 ) );
		float dot2 = Vector.dot( center.sub( v2 ), v1.sub( v2 ) );
		m.penetration = A.radius - separation;

		if (dot1 <= 0.0f)
		{
			if (Vector.distanceSq( center, v1 ) > A.radius * A.radius)
			{
				return;
			}
			m.contactCount = 1;
			B.u.muli( m.normal.set( v1 ).subi( center ) ).normalize();
			B.u.mul( v1, m.contacts[0] ).addi( b.position );
		}

		else if (dot2 <= 0.0f)
		{
			if (Vector.distanceSq( center, v2 ) > A.radius * A.radius)
			{
				return;
			}

			m.contactCount = 1;
			B.u.muli( m.normal.set( v2 ).subi( center ) ).normalize();
			B.u.mul( v2, m.contacts[0] ).addi( b.position );
		}

		else
		{
			Vector n = B.normals[faceNormal];

			if (Vector.dot( center.sub( v1 ), n ) > A.radius)
			{
				return;
			}

			m.contactCount = 1;
			B.u.mul( n, m.normal ).negi();
			m.contacts[0].set( a.position ).addsi( m.normal, A.radius );
		}
	}

}
