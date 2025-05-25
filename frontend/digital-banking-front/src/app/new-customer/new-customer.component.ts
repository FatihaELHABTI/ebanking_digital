import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CustomerService } from '../services/customer.service';
import { Customer } from '../model/customer.model';
import Swal from 'sweetalert2';
import { Router } from '@angular/router';

@Component({
  selector: 'app-new-customer',
  standalone: false,
  templateUrl: './new-customer.component.html',
  styleUrl: './new-customer.component.css'
})
export class NewCustomerComponent implements OnInit {

  // Déclare un formulaire réactif pour gérer les champs du formulaire de création d'un nouveau client
  newCustomerFormGroup! : FormGroup;

  constructor(private fb : FormBuilder, private customerService : CustomerService, private router : Router){}

  ngOnInit(): void {
    this.newCustomerFormGroup = this.fb.group({
      name : this.fb.control(null, [Validators.required, Validators.minLength(4)]),
      email : this.fb.control(null, [Validators.email])
    })
  }

  handleSaveCustomer(){
    if (this.newCustomerFormGroup.invalid) {
      Swal.fire({
        icon: "warning",
        title: "Formulaire invalide",
        text: "Veuillez remplir tous les champs avant de soumettre.",
      });
      return;
    }
    // Récupère tout les données du formulaire et les stocks dans un objet de type Customer
    let customer:Customer = this.newCustomerFormGroup.value;
    this.customerService.saveCustomer(customer).subscribe({
      next : data =>{
        Swal.fire({
          position: "top-end",
          icon: "success",
          title: "Client ajouté avec succès",
          showConfirmButton: false,
          timer: 1500
        });
        //this.newCustomerFormGroup.reset();
        this.router.navigateByUrl("/customers")
      },
      error : err =>{
        Swal.fire({
          icon: "error",
          title: "Erreur",
          text: "Impossible d'ajouter le client !",
          footer: '<a href="#">Voir les détails</a>'
        });
      }
    })
  }

}


/*

this.fb.group({...}) : crée un groupe de champs de formulaire.

Chaque champ comme title, description, etc., est créé avec this.fb.control(null), c'est-à-dire :

Un champ vide au départ (null)

Tu peux ensuite lier ces champs au HTML avec formControlName="title", etc.
*/
