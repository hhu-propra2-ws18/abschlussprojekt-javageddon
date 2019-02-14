package hhu.propra2.javageddon.teils.dataaccess;

import hhu.propra2.javageddon.teils.model.Artikel;
import hhu.propra2.javageddon.teils.model.Foto;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FotoRepository extends CrudRepository<Foto,Long> {

    List<Foto> findByArtikel(Artikel a);
}
