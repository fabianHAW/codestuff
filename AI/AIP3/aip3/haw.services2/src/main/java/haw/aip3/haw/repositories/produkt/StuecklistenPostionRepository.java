package haw.aip3.haw.repositories.produkt;

import haw.aip3.haw.entities.produkt.StuecklistenPosition;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface StuecklistenPostionRepository extends
		PagingAndSortingRepository<StuecklistenPosition, Long> {
	
	public StuecklistenPosition findByName(String name);

}
