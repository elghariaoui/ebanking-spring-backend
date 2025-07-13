package net.soufiane.ebankingbackend.dtos;

import lombok.Data;
import net.soufiane.ebankingbackend.enums.AccountStatus;

import java.util.Date;


@Data
public class CurrentBankAccountDTO extends BankAccountDTO {

    private double overdraft;
}