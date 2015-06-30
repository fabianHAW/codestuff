package haw.aip3.haw.geschaeftspartner.repositories;


import haw.aip3.haw.geschaeftspartner.entities.Geschaeftspartner;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface GeschaeftspartnerRepository extends
		PagingAndSortingRepository<Geschaeftspartner, Long> {

}
