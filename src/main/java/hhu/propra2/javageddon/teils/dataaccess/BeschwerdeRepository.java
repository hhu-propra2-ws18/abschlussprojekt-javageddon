package hhu.propra2.javageddon.teils.dataaccess;

import hhu.propra2.javageddon.teils.model.Beschwerde;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface BeschwerdeRepository extends CrudRepository<Beschwerde,Long> {

    List<Beschwerde> getAllByIdIsNotNull();
    List<Beschwerde> getAllByBearbeitetFalse();

}
