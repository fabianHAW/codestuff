package mps.produkt.services;

import mps.produkt.entities.Produkt;

public interface ProduktService {

	Produkt createProdukt();

	void save(Produkt produkt);

}
