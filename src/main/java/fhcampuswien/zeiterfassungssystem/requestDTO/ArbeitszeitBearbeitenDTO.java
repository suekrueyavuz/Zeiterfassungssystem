package fhcampuswien.zeiterfassungssystem.requestDTO;

import fhcampuswien.zeiterfassungssystem.Enum.ZeitStatus;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ArbeitszeitBearbeitenDTO {
    private Long firmaId;
    private ZeitStatus zeitStatus;
    private @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate arbeitstag;
}
