package haw.aip3.haw.entities.produkt;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Stueckliste {

	public Stueckliste() {
	}

	public Stueckliste(String name, Date gueltigAb, Date gueltigBis,
			Set<StuecklistenPosition> position) {
		this.name = name;
		this.gueltigAb = gueltigAb;
		this.gueltigBis = gueltigBis;
		this.position = position;
	}

	@Id
	@GeneratedValue
	private long stuecklisteNr;

	@Column
	private String name;

	@Column
	private Date gueltigAb;

	@Column
	private Date gueltigBis;

	@OneToMany(fetch = FetchType.EAGER)
	//@ElementCollection(targetClass = StuecklistenPosition.class)
	private Set<StuecklistenPosition> position;

	public long getStuecklisteNr() {
		return stuecklisteNr;
	}

	public void setStuecklisteNr(long stuecklisteNr) {
		this.stuecklisteNr = stuecklisteNr;
	}

	public String getStuecklisteName() {
		return name;
	}

	public void setStuecklisteName(String stuecklisteName) {
		this.name = stuecklisteName;
	}

	public Date getGueltigAb() {
		return gueltigAb;
	}

	public void setGueltigAb(Date gueltigAb) {
		this.gueltigAb = gueltigAb;
	}

	public Date getGueltigBis() {
		return gueltigBis;
	}

	public void setGueltigBis(Date gueltigBis) {
		this.gueltigBis = gueltigBis;
	}

	public Set<StuecklistenPosition> getPosition() {
		return position;
	}

	public void setPosition(Set<StuecklistenPosition> position) {
		this.position = position;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((gueltigAb == null) ? 0 : gueltigAb.hashCode());
		result = prime * result
				+ ((gueltigBis == null) ? 0 : gueltigBis.hashCode());
		result = prime * result
				+ ((position == null) ? 0 : position.hashCode());
		result = prime * result
				+ ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ (int) (stuecklisteNr ^ (stuecklisteNr >>> 32));
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
		Stueckliste other = (Stueckliste) obj;
		if (gueltigAb == null) {
			if (other.gueltigAb != null)
				return false;
		} else if (!gueltigAb.equals(other.gueltigAb))
			return false;
		if (gueltigBis == null) {
			if (other.gueltigBis != null)
				return false;
		} else if (!gueltigBis.equals(other.gueltigBis))
			return false;
		if (position == null) {
			if (other.position != null)
				return false;
		} else if (!position.equals(other.position))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (stuecklisteNr != other.stuecklisteNr)
			return false;
		return true;
	}

}
