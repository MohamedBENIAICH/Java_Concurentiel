import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ControlPanelService {

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

}
