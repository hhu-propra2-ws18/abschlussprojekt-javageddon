package hhu.propra2.javageddon.teils.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Adresse {

    @NotEmpty(message = "Die Adresse muss eine Hausnummer haben")
    private String hausnummer;

    @NotEmpty(message = "Die Adresse muss eine Strasse haben")
    private String strasse;

    @NotEmpty(message = "Die Adresse muss einen Ort haben")
    private String ort;

    @Min(value=1, message = "Die Adresse muss eine Postleitzahl haben")
    private int plz;

    public String adressAusgabe(){
        String ausgabe = strasse + " " + hausnummer + ", " + plz + " " + ort;
        return ausgabe;
    }
}
