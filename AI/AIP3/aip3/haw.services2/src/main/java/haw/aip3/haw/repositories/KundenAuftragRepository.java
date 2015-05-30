package haw.aip3.haw.repositories;

import haw.aip3.haw.entities.KundenAuftrag;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface KundenAuftragRepository extends
		PagingAndSortingRepository<KundenAuftrag, Long> {

}
