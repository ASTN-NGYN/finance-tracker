"use client";

import { useEffect, useState } from "react";
import { useAuth } from "@/app/context/AuthContext";
import { useRouter } from "next/navigation";
import { auth } from "@/lib/firebase";

import { DollarSign, TrendingDown, BarChart3, PiggyBank } from 'lucide-react';
import { MetricCard } from '../components/dashboard/metric-card';
import { RecentTransactionsTable } from '../components/dashboard/recent-transactions-table';
import { CategoriesCard } from "../components/dashboard/categories-card";
import { getTotalExpenses, getTotalIncome, getTotalSavings } from "../utils/api";

export default function Dashboard() {
  const router = useRouter();
  const { user, loading } = useAuth();

  const [totalIncome, setTotalIncome] = useState<number>(0);
  const [totalExpenses, setTotalExpenses] = useState<number>(0);
  const [totalSavings, setTotalSavings] = useState<number>(0);

  const MONTHLY_BUDGET = 1000;

  // Redirect if not logged in
  useEffect(() => {
    if (!loading && !user) {
      router.push("/login");
    }
  }, [user, loading, router]);

  // Fetch totals after authentication
  useEffect(() => {
    if (!user) return;

    async function fetchTotals() {
      const [income, expenses, savings] = await Promise.all([
        getTotalIncome(),
        getTotalExpenses(),
        getTotalSavings(),
      ]);

      setTotalIncome(income);
      setTotalExpenses(expenses);
      setTotalSavings(savings);
    }

    fetchTotals();
  }, [user]);

  if (loading || !user) return <p className="text-center mt-10">Loading...</p>;

  const monthlyBudgetLeft = MONTHLY_BUDGET - totalExpenses;

  return (
    <main className="p-6 space-y-6">
      {/* Logout Button */}
      <div className="flex justify-end">
        <button
          onClick={() => auth.signOut()}
          className="px-4 py-2 bg-red-500 text-white rounded-md hover:bg-red-600"
        >
          Logout
        </button>
      </div>

      {/* Metric Cards */}
      <div className="flex flex-col gap-4">
        <MetricCard
          title="Total Income"
          value={`$${totalIncome.toFixed(2)}`}
          icon={DollarSign}
          color="#10b981"
        />
        <MetricCard
          title="Total Expenses"
          value={`$${totalExpenses.toFixed(2)}`}
          icon={TrendingDown}
          color="#ff0000"
        />
        <MetricCard
          title="Monthly Budget Left"
          value={`$${monthlyBudgetLeft.toFixed(2)} / ${MONTHLY_BUDGET.toFixed(2)}`}
          icon={BarChart3}
          color="#197dff"
        />
        <MetricCard
          title="Total Savings"
          value={`$${totalSavings.toFixed(2)}`}
          icon={PiggyBank}
          color="#ff00aa"
        />
      </div>

      <RecentTransactionsTable />
      <CategoriesCard />
    </main>
  );
}
