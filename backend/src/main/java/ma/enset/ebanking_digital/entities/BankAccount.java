package ma.enset.ebanking_digital.entities;

import jakarta.persistence.*;
import lombok.*;
import ma.enset.ebanking_digital.enums.AccountStatus;

import java.util.Date;
import java.util.List;

@Entity
//c'est de JPA, pour heritage (il faut spécifier la stratégie d'héritage utilisé, dans ce cas, c'est "SINGLE_TABLE")
/*
dit a JPA tien toutes les classes qui héritent de cette classe, tu vas me les stocker dans une seule table
 */
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
// spécifier la colone ajouter a la table (dans le cas Single table)
@DiscriminatorColumn(name = "TYPE", length = 4, discriminatorType = DiscriminatorType.STRING)
@Data
@NoArgsConstructor @AllArgsConstructor @Getter @Setter
// on ajoute abstract dans le cas TABLE_PER_CLASS
public class BankAccount {

    @Id
    private String id;
    private double balance;
    private Date createdAt;
    // pour traduire la valeur de status en string
    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @OneToMany(mappedBy = "bankAccount", fetch = FetchType.LAZY)
    private List<AccountOperation>accountOperations;

    @ManyToOne
    private Customer customer;
}