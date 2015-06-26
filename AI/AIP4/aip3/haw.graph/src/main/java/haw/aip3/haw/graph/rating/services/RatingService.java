package haw.aip3.haw.graph.rating.services;

import haw.aip3.haw.graph.rating.dto.SalesData;
import haw.aip3.haw.graph.rating.dto.SalesDataForOftenSalesPerRegion;
import haw.aip3.haw.graph.rating.dto.SalesDataForRelatedProducts;
import haw.aip3.haw.graph.rating.nodes.BauteilNode;
import haw.aip3.haw.graph.rating.nodes.GeschaeftspartnerNode;

public interface RatingService {

	Iterable<? extends SalesData> showProductSalesByCity(String string);
	
	Iterable<? extends SalesDataForOftenSalesPerRegion> showOftenProductsalesByCity();

	Iterable<? extends SalesDataForRelatedProducts> showRelatedProducts();
	
	GeschaeftspartnerNode getOrCreateGeschaeftspartner(Long id, String name, String stadt);

	BauteilNode getOrCreateBauteil(Long id, String name);

	void addBestellung(GeschaeftspartnerNode k, BauteilNode p, double preis);

}