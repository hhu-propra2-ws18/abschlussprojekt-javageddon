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

    private Boolean akzeptiert;

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
}
