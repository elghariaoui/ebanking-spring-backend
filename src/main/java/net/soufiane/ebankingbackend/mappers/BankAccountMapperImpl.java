package net.soufiane.ebankingbackend.mappers;

import net.soufiane.ebankingbackend.dtos.AccountOperationDTO;
import net.soufiane.ebankingbackend.dtos.CurrentBankAccountDTO;
import net.soufiane.ebankingbackend.dtos.CustomerDTO;
import net.soufiane.ebankingbackend.dtos.SavingBankAccountDTO;
import net.soufiane.ebankingbackend.entities.AccountOperation;
import net.soufiane.ebankingbackend.entities.CurrentAccount;
import net.soufiane.ebankingbackend.entities.Customer;
import net.soufiane.ebankingbackend.entities.SavingAccount;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;


@Service
public class BankAccountMapperImpl {
    public CustomerDTO fromCustomer(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer, customerDTO);
        return customerDTO;
    }

    public Customer fromCustomerDTO(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        //customer.setId(customerDTO.getId());
        //customer.setName(customerDTO.getName());
        //customer.setEmail(customerDTO.getEmail());
        BeanUtils.copyProperties(customerDTO, customer);
        return customer;
    }

    public SavingBankAccountDTO fromSavingBankAccount(SavingAccount savingBankAccount) {
        SavingBankAccountDTO dto = new SavingBankAccountDTO();
        BeanUtils.copyProperties(savingBankAccount, dto);
        dto.setCustomerDTO(fromCustomer(savingBankAccount.getCustomer()));
        dto.setType(savingBankAccount.getClass().getSimpleName());
        return dto;
    }

    public SavingAccount fromSavingBankAccountDTO(SavingBankAccountDTO dto) {
        SavingAccount savingBankAccount = new SavingAccount();
        BeanUtils.copyProperties(dto, savingBankAccount);
        savingBankAccount.setCustomer(fromCustomerDTO(dto.getCustomerDTO()));

        return savingBankAccount;
    }

    public CurrentBankAccountDTO fromCurrentBankAccount(CurrentAccount currentBankAccount) {
        CurrentBankAccountDTO dto = new CurrentBankAccountDTO();
        BeanUtils.copyProperties(currentBankAccount, dto);
        dto.setCustomerDTO(fromCustomer(currentBankAccount.getCustomer()));
        dto.setType(currentBankAccount.getClass().getSimpleName());
        return dto;
    }

    public CurrentAccount fromCurrentBankAccountDTO(CurrentBankAccountDTO dto) {
        CurrentAccount currentBankAccount = new CurrentAccount();
        BeanUtils.copyProperties(dto, currentBankAccount);
        currentBankAccount.setCustomer(fromCustomerDTO(dto.getCustomerDTO()));
        return currentBankAccount;
    }

    public AccountOperationDTO fromAccountOperation(AccountOperation accountOperation) {
        AccountOperationDTO accountOperationDTO = new AccountOperationDTO();
        BeanUtils.copyProperties(accountOperation, accountOperationDTO);
        return accountOperationDTO;
    }

    public AccountOperation fromAccountOperationDTO(AccountOperationDTO accountOperationDTO) {
        AccountOperation accountOperation = new AccountOperation();
        BeanUtils.copyProperties(accountOperationDTO, accountOperation);
        // If you need to set the bank account, you can do it here
        //accountOperation.setBankAccount(fromBankAccountDTO(accountOperationDTO.getBankAccountDTO()));
        return accountOperation;


    }
}
