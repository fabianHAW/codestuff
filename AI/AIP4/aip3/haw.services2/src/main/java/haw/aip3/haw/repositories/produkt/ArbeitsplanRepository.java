package haw.aip3.haw.repositories.produkt;

import haw.aip3.haw.entities.produkt.Arbeitsplan;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface ArbeitsplanRepository extends
		PagingAndSortingRepository<Arbeitsplan, Long> {

}
