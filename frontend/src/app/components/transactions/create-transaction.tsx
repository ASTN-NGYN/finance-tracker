'use client';

import { useEffect, useState } from "react";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import {
  Select,
  SelectTrigger,
  SelectContent,
  SelectItem,
  SelectValue,
} from "@/components/ui/select";
import { CategoryDTO, createTransaction, TransactionDTO, getCategories } from "@/app/utils/api";

export default function CreateTransactionCard() {
  const [categories, setCategories] = useState<CategoryDTO[]>([]);
  const [amount, setAmount] = useState<number | "">("");
  const [description, setDescription] = useState<string>("");
  const [date, setDate] = useState<string>(new Date().toISOString().split("T")[0]);
  const [selectedCategoryId, setSelectedCategoryId] = useState<string | undefined>(undefined);

  useEffect(() => {
    async function fetchCategories() {
      try {
        const data = await getCategories();
        console.log("Fetched categories:", data);
        if (Array.isArray(data)) {
          setCategories(data);
        } else {
          console.error("Expected an array but got:", data);
        }
      } catch (err) {
        console.error("Failed to fetch categories:", err);
      }
    }

    fetchCategories();
  }, []);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!amount || !description || !date || !selectedCategoryId || selectedCategoryId === "none") {
      alert("Please fill out all fields.");
      return;
    }

    const transaction: TransactionDTO = {
      amount: Number(amount),
      description,
      date,
      categoryId: Number(selectedCategoryId),
    };

    try {
      const res = await createTransaction(transaction);
      console.log("Transaction created:", res);
      // Reset form
      setAmount("");
      setDescription("");
      setDate(new Date().toISOString().split("T")[0]);
      setSelectedCategoryId(undefined);
    } catch (err) {
      console.error("Failed to create transaction:", err);
    }
  };

  return (
    <div className="max-w-md mx-auto p-4 border rounded-md shadow-sm">
      <form onSubmit={handleSubmit} className="space-y-4">
        {/* Amount */}
        <div>
          <label htmlFor="amount">Amount</label>
          <Input
            type="number"
            id="amount"
            value={amount}
            onChange={(e) => setAmount(e.target.valueAsNumber || "")}
            placeholder="Enter transaction amount"
            required
          />
        </div>

        {/* Description */}
        <div>
          <label htmlFor="description">Description</label>
          <Input
            type="text"
            id="description"
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            placeholder="Enter transaction description"
            required
          />
        </div>

        {/* Date */}
        <div>
          <label htmlFor="date">Date</label>
          <Input
            type="date"
            id="date"
            value={date}
            onChange={(e) => setDate(e.target.value)}
            required
          />
        </div>

        {/* Category selector */}
        <div>
          <label htmlFor="category">Category</label>
          <Select
            value={selectedCategoryId}
            onValueChange={(value: string) => setSelectedCategoryId(value)}
          >
            <SelectTrigger id="category" className="cursor-pointer">
              <SelectValue placeholder="Select a category" />
            </SelectTrigger>
            <SelectContent>
              {categories.length > 0 ? (
                categories.map((cat) => (
                  <SelectItem key={cat.id} value={cat.id.toString()} className="cursor-pointer">
                    {cat.name}
                  </SelectItem>
                ))
              ) : (
                <SelectItem value="0" disabled>
                  No categories available
                </SelectItem>
              )}
            </SelectContent>
          </Select>
        </div>

        {/* Submit button */}
        <Button type="submit" className="w-full bg-blue-700 hover:bg-blue-800 cursor-pointer">
          Save Transaction
        </Button>
      </form>
    </div>
  );
}
