import React, { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from 'react-query';
import { PlusCircle, Edit, Trash, ShoppingBag } from 'lucide-react';
import { toast } from 'react-toastify';
import { Table } from '../components/ui/Table';
import { Button } from '../components/ui/Button';
import { Card } from '../components/ui/Card';
import { getVendors, deleteVendor } from '../api';
import { Vendor } from '../types';
import { VendorForm } from '../components/forms/VendorForm';

export const VendorList: React.FC = () => {
  const [selectedVendor, setSelectedVendor] = useState<Vendor | null>(null);
  const [isFormOpen, setIsFormOpen] = useState(false);
  const queryClient = useQueryClient();

  const { data: vendors = [], isLoading } = useQuery('vendors', async () => {
    const response = await getVendors();
    return response.data;
  });

  const deleteMutation = useMutation(
    (vendorId: number) => deleteVendor(vendorId),
    {
      onSuccess: () => {
        queryClient.invalidateQueries('vendors');
        toast.success('Vendor deleted successfully');
      },
      onError: () => {
        toast.error('Failed to delete vendor');
      },
    }
  );

  const handleEdit = (vendor: Vendor) => {
    setSelectedVendor(vendor);
    setIsFormOpen(true);
  };

  const handleDelete = (vendorId: number) => {
    if (window.confirm('Are you sure you want to delete this vendor?')) {
      deleteMutation.mutate(vendorId);
    }
  };

  const handleFormClose = () => {
    setIsFormOpen(false);
    setSelectedVendor(null);
  };

  const columns = [
    { header: 'ID', accessor: 'vendorId' },
    { header: 'Nom', accessor: 'vendorName' },
    { header: 'Événement', accessor: 'eventName' },
    { header: 'Lieu', accessor: 'location' },
    { header: 'Prix du billet', accessor: (vendor: Vendor) => `${vendor.ticketPrice} €` },
    { header: 'Contact', accessor: (vendor: Vendor) => vendor.email },
    {
      header: 'Actions',
      accessor: (vendor: Vendor) => (
        <div className="flex space-x-2">
          <Button
            variant="outline"
            size="sm"
            onClick={(e) => {
              e.stopPropagation();
              handleEdit(vendor);
            }}
          >
            <Edit size={16} />
          </Button>
          <Button
            variant="danger"
            size="sm"
            onClick={(e) => {
              e.stopPropagation();
              handleDelete(vendor.vendorId!);
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
        <h1 className="text-2xl font-bold text-gray-900">Vendeurs</h1>
        <Button
          onClick={() => {
            setSelectedVendor(null);
            setIsFormOpen(true);
          }}
        >
          <PlusCircle className="mr-2 h-5 w-5" />
          Ajouter un vendeur
        </Button>
      </div>

      {isFormOpen && (
        <Card className="mb-6">
          <VendorForm 
            vendor={selectedVendor}
            onClose={handleFormClose} 
          />
        </Card>
      )}

      <Table
        columns={columns}
        data={vendors}
        keyExtractor={(vendor) => vendor.vendorId || 0}
        isLoading={isLoading}
        emptyMessage="Aucun vendeur trouvé. Ajoutez votre premier vendeur !"
        onRowClick={handleEdit}
      />

      {vendors.length === 0 && !isLoading && !isFormOpen && (
        <div className="text-center py-12">
          <ShoppingBag className="mx-auto h-12 w-12 text-gray-400" />
          <h3 className="mt-2 text-lg font-medium text-gray-900">Aucun vendeur</h3>
          <p className="mt-1 text-gray-500">Commencez par créer un nouveau vendeur.</p>
          <div className="mt-6">
            <Button
              onClick={() => {
                setSelectedVendor(null);
                setIsFormOpen(true);
              }}
            >
              <PlusCircle className="mr-2 h-5 w-5" />
              Ajouter un vendeur
            </Button>
          </div>
        </div>
      )}
    </div>
  );
};