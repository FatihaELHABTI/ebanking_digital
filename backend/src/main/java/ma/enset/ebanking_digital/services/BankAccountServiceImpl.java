package ma.enset.ebanking_digital.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import ma.enset.ebanking_digital.dtos.*;
import ma.enset.ebanking_digital.entities.*;
import ma.enset.ebanking_digital.enums.OperationType;
import ma.enset.ebanking_digital.exceptions.*;
import ma.enset.ebanking_digital.mappers.BankAccountMapperImpl;
import ma.enset.ebanking_digital.repositories.*;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@Transactional
@AllArgsConstructor
// ajouter un attribut log pour loger les messages
@Slf4j
public class BankAccountServiceImpl implements BankAccountService {

    private final BeanNameAware beanNameAware;
    CustomerRepository customerRepository;
    BankAccountRepository bankAccountRepository;
    AccountOperationRepository accountOperationRepository;
    private BankAccountMapperImpl dtoMapper;

    //***************************************************************************************

    // Enregistre un nouveau client dans la base de données.
    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("Saving new Customer");
        Customer customer = dtoMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return dtoMapper.fromCustomer(savedCustomer);
    }

    //***************************************************************************************

    //  Crée et enregistre un compte courant (Compte pour les dépenses quotidiennes) pour un client donné.
    @Override
    public CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {

        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null) {
            throw new CustomerNotFoundException("Customer not found");
        }
        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreatedAt(new Date());
        currentAccount.setBalance(initialBalance);
        currentAccount.setOverDraft(overDraft);
        currentAccount.setCustomer(customer);
        CurrentAccount savedAccount =  bankAccountRepository.save(currentAccount);
        return dtoMapper.fromCurrentBankAccount(savedAccount);
    }

    //***************************************************************************************

    // Crée et enregistre un compte épargne (Compte pour économiser de l’argent) pour un client donné.
    @Override
    public SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null) {
            throw new CustomerNotFoundException("Customer not found");
        }
        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreatedAt(new Date());
        savingAccount.setBalance(initialBalance);
        savingAccount.setInterestRate(interestRate);
        savingAccount.setCustomer(customer);
        SavingAccount savedAccount =  bankAccountRepository.save(savingAccount);
        return dtoMapper.fromSavingBankAccount(savedAccount);
    }

    //***************************************************************************************

    // Récupère la liste de tous les clients enregistrés.
    @Override
    public List<CustomerDTO> listCustomers() {

        List<Customer> customers = customerRepository.findAll();
        List<CustomerDTO> customerDTOS =  customers.stream().map(customer -> dtoMapper.fromCustomer(customer)).collect(Collectors.toList());

        /*
        List<CustomerDTO> customerDTOS = new ArrayList<>();
        for (Customer customer : customers) {
            CustomerDTO customerDTO = dtoMapper.fromCustomer(customer);
            customerDTOS.add(customerDTO);
        }
         */

        return customerDTOS;
    }

    // Recherche un compte bancaire par son identifiant.
    @Override
    public BankAccountDTO getBankAccount(String accountId)throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).
                orElseThrow(() -> new BankAccountNotFoundException("BankAccount not found"));
        if (bankAccount instanceof SavingAccount){
            SavingAccount savingAccount = (SavingAccount) bankAccount;
            return dtoMapper.fromSavingBankAccount(savingAccount);
        } else {
            CurrentAccount currentAccount = (CurrentAccount) bankAccount;
            return dtoMapper.fromCurrentBankAccount(currentAccount);
        }
    }

    //***************************************************************************************

    //  Effectue une opération de débit (Sortie d’argent du compte) sur un compte bancaire donné.
    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).
                orElseThrow(() -> new BankAccountNotFoundException("BankAccount not found"));
        if (bankAccount.getBalance() < amount) {
            throw new BalanceNotSufficientException("Balance not Sufficient");
        }
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance() - amount);
        bankAccountRepository.save(bankAccount);
    }

    //***************************************************************************************

    // Effectue une opération de crédit (Entrée d’argent dans le compte) sur un compte bancaire donné.
    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).
                orElseThrow(() -> new BankAccountNotFoundException("BankAccount not found"));

        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance() + amount);
        bankAccountRepository.save(bankAccount);
    }

    //***************************************************************************************

    // Effectue une opération de crédit sur un compte bancaire donné.
    @Override
    public void transfert(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException {
        debit(accountIdSource, amount, "Transfer to "+accountIdDestination);
        credit(accountIdDestination, amount, "Transfer from "+accountIdSource);
    }

    //***************************************************************************************

    @Override
    public List<BankAccountDTO> bankAccountList(){
        List<BankAccount> bankAccounts = bankAccountRepository.findAll();
        List<BankAccountDTO> bankAccountDTOS = bankAccounts.stream().map(bankAccount -> {
            if (bankAccount instanceof SavingAccount) {
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
    public CustomerDTO getCustomer(Long accountId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(accountId)
                .orElseThrow(()-> new CustomerNotFoundException("Customer not found"));
        return dtoMapper.fromCustomer(customer);
    }

    //***************************************************************************************

    // Enregistre un nouveau client dans la base de données.
    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        log.info("Saving new Customer");
        Customer customer = dtoMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return dtoMapper.fromCustomer(savedCustomer);
    }

    //***************************************************************************************

    @Override
    public void deleteCustomer(Long customerId){
        customerRepository.deleteById(customerId);
    }

    //***************************************************************************************

    @Override
    public List<AccountOperationDTO> accountHistory(String accountId){
        List<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountId(accountId);
        return accountOperations.stream().map(op -> dtoMapper.fromAccountOperation(op)).collect(Collectors.toList());
    }

    //***************************************************************************************

    @Override
    public AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElse(null);
        if (bankAccount == null) {
            throw new BankAccountNotFoundException("Account not found");
        }
        Page<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountIdOrderByOperationDateDesc(accountId, PageRequest.of(page, size));
        AccountHistoryDTO accountHistoryDTO = new AccountHistoryDTO();

        List<AccountOperationDTO> accountOperationDTOS = accountOperations.getContent().stream().map(op -> dtoMapper.fromAccountOperation(op)).collect(Collectors.toList());
        accountHistoryDTO.setAccountOperationDTOS(accountOperationDTOS);
        accountHistoryDTO.setAccountId(bankAccount.getId());
        accountHistoryDTO.setBalance(bankAccount.getBalance());
        accountHistoryDTO.setCurrentPage(page);
        accountHistoryDTO.setPageSize(size);
        accountHistoryDTO.setTotalPages(accountOperations.getTotalPages());

        return accountHistoryDTO;
    }

    @Override
    public List<CustomerDTO> searchCustomer(String keyword){
        List<Customer> customers = customerRepository.searchCustomer(keyword);
        List<CustomerDTO> customerDTOS = customers.stream().map(customer -> dtoMapper.fromCustomer(customer)).collect(Collectors.toList());
        return customerDTOS;
    }



    //**************************************

    @Override
    public List<BankAccountDTO> bankAccountCustomer(Long customerId) {
        List<BankAccount> bankAccounts = bankAccountRepository.findByCustomerId(customerId);
        List<BankAccountDTO> bankAccountDTOS = bankAccounts.stream().map(bankAccount -> {
            if (bankAccount instanceof SavingAccount) {
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
    public long getTotalAccounts() {
        log.info("Fetching total number of accounts");
        return bankAccountRepository.count();
    }

    @Override
    public double getTotalBalance() {
        log.info("Calculating total balance across all accounts");
        List<BankAccount> bankAccounts = bankAccountRepository.findAll();
        return bankAccounts.stream()
                .mapToDouble(BankAccount::getBalance)
                .sum();
    }

    @Override
    public List<AccountOperationDTO> getRecentTransactions(int size) {
        log.info("Fetching recent transactions with size: {}", size);
        Page<AccountOperation> recentOperations = accountOperationRepository.findAll(
                PageRequest.of(0, size).withSort(org.springframework.data.domain.Sort.by("operationDate").descending())
        );
        return recentOperations.getContent().stream()
                .map(dtoMapper::fromAccountOperation)
                .collect(Collectors.toList());
    }

    @Override
    public long getTotalCustomers() {
        log.info("Fetching total number of customers");
        return customerRepository.count();
    }
}