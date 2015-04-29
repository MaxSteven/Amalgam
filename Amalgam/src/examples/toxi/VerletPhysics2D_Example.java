package examples.toxi;

import peasy.PeasyCam;
import processing.core.PApplet;
import toxi.color.ColorList;
import toxi.geom.Vec2D;
import toxi.physics2d.VerletParticle2D;
import amalgam.colour.QuickColours;
import amalgam.toxi.physics2D.MyVerletPhysics2D;
import amalgam.toxi.physics2D.NoiseBehavior2D;
import amalgam.toxi.physics2D.RenderTriangles;
import amalgam.toxi.physics2D.RestoringBehavior2D;
import controlP5.ControlP5;

@SuppressWarnings("serial")
public class VerletPhysics2D_Example extends PApplet {
	PeasyCam cam;
	ControlP5 cp5;

	MyVerletPhysics2D physics;
	ColorList clrs;

	public void setup() {
		size(1280, 720, P2D);
		cam = new PeasyCam(this, 500);
		smooth(8);
		background(0);

		clrs = QuickColours.get(500);

		physics = new MyVerletPhysics2D(this, new Vec2D(), 10, 0.1f, 1);
		for (int i = 0; i < 1000; i++) {
			Vec2D v = Vec2D.randomVector().scaleSelf(400).addSelf(new Vec2D(width / 2f, height / 2f));
			VerletParticle2D p = new VerletParticle2D(v);
			physics.addParticle(p);
		}

		physics.addBehavior(new RestoringBehavior2D(1f));
		physics.addBehavior(new NoiseBehavior2D(this, 1f, 100));

		physics.setRenderingStrategy(new RenderTriangles(this, physics, clrs, 200));
	}

	public void draw() {

		physics.update();
		physics.render();
	}
}