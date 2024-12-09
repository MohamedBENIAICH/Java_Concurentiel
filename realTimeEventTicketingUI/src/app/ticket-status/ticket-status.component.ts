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
  ticketPoolStatus: any = null; // Initialize to null
  private statusSubscription!: Subscription; // To manage subscription lifecycle

  constructor(private webSocketService: WebSocketService) {}

  ngOnInit(): void {
    // Subscribe to ticket pool status updates
    this.statusSubscription = this.webSocketService
      .getTicketPoolStatus()
      .subscribe({
        next: (status) => {
          this.ticketPoolStatus = status; // Update ticket pool status
        },
        error: (error) => {
          console.error('Error receiving ticket pool status:', error);
        }
      });
  }

  ngOnDestroy(): void {
    // Ensure subscription is cleaned up to prevent memory leaks
    if (this.statusSubscription) {
      this.statusSubscription.unsubscribe();
    }
  }
}
