import React, { useState, useEffect } from 'react';
import { useQuery, useMutation } from 'react-query';
import { toast } from 'react-toastify';
import { Settings as SettingsIcon, Save, RotateCcw } from 'lucide-react';
import { Card } from '../components/ui/Card';
import { Button } from '../components/ui/Button';
import { Input } from '../components/ui/Input';
import { getConfiguration, saveConfiguration, deleteAllCustomers } from '../api';
import { Configuration } from '../types';
import { useAppContext } from '../context/AppContext';

export const Settings: React.FC = () => {
  const { isSystemRunning } = useAppContext();
  const [formData, setFormData] = useState<Configuration>({
    totalTickets: 0,
    ticketReleaseRate: 0,
    customerRetrievalRate: 0,
    maxTicketCapacity: 0,
  });

  const [errors, setErrors] = useState<Record<string, string>>({});

  const { data: config, isLoading } = useQuery('configuration', async () => {
    const response = await getConfiguration();
    return response.data;
  });

  useEffect(() => {
    if (config) {
      setFormData(config);
    }
  }, [config]);

  const configMutation = useMutation(
    (data: Configuration) => saveConfiguration(data),
    {
      onSuccess: () => {
        toast.success('Configuration saved successfully');
      },
      onError: () => {
        toast.error('Failed to save configuration');
      },
    }
  );

  const resetCustomersMutation = useMutation(
    () => deleteAllCustomers(),
    {
      onSuccess: () => {
        toast.success('All customers have been deleted');
      },
      onError: () => {
        toast.error('Failed to delete customers');
      },
    }
  );

  const validate = (): boolean => {
    const newErrors: Record<string, string> = {};
    
    if (formData.totalTickets <= 0) {
      newErrors.totalTickets = 'Total tickets must be greater than 0';
    }
    
    if (formData.ticketReleaseRate <= 0) {
      newErrors.ticketReleaseRate = 'Ticket release rate must be greater than 0';
    }
    
    if (formData.customerRetrievalRate <= 0) {
      newErrors.customerRetrievalRate = 'Customer retrieval rate must be greater than 0';
    }
    
    if (formData.maxTicketCapacity <= 0) {
      newErrors.maxTicketCapacity = 'Max ticket capacity must be greater than 0';
    }
    
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    
    setFormData((prev) => ({
      ...prev,
      [name]: name === 'ticketReleaseRate' || name === 'customerRetrievalRate' 
        ? parseFloat(value) 
        : parseInt(value, 10),
    }));
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    
    if (validate()) {
      configMutation.mutate(formData);
    }
  };

  const handleResetCustomers = () => {
    if (window.confirm('Are you sure you want to delete all customers? This action cannot be undone.')) {
      resetCustomersMutation.mutate();
    }
  };

  if (isLoading) {
    return (
      <div className="flex justify-center items-center py-10">
        <div className="animate-spin rounded-full h-10 w-10 border-b-2 border-blue-900"></div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h1 className="text-2xl font-bold text-gray-900">System Settings</h1>
        <div className={`flex items-center px-3 py-1 rounded-full text-sm ${
          isSystemRunning ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'
        }`}>
          <span className={`h-2 w-2 rounded-full mr-2 ${
            isSystemRunning ? 'bg-green-500' : 'bg-red-500'
          }`}></span>
          System is {isSystemRunning ? 'Running' : 'Stopped'}
        </div>
      </div>

      <Card>
        <form onSubmit={handleSubmit}>
          <div className="mb-6">
            <h2 className="text-lg font-semibold text-gray-800 mb-4 flex items-center">
              <SettingsIcon className="mr-2 h-5 w-5" />
              System Configuration
            </h2>
            
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <Input
                label="Total Tickets"
                name="totalTickets"
                type="number"
                value={formData.totalTickets}
                onChange={handleChange}
                error={errors.totalTickets}
                disabled={isSystemRunning}
                required
              />
              
              <Input
                label="Ticket Release Rate"
                name="ticketReleaseRate"
                type="number"
                step="0.01"
                value={formData.ticketReleaseRate}
                onChange={handleChange}
                error={errors.ticketReleaseRate}
                disabled={isSystemRunning}
                required
              />
              
              <Input
                label="Customer Retrieval Rate"
                name="customerRetrievalRate"
                type="number"
                step="0.01"
                value={formData.customerRetrievalRate}
                onChange={handleChange}
                error={errors.customerRetrievalRate}
                disabled={isSystemRunning}
                required
              />
              
              <Input
                label="Max Ticket Capacity"
                name="maxTicketCapacity"
                type="number"
                value={formData.maxTicketCapacity}
                onChange={handleChange}
                error={errors.maxTicketCapacity}
                disabled={isSystemRunning}
                required
              />
            </div>
          </div>

          <div className="flex justify-between">
            <Button
              type="button"
              variant="danger"
              onClick={handleResetCustomers}
              disabled={isSystemRunning}
            >
              <RotateCcw className="mr-2 h-4 w-4" />
              Reset All Customers
            </Button>
            
            <Button
              type="submit"
              isLoading={configMutation.isLoading}
              disabled={isSystemRunning}
            >
              <Save className="mr-2 h-4 w-4" />
              Save Configuration
            </Button>
          </div>
        </form>

        {isSystemRunning && (
          <div className="mt-4 p-3 bg-yellow-50 border border-yellow-200 rounded-md text-yellow-800 text-sm">
            <p>
              <strong>Note:</strong> System configuration cannot be modified while the system is running.
              Please stop the system first to make changes.
            </p>
          </div>
        )}
      </Card>

      <Card>
        <h2 className="text-lg font-semibold text-gray-800 mb-4">Help & Information</h2>
        <div className="space-y-4 text-gray-600">
          <div>
            <h3 className="font-medium text-gray-800">Total Tickets</h3>
            <p className="text-sm">The total number of tickets available in the system.</p>
          </div>
          <div>
            <h3 className="font-medium text-gray-800">Ticket Release Rate</h3>
            <p className="text-sm">The rate at which tickets are released to vendors (tickets per second).</p>
          </div>
          <div>
            <h3 className="font-medium text-gray-800">Customer Retrieval Rate</h3>
            <p className="text-sm">The rate at which customers attempt to purchase tickets (customers per second).</p>
          </div>
          <div>
            <h3 className="font-medium text-gray-800">Max Ticket Capacity</h3>
            <p className="text-sm">The maximum number of tickets that can be processed simultaneously.</p>
          </div>
        </div>
      </Card>
    </div>
  );
};