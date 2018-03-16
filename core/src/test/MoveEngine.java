package test;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class MoveEngine extends Thread
{
	private long timePassed = 0;
	private long curTime = 0;
	private long lastTime = 0;
	private double timeFraction = 0.0;
	private ArrayList<Accel> constForces = new ArrayList<Accel>();

	public void run()
	{
		curTime = System.currentTimeMillis();
		initializeConstForces();
		while (Main.isRunning) {
			updateTime();
			applyConstForces();
			sumForces();
			moveEnts();
			try {
				sleep(1);
			} catch (InterruptedException e) {
			}
		}
	}

	private void updateTime()
	{
		lastTime = curTime;
		curTime = System.currentTimeMillis();
		timePassed = (curTime - lastTime);
		timeFraction = (timePassed / 1000.0);
	}

	private void initializeConstForces()
	{
		constForces.add(new Accel(0.0, Main.GRAVITY));
	}

	private synchronized void applyConstForces()
	{
		double xAccel = 0, yAccel = 0;
		// Find the total acceleration of all const forces.
		for (int i = 0; i < constForces.size(); i++) {
			xAccel += constForces.get(i).ax();
			yAccel += constForces.get(i).ay();
		}
		// Apply the sum acceleration to each entity.
		for (int i = 0; i < Main.living.size(); i++) {
			Spawn s = Main.living.get(i);
			s.addAccel(new Accel(xAccel, yAccel));
		}
	}

	private synchronized void sumForces()
	{
		for (int i = 0; i < Main.living.size(); i++) {
			Spawn s = Main.living.get(i);
			// Get the sum of all accelerations acting on object.
			Accel theAccel = s.sumAccel();
			// Apply the resulting change in velocity.
			double vx = s.vx() + (theAccel.ax() * timeFraction);
			double vy = s.vy() + (theAccel.ay() * timeFraction);
			s.updateVelocity(vx, vy);
			// Apply drag coefficient
			s.applyDrag(1.0 - (timeFraction * Main.DRAG));
		}
	}

	private synchronized void moveEnts()
	{
		for (int i = 0; i < Main.living.size(); i++) {
			Spawn s = Main.living.get(i);
			// Get the initial x and y coords.
			double oldX = s.getX(), oldY = s.getY();
			// Calculate the new x and y coords.
			double newX = oldX + (s.vx() * timeFraction);
			double newY = oldY + (s.vy() * timeFraction);
			s.updatePos(newX, newY);
			checkWallCollisions(s);
		}
		checkCollisions();
	}

	private synchronized void checkCollisions()
	{
		for (int i = 0; i < Main.living.size() - 1; i++) {
			Spawn s = Main.living.get(i);
			Point2D sCenter = s.getCenter();
			for (int j = i + 1; j < Main.living.size(); j++) {
				Spawn t = Main.living.get(j);
				if (t == null) break;
				Point2D tCenter = t.getCenter();
				double distBetween = sCenter.distance(tCenter);
				double bigR = s.getRadius() > t.getRadius() ? s.getRadius() : t
						.getRadius();
				if (distBetween < (bigR * 2)) collide(s, t, distBetween);
			}
		}
	}

	private synchronized void collide(Spawn s, Spawn t, double distBetween)
	{
		// Get the relative x and y dist between them.
		double relX = s.getX() - t.getX();
		double relY = s.getY() - t.getY();
		// Take the arctan to find the collision angle.
		double collisionAngle = Math.atan2(relY, relX);
		// if (collisionAngle < 0) collisionAngle += 2 * Math.PI;
		// Rotate the coordinate systems for each object's velocity to align
		// with the collision angle. We do this by supplying the collision angle
		// to the vector's rotateCoordinates method.
		Vector2D sVel = s.velVector(), tVel = t.velVector();
		sVel.rotateCoordinates(collisionAngle);
		tVel.rotateCoordinates(collisionAngle);
		// In the collision coordinate system, the contact normals lie on the
		// x-axis. Only the velocity values along this axis are affected. We can
		// now apply a simple 1D momentum equation where the new x-velocity of
		// the first object equals a negative times the x-velocity of the
		// second.
		double swap = sVel.x;
		sVel.x = tVel.x;
		tVel.x = swap;
		// Now we need to get the vectors back into normal coordinate space.
		sVel.restoreCoordinates();
		tVel.restoreCoordinates();
		// Give each object its new velocity.
		s.updateVelocity(sVel.x * Main.BOUNCE, sVel.y * Main.BOUNCE);
		t.updateVelocity(tVel.x * Main.BOUNCE, tVel.y * Main.BOUNCE);
		// Back them up in the opposite angle so they are not overlapping.
		double minDist = s.getRadius() + t.getRadius();
		double overlap = minDist - distBetween;
		double toMove = overlap / 2;
		double newX = s.getX() + (toMove * Math.cos(collisionAngle));
		double newY = s.getY() + (toMove * Math.sin(collisionAngle));
		s.updatePos(newX, newY);
		newX = t.getX() - (toMove * Math.cos(collisionAngle));
		newY = t.getY() - (toMove * Math.sin(collisionAngle));
		t.updatePos(newX, newY);
	}

	private synchronized void checkWallCollisions(Spawn s)
	{
		int maxY = 480 - s.dimY();
		int maxX = 640 - s.dimX();
		if (s.getY() > maxY) {
			s.updatePos(s.getX(), maxY);
			s.updateVelocity(s.vx(), (s.vy() * -Main.BOUNCE));
		}
		if (s.getX() > maxX) {
			s.updatePos(maxX, s.getY());
			s.updateVelocity((s.vx() * -Main.BOUNCE), s.vy());
		}
		if (s.getX() < 1) {
			s.updatePos(1, s.getY());
			s.updateVelocity((s.vx() * -Main.BOUNCE), s.vy());
		}
	}
}
