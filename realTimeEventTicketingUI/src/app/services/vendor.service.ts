import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Vendor } from '../vendor/vendor.module';

@Injectable({
  providedIn: 'root'
})
export class VendorService {

  constructor(private httpClient: HttpClient) { }

  api = "http://localhost:9090"

  public saveVendor(vendor: Vendor): Observable<Vendor> {
    return this.httpClient.post<Vendor>(`${this.api}/save/vendor`, vendor);
  }

  public getVendors(): Observable<Vendor[]> {
    return this.httpClient.get<Vendor[]>(`${this.api}/get/vendor`);
  }

  public deleteVendor(vendorId: number) {
    return this.httpClient.delete(`${this.api}/delete/vendor/${vendorId}`);
  }

}
