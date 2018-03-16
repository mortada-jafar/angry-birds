package test;

public class Vector2D
{
	public double x;
	public double y;
	public double restoreAngle;

	public Vector2D(double x, double y)
	{
		this.x = x;
		this.y = y;
		this.restoreAngle = 0.0;
	}

	public double angle()
	{
		return Math.atan2(y, x);
	}

	public double mag()
	{
		return Math.sqrt((x * x) + (y * y));
	}

	public void rotateCoordinates(double tiltAngle)
	{
		this.restoreAngle += tiltAngle;
		double angle = angle();
		double mag = mag();
		angle -= tiltAngle;
		x = mag * Math.cos(angle);
		y = mag * Math.sin(angle);
	}

	public void restoreCoordinates()
	{
		double angle = angle();
		double mag = mag();
		angle += restoreAngle;
		x = mag * Math.cos(angle);
		y = mag * Math.sin(angle);
		restoreAngle = 0.0;
	}
}
