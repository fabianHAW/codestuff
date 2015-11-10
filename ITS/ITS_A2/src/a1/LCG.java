package a1;

import util.ModulGenerator;

public class LCG {

	private long modul;
	private long faktor;
	private long inkrement;
	private long x0;

	public LCG(long x0) {
		this.modul = (long) Math.pow(2d, 24d);
		this.faktor = 16598013L;
		this.inkrement = 12820163L;
		this.x0 = x0;
		System.out.println(
				"Modul wurde geprueft: " + ModulGenerator.generateModul(this.modul, this.faktor, this.inkrement));
	}

	public long nextValue() {
		return ((this.faktor * this.x0) + this.inkrement) % this.modul;
	}

}
