package ma.enset.ebanking_digital.web;

import lombok.AllArgsConstructor;

import ma.enset.ebanking_digital.dtos.*;
import ma.enset.ebanking_digital.exceptions.*;
import ma.enset.ebanking_digital.repositories.*;
import ma.enset.ebanking_digital.services.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@AllArgsConstructor
@CrossOrigin("*")
public class BankAccountRestController {

    private BankAccountService bankAccountService;
    private BankAccountRepository bankAccountRepository;

    @GetMapping("/accounts/{accountId}")
    public BankAccountDTO getBankAccount(@PathVariable String accountId) throws BankAccountNotFoundException {

        return bankAccountService.getBankAccount(accountId);
    }

    @GetMapping("/accounts")
    public List<BankAccountDTO> listAccounts(){
        return bankAccountService.bankAccountList();
    }

    @GetMapping("/accounts/{accountId}/operations")
    public List<AccountOperationDTO> getHistory(@PathVariable String accountId){
        return bankAccountService.accountHistory(accountId);
    }

    @GetMapping("/accounts/{accountId}/pageOperations")
    public AccountHistoryDTO getAccountHistory(
            @PathVariable String accountId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size) throws BankAccountNotFoundException {
        return bankAccountService.getAccountHistory(accountId, page, size);
    }

    @PostMapping("/accounts/debit")
    public DebitDTO debit(@RequestBody DebitDTO debitDTO) throws BankAccountNotFoundException, BalanceNotSufficientException {
        this.bankAccountService.debit(debitDTO.getAccountId(), debitDTO.getAmount(), debitDTO.getDescription());
        return debitDTO;
    }

    @PostMapping("/accounts/credit")
    public CreditDTO credit(@RequestBody CreditDTO creditDTO) throws BankAccountNotFoundException{
        this.bankAccountService.credit(creditDTO.getAccountId(), creditDTO.getAmount(), creditDTO.getDescription());
        return creditDTO;
    }

    @PostMapping("/accounts/transfer")
    public void transfer(@RequestBody TransferRequestDTO transferRequestDTO) throws BankAccountNotFoundException, BalanceNotSufficientException {
        this.bankAccountService.transfert(
                transferRequestDTO.getAccountSource(),
                transferRequestDTO.getAccountDestination(),
                transferRequestDTO.getAmount());
    }

    @GetMapping("/accounts/count")
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    public long getTotalAccounts() {
        return bankAccountService.getTotalAccounts();
    }

    @GetMapping("/accounts/totalBalance")
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    public double getTotalBalance() {
        return bankAccountService.getTotalBalance();
    }

    @GetMapping("/accounts/recentTransactions")
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    public List<AccountOperationDTO> getRecentTransactions(@RequestParam(name = "size", defaultValue = "5") int size) {
        return bankAccountService.getRecentTransactions(size);
    }

    @GetMapping("/customers/count")
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    public long getTotalCustomers() {
        return bankAccountService.getTotalCustomers();
    }
}

/*
@RequestBody : permet de récupérer automatiquement les données envoyées par le client (en JSON) dans le corps de la requête, et de les convertir en objet Java (ici, un objet DebitDTO).
@RequestParam : Utilisé pour récupérer des paramètres de la requête présents dans l’URL après le "?"
@PathVariable : Utilisé pour extraire une valeur intégrée directement dans le chemin (path) de l’URL.
 */