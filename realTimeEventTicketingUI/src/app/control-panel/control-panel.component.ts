import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import {MatIconModule} from '@angular/material/icon';
import { ControlPanelService } from '../control-panel.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-control-panel',
  standalone: true,
  imports: [MatButtonModule, MatIconModule],
  templateUrl: './control-panel.component.html',
  styleUrl: './control-panel.component.css'
})
export class ControlPanelComponent {
  threadsRunning: boolean = false;
  statusMessage: string = '';

  constructor(private controlPanelService: ControlPanelService, private router: Router) {}

  ngOnInit(): void {
    this.threadsRunning = this.controlPanelService.isThreadsRunning();
  }


  startThreads(): void {
    this.controlPanelService.startThreads().subscribe({
      next: (response) => {
        this.statusMessage = response;
        this.controlPanelService.setThreadsRunning(true);
        this.reloadPage();
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
        this.controlPanelService.setThreadsRunning(false);
        this.reloadPage();
      },
      error: (error) => {
        console.error('Failed to stop threads:', error);
        this.statusMessage = 'Error stopping threads.';
      },
    });
  }

  private reloadPage(): void {
    // Refresh the current route without navigating elsewhere
    this.router.navigateByUrl('/logs', { skipLocationChange: true }).then(() => {
      this.router.navigate([this.router.url]); // Reload current route
    });
  }
}
