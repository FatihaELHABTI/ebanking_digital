package ma.enset.ebanking_digital.dtos;


import lombok.Data;

@Data
public class CreditDTO {

    private String accountId;
    private double amount;
    private String description;
}