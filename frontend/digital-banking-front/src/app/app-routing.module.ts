import { AuthenticationGuard } from './guards/authentication.guard';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CustomersComponent } from './customers/customers.component';
import { AccountsComponent } from './accounts/accounts.component';
import { NewCustomerComponent } from './new-customer/new-customer.component';
import { LoginComponent } from './login/login.component';
import { AdminTemplateComponent } from './admin-template/admin-template.component';
import { AuthorizationGuard } from './guards/authorization.guard';
import { NotAuthorizedComponent } from './not-authorized/not-authorized.component';
import {CustomerAccountComponent} from './customer-accounts/customer-accounts.component';

/* a chaque fois que je fais /admin/**, ce AuthenticationGuard prendre la route et il fait la verification
(qui se trouve dans AuthenticationGuard), si il n'a pas authentifier, il vas pas lissez de faire quelque chose */
const routes: Routes = [
  {path : "login", component : LoginComponent},
  {path : "", redirectTo : "/login", pathMatch : "full"},
  {path : "admin", component : AdminTemplateComponent, canActivate : [AuthenticationGuard],
    children : [
      {path : "customers", component : CustomersComponent},
      {path : "accounts", component : AccountsComponent},
      {path : "new-customer", component : NewCustomerComponent, canActivate : [AuthorizationGuard], data : {role : "ADMIN"}},
      {path : "customer-accounts/:id", component : CustomerAccountComponent},
      {path : "notAuthorized", component : NotAuthorizedComponent}
    ]}

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
