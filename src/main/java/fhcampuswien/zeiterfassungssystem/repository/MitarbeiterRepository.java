package fhcampuswien.zeiterfassungssystem.repository;

import fhcampuswien.zeiterfassungssystem.Enum.AusgeliehenStatus;
import fhcampuswien.zeiterfassungssystem.Enum.Role;
import fhcampuswien.zeiterfassungssystem.entity.Mitarbeiter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MitarbeiterRepository extends JpaRepository<Mitarbeiter, Long> {
    Optional<Mitarbeiter> findByUsername(String username);

    List<Mitarbeiter> findAllByAusgeliehenStatusAndRole(AusgeliehenStatus status, Role role);

    List<Mitarbeiter> findAllByRole(Role role);
}
