import { Component } from '@angular/core';
import { WebSocketService } from '../websocket.service';

@Component({
  selector: 'app-control-panel',
  standalone: true,
  imports: [],
  templateUrl: './control-panel.component.html',
  styleUrl: './control-panel.component.css'
})
export class ControlPanelComponent {
  threadsRunning = false;

  constructor(private webSocketService: WebSocketService) {}

  startThreads(): void {
    this.webSocketService.sendMessage('start');
    this.threadsRunning = true;
  }

  stopThreads(): void {
    this.webSocketService.sendMessage('stop');
    this.threadsRunning = false;
  }
}
