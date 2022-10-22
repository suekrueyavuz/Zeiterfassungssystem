package fhcampuswien.zeiterfassungssystem;

import fhcampuswien.zeiterfassungssystem.Enum.Role;
import fhcampuswien.zeiterfassungssystem.entity.AuftraggeberFirma;
import fhcampuswien.zeiterfassungssystem.entity.Mitarbeiter;
import fhcampuswien.zeiterfassungssystem.service.AuftraggeberFirmaService;
import fhcampuswien.zeiterfassungssystem.service.MitarbeiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalTime;

@SpringBootApplication(exclude = {ErrorMvcAutoConfiguration.class})
public class ZeiterfassungssystemApplication implements CommandLineRunner {
	@Autowired private MitarbeiterService mitarbeiterService;
	@Autowired private AuftraggeberFirmaService auftraggeberFirmaService;
	@Autowired private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(ZeiterfassungssystemApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		createTestMitarbeiter("Sükrü", "Yavuz", "sukru", "test123", Role.ROLE_MITARBEITER);
		createTestMitarbeiter("Shipdon", "Veseli", "shipdon", "test123", Role.ROLE_ADMIN);
		createTestMitarbeiter("Sükür", "Yavuz", "sukur", "test123", Role.ROLE_SCHICHTLEITER);

		createTestFirma("Primus", "primus", "test123");
		createTestFirma("Damak", "damak", "test123");
	}

	private void createTestMitarbeiter(String forename, String surname, String username, String password, Role role) {
		if (mitarbeiterService.getMitarbeiterByUsername(username) == null) {
			Mitarbeiter mitarbeiter = mitarbeiterService.save(Mitarbeiter.builder()
					.forename(forename)
					.surname(surname)
					.username(username)
					.password(passwordEncoder.encode(password))
					.role(role)
					.build());
			mitarbeiterService.save(mitarbeiter);
		}
	}

	private void createTestFirma(String name, String username, String password) {
		if (auftraggeberFirmaService.getFirmaByUsername(username) == null) {
			AuftraggeberFirma firma = auftraggeberFirmaService.save(AuftraggeberFirma.builder()
					.name(name)
					.username(username)
					.password(passwordEncoder.encode(password))
					.build());
			auftraggeberFirmaService.save(firma);
		}
	}
}
