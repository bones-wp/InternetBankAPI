package skillfactory.internetbankapi.entities;

import javax.persistence.*;
import javax.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "userId")
    private List<Operations> operations;

    public User(Long id, String name, String surname, Double balance) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.balance = balance;
    }
}
