package examples.toxi;

import peasy.PeasyCam;
import processing.core.PApplet;
import toxi.color.ColorList;
import toxi.geom.Vec3D;
import toxi.physics3d.VerletParticle3D;
import toxi.physics3d.behaviors.RestoringBehavior3D;
import amalgam.colour.QuickColours;
import amalgam.toxi.physics3D.MyVerletPhysics3D;
import amalgam.toxi.physics3D.NoiseBehaviour3D;
import amalgam.toxi.physics3D.RenderLines3D;
import controlP5.ControlP5;

@SuppressWarnings("serial")
public class VerletPhysics3D_Example extends PApplet {
	PeasyCam cam;
	ControlP5 cp5;

	MyVerletPhysics3D physics;
	ColorList clrs;

	public void setup() {
		size(1280, 720, P3D);
		cam = new PeasyCam(this, 500);
		smooth(8);

		clrs = QuickColours.get(500);

		physics = new MyVerletPhysics3D(this, new Vec3D(), 10, 0.1f, 1);
		for (int i = 0; i < 1000; i++) {
			Vec3D v = Vec3D.randomVector().scaleSelf(400);
			VerletParticle3D p = new VerletParticle3D(v);
			p.track(50);
			physics.addParticle(p);
		}

		physics.addBehavior(new RestoringBehavior3D(1f));
		physics.addBehavior(new NoiseBehaviour3D(this, 1f, 100));

		physics.setRenderingStrategy(new RenderLines3D(this, physics, clrs));
	}

	public void draw() {
		background(0);

		physics.update();
		physics.render();
	}
}