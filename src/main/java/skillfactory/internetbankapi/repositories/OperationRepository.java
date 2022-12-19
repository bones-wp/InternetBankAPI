package skillfactory.internetbankapi.repositories;

import org.springframework.data.repository.CrudRepository;
import skillfactory.internetbankapi.entities.Operation;

public interface OperationRepository extends CrudRepository<Operation, Long> {
}
