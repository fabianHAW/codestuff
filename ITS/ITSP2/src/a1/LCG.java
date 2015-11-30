package a1;

import util.ModulGenerator;

public class LCG {

	private long modulus;
	private long multiplier;
	private long increment;
	private long x0;

	public LCG(long x0) {
		this.modulus = (long) Math.pow(2d, 24d);
		this.multiplier = 16598013L;
		this.increment = 12820163L;
		this.x0 = x0;
		ModulGenerator.pruefeModul(this.modulus, this.multiplier, this.increment);
//		System.out.println(
//				"Modulus was checked: " + ModulGenerator.pruefeModul(this.modulus, this.multiplier, this.increment));
	}

	/**
	 * Generate a random value.
	 * 
	 * @return Random value
	 */
	public long nextValue() {
		this.x0 = ((this.multiplier * this.x0) + this.increment) % this.modulus;
		return this.x0;
	}

}
