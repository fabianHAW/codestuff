package mps.produkt.repositories;

import mps.produkt.entities.Produkt;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProduktRepository extends PagingAndSortingRepository<Produkt, Long> {
}
