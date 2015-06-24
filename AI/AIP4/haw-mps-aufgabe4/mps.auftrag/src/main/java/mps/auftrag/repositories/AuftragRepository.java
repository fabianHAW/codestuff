package mps.auftrag.repositories;

import java.util.Collection;

import mps.auftrag.entities.Auftrag;
import mps.kunde.entities.Kunde;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface AuftragRepository extends PagingAndSortingRepository<Auftrag, Long> {

	Collection<Auftrag> findByKunde(Kunde k);

}
