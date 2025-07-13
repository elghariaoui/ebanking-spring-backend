package net.soufiane.ebankingbackend.dtos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.soufiane.ebankingbackend.entities.BankAccount;
import net.soufiane.ebankingbackend.enums.OperationType;

import java.util.Date;

@Data

public class AccountOperationDTO {

    private Long id;
    private Date operationDate;
    private double amount;
    private OperationType operationType;
    private String description;
}