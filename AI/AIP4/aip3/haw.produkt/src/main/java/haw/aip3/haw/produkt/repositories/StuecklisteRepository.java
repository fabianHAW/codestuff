package haw.aip3.haw.produkt.repositories;

import haw.aip3.haw.produkt.entities.Stueckliste;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface StuecklisteRepository extends
		PagingAndSortingRepository<Stueckliste, Long> {
	public Stueckliste findByName(String name);
}
