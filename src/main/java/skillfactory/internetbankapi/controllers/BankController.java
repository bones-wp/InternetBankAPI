package skillfactory.internetbankapi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import skillfactory.internetbankapi.entities.User;
import skillfactory.internetbankapi.repositories.UserRepository;

import java.util.Optional;

@RestController
public class BankController {
    private static final Logger logger = LogManager.getLogger();

    private final UserRepository userRepository;

    @Autowired
    public BankController(UserRepository userRepository) {
        this.userRepository = userRepository;
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
    public ResponseEntity<Double> takeMoney (@PathVariable(name = "id") Long id, @RequestParam("sum") Double sum) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setBalance(user.getBalance() - sum);
            userRepository.save(user);
            logger.info("Снятие со счёта пользователя " + user.getName() + " " + sum + " $ прошло успешно!");
            return new ResponseEntity<>(HttpStatus.OK);
        }
        logger.info("Пользователь с ID:" + id + " не найден");
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @PutMapping(value = "balance/{id}")
    @Operation(summary = "Запрос на пополнение баланса пользователя по его ID", description = "Позволяет пополнить баланс пользователя по его ID из БД")

    public ResponseEntity<Double> putMoney (@PathVariable(name = "id") Long id, @RequestParam("sum") Double sum) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setBalance(user.getBalance() + sum);
            userRepository.save(user);
            logger.info("Пополнение счёта пользователя " + user.getName() + " на " + sum + " $ прошло успешно!");
            return new ResponseEntity<>(HttpStatus.OK);
        }
        logger.info("Пользователь с ID:" + id + " не найден");
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }
}