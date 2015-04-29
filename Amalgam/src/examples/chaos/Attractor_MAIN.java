package examples.chaos;

import peasy.PeasyCam;
import processing.core.PApplet;
import toxi.color.ColorList;
import toxi.geom.Vec3D;
import amalgam.chaos.Attractor;
import amalgam.chaos.SprottLinzG_Attractor;
import amalgam.colour.QuickColours;
import amalgam.utils.AIO;
import amalgam.utils.AUtils;
import controlP5.ControlP5;

/**
 * Main App for rendering chaotic systems
 */
@SuppressWarnings("serial")
public class Attractor_MAIN extends PApplet {
	Attractor att;

	ColorList colours;
	PeasyCam cam;
	ControlP5 cp5;

	boolean savePDF = false;

	public void setup() {
		size(1280, 720, P3D);
		smooth(8);

		cam = new PeasyCam(this, 50);
		colours = QuickColours.get(500);

		cp5 = new ControlP5(this);
		cp5.setAutoDraw(false);
		cp5.addFrameRate();

		// att = new SymmetricFlowAttractor(this, new Vec3D(1, 1, 0), 50);
		// ArrayList<Float> params = new ArrayList<>();
		// params.add((float) 1.522f);
		// att.setParams(params);

		// att = new ChenAttractor(this, new Vec3D(-0.1f, 0.5f, -0.6f), 50);
		// att = new ThomasAttractor(this, new Vec3D(0.1f, 0.1f, 0), 50);
		// att = new RosslerAttractor(this, new Vec3D(0.1f, 0.1f, 0), 50);
		// att = new LorenzAttractor(this, new Vec3D(0.1f, 0.1f, 0), 50);
		// att = new ModifiedLorenzAttractor(this, new Vec3D(0, 1, 0), 50);
		// att = new ModifiedLorenz2_Attractor(this, new Vec3D(5, 5, 5), 50);
		// att = new SprottLinzL_Attractor(this, new Vec3D(1, 1, 0), 50);
		att = new SprottLinzG_Attractor(this);
		// att = new NewChaosAttractor(this);
		// att = new HadleyAttractor(this);
		// att = new DuffingAttractor(this);
		// att = new NoseHooverAttractor(this, new Vec3D(1, 0, 0), 50);
		// att = new AizawaAttractor(this, new Vec3D(0.1f, 0, 0), 50);
		// att = new HalvorsenAttractor(this, new Vec3D(-5, 0, 0), 50);
	}

	public void draw() {
		background(0);

		// lights();
		// pointLight(200, 155, 180, 0, 0, 0);
		// directionalLight(155, 200, 200, 1, 1, 1);
		// directionalLight(155, 33, 200, 1, -1, 0);
		// pointLight(200, 100, 122, -150, -100, 150);
		// spotLight(255, 255, 155, 80, 120, 40, -1, -1, 0, PI / 2, 2);

		if (savePDF) {
			AIO.startRawExport(this);
		}

		for (int i = 0; i < 70; i++) {
			att.update(0.003);
		}
		att.draw();
		// att.draw(colours);
		// att.drawQuads(colours, 1.05f);
		// att.drawQuads(colours, 1.10f);

		if (savePDF) {
			AIO.endRawExport(this);
			savePDF = false;
		}
		// IO.saveFrmTGASequence(this, "vid3");

		AUtils.gui(this, cam, cp5);
	}

	public void keyPressed() {
		if (key == 's') {
			savePDF = true;
		}
		if (key == 't') {
			AIO.saveFrameTGA(this);
		}
		if (key == ' ') {
			setup();
		}
	}
	
	public void mouseClicked(){
		att.setPosition(new Vec3D(random(1),random(1),random(1)));
	}
}