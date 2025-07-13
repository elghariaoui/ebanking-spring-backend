package net.soufiane.ebankingbackend.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.soufiane.ebankingbackend.Exceptions.CustomerNotFoundException;
import net.soufiane.ebankingbackend.dtos.CustomerDTO;
import net.soufiane.ebankingbackend.entities.Customer;
import net.soufiane.ebankingbackend.services.BankAccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:4200") // Allow requests from Angular frontend
//@RequestMapping("/customers")
public class CustomerRestController {

    private BankAccountService bankAccountService;

    @GetMapping("/customers")
    public List<CustomerDTO> getCustomers() {
        log.info("Fetching all customers");
        List<CustomerDTO> customers = bankAccountService.getAllCustomers();
        if (customers.isEmpty()) {
            log.warn("No customers found");
        } else {
            log.info("Found {} customers", customers.size());
        }
        return customers;
    }


    @GetMapping("/customers/search")
    public List<CustomerDTO> searchCustomers(@RequestParam (name = "keyword", defaultValue = "") String keyword) {
        return bankAccountService.searchCustomers("%"+keyword+"%");
    }

    @GetMapping("/customers/{id}")
    public CustomerDTO getCustomerById(@PathVariable(name = "id") Long customerId) throws CustomerNotFoundException {
        log.info("Fetching customer with ID: {}", customerId);
        return bankAccountService.getCustomer(customerId);
    }

    @PostMapping("/customers")
    public CustomerDTO addCustomer(@RequestBody CustomerDTO customerDTO) throws CustomerNotFoundException {
        log.info("Adding new customer: {}", customerDTO.getName());
        CustomerDTO customer = bankAccountService.saveCustomer(customerDTO);
        log.info("Customer added with ID: {}", customer.getId());
        return bankAccountService.getCustomer(customer.getId());
    }

    @PutMapping("/customers/{id}")
    public CustomerDTO updateCustomer(@PathVariable(name = "id") Long customerId, @RequestBody CustomerDTO customerDTO) throws CustomerNotFoundException {
        log.info("Updating customer with ID: {}", customerId);
        customerDTO.setId(customerId);
        CustomerDTO updatedCustomer = bankAccountService.updateCustomer(customerDTO);
        log.info("Customer updated: {}", updatedCustomer.getName());
        return updatedCustomer;
    }

    @DeleteMapping("/customers/{id}")
    public void deleteCustomer(@PathVariable Long id) throws CustomerNotFoundException {
        log.info("Deleting customer with ID: {}", id);
        bankAccountService.deleteCustomer(id);
        log.info("Customer with ID: {} deleted successfully", id);
    }

}
