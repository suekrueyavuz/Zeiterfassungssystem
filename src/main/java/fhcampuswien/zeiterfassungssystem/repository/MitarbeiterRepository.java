package fhcampuswien.zeiterfassungssystem.repository;

import fhcampuswien.zeiterfassungssystem.entity.Mitarbeiter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MitarbeiterRepository extends JpaRepository<Mitarbeiter, Long> {

}
