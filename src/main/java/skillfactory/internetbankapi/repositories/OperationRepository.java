package skillfactory.internetbankapi.repositories;

import org.springframework.data.repository.CrudRepository;
import skillfactory.internetbankapi.entities.Operations;

public interface OperationRepository extends CrudRepository<Operations, Long> {
}
