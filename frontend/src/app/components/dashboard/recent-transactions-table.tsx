import {
    Table, TableBody, 
    TableCaption, TableCell, 
    TableHead, TableHeader, TableRow
} from "@/components/ui/table"

import { useState, useEffect } from "react";
import { getTransactionWithCategory, TransactionWithCategoryDTO } from "@/app/utils/api";
import Link from "next/link";

export function RecentTransactionsTable() {

    const [transactions, setTransactions] = useState<TransactionWithCategoryDTO[]>([]);

    useEffect(() => {
        async function fetchTransactions() {
            const data = await getTransactionWithCategory();
            setTransactions(data);
        }
        fetchTransactions();
    }, []);

    return (
        <div className="bg-white rounded-md shadow-md border border-gray-200">
            <Table className="">
                <TableHeader>
                    <TableRow>
                        <TableHead>Description</TableHead>
                        <TableHead>Category</TableHead>
                        <TableHead>Date</TableHead>
                        <TableHead className="text-right">Amount</TableHead>
                    </TableRow>
                </TableHeader>
                <TableBody>
                    {transactions
                        .sort((a, b) => new Date(b.date).getTime() - new Date(a.date).getTime())
                        .slice(0, 10)
                        .map((transaction) => (
                        <TableRow key={transaction.id}>
                            <TableCell>{transaction.description}</TableCell>
                            <TableCell>{transaction.categoryName}</TableCell>
                            <TableCell>{transaction.date}</TableCell>
                            <TableCell
                            className={`text-right font-medium ${
                                transaction.type === "EXPENSE" ? "text-red-500" : "text-green-500"
                            }`}
                            >
                            {transaction.type === "EXPENSE"
                                ? `-$${transaction.amount.toFixed(2)}`
                                : `$${transaction.amount.toFixed(2)}`}
                            </TableCell>
                        </TableRow>
                        ))}
                </TableBody>
            </Table>
            <div className="mt-4 text-center">
                <button className="text-blue-600 hover:text-blue-800 text-md font-medium mb-4">
                    <Link href={'/transactions'}>View All Transactions →</Link>
                </button>
            </div>
        </div>
    )
}