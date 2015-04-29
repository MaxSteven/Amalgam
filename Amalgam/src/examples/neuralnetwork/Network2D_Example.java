package examples.neuralnetwork;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import processing.core.PApplet;
import toxi.color.ColorList;
import toxi.geom.Vec2D;
import amalgam.colour.QuickColours;
import amalgam.lattice.IsoLattice;
import amalgam.lattice.Tesselation2D;
import amalgam.neuralnetwork.NeuralNetwork2D;
import amalgam.neuralnetwork.Neuron2D;

@SuppressWarnings("serial")
public class Network2D_Example extends PApplet {
	IsoLattice iso;
	Tesselation2D tess;
	float div = 100;

	NeuralNetwork2D network;
	ColorList clrs;

	public void setup() {
		size(1000, 500, P3D);
		background(0);
		smooth(8);

		clrs = QuickColours.get(500);
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

		if (frameCount == 1) {
			network.feedforward(5f);
		}

		tess.drawAllDetails(this, clrs, true);
		network.update();
		network.render();
	}

	public void mouseClicked() {
		network.feedforward(5f);
		network.resetFiring();
	}
}