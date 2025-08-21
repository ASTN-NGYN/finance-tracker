import { Card, CardHeader, CardTitle, CardContent, CardAction, CardFooter } from "@/components/ui/card";
import { LucideIcon } from "lucide-react";

import { useState, useEffect } from "react";
import { getTransactionWithCategory, TransactionWithCategoryDTO } from "@/app/utils/api";
import Link from "next/link";
import { Button } from "@/components/ui/button";

const categoriesData = [
  { name: "Food", color: "bg-red-500", totalAmount: 98.3, transactionCount: 2 },
  { name: "Transportation", color: "bg-blue-500", totalAmount: 45.2, transactionCount: 1 },
  { name: "Entertainment", color: "bg-purple-500", totalAmount: 0, transactionCount: 0 },
  { name: "Income", color: "bg-green-500", totalAmount: 4000, transactionCount: 2 },
];

export function CategoriesCard() {
  return (
    <Card className="w-full">
      <CardContent>
        <h2 className="text-xl font-semibold mb-4">Categories</h2>
        <div className="space-y-3">
          {categoriesData.map((category) => (
            <div
              key={category.name}
              className="flex justify-between items-center"
            >
              <div className="flex items-center gap-2">
                <span className={`w-3 h-3 rounded-full ${category.color}`}></span>
                <span>{category.name}</span>
              </div>
              <div className="text-right">
                <div className="font-medium">${category.totalAmount.toFixed(2)}</div>
                <div className="text-xs text-gray-500">
                  {category.transactionCount} {category.transactionCount === 1 ? "transaction" : "transactions"}
                </div>
              </div>
            </div>
          ))}
        </div>
      </CardContent>
      <CardFooter>
        <Link href="categories" className="w-full">
            <Button className="w-full bg-blue-700 hover:bg-blue-800 cursor-pointer">Manage Categories</Button>
        </Link>
      </CardFooter>
    </Card>
  );
}