package fhcampuswien.zeiterfassungssystem.requestDTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PasswortAendernDTO {
    private String neuesPasswort;
    private String altesPasswort;
}
