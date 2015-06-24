package mps.kunde.repositories;

import mps.kunde.entities.Kunde;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface KundeRepository extends PagingAndSortingRepository<Kunde, Long> {

}
