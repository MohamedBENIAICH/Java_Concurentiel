import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ControlPanelService {

  private threadsRunning: boolean = false;

  // Method to set threadsRunning
  setThreadsRunning(running: boolean): void {
    this.threadsRunning = running;
  }

  // Method to get threadsRunning
  isThreadsRunning(): boolean {
    return this.threadsRunning;
  }

  constructor(private httpClient: HttpClient) { }

  api = "http://localhost:9090"

  /**
   * Sends a request to start threads.
   * @returns Observable<string> with the response from the server.
   */
  public startThreads(): Observable<string> {
    return this.httpClient.post<string>(`${this.api}/start`, null, {
      responseType: 'text' as 'json', // To handle plain text responses
    });
  }

  /**
   * Sends a request to stop threads.
   * @returns Observable<string> with the response from the server.
   */
  public stopThreads(): Observable<string> {
    return this.httpClient.post<string>(`${this.api}/stop`, null, {
      responseType: 'text' as 'json', // To handle plain text responses
    });
  }

  /**
 * Sends a request to reset logs.
 * @returns Observable<string> containing the server's response message.
 */
public resetLogs(): Observable<string> {
  return this.httpClient.post<string>(`${this.api}/reset`, null, {
    responseType: 'text' as 'json',
  });
}

}
