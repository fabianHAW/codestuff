package haw.aip3.haw.repositories.fertigungsverwaltung;

import haw.aip3.haw.entities.produkt.Vorgang;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface VorgangRepository extends
PagingAndSortingRepository<Vorgang, Long> {


}
