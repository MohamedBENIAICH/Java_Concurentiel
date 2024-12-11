import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Customer } from './customer/customer.module';

@Injectable({
  providedIn: 'root'
})
export class CustomerService {

  constructor(private httpClient: HttpClient) { }

  api = "http://localhost:9090"

  public saveCustomer(customer: Customer): Observable<Customer> {
    return this.httpClient.post<Customer>(`${this.api}/save/customer`, customer);
  }

  public getCustomers(): Observable<Customer[]> {
    return this.httpClient.get<Customer[]>(`${this.api}/get/customer`);
  }

  public deleteCustomer(customerId: number) {
    return this.httpClient.delete(`${this.api}/delete/customer/${customerId}`);
  }
}
