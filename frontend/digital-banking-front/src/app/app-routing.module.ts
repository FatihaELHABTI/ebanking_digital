import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthenticationGuard } from './guards/authentication.guard';
import { AuthorizationGuard } from './guards/authorization.guard';
import { CustomersComponent } from './customers/customers.component';
import { AccountsComponent } from './accounts/accounts.component';
import { NewCustomerComponent } from './new-customer/new-customer.component';
import { LoginComponent } from './login/login.component';
import { AdminTemplateComponent } from './admin-template/admin-template.component';
import { NotAuthorizedComponent } from './not-authorized/not-authorized.component';
import { CustomerAccountComponent } from './customer-accounts/customer-accounts.component';
import { EditCustomerComponent } from './edit-customer/edit-customer.component';
import { DashboardComponent } from './dashboard/dashboard.component';

const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  {
    path: 'admin',
    component: AdminTemplateComponent,
    canActivate: [AuthenticationGuard],
    children: [
      { path: 'dashboard', component: DashboardComponent },
      { path: 'customers', component: CustomersComponent },
      { path: 'accounts', component: AccountsComponent },
      { path: 'new-customer', component: NewCustomerComponent, canActivate: [AuthorizationGuard], data: { role: 'ADMIN' } },
      { path: 'customer-accounts/:id', component: CustomerAccountComponent },
      { path: 'edit-customer/:id', component: EditCustomerComponent, canActivate: [AuthorizationGuard], data: { role: 'ADMIN' } },
      { path: 'notAuthorized', component: NotAuthorizedComponent },
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' } // Default child route
    ]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
