package org.angry.view;

import org.angry.Model.Matrix;
import org.angry.Model.Body;
/**
 * Created by MyBaby on 12/18/2015.
 */
public abstract class Shape
{

	public enum Type
	{
		Circle, Poly
	}

	public Body body;
	public float radius;
	public final Matrix u = new Matrix();

	public Shape()
	{
	}


	public abstract void initialize();

	public abstract void computeMass( float density );

	public abstract void setOrient( float radians );

	public abstract Type getType();

}
