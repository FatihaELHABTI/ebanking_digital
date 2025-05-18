package ma.enset.ebanking_digital.repositories;



import ma.enset.ebanking_digital.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount,String> {
}