package com.elmakaoui.ebankservice.services;

import com.elmakaoui.ebankservice.entities.BankAccount;
import com.elmakaoui.ebankservice.feign.CustomerRestClient;
import com.elmakaoui.ebankservice.model.Customer;
import com.elmakaoui.ebankservice.repository.BankAccountRepository;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class EbankService {
    private BankAccountRepository accountRepository;
    private CustomerRestClient customerRestClient;

    public EbankService(BankAccountRepository accountRepository, CustomerRestClient customerRestClient) {
        this.accountRepository = accountRepository;
        this.customerRestClient = customerRestClient;
    }

    @McpTool(description = "Get all bank accounts")
    public List<BankAccount> getAllBankAccounts(){
        return accountRepository.findAll();
    }

    @McpTool(description = "Find a bank account by id")
    public BankAccount getBankAccountById(@McpToolParam(description = "The bank account id") String id){
        BankAccount bankAccount = accountRepository.findById(id).orElseThrow(()-> new RuntimeException("Bank Account Not Fund"));
        bankAccount.setCustomer(customerRestClient.getCustomerById(bankAccount.getCustomerId()));
        return bankAccount;
    }

    @McpTool(description = "Save a new bank account ")
    public BankAccount save(@McpToolParam(description = "The bank account to save (balance, type, customerId)") BankAccount bankAccount){
        try{
            Customer customer = customerRestClient.getCustomerById(bankAccount.getCustomerId());
            bankAccount.setId(UUID.randomUUID().toString());
            bankAccount.setCreatedAt(new Date());
            return accountRepository.save(bankAccount);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}

