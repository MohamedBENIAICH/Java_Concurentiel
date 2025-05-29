import React, { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from 'react-query';
import { PlusCircle, Edit, Trash, Users } from 'lucide-react';
import { toast } from 'react-toastify';
import { Table } from '../components/ui/Table';
import { Button } from '../components/ui/Button';
import { Card } from '../components/ui/Card';
import { getCustomers, deleteCustomer } from '../api';
import { Customer } from '../types';
import { CustomerForm } from '../components/forms/CustomerForm';

export const CustomerList: React.FC = () => {
  const [selectedCustomer, setSelectedCustomer] = useState<Customer | null>(null);
  const [isFormOpen, setIsFormOpen] = useState(false);
  const queryClient = useQueryClient();

  const { data: customers = [], isLoading } = useQuery('customers', async () => {
    const response = await getCustomers();
    return response.data;
  });

  const deleteMutation = useMutation(
    (customerId: number) => deleteCustomer(customerId),
    {
      onSuccess: () => {
        queryClient.invalidateQueries('customers');
        toast.success('Customer deleted successfully');
      },
      onError: () => {
        toast.error('Failed to delete customer');
      },
    }
  );

  const handleEdit = (customer: Customer) => {
    setSelectedCustomer(customer);
    setIsFormOpen(true);
  };

  const handleDelete = (customerId: number) => {
    if (window.confirm('Are you sure you want to delete this customer?')) {
      deleteMutation.mutate(customerId);
    }
  };

  const handleFormClose = () => {
    setIsFormOpen(false);
    setSelectedCustomer(null);
  };

  const columns = [
    { header: 'ID', accessor: 'customerId' },
    { header: 'Nom', accessor: 'customerName' },
    { header: 'Email', accessor: 'customerEmail' },
    { header: 'Téléphone', accessor: 'customerTel' },
    { header: 'Quantité achetée', accessor: 'purchaseQuantity' },
    {
      header: 'Actions',
      accessor: (customer: Customer) => (
        <div className="flex space-x-2">
          <Button
            variant="outline"
            size="sm"
            onClick={(e) => {
              e.stopPropagation();
              handleEdit(customer);
            }}
          >
            <Edit size={16} />
          </Button>
          <Button
            variant="danger"
            size="sm"
            onClick={(e) => {
              e.stopPropagation();
              handleDelete(customer.customerId!);
            }}
          >
            <Trash size={16} />
          </Button>
        </div>
      ),
    },
  ];

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h1 className="text-2xl font-bold text-gray-900">Clients</h1>
        <Button
          onClick={() => {
            setSelectedCustomer(null);
            setIsFormOpen(true);
          }}
        >
          <PlusCircle className="mr-2 h-5 w-5" />
          Ajouter un client
        </Button>
      </div>

      {isFormOpen && (
        <Card className="mb-6">
          <CustomerForm 
            customer={selectedCustomer}
            onClose={handleFormClose} 
          />
        </Card>
      )}

      <Table
        columns={columns}
        data={customers}
        keyExtractor={(customer) => customer.customerId || 0}
        isLoading={isLoading}
        emptyMessage="Aucun client trouvé. Ajoutez votre premier client !"
        onRowClick={handleEdit}
      />

      {customers.length === 0 && !isLoading && !isFormOpen && (
        <div className="text-center py-12">
          <Users className="mx-auto h-12 w-12 text-gray-400" />
          <h3 className="mt-2 text-lg font-medium text-gray-900">Aucun client</h3>
          <p className="mt-1 text-gray-500">Commencez par créer un nouveau client.</p>
          <div className="mt-6">
            <Button
              onClick={() => {
                setSelectedCustomer(null);
                setIsFormOpen(true);
              }}
            >
              <PlusCircle className="mr-2 h-5 w-5" />
              Ajouter un client
            </Button>
          </div>
        </div>
      )}
    </div>
  );
};