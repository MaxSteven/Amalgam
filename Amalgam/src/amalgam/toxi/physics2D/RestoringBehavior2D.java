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

package amalgam.toxi.physics2D;

import toxi.geom.SpatialIndex;
import toxi.geom.Vec2D;
import toxi.physics2d.VerletParticle2D;
import toxi.physics2d.behaviors.ParticleBehavior2D;

/**
 * Currently very jerky - need to find a way to smooth things which doesnt use higher drag in the particle system.
 */
public class RestoringBehavior2D implements ParticleBehavior2D {

	protected float attrStrength;

	protected float strength;
	protected float jitter;
	protected float timeStep;

	private int age;
	private int life;

	private Vec2D axes;

	public RestoringBehavior2D(float strength) {
		this(strength, 0, new Vec2D(1, 1));
	}

	public RestoringBehavior2D(float strength, Vec2D axes) {
		this(strength, 0, axes);
	}

	public RestoringBehavior2D(float strength, float jitter, Vec2D axes) {
		this.strength = strength;
		this.jitter = jitter;

		// to restore only in only hor/vert directions.
		this.axes = axes;

		this.age = 0;
		this.life = (int) Float.POSITIVE_INFINITY;
	}

	public void apply(VerletParticle2D p) {
		Vec2D delta = p.getOrigPos().sub(p);
		// scale down differently? TODO ?

		Vec2D vt = new Vec2D(delta.x * axes.x, delta.y * axes.y);
		Vec2D f = vt.jitter(jitter).scaleSelf(attrStrength * 0.01f);
		p.addForce(f);

	}

	public void applyWithIndex(SpatialIndex<Vec2D> spaceHash) {
		throw new UnsupportedOperationException("not implemented");
	}

	public void configure(float timeStep) {
		this.timeStep = timeStep;
		setStrength(strength);
	}

	/**
	 * @return the jitter
	 */
	public float getJitter() {
		return jitter;
	}

	/**
	 * @return the strength
	 */
	public float getStrength() {
		return strength;
	}

	/**
	 * @param jitter
	 *            the jitter to set
	 */
	public void setJitter(float jitter) {
		this.jitter = jitter;
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
		return true;
	}

	@Override
	public void update() {
		age += 1;
	}

	@Override
	public int getAge() {
		return age;
	}

	@Override
	public int getLife() {
		return life;
	}

}
