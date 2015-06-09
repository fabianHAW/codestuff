package haw.aip3.haw.entities.produkt;

import haw.aip3.haw.exception.SizeLessThanOneException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.CollectionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;



@Entity
public class Arbeitsplan {

	public Arbeitsplan(){
		
	}
	
//	public Arbeitsplan(Bauteil b, ArrayList<Vorgang> vorgaenge) {
//		// TODO Auto-generated constructor stub
//		if(vorgaenge.size() == 0 || vorgaenge == null){
//			try {
//				throw new SizeLessThanOneException();
//			} catch (SizeLessThanOneException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		bauteil = b;
//		besteht_aus = vorgaenge;
//	}
	
	public Arbeitsplan(ArrayList<Vorgang> vorgaenge) {
		// TODO Auto-generated constructor stub
		if(vorgaenge.size() == 0 || vorgaenge == null){
			try {
				throw new SizeLessThanOneException();
			} catch (SizeLessThanOneException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		besteht_aus = vorgaenge;
	}
	
	@Id
	@GeneratedValue
	private Long nr;

//	@OneToOne(fetch=FetchType.EAGER)
//	private Bauteil bauteil;

	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.REFRESH)
	@ElementCollection(targetClass=Vorgang.class)
	List<Vorgang> besteht_aus;

	public Long getNr() {
		return nr;
	}

	public void setNr(Long nr) {
		this.nr = nr;
	}

//	public Bauteil getBauteil() {
//		return bauteil;
//	}
//
//	public void setBauteil(Bauteil bauteil) {
//		this.bauteil = bauteil;
//	}

	public List<Vorgang> getBesteht_aus() {
		return besteht_aus;
	}

	public void setBesteht_aus(List<Vorgang> besteht_aus) {
		this.besteht_aus = besteht_aus;
	}

//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + ((bauteil == null) ? 0 : bauteil.hashCode());
//		result = prime * result
//				+ ((besteht_aus == null) ? 0 : besteht_aus.hashCode());
//		result = prime * result + ((nr == null) ? 0 : nr.hashCode());
//		return result;
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		Arbeitsplan other = (Arbeitsplan) obj;
//		if (bauteil == null) {
//			if (other.bauteil != null)
//				return false;
//		} else if (!bauteil.equals(other.bauteil))
//			return false;
//		if (besteht_aus == null) {
//			if (other.besteht_aus != null)
//				return false;
//		} else if(besteht_aus.size() != other.besteht_aus.size()){
//			return false;
//		}else if (checkListNonEquality(besteht_aus, other.besteht_aus)){
//			return false;
//		}
//		if (nr == null) {
//			if (other.nr != null)
//				return false;
//		} else if (!nr.equals(other.nr))
//			return false;
//		return true;
//	}

	
	private boolean checkListNonEquality(List<Vorgang> a, List<Vorgang> b){
		Iterator<Vorgang> iter1 = a.iterator();
		Iterator<Vorgang> iter2 = b.iterator();
		while(iter1.hasNext() && iter2.hasNext()){
			if(!iter1.next().equals(iter2.next())){
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
		} else if(besteht_aus.size() != other.besteht_aus.size()){
			return false;
		}else if (checkListNonEquality(besteht_aus, other.besteht_aus)){
			return false;
		}
		if (nr == null) {
			if (other.nr != null)
				return false;
		} else if (!nr.equals(other.nr))
			return false;
		return true;
	}

}
