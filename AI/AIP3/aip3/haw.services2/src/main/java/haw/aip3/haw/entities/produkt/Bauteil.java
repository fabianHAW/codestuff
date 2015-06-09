package haw.aip3.haw.entities.produkt;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public abstract class Bauteil {

	public Bauteil() {
	}

	public Bauteil(String name) {
		this.name = name;
	}

	@Id
	@GeneratedValue
	private long bauteilNr;

	@Column
	private String name;

	public abstract boolean hasStueckliste();

	public long getBauteilNr() {
		return bauteilNr;
	}

	public void setBauteilNr(long bauteilNr) {
		this.bauteilNr = bauteilNr;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (bauteilNr ^ (bauteilNr >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Bauteil other = (Bauteil) obj;
		if (bauteilNr != other.bauteilNr) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}

}
