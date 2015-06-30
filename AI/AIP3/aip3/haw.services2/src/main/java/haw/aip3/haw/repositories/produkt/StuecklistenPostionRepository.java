package haw.aip3.haw.repositories.produkt;

import java.util.List;

import haw.aip3.haw.entities.produkt.StuecklistenPosition;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface StuecklistenPostionRepository extends
		PagingAndSortingRepository<StuecklistenPosition, Long> {
	
	public List<StuecklistenPosition> findByName(String name);
	

}
