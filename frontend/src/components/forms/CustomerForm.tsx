import React, { useState } from "react";
import { useMutation, useQueryClient } from "react-query";
import { toast } from "react-toastify";
import { X } from "lucide-react";
import { Button } from "../ui/Button";
import { Input } from "../ui/Input";
import { saveCustomer } from "../../api";
import { Customer } from "../../types";

interface CustomerFormProps {
  customer?: Customer | null;
  onClose: () => void;
}

export const CustomerForm: React.FC<CustomerFormProps> = ({
  customer,
  onClose,
}) => {
  const isEditing = !!customer;
  const queryClient = useQueryClient();

  const [formData, setFormData] = useState<Customer>({
    customerId: customer?.customerId || undefined,
    customerName: customer?.customerName || "",
    customerAddress: customer?.customerAddress || "",
    customerEmail: customer?.customerEmail || "",
    customerTel: customer?.customerTel || "",
    purchaseQuantity: customer?.purchaseQuantity || 0,
  });

  const [errors, setErrors] = useState<Record<string, string>>({});

  const mutation = useMutation((data: Customer) => saveCustomer(data), {
    onSuccess: () => {
      queryClient.invalidateQueries("customers");
      toast.success(
        `Customer ${isEditing ? "updated" : "created"} successfully`
      );
      onClose();
    },
    onError: () => {
      toast.error(`Failed to ${isEditing ? "update" : "create"} customer`);
    },
  });

  const validate = (): boolean => {
    const newErrors: Record<string, string> = {};

    if (!formData.customerName.trim()) {
      newErrors.customerName = "Customer name is required";
    }

    if (!formData.customerEmail.trim()) {
      newErrors.customerEmail = "Email is required";
    } else if (!/\S+@\S+\.\S+/.test(formData.customerEmail)) {
      newErrors.customerEmail = "Email is invalid";
    }

    if (formData.purchaseQuantity < 0) {
      newErrors.purchaseQuantity = "Purchase quantity cannot be negative";
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;

    setFormData((prev) => ({
      ...prev,
      [name]: name === "purchaseQuantity" ? parseInt(value, 10) : value,
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
          {isEditing ? "Modifier le client" : "Ajouter un nouveau client"}
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
            label="Nom du client"
            name="customerName"
            value={formData.customerName}
            onChange={handleChange}
            error={errors.customerName}
            required
          />

          <Input
            label="Email"
            name="customerEmail"
            type="email"
            value={formData.customerEmail}
            onChange={handleChange}
            error={errors.customerEmail}
            required
          />

          <Input
            label="Numéro de téléphone"
            name="customerTel"
            value={formData.customerTel}
            onChange={handleChange}
            error={errors.customerTel}
          />

          <Input
            label="Adresse"
            name="customerAddress"
            value={formData.customerAddress}
            onChange={handleChange}
            error={errors.customerAddress}
          />

          <Input
            label="Quantité achetée"
            name="purchaseQuantity"
            type="number"
            value={formData.purchaseQuantity}
            onChange={handleChange}
            error={errors.purchaseQuantity}
            required
          />
        </div>

        <div className="mt-6 flex justify-end space-x-3">
          <Button type="button" variant="outline" onClick={onClose}>
            Annuler
          </Button>
          <Button type="submit" isLoading={mutation.isLoading}>
            {isEditing ? "Mettre à jour le client" : "Créer le client"}
          </Button>
        </div>
      </form>
    </div>
  );
};
