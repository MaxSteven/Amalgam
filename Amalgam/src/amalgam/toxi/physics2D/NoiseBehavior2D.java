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

import processing.core.PApplet;
import toxi.geom.SpatialIndex;
import toxi.geom.Vec2D;
import toxi.math.noise.SimplexNoise;
import toxi.physics2d.VerletParticle2D;
import toxi.physics2d.behaviors.ParticleBehavior2D;

/**
 * Not working at the moment (3/10/14)
 */
public class NoiseBehavior2D implements ParticleBehavior2D {
	private final PApplet p5;
	protected float attrStrength;

	protected float strength;
	protected float scale;
	protected float jitter;
	protected float timeStep;

	private int age;
	private int life;

	private int seed;

	public NoiseBehavior2D(PApplet p5, float strength, float scale) {
		this(p5, strength, scale, 0);
	}

	public NoiseBehavior2D(PApplet p5, float strength, float scale, float jitter) {
		this.p5 = p5;
		this.strength = strength;
		this.scale = scale;
		this.jitter = jitter;

		this.age = 0;
		this.life = (int) Float.POSITIVE_INFINITY;

		seed = (int) p5.random(100);
	}

	public void apply(VerletParticle2D p) {
		// Attempting to make this work as intended (previous implementation pushes everything to the right in general )
//		Vec2D f = new Vec2D((float) SimplexNoise.noise(p.x / scale, p.y / scale, p5.frameCount/scale), (float) SimplexNoise.noise(p.y / scale, p.x / scale, p5.frameCount/scale));
		Vec2D f = new Vec2D(1, 0).rotate(p5.TAU*(float)SimplexNoise.noise(p.x / scale, p.y / scale, p5.frameCount/scale));

		f.normalize();
		f = f.jitter(jitter);
		f.scaleSelf(attrStrength);
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
