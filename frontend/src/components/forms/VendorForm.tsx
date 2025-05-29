import React, { useState } from 'react';
import { useMutation, useQueryClient } from 'react-query';
import { toast } from 'react-toastify';
import { X } from 'lucide-react';
import { Button } from '../ui/Button';
import { Input } from '../ui/Input';
import { saveVendor } from '../../api';
import { Vendor } from '../../types';

interface VendorFormProps {
  vendor?: Vendor | null;
  onClose: () => void;
}

export const VendorForm: React.FC<VendorFormProps> = ({ vendor, onClose }) => {
  const isEditing = !!vendor;
  const queryClient = useQueryClient();
  
  const [formData, setFormData] = useState<Vendor>({
    vendorId: vendor?.vendorId || undefined,
    vendorName: vendor?.vendorName || '',
    address: vendor?.address || '',
    email: vendor?.email || '',
    telNo: vendor?.telNo || '',
    eventName: vendor?.eventName || '',
    location: vendor?.location || '',
    ticketPrice: vendor?.ticketPrice || 0,
    ticketsPerRelease: vendor?.ticketsPerRelease || 0,
  });

  const [errors, setErrors] = useState<Record<string, string>>({});

  const mutation = useMutation(
    (data: Vendor) => saveVendor(data),
    {
      onSuccess: () => {
        queryClient.invalidateQueries('vendors');
        toast.success(`Vendor ${isEditing ? 'updated' : 'created'} successfully`);
        onClose();
      },
      onError: () => {
        toast.error(`Failed to ${isEditing ? 'update' : 'create'} vendor`);
      },
    }
  );

  const validate = (): boolean => {
    const newErrors: Record<string, string> = {};
    
    if (!formData.vendorName.trim()) {
      newErrors.vendorName = 'Vendor name is required';
    }
    
    if (!formData.email.trim()) {
      newErrors.email = 'Email is required';
    } else if (!/\S+@\S+\.\S+/.test(formData.email)) {
      newErrors.email = 'Email is invalid';
    }
    
    if (!formData.eventName.trim()) {
      newErrors.eventName = 'Event name is required';
    }
    
    if (!formData.location.trim()) {
      newErrors.location = 'Location is required';
    }
    
    if (formData.ticketPrice <= 0) {
      newErrors.ticketPrice = 'Ticket price must be greater than 0';
    }
    
    if (formData.ticketsPerRelease <= 0) {
      newErrors.ticketsPerRelease = 'Tickets per release must be greater than 0';
    }
    
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    
    setFormData((prev) => ({
      ...prev,
      [name]: name === 'ticketPrice' || name === 'ticketsPerRelease' 
        ? parseFloat(value) 
        : value,
    }));
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    
    if (validate()) {
      mutation.mutate(formData);
    }
  };

  return (
    <div>
      <div className="flex justify-between items-center mb-4">
        <h2 className="text-xl font-semibold text-gray-800">
          {isEditing ? 'Modifier le vendeur' : 'Ajouter un nouveau vendeur'}
        </h2>
        <button
          type="button"
          onClick={onClose}
          className="text-gray-500 hover:text-gray-700"
        >
          <X size={20} />
        </button>
      </div>

      <form onSubmit={handleSubmit}>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <Input
            label="Nom du vendeur"
            name="vendorName"
            value={formData.vendorName}
            onChange={handleChange}
            error={errors.vendorName}
            required
          />

          <Input
            label="Email"
            name="email"
            type="email"
            value={formData.email}
            onChange={handleChange}
            error={errors.email}
            required
          />

          <Input
            label="Numéro de téléphone"
            name="telNo"
            value={formData.telNo}
            onChange={handleChange}
            error={errors.telNo}
          />

          <Input
            label="Adresse"
            name="address"
            value={formData.address}
            onChange={handleChange}
            error={errors.address}
          />

          <Input
            label="Nom de l'événement"
            name="eventName"
            value={formData.eventName}
            onChange={handleChange}
            error={errors.eventName}
            required
          />

          <Input
            label="Lieu"
            name="location"
            value={formData.location}
            onChange={handleChange}
            error={errors.location}
            required
          />

          <Input
            label="Prix du billet"
            name="ticketPrice"
            type="number"
            value={formData.ticketPrice}
            onChange={handleChange}
            error={errors.ticketPrice}
            required
          />

          <Input
            label="Billets par mise en vente"
            name="ticketsPerRelease"
            type="number"
            value={formData.ticketsPerRelease}
            onChange={handleChange}
            error={errors.ticketsPerRelease}
            required
          />
        </div>

        <div className="mt-6 flex justify-end space-x-3">
          <Button
            type="button"
            variant="outline"
            onClick={onClose}
          >
            Annuler
          </Button>
          <Button
            type="submit"
            isLoading={mutation.isLoading}
          >
            {isEditing ? 'Mettre à jour le vendeur' : 'Créer le vendeur'}
          </Button>
        </div>
      </form>
    </div>
  );
};