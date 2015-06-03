package haw.aip3.haw.repositories;

import haw.aip3.haw.entities.StuecklistenPosition;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface StuecklistenPostionRepository extends
		PagingAndSortingRepository<StuecklistenPosition, Long> {
	
	public StuecklistenPosition findByName(String name);

}
