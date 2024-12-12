import { Component, OnInit } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import {MatTableModule} from '@angular/material/table';
import {MatButtonModule} from '@angular/material/button';
import {MatIconModule} from '@angular/material/icon';
import { Router, RouterModule } from '@angular/router';
import { TicketService } from '../services/ticket.service';
import { Ticket } from './ticket-list.module';

@Component({
  selector: 'app-ticket-list',
  standalone: true,
  imports: [MatTableModule, MatButtonModule, MatIconModule, RouterModule],
  templateUrl: './ticket-list.component.html',
  styleUrl: './ticket-list.component.css'
})
export class TicketListComponent implements OnInit {
  dataSource: Ticket[] = [];

	displayedColumns: string[] = ['transactionId', 'ticketNo', 'vendor', 'eventName', 'location', 'customer', 'ticketPrice', 'timestamp'];

	constructor(private ticketService: TicketService, private router: Router) {
		this.getTicketList();
	}
	ngOnInit(): void {

	}

	/**
	 * Retrieves tickets from database to display
	 */
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
