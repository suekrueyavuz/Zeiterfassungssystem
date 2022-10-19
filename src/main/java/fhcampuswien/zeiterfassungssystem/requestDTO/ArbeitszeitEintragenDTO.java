package fhcampuswien.zeiterfassungssystem.requestDTO;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ArbeitszeitEintragenDTO {
    private String startZeit;
    private String endZeit;
    private @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate arbeitstag;
}
