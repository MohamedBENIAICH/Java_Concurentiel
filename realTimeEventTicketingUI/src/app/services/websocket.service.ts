import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { webSocket, WebSocketSubject } from 'rxjs/webSocket';

@Injectable({
  providedIn: 'root',
})
export class WebSocketService {
	private socket: WebSocketSubject<any> | null = null;
  private statusSubject = new BehaviorSubject<any>(null); // Real-time ticket pool status
  private logsSubject = new BehaviorSubject<string[]>([]); // Log messages

  constructor() {
    this.connectWebSocket();
  }

  /**
   * Method to initialize the WebSocket connection
   */
  private connectWebSocket(): void {
    try {
      this.socket = webSocket('ws://localhost:9090/ws-native');
      this.listenToServerMessages(); 
    } catch (error) {
      console.log("Couldn't connect to WebSocket", error);
    }
  }

  /**
   * Listen to messages from the WebSocket server
   */
  private listenToServerMessages(): void {
    this.socket?.subscribe({
      next: (message) => this.handleServerMessage(message),
      error: (err) => {
        console.log('WebSocket error:', err);
        this.reconnectWebSocket(); 
      },
      complete: () => {
        console.log('WebSocket connection closed');
        this.reconnectWebSocket(); 
      },
    });
  }

  /**
   * Handle incoming messages from the server
   * @param message message received from backend
   */
  private handleServerMessage(message: any): void {
    switch (message.type) {
      case 'status':
        this.statusSubject.next(message.data);
        break;
      case 'log':
        this.updateLogs(message.data); 
        break;
      default:
        console.log('Unknown message type:', message.type);
    }
  }

  /**
   *  Update logs in the status
   * @param newLogs newly received logs
   */
  private updateLogs(newLogs: string[]): void {
    this.logsSubject.next(newLogs);
  }

  /**
   * Expose ticket pool status as an observable
   * @returns Observable ticketpool status
   */
  public getTicketPoolStatus(): Observable<any> {
    return this.statusSubject.asObservable();
  }

  /**
   * Expose logs as an observable
   * @returns Observable logs
   */
  public getLogs(): Observable<string[]> {
    return this.logsSubject.asObservable();
  }

  /**
   * Send a message to the WebSocket server
   * @param messageType type of message sent
   */
  public sendMessage(messageType: string): void {
    if (this.socket) {
      this.socket.next({ type: messageType });
    } else {
      console.log('WebSocket is not connected. Message not sent:', messageType);
    }
  }

  /**
   * Close the WebSocket connection
   */
  public closeConnection(): void {
    if (this.socket) {
      this.socket.complete();
      this.socket = null;
    }
  }

  /**
   * Reconnect to WebSocket after a failure every 5 seconds
   */
  private reconnectWebSocket(): void {
    console.log('Attempting to reconnect to WebSocket...');
    setTimeout(() => this.connectWebSocket(), 5000); 
  }
}