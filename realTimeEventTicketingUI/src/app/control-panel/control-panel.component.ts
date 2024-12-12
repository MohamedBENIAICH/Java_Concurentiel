import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import {MatIconModule} from '@angular/material/icon';
import { ControlPanelService } from '../services/control-panel.service';
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

  /**
   * This method calls the controlPanelService to start the vendor threads. 
   * Upon a successful response, it sets the threads to a running state, updates the status message, and reloads the page.
   */
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

  /**
   * Calls the controlPanelService to stop the vendor threads. 
   * Upon a successful response, it sets the threads to a non-running state,
   */
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

   /**
   * Calls the controlPanelService to reset the logs.
   */
  resetLogs(): void {
    this.controlPanelService.resetLogs().subscribe({
      next: (response) => {
        this.statusMessage = response;
        this.controlPanelService.setThreadsRunning(false);
        this.reloadPage();
      },
      error: (error) => {
        console.error('Failed to reset logs:', error);
        this.statusMessage = 'Error resetting logs';
      },
    });
  }


  /**
   * reloads page without pushing an entry into browser history
   */
  private reloadPage(): void {
    this.router.navigateByUrl('/logs', { skipLocationChange: true }).then(() => {
      this.router.navigate([this.router.url]);
    });
  }

  
}
