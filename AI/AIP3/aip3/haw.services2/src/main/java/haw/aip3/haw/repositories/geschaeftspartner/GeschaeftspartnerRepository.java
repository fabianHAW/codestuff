package haw.aip3.haw.repositories.geschaeftspartner;

import haw.aip3.haw.entities.geschaeftspartner.Geschaeftspartner;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface GeschaeftspartnerRepository extends
		PagingAndSortingRepository<Geschaeftspartner, Long> {

}
