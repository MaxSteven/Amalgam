package examples.neuralnetwork;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import peasy.PeasyCam;
import processing.core.PApplet;
import toxi.geom.Vec2D;
import toxi.geom.Vec3D;
import toxi.math.CircularInterpolation;
import amalgam.lattice.IsoLattice;
import amalgam.lattice.SubdivisionType;
import amalgam.lattice.Tesselation2D;
import amalgam.neuralnetwork.NeuralNetwork3D;
import amalgam.neuralnetwork.Neuron3D;

@SuppressWarnings("serial")
public class MAIN_NeuralNetwork3D extends PApplet {
	public static void main(String args[]) {
		PApplet.main(new String[]{"Network3D.MAIN_NeuralNetwork3D"});
	}

	PeasyCam cam;

	Tesselation2D tess;

	// Multiple networks
	NeuralNetwork3D network;

	// Controls
	float curvature = 500;
	int maxConnectsPerNeuron = 3;
	int maxShapes = 1500;

	public void setup() {
		size(1920, 1080, P3D);
		// size(displayWidth, displayHeight, P3D);

		smooth(4);
		frameRate(30);

		cam = new PeasyCam(this, 1000);
		cam.lookAt(width / 2, height / 2, 0);

		network = new NeuralNetwork3D(this);

		// =========================================================================
		// Subdivided setup
		// =========================================================================
		float div = 120;
		IsoLattice iso = new IsoLattice(new Vec2D(width / 2f, height / 2f), height * 2f, height * 2f, div, true);
		tess = iso.toTesselation();
		while (tess.getShapes().size() < maxShapes) {
			tess.subdivideRandom(0.05f, SubdivisionType.getRandom());
		}

		ArrayList<Vec2D> pts = tess.calculatePoints();
		for (Vec2D p : pts) {
			float diff = p.distanceTo(new Vec2D(width / 2f, height / 2f));
			float zScale = map(diff, 0, sqrt(width * width / 4 + height * height / 4), 1, 0);

			CircularInterpolation exp = new CircularInterpolation();
			// ExponentialInterpolation exp = new ExponentialInterpolation(0.5f);
			float h = exp.interpolate(0, curvature, zScale);

			Vec3D ppp = new Vec3D(p.x, p.y, h);
			network.addNeuron(new Neuron3D(this, ppp));
		}

		// Get dist to all other neurons. Sort, pick out first-maxConnects. connect
		for (int i = 0; i < network.countNeurons(); i++) {
			ArrayList<Vec2D> sortedPos = new ArrayList<Vec2D>();
			// Get positions and add to list for sorting
			Neuron3D n = network.getNeurons().get(i);
			for (int j = 0; j < network.countNeurons(); j++) {
				if (i != j) {
					Neuron3D m = network.getNeurons().get(j);
					float d = n.pos.distanceTo(m.pos);
					sortedPos.add(new Vec2D(j, d));
				}
			}

			// Sort according to d
			Collections.sort(sortedPos, new Comparator<Vec2D>() {
				@Override
				public int compare(Vec2D o1, Vec2D o2) {
					return Float.compare(o1.y, o2.y);
				}
			});

			// Connect to the shortest N connections (N = maxConnects)
			int connects = 0;
			for (int j = 0; j < sortedPos.size(); j++) {
				Vec2D indexDist = sortedPos.get(j);
				Neuron3D m = network.getNeurons().get((int) indexDist.x);

				// 60 DEG ONLY
				if (abs(n.pos.sub(m.pos).headingXY() % (2 * PI / 3)) < PI / 100 && connects < maxConnectsPerNeuron && n.pos.distanceTo(m.pos) < 2.5f * div) {
					network.connect(n, m, 1);
					network.connect(m, n, 1); // back connection
					connects++;
				}
			}
		}
	}

	public void draw() {
		background(0);
		network.update();
		network.render();
	}

	public void keyPressed() {
		switch (key) {
			case 'p' :
				network.resetFiring();
				network.feedforward(1.01f);
				break;
			case ' ' :
				setup();
				break;
			case '=' :
				curvature += 20;
				break;
			case '-' :
				curvature -= 20;
				break;
		}
	}

}