
package org.angry.Model;

/**
 * Created by MyBaby on 12/15/2015.
 */
public class Handel {

    public Body A;
    public Body B;
    public float penetration;
    public final Vector normal = new Vector();
    public final Vector[] contacts = {new Vector(), new Vector()};
    public int contactCount;
    public float e;
    public float df;
    public float sf;

    public Handel(Body a, Body b) {
        A = a;
        B = b;
    }

    public void solve() {
        int ia = A.shape.getType().ordinal();
        int ib = B.shape.getType().ordinal();
        Col.dispatch[ia][ib].handleCollision(this, A, B);
    }

    public void initialize() {
        e = StrictMath.min(A.restitution, B.restitution);

        sf = (float) StrictMath.sqrt(A.staticFriction * A.staticFriction + B.staticFriction * B.staticFriction);
        df = (float) StrictMath.sqrt(A.dynamicFriction * A.dynamicFriction + B.dynamicFriction * B.dynamicFriction);

    }

    public void applyImpulse() {
        if (ImpulseMath.equal(A.invMass + B.invMass, 0)) {
            infiniteMassCorrection();
            return;
        }

        for (int i = 0; i < contactCount; ++i) {
            //contact Vector
            Vector ra = contacts[i].sub(A.position);
            Vector rb = contacts[i].sub(B.position);

            // velocity nsbia
            Vector rv = B.velocity.add(Vector.cross(B.angularVelocity, rb, new Vector())).subi(A.velocity).subi(Vector.cross(A.angularVelocity, ra, new Vector()));


            float contactVel = Vector.dot(rv, normal);

            if (contactVel > 0) {
                return;
            }

            //vector of contact with normal vector
            float raCrossN = Vector.cross(ra, normal);
            float rbCrossN = Vector.cross(rb, normal);

            //sum of mass with inertia   // lakhty
            float invMassSum = A.invMass + B.invMass + (raCrossN * raCrossN) * A.invInertia + (rbCrossN * rbCrossN) * B.invInertia;


            float j = -(1.0f + e) * contactVel;
            j /= invMassSum;
            //  j/m1+m2
            j /= contactCount;

            // Apply impulse
            Vector impulse = normal.mul(j);
            A.applyImpulse(impulse.neg(), ra);
            B.applyImpulse(impulse, rb);

            // Friction impulse
            rv = B.velocity.add(Vector.cross(B.angularVelocity, rb, new Vector())).subi(A.velocity).subi(Vector.cross(A.angularVelocity, ra, new Vector()));
            Vector t = new Vector(rv);
            t.addsi(normal, -Vector.dot(rv, normal));
            t.normalize();

            // j tangent magnitude
            float jt = -Vector.dot(rv, t);
            jt /= invMassSum;
            jt /= contactCount;

            //khyly sgiry
            if (ImpulseMath.equal(jt, 0.0f)) {
                return;
            }

            Vector tangentImpulse;

            if (StrictMath.abs(jt) < j * sf) {
                tangentImpulse = t.mul(jt);
            } else {
                tangentImpulse = t.mul(j).muli(-df);
            }

            A.applyImpulse(tangentImpulse.neg(), ra);
            B.applyImpulse(tangentImpulse, rb);
        }
    }

    public void positionalCorrection() {
        float correction = StrictMath.max(penetration - ImpulseMath.PENETRATION_ALLOWANCE, 0.0f) / (A.invMass + B.invMass) * ImpulseMath.PENETRATION_CORRETION;

        A.position.addsi(normal, -A.invMass * correction);
        B.position.addsi(normal, B.invMass * correction);
    }

    public void infiniteMassCorrection() {
        A.velocity.set(0, 0);
        B.velocity.set(0, 0);
    }

}
