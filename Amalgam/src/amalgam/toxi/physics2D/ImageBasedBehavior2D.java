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
import processing.core.PImage;
import toxi.color.TColor;
import toxi.geom.SpatialIndex;
import toxi.geom.Vec2D;
import toxi.physics2d.VerletParticle2D;
import toxi.physics2d.behaviors.ParticleBehavior2D;

public class ImageBasedBehavior2D implements ParticleBehavior2D {
	private final PApplet p5;
	private final PImage image;

	protected float radius, radiusSquared;
	protected float strength;
	protected float jitter;
	protected float timeStep;

	private int life;
	private int maxLife;

	public ImageBasedBehavior2D(PApplet p5, PImage image, float strength) {
		this(p5, image, strength, 0);
	}

	public ImageBasedBehavior2D(PApplet p5, PImage image, float strength, float jitter) {
		this.p5 = p5;
		this.image = image;
		this.strength = strength;
		this.jitter = jitter;
		setRadius(radius);

		life = 0;
		this.maxLife = (int) Float.POSITIVE_INFINITY;

		// do it here?
		image.loadPixels();
	}

	// ONE ------------
	// public void apply(VerletParticle2D p) {
	// int clr = image.get((int) p.x, (int) p.y);
	// TColor colour = TColor.newARGB(clr);
	// float mag = colour.brightness();
	//
	// Vec2D f = new Vec2D(0, 1).rotate(PApplet.PI * mag);
	// f.scaleSelf(mag);
	// p.addForce(f.scaleSelf(strength));
	// }

	// TWO ------------ Crap
	// public void apply(VerletParticle2D p) {
	// int clr = image.get((int) p.x, (int) p.y);
	// TColor colour = TColor.newARGB(clr);
	// float mag = colour.brightness();
	//
	// Vec2D v = p.getVelocity();
	// v.rotate((mag) * PApplet.PI);
	// p.addForce(v.normalize());
	// }

	// REFRACTION ------------
	public void apply(VerletParticle2D p) {
		// Currently shit

		int clr = image.get((int) p.x, (int) p.y);
		TColor colour = TColor.newARGB(clr);
		float mag = colour.brightness();

		Vec2D prev = p.getPreviousPosition();
		int clrPrev = image.get((int) prev.x, (int) prev.y);
		TColor colourPrev = TColor.newARGB(clrPrev);
		float magPrev = colourPrev.brightness();

		if (PApplet.abs(mag - magPrev) > 0.2f) {
			Vec2D v = p.getVelocity();
			v.normalize();
			float theta = v.heading();
			theta += PApplet.PI / 2;
			float sinPhi = (magPrev / mag) * PApplet.sin(theta);
			float phi = PApplet.asin(sinPhi);
			Vec2D newVec = new Vec2D(0, 1).rotate(phi);
			p.addForce(newVec.scaleSelf(strength));
			// p.addVelocity(newVec);
		}
	}

	public void applyWithIndex(SpatialIndex<Vec2D> spaceHash) {
		throw new UnsupportedOperationException("applyWithIndex does not apply to this behavoir");
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
	}

	public boolean supportsSpatialIndex() {
		return true;
	}

	@Override
	public void update() {
	}

	@Override
	public int getAge() {
		return life;
	}

	@Override
	public int getLife() {
		return maxLife;
	}

}
