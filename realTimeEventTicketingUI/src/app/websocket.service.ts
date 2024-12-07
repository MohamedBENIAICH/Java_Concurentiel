import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { webSocket, WebSocketSubject } from 'rxjs/webSocket';

@Injectable({
  providedIn: 'root',
})
export class WebSocketService {
  private ticketPoolSocket: WebSocketSubject<any>;
  private logSocket: WebSocketSubject<any>;
  
  private statusSubject = new BehaviorSubject<any>(null); // To manage real-time ticket pool status
  private logsSubject = new BehaviorSubject<string[]>([]); // To manage logs

  constructor() {
    this.ticketPoolSocket = webSocket('ws://localhost:9090/status');
    this.logSocket = webSocket('ws://localhost:9090/logs');

    // Automatically subscribe to WebSocket updates
    this.listenToTicketPoolStatus();
    this.listenToLogs();
  }

  // Listen for ticket pool status updates
  private listenToTicketPoolStatus() {
    this.ticketPoolSocket.subscribe({
      next: (message) => this.statusSubject.next(message),
      error: (err) => console.error('Ticket Pool WebSocket error:', err),
      complete: () => console.warn('Ticket Pool WebSocket connection closed'),
    });
  }

  // Listen for logs
  private listenToLogs() {
    this.logSocket.subscribe({
      next: (log) => this.addLog(log),
      error: (err) => console.error('Log WebSocket error:', err),
      complete: () => console.warn('Log WebSocket connection closed'),
    });
  }

  // Add log to the logs array
  private addLog(log: string) {
    const currentLogs = this.logsSubject.value;
    this.logsSubject.next([...currentLogs, log]);
  }

  // Get real-time ticket pool status
  public getTicketPoolStatus(): Observable<any> {
    return this.statusSubject.asObservable();
  }

  // Get logs
  public getLogs(): Observable<string[]> {
    return this.logsSubject.asObservable();
  }

  // Close WebSocket connections
  public closeConnections(): void {
    this.ticketPoolSocket.complete();
    this.logSocket.complete();
  }

  // Send a message to the server
  public sendMessage(message: string): void {
    this.ticketPoolSocket.next(message);
  }
}