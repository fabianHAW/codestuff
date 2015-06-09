package haw.aip3.haw.entities.auftragsverwaltung;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class KundenAuftrag {

	public KundenAuftrag() {
	}

	public KundenAuftrag(Angebot a, boolean abgeschlossen, Date auftragsDatum) {
		this.angebot = a;
		this.abgeschlossen = abgeschlossen;
		this.auftragsDatum = auftragsDatum;
	}

	@Id
	@GeneratedValue
	private Long auftragsID;

	@Column
	private boolean abgeschlossen;

	@Column
	private Date auftragsDatum;

	@OneToOne(fetch=FetchType.EAGER, cascade=CascadeType.REFRESH)
	//@Column
	private Angebot angebot;


	public Long getAuftragsID() {
		return auftragsID;
	}

	public void setAuftragsID(Long auftragsID) {
		this.auftragsID = auftragsID;
	}

	public boolean isAbgeschlossen() {
		return abgeschlossen;
	}

	public void setAbgeschlossen(boolean abgeschlossen) {
		this.abgeschlossen = abgeschlossen;
	}

	public Date getAuftragsDatum() {
		return auftragsDatum;
	}

	public void setAuftragsDatum(Date auftragsDatum) {
		this.auftragsDatum = auftragsDatum;
	}

	public Angebot getAngebot() {
		return angebot;
	}

	public void setAngebot(Angebot angebot) {
		this.angebot = angebot;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (abgeschlossen ? 1231 : 1237);
		result = prime * result + ((angebot == null) ? 0 : angebot.hashCode());
		result = prime * result
				+ ((auftragsDatum == null) ? 0 : auftragsDatum.hashCode());
		result = prime * result
				+ ((auftragsID == null) ? 0 : auftragsID.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		System.out.println("0a");
		if (this == obj)
			return true;
		if (obj == null)
		{
			System.out.println("1");
				return false;
			}
		if (getClass() != obj.getClass())
		{
			System.out.println("2");
				return false;
			}
		KundenAuftrag other = (KundenAuftrag) obj;
		if (abgeschlossen != other.abgeschlossen)
		{
			System.out.println("3");
				return false;
			}
		if (angebot == null) {
			if (other.angebot != null)
			{
				System.out.println("4");
					return false;
				}
		} else if (!angebot.equals(other.angebot))
		{
			System.out.println("4b");
				return false;
			}
		if (auftragsDatum == null) {
			if (other.auftragsDatum != null)
			{
				System.out.println("5");
					return false;
				}
		} else if (!auftragsDatum.equals(other.auftragsDatum))
		{
			System.out.println("6b");
				return false;
			}
		if (auftragsID == null) {
			if (other.auftragsID != null)
			{
				System.out.println("7");
					return false;
				}
		} else if (!auftragsID.equals(other.auftragsID))
		{
			System.out.println("8");
				return false;
			}
		return true;
	}

}
