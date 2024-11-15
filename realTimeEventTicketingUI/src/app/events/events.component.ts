import { Component } from '@angular/core';
import {MatTableModule} from '@angular/material/table';

@Component({
  selector: 'app-events',
  standalone: true,
  imports: [MatTableModule],
  templateUrl: './events.component.html',
  styleUrl: './events.component.css'
})
export class EventsComponent {

  dataSource: Event[]= [];

	displayedColumns: string[] = ['eventId', 'eventName', 'venuer', 'eventStart', 'duration', 'eventType', 'ticketPrice', 'totalTicketsAvailable', 'ticketReleaseRate', 'ticketRetrievalRate', 'maximumBufferCapacity'];

}
