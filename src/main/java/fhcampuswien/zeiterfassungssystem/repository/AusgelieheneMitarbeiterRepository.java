package fhcampuswien.zeiterfassungssystem.repository;

import fhcampuswien.zeiterfassungssystem.Enum.ZeitStatus;
import fhcampuswien.zeiterfassungssystem.entity.AusgelieheneMitarbeiter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AusgelieheneMitarbeiterRepository extends JpaRepository<AusgelieheneMitarbeiter, Long> {
    @Query("SELECT u FROM AusgelieheneMitarbeiter u WHERE u.mitarbeiter.id = ?1 AND u.auftraggeberFirma.id = ?2 AND u.tag = ?3")
    AusgelieheneMitarbeiter getAusgelieheneMitarbeiterVonFirma(Long mitarbeiterId, Long firmaId, LocalDate arbeitstag);

    @Query("SELECT u FROM AusgelieheneMitarbeiter u WHERE u.auftraggeberFirma.id = ?1")
    List<AusgelieheneMitarbeiter> getAllByFirmaId(Long firmaId);

    @Query("SELECT u FROM AusgelieheneMitarbeiter u WHERE u.auftraggeberFirma.id = ?1 AND u.zeitStatus = ?2")
    List<AusgelieheneMitarbeiter> getAllByFirmaIdAndZeitstatus(Long firmaId, ZeitStatus status);

    @Query("SELECT u FROM AusgelieheneMitarbeiter u WHERE u.auftraggeberFirma.id = ?1 AND u.tag BETWEEN ?2 AND ?3")
    List<AusgelieheneMitarbeiter> getAllByFirmaIdAndZeitstatusFuerZeitraum(Long firmaId, LocalDate von, LocalDate bis);

    List<AusgelieheneMitarbeiter> findAllByMitarbeiter_Id(Long mitarbeiterId);
}
