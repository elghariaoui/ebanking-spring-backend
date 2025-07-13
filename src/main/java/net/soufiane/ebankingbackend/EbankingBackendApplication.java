package net.soufiane.ebankingbackend;

import net.soufiane.ebankingbackend.Exceptions.BalanceNotSufficientException;
import net.soufiane.ebankingbackend.Exceptions.BankAccountNotFoundException;
import net.soufiane.ebankingbackend.Exceptions.CustomerNotFoundException;
import net.soufiane.ebankingbackend.dtos.BankAccountDTO;
import net.soufiane.ebankingbackend.dtos.CurrentBankAccountDTO;
import net.soufiane.ebankingbackend.dtos.CustomerDTO;
import net.soufiane.ebankingbackend.dtos.SavingBankAccountDTO;
import net.soufiane.ebankingbackend.entities.*;
import net.soufiane.ebankingbackend.enums.AccountStatus;
import net.soufiane.ebankingbackend.enums.OperationType;
import net.soufiane.ebankingbackend.repositories.AccountOperationRepository;
import net.soufiane.ebankingbackend.repositories.BankAccountRepository;
import net.soufiane.ebankingbackend.repositories.CustomerRepository;
import net.soufiane.ebankingbackend.services.BankAccountService;
import net.soufiane.ebankingbackend.services.BankService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EbankingBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(EbankingBackendApplication.class, args);
	}

	//@Bean
	CommandLineRunner start(CustomerRepository customerRepository,
							BankAccountRepository bankAccountRepository,
							AccountOperationRepository accountOperationRepository) {
		return args -> {
			Stream.of("Hassan", "Soufiane", "Yassine", "Mohamed")
					.forEach(name -> {
						Customer customer = new Customer();
						customer.setName(name);
						customer.setEmail(name.toLowerCase() + "@gmail.com");
						customerRepository.save(customer);
					});
			customerRepository.findAll().forEach(customer -> {
				System.out.println(customer.getName());
				CurrentAccount currentAccount = new CurrentAccount();
				currentAccount.setId(UUID.randomUUID().toString());
				currentAccount.setBalance(Math.random() * 10000);
				currentAccount.setCreationDate(new Date());
				currentAccount.setOverdraft(9000);
				currentAccount.setStatus(AccountStatus.CREATED);
				currentAccount.setCustomer(customer);
				bankAccountRepository.save(currentAccount);

				SavingAccount savingAccount = new SavingAccount();
				savingAccount.setId(UUID.randomUUID().toString());
				savingAccount.setBalance(Math.random() * 9000);
				savingAccount.setCreationDate(new Date());
				savingAccount.setInterestRate(4);
				savingAccount.setStatus(AccountStatus.CREATED);
				savingAccount.setCustomer(customer);
				bankAccountRepository.save(savingAccount);

			});

			bankAccountRepository.findAll().forEach(bankAccount -> {
				for(int i = 0; i < 10; i++) {
					AccountOperation accountOperation = new AccountOperation();
					accountOperation.setOperationDate(new Date());
					accountOperation.setAmount(Math.random() * 12000);
					accountOperation.setOperationType(Math.random() > 0.5 ? OperationType.DEBIT: OperationType.CREDIT);
					accountOperation.setBankAccount(bankAccount);
					accountOperationRepository.save(accountOperation);
				}
			});

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


			System.out.println("Ebanking Backend Application has started successfully!");
		};
	}

	//@Bean
	CommandLineRunner initData(BankService bankService) {
		return args -> {
			bankService.consulter();
		};
	}

	@Bean
	CommandLineRunner commandLineRunner(BankAccountService bankAccountService) {

		return args -> {
			Stream.of("Hassan", "Soufiane", "Mohamed")
					.forEach(name -> {
						CustomerDTO customer = new CustomerDTO();
						customer.setName(name);
						customer.setEmail(name.toLowerCase() + "@gmail.com");
						bankAccountService.saveCustomer(customer);

					});
			bankAccountService.getAllCustomers().forEach(customer -> {

                try {
					bankAccountService.saveCurrentBankAccount(Math.random()*90000, 9000, customer.getId());
                    bankAccountService.saveSavingBankAccount(Math.random()*120000, 5.5, customer.getId());


                } catch (CustomerNotFoundException e) {
                    e.printStackTrace();
                }


            });

			List<BankAccountDTO> bankAccounts = bankAccountService.bankAccountsList();
			for (BankAccountDTO bankAccount : bankAccounts) {
				for(int i = 0; i < 10; i++) {
					String accountId;
					if (bankAccount instanceof SavingBankAccountDTO)
						accountId = ((SavingBankAccountDTO) bankAccount).getId();
					else
						accountId = ((CurrentBankAccountDTO) bankAccount).getId();
					try {
						bankAccountService.credit(accountId, 10000 + Math.random() * 120000, "Credit Operation");
						bankAccountService.debit(accountId, 1000 + Math.random() * 9000, "Debit Operation");
					} catch (BalanceNotSufficientException e) {
						e.printStackTrace();
					}

				}
			}
		};
	}


}
