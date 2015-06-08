package haw.aip3.haw.entities.produkt;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
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

	@ManyToOne(targetEntity=Bauteil.class, cascade=CascadeType.ALL)
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
		System.out.println("0f");
		if (this == obj)
			return true;
		if (obj == null)
		{
			System.out.println("1f");
				return false;
			}
		if (getClass() != obj.getClass())
		{
			System.out.println("2f");
				return false;
			}
		StuecklistenPosition other = (StuecklistenPosition) obj;
		if (bauteil == null) {
			if (other.bauteil != null)
			{
				System.out.println("3f");
					return false;
				}
		} else if (!bauteil.equals(other.bauteil))
		{
			System.out.println("4f");
				return false;
			}
		if (menge != other.menge)
		{
			System.out.println("5f");
				return false;
			}
		if (positionNr != other.positionNr)
		{
			System.out.println("6f");
				return false;
			}
		System.out.println("7f");
		return true;
	}

}
