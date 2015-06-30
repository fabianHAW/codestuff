package haw.aip3.haw.entities.produkt;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

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

	@ManyToMany(fetch = FetchType.EAGER, cascade=CascadeType.REFRESH)
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
		} else if (!compareSets(position, other.position)){
			System.out.println("jo");
			return false;
		}
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (stuecklisteNr != other.stuecklisteNr)
			return false;
		return true;
	}
	
	private boolean compareSets(Set<StuecklistenPosition> position1, Set<StuecklistenPosition> position2){
		if(position1.size() != position1.size()){
			return false;
		}
		
		Iterator<StuecklistenPosition> iter1 = position1.iterator();
		Iterator<StuecklistenPosition> iter2 = position2.iterator();
		int position1Elems = 0;
		int position2Elems = 0;
		
		while(iter1.hasNext()){
			StuecklistenPosition sp1 = iter1.next();
			position1Elems++;
			for(StuecklistenPosition sp : position2){
				if(sp.getPositionNr() == sp1.getPositionNr()){
					if(!sp.getBauteil().equals(sp1.getBauteil())){
						return false;
					}
					if(sp.getMenge() != sp1.getMenge()){
						return false;
					}
					if(!sp.getPositionName().equals(sp1.getPositionName())){
						return false;
					}
					position2Elems++;
				}
			}
		}
		
		if(position1Elems != position2Elems){
			return false;
		}
		
		position2Elems = 0;
		position1Elems = 0;
		
		while(iter2.hasNext()){
			StuecklistenPosition sp2 = iter2.next();
			position2Elems++;
			for(StuecklistenPosition sp : position1){
				if(sp.getPositionNr() == sp2.getPositionNr()){
					if(!sp.getBauteil().equals(sp2.getBauteil())){
						return false;
					}
					if(sp.getMenge() != sp2.getMenge()){
						return false;
					}
					if(!sp.getPositionName().equals(sp2.getPositionName())){
						return false;
					}
					position1Elems++;
				}
			}
		}
		
		if(position1Elems != position2Elems){
			return false;
		}
		
		return true;
	}

}
