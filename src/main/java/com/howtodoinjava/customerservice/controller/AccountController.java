package com.howtodoinjava.customerservice.controller;

import com.howtodoinjava.customerservice.domin.Account;
import com.howtodoinjava.customerservice.domin.Customer;
import com.howtodoinjava.customerservice.repository.AccountRepository;
import com.howtodoinjava.customerservice.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/customers")
public class AccountController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountRepository accountRepository;

    @PostMapping(value = "/{customerId}/accounts")
    @ResponseStatus(code = HttpStatus.CREATED)
    public Account save(@PathVariable Integer customerId, @RequestBody Account account) {
        return customerRepository.findById(customerId).map(customer -> {
            account.setCustomer(customer);
            return accountRepository.save(account);

        }).orElseThrow(() -> new RuntimeException("Customer [customerId=" + customerId + "] can't be found"));

    }

    @GetMapping(value = "/{customerId}/accounts")
    public Page<Account> all(@PathVariable Integer customerId, Pageable pageable) {
        return accountRepository.findByCustomerCustomerId(customerId, pageable);
    }

    @DeleteMapping(value = "/{customerId}/accounts/{accountId}")
    public ResponseEntity<?> deleteAccount(@PathVariable Integer customerId, @PathVariable Integer accountId) {

        if (!customerRepository.existsById(customerId)) {
            throw new RuntimeException("Customer [customerId=" + customerId + "] can't be found");
        }

        return accountRepository.findById(accountId).map(account -> {
            accountRepository.delete(account);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new RuntimeException("Account [accountId=" + accountId + "] can't be found"));

    }

    @PutMapping(value = "/{customerId}/accounts/{accountId}")
    public ResponseEntity<Account> updateAccount(@PathVariable Integer customerId, @PathVariable Integer accountId, @RequestBody Account newAccount) {

        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new RuntimeException("Customer [customerId=" + customerId + "] can't be found"));

        return accountRepository.findById(accountId).map(account -> {
            newAccount.setCustomer(customer);
            accountRepository.save(newAccount);
            return ResponseEntity.ok(newAccount);
        }).orElseThrow(() -> new RuntimeException("Account [accountId=" + accountId + "] can't be found"));


    }

}