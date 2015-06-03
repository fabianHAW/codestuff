package haw.aip3.haw.entities.produkt;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class StuecklistenPosition {

	public StuecklistenPosition() {
	}

	public StuecklistenPosition(String name, int menge) {
		this.name = name;
		this.menge = menge;
	}

	@Id
	@GeneratedValue
	private long positionNr;

	@Column
	private String name;

	@Column
	private int menge;

	public long getPositionNr() {
		return positionNr;
	}

	public void setPositionNr(long positionNr) {
		this.positionNr = positionNr;
	}

	public String getPositionName() {
		return name;
	}

	public void setPositionName(String positionName) {
		this.name = positionName;
	}

	public int getMenge() {
		return menge;
	}

	public void setMenge(int menge) {
		this.menge = menge;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + menge;
		result = prime * result
				+ ((name == null) ? 0 : name.hashCode());
		result = prime * result + (int) (positionNr ^ (positionNr >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StuecklistenPosition other = (StuecklistenPosition) obj;
		if (menge != other.menge)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (positionNr != other.positionNr)
			return false;
		return true;
	}

}
