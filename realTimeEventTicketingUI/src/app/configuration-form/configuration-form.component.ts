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
import { ConfigurationService } from '../configuration.service';
import { Configuration } from './configuration.module';

@Component({
  selector: 'app-configuration-form',
  standalone: true,
  imports: [MatFormFieldModule, MatIconModule, MatInputModule, MatSelectModule, MatOptionModule, MatRadioModule, MatCheckboxModule, MatDividerModule, MatButtonModule, FormsModule, RouterModule, CommonModule],
  providers: [HttpClient],
  templateUrl: './configuration-form.component.html',
  styleUrl: './configuration-form.component.css'
})
export class ConfigurationFormComponent implements OnInit {

	isInitializeConfiguration: boolean = false;

	configuration: any;

	constructor(private configurationService: ConfigurationService, private router: Router, private activatedRoute: ActivatedRoute) {}

	clickFunc() {
		console.log("This button works!");
		}

	ngOnInit(): void {
  
	  this.configuration = this.activatedRoute.snapshot.data['configuration'];
	  console.log(this.configuration);
  
	  if(this.configuration && this.configuration.configId == 1) {   // this means we're doing the update operation
		this.isInitializeConfiguration = false;  
	  } else {
		this.isInitializeConfiguration = true;
	  }
	}

	saveConfiguration(configurationForm: NgForm): void {

		if (configurationForm.valid) {
			if(this.isInitializeConfiguration) {
			this.configurationService.saveConfiguration(this.configuration).subscribe(
				{
				next: (res: Configuration) => {
					console.log(res);
					configurationForm.reset();
					this.router.navigate([''])
				},
				error: (err: HttpErrorResponse) => {
					console.log(err);
				}
				}
			);
			} else {
			this.configurationService.updateConfiguration(this.configuration).subscribe(
				{
				next: (res: Configuration) => {
					this.router.navigate([""]);
				}, 
				error: (err: HttpErrorResponse) => {
					console.log(err);
				}
				}
			)
			}
		}
    }

	validationErrors = {
		totalTickets: '',
		maxCapacity: '',
		intervals: '',
	  };
	
	  validateTotalTickets() {
		const totalTickets = Number(this.configuration.totalTickets);
		if (totalTickets < 20 || totalTickets >= 1000) {
		  this.validationErrors.totalTickets =
			'Total tickets must be between 20 and 999.';
		} else {
		  this.validationErrors.totalTickets = '';
		}
	  }
	
	  validateMaxCapacity() {
		const maxCapacity = Number(this.configuration.maxTicketCapacity);
		const totalTickets = Number(this.configuration.totalTickets);
	
		if (maxCapacity <= 0 || maxCapacity > 0.5 * totalTickets) {
		  this.validationErrors.maxCapacity =
			'Max capacity must be greater than 0 and not exceed 50% of total tickets.';
		} else {
		  this.validationErrors.maxCapacity = '';
		}
	  }
	
	  validateIntervals() {
		const releaseRate = Number(this.configuration.ticketReleaseRate);
		const retrievalRate = Number(this.configuration.customerRetrievalRate);
	
		if (releaseRate <= 0 || retrievalRate <= 0 || retrievalRate >= 10) {
		  this.validationErrors.intervals =
			'Retrieval interval must be between 0 and 10. Release rate must be greater than 0.';
		} else if (releaseRate > retrievalRate) {
		  this.validationErrors.intervals =
			'Release interval must not exceed retrieval interval.';
		} else {
		  this.validationErrors.intervals = '';
		}
	  }
	
	  clearForm() {
		this.configuration = {
		  totalTickets: '',
		  ticketReleaseRate: '',
		  customerRetrievalRate: '',
		  maxTicketCapacity: '',
		};
		this.validationErrors = {
		  totalTickets: '',
		  maxCapacity: '',
		  intervals: '',
		};
	  }
}
