import { Component } from '@angular/core';
import { ControlPanelComponent } from "../control-panel/control-panel.component";
import { CommonModule } from '@angular/common';
import { Subscription } from 'rxjs';
import { WebSocketService } from '../websocket.service';

@Component({
  selector: 'app-log-display',
  standalone: true,
  imports: [ControlPanelComponent, CommonModule],
  templateUrl: './log-display.component.html',
  styleUrl: './log-display.component.css'
})
export class LogDisplayComponent {
  logs: string[] = [];
  private logsSubscription!: Subscription; // Subscription to manage the observable

  constructor(private webSocketService: WebSocketService) {}

  ngOnInit(): void {
    // Subscribe to the logs observable to receive updates
    this.logsSubscription = this.webSocketService.getLogs().subscribe({
      next: (logs) => {
        this.logs = logs; // Update the logs list
      },
      error: (error) => {
        console.error('Error fetching logs:', error);
      },
    });
  }

  ngOnDestroy(): void {
    // Unsubscribe to avoid memory leaks when the component is destroyed
    if (this.logsSubscription) {
      this.logsSubscription.unsubscribe();
    }
  }
}
