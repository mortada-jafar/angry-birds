
package org.angry.Model;

import org.angry.view.Polygon;

/**
 * Created by MyBaby on 12/15/2015.
 */

public class Ppcol implements ColInterface {

    public static final Ppcol instance = new Ppcol();

    @Override
    public void handleCollision(Handel m, Body a, Body b) {
        Polygon A = (Polygon) a.shape;
        Polygon B = (Polygon) b.shape;
        m.contactCount = 0;

        int[] faceA = {0};
        float penetrationA = findAxisLeastPenetration(faceA, A, B);
        if (penetrationA >= 0.0f) {
            return;
        }

        int[] faceB = {0};
        float penetrationB = findAxisLeastPenetration(faceB, B, A);
        if (penetrationB >= 0.0f) {
            return;
        }

        int referenceIndex;
        boolean flip;

        Polygon RefPoly;
        Polygon IncPoly;

        if (ImpulseMath.gt(penetrationA, penetrationB)) {
            RefPoly = A;
            IncPoly = B;
            referenceIndex = faceA[0];
            flip = false;
        } else {
            RefPoly = B;
            IncPoly = A;
            referenceIndex = faceB[0];
            flip = true;
        }

        Vector[] incidentFace = Vector.arrayOf(2);

        findIncidentFace(incidentFace, RefPoly, IncPoly, referenceIndex);

        Vector v1 = RefPoly.vertices[referenceIndex];
        referenceIndex = referenceIndex + 1 == RefPoly.vertexCount ? 0 : referenceIndex + 1;
        Vector v2 = RefPoly.vertices[referenceIndex];

        v1 = RefPoly.u.mul(v1).addi(RefPoly.body.position);
        v2 = RefPoly.u.mul(v2).addi(RefPoly.body.position);

        Vector sidePlaneNormal = v2.sub(v1);
        sidePlaneNormal.normalize();

        Vector refFaceNormal = new Vector(sidePlaneNormal.y, -sidePlaneNormal.x);

        float refC = Vector.dot(refFaceNormal, v1);

        m.normal.set(refFaceNormal);
        if (flip) {
            m.normal.negi();
        }

        int cp = 0;
        float separation = Vector.dot(refFaceNormal, incidentFace[0]) - refC;
        if (separation <= 0.0f) {
            m.contacts[cp].set(incidentFace[0]);
            m.penetration = -separation;
            ++cp;
        } else {
            m.penetration = 0;
        }

        separation = Vector.dot(refFaceNormal, incidentFace[1]) - refC;

        if (separation <= 0.0f) {
            m.contacts[cp].set(incidentFace[1]);

            m.penetration += -separation;
            ++cp;
            m.penetration /= cp;
        }

        m.contactCount = cp;
    }

    public float findAxisLeastPenetration(int[] faceIndex, Polygon A, Polygon B) {
        float bestDistance = -Float.MAX_VALUE;
        int bestIndex = 0;

        for (int i = 0; i < A.vertexCount; ++i) {
            Vector nw = A.u.mul(A.normals[i]);

            Matrix buT = B.u.transpose();
            Vector n = buT.mul(nw);

            Vector s = B.getSupport(n.neg());
//            System.out.println(s.x + "   " + s.y);
            Vector v = buT.muli(A.u.mul(A.vertices[i]).addi(A.body.position).subi(B.body.position));

            float d = Vector.dot(n, s.sub(v));

            if (d > bestDistance) {
                bestDistance = d;
                bestIndex = i;
            }
        }

        faceIndex[0] = bestIndex;
        return bestDistance;
    }

    public void findIncidentFace(Vector[] v, Polygon RefPoly, Polygon IncPoly, int referenceIndex) {
        Vector referenceNormal = RefPoly.normals[referenceIndex];

        referenceNormal = RefPoly.u.mul(referenceNormal);
        referenceNormal = IncPoly.u.transpose().mul(referenceNormal);

        int incidentFace = 0;
        float minDot = Float.MAX_VALUE;
        for (int i = 0; i < IncPoly.vertexCount; ++i) {
            float dot = Vector.dot(referenceNormal, IncPoly.normals[i]);

            if (dot < minDot) {
                minDot = dot;
                incidentFace = i;
            }
        }

        v[0] = IncPoly.u.mul(IncPoly.vertices[incidentFace]).addi(IncPoly.body.position);
        incidentFace = incidentFace + 1 >= IncPoly.vertexCount ? 0 : incidentFace + 1;
        v[1] = IncPoly.u.mul(IncPoly.vertices[incidentFace]).addi(IncPoly.body.position);
    }


}
