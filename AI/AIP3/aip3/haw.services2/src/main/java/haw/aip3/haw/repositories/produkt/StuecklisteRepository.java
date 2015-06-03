package haw.aip3.haw.repositories.produkt;

import haw.aip3.haw.entities.produkt.Stueckliste;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface StuecklisteRepository extends
		PagingAndSortingRepository<Stueckliste, Long> {
	public Stueckliste findByName(String name);
}
