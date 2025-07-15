package net.soufiane.ebankingbackend.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.soufiane.ebankingbackend.Exceptions.BalanceNotSufficientException;
import net.soufiane.ebankingbackend.Exceptions.BankAccountNotFoundException;
import net.soufiane.ebankingbackend.Exceptions.CustomerNotFoundException;
import net.soufiane.ebankingbackend.dtos.*;
import net.soufiane.ebankingbackend.entities.*;
import net.soufiane.ebankingbackend.enums.AccountStatus;
import net.soufiane.ebankingbackend.enums.OperationType;
import net.soufiane.ebankingbackend.mappers.BankAccountMapperImpl;
import net.soufiane.ebankingbackend.repositories.AccountOperationRepository;
import net.soufiane.ebankingbackend.repositories.BankAccountRepository;
import net.soufiane.ebankingbackend.repositories.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankAccountServiceImpl implements BankAccountService {

    private BankAccountRepository bankAccountRepository;
    private CustomerRepository customerRepository;
    private AccountOperationRepository accountOperationRepository;
    private BankAccountMapperImpl dtoMapper;

    //private Logger log = LoggerFactory.getLogger(BankAccountServiceImpl.class().getName());

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("Save customer: {}", customerDTO.getName());
        Customer customer = dtoMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return dtoMapper.fromCustomer(savedCustomer);
    }


    @Override
    public SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interest, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null) {
            log.error("Customer with ID {} not found", customerId);
            throw new CustomerNotFoundException("Customer not found");
        }

        SavingAccount bankAccount = new SavingAccount();;
        log.info("Creating a new Current Account");

        bankAccount.setId(UUID.randomUUID().toString());
        bankAccount.setBalance(initialBalance);
        bankAccount.setCreationDate(new java.util.Date());
        bankAccount.setStatus(AccountStatus.CREATED);
        bankAccount.setInterestRate(interest);
        bankAccount.setCustomer(customer);

        log.info("Saving bank account with ID: {}", bankAccount.getId());
        SavingAccount savedBankAccount = bankAccountRepository.save(bankAccount);

        return dtoMapper.fromSavingBankAccount(savedBankAccount);
    }

    @Override
    public CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {

        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null) {
            log.error("Customer with ID {} not found", customerId);
            throw new CustomerNotFoundException("Customer not found");
        }

        CurrentAccount bankAccount = new CurrentAccount();;
        log.info("Creating a new Current Account");

        bankAccount.setId(UUID.randomUUID().toString());
        bankAccount.setBalance(initialBalance);
        bankAccount.setCreationDate(new java.util.Date());
        bankAccount.setStatus(AccountStatus.CREATED);
        bankAccount.setOverdraft(overDraft);
        bankAccount.setCustomer(customer);

        log.info("Saving bank account with ID: {}", bankAccount.getId());
        CurrentAccount currentAccountSaved = bankAccountRepository.save(bankAccount);

        return dtoMapper.fromCurrentBankAccount(currentAccountSaved);
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        log.info("Get all customers");
        List<Customer> customers = customerRepository.findAll();
        List<CustomerDTO> customerDTOs = new ArrayList<>();
        for (Customer customer : customers) {
            log.info("Customer ID: {}, Name: {}", customer.getId(), customer.getName());
            CustomerDTO customerDTO = dtoMapper.fromCustomer(customer);
            customerDTOs.add(customerDTO);
        }
        /*customers.stream().forEach(customer -> {
            log.info("Customer ID: {}, Name: {}", customer.getId(), customer.getName());
            dtoMapper.fromCustomer(customer);
        });

         */
        return customerDTOs;
    }


    @Override
    public BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException {
        log.info("Get bank account with ID: {}", accountId);
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
            .orElseThrow(() -> new BankAccountNotFoundException("Bank account not found"));


        if( bankAccount instanceof SavingAccount) {
            log.info("Bank account is a Saving Account");
            return dtoMapper.fromSavingBankAccount((SavingAccount) bankAccount);
        } else  {
            log.info("Bank account is a Current Account");
            return dtoMapper.fromCurrentBankAccount((CurrentAccount) bankAccount);
        }
    }

    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException {
        log.info("Debit bank account with ID: {}", accountId);
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new BankAccountNotFoundException("Bank account not found"));
        if(bankAccount.getBalance() < amount)
            throw new BalanceNotSufficientException("Insufficient balance for debit operation");

        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setOperationType(OperationType.DEBIT);
        accountOperation.setAmount(amount);
        accountOperation.setOperationDate(new java.util.Date());
        accountOperation.setDescription(description);
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance() - amount);
        bankAccountRepository.save(bankAccount);



    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException {
        log.info("Credit bank account with ID: {}", accountId);
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new BankAccountNotFoundException("Bank account not found"));

        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setOperationType(OperationType.CREDIT);
        accountOperation.setAmount(amount);
        accountOperation.setOperationDate(new java.util.Date());
        accountOperation.setDescription(description);
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance() + amount);
        bankAccountRepository.save(bankAccount);


    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException {
        debit(accountIdSource, amount, "Transfer to " + accountIdDestination);
        credit(accountIdDestination, amount, "Transfer from " + accountIdSource);
    }

    @Override
    public List<BankAccountDTO> bankAccountsList() {
        List<BankAccount> bankAccounts  = bankAccountRepository.findAll();
        List<BankAccountDTO> bankAccountDTOS =  bankAccounts.stream().map(bankAccount -> {
                if(bankAccount instanceof SavingAccount) {
                    SavingAccount savingAccount = (SavingAccount) bankAccount;
                    return dtoMapper.fromSavingBankAccount(savingAccount);
                }else {
                    CurrentAccount currentAccount = (CurrentAccount) bankAccount;
                    return dtoMapper.fromCurrentBankAccount(currentAccount);
                }

        }).collect(Collectors.toList());
        return bankAccountDTOS;
    }

    @Override
    public CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        log.info("Get customer with ID: {}", customerId);
        CustomerDTO customerDTO = dtoMapper.fromCustomer(customer);
        return customerDTO;
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        log.info("Update customer: {}", customerDTO.getName());
        Customer customer = dtoMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return dtoMapper.fromCustomer(savedCustomer);
    }

    @Override
    public void deleteCustomer(Long customerId) throws CustomerNotFoundException {
    log.info("Delete customer with ID: {}", customerId);
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        customerRepository.delete(customer);
        log.info("Customer with ID: {} deleted successfully", customerId);
    }

    @Override
    public List<AccountOperationDTO> accountHistory(String accountId) throws BankAccountNotFoundException {
        log.info("Get account history for account ID: {}", accountId);
        List<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountId(accountId);
        return accountOperations.stream().map(op->
                dtoMapper.fromAccountOperation(op)).collect(Collectors.toList());

    }

    @Override
    public AccountHistoryDTO getAccountHistory(String bankAccountId, int page, int size) throws BankAccountNotFoundException {
        log.info("Get account history for account ID: {}", bankAccountId);
        BankAccount bankAccount = bankAccountRepository.findById(bankAccountId)
                .orElseThrow(() -> new BankAccountNotFoundException("Bank account not found"));
        Page<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountIdOrderByOperationDateDesc(bankAccountId, PageRequest.of(page, size));
        AccountHistoryDTO accountHistoryDTO = new AccountHistoryDTO();
        /*List<AccountOperationDTO> accountOperationDTOS =  accountOperations.getContent().stream().map(op -> {
            AccountOperationDTO accountOperationDTO = dtoMapper.fromAccountOperation(op);
            return accountOperationDTO;
        }).collect(Collectors.toList());

*/      List<AccountOperationDTO> accountOperationDTOS = accountOperations.getContent().stream().map(op -> dtoMapper.fromAccountOperation(op)).collect(Collectors.toList());
        accountHistoryDTO.setAccountOperationDTOs(accountOperationDTOS);
        accountHistoryDTO.setAccountId(bankAccountId);
        accountHistoryDTO.setBalance(bankAccount.getBalance());
        accountHistoryDTO.setPageSize(size);
        accountHistoryDTO.setCurrentPage(page);
        accountHistoryDTO.setTotalPages(accountOperations.getTotalPages());
        return accountHistoryDTO;


    }

    @Override
    public List<CustomerDTO> searchCustomers(String keyword) {
        log.info("Search customers with keyword: {}", keyword);
        List<Customer> customers = customerRepository.searchCustomers(keyword);
        List<CustomerDTO> customerDTOS = customers.stream().map(customer -> dtoMapper.fromCustomer(customer)).collect(Collectors.toList());
        return customerDTOS;

    }


}
