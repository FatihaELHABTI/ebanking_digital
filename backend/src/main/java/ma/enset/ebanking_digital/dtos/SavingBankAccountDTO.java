package ma.enset.ebanking_digital.dtos;

import jakarta.persistence.*;
import lombok.*;
import ma.enset.ebanking_digital.enums.AccountStatus;

import java.util.Date;

@Data
public class SavingBankAccountDTO extends BankAccountDTO {

    private String id;
    private double balance;
    private Date createdAt;
    private AccountStatus status;
    private CustomerDTO customerDTO;
    private double interestRate;
}