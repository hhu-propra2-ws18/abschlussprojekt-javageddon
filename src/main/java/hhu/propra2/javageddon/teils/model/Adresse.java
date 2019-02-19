package hhu.propra2.javageddon.teils.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Adresse {

    private String hausnummer;

    private String strasse;

    private String ort;

    private int plz;

    public String strHsrAusgabe(){
        String ausgabe = strasse + " " + hausnummer + ", " + plz + " " + ort;
        return ausgabe;
    }

    public String plzOrtAusgabe(){
        String ausgabe = strasse + " " + hausnummer + ", " + plz + " " + ort;
        return ausgabe;
    }
}
