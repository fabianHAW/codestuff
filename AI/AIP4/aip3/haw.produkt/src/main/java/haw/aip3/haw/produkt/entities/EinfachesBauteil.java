package haw.aip3.haw.produkt.entities;

import javax.persistence.Entity;

@Entity
public class EinfachesBauteil extends Bauteil {

	public EinfachesBauteil() {
		super();
	}

	public EinfachesBauteil(String name) {
		super(name);
	}

	@Override
	public boolean hasStueckliste() {
		return false;
	}

}
