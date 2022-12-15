package skillfactory.internetbankapi.repositories;

import org.springframework.data.repository.CrudRepository;
import skillfactory.internetbankapi.entities.User;

public interface UserRepository extends CrudRepository<User, Long> {
}
