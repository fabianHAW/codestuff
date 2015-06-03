package haw.aip3.haw.repositories;

import haw.aip3.haw.entities.Fertigungsauftrag;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface FertigungsauftragRepository extends
		PagingAndSortingRepository<Fertigungsauftrag, Long> {

}
