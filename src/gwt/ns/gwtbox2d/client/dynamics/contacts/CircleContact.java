/*
 * JBox2D - A Java Port of Erin Catto's Box2D
 * 
 * JBox2D homepage: http://jbox2d.sourceforge.net/
 * Box2D homepage: http://www.box2d.org
 * 
 * This software is provided 'as-is', without any express or implied
 * warranty.  In no event will the authors be held liable for any damages
 * arising from the use of this software.
 * 
 * Permission is granted to anyone to use this software for any purpose,
 * including commercial applications, and to alter it and redistribute it
 * freely, subject to the following restrictions:
 * 
 * 1. The origin of this software must not be misrepresented; you must not
 * claim that you wrote the original software. If you use this software
 * in a product, an acknowledgment in the product documentation would be
 * appreciated but is not required.
 * 2. Altered source versions must be plainly marked as such, and must not be
 * misrepresented as being the original software.
 * 3. This notice may not be removed or altered from any source distribution.
 */

package gwt.ns.gwtbox2d.client.dynamics.contacts;

import gwt.ns.gwtbox2d.client.collision.Manifold;
import gwt.ns.gwtbox2d.client.collision.ManifoldPoint;
import gwt.ns.gwtbox2d.client.collision.shapes.CircleShape;
import gwt.ns.gwtbox2d.client.collision.shapes.Shape;
import gwt.ns.gwtbox2d.client.collision.shapes.ShapeType;
import gwt.ns.gwtbox2d.client.common.Vec2;
import gwt.ns.gwtbox2d.client.dynamics.Body;
import gwt.ns.gwtbox2d.client.dynamics.ContactListener;
import gwt.ns.gwtbox2d.client.pooling.SingletonPool;
import gwt.ns.gwtbox2d.client.pooling.TLContactPoint;
import gwt.ns.gwtbox2d.client.pooling.TLManifold;
import gwt.ns.gwtbox2d.client.pooling.TLVec2;

import java.util.ArrayList;
import java.util.List;


// Updated to rev 142 of b2CircleContact.h/cpp

public class CircleContact extends Contact implements ContactCreateFcn {

	public final Manifold m_manifold;
	public final ArrayList<Manifold> manifoldList = new ArrayList<Manifold>();

	public Contact create(final Shape shape1, final Shape shape2) {
		return new CircleContact(shape1, shape2);
	}

	public static void Destroy(final Contact contact) {
		((CircleContact) contact).destructor();
	}

	@Override
	public CircleContact clone() {
		return this;
	}

	public CircleContact() {
		super();
		m_manifold = new Manifold();
		m_manifoldCount = 0;
		manifoldList.add(m_manifold);
	}

	public CircleContact(final Shape shape1, final Shape shape2) {
		super(shape1, shape2);
		m_manifold = new Manifold();
		manifoldList.add(m_manifold);
		assert (m_shape1.getType() == ShapeType.CIRCLE_SHAPE);
		assert (m_shape2.getType() == ShapeType.CIRCLE_SHAPE);
		m_manifold.pointCount = 0;
		m_manifold.points[0].normalImpulse = 0.0f;
		m_manifold.points[0].tangentImpulse = 0.0f;
		m_manifold.points[0].localPoint1.setZero();
		m_manifold.points[0].localPoint2.setZero();
	}

	public void destructor() {

	}
	
	private static final TLManifold tlm0 = new TLManifold();
	private static final TLVec2 tlV1 = new TLVec2();
	private static final TLContactPoint tlCp = new TLContactPoint();
	@Override
	public void evaluate(final ContactListener listener) {
		//CollideCircle.collideCircle(m_manifold, (CircleShape) m_shape1,
		//        (CircleShape) m_shape2, false);
		final Body b1 = m_shape1.getBody();
		final Body b2 = m_shape2.getBody();

		final Manifold m0 = tlm0.get();
		final Vec2 v1 = tlV1.get();
		final ContactPoint cp = tlCp.get();

		m0.set(m_manifold);

		SingletonPool.getCollideCircle().collideCircles(m_manifold, (CircleShape)m_shape1, b1.m_xf, (CircleShape)m_shape2, b2.m_xf);

		cp.shape1 = m_shape1;
		cp.shape2 = m_shape2;
		cp.friction = m_friction;
		cp.restitution = m_restitution;


		if (m_manifold.pointCount > 0) {
			m_manifoldCount = 1;
			final ManifoldPoint mp = m_manifold.points[0];
			if (m0.pointCount == 0) {
				mp.normalImpulse = 0.0f;
				mp.tangentImpulse = 0.0f;

				if (listener != null) {
					b1.getWorldLocationToOut(mp.localPoint1, cp.position);
					//Vec2 v1 = b1.getLinearVelocityFromLocalPoint(mp.localPoint1);
					b1.getLinearVelocityFromLocalPointToOut(mp.localPoint1, v1);
					b2.getLinearVelocityFromLocalPointToOut(mp.localPoint2, cp.velocity);
					//cp.velocity = v2.sub(v1);
					cp.velocity.subLocal(v1);
					cp.normal.set(m_manifold.normal);
					cp.separation = mp.separation;
					cp.id.set(mp.id);
					listener.add(cp);
				}
			} else {
				final ManifoldPoint mp0 = m0.points[0];
				mp.normalImpulse = mp0.normalImpulse;
				mp.tangentImpulse = mp0.tangentImpulse;

				if (listener != null) {
					b1.getWorldLocationToOut(mp.localPoint1, cp.position);

					//Vec2 v1 = b1.getLinearVelocityFromLocalPoint(mp.localPoint1);
					b1.getLinearVelocityFromLocalPointToOut(mp.localPoint1, v1);
					//Vec2 v2 = b2.getLinearVelocityFromLocalPoint(mp.localPoint2);
					b2.getLinearVelocityFromLocalPointToOut(mp.localPoint2, cp.velocity);
					cp.velocity.subLocal(v1);
					cp.normal.set(m_manifold.normal);
					cp.separation = mp.separation;
					cp.id.set(mp.id);
					listener.persist(cp);
				}
			}
		} else {
			m_manifoldCount = 0;
			if (m0.pointCount > 0 && (listener != null)) {
				final ManifoldPoint mp0 = m0.points[0];
				b1.getWorldLocationToOut(mp0.localPoint1, cp.position);
				//Vec2 v1 = b1.getLinearVelocityFromLocalPoint(mp0.localPoint1);
				b1.getLinearVelocityFromLocalPointToOut(mp0.localPoint1, v1);
				//Vec2 v2 = b2.getLinearVelocityFromLocalPoint(mp.localPoint2);
				 b2.getLinearVelocityFromLocalPointToOut(mp0.localPoint2, cp.velocity);
				cp.velocity.subLocal(v1);
				cp.normal.set(m0.normal);
				cp.separation = mp0.separation;
				cp.id.set(mp0.id);
				listener.remove(cp);
			}
		}
	}

	@Override
	public List<Manifold> getManifolds() {
		return manifoldList;
	}

}