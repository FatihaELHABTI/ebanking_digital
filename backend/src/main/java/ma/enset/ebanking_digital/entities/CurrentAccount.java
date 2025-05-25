package ma.enset.ebanking_digital.entities;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
/*
quand je crée un CurrentAccount, la dans la clone type, il est affect comme valeur
CurrentAccount (il doit avoir maximum 4 caractère ex : CA et l'autre CC ) (dans le cas Single table)
 */
@DiscriminatorValue("CA")
@Data
@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class CurrentAccount extends BankAccount{

    private double overDraft;
}