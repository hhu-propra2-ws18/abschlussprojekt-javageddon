package hhu.propra2.javageddon.teils.dataaccess;

import hhu.propra2.javageddon.teils.model.Benutzer;
import hhu.propra2.javageddon.teils.model.Transaktion;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TransaktionRepository extends CrudRepository<Transaktion,Long> {

    List<Transaktion> findByKontoinhaber(Benutzer b);
}
