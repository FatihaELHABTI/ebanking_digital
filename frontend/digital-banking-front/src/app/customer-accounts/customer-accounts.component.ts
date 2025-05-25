import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Customer } from '../model/customer.model';
import { AccountsService } from '../services/account.service';
import { Observable, catchError, throwError } from 'rxjs';
import { AuthService } from '../services/auth.service';
import { BankAccount } from '../model/account.model';

@Component({
  selector: 'app-customer-accounts',
  standalone: false,
  templateUrl: './customer-accounts.component.html',
  styleUrl: './customer-accounts.component.css'
})
export class CustomerAccountComponent implements OnInit {
  customerId!: string;
  customer!: Customer;
  accountsObservable!: Observable<BankAccount[]>;
  errorMessage!: string;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private accountService: AccountsService,
    public authService: AuthService
  ) {
    this.customer = this.router.getCurrentNavigation()?.extras.state as Customer;
  }

  ngOnInit(): void {
    this.customerId = this.route.snapshot.params['id'];
    this.loadAccounts();
  }

  loadAccounts() {
    this.accountsObservable = this.accountService.getCustomerAccounts(this.customerId).pipe(
      catchError(err => {
        this.errorMessage = err.message;
        return throwError(err);
      })
    );
  }

  viewAccountDetails(accountId: string) {
    this.router.navigateByUrl(`/admin/accounts`, { state: { accountId } });
  }
}
