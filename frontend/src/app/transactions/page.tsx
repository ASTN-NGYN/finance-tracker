"use client";

import { useEffect } from "react";
import { useAuth } from "@/app/context/AuthContext";
import { useRouter } from "next/navigation";
import CreateTransactionCard from "@/app/components/transactions/create-transaction";
import FullTransactionsTable from "@/app/components/transactions/full-transactions-table";

export default function TransactionsPage() {
  const router = useRouter();
  const { user, loading } = useAuth();

  useEffect(() => {
    if (!loading && !user) {
      router.push("/login");
    }
  }, [user, loading, router]);

  if (loading || !user) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <p className="text-lg">Loading...</p>
      </div>
    );
  }

  return (
    <main>
      <CreateTransactionCard />
      <FullTransactionsTable />
    </main>
  )
}