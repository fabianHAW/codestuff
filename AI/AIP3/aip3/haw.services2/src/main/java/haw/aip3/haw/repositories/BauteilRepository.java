package haw.aip3.haw.repositories;

import haw.aip3.haw.entities.Bauteil;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface BauteilRepository extends PagingAndSortingRepository<Bauteil, Long>{
	public Bauteil findByName(String name);
}
