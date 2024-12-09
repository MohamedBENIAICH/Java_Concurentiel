import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { webSocket, WebSocketSubject } from 'rxjs/webSocket';

@Injectable({
  providedIn: 'root',
})
export class WebSocketService {
	private socket: WebSocketSubject<any>;
	private statusSubject = new BehaviorSubject<any>(null); // Real-time ticket pool status
	private logsSubject = new BehaviorSubject<string[]>([]); // Log messages

	constructor() {
		// Initialize WebSocket connection to match backend endpoint
		this.socket = webSocket('ws://localhost:9090/ws-native');

		// Listen to server messages
		this.listenToServerMessages();
	}

	// Subscribe to WebSocket messages
	private listenToServerMessages(): void {
		this.socket.subscribe({
		next: (message) => this.handleServerMessage(message),
		error: (err) => console.error('WebSocket error:', err),
		complete: () => console.warn('WebSocket connection closed'),
		});
	}

	// Process incoming server messages
	private handleServerMessage(message: any): void {
		switch (message.type) {
		case 'status':
			this.statusSubject.next(message.data); // Update status
			break;
		case 'log':
			this.addLog(message.data); // Add to logs
			break;
		default:
			console.warn('Unknown message type:', message.type);
		}
	}

	// Add a log to the logsSubject
	private addLog(log: string): void {
		const currentLogs = this.logsSubject.value;
		this.logsSubject.next([...currentLogs, log]);
	}

	// Expose ticket pool status as an observable
	public getTicketPoolStatus(): Observable<any> {
		return this.statusSubject.asObservable();
	}

	// Expose logs as an observable
	public getLogs(): Observable<string[]> {
		return this.logsSubject.asObservable();
	}

	// Send a message to the WebSocket server
	public sendMessage(message: string): void {
		this.socket.next({ message });
	}

	// Close the WebSocket connection
	public closeConnection(): void {
		this.socket.complete();
	}
}