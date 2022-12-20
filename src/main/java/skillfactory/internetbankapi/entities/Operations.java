package skillfactory.internetbankapi.entities;

import javax.persistence.*;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "OPERATIONS")
@Data
public class Operations {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dateOfOperation;

    @Enumerated(EnumType.STRING)
    private EnumOperations operationType;

    private Double sum;

    public Operations() {
    }

    public Operations(LocalDate dateOfOperation, EnumOperations operationType, Double sum) {
        this.dateOfOperation = dateOfOperation;
        this.operationType = operationType;
        this.sum = sum;
    }
}
