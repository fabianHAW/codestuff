package mps.rating.services;

import mps.rating.dto.SalesData;
import mps.rating.graph.nodes.KundeNode;
import mps.rating.graph.nodes.ProduktNode;

public interface RatingService {

	Iterable<? extends SalesData> showProductSalesByCity(String string);

	KundeNode getOrCreateKunde(Long id, String name, String stadt);

	ProduktNode getOrCreateProdukt(Long id, String name);

	void addBestellung(KundeNode k, ProduktNode p, int i);

}
