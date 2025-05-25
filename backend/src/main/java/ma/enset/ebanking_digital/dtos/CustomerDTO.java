package ma.enset.ebanking_digital.dtos;

import lombok.*;


@Data
@Getter @Setter
public class CustomerDTO {

    private Long id;
    private String name;
    private String email;

}