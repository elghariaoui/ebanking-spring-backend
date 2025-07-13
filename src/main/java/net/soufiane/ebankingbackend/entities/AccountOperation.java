package net.soufiane.ebankingbackend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.soufiane.ebankingbackend.enums.OperationType;
import java.util.Date;

@Data @NoArgsConstructor @AllArgsConstructor
@Entity
public class AccountOperation {
    @Id @GeneratedValue(strategy  = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    private Date operationDate;
    private double amount;
    @Enumerated(EnumType.STRING)
    private OperationType operationType;
    @ManyToOne
    private BankAccount bankAccount;
    private String description;
}