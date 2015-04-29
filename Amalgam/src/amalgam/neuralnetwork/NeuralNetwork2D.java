package amalgam.neuralnetwork;

import java.util.ArrayList;

import processing.core.PApplet;
import toxi.color.ColorList;

public class NeuralNetwork2D {
	private final PApplet p5;

	private ArrayList<Neuron2D> neurons;
	private ArrayList<Arc2D> arcs;
	private ArrayList<Signal2D> signals; // This will be handy?

	private float alpha; // base alpha for network
	private ColorList clrs; // base colours

	public NeuralNetwork2D(PApplet p5) {
		this.p5 = p5;
		neurons = new ArrayList<>();
		arcs = new ArrayList<>();
		signals = new ArrayList<>();
	}

	public NeuralNetwork2D(PApplet p5, ColorList clrs, float alpha) {
		this.p5 = p5;
		neurons = new ArrayList<>();
		arcs = new ArrayList<>();
		signals = new ArrayList<>();

		this.clrs = clrs;
		this.alpha = alpha;
	}

	public void addNeuron(Neuron2D n) {
		neurons.add(n);
	}

	// Connect two Neurons
	public void connect(Neuron2D a, Neuron2D b, float weight) {
		Arc2D c = new Arc2D(p5, a, b, weight);
		a.addArc(c);
		arcs.add(c);
	}

	// Send an input to a neuron towards the left. Should do something better to track multiple inputs
	public void feedforward(float input1) {
		for (Neuron2D n : neurons) {
			if (n.pos.x < 100) {
				n.feedforward(input1);
				break;
			}
		}
	}

	public void update() {
		signals.clear();
		for (Arc2D c : arcs) {
			c.update();
			signals.addAll(c.getSignals());
		}
	}

	public void render() {
		for (Arc2D c : arcs) {
			c.render();
		}
		for (Neuron2D n : neurons) {
			n.render();
		}
	}

	public void resetFiring() {
		for (Neuron2D n : neurons) {
			n.reset();
		}
	}	
	
	public void clear() {
		neurons.clear();
		arcs.clear();
	}

	public int countNeurons() {
		return neurons.size();
	}

	public int countArcs() {
		return arcs.size();
	}

	public ArrayList<Neuron2D> getNeurons() {
		return neurons;
	}

	public ArrayList<Arc2D> getArcs() {
		return arcs;
	}

	public ArrayList<Signal2D> getSignals() {
		return signals;
	}

}