package haw.aip3.haw.produkt.repositories;

import haw.aip3.haw.produkt.entities.Arbeitsplan;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface ArbeitsplanRepository extends
		PagingAndSortingRepository<Arbeitsplan, Long> {

}
