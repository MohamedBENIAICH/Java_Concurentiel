import React from "react";
import { useQuery } from "react-query";
import {
  Users,
  ShoppingBag,
  Ticket,
  Settings,
  RefreshCw,
  Activity,
} from "lucide-react";
import { Card, StatCard } from "../components/ui/Card";
import { Button } from "../components/ui/Button";
import { useNavigate } from "react-router-dom";
import { getVendors, getCustomers, getTickets, getConfiguration } from "../api";
import { useAppContext } from "../context/AppContext";
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer,
  PieChart,
  Pie,
  Cell,
} from "recharts";

const COLORS = ["#0088FE", "#00C49F", "#FFBB28", "#FF8042", "#8884d8"];

export const Dashboard: React.FC = () => {
  const { isSystemRunning } = useAppContext();

  const { data: vendors = [] } = useQuery("vendors", async () => {
    const response = await getVendors();
    return response.data;
  });

  const { data: customers = [] } = useQuery("customers", async () => {
    const response = await getCustomers();
    return response.data;
  });

  const { data: tickets = [] } = useQuery(
    "tickets",
    async () => {
      const response = await getTickets();
      return response.data;
    },
    {
      refetchInterval: 2000, // Refetch every 2 seconds
    }
  );

  const { data: config } = useQuery("configuration", async () => {
    const response = await getConfiguration();
    return response.data;
  });

  // Create vendor ticket data for chart
  const vendorTicketData = React.useMemo(() => {
    const vendorMap = new Map();

    tickets.forEach((ticket) => {
      const vendor = ticket.vendor;
      vendorMap.set(vendor, (vendorMap.get(vendor) || 0) + 1);
    });

    return Array.from(vendorMap.entries()).map(([name, value]) => ({
      name,
      value,
    }));
  }, [tickets]);

  // Create revenue data for chart
  const revenueData = React.useMemo(() => {
    // Group by date and calculate revenue
    const dateMap = new Map();

    tickets.forEach((ticket) => {
      const date = new Date(ticket.timestamp).toLocaleDateString();
      const revenue = ticket.ticketPrice;
      dateMap.set(date, (dateMap.get(date) || 0) + revenue);
    });

    return Array.from(dateMap.entries())
      .map(([date, revenue]) => ({
        date,
        revenue,
      }))
      .sort((a, b) => new Date(a.date).getTime() - new Date(b.date).getTime())
      .slice(-7); // Get the last 7 days
  }, [tickets]);

  const navigate = useNavigate();
  return (
    <div className="space-y-6">
      <div className="grid grid-cols-1 gap-6 sm:grid-cols-2 lg:grid-cols-4">
        <StatCard
          title="Nombre total de vendeurs"
          value={vendors.length}
          icon={<ShoppingBag size={24} />}
          className="border-l-4 border-blue-500"
        />
        <StatCard
          title="Nombre total de clients"
          value={customers.length}
          icon={<Users size={24} />}
          className="border-l-4 border-teal-500"
        />
        <StatCard
          title="Nombre total de billets vendus"
          value={tickets.length}
          icon={<Ticket size={24} />}
          className="border-l-4 border-purple-500"
        />
        <StatCard
          title="Statut du système"
          value={isSystemRunning ? "En cours d'exécution" : "Arrêté"}
          icon={<Activity size={24} />}
          className={`border-l-4 ${
            isSystemRunning ? "border-green-500" : "border-red-500"
          }`}
        />
      </div>

      <div className="grid grid-cols-1 gap-6 lg:grid-cols-2">
        <Card title="Aperçu des revenus" className="col-span-1">
          <div className="h-80">
            <ResponsiveContainer width="100%" height="100%">
              <BarChart
                data={revenueData}
                margin={{ top: 20, right: 30, left: 20, bottom: 5 }}
              >
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="date" />
                <YAxis />
                <Tooltip formatter={(value) => [`${value} €`, "Revenu"]} />
                <Legend formatter={() => "Revenu"} />
                <Bar dataKey="revenue" fill="#1E3A8A" name="Revenu" />
              </BarChart>
            </ResponsiveContainer>
          </div>
        </Card>

        <Card title="Billets par vendeur" className="col-span-1">
          <div className="flex flex-col space-y-2">
            <div className="self-end mb-2">
              <Button variant="outline" onClick={() => navigate('/concurrency-logs')}>
                Visualiser
              </Button>
            </div>
            <div className="h-80">
              <ResponsiveContainer width="100%" height="100%">
                <PieChart>
                  <Pie
                    data={vendorTicketData}
                    cx="50%"
                    cy="50%"
                    labelLine={false}
                    outerRadius={80}
                    fill="#8884d8"
                    dataKey="value"
                    label={({ name, percent }) =>
                      `${name} : ${(percent * 100).toFixed(0)}%`
                    }
                  >
                    {vendorTicketData.map((entry, index) => (
                      <Cell
                        key={`cell-${index}`}
                        fill={COLORS[index % COLORS.length]}
                      />
                    ))}
                  </Pie>
                  <Tooltip formatter={(value) => [`${value} billets`, "Nombre"]} />
                  <Legend formatter={() => "Nombre de billets"} />
                </PieChart>
              </ResponsiveContainer>
            </div>
          </div>
        </Card>

      <div className="grid grid-cols-1 gap-6 lg:grid-cols-2">
        <Card title="Configuration du système" className="col-span-1">
          {config ? (
            <div className="space-y-4">
              <div className="flex justify-between">
                <span className="font-medium text-gray-700">
                  Nombre total de billets :
                </span>
                <span className="text-gray-900">{config.totalTickets}</span>
              </div>
              <div className="flex justify-between">
                <span className="font-medium text-gray-700">
                  Taux de distribution des billets :
                </span>
                <span className="text-gray-900">
                  {config.ticketReleaseRate}
                </span>
              </div>
              <div className="flex justify-between">
                <span className="font-medium text-gray-700">
                  Taux de récupération des clients :
                </span>
                <span className="text-gray-900">
                  {config.customerRetrievalRate}
                </span>
              </div>
              <div className="flex justify-between">
                <span className="font-medium text-gray-700">
                  Capacité maximale de billets :
                </span>
                <span className="text-gray-900">
                  {config.maxTicketCapacity}
                </span>
              </div>
              <div className="pt-2">
                <Button
                  variant="outline"
                  className="w-full text-blue-900 border-blue-900 hover:bg-blue-50"
                  onClick={() => (window.location.href = "/settings")}
                >
                  <Settings className="mr-2 h-4 w-4" />
                  Gérer la configuration
                </Button>
              </div>
            </div>
          ) : (
            <div className="text-center py-4">
              <RefreshCw className="h-8 w-8 text-gray-400 mx-auto mb-2 animate-spin" />
              <p className="text-gray-500">Chargement de la configuration…</p>
            </div>
          )}
        </Card>

        <Card title="Dernières transactions" className="col-span-1">
          {tickets.length > 0 ? (
            <div className="divide-y divide-gray-200">
              {tickets.slice(0, 5).map((ticket) => (
                <div
                  key={ticket.transactionId}
                  className="py-3 flex justify-between"
                >
                  <div>
                    <p className="text-sm font-medium text-gray-900">
                      {ticket.customer} - {ticket.eventName}
                    </p>
                    <p className="text-sm text-gray-500">
                      {new Date(ticket.timestamp).toLocaleString()}
                    </p>
                  </div>
                  <div className="text-sm font-medium text-gray-900">
                    {ticket.ticketPrice} €
                  </div>
                </div>
              ))}
              <div className="pt-3">
                <Button
                  variant="outline"
                  className="w-full text-blue-900 border-blue-900 hover:bg-blue-50"
                  onClick={() => (window.location.href = "/tickets")}
                >
                  <Ticket className="mr-2 h-4 w-4" />
                  Voir tous les billets
                </Button>
              </div>
            </div>
          ) : (
            <div className="text-center py-4">
              <p className="text-gray-500">Aucune transaction trouvée</p>
            </div>
          )}
        </Card>
      </div>
    </div>
  </div>
  );
};
