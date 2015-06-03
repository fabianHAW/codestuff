package haw.aip3.haw.repositories;

import haw.aip3.haw.entities.Arbeitsplan;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface ArbeitsplanRepository extends
		PagingAndSortingRepository<Arbeitsplan, Long> {

}
