package haw.aip3.haw.entities.auftragsverwaltung;

import haw.aip3.haw.entities.produkt.Bauteil;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Angebot {
	
	public Angebot() {}
	
	public Angebot(Bauteil bauteil, Date gueltigAb, Date gueltigBis, double preis) {
		this.bauteil = bauteil;
		this.gueltigAb = gueltigAb;
		this.gueltigBis = gueltigBis;
		this.preis = preis;
	}
	
	public Angebot(int kundenID, String bauteil){
		//Kunde mit kundenID suchen
		this.kundenID = kundenID;
		
		//Bauteil aus produktService suchen und speichern.
		//this.bauteil = bauteil;
	}
	
	@Id
	@GeneratedValue
	private long angebotsNr;
	
	@Column
	private Date gueltigAb;
	
	@Column
	private Date gueltigBis;
	
	@Column
	private double preis;
	
	@ManyToOne(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	private Bauteil bauteil;
	
	@Column
	private int kundenID;
	
	public long getAngebotsNr() {
		return angebotsNr;
	}

	public void setAngebotsNr(long angebotsNr) {
		this.angebotsNr = angebotsNr;
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

	public double getPreis() {
		return preis;
	}

	public void setPreis(double preis) {
		this.preis = preis;
	}
	
	

	public Bauteil getBauteil() {
		return bauteil;
	}

	public void setBauteil(Bauteil bauteil) {
		this.bauteil = bauteil;
	}

	//Hashcode & Equals anpassen, wenn Übereinstimmung in Konstruktoränderung
	//da kundenID hinzugekommen als Attribut.
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (angebotsNr ^ (angebotsNr >>> 32));
		result = prime * result + ((bauteil == null) ? 0 : bauteil.hashCode());
		result = prime * result
				+ ((gueltigAb == null) ? 0 : gueltigAb.hashCode());
		result = prime * result
				+ ((gueltigBis == null) ? 0 : gueltigBis.hashCode());
		long temp;
		temp = Double.doubleToLongBits(preis);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		System.out.println("0c");
		if (this == obj)
			return true;
		if (obj == null)
		{
			System.out.println("1c");
				return false;
			}
		if (getClass() != obj.getClass())
		{
			System.out.println("2c");
				return false;
			}
		Angebot other = (Angebot) obj;
		if (angebotsNr != other.angebotsNr)
		{
			System.out.println("3c");
				return false;
			}
		if (bauteil == null) {
			if (other.bauteil != null)
			{
				System.out.println("4c");
					return false;
				}
		} else if (!bauteil.equals(other.bauteil))
		{
			System.out.println("5c");
				return false;
			}
		if (gueltigAb == null) {
			if (other.gueltigAb != null)
			{
				System.out.println("6c");
					return false;
				}
		} else if (!gueltigAb.equals(other.gueltigAb))
		{
			System.out.println("7c");
				return false;
			}
		if (gueltigBis == null) {
			if (other.gueltigBis != null)
			{
				System.out.println("8c");
					return false;
				}
		} else if (!gueltigBis.equals(other.gueltigBis))
		{
			System.out.println("9c");
				return false;
			}
		if (Double.doubleToLongBits(preis) != Double
				.doubleToLongBits(other.preis))
		{
			System.out.println("10c");
				return false;
			}
		return true;
	}

	
	

	
}
