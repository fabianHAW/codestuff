package haw.aip3.haw.repositories.produkt;

import haw.aip3.haw.entities.produkt.Bauteil;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface BauteilRepository extends PagingAndSortingRepository<Bauteil, Long>{
	public Bauteil findByName(String name);
}
