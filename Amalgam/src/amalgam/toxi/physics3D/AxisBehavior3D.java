/*
 *   __               .__       .__  ._____.           
 * _/  |_  _______  __|__| ____ |  | |__\_ |__   ______
 * \   __\/  _ \  \/  /  |/ ___\|  | |  || __ \ /  ___/
 *  |  | (  <_> >    <|  \  \___|  |_|  || \_\ \\___ \ 
 *  |__|  \____/__/\_ \__|\___  >____/__||___  /____  >
 *                   \/       \/             \/     \/ 
 *
 * Copyright (c) 2006-2011 Karsten Schmidt
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * http://creativecommons.org/licenses/LGPL/2.1/
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301, USA
 */

package amalgam.toxi.physics3D;

import toxi.geom.SpatialIndex;
import toxi.geom.Vec2D;
import toxi.geom.Vec3D;
import toxi.physics3d.VerletParticle3D;
import toxi.physics3d.behaviors.ParticleBehavior3D;

public class AxisBehavior3D implements ParticleBehavior3D {

	protected float attrStrength;

	protected Vec3D axis;
	protected float radius, radiusSquared;
	protected float strength;
	protected float jitter;
	protected float timeStep;

	private int life;
	private int maxLife;

	public AxisBehavior3D(Vec3D axis, float radius, float strength) {
		this(axis, radius, strength, 0);
	}

	public AxisBehavior3D(Vec3D axis, float radius, float strength, float jitter) {
		this.strength = strength;
		this.jitter = jitter;
		setRadius(radius);

		this.life = 0;
		this.maxLife = (int) Float.POSITIVE_INFINITY;

		this.axis = axis.normalize();
	}

	@Override
	public void apply(VerletParticle3D p) {
		float dot = p.dot(axis);
		Vec3D _x = axis.getNormalizedTo(dot);
		Vec3D diff = p.sub(_x);

		p.addForce(diff.scale(-attrStrength / 10000f));
		diff.crossSelf(axis);
		float dist = diff.magSquared();

		// if (dist < radiusSquared && dist > 0) {
		// diff.scaleSelf(attrStrength).scaleSelf(1000f / dist);
		// diff.scaleSelf(attrStrength).scaleSelf(dist/100000f);
		diff.scaleSelf(attrStrength).scaleSelf(1 / 1000f);
		p.addForce(diff);
		// }
	}

	public void applyWithIndex(SpatialIndex<Vec2D> spaceHash) {
		// no
	}

	public void configure(float timeStep) {
		this.timeStep = timeStep;
		setStrength(strength);
	}

	/**
	 * @return the attractor
	 */
	public Vec3D getAxis() {
		return axis;
	}

	/**
	 * @return the jitter
	 */
	public float getJitter() {
		return jitter;
	}

	/**
	 * @return the radius
	 */
	public float getRadius() {
		return radius;
	}

	/**
	 * @return the strength
	 */
	public float getStrength() {
		return strength;
	}

	/**
	 * @param attractor
	 *            the attractor to set
	 */
	public void setAxis(Vec3D axis) {
		this.axis = axis;
	}

	/**
	 * @param jitter
	 *            the jitter to set
	 */
	public void setJitter(float jitter) {
		this.jitter = jitter;
	}

	public void setRadius(float r) {
		this.radius = r;
		this.radiusSquared = r * r;
	}

	/**
	 * @param strength
	 *            the strength to set
	 */
	public void setStrength(float strength) {
		this.strength = strength;
		this.attrStrength = strength * timeStep;
	}

	public boolean supportsSpatialIndex() {
		return false;
	}

	// MY ADDITIONS

	@Override
	public void update() {
		life += 1;
	}

	public int getMaxLife() {
		return maxLife;
	}

	public int getLife() {
		return life;
	}

}
