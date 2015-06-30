package haw.aip3.haw.produkt.repositories;

import haw.aip3.haw.produkt.entities.StuecklistenPosition;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface StuecklistenPostionRepository extends
		PagingAndSortingRepository<StuecklistenPosition, Long> {
	
	public List<StuecklistenPosition> findByName(String name);
	

}
