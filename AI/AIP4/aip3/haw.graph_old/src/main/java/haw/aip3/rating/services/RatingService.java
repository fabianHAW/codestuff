package haw.aip3.rating.services;

import haw.aip3.rating.dto.SalesData;
import haw.aip3.rating.graph.nodes.BauteilNode;
import haw.aip3.rating.graph.nodes.GeschaeftspartnerNode;

public interface RatingService {

	Iterable<? extends SalesData> showProductSalesByCity(String string);

	GeschaeftspartnerNode getOrCreateGeschaeftspartner(Long id, String name, String stadt);

	BauteilNode getOrCreateBauteil(Long id, String name);

	void addBestellung(GeschaeftspartnerNode k, BauteilNode p, double preis);

}