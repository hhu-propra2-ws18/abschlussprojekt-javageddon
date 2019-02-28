package hhu.propra2.javageddon.teils.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.apache.tomcat.jni.Local;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static java.time.temporal.ChronoUnit.DAYS;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Reservierung {
    @Id
    @GeneratedValue
    private long id;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate start;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate ende;

    @ManyToOne
    @JoinColumn(name = "RESERVIERUNG_BENUTZER_ID")
    private Benutzer leihender;

    @ManyToOne
    @JoinColumn(name = "RESERVIERUNG_ARTIKEL_ID")
    private Artikel artikel;

    @Builder.Default
    private Boolean bearbeitet = false;

    @Builder.Default
    private Boolean akzeptiert = false;

    @Builder.Default
    private Boolean zurueckgegeben = false;

    @Builder.Default
    private Boolean zurueckerhalten = false;

    public String printReservierungsDauer(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        if (start.isEqual(ende)) {
            return start.format(formatter);
        }
        String formattedDates = start.format(formatter) + " - " + ende.format(formatter);
        return formattedDates;
    }
    
    public String printFormattedStart() {
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    	return start.format(formatter);
    }
    
    public String printFormattedEnde() {
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    	return ende.format(formatter);
    }

    public boolean containsDate(LocalDate checkDay) {
        if (!checkDay.isBefore(start) && !checkDay.isAfter(ende)) {
                return true; }
        return false;
    }

    public int calculateReservierungsLength(){
        return (int) DAYS.between(start, ende)+1;
    }

    public int ermittleStatus(){
        if (!bearbeitet && !start.isBefore(LocalDate.now())) return 1;           // Anfrage in Bearbeitung
        if (bearbeitet && !akzeptiert) return 2;                              // Anfrage storniert
        if (akzeptiert && !zurueckerhalten && start.isAfter(LocalDate.now())) return 3;   // Anfrage akzeptiert
        if (containsDate(LocalDate.now()) && !zurueckerhalten && akzeptiert) return 4;   // Ausleihe laeuft
        if (akzeptiert && zurueckerhalten) return 5;                          // Verleih abgeschlossen
        if (ende.isBefore(LocalDate.now()) && !zurueckgegeben && akzeptiert) return 6;      // Ausleihfrist abgelaufen
        if (start.isBefore(LocalDate.now()) && !bearbeitet) return 7;     // Bearbeitungsfrist abgelaufen
        return -1; // Zustand tritt nie ein
    }
}
