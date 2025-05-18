import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Customer } from '../model/customer.model';

@Component({
  selector: 'app-customer-accounts',
  standalone: false,
  templateUrl: './customer-accounts.component.html',
  styleUrl: './customer-accounts.component.css'
})
export class CustomerAccountsComponent implements OnInit {
  customerId!: string;
  customer: Customer;
  constructor(private activatedRouter: ActivatedRoute, private router: Router) {
    this.customer = router.getCurrentNavigation()?.extras.state as Customer;
  }

  ngOnInit(): void {
    this.customerId = this.activatedRouter.snapshot.params['id'];
  }

}
