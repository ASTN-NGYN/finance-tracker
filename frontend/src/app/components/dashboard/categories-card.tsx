import { Card, CardContent, CardFooter } from "@/components/ui/card";

import { useState, useEffect } from "react";
import { getTransactionWithCategory, TransactionWithCategoryDTO } from "@/app/utils/api";
import Link from "next/link";
import { Button } from "@/components/ui/button";
import error from "next/error";

interface CategorySummary {
    name: string;
    totalAmount: number;
    transactionCount: number;
}

export function CategoriesCard() {

    const [categories, setCategories] = useState<CategorySummary[]>([]);

    useEffect(() => {
        async function fetchCategories() {
            try {
                const transactions: TransactionWithCategoryDTO[] = await getTransactionWithCategory();
                const summaryMap: Record<string, CategorySummary> = {};

                transactions.forEach((t) => {
                    const name = t.categoryName;
                    if (!summaryMap[name]) {
                        summaryMap[name] = {
                            name,
                            totalAmount: 0,
                            transactionCount: 0,
                        };
                    }
                    summaryMap[name].totalAmount += t.amount;
                    summaryMap[name].transactionCount += 1;
                });

                setCategories(Object.values(summaryMap));
            } catch (err) {
                console.error("Failed to fetch categories:", error);
            }
        }
        fetchCategories();
    }, []);


    return (
        <Card className="w-full">
            <CardContent>
                <h2 className="text-xl font-semibold mb-4">Categories</h2>
                <div className="space-y-3">
                    {categories.map((category) => (
                        <div key={category.name} className="flex justify-between items-center">
                            <div className="flex items-center gap-2">
                                <span className="font-medium italic">{category.name}</span>
                            </div>
                            <div className="text-right">
                                <div className="font-medium">${category.totalAmount.toFixed(2)}</div>
                                <div className="text-xs text-gray-500">
                                    {category.transactionCount}{" "}
                                    {category.transactionCount === 1 ? "transaction" : "transactions"}
                                </div>
                            </div>
                        </div>
                    ))}
                </div>
            </CardContent>
            <CardFooter>
                <Link href="/categories" className="w-full">
                    <Button className="w-full text-md p-5 bg-blue-700 hover:bg-blue-800 cursor-pointer">
                        Manage Categories
                    </Button>
                </Link>
            </CardFooter>
        </Card>
    );
}