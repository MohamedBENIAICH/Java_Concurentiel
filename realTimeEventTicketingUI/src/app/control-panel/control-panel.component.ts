import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import {MatIconModule} from '@angular/material/icon';
import { ControlPanelService } from '../control-panel.service';

@Component({
  selector: 'app-control-panel',
  standalone: true,
  imports: [MatButtonModule, MatIconModule],
  templateUrl: './control-panel.component.html',
  styleUrl: './control-panel.component.css'
})
export class ControlPanelComponent {
  threadsRunning = false;
  statusMessage = '';

  constructor(private controlPanelService: ControlPanelService) {}

  startThreads(): void {
    this.controlPanelService.startThreads().subscribe({
      next: (response) => {
        this.statusMessage = response;
        this.threadsRunning = true;
      },
      error: (error) => {
        console.error('Failed to start threads:', error);
        this.statusMessage = 'Error starting threads.';
      },
    });
  }

  stopThreads(): void {
    this.controlPanelService.stopThreads().subscribe({
      next: (response) => {
        this.statusMessage = response;
        this.threadsRunning = false;
      },
      error: (error) => {
        console.error('Failed to stop threads:', error);
        this.statusMessage = 'Error stopping threads.';
      },
    });
  }
}
