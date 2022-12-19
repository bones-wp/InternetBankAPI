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
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
public class Operation {
    @Transient
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dateOfOperation;
    private Enum<EnumOperations> operationType;
    private Double sum;

    public Operation(Enum<EnumOperations> operationType, Double sum) {
        this.dateOfOperation = LocalDate.parse(LocalDate.now().format(dateFormatter));
        this.operationType = operationType;
        this.sum = sum;
    }
}
