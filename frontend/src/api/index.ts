import axios from 'axios';
import { Vendor, Customer, Ticket, Configuration } from '../types';

const API_URL = 'http://localhost:9090';

const api = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add response interceptor for logging
api.interceptors.response.use(
  (response) => {
    // Log the API endpoint and response data
    console.group(`API Response: ${response.config.method?.toUpperCase()} ${response.config.url}`);
    console.log('Status:', response.status);
    console.log('Data:', response.data);
    console.groupEnd();
    return response;
  },
  (error) => {
    // Log any API errors
    console.group(`API Error: ${error.config?.method?.toUpperCase()} ${error.config?.url}`);
    console.error('Status:', error.response?.status);
    console.error('Error:', error.response?.data || error.message);
    console.groupEnd();
    return Promise.reject(error);
  }
);

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

// Add a function to get customer name by ID
export const getCustomerName = async (customerId: number): Promise<string> => {
  try {
    const response = await getCustomer(customerId);
    return response.data.customerName;
  } catch (error) {
    console.error('Error fetching customer name:', error);
    return 'Unknown Customer';
  }
};

// Ticket API
export const getTickets = () => api.get('/get/tickets');
export const saveTicket = (ticket: Ticket) => api.post('/save/ticket', { params: { ticket } });

// Update send ticket functions to include PDF data
export const sendTicketToCustomer = async (transactionId: number, customerId: number, pdfData: string) => 
  api.post(`/send/ticket/${transactionId}/customer/${customerId}`, { pdfData });

export const sendAllTicketsToCustomer = async (customerId: number, pdfDataArray: { transactionId: number, pdfData: string }[]) => 
  api.post(`/send/tickets/customer/${customerId}`, { pdfDataArray });

// Configuration API
export const getConfiguration = () => api.get('/get/config');
export const saveConfiguration = (config: Configuration) => api.post('/save/config', config);

export default api;