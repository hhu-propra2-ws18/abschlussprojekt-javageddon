package hhu.propra2.javageddon.teils.dataaccess;

import hhu.propra2.javageddon.teils.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User,Long> {


}
