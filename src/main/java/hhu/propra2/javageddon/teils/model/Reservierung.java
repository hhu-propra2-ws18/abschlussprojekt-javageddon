package hhu.propra2.javageddon.teils.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.tomcat.jni.Local;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Reservierung {
    @Id
    @GeneratedValue
    private long id;

    private LocalDate start;

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

    public boolean isBetween(LocalDate checkDay) {
        if (!checkDay.isBefore(start) && !checkDay.isAfter(ende)) {
                return true; }
        return false;
    }
}
