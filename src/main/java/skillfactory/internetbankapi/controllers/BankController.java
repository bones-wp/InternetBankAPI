package skillfactory.internetbankapi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import skillfactory.internetbankapi.entities.EnumOperations;
import skillfactory.internetbankapi.entities.Operations;
import skillfactory.internetbankapi.entities.User;
import skillfactory.internetbankapi.repositories.OperationRepository;
import skillfactory.internetbankapi.repositories.UserRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@RestController
public class BankController {
    private static final Logger logger = LoggerFactory.getLogger(BankController.class);

    private final UserRepository userRepository;
    private final OperationRepository operationRepository;

    @Autowired
    public BankController(UserRepository userRepository, OperationRepository operationRepository) {
        this.userRepository = userRepository;
        this.operationRepository = operationRepository;
    }

    @GetMapping(value = "balance/{id}")
    @Operation(summary = "Запрос баланса пользователя по его ID", description = "Позволяет запросить баланс пользователя по его ID из БД")
    public ResponseEntity<Double> getBalance(@PathVariable(name = "id") Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return new ResponseEntity<>(user.getBalance(), HttpStatus.OK);
        }
        logger.info("Пользователь с ID:" + id + " не найден");
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "balance/{id}")
    @Operation(summary = "Запрос снятия денег с баланса пользователя по его ID", description = "Позволяет снять деньги с баланса пользователя по его ID из БД")
    public ResponseEntity<Double> takeMoney(@PathVariable(name = "id") Long id, @RequestParam("sum") Double sum) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (sum > 0.0) {
                user.setBalance(user.getBalance() - sum);
                userRepository.save(user);
                logger.info("Снятие со счёта пользователя " + user.getName() + " " + sum + " $ прошло успешно!");

                LocalDate date = LocalDate.now();

                Operations operations = new Operations();
                operations.setDateOfOperation(date);
                operations.setSum(sum);
                operations.setOperationType(EnumOperations.TAKE_MONEY);
                operationRepository.save(operations);

                return new ResponseEntity<>(HttpStatus.OK);
            }
            logger.info("Сумма:" + sum + " меньше 0");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Пользователь с ID:" + id + " не найден");
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @PutMapping(value = "balance/{id}")
    @Operation(summary = "Запрос на пополнение баланса пользователя по его ID", description = "Позволяет пополнить баланс пользователя по его ID из БД")
    public ResponseEntity<Double> putMoney(@PathVariable(name = "id") Long id, @RequestParam("sum") Double sum) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (sum > 0.0) {
                user.setBalance(user.getBalance() + sum);
                userRepository.save(user);
                logger.info("Пополнение счёта пользователя " + user.getName() + " на " + sum + " $ прошло успешно!");

                LocalDate date = LocalDate.now();

                Operations operations = new Operations();
                operations.setDateOfOperation(date);
                operations.setSum(sum);
                operations.setOperationType(EnumOperations.PUT_MONEY);
                operationRepository.save(operations);

                return new ResponseEntity<>(HttpStatus.OK);
            }
            logger.info("Сумма:" + sum + " меньше 0");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Пользователь с ID:" + id + " не найден");
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}