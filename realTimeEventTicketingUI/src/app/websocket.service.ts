import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { webSocket } from 'rxjs/webSocket';

@Injectable({
  providedIn: 'root',
})
export class LogService {
  private logSocket = webSocket<string>('ws://localhost:8080/logs'); // Specify the data type here

  getLogs(): Observable<string> {
    return this.logSocket.asObservable(); // Now it returns Observable<string>
  }

  sendMessage(message: string): void {
    this.logSocket.next(message);
  }

  closeConnection(): void {
    this.logSocket.complete();
  }
}