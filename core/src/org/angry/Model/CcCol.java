
package org.angry.Model;


import org.angry.view.Circle;

/**
 * Created by MyBaby on 12/15/2015.
 */
public class CcCol implements ColInterface {

    public static final CcCol instance = new CcCol();

    @Override
    public void handleCollision(Handel m, Body a, Body b) {
        Circle A = (Circle) a.shape;
        Circle B = (Circle) b.shape;


        Vector normal = b.position.sub(a.position);
        float dist_sqr = normal.lengthSq();
        float radius = A.radius + B.radius;
        if (dist_sqr >= radius * radius) {
            m.contactCount = 0;
            return;
        }
        float distance = (float) Math.sqrt(dist_sqr);

        m.contactCount = 1;

        m.penetration = radius - distance;
        m.normal.set(normal).divi(distance);
        m.contacts[0].set(m.normal).muli(A.radius).addi(a.position); // n.xx=r*n.x+a.x
    }

}
