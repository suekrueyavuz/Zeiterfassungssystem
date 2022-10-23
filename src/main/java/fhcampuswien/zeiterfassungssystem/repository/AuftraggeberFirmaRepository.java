package fhcampuswien.zeiterfassungssystem.repository;

import fhcampuswien.zeiterfassungssystem.entity.AuftraggeberFirma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuftraggeberFirmaRepository extends JpaRepository<AuftraggeberFirma, Long> {
    AuftraggeberFirma findByUsername(String username);

}
