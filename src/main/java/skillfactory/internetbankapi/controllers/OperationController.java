package skillfactory.internetbankapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import skillfactory.internetbankapi.entities.Operation;
import skillfactory.internetbankapi.entities.User;
import skillfactory.internetbankapi.repositories.OperationRepository;
import skillfactory.internetbankapi.repositories.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
public class OperationController {

    private final UserRepository userRepository;
    private final OperationRepository operationRepository;

    @Autowired
    public OperationController(UserRepository userRepository, OperationRepository operationRepository) {
        this.userRepository = userRepository;
        this.operationRepository = operationRepository;
    }


    @GetMapping(value = "operation/{id}")
    public List<Operation> getOperationList(@PathVariable(name = "id") Long id,
                                            @RequestParam(value = "start", required = false) LocalDate start,
                                            @RequestParam(value = "end", required = false) LocalDate end) {

        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            {

                if (start != null && end != null) {
                    start.datesUntil(end).forEach(this::);
                }


            }
        }
