package fhcampuswien.zeiterfassungssystem.requestDTO;

import fhcampuswien.zeiterfassungssystem.Enum.Status;
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
    private Status status;
    private @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate arbeitstag;
}
