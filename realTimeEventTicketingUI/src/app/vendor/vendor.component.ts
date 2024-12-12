import { Component, OnInit } from '@angular/core';
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
import { VendorService } from '../services/vendor.service';
import { Vendor } from './vendor.module';

@Component({
  selector: 'app-vendor',
  standalone: true,
  imports: [MatFormFieldModule, MatIconModule, MatInputModule, MatSelectModule, MatOptionModule, MatRadioModule, MatCheckboxModule, MatDividerModule, MatButtonModule, FormsModule, RouterModule, CommonModule],
  providers: [HttpClient],
  templateUrl: './vendor.component.html',
  styleUrl: './vendor.component.css'
})
export class VendorComponent {

  vendor: Vendor = {
    vendorName: "",
    address: "",
    location: "",
    email: "",
    telNo: 9412345678,
    eventName: "",
    ticketsPerRelease: 0,
    ticketPrice: 0
  };

  constructor(private vendorService: VendorService, private router: Router, private activatedRoute: ActivatedRoute) {

  }

  /**
   * Calls the vendorService to save the current form values. 
	 * After saving it logs the response and resets the form.
   * @param vendorForm NgForm that contains all completed and validated vendor information
   */
  saveVendor(vendorForm: NgForm): void {
    this.vendorService.saveVendor(this.vendor).subscribe(
      {
        next: (res: Vendor) => {
          console.log(res);
          vendorForm.reset();
        },
        error: (err: HttpErrorResponse) => {
          console.log(err);
        }
      }
    );
  }


}
