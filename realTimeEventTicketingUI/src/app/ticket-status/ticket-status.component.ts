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
  ticketPoolStatus: any = null;
  private statusSubscription!: Subscription;

  constructor(private webSocketService: WebSocketService) {}

  ngOnInit(): void {
    // Subscribe to ticket pool status observable
    this.statusSubscription = this.webSocketService.getTicketPoolStatus().subscribe({
      next: (status) => {
        this.ticketPoolStatus = status; // Update the status
      },
      error: (error) => {
        console.error('Error receiving ticket pool status:', error);
      }
    });

    // Request the latest ticket pool status from the server
    this.webSocketService.sendMessage('status');
  }

  ngOnDestroy(): void {
    // Clean up the subscription to avoid memory leaks
    if (this.statusSubscription) {
      this.statusSubscription.unsubscribe();
    }
  }
}
