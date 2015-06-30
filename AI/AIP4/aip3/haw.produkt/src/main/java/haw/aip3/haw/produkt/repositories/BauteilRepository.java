package haw.aip3.haw.produkt.repositories;

import haw.aip3.haw.produkt.entities.Bauteil;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface BauteilRepository extends PagingAndSortingRepository<Bauteil, Long>{
	public List<Bauteil> findByName(String name);
}
