import { Component, OnInit } from '@angular/core';
import { CustomerService } from '../services/customer.service';
import { AccountsService } from '../services/account.service';
import { Observable, catchError, throwError, forkJoin } from 'rxjs';
import { AccountOperation } from '../model/account.model';

@Component({
  selector: 'app-dashboard',
  standalone: false,
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit {
  totalCustomers!: Observable<number>;
  totalAccounts!: Observable<number>;
  totalBalance!: Observable<number>;
  recentTransactions!: Observable<AccountOperation[]>;
  errorMessage!: string;

  constructor(private customerService: CustomerService, private accountService: AccountsService) {}

  ngOnInit(): void {
    this.totalCustomers = this.customerService.getTotalCustomers().pipe(
      catchError(err => {
        this.errorMessage = err.message;
        return throwError(err);
      })
    );
    this.totalAccounts = this.accountService.getTotalAccounts().pipe(
      catchError(err => {
        this.errorMessage = err.message;
        return throwError(err);
      })
    );
    this.totalBalance = this.accountService.getTotalBalance().pipe(
      catchError(err => {
        this.errorMessage = err.message;
        return throwError(err);
      })
    );
    this.recentTransactions = this.accountService.getRecentTransactions().pipe(
      catchError(err => {
        this.errorMessage = err.message;
        return throwError(err);
      })
    );
  }
}
