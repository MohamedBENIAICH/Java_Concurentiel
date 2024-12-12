import { Component } from '@angular/core';
import { Customer } from '../customer/customer.module';
import { CustomerService } from '../services/customer.service';
import { HttpErrorResponse } from '@angular/common/http';
import {MatTableModule} from '@angular/material/table';
import {MatButtonModule} from '@angular/material/button';
import {MatIconModule} from '@angular/material/icon';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-customer-list',
  standalone: true,
  imports: [MatTableModule, MatButtonModule, MatIconModule, RouterModule],
  templateUrl: './customer-list.component.html',
  styleUrl: './customer-list.component.css'
})
export class CustomerListComponent {
  dataSource: Customer[] = [];

	displayedColumns: string[] = ['customerId', 'customerName', 'customerAddress', 'customerEmail', 'customerTel', 'purchaseQuantity' , 'delete'];

	constructor(private customerService: CustomerService, private router: Router) {
		this.getCustomerList();
	}
	ngOnInit(): void {}

	/**
	 * Retreives all customers in database to display
	 */
	getCustomerList(): void {
		this.customerService.getCustomers().subscribe(
			{
				next: (res: Customer[]) => {
					this.dataSource = res;
				},
				error: (err: HttpErrorResponse) => {
					console.log(err);
				}
			}
		)
	}

	/**
	 * Removes specific customer from database
	 * @param customerId id of the customer to be deleted
	 */
	deleteEmployee(customerId: number): void {
		this.customerService.deleteCustomer(customerId).subscribe(
			{
				next: (res) => {
					console.log(res);
					this.getCustomerList();
				},
				error: (err: HttpErrorResponse)=> {
					console.log(err);
				}
			}
		)
	}
}
