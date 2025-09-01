'use client';

import { useEffect, useState } from "react";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { Select, SelectTrigger, SelectContent, SelectItem, SelectValue } from "@/components/ui/select";
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogFooter, } from "@/components/ui/dialog";
import { CategoryDTO, createTransaction, TransactionDTO, getCategories } from "@/app/utils/api";

export default function CreateTransactionCard() {
  const [categories, setCategories] = useState<CategoryDTO[]>([]);
  const [amount, setAmount] = useState<number | "">("");
  const [description, setDescription] = useState<string>("");
  const [date, setDate] = useState<string>(new Date().toISOString().split("T")[0]);
  const [selectedCategoryId, setSelectedCategoryId] = useState<string | undefined>(undefined);

  const [dialogOpen, setDialogOpen] = useState(false);
  const [dialogMessage, setDialogMessage] = useState("");

  useEffect(() => {
    async function fetchCategories() {
      try {
        const data = await getCategories();
        if (Array.isArray(data)) setCategories(data);
      } catch (err) {
        console.error("Failed to fetch categories:", err);
        showDialog("Failed to fetch categories.");
      }
    }
    fetchCategories();
  }, []);

  const showDialog = (message: string) => {
    setDialogMessage(message);
    setDialogOpen(true);
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!amount || !description || !date || !selectedCategoryId || selectedCategoryId === "none") {
      showDialog("Please fill out all fields.");
      return;
    }

    const transaction: TransactionDTO = {
      amount: Number(amount),
      description,
      date,
      categoryId: Number(selectedCategoryId),
    };

    try {
      await createTransaction(transaction);
      showDialog(`Transaction saved!`);

      setAmount("");
      setDescription("");
      setDate(new Date().toISOString().split("T")[0]);
      setSelectedCategoryId(undefined);
    } catch (err) {
      console.error("Failed to create transaction:", err);
      showDialog("Failed to create transaction. Please try again.");
    }
  };

  return (
    <div className="max-w-md mx-auto p-4 border rounded-md shadow-sm relative">
      <form onSubmit={handleSubmit} className="space-y-4">
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

        <Button type="submit" className="w-full bg-blue-700 hover:bg-blue-800 cursor-pointer">
          Save Transaction
        </Button>
      </form>

      <Dialog open={dialogOpen} onOpenChange={setDialogOpen}>
        <DialogContent
          className="bg-white text-black rounded-xl shadow-xl p-6 w-[90%] max-w-md text-center
                    flex flex-col justify-between min-h-[250px] md:min-h-[350px] lg:min-h-[400px] mt-[-50px]"
          style={{ top: "40%" }}
        >
          <DialogHeader>
            <DialogTitle className="text-lg font-semibold text-black">
              Notification
            </DialogTitle>
          </DialogHeader>
          <p className="my-4 text-black text-xl text-center">{dialogMessage}</p>
          <DialogFooter className="flex justify-end">
            <Button
              onClick={() => setDialogOpen(false)}
              className="bg-blue-700 hover:bg-blue-800 text-white cursor-pointer"
            >
              OK
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>
    </div>
  );
}
