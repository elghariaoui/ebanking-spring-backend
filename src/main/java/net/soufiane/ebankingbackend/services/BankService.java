package net.soufiane.ebankingbackend.services;

import jakarta.transaction.Transactional;
import net.soufiane.ebankingbackend.entities.BankAccount;
import net.soufiane.ebankingbackend.entities.CurrentAccount;
import net.soufiane.ebankingbackend.entities.SavingAccount;
import net.soufiane.ebankingbackend.repositories.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class BankService  {

    @Autowired
    private BankAccountRepository bankAccountRepository;

    public void consulter() {

        BankAccount bankAccount = bankAccountRepository.findById("06923abd-fd45-421e-b81b-78508a1b9cfe").orElse(null);
        System.out.println("******************************");
        System.out.println("Bank Account Details:" + bankAccount.getClass().getSimpleName());
        System.out.println("Bank Account ID: " + bankAccount.getId());
        System.out.println("Bank Account Balance: " + bankAccount.getBalance());
        System.out.println("Bank Account Customer: " + bankAccount.getCustomer().getName());
        System.out.println("Bank Account Date: " + bankAccount.getCreationDate());
        System.out.println("Bank Account Status: " + bankAccount.getStatus());
        if (bankAccount instanceof SavingAccount) {
            System.out.println("Interest Rate: " + ((SavingAccount) bankAccount).getInterestRate());
        } else if (bankAccount instanceof CurrentAccount) {
            System.out.println("Overdraft: " + ((CurrentAccount) bankAccount).getOverdraft());
        }
        bankAccount.getAccountOperations().forEach(accountOperation -> {

            System.out.println("============================");
            System.out.println("Operation ID: " + accountOperation.getId());
            System.out.println("Operation Date: " + accountOperation.getOperationDate());
            System.out.println("Operation Amount: " + accountOperation.getAmount());
            System.out.println("Operation Type: " + accountOperation.getOperationType());
        });

    }


}
