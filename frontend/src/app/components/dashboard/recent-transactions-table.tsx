import {
    Table, TableBody, 
    TableCaption, TableCell, 
    TableHead, TableHeader, TableRow
} from "@/components/ui/table"

export function RecentTransactionsTable() {
    const transactions = [
    { date: "2025-08-10", description: "Grocery Store", category: "Food", amount: -54.32 },
    { date: "2025-08-08", description: "Paycheck", category: "Income", amount: 2000 },
    { date: "2025-08-07", description: "Electric Bill", category: "Utilities", amount: -120 },
    { date: "2025-08-05", description: "Coffee Shop", category: "Food", amount: -4.75 },
    ];

    return (
        <div className="bg-white rounded-md shadow-md border border-gray-200">
            <Table className="">
                <TableCaption>A list of recent transactions.</TableCaption>
                <TableHeader>
                    <TableRow>
                        <TableHead>Description</TableHead>
                        <TableHead>Category</TableHead>
                        <TableHead>Date</TableHead>
                        <TableHead className="text-right">Amount</TableHead>
                    </TableRow>
                </TableHeader>
                <TableBody>
                    {transactions.map((tx, i) => (
                        <TableRow key={i}>
                            <TableCell>{tx.description}</TableCell>
                            <TableCell>{tx.category}</TableCell>
                            <TableCell>{tx.date}</TableCell>
                            <TableCell
                                className={`text-right font-medium ${
                                tx.amount < 0 ? "text-red-500" : "text-green-500"
                                }`}
                            >
                                {tx.amount < 0
                                ? `-$${Math.abs(tx.amount).toFixed(2)}`
                                : `$${tx.amount.toFixed(2)}`}
                            </TableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
        </div>
    )
}