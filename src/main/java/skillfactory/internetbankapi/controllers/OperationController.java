package skillfactory.internetbankapi.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import skillfactory.internetbankapi.entities.Operations;
import skillfactory.internetbankapi.entities.User;
import skillfactory.internetbankapi.repositories.UserRepository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class OperationController {
    private static final Logger logger = LoggerFactory.getLogger(OperationController.class);


    private final UserRepository userRepository;

    @Autowired
    public OperationController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @GetMapping(value = "/operation/{id}")
    public ResponseEntity<List<Operations>> getOperationList(@PathVariable(name = "id") Long id,
                                                             @RequestParam(value = "start", required = false) String start,
                                                             @RequestParam(value = "end", required = false) String end) {

        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            List<Operations> operationsList = optionalUser.get().getOperations();

            if (start != null && end != null) {
                LocalDate startDate = LocalDate.parse(start);
                LocalDate endDate = LocalDate.parse(end);

                List<Operations> operationsListFilter = new ArrayList<>();
                for (Operations o : operationsList) {
                    if (o.getDateOfOperation().isAfter(startDate) && o.getDateOfOperation().isBefore(endDate)) {
                        operationsListFilter.add(o);
                    }
                }
                logger.info("Операции пользователя с " + start + " по " + end);
                return new ResponseEntity<>(operationsListFilter, HttpStatus.OK);
            } else {
                logger.info("Операции пользователя за всё время");
                return new ResponseEntity<>(operationsList, HttpStatus.OK);
            }
        }
        logger.info("Пользователь с ID:" + id + " не найден");
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
