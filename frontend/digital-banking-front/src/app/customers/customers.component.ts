import { CustomerService } from './../services/customer.service';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { catchError, Observable, throwError } from 'rxjs';
import { Customer } from '../model/customer.model';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-customers',
  templateUrl: './customers.component.html',
  styleUrl: './customers.component.css',
  standalone: false,
})
export class CustomersComponent implements OnInit {
  customers!: Observable<Array<Customer>>;
  errorMessage!: string;
  searchFormGroup: FormGroup | undefined;

  constructor(
    private customerService: CustomerService,
    private fb: FormBuilder,
    public authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.searchFormGroup = this.fb.group({
      keyword: this.fb.control(''),
    });
    this.handleSearchCustomers();
  }

  getCustomers() {
    this.customers = this.customerService.getCustomers().pipe(
      catchError((err) => {
        this.errorMessage = err.message;
        return throwError(err);
      })
    );
  }

  handleSearchCustomers() {
    let kw = this.searchFormGroup?.value.keyword;
    this.customers = this.customerService.serachCustomers(kw).pipe(
      catchError((err) => {
        this.errorMessage = err.message;
        return throwError(err);
      })
    );
  }

  onEditCustomer(customer: Customer) {
    console.log('Navigating to edit customer:', customer.id); // Debug log
    this.router.navigateByUrl(`/admin/edit-customer/${customer.id}`, { state: customer });
  }

  onDeleteCustomer(customer: Customer) {
    this.customerService.deleteCustomer(customer.id).subscribe({
      next: () => {
        this.handleSearchCustomers();
      },
    });
  }

  handleCustomerAccounts(customer: Customer) {
    this.router.navigateByUrl('/customer-accounts/' + customer.id, { state: customer });
  }
}
