package hhu.propra2.javageddon.teils.services;

import hhu.propra2.javageddon.teils.dataaccess.ReservierungRepository;
import hhu.propra2.javageddon.teils.model.Artikel;
import hhu.propra2.javageddon.teils.model.Benutzer;
import hhu.propra2.javageddon.teils.model.Reservierung;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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

    public List<Reservierung> findReservierungByLeihenderAndSichtbar(Benutzer b, Boolean tf){
        return alleReservierungen.findByLeihenderAndSichtbar(b, tf);
    }

    public List<Reservierung> findReservierungByArtikelAndLeihender(Artikel a, Benutzer b){
        return alleReservierungen.findByArtikelAndLeihender(a,b);
    }

    public Reservierung findReservierungById(long id) {
        return  alleReservierungen.findById(id);
    }
    
    public List<Reservierung> findReservierungByArtikelEigentuemerAndNichtBearbeitet(Benutzer b) {
    	return alleReservierungen.findByArtikelEigentuemerAndBearbeitet(b, false);
    }

    public List<Reservierung> findReservierungByArtikelEigentuemerAndNichtAbgeschlossen(Benutzer b) {
        List<Reservierung> artikelReservierungen = alleReservierungen.findByArtikelEigentuemerAndZurueckerhaltenAndAkzeptiert(b, false, true);
        List<Reservierung> zukuenftigeReservierungen = new ArrayList<Reservierung>();
        LocalDate currentDay = LocalDate.now();
        for (Reservierung res : artikelReservierungen) {
            if (res.getStart().isAfter(currentDay)) {
                zukuenftigeReservierungen.add(res);
            }
        }
        artikelReservierungen.removeAll(zukuenftigeReservierungen);

        return artikelReservierungen
                .stream()
                .sorted((r1,r2) -> r1.getEnde().compareTo(r2.getEnde()))
                .collect(Collectors.toList());
    }

    public List<Reservierung> findCurrentReservierungByArtikelOrderedByDate(Artikel a){
        List<Reservierung> artikelReservierungen = alleReservierungen.findByArtikel(a);
        List<Reservierung> vergangeneReservierungen = new ArrayList<Reservierung>();
        LocalDate currentDay = LocalDate.now();
        for (Reservierung res : artikelReservierungen) {
            if (res.getEnde().isBefore(currentDay)) {
                vergangeneReservierungen.add(res);
            }
        }
        artikelReservierungen.removeAll(vergangeneReservierungen);

        return artikelReservierungen
                .stream()
                .sorted((r1,r2) -> r1.getEnde().compareTo(r2.getEnde()))
                .collect(Collectors.toList());
    }

    public boolean isAllowedReservierungsDate(Artikel a, LocalDate startAntrag, LocalDate endeAntrag) {
        if (startAntrag.isBefore(LocalDate.now())) {
            return false;
        }
        if (endeAntrag.isBefore(startAntrag)) {
            return false;
        }
        Reservierung testDate = Reservierung.builder().start(startAntrag).ende(endeAntrag).build();
        List<Reservierung> akzeptierteReservierungen = alleReservierungen.findByArtikelAndAkzeptiert(a, true);
        List<Reservierung> reservierungenInBearbeitung = alleReservierungen.findByArtikelAndBearbeitet(a, false);
        List<Reservierung> artikelReservierungen = new ArrayList<Reservierung>();
        artikelReservierungen.addAll(akzeptierteReservierungen);
        artikelReservierungen.addAll(reservierungenInBearbeitung);
        for (Reservierung res : artikelReservierungen) {
            if (res.containsDate(startAntrag) || res.containsDate(endeAntrag)
                    || testDate.containsDate(res.getStart())
                    || testDate.containsDate(res.getEnde())) {
                return false;
            }
        }
        return true;
    }

    public List<Reservierung> fristAbgelaufeneReservierungen(Benutzer b){
        List<Reservierung> leihender_Reservierungen = alleReservierungen.findByLeihender(b);
        List<Reservierung> abgelaufeneReservierungen = new ArrayList<Reservierung>();
        LocalDate currentDay = LocalDate.now();
        for (Reservierung res : leihender_Reservierungen) {
            if (res.getEnde().isBefore(currentDay) && !res.getZurueckgegeben() && res.getAkzeptiert()) {
                abgelaufeneReservierungen.add(res);
            }
        }
        return abgelaufeneReservierungen;
    }

    public List<Reservierung> findCurrentReservierungByArtikelAndAkzeptiert(Artikel a){
        List<Reservierung> artikelReservierungen = alleReservierungen.findByArtikelAndAkzeptiert(a, true);
        List<Reservierung> vergangeneReservierungen = new ArrayList<Reservierung>();
        LocalDate currentDay = LocalDate.now();
        for (Reservierung res : artikelReservierungen) {
            if (res.getEnde().isBefore(currentDay)) {
                vergangeneReservierungen.add(res);
            }
        }
        artikelReservierungen.removeAll(vergangeneReservierungen);
        return artikelReservierungen;
    }

    public List<Reservierung> findCurrentReservierungByArtikelAndBearbeitet(Artikel a){
        List<Reservierung> artikelReservierungen = alleReservierungen.findByArtikelAndBearbeitet(a, false);
        List<Reservierung> vergangeneReservierungen = new ArrayList<Reservierung>();
        LocalDate currentDay = LocalDate.now();
        for (Reservierung res : artikelReservierungen) {
            if (res.getEnde().isBefore(currentDay)) {
                vergangeneReservierungen.add(res);
            }
        }
        artikelReservierungen.removeAll(vergangeneReservierungen);
        return artikelReservierungen;
    }

    public List<Reservierung> orderByDate(List<Reservierung> reservierung){
        return reservierung
                .stream()
                .sorted((r1,r2) -> r1.getEnde().compareTo(r2.getEnde()))
                .collect(Collectors.toList());
    }

}
