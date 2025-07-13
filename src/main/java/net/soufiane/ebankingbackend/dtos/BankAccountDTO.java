package net.soufiane.ebankingbackend.dtos;

import lombok.Data;
import net.soufiane.ebankingbackend.enums.AccountStatus;

import java.util.Date;


@Data
public class BankAccountDTO {

    private String id;
    private double balance;
    private Date creationDate;
    private AccountStatus status;
    private CustomerDTO customerDTO;
    private String type; // "CurrentAccount" or "SavingAccount"

}