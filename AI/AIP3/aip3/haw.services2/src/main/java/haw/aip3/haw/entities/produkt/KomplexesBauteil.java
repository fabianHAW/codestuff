package haw.aip3.haw.entities.produkt;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class KomplexesBauteil extends Bauteil {

	public KomplexesBauteil() {
		super();
	}

	public KomplexesBauteil(String name, Stueckliste stueckliste, Arbeitsplan arbeitsplan) {
		super(name);
		this.stueckliste = stueckliste;
		this.arbeitsplan = arbeitsplan;
	}

	@OneToOne(cascade=CascadeType.ALL)
	Arbeitsplan arbeitsplan;
	
	@OneToOne(cascade=CascadeType.ALL)
	private Stueckliste stueckliste;

	@Override
	public boolean hasStueckliste() {
		return true;
	}

	public Stueckliste getStueckliste() {
		return stueckliste;
	}

	public void setStueckliste(Stueckliste stueckliste) {
		this.stueckliste = stueckliste;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((stueckliste == null) ? 0 : stueckliste.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
		{
			System.out.println("1e");
				return false;
			}
		if (getClass() != obj.getClass())
		{
			System.out.println("2e");
				return false;
			}
		KomplexesBauteil other = (KomplexesBauteil) obj;
		if (stueckliste == null) {
			if (other.stueckliste != null)
			{
				System.out.println("3e");
					return false;
				}
		} else if (!stueckliste.equals(other.stueckliste))
		{
			System.out.println("4e");
				return false;
			};
		return true;
	}

	public Arbeitsplan getArbeitsplan() {
		return arbeitsplan;
	}

	public void setArbeitsplan(Arbeitsplan arbeitsplan) {
		this.arbeitsplan = arbeitsplan;
	}

}
