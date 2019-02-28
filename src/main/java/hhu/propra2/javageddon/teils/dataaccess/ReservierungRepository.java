package hhu.propra2.javageddon.teils.dataaccess;

import hhu.propra2.javageddon.teils.model.Artikel;
import hhu.propra2.javageddon.teils.model.Benutzer;
import org.springframework.data.repository.CrudRepository;
import hhu.propra2.javageddon.teils.model.Reservierung;

import java.util.List;

public interface ReservierungRepository extends CrudRepository<Reservierung, Long> {

    List<Reservierung> findAll();
    List<Reservierung> findByArtikel(Artikel a);
    List<Reservierung> findByLeihender(Benutzer b);
    List<Reservierung> findByLeihenderAndSichtbar(Benutzer b, boolean tf);
    List<Reservierung> findByArtikelAndLeihender(Artikel a, Benutzer b);
    List<Reservierung> findByArtikelEigentuemerAndBearbeitet(Benutzer b, boolean tf);
    List<Reservierung> findByArtikelEigentuemerAndZurueckerhaltenAndAkzeptiert(Benutzer b, boolean tf, boolean ft);
    List<Reservierung> findByArtikelAndAkzeptiertAndZurueckerhalten(Artikel a, boolean tf, boolean ft);
    List<Reservierung> findByArtikelAndAkzeptiert(Artikel a, boolean tf);
    List<Reservierung> findByArtikelAndBearbeitet(Artikel a, boolean tf);
    Reservierung findById(long i);

}
