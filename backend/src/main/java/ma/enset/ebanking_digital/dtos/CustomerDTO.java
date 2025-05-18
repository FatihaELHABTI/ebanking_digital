package ma.enset.ebanking_digital.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
public class CustomerDTO {
    private Long id;
    private String name;
    private String email;
}