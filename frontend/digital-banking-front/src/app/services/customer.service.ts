import { Customer } from './../model/customer.model';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import {environment} from '../../environment/environment';


@Injectable({
  providedIn: 'root'
})
export class CustomerService {


  constructor(private http : HttpClient) {}

  getCustomers():Observable<Array<Customer>>{
    return this.http.get<Array<Customer>>(environment.backendHost+"/customers");
  }

  serachCustomers(keyword : string):Observable<Array<Customer>>{
    return this.http.get<Array<Customer>>(environment.backendHost+"/customers/search?keyword="+keyword);
  }

  saveCustomer(customer : Customer):Observable<Customer>{
    return this.http.post<Customer>(environment.backendHost+"/customers", customer);
  }

  deleteCustomer(idCustomer : number){
    return this.http.delete(environment.backendHost+"/customers/"+idCustomer);
  }
  updateCustomer(customer: Customer): Observable<Customer> {
    return this.http.put<Customer>(`${environment.backendHost}/customers/${customer.id}`, customer);
  }
  // New method to fetch a single customer by ID
  getCustomer(customerId: string): Observable<Customer> {
    return this.http.get<Customer>(`${environment.backendHost}/customers/${customerId}`);
  }

  getTotalCustomers(): Observable<number> {
    return this.http.get<number>(`${environment.backendHost}/customers/count`);
  }
}

// les méthodes get, post, put retourne un objet de type Observable
