import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CustomerService } from '../services/customer.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-new-customer',
  standalone: false,
  templateUrl: './new-customer.component.html',
  styleUrl: './new-customer.component.css'
})
export class NewCustomerComponent implements OnInit {

  newCustomerFormGroup!: FormGroup;
  constructor(private customerService: CustomerService, private fb: FormBuilder, private router: Router) { }

  ngOnInit(): void {
    this.newCustomerFormGroup = this.fb.group({
      name: this.fb.control("", [Validators.required, Validators.minLength(5)]),
      email: this.fb.control("", [Validators.required, Validators.email])
    });
  }

  handleSaveCustomer() {
    let customer = this.newCustomerFormGroup.value;
    this.customerService.saveCustomer(customer).subscribe({
      next: (data) => {
        alert('customer has been saved successfully');
        // this.newCustomerFormGroup.reset();
        this.router.navigateByUrl("/customers");
      },
      error: (error) => {
        console.log(error);
      }
    });
  }
}
