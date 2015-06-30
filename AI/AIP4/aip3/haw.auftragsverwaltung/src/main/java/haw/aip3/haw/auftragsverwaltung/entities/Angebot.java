package haw.aip3.haw.auftragsverwaltung.entities;

import haw.aip3.haw.base.entities.IAngebot;
import haw.aip3.haw.base.entities.IBauteil;
import haw.aip3.haw.produkt.entities.Bauteil;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Angebot implements IAngebot{

	public Angebot() {
	}

	public Angebot(IBauteil bauteil, Date gueltigAb, Date gueltigBis,
			double preis) {
		this.bauteil = bauteil;
		this.gueltigAb = gueltigAb;
		this.gueltigBis = gueltigBis;
		this.preis = preis;
	}

	public Angebot(int kundenID, String bauteil) {
		this.kundenID = kundenID;
	}

	public int getKundenID() {
		return kundenID;
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

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH, targetEntity = Bauteil.class)
	private IBauteil bauteil;

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

	public IBauteil getBauteil() {
		return bauteil;
	}

	public void setBauteil(IBauteil bauteil) {
		this.bauteil = bauteil;
	}

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
		if (this == obj)
			return true;
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Angebot other = (Angebot) obj;
		if (angebotsNr != other.angebotsNr) {
			return false;
		}
		if (bauteil == null) {
			if (other.bauteil != null) {
				return false;
			}
		} else if (!bauteil.equals(other.bauteil)) {
			return false;
		}
		if (gueltigAb == null) {
			if (other.gueltigAb != null) {
				return false;
			}
		} else if (!gueltigAb.equals(other.gueltigAb)) {
			return false;
		}
		if (gueltigBis == null) {
			if (other.gueltigBis != null) {
				return false;
			}
		} else if (!gueltigBis.equals(other.gueltigBis)) {
			return false;
		}
		if (Double.doubleToLongBits(preis) != Double
				.doubleToLongBits(other.preis)) {
			return false;
		}
		return true;
	}

}
