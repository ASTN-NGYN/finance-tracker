"use client";

import CreateTransactionCard from "../components/transactions/create-transaction";
import FullTransactionsTable from "../components/transactions/full-transactions-table";


export default function TransactionsPage() {
  return (
    <main>
      <CreateTransactionCard />
      <FullTransactionsTable />
    </main>
  )
}