import { Component } from '@angular/core';
import { WebSocketService } from '../websocket.service';
import { CommonModule } from '@angular/common';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-ticket-status',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './ticket-status.component.html',
  styleUrl: './ticket-status.component.css'
})
export class TicketStatusComponent {
  ticketPoolStatus: any;
  private statusSubscription!: Subscription;

  constructor(private webSocketService: WebSocketService) {}

  ngOnInit(): void {
    this.statusSubscription = this.webSocketService
      .getTicketPoolStatus()
      .subscribe((status) => {
        this.ticketPoolStatus = status;
      });
  }

  ngOnDestroy(): void {
    if (this.statusSubscription) {
      this.statusSubscription.unsubscribe();
    }
  }
}
