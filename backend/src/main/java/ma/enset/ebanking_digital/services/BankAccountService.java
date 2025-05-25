package ma.enset.ebanking_digital.services;


import jakarta.transaction.Transactional;

import ma.enset.ebanking_digital.dtos.*;
import ma.enset.ebanking_digital.exceptions.*;
import org.springframework.stereotype.Service;

import java.util.List;

public interface BankAccountService {

    CustomerDTO saveCustomer(CustomerDTO customerDTO);
    CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId)throws CustomerNotFoundException;
    SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId)throws CustomerNotFoundException;
    List<CustomerDTO> listCustomers();
    BankAccountDTO getBankAccount(String accountId)throws BankAccountNotFoundException;
    void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException;
    void credit(String accountId, double amount, String description) throws BankAccountNotFoundException;
    void transfert(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException;

    List<BankAccountDTO> bankAccountList();
    public CustomerDTO getCustomer(Long accountId) throws CustomerNotFoundException;

    // Enregistre un nouveau client dans la base de données.
    CustomerDTO updateCustomer(CustomerDTO customerDTO);

    void deleteCustomer(Long customerId);

    List<AccountOperationDTO> accountHistory(String accountId);

    AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException;

    List<CustomerDTO> searchCustomer(String keyword);

    List<BankAccountDTO> bankAccountCustomer(Long customerId);
    long getTotalAccounts();
    double getTotalBalance();
    List<AccountOperationDTO> getRecentTransactions(int size);
    long getTotalCustomers();
}