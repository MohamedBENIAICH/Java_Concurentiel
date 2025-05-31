import React, { useState, useEffect } from "react";
import { useQuery } from "react-query";
import { Ticket as TicketIcon, Download, Eye } from "lucide-react";
import { format } from "date-fns";
import { Table } from "../components/ui/Table";
import { Button } from "../components/ui/Button";
import { Card } from "../components/ui/Card";
import { getTickets } from "../api";
import { Ticket } from "../types";
import html2pdf from "html2pdf.js";
import { QRCodeSVG } from "qrcode.react";
import ReactDOM from "react-dom";

export const TicketList: React.FC = () => {
  const [selectedTicket, setSelectedTicket] = useState<Ticket | null>(null);
  const [showModal, setShowModal] = useState(false);
  const [showQr, setShowQr] = useState(false);

  const { data: tickets = [], isLoading } = useQuery("tickets", async () => {
    const response = await getTickets();
    return response.data;
  });

  // Show QR code after animation
  useEffect(() => {
    let timer: NodeJS.Timeout;
    if (showModal) {
      setShowQr(false);
      timer = setTimeout(() => setShowQr(true), 1000); // reduced from 5000ms to 1000ms
    }
    return () => clearTimeout(timer);
  }, [showModal, selectedTicket]);

  const exportToCSV = () => {
    if (tickets.length === 0) return;

    const headers = [
      "Transaction ID",
      "Ticket No",
      "Vendor",
      "Event",
      "Location",
      "Customer",
      "Price",
      "Timestamp",
    ];
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
      headers.join(","),
      ...csvData.map((row) => row.join(",")),
    ].join("\n");

    const blob = new Blob([csvContent], { type: "text/csv;charset=utf-8;" });
    const url = URL.createObjectURL(blob);
    const link = document.createElement("a");
    link.href = url;
    link.setAttribute(
      "download",
      `tickets_export_${new Date().toISOString().split("T")[0]}.csv`
    );
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

  const downloadTicketAsPDF = () => {
    if (!selectedTicket) return;

    // Revenir à une approche plus simple pour résoudre le problème de PDF vide
    const element = document.getElementById("ticket-pdf-content");
    if (!element) {
      console.error("Element ticket-pdf-content not found");
      return;
    }

    // Créer une copie du contenu pour le PDF
    const pdfContent = document.createElement("div");
    pdfContent.style.width = "210mm"; // A4 width
    pdfContent.style.padding = "15mm";
    pdfContent.style.backgroundColor = "white";
    pdfContent.style.fontFamily = "Arial, sans-serif";
    
    // Contenu simple mais bien formaté
    pdfContent.innerHTML = `
      <div style="text-align: center; margin-bottom: 20px;">
        <h1 style="color: #3b82f6; margin: 0; font-size: 24px;">Event Ticket</h1>
        <p style="color: #64748b; margin: 5px 0 0 0;">Ticket #${selectedTicket.ticketNo}</p>
      </div>
      
      <div style="display: flex; justify-content: center; margin: 20px 0;">
        <div id="qrcode-container" style="padding: 10px; background: white; border: 1px solid #e2e8f0; border-radius: 8px;"></div>
      </div>
      
      <table style="width: 100%; border-collapse: collapse; margin-top: 20px; border: 1px solid #e2e8f0;">
        <thead>
          <tr style="background-color: #f1f5f9;">
            <th style="text-align: left; padding: 10px; border: 1px solid #e2e8f0; color: #1e3a8a;">Field</th>
            <th style="text-align: left; padding: 10px; border: 1px solid #e2e8f0; color: #1e3a8a;">Value</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td style="padding: 10px; border: 1px solid #e2e8f0; font-weight: bold; color: #475569;">Transaction ID</td>
            <td style="padding: 10px; border: 1px solid #e2e8f0;">${selectedTicket.transactionId}</td>
          </tr>
          <tr style="background-color: #f8fafc;">
            <td style="padding: 10px; border: 1px solid #e2e8f0; font-weight: bold; color: #475569;">Event</td>
            <td style="padding: 10px; border: 1px solid #e2e8f0;">${selectedTicket.eventName}</td>
          </tr>
          <tr>
            <td style="padding: 10px; border: 1px solid #e2e8f0; font-weight: bold; color: #475569;">Location</td>
            <td style="padding: 10px; border: 1px solid #e2e8f0;">${selectedTicket.location}</td>
          </tr>
          <tr style="background-color: #f8fafc;">
            <td style="padding: 10px; border: 1px solid #e2e8f0; font-weight: bold; color: #475569;">Vendor</td>
            <td style="padding: 10px; border: 1px solid #e2e8f0;">${selectedTicket.vendor}</td>
          </tr>
          <tr>
            <td style="padding: 10px; border: 1px solid #e2e8f0; font-weight: bold; color: #475569;">Customer</td>
            <td style="padding: 10px; border: 1px solid #e2e8f0;">${selectedTicket.customer}</td>
          </tr>
          <tr style="background-color: #f8fafc;">
            <td style="padding: 10px; border: 1px solid #e2e8f0; font-weight: bold; color: #475569;">Price</td>
            <td style="padding: 10px; border: 1px solid #e2e8f0;">$${selectedTicket.ticketPrice}</td>
          </tr>
          <tr>
            <td style="padding: 10px; border: 1px solid #e2e8f0; font-weight: bold; color: #475569;">Date</td>
            <td style="padding: 10px; border: 1px solid #e2e8f0;">${format(
              new Date(selectedTicket.timestamp),
              "MMM dd, yyyy HH:mm"
            )}</td>
          </tr>
        </tbody>
      </table>
      
      <div style="margin-top: 30px; border-top: 1px solid #e2e8f0; padding-top: 15px; text-align: center;">
        <p style="color: #64748b; font-size: 12px; margin: 5px 0;">Generated on ${format(new Date(), "MMM dd, yyyy HH:mm")}</p>
        <p style="color: #64748b; font-size: 12px; margin: 5px 0;">Please present this ticket at the event entrance.</p>
      </div>
    `;
    
    // Ajouter temporairement à la page
    document.body.appendChild(pdfContent);
    
    // Ajouter le QR code
    const qrContainer = pdfContent.querySelector("#qrcode-container");
    if (qrContainer) {
      const qrData = JSON.stringify({
        transactionId: selectedTicket.transactionId,
        ticketNo: selectedTicket.ticketNo,
        eventName: selectedTicket.eventName,
        location: selectedTicket.location,
        vendor: selectedTicket.vendor,
        customer: selectedTicket.customer,
        price: selectedTicket.ticketPrice,
        date: format(new Date(selectedTicket.timestamp), "MMM dd, yyyy HH:mm"),
      });
      
      // Rendre le QR code directement
      ReactDOM.render(
        <QRCodeSVG value={qrData} size={150} level="H" />,
        qrContainer
      );
    }
    
    // Attendre un court instant pour s'assurer que le QR code est rendu
    setTimeout(() => {
      // Options pour html2pdf
      const options = {
        margin: 10,
        filename: `ticket_${selectedTicket.ticketNo}.pdf`,
        image: { type: 'jpeg', quality: 0.98 },
        html2canvas: { scale: 2, useCORS: true },
        jsPDF: { unit: 'mm', format: 'a4', orientation: 'portrait' }
      };
      
      // Générer le PDF
      html2pdf()
        .from(pdfContent)
        .set(options)
        .save()
        .then(() => {
          // Nettoyer - supprimer le conteneur après la génération du PDF
          document.body.removeChild(pdfContent);
        });
    }, 500); // Attendre 500ms pour s'assurer que le QR code est rendu
  };

  const columns = [
    { header: "Ticket No", accessor: "ticketNo" },
    { header: "Vendor", accessor: "vendor" },
    { header: "Event", accessor: "eventName" },
    { header: "Location", accessor: "location" },
    { header: "Customer", accessor: "customer" },
    { header: "Price", accessor: (ticket: Ticket) => `$${ticket.ticketPrice}` },
    {
      header: "Timestamp",
      accessor: (ticket: Ticket) =>
        format(new Date(ticket.timestamp), "MMM dd, yyyy HH:mm"),
    },
    {
      header: "Actions",
      accessor: (ticket: Ticket) => (
        <button
          onClick={() => viewTicket(ticket)}
          className="text-blue-600 hover:text-blue-800 focus:outline-none"
          title="View Ticket"
        >
          <Eye className="h-5 w-5" />
        </button>
      ),
    },
  ];

  return (
    <div className="space-y-6">
      {/* Ticket View Modal */}
      {showModal && selectedTicket && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white rounded-lg p-3 max-w-sm w-full">
            <div className="flex justify-between items-center mb-4">
              <h2 className="text-xl font-bold">Ticket Details</h2>
              <button
                onClick={closeModal}
                className="text-gray-500 hover:text-gray-700 focus:outline-none"
              >
                ×
              </button>
            </div>
            {/* Add Download Button */}
            <div className="mb-2 flex justify-end">
              <Button onClick={downloadTicketAsPDF} variant="outline" className="text-sm py-1 px-2">
                Download as PDF
              </Button>
            </div>
            {/* Add id to this container */}
            <div className="cont" id="ticket-pdf-content">
              <div className="printer-top"></div>
              <div className="paper-cont">
                <div className="printer-bottom"></div>
                <div
                  className="paper"
                  style={{
                    display: "flex",
                    flexDirection: "column",
                    justifyContent: "flex-start",
                    minHeight: "447px",
                    height: "auto",
                    overflow: "visible",
                  }}
                >
                  <div className="main-contents">
                    <div className="success-icon" style={{ width: "50px", height: "50px", fontSize: "24px", margin: "10px auto" }}>✓</div>
                    <div className="success-title" style={{ fontSize: "18px", marginBottom: "5px" }}>Event Ticket</div>
                    
                    {/* QR Code at the top, only after animation */}
                    {showQr && (
                      <div
                        style={{
                          display: "flex",
                          justifyContent: "center",
                          marginBottom: "10px",
                          marginTop: "5px"
                        }}
                      >
                        <div
                          style={{
                            background: "#fff",
                            padding: 4,
                            borderRadius: 4,
                            border: "1px solid #eee"
                          }}
                        >
                          <QRCodeSVG
                            value={JSON.stringify({
                              transactionId: selectedTicket.transactionId,
                              ticketNo: selectedTicket.ticketNo,
                              eventName: selectedTicket.eventName,
                              location: selectedTicket.location,
                              vendor: selectedTicket.vendor,
                              customer: selectedTicket.customer,
                              price: selectedTicket.ticketPrice,
                              date: format(
                                new Date(selectedTicket.timestamp),
                                "MMM dd, yyyy HH:mm"
                              ),
                            })}
                            size={90}
                            level="H"
                          />
                        </div>
                      </div>
                    )}
                    
                    <div className="success-description" style={{ marginBottom: "20px" }}>
                      <table style={{ width: "100%", borderCollapse: "collapse", marginTop: "5px", fontSize: "12px" }}>
                        <tbody>
                          <tr>
                            <td style={{ padding: "4px", borderBottom: "1px solid #eee", fontWeight: "bold", width: "35%" }}>Transaction ID:</td>
                            <td style={{ padding: "4px", borderBottom: "1px solid #eee" }}>{selectedTicket.transactionId}</td>
                          </tr>
                          <tr>
                            <td style={{ padding: "4px", borderBottom: "1px solid #eee", fontWeight: "bold" }}>Ticket No:</td>
                            <td style={{ padding: "4px", borderBottom: "1px solid #eee" }}>{selectedTicket.ticketNo}</td>
                          </tr>
                          <tr>
                            <td style={{ padding: "4px", borderBottom: "1px solid #eee", fontWeight: "bold" }}>Event:</td>
                            <td style={{ padding: "4px", borderBottom: "1px solid #eee" }}>{selectedTicket.eventName}</td>
                          </tr>
                          <tr>
                            <td style={{ padding: "4px", borderBottom: "1px solid #eee", fontWeight: "bold" }}>Location:</td>
                            <td style={{ padding: "4px", borderBottom: "1px solid #eee" }}>{selectedTicket.location}</td>
                          </tr>
                          <tr>
                            <td style={{ padding: "4px", borderBottom: "1px solid #eee", fontWeight: "bold" }}>Vendor:</td>
                            <td style={{ padding: "4px", borderBottom: "1px solid #eee" }}>{selectedTicket.vendor}</td>
                          </tr>
                          <tr>
                            <td style={{ padding: "4px", borderBottom: "1px solid #eee", fontWeight: "bold" }}>Customer:</td>
                            <td style={{ padding: "4px", borderBottom: "1px solid #eee" }}>{selectedTicket.customer}</td>
                          </tr>
                          <tr>
                            <td style={{ padding: "4px", borderBottom: "1px solid #eee", fontWeight: "bold" }}>Price:</td>
                            <td style={{ padding: "4px", borderBottom: "1px solid #eee" }}>${selectedTicket.ticketPrice}</td>
                          </tr>
                          <tr>
                            <td style={{ padding: "4px", borderBottom: "1px solid #eee", fontWeight: "bold" }}>Date:</td>
                            <td style={{ padding: "4px", borderBottom: "1px solid #eee" }}>
                              {format(new Date(selectedTicket.timestamp), "MMM dd, yyyy HH:mm")}
                            </td>
                          </tr>
                        </tbody>
                      </table>
                    </div>
                  </div>
                  {/* QR Code removed from bottom and moved to top */}
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
            <h3 className="text-lg font-semibold text-gray-800 mb-2">
              Ticket Statistics
            </h3>
            <p className="text-gray-600">
              Total Tickets:{" "}
              <span className="font-semibold">{tickets.length}</span>
            </p>
          </div>
          <div className="mt-4 md:mt-0">
            <div className="flex flex-col md:flex-row md:items-center md:space-x-4">
              <div className="bg-green-100 text-green-800 p-3 rounded-md flex items-center">
                <div className="text-3xl font-bold">
                  $
                  {tickets
                    .reduce((sum, ticket) => sum + ticket.ticketPrice, 0)
                    .toFixed(2)}
                </div>
                <div className="ml-2 text-sm">Total Revenue</div>
              </div>
              <div className="bg-blue-100 text-blue-800 p-3 rounded-md flex items-center mt-2 md:mt-0">
                <div className="text-3xl font-bold">
                  {new Set(tickets.map((ticket) => ticket.customer)).size}
                </div>
                <div className="ml-2 text-sm">Unique Customers</div>
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
          <p className="mt-1 text-gray-500">
            No ticket transactions have been recorded yet.
          </p>
        </div>
      )}
    </div>
  );
};

// CSS for the ticket printer animation
document.head.insertAdjacentHTML(
  "beforeend",
  `
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
    overflow: visible;
    height: auto;
    min-height: 467px;
  }

  .paper {
    background: #ffffff;
    font-family: 'Poppins', sans-serif;
    min-height: 447px;
    height: auto;
    position: relative;
    z-index: 2;
    margin: 0 12px;
    margin-top: -12px;
    animation: print 5000ms cubic-bezier(0.68, -0.55, 0.265, 0.9) 1;
    -moz-animation: print 5000ms cubic-bezier(0.68, -0.55, 0.265, 0.9) 1;
    width: 95%;
    padding-bottom: 30px;
  }

  .main-contents {
    margin: 0 8px;
    padding: 12px;
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
    font-size: 36px;
    height: 50px;
    background: #359d00;
    border-radius: 50%;
    width: 50px;
    height: 50px;
    margin: 10px auto;
    color: #fff;
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .success-title {
    font-size: 18px;
    text-align: center;
    color: #666;
    font-weight: bold;
    margin-bottom: 8px;
  }

  .success-description {
    font-size: 12px;
    line-height: 16px;
    color: #333;
    text-align: left;
    margin-bottom: 12px;
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
`
);
