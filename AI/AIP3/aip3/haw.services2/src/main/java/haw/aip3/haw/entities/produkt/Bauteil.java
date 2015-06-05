package haw.aip3.haw.entities.produkt;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public abstract class Bauteil {

	public Bauteil(){}
	
	public Bauteil(String name){
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
		if (obj == null)
		{
			System.out.println("1d");
				return false;
			}
		if (getClass() != obj.getClass())
		{
			System.out.println("2d");
				return false;
			}
		Bauteil other = (Bauteil) obj;
		if (bauteilNr != other.bauteilNr)
		{
			System.out.println("3d");
				return false;
			}
		if (name == null) {
			if (other.name != null)
			{
				System.out.println("4d");
					return false;
				}
		} else if (!name.equals(other.name))
		{
			System.out.println("5d");
				return false;
			}
		return true;
	}
	
	
}
