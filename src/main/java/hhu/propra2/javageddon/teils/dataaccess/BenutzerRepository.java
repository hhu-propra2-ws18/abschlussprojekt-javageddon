package hhu.propra2.javageddon.teils.dataaccess;

import hhu.propra2.javageddon.teils.model.Benutzer;
import org.springframework.data.repository.CrudRepository;

public interface BenutzerRepository extends CrudRepository<Benutzer,Long> {

    Benutzer findById(long i);

    boolean existsByEmail(String email);
    boolean existsByName(String name);
}
