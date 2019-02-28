package hhu.propra2.javageddon.teils.dataaccess;

import hhu.propra2.javageddon.teils.model.Benutzer;
import hhu.propra2.javageddon.teils.model.VerkaufArtikel;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VerkaufArtikelRepository extends CrudRepository<VerkaufArtikel, Long> {
    List<VerkaufArtikel> findAll();
    List<VerkaufArtikel> findByEigentuemer(Benutzer b);
    VerkaufArtikel findById(long l);
}
