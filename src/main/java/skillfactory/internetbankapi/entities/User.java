package skillfactory.internetbankapi.entities;

import javax.persistence.*;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "USERS")
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 2, message = "Не меньше 2 знаков")
    private String name;

    @Size(min = 2, message = "Не меньше 2 знаков")
    private String surname;

    private Double balance;

    public User(String name, String surname, Double balance) {
        this.name = name;
        this.surname = surname;
        this.balance = balance;
    }
}
