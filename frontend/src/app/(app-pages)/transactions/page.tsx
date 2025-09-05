"use client";

import CreateTransactionCard from "@/app/components/transactions/create-transaction";
import FullTransactionsTable from "@/app/components/transactions/full-transactions-table";


export default function TransactionsPage() {
  return (
    <main>
      <CreateTransactionCard />
      <FullTransactionsTable />
    </main>
  )
}