import { Component } from '@angular/core';
import { ControlPanelComponent } from "../control-panel/control-panel.component";
import { CommonModule } from '@angular/common';
import { Subscription } from 'rxjs';
import { WebSocketService } from '../websocket.service';
import {MatButtonModule} from '@angular/material/button';

@Component({
  selector: 'app-log-display',
  standalone: true,
  imports: [ControlPanelComponent, CommonModule, MatButtonModule],
  templateUrl: './log-display.component.html',
  styleUrl: './log-display.component.css'
})
export class LogDisplayComponent {
  logs: string[] = [];
  private logsSubscription!: Subscription;
  private seenLogs = new Set<string>(); // To track unique logs

  constructor(private webSocketService: WebSocketService) {}

  ngOnInit(): void {
    this.logsSubscription = this.webSocketService.getLogs().subscribe({
      next: (newLogs) => {
        // Filter out already-seen logs
        const uniqueLogs = newLogs.filter((log) => !this.seenLogs.has(log));
        uniqueLogs.forEach((log) => this.seenLogs.add(log)); // Mark logs as seen
        this.logs = [...this.logs, ...uniqueLogs]; // Add only unique logs
      },
      error: (error) => {
        console.error('Error fetching logs:', error);
      },
    });
  }

  resetLogs(): void {
    this.logs = [];
    this.seenLogs.clear(); // Clear the set to allow all logs to be displayed again
  }

  ngOnDestroy(): void {
    if (this.logsSubscription) {
      this.logsSubscription.unsubscribe();
    }
  }
}
