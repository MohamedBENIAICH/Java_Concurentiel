import React from 'react';
import { useQuery } from 'react-query';
import { Ticket as TicketIcon, Download } from 'lucide-react';
import { format } from 'date-fns';
import { Table } from '../components/ui/Table';
import { Button } from '../components/ui/Button';
import { Card } from '../components/ui/Card';
import { getTickets } from '../api';
import { Ticket } from '../types';

export const TicketList: React.FC = () => {
  const { data: tickets = [], isLoading } = useQuery('tickets', async () => {
    const response = await getTickets();
    return response.data;
  });

  const exportToCSV = () => {
    if (tickets.length === 0) return;

    const headers = ['Transaction ID', 'Ticket No', 'Vendor', 'Event', 'Location', 'Customer', 'Price', 'Timestamp'];
    const csvData = tickets.map((ticket) => [
      ticket.transactionId,
      ticket.ticketNo,
      ticket.vendor,
      ticket.eventName,
      ticket.location,
      ticket.customer,
      ticket.ticketPrice,
      new Date(ticket.timestamp).toLocaleString(),
    ]);

    const csvContent = [
      headers.join(','),
      ...csvData.map((row) => row.join(',')),
    ].join('\n');

    const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' });
    const url = URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.setAttribute('download', `tickets_export_${new Date().toISOString().split('T')[0]}.csv`);
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  };

  const columns = [
    { header: 'Ticket No', accessor: 'ticketNo' },
    { header: 'Vendor', accessor: 'vendor' },
    { header: 'Event', accessor: 'eventName' },
    { header: 'Location', accessor: 'location' },
    { header: 'Customer', accessor: 'customer' },
    { header: 'Price', accessor: (ticket: Ticket) => `$${ticket.ticketPrice}` },
    { 
      header: 'Timestamp', 
      accessor: (ticket: Ticket) => format(new Date(ticket.timestamp), 'MMM dd, yyyy HH:mm') 
    },
  ];

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h1 className="text-2xl font-bold text-gray-900">Tickets</h1>
        <Button
          variant="outline"
          onClick={exportToCSV}
          disabled={tickets.length === 0}
        >
          <Download className="mr-2 h-5 w-5" />
          Export to CSV
        </Button>
      </div>

      <Card className="mb-4">
        <div className="flex flex-col md:flex-row md:justify-between md:items-center">
          <div>
            <h3 className="text-lg font-semibold text-gray-800 mb-2">Ticket Statistics</h3>
            <p className="text-gray-600">
              Total Tickets: <span className="font-semibold">{tickets.length}</span>
            </p>
          </div>
          <div className="mt-4 md:mt-0">
            <div className="flex flex-col md:flex-row md:items-center md:space-x-4">
              <div className="bg-green-100 text-green-800 p-3 rounded-md flex items-center">
                <div className="text-3xl font-bold">
                  ${tickets.reduce((sum, ticket) => sum + ticket.ticketPrice, 0).toFixed(2)}
                </div>
                <div className="ml-2 text-sm">
                  Total Revenue
                </div>
              </div>
              <div className="bg-blue-100 text-blue-800 p-3 rounded-md flex items-center mt-2 md:mt-0">
                <div className="text-3xl font-bold">
                  {new Set(tickets.map(ticket => ticket.customer)).size}
                </div>
                <div className="ml-2 text-sm">
                  Unique Customers
                </div>
              </div>
            </div>
          </div>
        </div>
      </Card>

      <Table
        columns={columns}
        data={tickets}
        keyExtractor={(ticket) => ticket.transactionId || 0}
        isLoading={isLoading}
        emptyMessage="No tickets found."
      />

      {tickets.length === 0 && !isLoading && (
        <div className="text-center py-12">
          <TicketIcon className="mx-auto h-12 w-12 text-gray-400" />
          <h3 className="mt-2 text-lg font-medium text-gray-900">No tickets</h3>
          <p className="mt-1 text-gray-500">No ticket transactions have been recorded yet.</p>
        </div>
      )}
    </div>
  );
};