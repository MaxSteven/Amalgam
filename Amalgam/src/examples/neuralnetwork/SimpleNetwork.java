package examples.neuralnetwork;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import processing.core.PApplet;
import toxi.color.ColorList;
import toxi.color.TColor;
import toxi.geom.Rect;
import toxi.geom.Vec2D;
import amalgam.colour.WeightedGradient;
import amalgam.lattice.IsoLattice;
import amalgam.lattice.Tesselation2D;
import amalgam.neuralnetwork.NeuralNetwork2D;
import amalgam.neuralnetwork.Neuron2D;
import controlP5.ControlP5;

@SuppressWarnings("serial")
public class SimpleNetwork extends PApplet {
	ControlP5 cp5;

	IsoLattice iso;
	Tesselation2D tess;
	float div = 100;

	// Multiple networks
	NeuralNetwork2D network;
	Rect bounds;

	ColorList clrs;

	public void setup() {
		size(500, 500, P3D); // P3D needed for strokeWeight > 2
		background(0);
		smooth(8);
		cp5 = new ControlP5(this);
		cp5.addFrameRate();

		clrs = new ColorList();
		clrs.add(TColor.newHex("000000"));
		clrs.add(TColor.newHex("79bb22"));
		clrs.add(TColor.newHex("ffffff"));
		WeightedGradient grad = new WeightedGradient(clrs);
		clrs = grad.getGradient(50);

		network = new NeuralNetwork2D(this);

		// =========================================================================
		// Subdivided setup
		// =========================================================================
		IsoLattice iso = new IsoLattice(new Vec2D(width / 2f, height / 2f), width * 1.2f, height * 1.2f, div, true);
		tess = iso.toTesselation();

		ArrayList<Vec2D> pts = tess.calculatePoints();
		for (Vec2D p : pts) {
			network.addNeuron(new Neuron2D(this, p));
		}

		// Get dist to all other neurons. Sort, pick out first-maxConnects. connect
		int maxConnects = 6;
		for (int i = 0; i < network.countNeurons(); i++) {

			ArrayList<Vec2D> sortedPos = new ArrayList<Vec2D>();
			// Get positions and add to list for sorting
			Neuron2D n = network.getNeurons().get(i);
			for (int j = 0; j < network.countNeurons(); j++) {
				if (i != j) {
					Neuron2D m = network.getNeurons().get(j);
					float d = n.pos.distanceTo(m.pos);
					sortedPos.add(new Vec2D(j, d));
				}
			}

			// Sort according to d
			Collections.sort(sortedPos, new Comparator<Vec2D>() {
				@Override
				public int compare(Vec2D o1, Vec2D o2) {
					return (int) (o1.y - o2.y);
				}
			});

			// Connect to the shortest N connections (N = maxConnects)
			for (int j = 0; j < maxConnects; j++) {
				Vec2D indexDist = sortedPos.get(j);
				Neuron2D m = network.getNeurons().get((int) indexDist.x);
				network.connect(n, m, 1);
				// back connection
				network.connect(m, n, 1);
			}
		}
		// =========================================================================
		// =========================================================================
	}

	public void draw() {
		background(0);
		tess.drawAllDetails(this, clrs, true);

		network.update();
		network.render();
	}

	public void mouseClicked() {
		network.feedforward(5f);
		network.resetFiring();
	}
}