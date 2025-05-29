import axios from 'axios';
import { Vendor, Customer, Ticket, Configuration } from '../types';

const API_URL = 'http://localhost:9090';

const api = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Task Manager API
export const startThreads = () => api.post('/start');
export const stopThreads = () => api.post('/stop');
export const resetLogs = () => api.post('/reset');

// Vendor API
export const getVendors = () => api.get('/get/vendor');
export const getVendor = (vendorId: number) => api.get(`/get/vendor/${vendorId}`);
export const saveVendor = (vendor: Vendor) => api.post('/save/vendor', vendor);
export const deleteVendor = (vendorId: number) => api.delete(`/delete/vendor/${vendorId}`);

// Customer API
export const getCustomers = () => api.get('/get/customer');
export const getCustomer = (customerId: number) => api.get(`/get/customer/${customerId}`);
export const saveCustomer = (customer: Customer) => api.post('/save/customer', customer);
export const deleteCustomer = (customerId: number) => api.delete(`/delete/customer/${customerId}`);
export const deleteAllCustomers = () => api.delete('/delete/customer');

// Ticket API
export const getTickets = () => api.get('/get/tickets');
export const saveTicket = (ticket: Ticket) => api.post('/save/ticket', { params: { ticket } });

// Configuration API
export const getConfiguration = () => api.get('/get/config');
export const saveConfiguration = (config: Configuration) => api.post('/save/config', config);

export default api;