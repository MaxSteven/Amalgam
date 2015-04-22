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

import processing.core.PApplet;
import toxi.geom.Vec3D;
import toxi.physics3d.VerletParticle3D;
import toxi.physics3d.behaviors.ParticleBehavior3D;

public class NoiseBehaviour3D implements ParticleBehavior3D {
	private final PApplet p5;

	protected Vec3D attractor;
	protected float attrStrength;

	protected float strength, scale, jitter;
	protected float timeStep;

	private int seed, age, life;

	public NoiseBehaviour3D(PApplet p5, float strength, float scale) {
		this(p5, strength, scale, 0);
	}

	public NoiseBehaviour3D(PApplet p5, float strength, float scale, float jitter) {
		this.p5 = p5;
		this.strength = strength;
		this.scale = scale;
		this.jitter = jitter;

		this.age = 0;
		this.life = Integer.MAX_VALUE;

		seed = (int) p5.random(100);
	}

	@Override
	public void update() {
		age++; // update is never called from VerletPhysics3D...? Age always stays the same :/
	}

	public void apply(VerletParticle3D p) {
		// Vec3D f = new Vec3D(1f, 0, 0).rotateX(PApplet.TWO_PI * p5.noise(p5.frameCount / scale, p5.frameCount / scale)).rotateY(PApplet.TWO_PI * p5.noise(p.y / scale, p5.frameCount / scale - p.z / scale)).rotateZ(PApplet.TWO_PI * p5.noise((p5.height / 2 - p.y) / scale, p5.frameCount / scale -
		// (p5.width / 2 - p.x) / scale));

		// Vec3D f = new Vec3D(1 - 2 * p5.noise(p5.frameCount/100f,0, p.getPreviousPosition().magnitude()/100f), 1 - 2 * p5.noise(p5.frameCount/200f,p.headingXZ()/10f, -p.getPreviousPosition().magnitude()/100f), 1 - 2 * p5.noise(-p5.frameCount/150f,p.headingXY()/10f,
		// -p.getPreviousPosition().magnitude()/100f));

		// Vec3D f = new Vec3D(0, 1, 0).rotateX(PApplet.TAU * (0.5f - p5.noise(p5.frameCount / scale + p.x / scale, p.y / scale, p.z / scale))).rotateZ(PApplet.TAU * p5.noise(p.z / scale, p.x / scale, p.y / scale));
		Vec3D f = new Vec3D(1 - 2 * p5.noise(p5.frameCount / scale + p.x / scale, p.y / scale, age / scale), 1 - 2 * p5.noise(p.z / scale, p.x / scale, age / scale), 1 - 2 * p5.noise(p.y / scale, p.z / scale, age / scale));
		// Vec3D f = new Vec3D((float) SimplexNoise.noise(p5.frameCount / scale + p.x / scale, p.y / scale, 0), (float) SimplexNoise.noise(p.z / scale, p.x / scale, 0), (float) SimplexNoise.noise(p.y / scale, p.z / scale, 0));
		// Vec3D f = new Vec3D((float) SimplexNoise.noise(p5.frameCount / scale + p.x / scale, p.y / scale, 0), (float) SimplexNoise.noise(p.z / scale, p5.frameCount/scale +p.x / scale, p5.frameCount/scale), (float) SimplexNoise.noise(p.y / scale, p.z / scale, 0));

		f.normalize();
		f = f.jitter(jitter);
		f.scaleSelf(attrStrength);
		p.addForce(f);
	}

	public void configure(float timeStep) {
		this.timeStep = timeStep;
		setStrength(strength);
	}

	/**
	 * @return the attractor
	 */
	public Vec3D getAttractor() {
		return attractor;
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
	 * @param attractor
	 *            the attractor to set
	 */
	public void setAttractor(Vec3D attractor) {
		this.attractor = attractor;
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

}
