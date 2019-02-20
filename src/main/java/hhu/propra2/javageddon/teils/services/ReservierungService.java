package hhu.propra2.javageddon.teils.services;

import hhu.propra2.javageddon.teils.dataaccess.ReservierungRepository;
import hhu.propra2.javageddon.teils.model.Artikel;
import hhu.propra2.javageddon.teils.model.Benutzer;
import hhu.propra2.javageddon.teils.model.Reservierung;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class ReservierungService {

    private ReservierungRepository alleReservierungen;

    public ReservierungService(ReservierungRepository reservierungen){
        this.alleReservierungen = reservierungen;
    }

    public Reservierung addReservierung(Reservierung r) {
        return alleReservierungen.save(r);
    }

    public List<Reservierung> findReservierungByArtikel(Artikel a){
        return alleReservierungen.findByArtikel(a);
    }

    public List<Reservierung> findReservierungByLeihender(Benutzer b){
        return alleReservierungen.findByLeihender(b);
    }

    public List<Reservierung> findReservierungByArtikelAndLeihender(Artikel a, Benutzer b){
        return alleReservierungen.findByArtikelAndLeihender(a,b);
    }

    public List<Reservierung> findCurrentReservierungByArtikelOrderedByDate(Artikel a){
        List<Reservierung> artikelReservierungen = alleReservierungen.findByArtikel(a);
        LocalDate currentDay = LocalDate.now();
        for (Reservierung res : artikelReservierungen) {
            if (res.getEnde().isBefore(currentDay)) {
                artikelReservierungen.remove(res);
            }
        }
        return  artikelReservierungen
        		.stream()
        		.sorted((r1,r2) -> r1.getEnde().compareTo(r2.getEnde()))
        		.collect(Collectors.toList());
    }

}
