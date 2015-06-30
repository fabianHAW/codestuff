package haw.aip3.haw.produkt.entities;

import haw.aip3.haw.base.entities.IArbeitsplan;
import haw.aip3.haw.base.entities.IVorgang;
import haw.aip3.haw.produkt.entities.exceptions.SizeLessThanOneException;

import java.util.Iterator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Arbeitsplan implements IArbeitsplan{

	public Arbeitsplan() {

	}

	public Arbeitsplan(List<IVorgang> vorgaenge) {
		if (vorgaenge.size() == 0 || vorgaenge == null) {
			try {
				throw new SizeLessThanOneException();
			} catch (SizeLessThanOneException e) {
				e.printStackTrace();
			}
		}
		besteht_aus = vorgaenge;
	}

	@Id
	@GeneratedValue
	private Long nr;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH, targetEntity = Vorgang.class)
	List<IVorgang> besteht_aus;

	public Long getNr() {
		return nr;
	}

	public void setNr(Long nr) {
		this.nr = nr;
	}

	public List<IVorgang> getBesteht_aus() {
		return besteht_aus;
	}

	public void setBesteht_aus(List<IVorgang> besteht_aus) {
		this.besteht_aus = besteht_aus;
	}

	private boolean checkListNonEquality(List<IVorgang> a, List<IVorgang> b) {
		Iterator<IVorgang> iter1 = a.iterator();
		Iterator<IVorgang> iter2 = b.iterator();
		while (iter1.hasNext() && iter2.hasNext()) {
			if (!iter1.next().equals(iter2.next())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((besteht_aus == null) ? 0 : besteht_aus.hashCode());
		result = prime * result + ((nr == null) ? 0 : nr.hashCode());
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
		Arbeitsplan other = (Arbeitsplan) obj;
		if (besteht_aus == null) {
			if (other.besteht_aus != null)
				return false;
		} else if (besteht_aus.size() != other.besteht_aus.size()) {
			return false;
		} else if (checkListNonEquality(besteht_aus, other.besteht_aus)) {
			return false;
		}
		if (nr == null) {
			if (other.nr != null)
				return false;
		} else if (!nr.equals(other.nr))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Arbeitsplan [nr=" + nr + ", besteht_aus=" + besteht_aus + "]";
	}

	
}
