package ma.enset.ebanking_digital.dtos;


import lombok.Data;

@Data
public class TransferRequestDTO {

    private String accountSource;
    private String accountDestination;
    private double amount;
    private String description;
}