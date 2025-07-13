package net.soufiane.ebankingbackend.services;

import net.soufiane.ebankingbackend.Exceptions.BalanceNotSufficientException;
import net.soufiane.ebankingbackend.Exceptions.BankAccountNotFoundException;
import net.soufiane.ebankingbackend.Exceptions.CustomerNotFoundException;
import net.soufiane.ebankingbackend.dtos.*;
import net.soufiane.ebankingbackend.entities.BankAccount;
import net.soufiane.ebankingbackend.entities.CurrentAccount;
import net.soufiane.ebankingbackend.entities.Customer;
import net.soufiane.ebankingbackend.entities.SavingAccount;

import java.util.List;

public interface BankAccountService {

    CustomerDTO saveCustomer(CustomerDTO customerDTO);
    CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException;
    SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interest, Long customerId) throws CustomerNotFoundException;
    List<CustomerDTO> getAllCustomers();
    BankAccountDTO getBankAccount(String AccountId) throws BankAccountNotFoundException;
    void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException;
    void credit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException;
    void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException;
    List<BankAccountDTO> bankAccountsList();

    CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException;

    CustomerDTO updateCustomer(CustomerDTO customerDTO);

    void deleteCustomer(Long customerId) throws CustomerNotFoundException;

    List<AccountOperationDTO> accountHistory(String accountId) throws BankAccountNotFoundException;

    AccountHistoryDTO getAccountHistory(String bankAccountId, int page, int size) throws BankAccountNotFoundException;

    List<CustomerDTO> searchCustomers(String keyword);
}
