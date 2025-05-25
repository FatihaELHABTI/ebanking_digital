import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { Observable } from 'rxjs';
import {AccountDetails, AccountOperation, BankAccount} from '../model/account.model';
import {environment} from '../../environment/environment';

@Injectable({
  providedIn: 'root'
})
export class AccountsService {

  constructor(private http : HttpClient) { }

  public getAccount(accountId : string, page : number, size : number):Observable<AccountDetails>{
    return this.http.get<AccountDetails>(environment.backendHost+"/accounts/"+accountId+"/pageOperations?page="+page+"&size="+size);
  }

  public debit(accountId : string, amount : number, description : string){
    let data = {accountId : accountId, amount : amount, description : description}
    return this.http.post(environment.backendHost+"/accounts/debit", data);
  }

  public credit(accountId : string, amount : number, description : string){
    let data = {accountId : accountId, amount : amount, description : description}
    return this.http.post(environment.backendHost+"/accounts/credit", data);
  }

  public transfer(accountSource : string, accountDestination : string,  amount : number, description : string){
    let data = {accountSource, accountDestination, amount, description}
    return this.http.post(environment.backendHost+"/accounts/transfer", data);
  }

  public getCustomerAccounts(customerId: string): Observable<BankAccount[]> {
    return this.http.get<BankAccount[]>(`${environment.backendHost}/customers/${customerId}/accounts`);
  }
  getTotalAccounts(): Observable<number> {
    return this.http.get<number>(`${environment.backendHost}/accounts/count`);
  }

  getTotalBalance(): Observable<number> {
    return this.http.get<number>(`${environment.backendHost}/accounts/totalBalance`);
  }

  getRecentTransactions(): Observable<AccountOperation[]> {
    return this.http.get<AccountOperation[]>(`${environment.backendHost}/accounts/recentTransactions?size=5`);
  }

}
