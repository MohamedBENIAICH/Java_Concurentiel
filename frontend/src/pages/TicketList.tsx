import React, { useState } from 'react';
import { useQuery } from 'react-query';
import { Ticket as TicketIcon, Download, Eye } from 'lucide-react';
import { format } from 'date-fns';
import { Table } from '../components/ui/Table';
import { Button } from '../components/ui/Button';
import { Card } from '../components/ui/Card';
import { getTickets } from '../api';
import { Ticket } from '../types';

export const TicketList: React.FC = () => {
  const [selectedTicket, setSelectedTicket] = useState<Ticket | null>(null);
  const [showModal, setShowModal] = useState(false);

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

  const viewTicket = (ticket: Ticket) => {
    setSelectedTicket(ticket);
    setShowModal(true);
  };

  const closeModal = () => {
    setShowModal(false);
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
    {
      header: 'Actions',
      accessor: (ticket: Ticket) => (
        <button 
          onClick={() => viewTicket(ticket)}
          className="text-blue-600 hover:text-blue-800 focus:outline-none"
          title="View Ticket"
        >
          <Eye className="h-5 w-5" />
        </button>
      )
    }
  ];

  return (
    <div className="space-y-6">
      {/* Ticket View Modal */}
      {showModal && selectedTicket && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white rounded-lg p-4 max-w-md w-full">
            <div className="flex justify-between items-center mb-4">
              <h2 className="text-xl font-bold">Ticket Details</h2>
              <button 
                onClick={closeModal}
                className="text-gray-500 hover:text-gray-700 focus:outline-none"
              >
                ×
              </button>
            </div>
            
            <div className="cont">
              <div className="printer-top"></div>

              <div className="paper-cont">
                <div className="printer-bottom"></div>

                <div className="paper">
                  <div className="main-contents">
                    <div className="success-icon">✓</div>
                    <div className="success-title">
                      Event Ticket
                    </div>
                    <div className="success-description">
                      <p><strong>Transaction ID:</strong> {selectedTicket.transactionId}</p>
                      <p><strong>Ticket No:</strong> {selectedTicket.ticketNo}</p>
                      <p><strong>Event:</strong> {selectedTicket.eventName}</p>
                      <p><strong>Location:</strong> {selectedTicket.location}</p>
                      <p><strong>Vendor:</strong> {selectedTicket.vendor}</p>
                      <p><strong>Customer:</strong> {selectedTicket.customer}</p>
                      <p><strong>Price:</strong> ${selectedTicket.ticketPrice}</p>
                      <p><strong>Date:</strong> {format(new Date(selectedTicket.timestamp), 'MMM dd, yyyy HH:mm')}</p>
                    </div>
                  </div>
                  <div className="jagged-edge"></div>
                </div>
              </div>
            </div>
          </div>
        </div>
      )}

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

// CSS for the ticket printer animation
document.head.insertAdjacentHTML('beforeend', `
<style>
  .cont {
    max-width: 380px;
    margin: 20px auto;
    overflow: hidden;
  }

  .printer-top {
    z-index: 1;
    border: 6px solid #666666;
    height: 6px;
    border-bottom: 0;
    border-radius: 6px 6px 0 0;
    background: #333333;
  }

  .printer-bottom {
    z-index: 0;
    border: 6px solid #666666;
    height: 6px;
    border-top: 0;
    border-radius: 0 0 6px 6px;
    background: #333333;
  }

  .paper-cont {
    position: relative;
    overflow: hidden;
    height: 467px;
  }

  .paper {
    background: #ffffff;
    font-family: 'Poppins', sans-serif;
    height: 447px;
    position: absolute;
    z-index: 2;
    margin: 0 12px;
    margin-top: -12px;
    animation: print 5000ms cubic-bezier(0.68, -0.55, 0.265, 0.9) 1;
    -moz-animation: print 5000ms cubic-bezier(0.68, -0.55, 0.265, 0.9) 1;
    width: 95%;
  }

  .main-contents {
    margin: 0 12px;
    padding: 24px;
  }

  .jagged-edge {
    position: relative;
    height: 20px;
    width: 100%;
    margin-top: -1px;
  }

  .jagged-edge:after {
    content: "";
    display: block;
    position: absolute;
    left: 0;
    right: 0;
    height: 20px;
    background: linear-gradient(45deg,
    transparent 33.333%,
    #ffffff 33.333%,
    #ffffff 66.667%,
    transparent 66.667%),
    linear-gradient(-45deg,
            transparent 33.333%,
            #ffffff 33.333%,
            #ffffff 66.667%,
            transparent 66.667%);
    background-size: 16px 40px;
    background-position: 0 -20px;
  }

  .success-icon {
    text-align: center;
    font-size: 48px;
    height: 72px;
    background: #359d00;
    border-radius: 50%;
    width: 72px;
    height: 72px;
    margin: 16px auto;
    color: #fff;
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .success-title {
    font-size: 22px;
    text-align: center;
    color: #666;
    font-weight: bold;
    margin-bottom: 16px;
  }

  .success-description {
    font-size: 15px;
    line-height: 21px;
    color: #333;
    text-align: left;
    margin-bottom: 24px;
  }

  .success-description p {
    margin: 8px 0;
  }

  @keyframes print {
    0% {
      transform: translateY(-90%);
    }

    100% {
      transform: translateY(0%);
    }
  }

  @-webkit-keyframes print {
    0% {
      -webkit-transform: translateY(-90%);
    }

    100% {
      -webkit-transform: translateY(0%);
    }
  }

  @-moz-keyframes print {
    0% {
      -moz-transform: translateY(-90%);
    }

    100% {
      -moz-transform: translateY(0%);
    }
  }

  @-ms-keyframes print {
    0% {
      -ms-transform: translateY(-90%);
    }

    100% {
      -ms-transform: translateY(0%);
    }
  }
</style>
`);