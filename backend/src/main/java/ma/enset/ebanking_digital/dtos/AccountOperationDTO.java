package ma.enset.ebanking_digital.dtos;

import jakarta.persistence.*;
import lombok.*;
import ma.enset.ebanking_digital.enums.OperationType;

import java.util.Date;

@Data
public class AccountOperationDTO {

    private Long id;
    private Date operationDate;
    private double amount;
    private String description;
    private OperationType type;

}