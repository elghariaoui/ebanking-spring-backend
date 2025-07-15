package net.soufiane.ebankingbackend.web;

import net.soufiane.ebankingbackend.Exceptions.BalanceNotSufficientException;
import net.soufiane.ebankingbackend.Exceptions.BankAccountNotFoundException;
import net.soufiane.ebankingbackend.dtos.*;
import net.soufiane.ebankingbackend.services.BankAccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class BankAccountRestAPI {
    BankAccountService bankAccountService;

    public BankAccountRestAPI(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @GetMapping("/accounts/{bankAccountId}")
    public BankAccountDTO getBankAccount(@PathVariable String bankAccountId) throws BankAccountNotFoundException {
        return bankAccountService.getBankAccount(bankAccountId);
    }

    @GetMapping("/accounts")
    public List<BankAccountDTO> getBankAccounts() throws BankAccountNotFoundException {
        List<BankAccountDTO> bankAccountDTOS = bankAccountService.bankAccountsList() ;
        return bankAccountDTOS;
    }

    @GetMapping("/accounts/{bankAccountId}/operations")
    public List<AccountOperationDTO> getBankAccountOperations(@PathVariable String bankAccountId) throws BankAccountNotFoundException {
        return bankAccountService.accountHistory(bankAccountId);
    }

    @GetMapping("/accounts/{bankAccountId}/pageOperations")
    public AccountHistoryDTO getBankAccountHistory(@PathVariable String bankAccountId,
                                                         @RequestParam (name = "page", defaultValue = "0") int page,
                                                         @RequestParam (name = "size", defaultValue = "5") int size) throws BankAccountNotFoundException {
        return bankAccountService.getAccountHistory(bankAccountId, page, size);
    }

    @PostMapping("/accounts/debit")
    public DebitDTO debit(@RequestBody DebitDTO debitDTO) throws BankAccountNotFoundException, BalanceNotSufficientException {
        this.bankAccountService.debit(debitDTO.getAccountId(), debitDTO.getAmount(),debitDTO.getDescription());
        return debitDTO;
    }

    @PostMapping("/accounts/credit")
    public CreditDTO credit(@RequestBody CreditDTO creditDTO) throws BankAccountNotFoundException, BalanceNotSufficientException {
        this.bankAccountService.credit(creditDTO.getAccountId(), creditDTO.getAmount(),creditDTO.getDescription());
        return creditDTO;
    }

    @PostMapping("/accounts/transfer")
    public void transfer(@RequestBody TransfertRequestDTO transferDTO) throws BankAccountNotFoundException, BalanceNotSufficientException {
        this.bankAccountService.transfer(transferDTO.getAccountSource(), transferDTO.getAccountDestination(), transferDTO.getAmount());

    }

}
