package ma.enset.ebanking_digital.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import ma.enset.ebanking_digital.dtos.*;
import ma.enset.ebanking_digital.exceptions.*;
import ma.enset.ebanking_digital.services.BankAccountService;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// c'est un web service
@RestController
@AllArgsConstructor
@Slf4j
// Permet de gérer les requêtes provenant de domaines différents (CORS - Cross-Origin Resource Sharing)
@CrossOrigin("*") // autorisé tout les domaines
public class CustomerRestController {

    private BankAccountService bankAccountService;

    @GetMapping("/customers")
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    public List<CustomerDTO> customers(){
        return bankAccountService.listCustomers();
    }

    @GetMapping("/customers/{id}")
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    public CustomerDTO getCustomer(@PathVariable(name = "id") Long customerId) throws CustomerNotFoundException {
        return bankAccountService.getCustomer(customerId);
    }

    @PostMapping("/customers")
    @PostAuthorize("hasAuthority('SCOPE_ADMIN')")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO) {
        return bankAccountService.saveCustomer(customerDTO);
    }

    @PutMapping("/customers/{customerId}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public CustomerDTO updateCustomer(@PathVariable Long customerId,@RequestBody CustomerDTO customerDTO) {
        customerDTO.setId(customerId);
        return bankAccountService.updateCustomer(customerDTO);
    }

    @DeleteMapping("/customers/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public void deleteCustomer(@PathVariable Long id){
        bankAccountService.deleteCustomer(id);
    }

    @GetMapping("/customers/search")
    public List<CustomerDTO> searchCustomer(@RequestParam(name = "keyword", defaultValue = "") String keyword) {
        return bankAccountService.searchCustomer("%"+keyword+"%");
    }
}


/*
@RequestBody CustomerDTO request : indiquer a spring comme quoi les données de customerDTO on va
les récupérer a partir du corps de la requétte (@RequestBody) en format Json.

@PreAuthorize : Elle permet de contrôler l'accès à un endpoint REST ou une méthode selon les autorités
(roles/permissions) contenues dans le token JWT.
 */