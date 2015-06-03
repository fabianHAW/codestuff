package haw.aip3.haw.repositories;

import haw.aip3.haw.entities.Stueckliste;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface StuecklisteRepository extends
		PagingAndSortingRepository<Stueckliste, Long> {
	public Stueckliste findByName(String name);
}
