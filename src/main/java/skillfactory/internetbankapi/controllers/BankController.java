package skillfactory.internetbankapi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import skillfactory.internetbankapi.entities.EnumOperations;
import skillfactory.internetbankapi.entities.Operations;
import skillfactory.internetbankapi.entities.User;
import skillfactory.internetbankapi.services.UserService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
public class BankController {
    private static final Logger logger = LoggerFactory.getLogger(BankController.class);

    private final UserService userService;

    @Autowired
    public BankController(UserService userService) {
        this.userService = userService;
    }

    @Transactional
    @GetMapping(value = "/balance/{id}")
    @Operation(summary = "Запрос баланса пользователя по его ID")
    public ResponseEntity<Double> getBalance(@PathVariable(name = "id") Long id) {
        Optional<User> optionalUser = userService.findUserById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return new ResponseEntity<>(user.getBalance(), HttpStatus.OK);
        }
        logger.info("Пользователь с ID:" + id + " не найден");
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Transactional
    @PostMapping(value = "/balance/{id}")
    @Operation(summary = "Запрос снятия денег с баланса пользователя по его ID")
    public ResponseEntity<Double> takeMoney(@PathVariable(name = "id") Long id, @RequestParam("sum") Double sum) {
        Optional<User> optionalUser = userService.findUserById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            List<Operations> operationsList = user.getOperations();
            if (sum > 0.0 && user.getBalance() - sum > 0.0) {
                user.setBalance(user.getBalance() - sum);

                LocalDate date = LocalDate.now();

                operationsList.add(new Operations(date, EnumOperations.TAKE_MONEY, sum));

                user.setOperations(operationsList);

                userService.save(user);
                logger.info("Снятие со счёта пользователя " + user.getName() + " " + sum + " $ прошло успешно!");

                return new ResponseEntity<>(user.getBalance(), HttpStatus.OK);
            }
            logger.info("Введёная сумма:" + sum + " меньше 0, либо у пользователя не хватает средств на счёте");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Пользователь с ID:" + id + " не найден");
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @Transactional
    @PutMapping(value = "/balance/{id}")
    @Operation(summary = "Запрос на пополнение баланса пользователя по его ID")
    public ResponseEntity<Double> putMoney(@PathVariable(name = "id") Long id, @RequestParam("sum") Double sum) {
        Optional<User> optionalUser = userService.findUserById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            List<Operations> operationsList = user.getOperations();
            if (sum > 0.0) {
                user.setBalance(user.getBalance() + sum);

                LocalDate date = LocalDate.now();

                operationsList.add(new Operations(date, EnumOperations.PUT_MONEY, sum));

                user.setOperations(operationsList);

                userService.save(user);
                logger.info("Пополнение счёта пользователя " + user.getName() + " на " + sum + " $ прошло успешно!");

                return new ResponseEntity<>(user.getBalance(), HttpStatus.OK);
            }
            logger.info("Сумма:" + sum + " меньше 0");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Пользователь с ID:" + id + " не найден");
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @Transactional
    @PostMapping(value = "/transfer/{idFrom}/{idTo}")
    @Operation(summary = "Запрос на перевод денег между пользователями по их ID")
    public ResponseEntity<List<Double>> transferMoney(@PathVariable(name = "idFrom") Long idFrom,
                                                @PathVariable(name = "idTo") Long idTo,@RequestParam("sum") Double sum) {
        Optional<User> optionalUserFrom = userService.findUserById(idFrom);
        Optional<User> optionalUserTo = userService.findUserById(idTo);
        if (optionalUserFrom.isPresent() && optionalUserTo.isPresent()) {
            User userFrom = optionalUserFrom.get();
            User userTo = optionalUserTo.get();
            List<Operations> operationsListFrom = userFrom.getOperations();
            List<Operations> operationsListTo = userTo.getOperations();
            if (sum > 0.0 && userFrom.getBalance() - sum > 0.0) {
                userFrom.setBalance(userFrom.getBalance() - sum);
                userTo.setBalance(userTo.getBalance() + sum);

                LocalDate date = LocalDate.now();

                operationsListFrom.add(new Operations(date, EnumOperations.TRANSFER_FROM_CLIENT, sum));
                operationsListTo.add(new Operations(date, EnumOperations.TRANSFER_TO_CLIENT, sum));

                userFrom.setOperations(operationsListFrom);
                userTo.setOperations(operationsListTo);

                userService.save(userFrom);
                userService.save(userTo);

                List<Double> users = new ArrayList<>(Arrays.asList(userFrom.getBalance(),userTo.getBalance()));

                logger.info("Перевод денег от " + userFrom.getName() + " на счёт " + userTo.getName() + " в размере: " + sum + " $ прошёл успешно!");

                return new ResponseEntity<>(users, HttpStatus.OK);
            }
            logger.info("Введёная сумма:" + sum + " меньше 0, либо у пользователя не хватает средств на счёте");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Пользователь с ID:" + idFrom + "или " + idTo + " не найден");
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}