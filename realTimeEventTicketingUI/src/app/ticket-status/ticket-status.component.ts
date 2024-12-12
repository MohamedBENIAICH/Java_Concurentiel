import { Component } from '@angular/core';
import { WebSocketService } from '../services/websocket.service';
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
  ticketPoolStatus: any = null;
  private statusSubscription!: Subscription;

  constructor(private webSocketService: WebSocketService) {}

  /**
   * Called when component is initialized and then subscribes to the TicketPool status message updates from WebSocketService.
   */
  ngOnInit(): void {
    this.statusSubscription = this.webSocketService.getTicketPoolStatus().subscribe({
      next: (status) => {
        this.ticketPoolStatus = status; 
      },
      error: (error) => {
        console.error('Error receiving ticket pool status:', error);
      }
    });

    this.webSocketService.sendMessage('status');
  }

  /**
   * Called when navigating to a different route and unsubscribes from TicketPool status messages to prevent memory leaks
   */
  ngOnDestroy(): void {
    if (this.statusSubscription) {
      this.statusSubscription.unsubscribe();
    }
  }
}
