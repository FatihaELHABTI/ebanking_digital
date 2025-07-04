import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { AccountsService } from '../services/account.service';
import { catchError, Observable, throwError } from 'rxjs';
import { AccountDetails } from '../model/account.model';
import Swal from 'sweetalert2';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-accounts',
  standalone: false,
  templateUrl: './accounts.component.html',
  styleUrl: './accounts.component.css'
})
export class AccountsComponent implements OnInit{

  accountFormGroup! : FormGroup;
  currentPage : number = 0;
  pageSize : number = 5;
  accountObservable! : Observable<AccountDetails>;
  operationFormGroup! : FormGroup;
  errorMessage! : string;


  constructor(private fb : FormBuilder, private accountService : AccountsService, public authService : AuthService){}

  ngOnInit(): void {
    this.accountFormGroup = this.fb.group({
      accountId : this.fb.control('')
    });

    this.operationFormGroup = this.fb.group({
      operationType : this.fb.control(null),
      amount : this.fb.control(0),
      description : this.fb.control(null),
      accountDestination : this.fb.control(null)
    });
    // const state = this.router.getCurrentNavigation()?.extras.state;
    // if (state && state.accountId) {
    //   this.accountFormGroup.patchValue({ accountId: state.accountId });
    //   this.handleSearchAccount();
    // }
  }

  handleSearchAccount(){
    let accountId : string = this.accountFormGroup.value.accountId;
    // au lieux d'utilisé .subscibe....
    this.accountObservable =  this.accountService.getAccount(accountId, this.currentPage, this.pageSize).pipe(
      catchError(err => {
        this.errorMessage = err.message;
        return throwError(err);
      })
    );
  }

  goToPage(page : number){
    this.currentPage = page;
    this.handleSearchAccount();
  }

  handleAccountOperation(){
    let accountId : string = this.accountFormGroup.value.accountId;
    let operationType = this.operationFormGroup.value.operationType;
    let amount : number = this.operationFormGroup.value.amount;
    let description : string = this.operationFormGroup.value.description;
    let accountDestination : string = this.operationFormGroup.value.accountDestination;

    if(operationType == 'DEBIT'){
      this.accountService.debit(accountId, amount, description).subscribe({
        next : data =>{
          Swal.fire({
            title: "Success Debit ",
            icon: "success",
            draggable: true
          });
          this.operationFormGroup.reset();
          this.handleSearchAccount();
        }, error : err =>{
          console.log(err)
        }
      })
    }
    else if(operationType == 'CREDIT'){

      this.accountService.credit(accountId, amount, description).subscribe({
        next : data =>{
          Swal.fire({
            title: "Success Credit ",
            icon: "success",
            draggable: true
          });
          this.operationFormGroup.reset();
          this.handleSearchAccount();
        }, error : err =>{
          console.log(err)
        }
      })

    } else if(operationType == 'TRANSFER'){

      this.accountService.transfer(accountId, accountDestination,  amount, description).subscribe({
        next : data =>{
          Swal.fire({
            title: "Success transfer ",
            icon: "success",
            draggable: true
          });
          this.operationFormGroup.reset();
          this.handleSearchAccount();
        }, error : err =>{
          console.log(err)
        }
      })
    }
  }


}
