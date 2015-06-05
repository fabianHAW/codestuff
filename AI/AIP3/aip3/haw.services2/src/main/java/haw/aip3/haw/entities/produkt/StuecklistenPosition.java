package haw.aip3.haw.entities.produkt;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Type;

@Entity
public class StuecklistenPosition {

	public StuecklistenPosition() {
	}

	public StuecklistenPosition(String name, int menge, Bauteil b) {
		this.name = name;
		this.menge = menge;
		this.bauteil = b;
	}

	@Id
	@GeneratedValue
	private long positionNr;

	@OneToOne(targetEntity=Bauteil.class, cascade=CascadeType.ALL)
	Bauteil bauteil;
	
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


	public Bauteil getBauteil() {
		return bauteil;
	}

	public void setBauteil(Bauteil bauteil) {
		this.bauteil = bauteil;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bauteil == null) ? 0 : bauteil.hashCode());
		result = prime * result + menge;
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
		if (bauteil == null) {
			if (other.bauteil != null)
				return false;
		} else if (!bauteil.equals(other.bauteil))
			return false;
		if (menge != other.menge)
			return false;
		if (positionNr != other.positionNr)
			return false;
		return true;
	}

}
