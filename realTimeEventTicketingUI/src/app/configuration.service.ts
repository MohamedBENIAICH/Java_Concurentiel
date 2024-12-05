import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Configuration } from './configuration-form/configuration.module';

@Injectable({
  providedIn: 'root'
})
export class ConfigurationService {

  constructor(private httpClient: HttpClient) { }

	api = "http://localhost:9090"

	  public saveConfiguration(configuration: Configuration): Observable<Configuration> {
		  return this.httpClient.post<Configuration>(`${this.api}/save/configuration`, configuration);
	  }

    public getConfiguration() {
      return this.httpClient.get<Configuration>(`${this.api}/get/configuration`);
    }

    public updateConfiguration(configuration: Configuration) {
      return this.httpClient.put<Configuration>(`${this.api}/update/config`, configuration);
    }


}
