package ma.enset.ebanking_digital.dtos;



import lombok.*;
import ma.enset.ebanking_digital.enums.AccountStatus;

import java.util.Date;

@Data
@Getter @Setter
public class CurrentBankAccountDTO extends BankAccountDTO {

    private String id;
    private double balance;
    private Date createdAt;
    private AccountStatus status;
    private CustomerDTO customerDTO;
    private double overDraft;
}