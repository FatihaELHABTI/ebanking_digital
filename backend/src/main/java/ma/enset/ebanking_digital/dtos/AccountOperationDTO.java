package ma.enset.ebanking_digital.dtos;

import lombok.Data;
import ma.enset.ebanking_digital.enums.OperationType;

import java.util.Date;

@Data
public class AccountOperationDTO {

    private Long id;
    private Date operationDate;
    private double amount;
    private String description;
    private OperationType type;
    private String accountId; // Added to include the associated account ID
}