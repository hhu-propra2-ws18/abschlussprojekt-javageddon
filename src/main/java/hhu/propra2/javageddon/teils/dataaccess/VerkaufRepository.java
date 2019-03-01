package hhu.propra2.javageddon.teils.dataaccess;

import hhu.propra2.javageddon.teils.model.Verkauf;
import hhu.propra2.javageddon.teils.model.Benutzer;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VerkaufRepository extends CrudRepository<Verkauf, Long> {
    Verkauf findById(long id);
    List<Verkauf> findAll();
    List<Verkauf> findByKaeufer(Benutzer b);
    List<Verkauf> findByArtikelEigentuemerAndBearbeitet(Benutzer b, boolean tf);

}
