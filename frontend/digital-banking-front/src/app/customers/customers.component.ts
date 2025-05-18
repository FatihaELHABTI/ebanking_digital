import { Component, OnInit } from '@angular/core';
import { CustomerService } from '../services/customer.service';
import { Customer } from '../model/customer.model';
import { catchError, filter, map, Observable, throwError } from 'rxjs';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-customers',
  standalone: false,
  templateUrl: './customers.component.html',
  styleUrl: './customers.component.css',
})
export class CustomersComponent implements OnInit {
  customers!: Observable<Array<Customer>>;
  errorMessage!: string;
  searchFormGroup!: FormGroup;
  constructor(private customerService: CustomerService, private fb: FormBuilder, private router: Router) { }

  ngOnInit(): void {
    this.searchFormGroup = this.fb.group({
      keyword: this.fb.control("")
    });
    this.handleSearchCustomers();
  }

  handleSearchCustomers() {
    let keyword = this.searchFormGroup.value.keyword;
    this.customers = this.customerService.searchCustomers(keyword).pipe(
      catchError(err => {
        this.errorMessage = err.message;
        return throwError(err)
      })
    );
  }

  handleDeleteCustomer(customer: Customer) {
    let conf = confirm('do you want to delete customer ' + customer.name);
    if (!conf) return;
    this.customerService.deleteCustomer(customer.id).subscribe(
      {
        next: (response) => {
          this.customers = this.customers.pipe(
            map((customers) => customers.filter((item) => item.id !== customer.id)),
          );
        },
        error: (error) => console.log(error)
      }
    );
  }

  handleCustomerAccounts(customer: Customer) {
    this.router.navigateByUrl('/customer-accounts/'+customer.id, {state: customer});
  }

}
