import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatIconModule} from '@angular/material/icon';
import {MatInputModule} from '@angular/material/input';
import {MatSelectModule} from '@angular/material/select';
import { MatOptionModule } from '@angular/material/core';
import {MatRadioModule} from '@angular/material/radio';
import {MatCheckboxModule} from '@angular/material/checkbox';
import {MatDividerModule} from '@angular/material/divider';
import {MatButtonModule} from '@angular/material/button';
import { FormsModule, NgForm } from '@angular/forms';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { Router } from '@angular/router';
import { Customer } from './customer.module';
import { CustomerService } from '../customer.service';

@Component({
  selector: 'app-customer',
  standalone: true,
  imports: [MatFormFieldModule, MatIconModule, MatInputModule, MatSelectModule, MatOptionModule, MatRadioModule, MatCheckboxModule, MatDividerModule, MatButtonModule, FormsModule, RouterModule, CommonModule],
  providers: [HttpClient],
  templateUrl: './customer.component.html',
  styleUrl: './customer.component.css'
})
export class CustomerComponent {

	customer: Customer = {
		customerName: "",
		customerAddress: "",
		customerEmail: "",
		customerTel: 9412345678,
		purchaseQuantity: 0
	};

	constructor(private customerService: CustomerService, private router: Router, private activatedRoute: ActivatedRoute) {}

	saveCustomer(customerForm: NgForm): void {
		this.customerService.saveCustomer(this.customer).subscribe(
		  {
			next: (res: Customer) => {
			  console.log(res);
			  customerForm.reset();
			},
			error: (err: HttpErrorResponse) => {
			  console.log(err);
			}
		  }
		);
	  }

}
