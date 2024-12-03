import { Component, OnInit } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import {MatTableModule} from '@angular/material/table';
import {MatButtonModule} from '@angular/material/button';
import {MatIconModule} from '@angular/material/icon';
import { Router, RouterModule } from '@angular/router';
import { Ticket } from './log-display.module';
import { TicketService } from '../ticket.service';

@Component({
  selector: 'app-log-display',
  standalone: true,
  imports: [MatTableModule, MatButtonModule, MatIconModule, RouterModule],
  templateUrl: './log-display.component.html',
  styleUrl: './log-display.component.css'
})
export class LogDisplayComponent implements OnInit {

	dataSource: Ticket[] = [];

	displayedColumns: string[] = ['ticketNo', 'vendor', 'eventName', 'location', 'customer', 'ticketPrice', 'timestamp'];

	constructor(private ticketService: TicketService, private router: Router) {
		this.getTicketList();
	}
	ngOnInit(): void {

	}

	getTicketList(): void {
		this.ticketService.getTickets().subscribe(
			{
				next: (res: Ticket[]) => {
					this.dataSource = res;
				},
				error: (err: HttpErrorResponse) => {
					console.log(err);
				}
			}
		)
	}

}
