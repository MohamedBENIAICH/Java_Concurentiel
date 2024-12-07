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
  private logsSubscription!: Subscription;  // non-null assertion operator

  constructor(private webSocketService: WebSocketService) {}

  ngOnInit(): void {
    this.logsSubscription = this.webSocketService.getLogs().subscribe((logs) => {
      this.logs = logs;
    });
  }

  ngOnDestroy(): void {
    if (this.logsSubscription) {
      this.logsSubscription.unsubscribe();
    }
  }
}
