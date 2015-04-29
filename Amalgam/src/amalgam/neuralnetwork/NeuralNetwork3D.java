package amalgam.neuralnetwork;

import java.util.ArrayList;

import processing.core.PApplet;
import toxi.color.ColorList;
import toxi.color.TColor;
import amalgam.utils.ARnd;

public class NeuralNetwork3D {
	private final PApplet p5;

	private ArrayList<Neuron3D> neurons;
	private ArrayList<Arc3D> arcs;
	private ArrayList<Signal3D> signals; // This will be handy?

	private float alpha;
	private ColorList clrs;

	public NeuralNetwork3D(PApplet p5) {
		this.p5 = p5;
		neurons = new ArrayList<>();
		arcs = new ArrayList<>();
		signals = new ArrayList<>();

		this.clrs = new ColorList();
		clrs.add(TColor.WHITE.copy());
		this.alpha = 255;
	}

	public NeuralNetwork3D(PApplet p5, ColorList clrs, float alpha) {
		this.p5 = p5;
		neurons = new ArrayList<>();
		arcs = new ArrayList<>();
		signals = new ArrayList<>();

		this.clrs = clrs;
		this.alpha = alpha;
	}

	public void addNeuron(Neuron3D n) {
		n.setAlpha(alpha);
		n.setColour(ARnd.get(clrs));
		neurons.add(n);
	}

	// Connect 2 Neurons
	public void connect(Neuron3D a, Neuron3D b, float weight) {
		Arc3D c = new Arc3D(p5, a, b, weight);
		c.setAlpha(alpha);
		c.setColour(ARnd.get(clrs));
		a.addArc(c);
		arcs.add(c);
	}

	// Start the firing process
	public void feedforward(float input1) {
		Neuron3D n = ARnd.get(neurons);
		n.feedforward(input1);
	}

	public void update() {
		signals.clear();
		for (Arc3D c : arcs) {
			c.update();
			signals.addAll(c.getSignals());
		}
	}

	public void render() {
		for (Arc3D c : arcs) {
			c.render();
		}
		for (Neuron3D n : neurons) {
			n.render();
		}
	}

	public void resetFiring() {
		for (Neuron3D n : neurons) {
			n.reset();
		}
	}

	public void clear() {
		neurons.clear();
		arcs.clear();
	}

	// Getters/Setters/Utilities

	public int countNeurons() {
		return neurons.size();
	}

	public int countArcs() {
		return arcs.size();
	}

	public ArrayList<Neuron3D> getNeurons() {
		return neurons;
	}

	public ArrayList<Arc3D> getArcs() {
		return arcs;
	}

	public ArrayList<Signal3D> getSignals() {
		return signals;
	}

	public void setClrs(ColorList clrs) {
		this.clrs = clrs;
	}

	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}

}