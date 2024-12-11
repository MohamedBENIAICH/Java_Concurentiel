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
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { Router } from '@angular/router';
import { ConfigurationService } from '../configuration.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-configuration-form',
  standalone: true,
  imports: [MatFormFieldModule, MatIconModule, MatInputModule, MatSelectModule, MatOptionModule, MatRadioModule, MatCheckboxModule, MatDividerModule, MatButtonModule, FormsModule, RouterModule, CommonModule, ReactiveFormsModule],
  providers: [HttpClient],
  templateUrl: './configuration-form.component.html',
  styleUrl: './configuration-form.component.css'
})
export class ConfigurationFormComponent {

	form: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private configurationService: ConfigurationService,
    private router: Router,
    private activatedRoute: ActivatedRoute
  ) {
    this.form = this.formBuilder.group({
      totalTickets: [0, [Validators.required, Validators.min(20), Validators.max(999)]],
      maxTicketCapacity: [0, [Validators.required, Validators.min(1)]],
      ticketReleaseRate: [0, [Validators.required, Validators.min(1)]],
      customerRetrievalRate: [0, [Validators.required, Validators.min(1)]],
    });

    this.form.valueChanges.subscribe(() => {
      this.validateForm();
    });
  }

  validateForm() {
    const totalTickets = this.form.get('totalTickets')?.value || 0;
    const maxTicketCapacity = this.form.get('maxTicketCapacity')?.value || 0;
    const ticketReleaseRate = this.form.get('ticketReleaseRate')?.value || 0;
    const customerRetrievalRate = this.form.get('customerRetrievalRate')?.value || 0;

    // Validate maxCapacity
    if (maxTicketCapacity <= 0 || maxTicketCapacity > 0.5 * totalTickets) {
      this.form.get('maxTicketCapacity')?.setErrors({
        maxCapacityInvalid: true,
        message: 'Max capacity must be greater than 0 and not exceed 50% of total tickets.',
      });
    } else {
      this.form.get('maxTicketCapacity')?.setErrors(null);
    }

    // Validate intervals
    if (ticketReleaseRate <= 0 || customerRetrievalRate <= 0) {
      this.form.get('ticketReleaseRate')?.setErrors({
        intervalInvalid: true,
        message: 'Intervals must be a single digit greater than 0.',
      });
    } else if (ticketReleaseRate > customerRetrievalRate) {
      this.form.get('ticketReleaseRate')?.setErrors({
        intervalMismatch: true,
        message: 'Release rate must not exceed retrieval rate.',
      });
    } else {
      this.form.get('ticketReleaseRate')?.setErrors(null);
    }
  }

  saveConfiguration(): void {
    if (this.form.valid) {
      this.configurationService.saveConfiguration(this.form.value).subscribe({
        next: (res: any) => {
          console.log(res);
          this.form.reset();
        },
        error: (err: HttpErrorResponse) => {
          console.log(err);
        },
      });
    }
  }

}
