package hhu.propra2.javageddon.teils.dataaccess;

import hhu.propra2.javageddon.teils.model.Benutzer;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface BenutzerRepository extends CrudRepository<Benutzer,Long> {


    List<Benutzer> getAllByIdIsNotNull();

    boolean existsByEmail(String email);
    boolean existsByName(String name);
    Benutzer findByName(String username);
}
