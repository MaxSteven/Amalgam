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

import java.util.List;

import processing.core.PApplet;
import toxi.geom.SpatialIndex;
import toxi.geom.Vec2D;
import toxi.physics2d.VerletParticle2D;
import toxi.physics2d.behaviors.ParticleBehavior2D;

/**
 * Changed to f = -kx and infinite radius application. TODO: remove radius etc
 */
public class MouseBehavior2D implements ParticleBehavior2D {
	private final PApplet p5;

	protected Vec2D attractor;
	protected float attrStrength;

	protected float radius, radiusSquared;
	protected float strength;
	protected float jitter;
	protected float timeStep;

	private int life;
	private int maxLife;

	private float scaling;

	public MouseBehavior2D(PApplet p5, float radius, float strength) {
		this(p5, radius, strength, 0);
	}

	public MouseBehavior2D(PApplet p5, float radius, float strength, float jitter) {
		this.p5 = p5;
		this.attractor = new Vec2D(p5.mouseX, p5.mouseY);
		this.strength = strength;
		this.jitter = jitter;
		setRadius(radius);

		this.scaling = 1;

		this.life = 0;
		this.maxLife = (int) Float.POSITIVE_INFINITY;
	}

	public MouseBehavior2D(PApplet p5, float radius, float strength, float scaling, float jitter) {
		this.p5 = p5;
		this.attractor = new Vec2D(scaling * p5.mouseX, scaling * p5.mouseY);
		this.strength = strength;
		this.jitter = jitter;
		setRadius(radius);

		this.scaling = scaling; // should radius also be scaled here?

		this.life = 0;
		this.maxLife = (int) Float.POSITIVE_INFINITY;
	}

	@Override
	public void update() {
		life += 1;
		attractor.set(scaling * p5.mouseX, scaling * p5.mouseY);
	}

	public void apply(VerletParticle2D p) {
		Vec2D delta = attractor.sub(p);
		float dist = delta.magSquared();
		if (dist < radiusSquared) {
			// Vec2D f = delta.normalizeTo((1.0f - dist / radiusSquared)).jitter(jitter).scaleSelf(attrStrength); original.. not smooth at small distances
			Vec2D f = delta.jitter(jitter).scaleSelf(attrStrength * 0.005f);
			p.addForce(f);
		}
	}

	public void applyWithIndex(SpatialIndex<Vec2D> spaceHash) {
		List<Vec2D> selection = spaceHash.itemsWithinRadius(attractor, radius, null);
		final Vec2D temp = new Vec2D();
		if (selection != null) {
			for (Vec2D p : selection) {
				temp.set(p);
				apply((VerletParticle2D) p);
				spaceHash.reindex(temp, p);
			}
		}
	}

	public void configure(float timeStep) {
		this.timeStep = timeStep;
		setStrength(strength);
	}

	/**
	 * @return the attractor
	 */
	public Vec2D getAttractor() {
		return attractor;
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
	public void setAttractor(Vec2D attractor) {
		this.attractor = attractor;
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
		return true;
	}

	//

	@Override
	public int getAge() {
		return life;
	}

	@Override
	public int getLife() {
		return maxLife;
	}
}
