package net.soufiane.ebankingbackend.dtos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.soufiane.ebankingbackend.entities.AccountOperation;
import net.soufiane.ebankingbackend.entities.Customer;
import net.soufiane.ebankingbackend.enums.AccountStatus;

import java.util.Date;
import java.util.List;


@Data

public class SavingBankAccountDTO extends BankAccountDTO {

    private double interestRate;
}