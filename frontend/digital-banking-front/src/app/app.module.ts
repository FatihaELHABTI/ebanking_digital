import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavbarComponent } from './navbar/navbar.component';
import { CustomersComponent } from './customers/customers.component';
import { AccountsComponent } from './accounts/accounts.component';
import { provideHttpClient } from '@angular/common/http';
import { ReactiveFormsModule } from '@angular/forms';
import { NewCustomerComponent } from './new-customer/new-customer.component';
import { CustomerAccountsComponent } from './customer-accounts/customer-accounts.component';

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    CustomersComponent,
    AccountsComponent,
    NewCustomerComponent,
    CustomerAccountsComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule
  ],
  providers: [
    provideHttpClient()
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
