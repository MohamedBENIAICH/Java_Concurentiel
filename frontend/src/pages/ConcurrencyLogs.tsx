import React from "react";
import { Card } from "../components/ui/Card";
//import { Button } from "../components/ui/Button";
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer, LineChart, Line } from "recharts";

import { useQuery } from "react-query";

import { getTickets, getCustomers } from "../api";
import { Ticket } from "../types";

const fetchTickets = async () => {
  const response = await getTickets();
  return response.data as Ticket[];
};

export const ConcurrencyLogs: React.FC = () => {
  const { data: tickets = [], isLoading, error } = useQuery("tickets", fetchTickets, {
    refetchInterval: 2000,
  });
  const { data: customers = [] } = useQuery("customers", async () => {
    const response = await getCustomers();
    return response.data;
  });

  // Mapping customerId -> customerName
  const customerNames = React.useMemo(() => {
    const map: Record<number, string> = {};
    customers.forEach((c: any) => {
      if (c.customerId) map[c.customerId] = c.customerName;
    });
    return map;
  }, [customers]);

  // Calculer les stats dynamiquement à partir des tickets
  const stats = React.useMemo(() => {
    // Grouper les tickets par minute
    const map: { [minute: string]: { concurrents: Set<string>; tickets: number } } = {};
    tickets.forEach((ticket: Ticket) => {
      const date = new Date(ticket.timestamp);
      // Format: HH:mm (ou YYYY-MM-DD HH:mm si plusieurs jours)
      const minute = date.toLocaleDateString() + ' ' + date.getHours().toString().padStart(2, '0') + ':' + date.getMinutes().toString().padStart(2, '0');
      if (!map[minute]) {
        map[minute] = { concurrents: new Set(), tickets: 0 };
      }
      map[minute].concurrents.add(ticket.vendor);
      map[minute].tickets++;
    });
    // Transformer en tableau pour les charts
    return Object.entries(map)
      .sort(([a], [b]) => a.localeCompare(b))
      .map(([minute, { concurrents, tickets }]) => ({
        minute,
        concurrents: concurrents.size,
        tickets,
      }));
  }, [tickets]);
  const isStatsLoading = isLoading;
  const statsError = error;

  // Grouper les tickets par vendeur
  const ticketsByVendor: { [vendor: string]: Ticket[] } = React.useMemo(() => {
    const map: { [vendor: string]: Ticket[] } = {};
    tickets.forEach((ticket: Ticket) => {
      if (!map[ticket.vendor]) map[ticket.vendor] = [];
      map[ticket.vendor].push(ticket);
    });
    return map;
  }, [tickets]);

  return (
    <div className="p-4 space-y-6">
      <Card title="Tickets émis (par vendeur)">
        {isLoading ? (
          <div className="text-center py-4 text-gray-500">Chargement des tickets…</div>
        ) : error ? (
          <div className="text-center py-4 text-red-500">Erreur lors du chargement des tickets</div>
        ) : (
          <div className="overflow-x-auto grid grid-cols-1 md:grid-cols-2 gap-6">
            {Object.keys(ticketsByVendor).length === 0 && (
              <div className="col-span-2 text-center text-gray-400">Aucun ticket à afficher</div>
            )}
            {Object.entries(ticketsByVendor).map(([vendor, vendorTickets]) => (
              <div key={vendor} className="border rounded-lg shadow bg-white">
                <div className="bg-blue-50 px-4 py-2 font-bold text-blue-900 rounded-t-lg">{vendor}</div>
                <table className="min-w-full text-sm text-left">
                  <thead>
                    <tr className="bg-blue-100">
                      <th className="px-3 py-2">Heure</th>
                      <th className="px-3 py-2">Ticket Numéro</th>
                      <th className="px-3 py-2">Client</th>
                      <th className="px-3 py-2">Prix</th>
                      <th className="px-3 py-2">Statut</th>
                    </tr>
                  </thead>
                  <tbody>
                    {vendorTickets.map((ticket: Ticket, i: number) => (
                      <tr key={i}>
                        <td className="px-3 py-1 font-mono">{new Date(ticket.timestamp).toLocaleTimeString()}</td>
                        <td className="px-3 py-1">{ticket.ticketNo}</td>
                        <td className="px-3 py-1">{ticket.customerId ? customerNames[ticket.customerId] || "—" : "—"}</td>
                        <td className="px-3 py-1">{ticket.ticketPrice} €</td>
                        <td className="px-3 py-1 font-bold text-green-600">Émis</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            ))}
          </div>
        )}
      </Card>
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        <Card title="Nombre d'accès concurrents (par minute)">
          {isStatsLoading ? (
            <div className="text-center py-4 text-gray-500">Chargement des statistiques…</div>
          ) : statsError ? (
            <div className="text-center py-4 text-red-500">Erreur lors du chargement des statistiques</div>
          ) : (
            <>
              <ResponsiveContainer width="100%" height={240}>
                <BarChart data={stats}>
                  <CartesianGrid strokeDasharray="3 3" />
                  <XAxis dataKey="minute" />
                  <YAxis />
                  <Tooltip contentStyle={{ fontFamily: 'inherit' }} formatter={(value: any, name: string) => [value, name]} labelFormatter={label => `Minute : ${label}`} />
                  <Bar dataKey="concurrents" fill="#3b82f6" name="Tickets en concurrence" />
                  <Bar dataKey="tickets" fill="#16a34a" name="Tickets délivrés" />
                  <Legend />
                </BarChart>
              </ResponsiveContainer>
            </>
          )}
        </Card>
        <Card title="Charge concurrente (courbe)">
          {isStatsLoading ? (
            <div className="text-center py-4 text-gray-500">Chargement des statistiques…</div>
          ) : statsError ? (
            <div className="text-center py-4 text-red-500">Erreur lors du chargement des statistiques</div>
          ) : (
            <>
              <ResponsiveContainer width="100%" height={240}>
                <LineChart data={stats}>
                  <CartesianGrid strokeDasharray="3 3" />
                  <XAxis dataKey="minute" />
                  <YAxis />
                  <Tooltip contentStyle={{ fontFamily: 'inherit' }} formatter={(value: any, name: string) => [value, name]} labelFormatter={label => `Minute : ${label}`} />
                  <Line type="monotone" dataKey="concurrents" stroke="#f59e42" name="Tickets en concurrence" />
                  <Line type="monotone" dataKey="tickets" stroke="#1d4ed8" name="Tickets délivrés" />
                  <Legend />
                </LineChart>
              </ResponsiveContainer>
            </>
          )}
        </Card>
      </div>
    </div>
  );
};

export default ConcurrencyLogs;
