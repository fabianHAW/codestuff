package haw.aip3.haw.auftragsverwaltung.repositories;


import haw.aip3.haw.auftragsverwaltung.entities.KundenAuftrag;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface KundenAuftragRepository extends
		PagingAndSortingRepository<KundenAuftrag, Long> {

}
