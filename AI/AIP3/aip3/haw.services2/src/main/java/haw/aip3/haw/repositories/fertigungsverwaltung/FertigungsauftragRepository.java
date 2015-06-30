package haw.aip3.haw.repositories.fertigungsverwaltung;

import haw.aip3.haw.entities.fertigungsverwaltung.Fertigungsauftrag;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface FertigungsauftragRepository extends
		PagingAndSortingRepository<Fertigungsauftrag, Long> {

}
