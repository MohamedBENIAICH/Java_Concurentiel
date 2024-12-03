import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Ticket } from './log-display/log-display.module';

@Injectable({
  providedIn: 'root'
})
export class TicketService {

  constructor(private httpClient: HttpClient) { }

	api = "http://localhost:9090"

	public saveTicket(ticket: Ticket): Observable<Ticket> {
		return this.httpClient.post<Ticket>(`${this.api}/save/ticket`, ticket);
	  }

	public getTickets():Observable<Ticket[]> {
		return this.httpClient.get<Ticket[]>(`${this.api}/get/tickets`);
	}
	
}
