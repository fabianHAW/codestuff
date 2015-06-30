package haw.aip3.haw.fertigungsverwaltung.repositories;

import haw.aip3.haw.fertigungsverwaltung.entities.Fertigungsauftrag;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface FertigungsauftragRepository extends
		PagingAndSortingRepository<Fertigungsauftrag, Long> {

}
