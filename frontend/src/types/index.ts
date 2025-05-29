export interface Vendor {
  vendorId?: number;
  vendorName: string;
  address: string;
  email: string;
  telNo: string;
  eventName: string;
  location: string;
  ticketPrice: number;
  ticketsPerRelease: number;
}

export interface Customer {
  customerId?: number;
  customerName: string;
  customerAddress: string;
  customerEmail: string;
  customerTel: string;
  purchaseQuantity: number;
}

export interface Ticket {
  transactionId?: number;
  ticketNo: number;
  vendor: string;
  eventName: string;
  location: string;
  customer: string;
  ticketPrice: number;
  timestamp: string;
}

export interface Configuration {
  totalTickets: number;
  ticketReleaseRate: number;
  customerRetrievalRate: number;
  maxTicketCapacity: number;
}