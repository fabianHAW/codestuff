package haw.aip3.haw.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Stueckliste {

	public Stueckliste() {
	}

	public Stueckliste(String name, Date gueltigAb, Date gueltigBis, int menge) {
		this.stuecklisteName = name;
		this.gueltigAb = gueltigAb;
		this.gueltigBis = gueltigBis;
		this.menge = menge;
	}

	@Id
	@GeneratedValue
	private long stuecklisteNr;

	@Column
	private String stuecklisteName;

	@Column
	private Date gueltigAb;

	@Column
	private Date gueltigBis;

	@Column
	private int menge;

	public long getStuecklisteNr() {
		return stuecklisteNr;
	}

	public void setStuecklisteNr(long stuecklisteNr) {
		this.stuecklisteNr = stuecklisteNr;
	}

	public String getStuecklisteName() {
		return stuecklisteName;
	}

	public void setStuecklisteName(String stuecklisteName) {
		this.stuecklisteName = stuecklisteName;
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
		result = prime * result
				+ ((gueltigAb == null) ? 0 : gueltigAb.hashCode());
		result = prime * result
				+ ((gueltigBis == null) ? 0 : gueltigBis.hashCode());
		result = prime * result + menge;
		result = prime * result
				+ ((stuecklisteName == null) ? 0 : stuecklisteName.hashCode());
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
		if (menge != other.menge)
			return false;
		if (stuecklisteName == null) {
			if (other.stuecklisteName != null)
				return false;
		} else if (!stuecklisteName.equals(other.stuecklisteName))
			return false;
		if (stuecklisteNr != other.stuecklisteNr)
			return false;
		return true;
	}

}
