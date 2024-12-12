import { Component } from '@angular/core';
import { Vendor } from '../vendor/vendor.module';
import { VendorService } from '../services/vendor.service';
import { HttpErrorResponse } from '@angular/common/http';
import {MatTableModule} from '@angular/material/table';
import {MatButtonModule} from '@angular/material/button';
import {MatIconModule} from '@angular/material/icon';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-vendor-list',
  standalone: true,
  imports: [MatTableModule, MatButtonModule, MatIconModule, RouterModule],
  templateUrl: './vendor-list.component.html',
  styleUrl: './vendor-list.component.css'
})
export class VendorListComponent {
  dataSource: Vendor[] = [];

	displayedColumns: string[] = ['vendorId', 'vendorName', 'address', 'email', 'telNo', 'eventName', 'location', 'ticketPrice', 'ticketsPerRelease' , 'delete'];

	constructor(private vendorService: VendorService, private router: Router) {
		this.getVendorList();
	}
	ngOnInit(): void {}

	/**
	 * Retreives all vendors in database to display
	 */
	getVendorList(): void {
		this.vendorService.getVendors().subscribe(
			{
				next: (res: Vendor[]) => {
					this.dataSource = res;
				},
				error: (err: HttpErrorResponse) => {
					console.log(err);
				}
			}
		)
	}

	/**
	 * Removes specific vendor from database
	 * @param vendorId id of the vendor to be deleted
	 */
	deleteVendor(vendorId: number): void {
		this.vendorService.deleteVendor(vendorId).subscribe(
			{
				next: (res) => {
					console.log(res);
					this.getVendorList();
				},
				error: (err: HttpErrorResponse)=> {
					console.log(err);
				}
			}
		)
	}
}
