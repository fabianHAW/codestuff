package haw.aip3.haw.repositories.auftragsverwaltung;

import haw.aip3.haw.entities.auftragsverwaltung.KundenAuftrag;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface KundenAuftragRepository extends
		PagingAndSortingRepository<KundenAuftrag, Long> {

}
