import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from 'react-query';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

import { Layout } from './components/layout/Layout';
import { Dashboard } from './pages/Dashboard';
import { VendorList } from './pages/VendorList';
import { CustomerList } from './pages/CustomerList';
import { TicketList } from './pages/TicketList';
import { Settings } from './pages/Settings';
import { ConcurrencyLogs } from './pages/ConcurrencyLogs';
import { AppProvider } from './context/AppContext';

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      refetchOnWindowFocus: false,
      retry: 1,
      staleTime: 30000,
    },
  },
});

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <AppProvider>
        <BrowserRouter>
          <Layout>
            <Routes>
              <Route path="/" element={<Dashboard />} />
              <Route path="/vendors" element={<VendorList />} />
              <Route path="/customers" element={<CustomerList />} />
              <Route path="/tickets" element={<TicketList />} />
              <Route path="/settings" element={<Settings />} />
              <Route path="/concurrency-logs" element={<ConcurrencyLogs />} />
              <Route path="*" element={<Navigate to="/" replace />} />
            </Routes>
          </Layout>
        </BrowserRouter>
        <ToastContainer position="top-right" autoClose={3000} />
      </AppProvider>
    </QueryClientProvider>
  );
}

export default App;