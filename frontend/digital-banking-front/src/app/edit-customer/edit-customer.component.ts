import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CustomerService } from '../services/customer.service';
import { Customer } from '../model/customer.model';
import { ActivatedRoute, Router } from '@angular/router';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-edit-customer',
  templateUrl: './edit-customer.component.html',
  styleUrl: './edit-customer.component.css',
  standalone: false,
})
export class EditCustomerComponent implements OnInit {
  editCustomerFormGroup!: FormGroup;
  customerId!: string;

  constructor(
    private fb: FormBuilder,
    private customerService: CustomerService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.customerId = this.route.snapshot.params['id'];
    this.editCustomerFormGroup = this.fb.group({
      name: this.fb.control(null, [Validators.required, Validators.minLength(4)]),
      email: this.fb.control(null, [Validators.email, Validators.required])
    });

    // Fetch customer details
    this.customerService.getCustomer(this.customerId).subscribe({
      next: (customer: Customer) => {
        this.editCustomerFormGroup.patchValue({
          name: customer.name,
          email: customer.email
        });
      },
      error: (err) => {
        Swal.fire({
          icon: 'error',
          title: 'Erreur',
          text: 'Impossible de charger les données du client !'
        });
      }
    });
  }

  handleUpdateCustomer() {
    if (this.editCustomerFormGroup.invalid) {
      Swal.fire({
        icon: 'warning',
        title: 'Formulaire invalide',
        text: 'Veuillez remplir tous les champs correctement.'
      });
      return;
    }

    const customer: Customer = {
      id: parseInt(this.customerId),
      ...this.editCustomerFormGroup.value
    };

    this.customerService.updateCustomer(customer).subscribe({
      next: () => {
        Swal.fire({
          position: 'top-end',
          icon: 'success',
          title: 'Client mis à jour avec succès',
          showConfirmButton: false,
          timer: 1500
        });
        this.router.navigateByUrl('/admin/customers');
      },
      error: (err) => {
        Swal.fire({
          icon: 'error',
          title: 'Erreur',
          text: 'Impossible de mettre à jour le client !'
        });
      }
    });
  }
}
