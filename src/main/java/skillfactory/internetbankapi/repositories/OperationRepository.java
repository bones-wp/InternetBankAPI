package skillfactory.internetbankapi.repositories;

import org.springframework.data.repository.CrudRepository;
import skillfactory.internetbankapi.entities.Operations;

import java.time.LocalDate;
import java.util.List;

public interface OperationRepository extends CrudRepository<Operations, Long> {
}
