package hhu.propra2.javageddon.teils.dataaccess;

import hhu.propra2.javageddon.teils.model.Artikel;
import hhu.propra2.javageddon.teils.model.Benutzer;
import org.springframework.data.repository.CrudRepository;
import hhu.propra2.javageddon.teils.model.Reservierung;

import java.util.List;

public interface ReservierungRepository extends CrudRepository<Reservierung, Long> {

    List<Reservierung> findByArtikel(Artikel a);
    List<Reservierung> findByLeihender(Benutzer b);
    List<Reservierung> findByArtikelAndLeihender(Artikel a, Benutzer b);
    List<Reservierung> findByArtikelEigentuemer(Benutzer b);

}
