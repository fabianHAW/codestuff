package haw.aip3.haw.entities.fertigungsverwaltung;

import haw.aip3.haw.entities.auftragsverwaltung.KundenAuftrag;
import haw.aip3.haw.entities.produkt.Bauteil;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class Fertigungsauftrag {

	public Fertigungsauftrag(){
		
	}
	
	public Fertigungsauftrag(KundenAuftrag k) {
		// TODO Auto-generated constructor stub
		kundenAuftrag = k;
	}
	
	@Id
	@GeneratedValue
	private Long nr;
	
	@ManyToOne(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	private Bauteil bauteil;
	
	@OneToOne(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	private KundenAuftrag kundenAuftrag;

	public Long getNr() {
		return nr;
	}

	public void setNr(Long nr) {
		this.nr = nr;
	}

	public Bauteil getBauteil() {
		return bauteil;
	}

	public void setBauteil(Bauteil bauteil) {
		this.bauteil = bauteil;
	}

	public KundenAuftrag getKundenAuftrag() {
		return kundenAuftrag;
	}

	public void setKundenAuftrag(KundenAuftrag kundenAuftrag) {
		this.kundenAuftrag = kundenAuftrag;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bauteil == null) ? 0 : bauteil.hashCode());
		result = prime * result
				+ ((kundenAuftrag == null) ? 0 : kundenAuftrag.hashCode());
		result = prime * result + ((nr == null) ? 0 : nr.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		System.out.println("0");
		if (this == obj)
			return true;
		if (obj == null){
		System.out.println("1");
			return false;
		}
		if (getClass() != obj.getClass())
		{
			System.out.println("2");
				return false;
			}
		Fertigungsauftrag other = (Fertigungsauftrag) obj;
		if (bauteil == null) {
			if (other.bauteil != null)
			{
				System.out.println("3");
					return false;
				}
		} else if (!bauteil.equals(other.bauteil))
		{
			System.out.println("4");
				return false;
			}
		if (kundenAuftrag == null) {
			if (other.kundenAuftrag != null)
			{
				System.out.println("5");
					return false;
				}
		} else if (!kundenAuftrag.equals(other.kundenAuftrag))
		{
			System.out.println("6");
				return false;
			}
		if (nr == null) {
			if (other.nr != null)
			{
				System.out.println("7");
					return false;
				}
		} else if (!nr.equals(other.nr))
		{
			System.out.println("8");
				return false;
			}
		return true;
	}

	

}
