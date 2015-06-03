package haw.aip3.haw.entities.produkt;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Vorgang {

	public Vorgang() {
		// TODO Auto-generated constructor stub
	}
	
	public Vorgang(VorgangArtTyp typ, long ruestzeit, long maschinenzeit, long personenzeit){
		this.art = typ;
		this.personenzeit = personenzeit;
		this.maschinenzeit = maschinenzeit;
		this.personenzeit = personenzeit;
	}
	
	@Id
	@GeneratedValue
	@Column
	private Long nr;
	
	@Column(nullable = false)
	private VorgangArtTyp art;
	
	@Column(nullable = false)
	private long ruestzeit;
	
	@Column(nullable = false)
	private long maschinenzeit;
	
	@Column(nullable = false)
	private long personenzeit;
	
	public enum VorgangArtTyp {
		BEREITSTELLUNG, MONTAGE;
	}

	public Long getNr() {
		return nr;
	}

	public void setNr(Long nr) {
		this.nr = nr;
	}

	public VorgangArtTyp getArt() {
		return art;
	}

	public void setArt(VorgangArtTyp art) {
		this.art = art;
	}

	public long getRuestzeit() {
		return ruestzeit;
	}

	public void setRuestzeit(long ruestzeit) {
		this.ruestzeit = ruestzeit;
	}

	public long getMaschinenzeit() {
		return maschinenzeit;
	}

	public void setMaschinenzeit(long maschinenzeit) {
		this.maschinenzeit = maschinenzeit;
	}

	public long getPersonenzeit() {
		return personenzeit;
	}

	public void setPersonenzeit(long personenzeit) {
		this.personenzeit = personenzeit;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((art == null) ? 0 : art.hashCode());
		result = prime * result
				+ (int) (maschinenzeit ^ (maschinenzeit >>> 32));
		result = prime * result + ((nr == null) ? 0 : nr.hashCode());
		result = prime * result + (int) (personenzeit ^ (personenzeit >>> 32));
		result = prime * result + (int) (ruestzeit ^ (ruestzeit >>> 32));
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
		Vorgang other = (Vorgang) obj;
		if (art != other.art)
			return false;
		if (maschinenzeit != other.maschinenzeit)
			return false;
		if (nr == null) {
			if (other.nr != null)
				return false;
		} else if (!nr.equals(other.nr))
			return false;
		if (personenzeit != other.personenzeit)
			return false;
		if (ruestzeit != other.ruestzeit)
			return false;
		return true;
	}

}
