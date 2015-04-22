package amalgam.audio;


/**
 * Convenience class for passing minim data each frame
 */
public class SoundData {
	public float energyScaling;
	public float[] energies;
	public float totalEnergy;
	public boolean kick, hat, snare;

	public SoundData() {
		totalEnergy = 0;
		energyScaling = 1;
		energies = new float[]{1};
	}

	public void update(float energyScaling, float energies[], float tot, boolean kick, boolean hat, boolean snare) {
		this.energyScaling = energyScaling;
		this.energies = energies;
		this.totalEnergy = tot;
		this.kick = kick;
		this.hat = hat;
		this.snare = snare;
	}
}
